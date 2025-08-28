package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.optifine.gui.GuiButtonOF;
import net.optifine.gui.GuiScreenCapeOF;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Checked
@Mixin(GuiCustomizeSkin.class)
public abstract class MixinGuiCustomizeSkin extends GuiScreen {

    @Inject(method = "initGui", at = @At(value = "TAIL"))
    public void injectInitGui(CallbackInfo ci, @Local LocalIntRef lvt_1_1_){
        GuiButtonOF of = new GuiButtonOF(210, this.width / 2 - 100, this.height / 6 + 24 * (lvt_1_1_.get() >> 1), I18n.format("of.options.skinCustomisation.ofCape"));
        lvt_1_1_.set(lvt_1_1_.get() + 2);
        GuiButton done = this.buttonList.getLast();
        this.buttonList.add(this.buttonList.size() -1, of);
        done.y = this.height / 6 + 24 * (lvt_1_1_.get() >> 1);
        this.buttonList.add(done);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void injectActionPerformed(GuiButton button, CallbackInfo ci){
        if (button.enabled) {
            if (button.id == 210) {
                this.mc.displayGuiScreen(new GuiScreenCapeOF(this));
            }
        }
    }
}

/*
+++ net/minecraft/client/gui/GuiCustomizeSkin.java	Tue Aug 19 14:59:58 2025
@@ -1,11 +1,14 @@
 package net.minecraft.client.gui;

+import java.io.IOException;
 import net.minecraft.client.resources.I18n;
 import net.minecraft.client.settings.GameSettings;
 import net.minecraft.entity.player.EnumPlayerModelParts;
+import net.optifine.gui.GuiButtonOF;
+import net.optifine.gui.GuiScreenCapeOF;

 public class GuiCustomizeSkin extends GuiScreen {
    private final GuiScreen parentScreen;
    private String title;

    public GuiCustomizeSkin(GuiScreen var1) {
@@ -33,25 +36,31 @@
             )
          );
       if (++var1 % 2 == 1) {
          var1++;
       }

+      this.buttonList.add(new GuiButtonOF(210, this.width / 2 - 100, this.height / 6 + 24 * (var1 >> 1), I18n.format("of.options.skinCustomisation.ofCape")));
+      var1 += 2;
       this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 24 * (var1 >> 1), I18n.format("gui.done")));
    }

-   protected void keyTyped(char var1, int var2) {
+   protected void keyTyped(char var1, int var2) throws IOException {
       if (var2 == 1) {
          this.mc.gameSettings.saveOptions();
       }

       super.keyTyped(var1, var2);
    }

-   protected void actionPerformed(GuiButton var1) {
+   protected void actionPerformed(GuiButton var1) throws IOException {
       if (var1.enabled) {
+         if (var1.id == 210) {
+            this.mc.displayGuiScreen(new GuiScreenCapeOF(this));
+         }
+
          if (var1.id == 200) {
             this.mc.gameSettings.saveOptions();
             this.mc.displayGuiScreen(this.parentScreen);
          } else if (var1.id == 199) {
             this.mc.gameSettings.setOptionValue(GameSettings.Options.MAIN_HAND, 1);
             var1.displayString = this.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND);
@@ -63,13 +72,13 @@
          }
       }
    }

    public void drawScreen(int var1, int var2, float var3) {
       this.drawDefaultBackground();
-      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
+      this.a(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
       super.drawScreen(var1, var2, var3);
    }

    private String getMessage(EnumPlayerModelParts var1) {
       String var2;
       if (this.mc.gameSettings.getModelParts().contains(var1)) {
 */
