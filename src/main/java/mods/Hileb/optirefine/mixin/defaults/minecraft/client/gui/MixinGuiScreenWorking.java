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
/*
+++ net/minecraft/client/gui/GuiScreenWorking.java	Tue Aug 19 14:59:58 2025
@@ -1,15 +1,18 @@
 package net.minecraft.client.gui;

 import net.minecraft.util.IProgressUpdate;
+import net.optifine.CustomLoadingScreen;
+import net.optifine.CustomLoadingScreens;

 public class GuiScreenWorking extends GuiScreen implements IProgressUpdate {
    private String title = "";
    private String stage = "";
    private int progress;
    private boolean doneWorking;
+   private CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

    public void displaySavingString(String var1) {
       this.resetProgressAndMessage(var1);
    }

    public void resetProgressAndMessage(String var1) {
@@ -30,16 +33,24 @@
       this.doneWorking = true;
    }

    public void drawScreen(int var1, int var2, float var3) {
       if (this.doneWorking) {
          if (!this.mc.isConnectedToRealms()) {
-            this.mc.displayGuiScreen(null);
+            this.mc.displayGuiScreen((GuiScreen)null);
          }
       } else {
-         this.drawDefaultBackground();
-         this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 70, 16777215);
-         this.drawCenteredString(this.fontRenderer, this.stage + " " + this.progress + "%", this.width / 2, 90, 16777215);
+         if (this.customLoadingScreen != null && this.mc.world == null) {
+            this.customLoadingScreen.drawBackground(this.width, this.height);
+         } else {
+            this.drawDefaultBackground();
+         }
+
+         if (this.progress > 0) {
+            this.a(this.fontRenderer, this.title, this.width / 2, 70, 16777215);
+            this.a(this.fontRenderer, this.stage + " " + this.progress + "%", this.width / 2, 90, 16777215);
+         }
+
          super.drawScreen(var1, var2, var3);
       }
    }
 }
 */
