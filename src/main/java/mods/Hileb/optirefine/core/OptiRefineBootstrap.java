package mods.Hileb.optirefine.core;

import mods.Hileb.optirefine.core.transformer.OptiRefineConfigTransformer;
import mods.Hileb.optirefine.core.transformer.OptifineForgeTransformer;
import mods.Hileb.optirefine.core.transformer.OptifineTransformerTransformer;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;

import java.io.File;

/** Launch-time state without class-initialization registration side effects. */
public final class OptiRefineBootstrap {
    private static File source;
    private static boolean registered;

    private OptiRefineBootstrap() {}

    public static synchronized void registerTransformers() {
        if (registered) return;
        TransformerHelper.registerTransformer(new OptifineTransformerTransformer());
        TransformerHelper.registerTransformer(new OptifineForgeTransformer());
        TransformerHelper.registerTransformer(new OptiRefineConfigTransformer());
        registered = true;
    }

    static void setSource(File location) { source = location; }
    static File source() { return source; }
}
