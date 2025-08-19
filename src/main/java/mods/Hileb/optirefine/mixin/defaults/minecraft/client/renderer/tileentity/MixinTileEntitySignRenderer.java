package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntitySign;
import net.optifine.CustomColors;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntitySignRenderer.class)
public abstract class MixinTileEntitySignRenderer {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static double textRenderDistanceSq = 4096.0;

    @Redirect(method = "render(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    public int injectdrawStringColor(FontRenderer instance, String p_78276_1_, int p_78276_2_, int p_78276_3_, int p_78276_4_){
        if (Config.isCustomColors()) {
            return instance.drawString(p_78276_1_, p_78276_2_, p_78276_3_, CustomColors.getSignTextColor(p_78276_4_));
        } else return instance.drawString(p_78276_1_, p_78276_2_, p_78276_3_, p_78276_4_);
    }


    @Definition(id = "p_192841_9_", local = @Local(ordinal = 6))
    @Expression("p_192841_9_ < 0")
    @ModifyExpressionValue(method = "render(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    public boolean disableTextRendering(boolean o, @Local(ordinal = 1) TileEntitySign entity){
        return o && isRenderText(entity);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static boolean isRenderText(TileEntitySign tileEntity) {
        if (Shaders.isShadowPass) {
            return false;
        } else {
            if (!Config.zoomMode && tileEntity.lineBeingEdited < 0) {
                Entity viewEntity = Config.getMinecraft().getRenderViewEntity();
                return !(viewEntity != null && tileEntity.getDistanceSq(viewEntity.posX, viewEntity.posY, viewEntity.posZ) > textRenderDistanceSq);
            }
            return true;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private static void updateTextRenderDistance() {
        Minecraft mc = Config.getMinecraft();
        double fov = Config.limit(mc.gameSettings.fovSetting, 1.0F, 120.0F);
        double textRenderDistance = Math.max(1.5 * mc.displayHeight / fov, 16.0);
        textRenderDistanceSq = textRenderDistance * textRenderDistance;
    }

}

/*
@@ -1,34 +1,39 @@
 package net.minecraft.client.renderer.tileentity;

 import java.util.List;
 import net.minecraft.block.Block;
+import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.FontRenderer;
 import net.minecraft.client.gui.GuiUtilRenderComponents;
 import net.minecraft.client.model.ModelSign;
 import net.minecraft.client.renderer.GlStateManager;
+import net.minecraft.entity.Entity;
 import net.minecraft.init.Blocks;
 import net.minecraft.tileentity.TileEntitySign;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.text.ITextComponent;
+import net.optifine.CustomColors;
+import net.optifine.shaders.Shaders;

 public class TileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign> {
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    private final ModelSign model = new ModelSign();
+   private static double textRenderDistanceSq = 4096.0;

    public void render(TileEntitySign var1, double var2, double var4, double var6, float var8, int var9, float var10) {
-      Block var11 = var1.getBlockType();
+      Block var11 = var1.x();
       GlStateManager.pushMatrix();
       float var12 = 0.6666667F;
       if (var11 == Blocks.STANDING_SIGN) {
          GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 0.5F, (float)var6 + 0.5F);
-         float var13 = var1.getBlockMetadata() * 360 / 16.0F;
+         float var13 = var1.v() * 360 / 16.0F;
          GlStateManager.rotate(-var13, 0.0F, 1.0F, 0.0F);
          this.model.signStick.showModel = true;
       } else {
-         int var20 = var1.getBlockMetadata();
+         int var20 = var1.v();
          float var14 = 0.0F;
          if (var20 == 2) {
             var14 = 180.0F;
          }

          if (var20 == 4) {
@@ -58,30 +63,36 @@

       GlStateManager.enableRescaleNormal();
       GlStateManager.pushMatrix();
       GlStateManager.scale(0.6666667F, -0.6666667F, -0.6666667F);
       this.model.renderSign();
       GlStateManager.popMatrix();
-      FontRenderer var21 = this.getFontRenderer();
-      float var22 = 0.010416667F;
-      GlStateManager.translate(0.0F, 0.33333334F, 0.046666667F);
-      GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
-      GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
-      GlStateManager.depthMask(false);
-      boolean var15 = false;
-      if (var9 < 0) {
-         for (int var16 = 0; var16 < var1.signText.length; var16++) {
-            if (var1.signText[var16] != null) {
-               ITextComponent var17 = var1.signText[var16];
-               List var18 = GuiUtilRenderComponents.splitText(var17, 90, var21, false, true);
-               String var19 = var18 != null && !var18.isEmpty() ? ((ITextComponent)var18.get(0)).getFormattedText() : "";
-               if (var16 == var1.lineBeingEdited) {
-                  var19 = "> " + var19 + " <";
-                  var21.drawString(var19, -var21.getStringWidth(var19) / 2, var16 * 10 - var1.signText.length * 5, 0);
-               } else {
-                  var21.drawString(var19, -var21.getStringWidth(var19) / 2, var16 * 10 - var1.signText.length * 5, 0);
+      if (isRenderText(var1)) {
+         FontRenderer var21 = this.getFontRenderer();
+         float var22 = 0.010416667F;
+         GlStateManager.translate(0.0F, 0.33333334F, 0.046666667F);
+         GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
+         GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
+         GlStateManager.depthMask(false);
+         int var15 = 0;
+         if (Config.isCustomColors()) {
+            var15 = CustomColors.getSignTextColor(var15);
+         }
+
+         if (var9 < 0) {
+            for (int var16 = 0; var16 < var1.signText.length; var16++) {
+               if (var1.signText[var16] != null) {
+                  ITextComponent var17 = var1.signText[var16];
+                  List var18 = GuiUtilRenderComponents.splitText(var17, 90, var21, false, true);
+                  String var19 = var18 != null && !var18.isEmpty() ? ((ITextComponent)var18.get(0)).getFormattedText() : "";
+                  if (var16 == var1.lineBeingEdited) {
+                     var19 = "> " + var19 + " <";
+                     var21.drawString(var19, -var21.getStringWidth(var19) / 2, var16 * 10 - var1.signText.length * 5, var15);
+                  } else {
+                     var21.drawString(var19, -var21.getStringWidth(var19) / 2, var16 * 10 - var1.signText.length * 5, var15);
+                  }
                }
             }
          }
       }

       GlStateManager.depthMask(true);
@@ -89,8 +100,31 @@
       GlStateManager.popMatrix();
       if (var9 >= 0) {
          GlStateManager.matrixMode(5890);
          GlStateManager.popMatrix();
          GlStateManager.matrixMode(5888);
       }
+   }
+
+   private static boolean isRenderText(TileEntitySign var0) {
+      if (Shaders.isShadowPass) {
+         return false;
+      } else {
+         if (!Config.zoomMode && var0.lineBeingEdited < 0) {
+            Entity var1 = Config.getMinecraft().getRenderViewEntity();
+            double var2 = var0.a(var1.posX, var1.posY, var1.posZ);
+            if (var2 > textRenderDistanceSq) {
+               return false;
+            }
+         }
+
+         return true;
+      }
+   }
+
+   public static void updateTextRenderDistance() {
+      Minecraft var0 = Config.getMinecraft();
+      double var1 = Config.limit(var0.gameSettings.fovSetting, 1.0F, 120.0F);
+      double var3 = Math.max(1.5 * var0.displayHeight / var1, 16.0);
+      textRenderDistanceSq = var3 * var3;
    }
 }
 */
