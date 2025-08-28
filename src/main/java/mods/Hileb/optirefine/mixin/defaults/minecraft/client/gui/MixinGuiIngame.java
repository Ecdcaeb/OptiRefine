package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Checked
@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {
    @WrapOperation(method = "renderExpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    public int redirectDrawString(FontRenderer instance, String text, int x, int y, int color, Operation<Integer> original){
        if (color == 0) {
            return original.call(instance, text, x, y, color);
        } else {
            int col = color;
            if (Config.isCustomColors()) {
                 col = CustomColors.getExpBarTextColor(color);
            }
            return original.call(instance, text, x, y, col);
        }
    }

    @WrapMethod(method = "renderVignette")
    public void injectRenderVignette(float p_180480_1_, ScaledResolution p_180480_2_, Operation<Void> original){
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        } else original.call(p_180480_1_, p_180480_2_);
    }
}

/*
+++ net/minecraft/client/gui/GuiIngame.java	Tue Aug 19 14:59:58 2025
@@ -9,12 +9,13 @@
 import java.util.Collection;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
 import javax.annotation.Nullable;
 import net.minecraft.block.material.Material;
+import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.chat.IChatListener;
 import net.minecraft.client.gui.chat.NarratorChatListener;
 import net.minecraft.client.gui.chat.NormalChatListener;
 import net.minecraft.client.gui.chat.OverlayChatListener;
 import net.minecraft.client.gui.inventory.GuiContainer;
@@ -49,16 +50,22 @@
 import net.minecraft.util.FoodStats;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.StringUtils;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.RayTraceResult;
+import net.minecraft.util.math.RayTraceResult.Type;
 import net.minecraft.util.text.ChatType;
 import net.minecraft.util.text.ITextComponent;
 import net.minecraft.util.text.TextFormatting;
 import net.minecraft.world.border.WorldBorder;
+import net.optifine.CustomColors;
+import net.optifine.CustomItems;
+import net.optifine.TextureAnimations;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;

 public class GuiIngame extends Gui {
    private static final ResourceLocation VIGNETTE_TEX_PATH = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation PUMPKIN_BLUR_TEX_PATH = new ResourceLocation("textures/misc/pumpkinblur.png");
    private final Random rand = new Random();
@@ -121,13 +128,13 @@
    public void renderGameOverlay(float var1) {
       ScaledResolution var2 = new ScaledResolution(this.mc);
       int var3 = var2.getScaledWidth();
       int var4 = var2.getScaledHeight();
       FontRenderer var5 = this.getFontRenderer();
       GlStateManager.enableBlend();
-      if (Minecraft.isFancyGraphicsEnabled()) {
+      if (Config.isVignetteEnabled()) {
          this.renderVignette(this.mc.player.getBrightness(), var2);
       } else {
          GlStateManager.enableDepth();
          GlStateManager.tryBlendFuncSeparate(
             GlStateManager.SourceFactor.SRC_ALPHA,
             GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
@@ -155,12 +162,13 @@
       }

       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       this.mc.getTextureManager().bindTexture(ICONS);
       GlStateManager.enableBlend();
       this.renderAttackIndicator(var1, var2);
+      GlStateManager.enableAlpha();
       GlStateManager.tryBlendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
       );
       this.mc.profiler.startSection("bossHealth");
       this.overlayBoss.renderBossHealth();
       this.mc.profiler.endSection();
@@ -308,48 +316,49 @@
       GlStateManager.translate(0.0F, (float)(var4 - 48), 0.0F);
       this.mc.profiler.startSection("chat");
       this.persistantChatGUI.drawChat(this.updateCounter);
       this.mc.profiler.endSection();
       GlStateManager.popMatrix();
       var24 = var16.getObjectiveInDisplaySlot(0);
-      if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown()
-         || this.mc.isIntegratedServerRunning() && this.mc.player.connection.getPlayerInfoMap().size() <= 1 && var24 == null) {
-         this.overlayPlayerList.updatePlayerList(false);
-      } else {
+      if (this.mc.gameSettings.keyBindPlayerList.isKeyDown()
+         && (!this.mc.isIntegratedServerRunning() || this.mc.player.connection.getPlayerInfoMap().size() > 1 || var24 != null)) {
          this.overlayPlayerList.updatePlayerList(true);
          this.overlayPlayerList.renderPlayerlist(var3, var16, var24);
+      } else {
+         this.overlayPlayerList.updatePlayerList(false);
       }

       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       GlStateManager.disableLighting();
       GlStateManager.enableAlpha();
    }

    private void renderAttackIndicator(float var1, ScaledResolution var2) {
       GameSettings var3 = this.mc.gameSettings;
       if (var3.thirdPersonView == 0) {
          if (this.mc.playerController.isSpectator() && this.mc.pointedEntity == null) {
             RayTraceResult var4 = this.mc.objectMouseOver;
-            if (var4 == null || var4.typeOfHit != RayTraceResult.Type.BLOCK) {
+            if (var4 == null || var4.typeOfHit != Type.BLOCK) {
                return;
             }

             BlockPos var5 = var4.getBlockPos();
-            if (!this.mc.world.getBlockState(var5).getBlock().hasTileEntity() || !(this.mc.world.getTileEntity(var5) instanceof IInventory)) {
+            IBlockState var6 = this.mc.world.getBlockState(var5);
+            if (!ReflectorForge.blockHasTileEntity(var6) || !(this.mc.world.getTileEntity(var5) instanceof IInventory)) {
                return;
             }
          }

          int var11 = var2.getScaledWidth();
          int var12 = var2.getScaledHeight();
          if (var3.showDebugInfo && !var3.hideGUI && !this.mc.player.hasReducedDebug() && !var3.reducedDebugInfo) {
             GlStateManager.pushMatrix();
             GlStateManager.translate((float)(var11 / 2), (float)(var12 / 2), this.zLevel);
-            Entity var13 = this.mc.getRenderViewEntity();
-            GlStateManager.rotate(var13.prevRotationPitch + (var13.rotationPitch - var13.prevRotationPitch) * var1, -1.0F, 0.0F, 0.0F);
-            GlStateManager.rotate(var13.prevRotationYaw + (var13.rotationYaw - var13.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
+            Entity var14 = this.mc.getRenderViewEntity();
+            GlStateManager.rotate(var14.prevRotationPitch + (var14.rotationPitch - var14.prevRotationPitch) * var1, -1.0F, 0.0F, 0.0F);
+            GlStateManager.rotate(var14.prevRotationYaw + (var14.rotationYaw - var14.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
             GlStateManager.scale(-1.0F, -1.0F, -1.0F);
             OpenGlHelper.renderDirections(10);
             GlStateManager.popMatrix();
          } else {
             GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
@@ -357,25 +366,25 @@
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
             );
             GlStateManager.enableAlpha();
             this.drawTexturedModalRect(var11 / 2 - 7, var12 / 2 - 7, 0, 0, 16, 16);
             if (this.mc.gameSettings.attackIndicator == 1) {
-               float var6 = this.mc.player.getCooledAttackStrength(0.0F);
+               float var13 = this.mc.player.getCooledAttackStrength(0.0F);
                boolean var7 = false;
-               if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof EntityLivingBase && var6 >= 1.0F) {
+               if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof EntityLivingBase && var13 >= 1.0F) {
                   var7 = this.mc.player.getCooldownPeriod() > 5.0F;
                   var7 &= ((EntityLivingBase)this.mc.pointedEntity).isEntityAlive();
                }

                int var8 = var12 / 2 - 7 + 16;
                int var9 = var11 / 2 - 8;
                if (var7) {
                   this.drawTexturedModalRect(var9, var8, 68, 94, 16, 16);
-               } else if (var6 < 1.0F) {
-                  int var10 = (int)(var6 * 17.0F);
+               } else if (var13 < 1.0F) {
+                  int var10 = (int)(var13 * 17.0F);
                   this.drawTexturedModalRect(var9, var8, 36, 94, 16, 4);
                   this.drawTexturedModalRect(var9, var8, 52, 94, var10, 4);
                }
             }
          }
       }
@@ -388,44 +397,62 @@
          GlStateManager.enableBlend();
          int var3 = 0;
          int var4 = 0;

          for (PotionEffect var6 : Ordering.natural().reverse().sortedCopy(var2)) {
             Potion var7 = var6.getPotion();
-            if (var7.hasStatusIcon() && var6.doesShowParticles()) {
-               int var8 = var1.getScaledWidth();
-               byte var9 = 1;
+            boolean var8 = var7.hasStatusIcon();
+            if (Reflector.ForgePotion_shouldRenderHUD.exists()) {
+               if (!Reflector.callBoolean(var7, Reflector.ForgePotion_shouldRenderHUD, new Object[]{var6})) {
+                  continue;
+               }
+
+               this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
+               var8 = true;
+            }
+
+            if (var8 && var6.doesShowParticles()) {
+               int var9 = var1.getScaledWidth();
+               byte var10 = 1;
                if (this.mc.isDemo()) {
-                  var9 += 15;
+                  var10 += 15;
                }

-               int var10 = var7.getStatusIconIndex();
+               int var11 = var7.getStatusIconIndex();
                if (var7.isBeneficial()) {
                   var3++;
-                  var8 -= 25 * var3;
+                  var9 -= 25 * var3;
                } else {
                   var4++;
-                  var8 -= 25 * var4;
-                  var9 += 26;
+                  var9 -= 25 * var4;
+                  var10 += 26;
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-               float var11 = 1.0F;
+               float var12 = 1.0F;
                if (var6.getIsAmbient()) {
-                  this.drawTexturedModalRect(var8, var9, 165, 166, 24, 24);
+                  this.drawTexturedModalRect(var9, var10, 165, 166, 24, 24);
                } else {
-                  this.drawTexturedModalRect(var8, var9, 141, 166, 24, 24);
+                  this.drawTexturedModalRect(var9, var10, 141, 166, 24, 24);
                   if (var6.getDuration() <= 200) {
-                     int var12 = 10 - var6.getDuration() / 20;
-                     var11 = MathHelper.clamp(var6.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F)
-                        + MathHelper.cos(var6.getDuration() * (float) Math.PI / 5.0F) * MathHelper.clamp(var12 / 10.0F * 0.25F, 0.0F, 0.25F);
+                     int var13 = 10 - var6.getDuration() / 20;
+                     var12 = MathHelper.clamp(var6.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F)
+                        + MathHelper.cos(var6.getDuration() * (float) Math.PI / 5.0F) * MathHelper.clamp(var13 / 10.0F * 0.25F, 0.0F, 0.25F);
                   }
                }

-               GlStateManager.color(1.0F, 1.0F, 1.0F, var11);
-               this.drawTexturedModalRect(var8 + 3, var9 + 3, var10 % 8 * 18, 198 + var10 / 8 * 18, 18, 18);
+               GlStateManager.color(1.0F, 1.0F, 1.0F, var12);
+               if (Reflector.ForgePotion_renderHUDEffect.exists()) {
+                  if (var7.hasStatusIcon()) {
+                     this.drawTexturedModalRect(var9 + 3, var10 + 3, var11 % 8 * 18, 198 + var11 / 8 * 18, 18, 18);
+                  }
+
+                  Reflector.call(var7, Reflector.ForgePotion_renderHUDEffect, new Object[]{var6, this, var9, Integer.valueOf(var10), this.zLevel, var12});
+               } else {
+                  this.drawTexturedModalRect(var9 + 3, var10 + 3, var11 % 8 * 18, 198 + var11 / 8 * 18, 18, 18);
+               }
             }
          }
       }
    }

    protected void renderHotbar(ScaledResolution var1, float var2) {
@@ -457,26 +484,30 @@
             GlStateManager.SourceFactor.SRC_ALPHA,
             GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
             GlStateManager.SourceFactor.ONE,
             GlStateManager.DestFactor.ZERO
          );
          RenderHelper.enableGUIStandardItemLighting();
+         CustomItems.setRenderOffHand(false);

          for (int var10 = 0; var10 < 9; var10++) {
             int var11 = var6 - 90 + var10 * 20 + 2;
             int var12 = var1.getScaledHeight() - 16 - 3;
-            this.renderHotbarItem(var11, var12, var2, var3, var3.inventory.mainInventory.get(var10));
+            this.renderHotbarItem(var11, var12, var2, var3, (ItemStack)var3.inventory.mainInventory.get(var10));
          }

          if (!var4.isEmpty()) {
+            CustomItems.setRenderOffHand(true);
             int var14 = var1.getScaledHeight() - 16 - 3;
             if (var5 == EnumHandSide.LEFT) {
                this.renderHotbarItem(var6 - 91 - 26, var14, var2, var3, var4);
             } else {
                this.renderHotbarItem(var6 + 91 + 10, var14, var2, var3, var4);
             }
+
+            CustomItems.setRenderOffHand(false);
          }

          if (this.mc.gameSettings.attackIndicator == 2) {
             float var15 = this.mc.player.getCooledAttackStrength(0.0F);
             if (var15 < 1.0F) {
                int var16 = var1.getScaledHeight() - 20;
@@ -528,20 +559,25 @@
          }
       }

       this.mc.profiler.endSection();
       if (this.mc.player.experienceLevel > 0) {
          this.mc.profiler.startSection("expLevel");
-         String var7 = "" + this.mc.player.experienceLevel;
-         int var8 = (var1.getScaledWidth() - this.getFontRenderer().getStringWidth(var7)) / 2;
-         int var9 = var1.getScaledHeight() - 31 - 4;
-         this.getFontRenderer().drawString(var7, var8 + 1, var9, 0);
-         this.getFontRenderer().drawString(var7, var8 - 1, var9, 0);
-         this.getFontRenderer().drawString(var7, var8, var9 + 1, 0);
-         this.getFontRenderer().drawString(var7, var8, var9 - 1, 0);
-         this.getFontRenderer().drawString(var7, var8, var9, 8453920);
+         int var8 = 8453920;
+         if (Config.isCustomColors()) {
+            var8 = CustomColors.getExpBarTextColor(var8);
+         }
+
+         String var9 = "" + this.mc.player.experienceLevel;
+         int var10 = (var1.getScaledWidth() - this.getFontRenderer().getStringWidth(var9)) / 2;
+         int var7 = var1.getScaledHeight() - 31 - 4;
+         this.getFontRenderer().drawString(var9, var10 + 1, var7, 0);
+         this.getFontRenderer().drawString(var9, var10 - 1, var7, 0);
+         this.getFontRenderer().drawString(var9, var10, var7 + 1, 0);
+         this.getFontRenderer().drawString(var9, var10, var7 - 1, 0);
+         this.getFontRenderer().drawString(var9, var10, var7, var8);
          this.mc.profiler.endSection();
       }
    }

    public void renderSelectedItem(ScaledResolution var1) {
       this.mc.profiler.startSection("selectedItemName");
@@ -599,49 +635,49 @@
       Collection var4 = var3.getSortedScores(var1);
       ArrayList var5 = Lists.newArrayList(Iterables.filter(var4, new Predicate<Score>() {
          public boolean apply(@Nullable Score var1) {
             return var1.getPlayerName() != null && !var1.getPlayerName().startsWith("#");
          }
       }));
-      ArrayList var21;
+      ArrayList var20;
       if (var5.size() > 15) {
-         var21 = Lists.newArrayList(Iterables.skip(var5, var4.size() - 15));
+         var20 = Lists.newArrayList(Iterables.skip(var5, var4.size() - 15));
       } else {
-         var21 = var5;
+         var20 = var5;
       }

       int var6 = this.getFontRenderer().getStringWidth(var1.getDisplayName());

-      for (Score var8 : var21) {
+      for (Score var8 : var20) {
          ScorePlayerTeam var9 = var3.getPlayersTeam(var8.getPlayerName());
          String var10 = ScorePlayerTeam.formatPlayerName(var9, var8.getPlayerName()) + ": " + TextFormatting.RED + var8.getScorePoints();
          var6 = Math.max(var6, this.getFontRenderer().getStringWidth(var10));
       }

-      int var22 = var21.size() * this.getFontRenderer().FONT_HEIGHT;
-      int var23 = var2.getScaledHeight() / 2 + var22 / 3;
-      byte var24 = 3;
-      int var25 = var2.getScaledWidth() - var6 - 3;
+      int var21 = var20.size() * this.getFontRenderer().FONT_HEIGHT;
+      int var22 = var2.getScaledHeight() / 2 + var21 / 3;
+      byte var23 = 3;
+      int var24 = var2.getScaledWidth() - var6 - 3;
       int var11 = 0;

-      for (Score var13 : var21) {
+      for (Score var13 : var20) {
          var11++;
          ScorePlayerTeam var14 = var3.getPlayersTeam(var13.getPlayerName());
          String var15 = ScorePlayerTeam.formatPlayerName(var14, var13.getPlayerName());
          String var16 = TextFormatting.RED + "" + var13.getScorePoints();
-         int var18 = var23 - var11 * this.getFontRenderer().FONT_HEIGHT;
-         int var19 = var2.getScaledWidth() - 3 + 2;
-         drawRect(var25 - 2, var18, var19, var18 + this.getFontRenderer().FONT_HEIGHT, 1342177280);
-         this.getFontRenderer().drawString(var15, var25, var18, 553648127);
-         this.getFontRenderer().drawString(var16, var19 - this.getFontRenderer().getStringWidth(var16), var18, 553648127);
-         if (var11 == var21.size()) {
-            String var20 = var1.getDisplayName();
-            drawRect(var25 - 2, var18 - this.getFontRenderer().FONT_HEIGHT - 1, var19, var18 - 1, 1610612736);
-            drawRect(var25 - 2, var18 - 1, var19, var18, 1342177280);
+         int var17 = var22 - var11 * this.getFontRenderer().FONT_HEIGHT;
+         int var18 = var2.getScaledWidth() - 3 + 2;
+         drawRect(var24 - 2, var17, var18, var17 + this.getFontRenderer().FONT_HEIGHT, 1342177280);
+         this.getFontRenderer().drawString(var15, var24, var17, 553648127);
+         this.getFontRenderer().drawString(var16, var18 - this.getFontRenderer().getStringWidth(var16), var17, 553648127);
+         if (var11 == var20.size()) {
+            String var19 = var1.getDisplayName();
+            drawRect(var24 - 2, var17 - this.getFontRenderer().FONT_HEIGHT - 1, var18, var17 - 1, 1610612736);
+            drawRect(var24 - 2, var17 - 1, var18, var17, 1342177280);
             this.getFontRenderer()
-               .drawString(var20, var25 + var6 / 2 - this.getFontRenderer().getStringWidth(var20) / 2, var18 - this.getFontRenderer().FONT_HEIGHT, 553648127);
+               .drawString(var19, var24 + var6 / 2 - this.getFontRenderer().getStringWidth(var19) / 2, var17 - this.getFontRenderer().FONT_HEIGHT, 553648127);
          }
       }
    }

    private void renderPlayerStats(ScaledResolution var1) {
       if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
@@ -876,51 +912,64 @@
       GlStateManager.enableDepth();
       GlStateManager.enableAlpha();
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderVignette(float var1, ScaledResolution var2) {
-      var1 = 1.0F - var1;
-      var1 = MathHelper.clamp(var1, 0.0F, 1.0F);
-      WorldBorder var3 = this.mc.world.getWorldBorder();
-      float var4 = (float)var3.getClosestDistance(this.mc.player);
-      double var5 = Math.min(var3.getResizeSpeed() * var3.getWarningTime() * 1000.0, Math.abs(var3.getTargetSize() - var3.getDiameter()));
-      double var7 = Math.max((double)var3.getWarningDistance(), var5);
-      if (var4 < var7) {
-         var4 = 1.0F - (float)(var4 / var7);
+      if (!Config.isVignetteEnabled()) {
+         GlStateManager.enableDepth();
+         GlStateManager.tryBlendFuncSeparate(
+            GlStateManager.SourceFactor.SRC_ALPHA,
+            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
+            GlStateManager.SourceFactor.ONE,
+            GlStateManager.DestFactor.ZERO
+         );
       } else {
-         var4 = 0.0F;
-      }
+         var1 = 1.0F - var1;
+         var1 = MathHelper.clamp(var1, 0.0F, 1.0F);
+         WorldBorder var3 = this.mc.world.getWorldBorder();
+         float var4 = (float)var3.getClosestDistance(this.mc.player);
+         double var5 = Math.min(var3.getResizeSpeed() * var3.getWarningTime() * 1000.0, Math.abs(var3.getTargetSize() - var3.getDiameter()));
+         double var7 = Math.max((double)var3.getWarningDistance(), var5);
+         if (var4 < var7) {
+            var4 = 1.0F - (float)(var4 / var7);
+         } else {
+            var4 = 0.0F;
+         }

-      this.prevVignetteBrightness = (float)(this.prevVignetteBrightness + (var1 - this.prevVignetteBrightness) * 0.01);
-      GlStateManager.disableDepth();
-      GlStateManager.depthMask(false);
-      GlStateManager.tryBlendFuncSeparate(
-         GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
-      );
-      if (var4 > 0.0F) {
-         GlStateManager.color(0.0F, var4, var4, 1.0F);
-      } else {
-         GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
-      }
+         this.prevVignetteBrightness = (float)(this.prevVignetteBrightness + (var1 - this.prevVignetteBrightness) * 0.01);
+         GlStateManager.disableDepth();
+         GlStateManager.depthMask(false);
+         GlStateManager.tryBlendFuncSeparate(
+            GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
+         );
+         if (var4 > 0.0F) {
+            GlStateManager.color(0.0F, var4, var4, 1.0F);
+         } else {
+            GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
+         }

-      this.mc.getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
-      Tessellator var9 = Tessellator.getInstance();
-      BufferBuilder var10 = var9.getBuffer();
-      var10.begin(7, DefaultVertexFormats.POSITION_TEX);
-      var10.pos(0.0, var2.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
-      var10.pos(var2.getScaledWidth(), var2.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
-      var10.pos(var2.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
-      var10.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
-      var9.draw();
-      GlStateManager.depthMask(true);
-      GlStateManager.enableDepth();
-      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-      GlStateManager.tryBlendFuncSeparate(
-         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
-      );
+         this.mc.getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
+         Tessellator var9 = Tessellator.getInstance();
+         BufferBuilder var10 = var9.getBuffer();
+         var10.begin(7, DefaultVertexFormats.POSITION_TEX);
+         var10.pos(0.0, var2.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
+         var10.pos(var2.getScaledWidth(), var2.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
+         var10.pos(var2.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
+         var10.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
+         var9.draw();
+         GlStateManager.depthMask(true);
+         GlStateManager.enableDepth();
+         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+         GlStateManager.tryBlendFuncSeparate(
+            GlStateManager.SourceFactor.SRC_ALPHA,
+            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
+            GlStateManager.SourceFactor.ONE,
+            GlStateManager.DestFactor.ZERO
+         );
+      }
    }

    private void renderPortal(float var1, ScaledResolution var2) {
       if (var1 < 1.0F) {
          var1 *= var1;
          var1 *= var1;
@@ -932,13 +981,13 @@
       GlStateManager.depthMask(false);
       GlStateManager.tryBlendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
       );
       GlStateManager.color(1.0F, 1.0F, 1.0F, var1);
       this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
-      TextureAtlasSprite var3 = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.PORTAL.getDefaultState());
+      TextureAtlasSprite var3 = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.PORTAL.t());
       float var4 = var3.getMinU();
       float var5 = var3.getMinV();
       float var6 = var3.getMaxU();
       float var7 = var3.getMaxV();
       Tessellator var8 = Tessellator.getInstance();
       BufferBuilder var9 = var8.getBuffer();
@@ -972,12 +1021,16 @@

          this.itemRenderer.renderItemOverlays(this.mc.fontRenderer, var5, var1, var2);
       }
    }

    public void updateTick() {
+      if (this.mc.world == null) {
+         TextureAnimations.updateAnimations();
+      }
+
       if (this.overlayMessageTime > 0) {
          this.overlayMessageTime--;
       }

       if (this.titlesTimer > 0) {
          this.titlesTimer--;
 */