package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(GuiDownloadTerrain.class)
public abstract class MixinGuiDownloadTerrain extends GuiScreen {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private CustomLoadingScreen optiRefine$customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

// [AUDIT-OK] target drawScreen matches baseline; drawBackground(I)V invoke (inherited GuiScreen) matches; replicates OF
    @WrapOperation(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiDownloadTerrain;drawBackground(I)V"))
    public void injectDrawScreen(GuiDownloadTerrain instance, int i, Operation<Void> original) {
        if (optiRefine$customLoadingScreen != null) {
            optiRefine$customLoadingScreen.drawBackground(this.width, this.height);
        } else original.call(instance, i);
    }

}
