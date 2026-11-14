package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinModernCemFrameClock {
    @Inject(method = "updateCameraAndRender", at = @At("HEAD"))
    private void optiRefine$beginCemFrame(float partialTicks, long finishTimeNano, CallbackInfo ci) {
        CemRenderContext.beginFrame(System.nanoTime());
    }
}
