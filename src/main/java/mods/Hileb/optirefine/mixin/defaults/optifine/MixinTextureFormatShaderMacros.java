package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.resources.ModernTextureProperties;
import net.optifine.shaders.config.ShaderMacros;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ShaderMacros.class, remap = false)
public abstract class MixinTextureFormatShaderMacros {
    @Inject(method = "getOptionMacroLines", at = @At("RETURN"), cancellable = true, remap = false)
    private static void optiRefine$defineAtlasTextureFormat(CallbackInfoReturnable<String> cir) {
        String macros = ModernTextureProperties.blockAtlasMacros();
        if (!macros.isEmpty()) {
            cir.setReturnValue(cir.getReturnValue() + macros);
        }
    }
}
