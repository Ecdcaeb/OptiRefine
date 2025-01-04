package mods.Hileb.optirefine.core.transformer.aftermixin;

import mods.Hileb.optirefine.core.transformer.TransformerHelper;
import org.objectweb.asm.tree.ClassNode;

public class GuiScreenOFTransformer implements TransformerHelper.ITransformer {
    @Override
    public void transform(ClassNode classNode) {
        classNode.superName = "net/optifine/gui/GuiScreenOF";
    }
}
