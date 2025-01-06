package mods.Hileb.optirefine.core.transformer;

import mods.Hileb.optirefine.core.OptiRefineBlackboard;
import mods.Hileb.optirefine.core.transformer.aftermixin.GuiScreenOFTransformer;
import mods.Hileb.optirefine.core.transformer.aftermixin.ModelBoxNewConstructorTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import top.outlands.foundation.IExplicitTransformer;
import top.outlands.foundation.TransformerDelegate;

public class TransformerHelper {
    @FunctionalInterface
    public interface ITransformer extends IExplicitTransformer{
        void transform(ClassNode classNode);

        @Override
        default byte[] transform(byte[] bytes){
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            this.transform(classNode);

            ClassWriter classWriter = new ClassWriter(0);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
    }

    @FunctionalInterface
    public interface ITransformerx extends IExplicitTransformer{
        boolean transform(ClassNode classNode);

        @Override
        default byte[] transform(byte[] bytes){
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            boolean frames = this.transform(classNode);

            int flags = ClassWriter.COMPUTE_MAXS;
            if (frames) {
                flags |= ClassWriter.COMPUTE_FRAMES;
            }

            ClassWriter classWriter = new ClassWriter(flags);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
    }

    public static void setupOptiRefineTransformers(){
        TransformerDelegate.registerExplicitTransformer(
                new OptifineTransformerTransformer(),
                "optifine.OptiFineClassTransformer"
        );
        TransformerDelegate.registerExplicitTransformer(
                new OptiRefineRuntimePublicTransformer(),
                OptiRefineBlackboard.CLASSES.toArray(String[]::new)
        );
        // - extends GuiScreen + extends GuiScreenOF
        TransformerDelegate.registerExplicitTransformer(
                new GuiScreenOFTransformer(),
                "net.minecraft.client.gui.GuiVideoSettings"
        );

        TransformerDelegate.registerExplicitTransformer(
                new ModelBoxNewConstructorTransformer(),
                "net.minecraft.client.model.ModelBox"
        );

    }
}
