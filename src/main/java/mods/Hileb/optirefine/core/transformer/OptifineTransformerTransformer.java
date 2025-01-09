package mods.Hileb.optirefine.core.transformer;

import mods.Hileb.optirefine.library.foundationx.mini.MiniTransformer;
import mods.Hileb.optirefine.library.foundationx.mini.PatchContext;
import mods.Hileb.optirefine.library.foundationx.mini.annotation.Patch;
import org.objectweb.asm.tree.*;

@SuppressWarnings("unused") //ASM invoked
@Patch.Class("optifine.OptiFineClassTransformer")
public class OptifineTransformerTransformer extends MiniTransformer {

    @Patch.Method("transform(Ljava/lang/String;Ljava/lang/String;[B)[B")
    @Patch.Method.AffectsControlFlow
    public void patch$transform(PatchContext context){
        LabelNode labelNode = new LabelNode();
        context.jumpToStart();
        context.add(
                GETSTATIC("mods/Hileb/optirefine/core/OptiRefineBlackboard", "CLASSES", "Ljava/util/HashSet;"),
                INVOKEVIRTUAL("java/util/HashSet", "contains", "(Ljava/lang/Object;)Z", true),
                IFNE(labelNode),
                ALOAD(3),
                ARETURN(),
                labelNode
        );
    }
}
