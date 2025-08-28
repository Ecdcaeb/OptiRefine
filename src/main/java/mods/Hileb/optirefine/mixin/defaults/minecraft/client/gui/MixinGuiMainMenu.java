package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Checked
@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

    @ModifyConstant(method = "drawPanorama", constant = @Constant(intValue = 64))
    public int injectDrawPanorama(int value) {
        CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
        if (cpp != null) {
            return cpp.getBlur1();
        } else return value;
    }

    @ModifyConstant(method = "rotateAndBlurSkybox", constant = @Constant(intValue = 3))
    public int injectRotateAndBlurSkybox(int value){
        CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
        if (cpp != null) {
            return cpp.getBlur2();
        } else return value;
    }

    @Shadow
    protected abstract void rotateAndBlurSkybox();

    @Shadow
    protected abstract void drawPanorama(int p, int p1, float p2);

    @Shadow public abstract void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_);

    @WrapMethod(method = "renderSkybox")
    public void injectRenderSkybox(int mouseX, int mouseY, float partialTicks, Operation<Void> original){
        CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
        if (cpp != null) {
            this.mc.getFramebuffer().unbindFramebuffer();
            GlStateManager.viewport(0, 0, 256, 256);
            this.drawPanorama(mouseX, mouseY, partialTicks);
            for (int i = 0; i < cpp.getBlur3(); i++) {
                this.rotateAndBlurSkybox();
                this.rotateAndBlurSkybox();
            }
            this.mc.getFramebuffer().bindFramebuffer(true);
            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            float f = 120.0F / (float)(Math.max(this.width, this.height));
            float f1 = (float)this.height * f / 256.0F;
            float f2 = (float)this.width * f / 256.0F;
            int i = this.width;
            int j = this.height;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(0.0F, j, this.zLevel).tex(0.5F - f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            bufferbuilder.pos(i, j, this.zLevel).tex(0.5F - f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            bufferbuilder.pos(i, 0.0F, this.zLevel).tex(0.5F + f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            bufferbuilder.pos(0.0F, 0.0F, this.zLevel).tex(0.5F + f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            tessellator.draw();
        } else original.call(mouseX, mouseY, partialTicks);
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V"))
    public void redirectDrawGradientRect1(GuiMainMenu instance, int i1, int i2, int i3, int i4, int i5, int i6){
        CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
        if (cpp != null) {
            if (i5 == -2130706433) {
                this.drawGradientRect(i1, i2, i3, i4, cpp.getOverlay1Top(), cpp.getOverlay1Bottom());
            } else if (i5 == 0) {
                this.drawGradientRect(i1, i2, i3, i4, cpp.getOverlay2Top(), cpp.getOverlay2Bottom());
            } else this.drawGradientRect(i1, i2, i3, i4, i5, i6);
        } else this.drawGradientRect(i1, i2, i3, i4, i5, i6);
    }

}
/*
+++ net/minecraft/client/gui/GuiMainMenu.java	Tue Aug 19 14:59:58 2025
@@ -1,18 +1,20 @@
 package net.minecraft.client.gui;

+import com.google.common.base.Strings;
 import com.google.common.collect.Lists;
 import com.google.common.util.concurrent.Runnables;
 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.net.URI;
 import java.nio.charset.StandardCharsets;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.Date;
+import java.util.List;
 import java.util.Random;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.Tessellator;
@@ -26,12 +28,15 @@
 import net.minecraft.util.StringUtils;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.text.TextFormatting;
 import net.minecraft.world.WorldServerDemo;
 import net.minecraft.world.storage.ISaveFormat;
 import net.minecraft.world.storage.WorldInfo;
+import net.optifine.CustomPanorama;
+import net.optifine.CustomPanoramaProperties;
+import net.optifine.reflect.Reflector;
 import org.apache.commons.io.IOUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 import org.lwjgl.input.Mouse;
 import org.lwjgl.opengl.GLContext;
 import org.lwjgl.util.glu.Project;
@@ -69,12 +74,14 @@
    private ResourceLocation backgroundTexture;
    private GuiButton realmsButton;
    private boolean hasCheckedForRealmsNotification;
    private GuiScreen realmsNotification;
    private int widthCopyright;
    private int widthCopyrightRest;
+   private GuiButton modButton;
+   private GuiScreen modUpdateNotification;

    public GuiMainMenu() {
       this.splashText = "missingno";
       IResource var1 = null;

       try {
@@ -120,13 +127,13 @@
    }

    public boolean doesGuiPauseGame() {
       return false;
    }

-   protected void keyTyped(char var1, int var2) {
+   protected void keyTyped(char var1, int var2) throws IOException {
    }

    public void initGui() {
       this.viewportTexture = new DynamicTexture(256, 256);
       this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
       this.widthCopyright = this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!");
@@ -154,13 +161,13 @@
       this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, var3 + 72 + 12));
       synchronized (this.threadLock) {
          this.openGLWarning1Width = this.fontRenderer.getStringWidth(this.openGLWarning1);
          this.openGLWarning2Width = this.fontRenderer.getStringWidth(this.openGLWarning2);
          int var5 = Math.max(this.openGLWarning1Width, this.openGLWarning2Width);
          this.openGLWarningX1 = (this.width - var5) / 2;
-         this.openGLWarningY1 = this.buttonList.get(0).y - 24;
+         this.openGLWarningY1 = ((GuiButton)this.buttonList.get(0)).y - 24;
          this.openGLWarningX2 = this.openGLWarningX1 + var5;
          this.openGLWarningY2 = this.openGLWarningY1 + 24;
       }

       this.mc.setConnectedToRealms(false);
       if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.hasCheckedForRealmsNotification) {
@@ -170,31 +177,42 @@
       }

       if (this.areRealmsNotificationsEnabled()) {
          this.realmsNotification.setGuiSize(this.width, this.height);
          this.realmsNotification.initGui();
       }
+
+      if (Reflector.NotificationModUpdateScreen_init.exists()) {
+         this.modUpdateNotification = (GuiScreen)Reflector.call(Reflector.NotificationModUpdateScreen_init, new Object[]{this, this.modButton});
+      }
    }

    private void addSingleplayerMultiplayerButtons(int var1, int var2) {
       this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var1, I18n.format("menu.singleplayer")));
       this.buttonList.add(new GuiButton(2, this.width / 2 - 100, var1 + var2 * 1, I18n.format("menu.multiplayer")));
-      this.realmsButton = this.addButton(new GuiButton(14, this.width / 2 - 100, var1 + var2 * 2, I18n.format("menu.online")));
+      if (Reflector.GuiModList_Constructor.exists()) {
+         this.realmsButton = this.addButton(
+            new GuiButton(14, this.width / 2 + 2, var1 + var2 * 2, 98, 20, I18n.format("menu.online").replace("Minecraft", "").trim())
+         );
+         this.buttonList.add(this.modButton = new GuiButton(6, this.width / 2 - 100, var1 + var2 * 2, 98, 20, I18n.format("fml.menu.mods")));
+      } else {
+         this.realmsButton = this.addButton(new GuiButton(14, this.width / 2 - 100, var1 + var2 * 2, I18n.format("menu.online")));
+      }
    }

    private void addDemoButtons(int var1, int var2) {
       this.buttonList.add(new GuiButton(11, this.width / 2 - 100, var1, I18n.format("menu.playdemo")));
       this.buttonResetDemo = this.addButton(new GuiButton(12, this.width / 2 - 100, var1 + var2 * 1, I18n.format("menu.resetdemo")));
       ISaveFormat var3 = this.mc.getSaveLoader();
       WorldInfo var4 = var3.getWorldInfo("Demo_World");
       if (var4 == null) {
          this.buttonResetDemo.enabled = false;
       }
    }

-   protected void actionPerformed(GuiButton var1) {
+   protected void actionPerformed(GuiButton var1) throws IOException {
       if (var1.id == 0) {
          this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
       }

       if (var1.id == 5) {
          this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
@@ -213,12 +231,16 @@
       }

       if (var1.id == 4) {
          this.mc.shutdown();
       }

+      if (var1.id == 6 && Reflector.GuiModList_Constructor.exists()) {
+         this.mc.displayGuiScreen((GuiScreen)Reflector.newInstance(Reflector.GuiModList_Constructor, new Object[]{this}));
+      }
+
       if (var1.id == 11) {
          this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS);
       }

       if (var1.id == 12) {
          ISaveFormat var2 = this.mc.getSaveLoader();
@@ -285,52 +307,62 @@
       GlStateManager.disableCull();
       GlStateManager.depthMask(false);
       GlStateManager.tryBlendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
       );
       byte var6 = 8;
+      int var7 = 64;
+      CustomPanoramaProperties var8 = CustomPanorama.getCustomPanoramaProperties();
+      if (var8 != null) {
+         var7 = var8.getBlur1();
+      }

-      for (int var7 = 0; var7 < 64; var7++) {
+      for (int var9 = 0; var9 < var7; var9++) {
          GlStateManager.pushMatrix();
-         float var8 = (var7 % 8 / 8.0F - 0.5F) / 64.0F;
-         float var9 = (var7 / 8 / 8.0F - 0.5F) / 64.0F;
-         float var10 = 0.0F;
-         GlStateManager.translate(var8, var9, 0.0F);
+         float var10 = (var9 % 8 / 8.0F - 0.5F) / 64.0F;
+         float var11 = (var9 / 8 / 8.0F - 0.5F) / 64.0F;
+         float var12 = 0.0F;
+         GlStateManager.translate(var10, var11, 0.0F);
          GlStateManager.rotate(MathHelper.sin(this.panoramaTimer / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
          GlStateManager.rotate(-this.panoramaTimer * 0.1F, 0.0F, 1.0F, 0.0F);

-         for (int var11 = 0; var11 < 6; var11++) {
+         for (int var13 = 0; var13 < 6; var13++) {
             GlStateManager.pushMatrix();
-            if (var11 == 1) {
+            if (var13 == 1) {
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
             }

-            if (var11 == 2) {
+            if (var13 == 2) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
             }

-            if (var11 == 3) {
+            if (var13 == 3) {
                GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
             }

-            if (var11 == 4) {
+            if (var13 == 4) {
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
             }

-            if (var11 == 5) {
+            if (var13 == 5) {
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
             }

-            this.mc.getTextureManager().bindTexture(TITLE_PANORAMA_PATHS[var11]);
+            ResourceLocation[] var14 = TITLE_PANORAMA_PATHS;
+            if (var8 != null) {
+               var14 = var8.getPanoramaLocations();
+            }
+
+            this.mc.getTextureManager().bindTexture(var14[var13]);
             var5.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
-            int var12 = 255 / (var7 + 1);
-            float var13 = 0.0F;
-            var5.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, var12).endVertex();
-            var5.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, var12).endVertex();
-            var5.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, var12).endVertex();
-            var5.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, var12).endVertex();
+            int var15 = 255 / (var9 + 1);
+            float var16 = 0.0F;
+            var5.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, var15).endVertex();
+            var5.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, var15).endVertex();
+            var5.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, var15).endVertex();
+            var5.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, var15).endVertex();
             var4.draw();
             GlStateManager.popMatrix();
          }

          GlStateManager.popMatrix();
          GlStateManager.colorMask(true, true, true, false);
@@ -359,120 +391,171 @@
       GlStateManager.colorMask(true, true, true, false);
       Tessellator var1 = Tessellator.getInstance();
       BufferBuilder var2 = var1.getBuffer();
       var2.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
       GlStateManager.disableAlpha();
       byte var3 = 3;
+      int var4 = 3;
+      CustomPanoramaProperties var5 = CustomPanorama.getCustomPanoramaProperties();
+      if (var5 != null) {
+         var4 = var5.getBlur2();
+      }

-      for (int var4 = 0; var4 < 3; var4++) {
-         float var5 = 1.0F / (var4 + 1);
-         int var6 = this.width;
-         int var7 = this.height;
-         float var8 = (var4 - 1) / 256.0F;
-         var2.pos(var6, var7, this.zLevel).tex(0.0F + var8, 1.0).color(1.0F, 1.0F, 1.0F, var5).endVertex();
-         var2.pos(var6, 0.0, this.zLevel).tex(1.0F + var8, 1.0).color(1.0F, 1.0F, 1.0F, var5).endVertex();
-         var2.pos(0.0, 0.0, this.zLevel).tex(1.0F + var8, 0.0).color(1.0F, 1.0F, 1.0F, var5).endVertex();
-         var2.pos(0.0, var7, this.zLevel).tex(0.0F + var8, 0.0).color(1.0F, 1.0F, 1.0F, var5).endVertex();
+      for (int var6 = 0; var6 < var4; var6++) {
+         float var7 = 1.0F / (var6 + 1);
+         int var8 = this.width;
+         int var9 = this.height;
+         float var10 = (var6 - 1) / 256.0F;
+         var2.pos(var8, var9, this.e).tex(0.0F + var10, 1.0).color(1.0F, 1.0F, 1.0F, var7).endVertex();
+         var2.pos(var8, 0.0, this.e).tex(1.0F + var10, 1.0).color(1.0F, 1.0F, 1.0F, var7).endVertex();
+         var2.pos(0.0, 0.0, this.e).tex(1.0F + var10, 0.0).color(1.0F, 1.0F, 1.0F, var7).endVertex();
+         var2.pos(0.0, var9, this.e).tex(0.0F + var10, 0.0).color(1.0F, 1.0F, 1.0F, var7).endVertex();
       }

       var1.draw();
       GlStateManager.enableAlpha();
       GlStateManager.colorMask(true, true, true, true);
    }

    private void renderSkybox(int var1, int var2, float var3) {
       this.mc.getFramebuffer().unbindFramebuffer();
       GlStateManager.viewport(0, 0, 256, 256);
       this.drawPanorama(var1, var2, var3);
       this.rotateAndBlurSkybox();
-      this.rotateAndBlurSkybox();
-      this.rotateAndBlurSkybox();
-      this.rotateAndBlurSkybox();
-      this.rotateAndBlurSkybox();
-      this.rotateAndBlurSkybox();
-      this.rotateAndBlurSkybox();
+      int var4 = 3;
+      CustomPanoramaProperties var5 = CustomPanorama.getCustomPanoramaProperties();
+      if (var5 != null) {
+         var4 = var5.getBlur3();
+      }
+
+      for (int var6 = 0; var6 < var4; var6++) {
+         this.rotateAndBlurSkybox();
+         this.rotateAndBlurSkybox();
+      }
+
       this.mc.getFramebuffer().bindFramebuffer(true);
       GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
-      float var4 = 120.0F / (this.width > this.height ? this.width : this.height);
-      float var5 = this.height * var4 / 256.0F;
-      float var6 = this.width * var4 / 256.0F;
-      int var7 = this.width;
-      int var8 = this.height;
-      Tessellator var9 = Tessellator.getInstance();
-      BufferBuilder var10 = var9.getBuffer();
-      var10.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
-      var10.pos(0.0, var8, this.zLevel).tex(0.5F - var5, 0.5F + var6).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
-      var10.pos(var7, var8, this.zLevel).tex(0.5F - var5, 0.5F - var6).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
-      var10.pos(var7, 0.0, this.zLevel).tex(0.5F + var5, 0.5F - var6).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
-      var10.pos(0.0, 0.0, this.zLevel).tex(0.5F + var5, 0.5F + var6).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
-      var9.draw();
+      float var13 = 120.0F / (this.width > this.height ? this.width : this.height);
+      float var7 = this.height * var13 / 256.0F;
+      float var8 = this.width * var13 / 256.0F;
+      int var9 = this.width;
+      int var10 = this.height;
+      Tessellator var11 = Tessellator.getInstance();
+      BufferBuilder var12 = var11.getBuffer();
+      var12.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
+      var12.pos(0.0, var10, this.e).tex(0.5F - var7, 0.5F + var8).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
+      var12.pos(var9, var10, this.e).tex(0.5F - var7, 0.5F - var8).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
+      var12.pos(var9, 0.0, this.e).tex(0.5F + var7, 0.5F - var8).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
+      var12.pos(0.0, 0.0, this.e).tex(0.5F + var7, 0.5F + var8).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
+      var11.draw();
    }

    public void drawScreen(int var1, int var2, float var3) {
       this.panoramaTimer += var3;
       GlStateManager.disableAlpha();
       this.renderSkybox(var1, var2, var3);
       GlStateManager.enableAlpha();
       short var4 = 274;
       int var5 = this.width / 2 - 137;
       byte var6 = 30;
-      this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
-      this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
+      int var7 = -2130706433;
+      int var8 = 16777215;
+      int var9 = 0;
+      int var10 = Integer.MIN_VALUE;
+      CustomPanoramaProperties var11 = CustomPanorama.getCustomPanoramaProperties();
+      if (var11 != null) {
+         var7 = var11.getOverlay1Top();
+         var8 = var11.getOverlay1Bottom();
+         var9 = var11.getOverlay2Top();
+         var10 = var11.getOverlay2Bottom();
+      }
+
+      if (var7 != 0 || var8 != 0) {
+         this.a(0, 0, this.width, this.height, var7, var8);
+      }
+
+      if (var9 != 0 || var10 != 0) {
+         this.a(0, 0, this.width, this.height, var9, var10);
+      }
+
       this.mc.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       if (this.minceraftRoll < 1.0E-4) {
-         this.drawTexturedModalRect(var5 + 0, 30, 0, 0, 99, 44);
-         this.drawTexturedModalRect(var5 + 99, 30, 129, 0, 27, 44);
-         this.drawTexturedModalRect(var5 + 99 + 26, 30, 126, 0, 3, 44);
-         this.drawTexturedModalRect(var5 + 99 + 26 + 3, 30, 99, 0, 26, 44);
-         this.drawTexturedModalRect(var5 + 155, 30, 0, 45, 155, 44);
+         this.b(var5 + 0, 30, 0, 0, 99, 44);
+         this.b(var5 + 99, 30, 129, 0, 27, 44);
+         this.b(var5 + 99 + 26, 30, 126, 0, 3, 44);
+         this.b(var5 + 99 + 26 + 3, 30, 99, 0, 26, 44);
+         this.b(var5 + 155, 30, 0, 45, 155, 44);
       } else {
-         this.drawTexturedModalRect(var5 + 0, 30, 0, 0, 155, 44);
-         this.drawTexturedModalRect(var5 + 155, 30, 0, 45, 155, 44);
+         this.b(var5 + 0, 30, 0, 0, 155, 44);
+         this.b(var5 + 155, 30, 0, 45, 155, 44);
       }

       this.mc.getTextureManager().bindTexture(field_194400_H);
-      drawModalRectWithCustomSizedTexture(var5 + 88, 67, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);
+      a(var5 + 88, 67, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);
+      if (Reflector.ForgeHooksClient_renderMainMenu.exists()) {
+         this.splashText = Reflector.callString(
+            Reflector.ForgeHooksClient_renderMainMenu, new Object[]{this, this.fontRenderer, this.width, this.height, this.splashText}
+         );
+      }
+
       GlStateManager.pushMatrix();
       GlStateManager.translate((float)(this.width / 2 + 90), 70.0F, 0.0F);
       GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
-      float var7 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
-      var7 = var7 * 100.0F / (this.fontRenderer.getStringWidth(this.splashText) + 32);
-      GlStateManager.scale(var7, var7, var7);
-      this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, -256);
+      float var12 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
+      var12 = var12 * 100.0F / (this.fontRenderer.getStringWidth(this.splashText) + 32);
+      GlStateManager.scale(var12, var12, var12);
+      this.a(this.fontRenderer, this.splashText, 0, -8, -256);
       GlStateManager.popMatrix();
-      String var8 = "Minecraft 1.12.2";
+      String var13 = "Minecraft 1.12.2";
       if (this.mc.isDemo()) {
-         var8 = var8 + " Demo";
+         var13 = var13 + " Demo";
       } else {
-         var8 = var8 + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType());
+         var13 = var13 + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType());
       }

-      this.drawString(this.fontRenderer, var8, 2, this.height - 10, -1);
-      this.drawString(this.fontRenderer, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, -1);
+      if (Reflector.FMLCommonHandler_getBrandings.exists()) {
+         Object var14 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
+         List var15 = Lists.reverse((List)Reflector.call(var14, Reflector.FMLCommonHandler_getBrandings, new Object[]{true}));
+
+         for (int var16 = 0; var16 < var15.size(); var16++) {
+            String var17 = (String)var15.get(var16);
+            if (!Strings.isNullOrEmpty(var17)) {
+               this.c(this.fontRenderer, var17, 2, this.height - (10 + var16 * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
+            }
+         }
+      } else {
+         this.c(this.fontRenderer, var13, 2, this.height - 10, -1);
+      }
+
+      this.c(this.fontRenderer, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, -1);
       if (var1 > this.widthCopyrightRest
          && var1 < this.widthCopyrightRest + this.widthCopyright
          && var2 > this.height - 10
          && var2 < this.height
          && Mouse.isInsideWindow()) {
-         drawRect(this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, -1);
+         a(this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, -1);
       }

       if (this.openGLWarning1 != null && !this.openGLWarning1.isEmpty()) {
-         drawRect(this.openGLWarningX1 - 2, this.openGLWarningY1 - 2, this.openGLWarningX2 + 2, this.openGLWarningY2 - 1, 1428160512);
-         this.drawString(this.fontRenderer, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
-         this.drawString(this.fontRenderer, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, this.buttonList.get(0).y - 12, -1);
+         a(this.openGLWarningX1 - 2, this.openGLWarningY1 - 2, this.openGLWarningX2 + 2, this.openGLWarningY2 - 1, 1428160512);
+         this.c(this.fontRenderer, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
+         this.c(this.fontRenderer, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, ((GuiButton)this.buttonList.get(0)).y - 12, -1);
       }

       super.drawScreen(var1, var2, var3);
       if (this.areRealmsNotificationsEnabled()) {
          this.realmsNotification.drawScreen(var1, var2, var3);
       }
+
+      if (this.modUpdateNotification != null) {
+         this.modUpdateNotification.drawScreen(var1, var2, var3);
+      }
    }

-   protected void mouseClicked(int var1, int var2, int var3) {
+   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
       super.mouseClicked(var1, var2, var3);
       synchronized (this.threadLock) {
          if (!this.openGLWarning1.isEmpty()
             && !StringUtils.isNullOrEmpty(this.openGLWarningLink)
             && var1 >= this.openGLWarningX1
             && var1 <= this.openGLWarningX2
 */
