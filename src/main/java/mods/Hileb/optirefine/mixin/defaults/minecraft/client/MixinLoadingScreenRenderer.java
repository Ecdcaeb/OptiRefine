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

// [AUDIT-OK] OF setLoadingProgress: when CustomLoadingScreens.getCustomLoadingScreen() != null draw the
// custom background instead of the vanilla options-background quad; the progress bar + text lines draw
// on top in the same guarded body. First Tessellator.draw() in the method IS the background quad (the
// FML guard is landed natively by cleanroom around the whole body).
    @WrapOperation(method = "setLoadingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()I", ordinal = 0))
    public int injectDrawCustomBackground(Tessellator instance, Operation<Integer> original, @Local ScaledResolution scaledResolution) {
        CustomLoadingScreen scr = CustomLoadingScreens.getCustomLoadingScreen();
        if (scr != null) {
            scr.drawBackground(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            return 0;
        } else {
            return original.call(instance);
        }
    }
}
