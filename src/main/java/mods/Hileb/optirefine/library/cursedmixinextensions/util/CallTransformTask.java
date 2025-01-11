package mods.Hileb.optirefine.library.cursedmixinextensions.util;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;

@FunctionalInterface
public interface CallTransformTask {
    void transform(InsnList method, MethodInsnNode methodInsnNode);

    @FunctionalInterface
    interface Simple extends CallTransformTask{
        @Override
        default void transform(InsnList method, MethodInsnNode methodInsnNode){
            transform(methodInsnNode);
        }

        void transform(MethodInsnNode methodInsnNode);
    }
}
