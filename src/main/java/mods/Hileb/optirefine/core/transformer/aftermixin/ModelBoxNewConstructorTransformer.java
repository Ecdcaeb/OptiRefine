package mods.Hileb.optirefine.core.transformer.aftermixin;

import mods.Hileb.optirefine.core.transformer.TransformerHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ModelBoxNewConstructorTransformer implements TransformerHelper.ITransformer {
    @Override
    public void transform(ClassNode classNode) {
        for (MethodNode mn : classNode.methods){
            if ("optirefine$init".equals(mn.name)) {
                mn.instructions.insert(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false));
                mn.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 0));
            }
        }
    }
}
