package mods.Hileb.optirefine.library.foundationx.asm;

import mods.Hileb.optirefine.library.foundationx.ASMHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import top.outlands.foundation.IExplicitTransformer;

/**
 * Packaged as ASM to make it more convenient.
 */
@FunctionalInterface
public interface IASMClassTransformer extends IExplicitTransformer , ASMHelper {
    /**
     * @param classNode node
     * @return the flags, {@link ClassWriter}
     */
    int transform(ClassNode classNode);

    @Override
    @Deprecated
    default byte[] transform(byte[] bytes){
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        ClassWriter classWriter = createClassWriter(this.transform(classNode));
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    /**
     * @param flags the flags
     * @return the created class writer. e.g. {@link LaunchClassWriter} or {@link NonLoadingClassWriter}
     */
    default ClassWriter createClassWriter(int flags){
        return new LaunchClassWriter(flags);
    }
}
