package mods.Hileb.optirefine.core.transformer;

import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.*;

public class OptiRefineConfigTransformer implements TransformerHelper.TargetedASMTransformer {
    private static final Remapper CONFIG_REMAPPER = new SimpleRemapper("mods/Hileb/optirefine/optifine/Config", "Config");

    public static void transform0(ClassNode classNode){
        for (MethodNode mn : classNode.methods) {
            for (AbstractInsnNode abstractInsnNode : mn.instructions) {
                switch (abstractInsnNode) {
                    case MethodInsnNode methodInsnNode: {
                        methodInsnNode.owner = methodInsnNode.owner.replace("mods/Hileb/optirefine/optifine/Config", "Config");
                        methodInsnNode.name = methodInsnNode.name.replace("mods/Hileb/optirefine/optifine/Config", "Config");
                        methodInsnNode.desc = methodInsnNode.desc.replace("mods/Hileb/optirefine/optifine/Config", "Config");
                        break;
                    }
                    case FieldInsnNode fieldInsnNode: {
                        fieldInsnNode.owner = fieldInsnNode.owner.replace("mods/Hileb/optirefine/optifine/Config", "Config");
                        fieldInsnNode.name = fieldInsnNode.name.replace("mods/Hileb/optirefine/optifine/Config", "Config");
                        fieldInsnNode.desc = fieldInsnNode.desc.replace("mods/Hileb/optirefine/optifine/Config", "Config");
                        break;
                    }
                    case LdcInsnNode ldcInsnNode: {
                        switch (ldcInsnNode.cst) {
                            case String str :{
                                ldcInsnNode.cst = str.replace("mods/Hileb/optirefine/optifine/Config", "Config");
                                break;
                            }
                            case Type type :{
                                ldcInsnNode.cst = Type.getType(type.toString().replace("mods/Hileb/optirefine/optifine/Config", "Config"));
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                        break;
                    }
                    default:
                }
            }
        }
    }

    @Override
    public String[] getTargets() {
        return new String[]{
                "mods.Hileb.optirefine.optifine.client.GameSettingsOptionOF"
        };
    }

    @Override
    public int transform(ClassNode classNode) {
        transform0(classNode);
        return 0;
    }
}
