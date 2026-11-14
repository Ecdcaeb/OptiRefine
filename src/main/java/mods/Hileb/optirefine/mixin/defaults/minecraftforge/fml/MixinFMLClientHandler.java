package mods.Hileb.optirefine.mixin.defaults.minecraftforge.fml;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mods.Hileb.optirefine.Reference;
import mods.Hileb.optirefine.core.OptiRefineCore;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
@Mixin(FMLClientHandler.class)
public abstract class MixinFMLClientHandler {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
// [AUDIT-FIXED] handleLoadingScreen compensation removed: custom loading background now draws inside
// LoadingScreenRenderer.setLoadingProgress (MixinLoadingScreenRenderer), so handleLoadingScreen keeps
// FML's original return value (true only while GuiNotification is up) — OF-exact.

    @ModifyReturnValue(method = "getAdditionalBrandingInformation", at = @At("RETURN"))
    // [AUDIT-FIXED] keep the F3 branding line short: name only, no version (full version already in the mod list)
    public List<String> optiRefine$appendOptiRefineBranding(List<String> original){
        if (original.isEmpty()) return original;
        List<String> branding = new ArrayList<>(original);
        branding.replaceAll(s -> {
            if (s.startsWith("Optifine")) {
                return s + " + OptiRefine";
            } else return s;
        });
        return branding;
    }
}
