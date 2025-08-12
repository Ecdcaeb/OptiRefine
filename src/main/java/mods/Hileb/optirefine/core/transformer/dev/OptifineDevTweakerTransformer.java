package mods.Hileb.optirefine.core.transformer.dev;

import mods.Hileb.optirefine.library.foundationx.mini.MiniTransformer;
import mods.Hileb.optirefine.library.foundationx.mini.PatchContext;
import mods.Hileb.optirefine.library.foundationx.mini.annotation.Patch;
import org.objectweb.asm.Type;

import static mods.Hileb.optirefine.library.foundationx.ASMHelper.*;

@Patch.Class("ofdev.launchwrapper.OptifineDevTransformerWrapper")
public class OptifineDevTweakerTransformer extends MiniTransformer {

    @SuppressWarnings("unused")
    @Patch.Method("<clinit>()V")
    @Patch.Method.AffectsControlFlow
    public void patch$clinit(PatchContext context) {
        for (PatchContext.SearchResult searchResult : context.search(
                LDC(Type.getType("Lnet/minecraft/launchwrapper/LaunchClassLoader;")),
                GETSTATIC("net/minecraft/launchwrapper/Launch", "classLoader", "Lnet/minecraft/launchwrapper/LaunchClassLoader;"),
                LDC("transformers"),
                INVOKESTATIC("ofdev/common/Utils", "getFieldValue", "(Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", false)
        )) {
            searchResult.jumpBefore();
            context.add(
                    GETSTATIC("top/outlands/foundation/boot/TransformerHolder", "transformers", "Ljava/util/List;")
            );
            searchResult.erase();
        }
    }
}
