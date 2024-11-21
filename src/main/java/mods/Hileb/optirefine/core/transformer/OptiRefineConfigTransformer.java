package mods.Hileb.optirefine.core.transformer;

import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

public class OptiRefineConfigTransformer {
    private static final Remapper CONFIG_REMAPPER = new SimpleRemapper("mods/Hileb/optirefine/optifine/Config", "Config");

    public static void transform(ClassNode classNode){
        classNode.accept(new ClassRemapper(classNode, CONFIG_REMAPPER));
    }
}
