package mods.Hileb.optirefine.mixin.defaults.minecraftforge.fml;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mods.Hileb.optirefine.Reference;
import mods.Hileb.optirefine.core.OptiRefineCore;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
@Mixin(FMLClientHandler.class)
public abstract class MixinFMLClientHandler {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1

    @Inject(method = "handleLoadingScreen", at = @At("RETURN"), remap = false, cancellable = true)
// [AUDIT-OK] custom loading screen when vanilla returns false; remap=false correct (Forge classes not in tsrg); CIR correct for non-void target; net.optifine.CustomLoadingScreens API not verifiable from patch trees (OF jar class)
    public void inject$handleLoadingScreen(ScaledResolution scaledResolution, CallbackInfoReturnable<Boolean> returnable){
        if (!returnable.getReturnValueZ()) {
            CustomLoadingScreen scr = CustomLoadingScreens.getCustomLoadingScreen();
            if (scr != null) {
                scr.drawBackground(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
                returnable.setReturnValue(true);
            }
        }
    }

    @ModifyReturnValue(method = "getAdditionalBrandingInformation", at = @At("RETURN"))
    // [AUDIT-FIXED] keep the F3 branding line short: name only, no version (full version already in the mod list)
    public List<String> modifyOptifineBrand(List<String> original){
        original.replaceAll(s -> {
            if (s.startsWith("Optifine")) {
                return s + " + OptiRefine";
            } else return s;
        });
        return original;
    }
}
