package mods.Hileb.optirefine.core.transformer.dev;

import mods.Hileb.optirefine.library.foundationx.mini.MiniTransformer;
import mods.Hileb.optirefine.library.foundationx.mini.PatchContext;
import mods.Hileb.optirefine.library.foundationx.mini.annotation.Patch;

import static mods.Hileb.optirefine.library.foundationx.ASMHelper.*;

@Patch.Class("ofdev.common.Utils")
public class OptifineDevUtilTransformer extends MiniTransformer {

    @Patch.Method("checkAndNotifyExtractedOptifineJar(Ljava/lang/ClassLoader;)V")
    public void patch$checkAndNotifyExtractedOptifineJar(PatchContext context) {
        context.jumpToStart();
        context.add(RETURN());
    }
}
