package mods.Hileb.optirefine.library.foundationx;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import top.outlands.foundation.IExplicitTransformer;
import top.outlands.foundation.TransformerDelegate;

public class TransformerHelper {

    @FunctionalInterface
    public interface ASMTransformer extends IExplicitTransformer{
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
            ClassWriter classWriter = new ClassWriter(this.transform(classNode));
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
    }

    public interface TargetedASMTransformer extends ASMTransformer {
        String[] getTargets();
    }

    public static void registerTransformer(TargetedASMTransformer transformer){
        TransformerDelegate.registerExplicitTransformer(
                transformer,
                transformer.getTargets()
        );
    }

    public static void registerTransformer(IClassTransformer transformer){
        TransformerDelegate.registerTransformer(transformer);
    }

    public static void registerTransformer(IExplicitTransformer transformer, String... targets){
        TransformerDelegate.registerExplicitTransformer(transformer, targets);
    }
    
}
