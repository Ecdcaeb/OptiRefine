package mods.Hileb.optirefine.core.transformer;

import mods.Hileb.optirefine.core.OptiRefineLog;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ListIterator;

public final class RenderLibTransformer implements TransformerHelper.TargetedASMTransformer {
    private static final String RENDER_GLOBAL = "net/minecraft/client/renderer/RenderGlobal";
    private static final String ENTITY_PASS = "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V";
    private static final String ENTITY_MANAGER = "meldexun/renderlib/renderer/EntityRenderManager";
    private static final String TILE_MANAGER = "meldexun/renderlib/renderer/TileEntityRenderManager";
    private static final String TILE_DISPATCHER = "net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher";
    private static final String TILE_RENDER = "(Lnet/minecraft/tileentity/TileEntity;FI)V";

    @Override
    public String[] getTargets() {
        return new String[]{"meldexun.renderlib.asm.RenderLibClassTransformer", RENDER_GLOBAL.replace('/', '.')};
    }

    @Override
    public int transform(ClassNode node) {
        if (RENDER_GLOBAL.equals(node.name)) {
            configureEntityPass(node);
            return 0;
        }

        for (MethodNode method : node.methods) {
            if (!method.desc.equals("(Lorg/objectweb/asm/tree/MethodNode;)V") || !looksForShaderEntityCall(method)) {
                continue;
            }

            // RenderLib transforms RenderGlobal before Mixins. OptiRefine leaves that body in its vanilla layout.
            boolean updated = false;
            for (ListIterator<AbstractInsnNode> iter = method.instructions.iterator(); iter.hasNext(); ) {
                if (iter.next() instanceof FieldInsnNode field && field.getOpcode() == Opcodes.GETSTATIC
                        && field.owner.equals(node.name) && field.name.equals("OPTIFINE_DETECTED") && field.desc.equals("Z")) {
                    iter.set(new InsnNode(Opcodes.ICONST_0));
                    updated = true;
                }
            }
            if (updated) {
                OptiRefineLog.log.info("Using vanilla RenderGlobal anchors for RenderLib's entity transformer");
            }
        }
        return 0;
    }

    private static void configureEntityPass(ClassNode node) {
        MethodNode route = null;
        for (MethodNode method : node.methods) {
            if (method.name.equals("optiRefine$useRenderLibEntityPass") && method.desc.equals("()Z")) {
                route = method;
                break;
            }
        }
        if (route == null) return;

        // Foundation runs this after RenderLib and MixinExtras. Route to the original
        // body only when it still contains both RenderLib draw calls.
        for (MethodNode method : node.methods) {
            if (!method.desc.equals(ENTITY_PASS)) continue;
            MethodInsnNode entities = findDraw(method, ENTITY_MANAGER, "renderEntities");
            MethodInsnNode tiles = findDraw(method, TILE_MANAGER, "renderTileEntities");
            if (entities == null || tiles == null) continue;

            routeDraw(method, entities, "optiRefine$renderLibEntities");
            routeDraw(method, tiles, "optiRefine$renderLibBlockEntities");
            routeDamageOverlay(method);
            route.instructions.clear();
            route.instructions.add(new InsnNode(Opcodes.ICONST_1));
            route.instructions.add(new InsnNode(Opcodes.IRETURN));
            if (route.localVariables != null) route.localVariables.clear();
            OptiRefineLog.log.info("Keeping RenderLib's entity and block-entity render path");
            return;
        }
    }

    private static void routeDraw(MethodNode method, MethodInsnNode call, String name) {
        method.instructions.insertBefore(call, new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.insertBefore(call, new InsnNode(Opcodes.SWAP));
        method.maxStack++;
        call.setOpcode(Opcodes.INVOKESPECIAL);
        call.owner = RENDER_GLOBAL;
        call.name = name;
    }

    private static void routeDamageOverlay(MethodNode method) {
        boolean damageOverlay = false;
        for (AbstractInsnNode instruction : method.instructions) {
            if (!(instruction instanceof MethodInsnNode call)) continue;
            if (call.owner.equals(RENDER_GLOBAL) && call.desc.equals("()V")) {
                switch (call.name) {
                    case "preRenderDamagedBlocks", "func_180443_s" -> {
                        call.name = "optiRefine$beginRenderLibDamageOverlay";
                        damageOverlay = true;
                    }
                    case "postRenderDamagedBlocks", "func_174969_t" -> {
                        call.name = "optiRefine$endRenderLibDamageOverlay";
                        damageOverlay = false;
                    }
                }
            } else if (damageOverlay && call.owner.equals(TILE_DISPATCHER) && call.desc.equals(TILE_RENDER)
                    && (call.name.equals("render") || call.name.equals("func_180546_a"))) {
                call.setOpcode(Opcodes.INVOKESTATIC);
                call.owner = RENDER_GLOBAL;
                call.name = "optiRefine$renderLibDamagedBlockEntity";
                call.desc = "(L" + TILE_DISPATCHER + ";" + TILE_RENDER.substring(1);
            }
        }
    }

    private static MethodInsnNode findDraw(MethodNode method, String owner, String name) {
        for (AbstractInsnNode instruction : method.instructions) {
            if (instruction instanceof MethodInsnNode call && call.getOpcode() == Opcodes.INVOKESTATIC
                    && call.owner.equals(owner) && call.name.equals(name) && call.desc.equals("(F)V")) {
                return call;
            }
        }
        return null;
    }

    private static boolean looksForShaderEntityCall(MethodNode method) {
        for (AbstractInsnNode instruction : method.instructions) {
            if (instruction instanceof LdcInsnNode name && "endEntities".equals(name.cst)
                    && instruction.getPrevious() instanceof LdcInsnNode owner
                    && "net/optifine/shaders/Shaders".equals(owner.cst)) {
                return true;
            }
        }
        return false;
    }
}
