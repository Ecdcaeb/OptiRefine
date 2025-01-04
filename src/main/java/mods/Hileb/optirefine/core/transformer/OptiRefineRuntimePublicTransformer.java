package mods.Hileb.optirefine.core.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;
import top.outlands.foundation.IExplicitTransformer;

public class OptiRefineRuntimePublicTransformer implements IExplicitTransformer {
    private static final Remapper CONFIG_REMAPPER = new SimpleRemapper("mods/Hileb/optirefine/optifine/Config", "Config");
    @Override
    public byte[] transform(byte[] bytes) {
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        classNode.access = toPublic(classNode.access);
        classNode.methods.forEach((mn) -> mn.access = toPublic(mn.access));
        classNode.fields.forEach((mn) -> mn.access = toPublic(mn.access));

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        classNode.accept(new ClassRemapper(classNode, CONFIG_REMAPPER));
        return classWriter.toByteArray();
    }

    private static int toPublic(int access) {
        return access & -7 | 1;
    }
}
