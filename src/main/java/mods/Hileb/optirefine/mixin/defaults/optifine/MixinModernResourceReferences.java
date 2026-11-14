package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.resources.ModernResourcePaths;
import net.optifine.util.TextureUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TextureUtils.class, remap = false)
public abstract class MixinModernResourceReferences {
    @Inject(method = "fixResourcePath", at = @At("RETURN"), cancellable = true, remap = false)
    private static void optirefine$modernAbsoluteReference(String path, String basePath, CallbackInfoReturnable<String> cir) {
        String resolved = cir.getReturnValue();
        String canonical = ModernResourcePaths.normalizeReference(resolved, basePath);
        if (canonical != resolved) {
            cir.setReturnValue(canonical);
        }
    }
}
