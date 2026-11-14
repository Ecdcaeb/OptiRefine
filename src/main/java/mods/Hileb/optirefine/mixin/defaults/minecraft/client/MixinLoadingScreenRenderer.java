package mods.Hileb.optirefine.mixin.defaults.minecraft.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LoadingScreenRenderer.class)
public abstract class MixinLoadingScreenRenderer {

    // The vanilla background vertices are already queued here; discard and finish
    // that batch before the custom screen starts its own Tessellator batch.
    @WrapOperation(method = "setLoadingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()V", ordinal = 0))
    public void injectDrawCustomBackground(Tessellator instance, Operation<Void> original, @Local ScaledResolution scaledResolution) {
        CustomLoadingScreen scr = CustomLoadingScreens.getCustomLoadingScreen();
        if (scr != null) {
            instance.getBuffer().finishDrawing();
            instance.getBuffer().reset();
            scr.drawBackground(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        } else {
            original.call(instance);
        }
    }
}
