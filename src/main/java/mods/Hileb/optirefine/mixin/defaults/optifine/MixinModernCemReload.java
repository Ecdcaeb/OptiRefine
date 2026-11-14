package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.optifine.entity.model.CustomEntityModels", remap = false)
public abstract class MixinModernCemReload {
    @Inject(method = "update", at = @At("HEAD"))
    private static void optiRefine$reloadVariables(CallbackInfo ci) {
        CemRenderContext.reload();
    }
}
