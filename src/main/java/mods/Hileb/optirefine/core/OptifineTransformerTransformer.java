package mods.Hileb.optirefine.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import top.outlands.foundation.IExplicitTransformer;

public class OptifineTransformerTransformer implements IExplicitTransformer {
    @Override
    public byte[] transform(byte[] bytes) {
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        for (MethodNode mn : classNode.methods) {
            if ("transform".equals(mn.name) && "(Ljava/lang/String;Ljava/lang/String;[B)[B".equals(mn.desc)) {
                LabelNode labelNode = new LabelNode(new Label());
                mn.instructions.add(labelNode);
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                mn.instructions.add(new InsnNode(Opcodes.ARETURN));

                mn.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, "mods/Hileb/optirefine/core/OptifineTransformerTransformer", "canTranform", "(Ljava/lang/String;)Z)", false));
                mn.instructions.insert(new JumpInsnNode(Opcodes.IFEQ, labelNode));
            }
        }

        var classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    @SuppressWarnings("unused") //ASM invoked
    public static boolean canTransform(String name) {
        return !OptiRefineBlackboard.CLASSES.contains(name);
    }
}
