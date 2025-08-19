package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderItemFrame.class)
public abstract class MixinRenderItemFrame {

    @Shadow @Final
    private Minecraft mc;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static double itemRenderDistanceSq = 4096.0;

    @ModifyExpressionValue(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    public boolean extraRenderCondition(boolean original, @Local(argsOnly = true) EntityItemFrame itemFrame){
        if (!original) {
            if (!this.isRenderItem(itemFrame)) {
                return true;
            }

            if (!Config.zoomMode) {
                Entity player = this.mc.player;
                return itemFrame.getDistanceSq(player.posX, player.posY, player.posZ) > itemRenderDistanceSq;
            }
            return false;
        } else return true;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean isRenderItem(EntityItemFrame itemFrame) {
        if (Shaders.isShadowPass) {
            return false;
        } else {
            if (!Config.zoomMode) {
                Entity viewEntity = this.mc.getRenderViewEntity();
                double distSq = itemFrame.getDistanceSq(viewEntity.posX, viewEntity.posY, viewEntity.posZ);
                return !(distSq > itemRenderDistanceSq);
            }

            return true;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private static void updateItemRenderDistance() {
        Minecraft mc = Config.getMinecraft();
        double fov = Config.limit(mc.gameSettings.fovSetting, 1.0F, 120.0F);
        double itemRenderDistance = Math.max(6.0 * mc.displayHeight / fov, 16.0);
        itemRenderDistanceSq = itemRenderDistance * itemRenderDistance;
    }
}

/*
+++ net/minecraft/client/renderer/entity/RenderItemFrame.java	Tue Aug 19 14:59:58 2025
@@ -1,51 +1,58 @@
 package net.minecraft.client.renderer.entity;

 import javax.annotation.Nullable;
 import net.minecraft.client.Minecraft;
+import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.client.renderer.BlockRendererDispatcher;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.RenderHelper;
 import net.minecraft.client.renderer.RenderItem;
 import net.minecraft.client.renderer.block.model.IBakedModel;
-import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
 import net.minecraft.client.renderer.block.model.ModelManager;
 import net.minecraft.client.renderer.block.model.ModelResourceLocation;
+import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
 import net.minecraft.client.renderer.texture.TextureMap;
+import net.minecraft.entity.Entity;
 import net.minecraft.entity.item.EntityItemFrame;
 import net.minecraft.init.Items;
+import net.minecraft.item.ItemMap;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.world.storage.MapData;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;
+import net.optifine.shaders.Shaders;

 public class RenderItemFrame extends Render<EntityItemFrame> {
    private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
    private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
    private final RenderItem itemRenderer;
+   private static double itemRenderDistanceSq = 4096.0;

    public RenderItemFrame(RenderManager var1, RenderItem var2) {
       super(var1);
       this.itemRenderer = var2;
    }

    public void doRender(EntityItemFrame var1, double var2, double var4, double var6, float var8, float var9) {
       GlStateManager.pushMatrix();
-      BlockPos var10 = var1.getHangingPosition();
+      BlockPos var10 = var1.q();
       double var11 = var10.getX() - var1.posX + var2;
       double var13 = var10.getY() - var1.posY + var4;
       double var15 = var10.getZ() - var1.posZ + var6;
       GlStateManager.translate(var11 + 0.5, var13 + 0.5, var15 + 0.5);
       GlStateManager.rotate(180.0F - var1.rotationYaw, 0.0F, 1.0F, 0.0F);
       this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
       BlockRendererDispatcher var17 = this.mc.getBlockRendererDispatcher();
       ModelManager var18 = var17.getBlockModelShapes().getModelManager();
       IBakedModel var19;
-      if (var1.getDisplayedItem().getItem() == Items.FILLED_MAP) {
+      if (var1.getDisplayedItem().getItem() instanceof ItemMap) {
          var19 = var18.getModel(this.mapModel);
       } else {
          var19 = var18.getModel(this.itemFrameModel);
       }

       GlStateManager.pushMatrix();
@@ -62,46 +69,60 @@
       }

       GlStateManager.popMatrix();
       GlStateManager.translate(0.0F, 0.0F, 0.4375F);
       this.renderItem(var1);
       GlStateManager.popMatrix();
-      this.renderName(var1, var2 + var1.facingDirection.getXOffset() * 0.3F, var4 - 0.25, var6 + var1.facingDirection.getZOffset() * 0.3F);
+      this.renderName(var1, var2 + var1.EMPTY_EQUIPMENT.getXOffset() * 0.3F, var4 - 0.25, var6 + var1.EMPTY_EQUIPMENT.getZOffset() * 0.3F);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(EntityItemFrame var1) {
       return null;
    }

    private void renderItem(EntityItemFrame var1) {
       ItemStack var2 = var1.getDisplayedItem();
       if (!var2.isEmpty()) {
+         if (!this.isRenderItem(var1)) {
+            return;
+         }
+
+         if (!Config.zoomMode) {
+            EntityPlayerSP var3 = this.mc.player;
+            double var4 = var1.getDistanceSq(var3.posX, var3.posY, var3.posZ);
+            if (var4 > 4096.0) {
+               return;
+            }
+         }
+
          GlStateManager.pushMatrix();
          GlStateManager.disableLighting();
-         boolean var3 = var2.getItem() == Items.FILLED_MAP;
-         int var4 = var3 ? var1.getRotation() % 4 * 2 : var1.getRotation();
-         GlStateManager.rotate(var4 * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
-         if (var3) {
-            this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
-            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
-            float var5 = 0.0078125F;
-            GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
-            GlStateManager.translate(-64.0F, -64.0F, 0.0F);
-            MapData var6 = Items.FILLED_MAP.getMapData(var2, var1.world);
-            GlStateManager.translate(0.0F, 0.0F, -1.0F);
-            if (var6 != null) {
-               this.mc.entityRenderer.getMapItemRenderer().renderMap(var6, true);
+         boolean var7 = var2.getItem() instanceof ItemMap;
+         int var8 = var7 ? var1.getRotation() % 4 * 2 : var1.getRotation();
+         GlStateManager.rotate(var8 * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
+         if (!Reflector.postForgeBusEvent(Reflector.RenderItemInFrameEvent_Constructor, new Object[]{var1, this})) {
+            if (var7) {
+               this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
+               GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
+               float var5 = 0.0078125F;
+               GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
+               GlStateManager.translate(-64.0F, -64.0F, 0.0F);
+               MapData var6 = ReflectorForge.getMapData(Items.FILLED_MAP, var2, var1.world);
+               GlStateManager.translate(0.0F, 0.0F, -1.0F);
+               if (var6 != null) {
+                  this.mc.entityRenderer.getMapItemRenderer().renderMap(var6, true);
+               }
+            } else {
+               GlStateManager.scale(0.5F, 0.5F, 0.5F);
+               GlStateManager.pushAttrib();
+               RenderHelper.enableStandardItemLighting();
+               this.itemRenderer.renderItem(var2, TransformType.FIXED);
+               RenderHelper.disableStandardItemLighting();
+               GlStateManager.popAttrib();
             }
-         } else {
-            GlStateManager.scale(0.5F, 0.5F, 0.5F);
-            GlStateManager.pushAttrib();
-            RenderHelper.enableStandardItemLighting();
-            this.itemRenderer.renderItem(var2, ItemCameraTransforms.TransformType.FIXED);
-            RenderHelper.disableStandardItemLighting();
-            GlStateManager.popAttrib();
          }

          GlStateManager.enableLighting();
          GlStateManager.popMatrix();
       }
    }
@@ -110,13 +131,36 @@
       if (Minecraft.isGuiEnabled()
          && !var1.getDisplayedItem().isEmpty()
          && var1.getDisplayedItem().hasDisplayName()
          && this.renderManager.pointedEntity == var1) {
          double var8 = var1.getDistanceSq(this.renderManager.renderViewEntity);
          float var10 = var1.isSneaking() ? 32.0F : 64.0F;
-         if (!(var8 >= var10 * var10)) {
+         if (var8 < var10 * var10) {
             String var11 = var1.getDisplayedItem().getDisplayName();
             this.renderLivingLabel(var1, var11, var2, var4, var6, 64);
          }
       }
+   }
+
+   private boolean isRenderItem(EntityItemFrame var1) {
+      if (Shaders.isShadowPass) {
+         return false;
+      } else {
+         if (!Config.zoomMode) {
+            Entity var2 = this.mc.getRenderViewEntity();
+            double var3 = var1.getDistanceSq(var2.posX, var2.posY, var2.posZ);
+            if (var3 > itemRenderDistanceSq) {
+               return false;
+            }
+         }
+
+         return true;
+      }
+   }
+
+   public static void updateItemRenderDistance() {
+      Minecraft var0 = Config.getMinecraft();
+      double var1 = Config.limit(var0.gameSettings.fovSetting, 1.0F, 120.0F);
+      double var3 = Math.max(6.0 * var0.displayHeight / var1, 16.0);
+      itemRenderDistanceSq = var3 * var3;
    }
 }
 */
