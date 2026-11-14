package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.optifine.shaders.IShaderPack;
import net.optifine.shaders.config.ShaderOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(targets = "net.optifine.shaders.config.ShaderPackParser", remap = false)
public abstract class MixinModernShaderOptions {
    @Shadow
    private static void collectShaderOptions(IShaderPack pack, String path, Map<String, ShaderOption> options) {
        throw new AssertionError();
    }

    @Inject(method = "collectShaderOptions(Lnet/optifine/shaders/IShaderPack;Ljava/lang/String;[Ljava/lang/String;Ljava/util/Map;)V", at = @At("RETURN"))
    private static void optiRefine$computeOptions(IShaderPack pack, String directory, String[] programs, Map<String, ShaderOption> options, CallbackInfo ci) {
        // Use G5's own include traversal, option parser, usage check and ambiguity merge.
        // This runs both for /shaders and every discovered world<dimension> override.
        for (String program : programs) {
            if (program.isEmpty() || program.startsWith("gbuffers_") || program.endsWith("_pre")) continue;
            String base = directory + "/" + program;
            collectShaderOptions(pack, base + ".csh", options);
            for (char suffix = 'a'; suffix <= 'z'; suffix++) collectShaderOptions(pack, base + "_" + suffix + ".csh", options);
        }
    }
}
