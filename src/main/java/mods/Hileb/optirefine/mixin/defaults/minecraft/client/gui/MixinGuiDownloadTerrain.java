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
/*
+++ net/minecraft/client/gui/GuiDownloadTerrain.java	Tue Aug 19 14:59:58 2025
@@ -1,18 +1,27 @@
 package net.minecraft.client.gui;

 import net.minecraft.client.resources.I18n;
+import net.optifine.CustomLoadingScreen;
+import net.optifine.CustomLoadingScreens;

 public class GuiDownloadTerrain extends GuiScreen {
+   private CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();
+
    public void initGui() {
       this.buttonList.clear();
    }

    public void drawScreen(int var1, int var2, float var3) {
-      this.drawBackground(0);
-      this.drawCenteredString(this.fontRenderer, I18n.format("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 - 50, 16777215);
+      if (this.customLoadingScreen != null) {
+         this.customLoadingScreen.drawBackground(this.width, this.height);
+      } else {
+         this.drawBackground(0);
+      }
+
+      this.a(this.fontRenderer, I18n.format("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 - 50, 16777215);
       super.drawScreen(var1, var2, var3);
    }

    public boolean doesGuiPauseGame() {
       return false;
    }
 */
