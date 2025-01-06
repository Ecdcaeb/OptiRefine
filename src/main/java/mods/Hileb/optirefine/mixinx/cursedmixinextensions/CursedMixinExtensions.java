package mods.Hileb.optirefine.mixinx.cursedmixinextensions;

import com.llamalad7.mixinextras.service.MixinExtrasService;
import com.llamalad7.mixinextras.service.MixinExtrasVersion;
import mods.Hileb.optirefine.mixinx.cursedmixinextensions.annotations.*;
import mods.Hileb.optirefine.mixinx.cursedmixinextensions.annotations.ChangeSuperClass;
import mods.Hileb.optirefine.mixinx.cursedmixinextensions.annotations.ShadowSuper;
import mods.Hileb.optirefine.mixinx.cursedmixinextensions.annotations.Public;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.util.Annotations;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author LlamaLad7
 * @author Cat Core
 */
public class CursedMixinExtensions implements IExtension {
    private static boolean isLaunched = false;
    public static void bootstrap(){
        if (!isLaunched) {
            MixinExtrasService.getInstance().offerExtension(MixinExtrasVersion.V0_5_0_BETA_4.getNumber(), new CursedMixinExtensions());
            isLaunched = true;
        }
    }

    protected static void postApply(ClassNode targetClass) {
        AnnotationNode changeSuperClass = Annotations.getVisible(targetClass, ChangeSuperClass.class);

        if (changeSuperClass != null) {
            String oldOwner = targetClass.superName;
            targetClass.superName = Annotations.<Type>getValue(changeSuperClass).getInternalName();
            transformCalls(targetClass, call -> {
                if (call.getOpcode() == Opcodes.INVOKESPECIAL && call.owner.equals(oldOwner)) {
                    call.owner = targetClass.superName;
                }
            });
        }

        List<String> ctrToReplace = new ArrayList<>();

        for (ListIterator<MethodNode> it = targetClass.methods.listIterator(); it.hasNext(); ) {
            MethodNode method = it.next();

            if (Annotations.getVisible(method, ShadowSuperConstructor.class) != null) {
                it.remove();
                transformCalls(targetClass, call -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                        call.owner = targetClass.superName;
                    }
                });
                continue;
            }

            if (Annotations.getVisible(method, ShadowConstructor.class) != null) {
                it.remove();
                transformCalls(targetClass, call -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                    }
                });
                continue;
            }

            AnnotationNode superMethod = Annotations.getVisible(method, ShadowSuper.class);

            if (superMethod != null) {
                it.remove();
                String targetName = Annotations.getValue(superMethod);

                transformCalls(targetClass, call -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = targetName;
                        call.owner = targetClass.superName;
                    }
                });

                continue;
            }

            if (Annotations.getVisible(method, Public.class) != null) {
                method.access |= Opcodes.ACC_PUBLIC;
                method.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
            }

            if (Annotations.getVisible(method, NewConstructor.class) != null) {
                transformCalls(targetClass, call -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                    }
                });
                method.name = "<init>";
            }

            if (Annotations.getVisible(method, ReplaceConstructor.class) != null) {
                ctrToReplace.add(method.desc);
                transformCalls(targetClass, call -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                    }
                });
                method.name = "<init>";
            }
        }

        if (!ctrToReplace.isEmpty()) {
            for (ListIterator<MethodNode> it = targetClass.methods.listIterator(); it.hasNext(); ) {
                MethodNode method = it.next();

                if (Annotations.getVisible(method, ReplaceConstructor.class) == null) {
                    if (Objects.equals(method.name, "<init>") && ctrToReplace.contains(method.desc)) {
                        it.remove();
                    }
                }
            }
        }

        for (FieldNode field : targetClass.fields) {
            if (Annotations.getVisible(field, Public.class) != null) {
                field.access |= Opcodes.ACC_PUBLIC;
                field.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
            }
        }
    }

    private static void transformCalls(ClassNode classNode, Consumer<MethodInsnNode> consumer) {
        for (MethodNode method : classNode.methods) {
            for (AbstractInsnNode insn : method.instructions) {
                if (insn instanceof MethodInsnNode call) {
                    consumer.accept(call);
                }
            }
        }
    }

    @Override
    public boolean checkActive(MixinEnvironment mixinEnvironment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext iTargetClassContext) {

    }

    @Override
    public void postApply(ITargetClassContext iTargetClassContext) {
        postApply(iTargetClassContext.getClassNode());
    }

    @Override
    public void export(MixinEnvironment mixinEnvironment, String s, boolean b, ClassNode classNode) {

    }
}
