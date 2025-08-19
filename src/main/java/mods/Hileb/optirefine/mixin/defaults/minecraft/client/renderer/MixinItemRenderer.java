package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

public class MixinItemRenderer {
}

/*
+++ net/minecraft/client/renderer/ItemRenderer.java	Tue Aug 19 14:59:58 2025
@@ -5,31 +5,37 @@
 import net.minecraft.block.Block;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.AbstractClientPlayer;
 import net.minecraft.client.entity.EntityPlayerSP;
-import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
+import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
 import net.minecraft.client.renderer.entity.Render;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.entity.RenderPlayer;
 import net.minecraft.client.renderer.texture.TextureAtlasSprite;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.init.Items;
 import net.minecraft.item.Item;
+import net.minecraft.item.ItemBow;
+import net.minecraft.item.ItemMap;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.BlockRenderLayer;
 import net.minecraft.util.EnumBlockRenderType;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.EnumHandSide;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.storage.MapData;
+import net.optifine.DynamicLights;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;
+import net.optifine.shaders.Shaders;

 public class ItemRenderer {
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    private final Minecraft mc;
    private ItemStack itemStackMainHand = ItemStack.EMPTY;
@@ -44,23 +50,23 @@
    public ItemRenderer(Minecraft var1) {
       this.mc = var1;
       this.renderManager = var1.getRenderManager();
       this.itemRenderer = var1.getRenderItem();
    }

-   public void renderItem(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3) {
+   public void renderItem(EntityLivingBase var1, ItemStack var2, TransformType var3) {
       this.renderItemSide(var1, var2, var3, false);
    }

-   public void renderItemSide(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3, boolean var4) {
+   public void renderItemSide(EntityLivingBase var1, ItemStack var2, TransformType var3, boolean var4) {
       if (!var2.isEmpty()) {
          Item var5 = var2.getItem();
          Block var6 = Block.getBlockFromItem(var5);
          GlStateManager.pushMatrix();
          boolean var7 = this.itemRenderer.shouldRenderItemIn3D(var2) && var6.getRenderLayer() == BlockRenderLayer.TRANSLUCENT;
-         if (var7) {
+         if (var7 && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask)) {
             GlStateManager.depthMask(false);
          }

          this.itemRenderer.renderItem(var2, var1, var3, var4);
          if (var7) {
             GlStateManager.depthMask(true);
@@ -78,12 +84,16 @@
       GlStateManager.popMatrix();
    }

    private void setLightmap() {
       EntityPlayerSP var1 = this.mc.player;
       int var2 = this.mc.world.getCombinedLight(new BlockPos(var1.posX, var1.posY + var1.getEyeHeight(), var1.posZ), 0);
+      if (Config.isDynamicLights()) {
+         var2 = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), var2);
+      }
+
       float var3 = var2 & 65535;
       float var4 = var2 >> 16;
       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var3, var4);
    }

    private void rotateArm(float var1) {
@@ -183,13 +193,13 @@
       var3.begin(7, DefaultVertexFormats.POSITION_TEX);
       var3.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
       var3.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
       var3.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
       var3.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
       var2.draw();
-      MapData var4 = Items.FILLED_MAP.getMapData(var1, this.mc.world);
+      MapData var4 = ReflectorForge.getMapData(Items.FILLED_MAP, var1, this.mc.world);
       if (var4 != null) {
          this.mc.entityRenderer.getMapItemRenderer().renderMap(var4, false);
       }

       GlStateManager.enableLighting();
    }
@@ -211,13 +221,13 @@
       this.mc.getTextureManager().bindTexture(var12.getLocationSkin());
       GlStateManager.translate(var5 * -1.0F, 3.6F, 3.5F);
       GlStateManager.rotate(var5 * 120.0F, 0.0F, 0.0F, 1.0F);
       GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
       GlStateManager.rotate(var5 * -135.0F, 0.0F, 1.0F, 0.0F);
       GlStateManager.translate(var5 * 5.6F, 0.0F, 0.0F);
-      RenderPlayer var13 = (RenderPlayer)this.renderManager.<AbstractClientPlayer>getEntityRenderObject(var12);
+      RenderPlayer var13 = (RenderPlayer)this.renderManager.getEntityRenderObject(var12);
       GlStateManager.disableCull();
       if (var4) {
          var13.renderRightArm(var12);
       } else {
          var13.renderLeftArm(var12);
       }
@@ -263,13 +273,13 @@
       float var5 = var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1;
       float var6 = var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1;
       boolean var7 = true;
       boolean var8 = true;
       if (var2.isHandActive()) {
          ItemStack var9 = var2.getActiveItemStack();
-         if (var9.getItem() == Items.BOW) {
+         if (var9.getItem() instanceof ItemBow) {
             EnumHand var10 = var2.getActiveHand();
             var7 = var10 == EnumHand.MAIN_HAND;
             var8 = !var7;
          }
       }

@@ -277,125 +287,141 @@
       this.setLightmap();
       this.rotateArm(var1);
       GlStateManager.enableRescaleNormal();
       if (var7) {
          float var11 = var4 == EnumHand.MAIN_HAND ? var3 : 0.0F;
          float var13 = 1.0F - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * var1);
-         this.renderItemInFirstPerson(var2, var1, var5, EnumHand.MAIN_HAND, var11, this.itemStackMainHand, var13);
+         if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists()
+            || !Reflector.callBoolean(
+               Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, new Object[]{EnumHand.MAIN_HAND, var1, var5, var11, var13, this.itemStackMainHand}
+            )) {
+            this.renderItemInFirstPerson(var2, var1, var5, EnumHand.MAIN_HAND, var11, this.itemStackMainHand, var13);
+         }
       }

       if (var8) {
          float var12 = var4 == EnumHand.OFF_HAND ? var3 : 0.0F;
          float var14 = 1.0F - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * var1);
-         this.renderItemInFirstPerson(var2, var1, var5, EnumHand.OFF_HAND, var12, this.itemStackOffHand, var14);
+         if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists()
+            || !Reflector.callBoolean(
+               Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, new Object[]{EnumHand.OFF_HAND, var1, var5, var12, var14, this.itemStackOffHand}
+            )) {
+            this.renderItemInFirstPerson(var2, var1, var5, EnumHand.OFF_HAND, var12, this.itemStackOffHand, var14);
+         }
       }

       GlStateManager.disableRescaleNormal();
       RenderHelper.disableStandardItemLighting();
    }

    public void renderItemInFirstPerson(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7) {
-      boolean var8 = var4 == EnumHand.MAIN_HAND;
-      EnumHandSide var9 = var8 ? var1.getPrimaryHand() : var1.getPrimaryHand().opposite();
-      GlStateManager.pushMatrix();
-      if (var6.isEmpty()) {
-         if (var8 && !var1.isInvisible()) {
-            this.renderArmFirstPerson(var7, var5, var9);
-         }
-      } else if (var6.getItem() == Items.FILLED_MAP) {
-         if (var8 && this.itemStackOffHand.isEmpty()) {
-            this.renderMapFirstPerson(var3, var7, var5);
-         } else {
-            this.renderMapFirstPersonSide(var7, var9, var5, var6);
-         }
-      } else {
-         boolean var10 = var9 == EnumHandSide.RIGHT;
-         if (var1.isHandActive() && var1.getItemInUseCount() > 0 && var1.getActiveHand() == var4) {
-            int var17 = var10 ? 1 : -1;
-            switch (var6.getItemUseAction()) {
-               case NONE:
-                  this.transformSideFirstPerson(var9, var7);
-                  break;
-               case EAT:
-               case DRINK:
-                  this.transformEatFirstPerson(var2, var9, var6);
-                  this.transformSideFirstPerson(var9, var7);
-                  break;
-               case BLOCK:
-                  this.transformSideFirstPerson(var9, var7);
-                  break;
-               case BOW:
-                  this.transformSideFirstPerson(var9, var7);
-                  GlStateManager.translate(var17 * -0.2785682F, 0.18344387F, 0.15731531F);
-                  GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
-                  GlStateManager.rotate(var17 * 35.3F, 0.0F, 1.0F, 0.0F);
-                  GlStateManager.rotate(var17 * -9.785F, 0.0F, 0.0F, 1.0F);
-                  float var18 = var6.getMaxItemUseDuration() - (this.mc.player.getItemInUseCount() - var2 + 1.0F);
-                  float var19 = var18 / 20.0F;
-                  var19 = (var19 * var19 + var19 * 2.0F) / 3.0F;
-                  if (var19 > 1.0F) {
-                     var19 = 1.0F;
-                  }
-
-                  if (var19 > 0.1F) {
-                     float var21 = MathHelper.sin((var18 - 0.1F) * 1.3F);
-                     float var15 = var19 - 0.1F;
-                     float var16 = var21 * var15;
-                     GlStateManager.translate(var16 * 0.0F, var16 * 0.004F, var16 * 0.0F);
-                  }
-
-                  GlStateManager.translate(var19 * 0.0F, var19 * 0.0F, var19 * 0.04F);
-                  GlStateManager.scale(1.0F, 1.0F, 1.0F + var19 * 0.2F);
-                  GlStateManager.rotate(var17 * 45.0F, 0.0F, -1.0F, 0.0F);
+      if (!Config.isShaders() || !Shaders.isSkipRenderHand(var4)) {
+         boolean var8 = var4 == EnumHand.MAIN_HAND;
+         EnumHandSide var9 = var8 ? var1.getPrimaryHand() : var1.getPrimaryHand().opposite();
+         GlStateManager.pushMatrix();
+         if (var6.isEmpty()) {
+            if (var8 && !var1.isInvisible()) {
+               this.renderArmFirstPerson(var7, var5, var9);
+            }
+         } else if (var6.getItem() instanceof ItemMap) {
+            if (var8 && this.itemStackOffHand.isEmpty()) {
+               this.renderMapFirstPerson(var3, var7, var5);
+            } else {
+               this.renderMapFirstPersonSide(var7, var9, var5, var6);
             }
          } else {
-            float var11 = -0.4F * MathHelper.sin(MathHelper.sqrt(var5) * (float) Math.PI);
-            float var12 = 0.2F * MathHelper.sin(MathHelper.sqrt(var5) * (float) (Math.PI * 2));
-            float var13 = -0.2F * MathHelper.sin(var5 * (float) Math.PI);
-            int var14 = var10 ? 1 : -1;
-            GlStateManager.translate(var14 * var11, var12, var13);
-            this.transformSideFirstPerson(var9, var7);
-            this.transformFirstPerson(var9, var5);
+            boolean var10 = var9 == EnumHandSide.RIGHT;
+            if (var1.isHandActive() && var1.getItemInUseCount() > 0 && var1.getActiveHand() == var4) {
+               int var17 = var10 ? 1 : -1;
+               switch (var6.getItemUseAction()) {
+                  case NONE:
+                     this.transformSideFirstPerson(var9, var7);
+                     break;
+                  case EAT:
+                  case DRINK:
+                     this.transformEatFirstPerson(var2, var9, var6);
+                     this.transformSideFirstPerson(var9, var7);
+                     break;
+                  case BLOCK:
+                     this.transformSideFirstPerson(var9, var7);
+                     break;
+                  case BOW:
+                     this.transformSideFirstPerson(var9, var7);
+                     GlStateManager.translate(var17 * -0.2785682F, 0.18344387F, 0.15731531F);
+                     GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
+                     GlStateManager.rotate(var17 * 35.3F, 0.0F, 1.0F, 0.0F);
+                     GlStateManager.rotate(var17 * -9.785F, 0.0F, 0.0F, 1.0F);
+                     float var18 = var6.getMaxItemUseDuration() - (this.mc.player.getItemInUseCount() - var2 + 1.0F);
+                     float var19 = var18 / 20.0F;
+                     var19 = (var19 * var19 + var19 * 2.0F) / 3.0F;
+                     if (var19 > 1.0F) {
+                        var19 = 1.0F;
+                     }
+
+                     if (var19 > 0.1F) {
+                        float var21 = MathHelper.sin((var18 - 0.1F) * 1.3F);
+                        float var15 = var19 - 0.1F;
+                        float var16 = var21 * var15;
+                        GlStateManager.translate(var16 * 0.0F, var16 * 0.004F, var16 * 0.0F);
+                     }
+
+                     GlStateManager.translate(var19 * 0.0F, var19 * 0.0F, var19 * 0.04F);
+                     GlStateManager.scale(1.0F, 1.0F, 1.0F + var19 * 0.2F);
+                     GlStateManager.rotate(var17 * 45.0F, 0.0F, -1.0F, 0.0F);
+               }
+            } else {
+               float var11 = -0.4F * MathHelper.sin(MathHelper.sqrt(var5) * (float) Math.PI);
+               float var12 = 0.2F * MathHelper.sin(MathHelper.sqrt(var5) * (float) (Math.PI * 2));
+               float var13 = -0.2F * MathHelper.sin(var5 * (float) Math.PI);
+               int var14 = var10 ? 1 : -1;
+               GlStateManager.translate(var14 * var11, var12, var13);
+               this.transformSideFirstPerson(var9, var7);
+               this.transformFirstPerson(var9, var5);
+            }
+
+            this.renderItemSide(var1, var6, var10 ? TransformType.FIRST_PERSON_RIGHT_HAND : TransformType.FIRST_PERSON_LEFT_HAND, !var10);
          }

-         this.renderItemSide(
-            var1, var6, var10 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !var10
-         );
+         GlStateManager.popMatrix();
       }
-
-      GlStateManager.popMatrix();
    }

    public void renderOverlays(float var1) {
       GlStateManager.disableAlpha();
       if (this.mc.player.isEntityInsideOpaqueBlock()) {
          IBlockState var2 = this.mc.world.getBlockState(new BlockPos(this.mc.player));
-         EntityPlayerSP var3 = this.mc.player;
+         BlockPos var3 = new BlockPos(this.mc.player);
+         EntityPlayerSP var4 = this.mc.player;

-         for (int var4 = 0; var4 < 8; var4++) {
-            double var5 = var3.posX + ((var4 >> 0) % 2 - 0.5F) * var3.width * 0.8F;
-            double var7 = var3.posY + ((var4 >> 1) % 2 - 0.5F) * 0.1F;
-            double var9 = var3.posZ + ((var4 >> 2) % 2 - 0.5F) * var3.width * 0.8F;
-            BlockPos var11 = new BlockPos(var5, var7 + var3.getEyeHeight(), var9);
-            IBlockState var12 = this.mc.world.getBlockState(var11);
-            if (var12.causesSuffocation()) {
-               var2 = var12;
+         for (int var5 = 0; var5 < 8; var5++) {
+            double var6 = var4.posX + ((var5 >> 0) % 2 - 0.5F) * var4.width * 0.8F;
+            double var8 = var4.posY + ((var5 >> 1) % 2 - 0.5F) * 0.1F;
+            double var10 = var4.posZ + ((var5 >> 2) % 2 - 0.5F) * var4.width * 0.8F;
+            BlockPos var12 = new BlockPos(var6, var8 + var4.getEyeHeight(), var10);
+            IBlockState var13 = this.mc.world.getBlockState(var12);
+            if (var13.r()) {
+               var2 = var13;
+               var3 = var12;
             }
          }

-         if (var2.getRenderType() != EnumBlockRenderType.INVISIBLE) {
-            this.renderSuffocationOverlay(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(var2));
+         if (var2.i() != EnumBlockRenderType.INVISIBLE) {
+            Object var14 = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
+            if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, new Object[]{this.mc.player, var1, var14, var2, var3})) {
+               this.renderSuffocationOverlay(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(var2));
+            }
          }
       }

       if (!this.mc.player.isSpectator()) {
-         if (this.mc.player.isInsideOfMaterial(Material.WATER)) {
+         if (this.mc.player.isInsideOfMaterial(Material.WATER)
+            && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, new Object[]{this.mc.player, var1})) {
             this.renderWaterOverlayTexture(var1);
          }

-         if (this.mc.player.isBurning()) {
+         if (this.mc.player.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, new Object[]{this.mc.player, var1})) {
             this.renderFireInFirstPerson();
          }
       }

       GlStateManager.enableAlpha();
    }
@@ -424,39 +450,44 @@
       var2.draw();
       GlStateManager.popMatrix();
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderWaterOverlayTexture(float var1) {
-      this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
-      Tessellator var2 = Tessellator.getInstance();
-      BufferBuilder var3 = var2.getBuffer();
-      float var4 = this.mc.player.getBrightness();
-      GlStateManager.color(var4, var4, var4, 0.5F);
-      GlStateManager.enableBlend();
-      GlStateManager.tryBlendFuncSeparate(
-         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
-      );
-      GlStateManager.pushMatrix();
-      float var5 = 4.0F;
-      float var6 = -1.0F;
-      float var7 = 1.0F;
-      float var8 = -1.0F;
-      float var9 = 1.0F;
-      float var10 = -0.5F;
-      float var11 = -this.mc.player.rotationYaw / 64.0F;
-      float var12 = this.mc.player.rotationPitch / 64.0F;
-      var3.begin(7, DefaultVertexFormats.POSITION_TEX);
-      var3.pos(-1.0, -1.0, -0.5).tex(4.0F + var11, 4.0F + var12).endVertex();
-      var3.pos(1.0, -1.0, -0.5).tex(0.0F + var11, 4.0F + var12).endVertex();
-      var3.pos(1.0, 1.0, -0.5).tex(0.0F + var11, 0.0F + var12).endVertex();
-      var3.pos(-1.0, 1.0, -0.5).tex(4.0F + var11, 0.0F + var12).endVertex();
-      var2.draw();
-      GlStateManager.popMatrix();
-      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-      GlStateManager.disableBlend();
+      if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
+         this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
+         Tessellator var2 = Tessellator.getInstance();
+         BufferBuilder var3 = var2.getBuffer();
+         float var4 = this.mc.player.getBrightness();
+         GlStateManager.color(var4, var4, var4, 0.5F);
+         GlStateManager.enableBlend();
+         GlStateManager.tryBlendFuncSeparate(
+            GlStateManager.SourceFactor.SRC_ALPHA,
+            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
+            GlStateManager.SourceFactor.ONE,
+            GlStateManager.DestFactor.ZERO
+         );
+         GlStateManager.pushMatrix();
+         float var5 = 4.0F;
+         float var6 = -1.0F;
+         float var7 = 1.0F;
+         float var8 = -1.0F;
+         float var9 = 1.0F;
+         float var10 = -0.5F;
+         float var11 = -this.mc.player.rotationYaw / 64.0F;
+         float var12 = this.mc.player.rotationPitch / 64.0F;
+         var3.begin(7, DefaultVertexFormats.POSITION_TEX);
+         var3.pos(-1.0, -1.0, -0.5).tex(4.0F + var11, 4.0F + var12).endVertex();
+         var3.pos(1.0, -1.0, -0.5).tex(0.0F + var11, 4.0F + var12).endVertex();
+         var3.pos(1.0, 1.0, -0.5).tex(0.0F + var11, 0.0F + var12).endVertex();
+         var3.pos(-1.0, 1.0, -0.5).tex(4.0F + var11, 0.0F + var12).endVertex();
+         var2.draw();
+         GlStateManager.popMatrix();
+         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+         GlStateManager.disableBlend();
+      }
    }

    private void renderFireInFirstPerson() {
       Tessellator var1 = Tessellator.getInstance();
       BufferBuilder var2 = var1.getBuffer();
       GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
@@ -481,12 +512,13 @@
          float var12 = -0.5F;
          float var13 = 0.5F;
          float var14 = -0.5F;
          GlStateManager.translate(-(var4 * 2 - 1) * 0.24F, -0.3F, 0.0F);
          GlStateManager.rotate((var4 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
          var2.begin(7, DefaultVertexFormats.POSITION_TEX);
+         var2.setSprite(var5);
          var2.pos(-0.5, -0.5, -0.5).tex(var7, var9).endVertex();
          var2.pos(0.5, -0.5, -0.5).tex(var6, var9).endVertex();
          var2.pos(0.5, 0.5, -0.5).tex(var6, var8).endVertex();
          var2.pos(-0.5, 0.5, -0.5).tex(var7, var8).endVertex();
          var1.draw();
          GlStateManager.popMatrix();
@@ -506,24 +538,48 @@
       ItemStack var3 = var1.getHeldItemOffhand();
       if (var1.isRowingBoat()) {
          this.equippedProgressMainHand = MathHelper.clamp(this.equippedProgressMainHand - 0.4F, 0.0F, 1.0F);
          this.equippedProgressOffHand = MathHelper.clamp(this.equippedProgressOffHand - 0.4F, 0.0F, 1.0F);
       } else {
          float var4 = var1.getCooledAttackStrength(1.0F);
-         this.equippedProgressMainHand = this.equippedProgressMainHand
-            + MathHelper.clamp((Objects.equals(this.itemStackMainHand, var2) ? var4 * var4 * var4 : 0.0F) - this.equippedProgressMainHand, -0.4F, 0.4F);
-         this.equippedProgressOffHand = this.equippedProgressOffHand
-            + MathHelper.clamp((Objects.equals(this.itemStackOffHand, var3) ? 1 : 0) - this.equippedProgressOffHand, -0.4F, 0.4F);
+         if (Reflector.ForgeHooksClient_shouldCauseReequipAnimation.exists()) {
+            boolean var5 = Reflector.callBoolean(
+               Reflector.ForgeHooksClient_shouldCauseReequipAnimation, new Object[]{this.itemStackMainHand, var2, var1.inventory.currentItem}
+            );
+            boolean var6 = Reflector.callBoolean(Reflector.ForgeHooksClient_shouldCauseReequipAnimation, new Object[]{this.itemStackOffHand, var3, -1});
+            if (!var5 && !Objects.equals(this.itemStackMainHand, var2)) {
+               this.itemStackMainHand = var2;
+            }
+
+            if (!var5 && !Objects.equals(this.itemStackOffHand, var3)) {
+               this.itemStackOffHand = var3;
+            }
+
+            this.equippedProgressMainHand = this.equippedProgressMainHand
+               + MathHelper.clamp((!var5 ? var4 * var4 * var4 : 0.0F) - this.equippedProgressMainHand, -0.4F, 0.4F);
+            this.equippedProgressOffHand = this.equippedProgressOffHand + MathHelper.clamp((!var6 ? 1 : 0) - this.equippedProgressOffHand, -0.4F, 0.4F);
+         } else {
+            this.equippedProgressMainHand = this.equippedProgressMainHand
+               + MathHelper.clamp((Objects.equals(this.itemStackMainHand, var2) ? var4 * var4 * var4 : 0.0F) - this.equippedProgressMainHand, -0.4F, 0.4F);
+            this.equippedProgressOffHand = this.equippedProgressOffHand
+               + MathHelper.clamp((Objects.equals(this.itemStackOffHand, var3) ? 1 : 0) - this.equippedProgressOffHand, -0.4F, 0.4F);
+         }
       }

       if (this.equippedProgressMainHand < 0.1F) {
          this.itemStackMainHand = var2;
+         if (Config.isShaders()) {
+            Shaders.setItemToRenderMain(this.itemStackMainHand);
+         }
       }

       if (this.equippedProgressOffHand < 0.1F) {
          this.itemStackOffHand = var3;
+         if (Config.isShaders()) {
+            Shaders.setItemToRenderOff(this.itemStackOffHand);
+         }
       }
    }

    public void resetEquippedProgress(EnumHand var1) {
       if (var1 == EnumHand.MAIN_HAND) {
          this.equippedProgressMainHand = 0.0F;
 */
