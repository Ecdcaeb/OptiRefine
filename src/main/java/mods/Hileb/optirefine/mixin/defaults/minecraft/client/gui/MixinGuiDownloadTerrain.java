package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiDownloadTerrain.class)
public abstract class MixinGuiDownloadTerrain extends GuiScreen {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    
    private CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiDownloadTerrain;drawBackground(I)V"))
    public void injectDrawScreen(GuiDownloadTerrain instance, int i) {
        if (customLoadingScreen != null) {
            customLoadingScreen.drawBackground(this.width, this.height);
        } else instance.drawBackground(i);
    }

}
