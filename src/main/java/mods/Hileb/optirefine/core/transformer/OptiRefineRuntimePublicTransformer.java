package mods.Hileb.optirefine.core.transformer;

import mods.Hileb.optirefine.core.OptiRefineBlackboard;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import net.minecraftforge.fml.common.asm.transformers.EventSubscriberTransformer;
import net.minecraftforge.fml.common.asm.transformers.EventSubscriptionTransformer;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

public class OptiRefineRuntimePublicTransformer implements TransformerHelper.TargetedASMTransformer {
    private static final Remapper CONFIG_REMAPPER = new SimpleRemapper("mods/Hileb/optirefine/optifine/Config", "Config");


    private static int toPublic(int access) {
        return access & -7 | 1;
    }

    @Override
    public int transform(ClassNode classNode) {
        classNode.access = toPublic(classNode.access);
//        HashMap<String, FieldNode> fs = new HashMap<>();
//        for (FieldNode fn : ImmutableList.copyOf(classNode.fields)) {
//            if (fs.put(fn.name + fn.desc, fn) instanceof FieldNode fieldNode) {
//                classNode.fields.remove(fieldNode);
//                System.err.println("Error at Duplicate field " +fn.name+fn.desc + " for " + classNode.name);
//            }
//        }
        //classNode.methods.forEach((mn) -> mn.access = toPublic(mn.access));
        //classNode.fields.forEach((mn) -> mn.access = toPublic(mn.access));
        return 0;
    }

    @Override
    public String[] getTargets() {
        return OptiRefineBlackboard.CLASSES.toArray(String[]::new);
    }
}
