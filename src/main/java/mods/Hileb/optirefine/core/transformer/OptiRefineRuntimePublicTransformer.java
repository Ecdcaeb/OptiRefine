package mods.Hileb.optirefine.core.transformer;

import mods.Hileb.optirefine.core.OptiRefineBlackboard;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
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
        //classNode.methods.forEach((mn) -> mn.access = toPublic(mn.access));
        //classNode.fields.forEach((mn) -> mn.access = toPublic(mn.access));
        return 0;
    }

    @Override
    public String[] getTargets() {
        return OptiRefineBlackboard.CLASSES.toArray(String[]::new);
    }
}
