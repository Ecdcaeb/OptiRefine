package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiScreenWorking.class)
public abstract class MixinGuiScreenWorking extends GuiScreen {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    
    private CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreenWorking;drawDefaultBackground()V"))
    public void injectDrawScreen(GuiScreenWorking instance){
        if (this.customLoadingScreen != null && this.mc.world == null) {
            this.customLoadingScreen.drawBackground(this.width, this.height);
        } else {
            this.drawDefaultBackground();
        }
    }
}
