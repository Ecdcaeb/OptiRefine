package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

public class MixinRenderItem
{
}
/*
--- net/minecraft/client/renderer/RenderItem.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/RenderItem.java	Tue Aug 19 14:59:58 2025
@@ -1,37 +1,26 @@
 package net.minecraft.client.renderer;

 import java.util.List;
 import javax.annotation.Nullable;
 import net.minecraft.block.Block;
-import net.minecraft.block.BlockDirt;
-import net.minecraft.block.BlockDoublePlant;
-import net.minecraft.block.BlockFlower;
-import net.minecraft.block.BlockHugeMushroom;
-import net.minecraft.block.BlockPlanks;
-import net.minecraft.block.BlockPrismarine;
-import net.minecraft.block.BlockQuartz;
-import net.minecraft.block.BlockRedSandstone;
-import net.minecraft.block.BlockSand;
-import net.minecraft.block.BlockSandStone;
-import net.minecraft.block.BlockSilverfish;
-import net.minecraft.block.BlockStone;
-import net.minecraft.block.BlockStoneBrick;
-import net.minecraft.block.BlockStoneSlab;
-import net.minecraft.block.BlockStoneSlabNew;
-import net.minecraft.block.BlockTallGrass;
-import net.minecraft.block.BlockWall;
+import net.minecraft.block.BlockDirt.DirtType;
+import net.minecraft.block.BlockDoublePlant.EnumPlantType;
+import net.minecraft.block.BlockFlower.EnumFlowerType;
+import net.minecraft.block.BlockWall.EnumType;
+import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.client.gui.FontRenderer;
 import net.minecraft.client.renderer.block.model.BakedQuad;
 import net.minecraft.client.renderer.block.model.IBakedModel;
 import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
 import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
 import net.minecraft.client.renderer.block.model.ModelManager;
 import net.minecraft.client.renderer.block.model.ModelResourceLocation;
+import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
 import net.minecraft.client.renderer.color.ItemColors;
 import net.minecraft.client.renderer.texture.TextureManager;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.client.renderer.texture.TextureUtil;
 import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
@@ -42,33 +31,52 @@
 import net.minecraft.crash.ICrashReportDetail;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.item.EnumDyeColor;
 import net.minecraft.item.Item;
-import net.minecraft.item.ItemFishFood;
 import net.minecraft.item.ItemStack;
-import net.minecraft.tileentity.TileEntityStructure;
+import net.minecraft.item.ItemFishFood.FishType;
+import net.minecraft.tileentity.TileEntityStructure.Mode;
+import net.minecraft.util.BlockRenderLayer;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.ReportedException;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.Vec3i;
 import net.minecraft.world.World;
+import net.optifine.CustomColors;
+import net.optifine.CustomItems;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;
+import net.optifine.shaders.Shaders;
+import net.optifine.shaders.ShadersRender;

 public class RenderItem implements IResourceManagerReloadListener {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private boolean notRenderingEffectsInGUI = true;
    public float zLevel;
    private final ItemModelMesher itemModelMesher;
    private final TextureManager textureManager;
    private final ItemColors itemColors;
+   private ResourceLocation modelLocation = null;
+   private boolean renderItemGui = false;
+   public ModelManager modelManager = null;
+   private boolean renderModelHasEmissive = false;
+   private boolean renderModelEmissive = false;
+   private boolean forgeAllowEmissiveItems = Reflector.getFieldValueBoolean(Reflector.ForgeModContainer_allowEmissiveItems, false);

    public RenderItem(TextureManager var1, ModelManager var2, ItemColors var3) {
       this.textureManager = var1;
-      this.itemModelMesher = new ItemModelMesher(var2);
+      this.modelManager = var2;
+      if (Reflector.ItemModelMesherForge_Constructor.exists()) {
+         this.itemModelMesher = (ItemModelMesher)Reflector.newInstance(Reflector.ItemModelMesherForge_Constructor, new Object[]{var2});
+      } else {
+         this.itemModelMesher = new ItemModelMesher(var2);
+      }
+
       this.registerItems();
       this.itemColors = var3;
    }

    public ItemModelMesher getItemModelMesher() {
       return this.itemModelMesher;
@@ -91,97 +99,166 @@
    }

    private void renderModel(IBakedModel var1, ItemStack var2) {
       this.renderModel(var1, -1, var2);
    }

-   private void renderModel(IBakedModel var1, int var2) {
+   public void renderModel(IBakedModel var1, int var2) {
       this.renderModel(var1, var2, ItemStack.EMPTY);
    }

    private void renderModel(IBakedModel var1, int var2, ItemStack var3) {
       Tessellator var4 = Tessellator.getInstance();
       BufferBuilder var5 = var4.getBuffer();
+      boolean var6 = Minecraft.getMinecraft().getTextureMapBlocks().isTextureBound();
+      boolean var7 = Config.isMultiTexture() && var6;
+      if (var7) {
+         var5.setBlockLayer(BlockRenderLayer.SOLID);
+      }
+
       var5.begin(7, DefaultVertexFormats.ITEM);

-      for (EnumFacing var9 : EnumFacing.values()) {
-         this.renderQuads(var5, var1.getQuads(null, var9, 0L), var2, var3);
+      for (EnumFacing var11 : EnumFacing.VALUES) {
+         this.renderQuads(var5, var1.getQuads((IBlockState)null, var11, 0L), var2, var3);
       }

-      this.renderQuads(var5, var1.getQuads(null, null, 0L), var2, var3);
+      this.renderQuads(var5, var1.getQuads((IBlockState)null, (EnumFacing)null, 0L), var2, var3);
       var4.draw();
+      if (var7) {
+         var5.setBlockLayer(null);
+         GlStateManager.bindCurrentTexture();
+      }
    }

    public void renderItem(ItemStack var1, IBakedModel var2) {
       if (!var1.isEmpty()) {
          GlStateManager.pushMatrix();
          GlStateManager.translate(-0.5F, -0.5F, -0.5F);
          if (var2.isBuiltInRenderer()) {
             GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
             GlStateManager.enableRescaleNormal();
-            TileEntityItemStackRenderer.instance.renderByItem(var1);
+            if (Reflector.ForgeItem_getTileEntityItemStackRenderer.exists()) {
+               TileEntityItemStackRenderer var5 = (TileEntityItemStackRenderer)Reflector.call(
+                  var1.getItem(), Reflector.ForgeItem_getTileEntityItemStackRenderer, new Object[0]
+               );
+               var5.renderByItem(var1);
+            } else {
+               TileEntityItemStackRenderer.instance.renderByItem(var1);
+            }
          } else {
+            if (Config.isCustomItems()) {
+               var2 = CustomItems.getCustomItemModel(var1, var2, this.modelLocation, false);
+               this.modelLocation = null;
+            }
+
+            this.renderModelHasEmissive = false;
             this.renderModel(var2, var1);
-            if (var1.hasEffect()) {
+            if (this.renderModelHasEmissive) {
+               float var3 = OpenGlHelper.lastBrightnessX;
+               float var4 = OpenGlHelper.lastBrightnessY;
+               OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, var4);
+               this.renderModelEmissive = true;
+               this.renderModel(var2, var1);
+               this.renderModelEmissive = false;
+               OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var3, var4);
+            }
+
+            if (var1.hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomEffect(this, var1, var2))) {
                this.renderEffect(var2);
             }
          }

          GlStateManager.popMatrix();
       }
    }

    private void renderEffect(IBakedModel var1) {
-      GlStateManager.depthMask(false);
-      GlStateManager.depthFunc(514);
-      GlStateManager.disableLighting();
-      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
-      this.textureManager.bindTexture(RES_ITEM_GLINT);
-      GlStateManager.matrixMode(5890);
-      GlStateManager.pushMatrix();
-      GlStateManager.scale(8.0F, 8.0F, 8.0F);
-      float var2 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
-      GlStateManager.translate(var2, 0.0F, 0.0F);
-      GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
-      this.renderModel(var1, -8372020);
-      GlStateManager.popMatrix();
-      GlStateManager.pushMatrix();
-      GlStateManager.scale(8.0F, 8.0F, 8.0F);
-      float var3 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
-      GlStateManager.translate(-var3, 0.0F, 0.0F);
-      GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
-      this.renderModel(var1, -8372020);
-      GlStateManager.popMatrix();
-      GlStateManager.matrixMode(5888);
-      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
-      GlStateManager.enableLighting();
-      GlStateManager.depthFunc(515);
-      GlStateManager.depthMask(true);
-      this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
+      if (!Config.isCustomItems() || CustomItems.isUseGlint()) {
+         if (!Config.isShaders() || !Shaders.isShadowPass) {
+            GlStateManager.depthMask(false);
+            GlStateManager.depthFunc(514);
+            GlStateManager.disableLighting();
+            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
+            this.textureManager.bindTexture(RES_ITEM_GLINT);
+            if (Config.isShaders() && !this.renderItemGui) {
+               ShadersRender.renderEnchantedGlintBegin();
+            }
+
+            GlStateManager.matrixMode(5890);
+            GlStateManager.pushMatrix();
+            GlStateManager.scale(8.0F, 8.0F, 8.0F);
+            float var2 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
+            GlStateManager.translate(var2, 0.0F, 0.0F);
+            GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
+            this.renderModel(var1, -8372020);
+            GlStateManager.popMatrix();
+            GlStateManager.pushMatrix();
+            GlStateManager.scale(8.0F, 8.0F, 8.0F);
+            float var3 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
+            GlStateManager.translate(-var3, 0.0F, 0.0F);
+            GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
+            this.renderModel(var1, -8372020);
+            GlStateManager.popMatrix();
+            GlStateManager.matrixMode(5888);
+            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
+            GlStateManager.enableLighting();
+            GlStateManager.depthFunc(515);
+            GlStateManager.depthMask(true);
+            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
+            if (Config.isShaders() && !this.renderItemGui) {
+               ShadersRender.renderEnchantedGlintEnd();
+            }
+         }
+      }
    }

    private void putQuadNormal(BufferBuilder var1, BakedQuad var2) {
       Vec3i var3 = var2.getFace().getDirectionVec();
       var1.putNormal(var3.getX(), var3.getY(), var3.getZ());
    }

    private void renderQuad(BufferBuilder var1, BakedQuad var2, int var3) {
-      var1.addVertexData(var2.getVertexData());
-      var1.putColor4(var3);
+      if (this.renderModelEmissive) {
+         if (var2.getQuadEmissive() == null) {
+            return;
+         }
+
+         var2 = var2.getQuadEmissive();
+      } else if (var2.getQuadEmissive() != null) {
+         this.renderModelHasEmissive = true;
+      }
+
+      if (var1.isMultiTexture()) {
+         var1.addVertexData(var2.getVertexDataSingle());
+      } else {
+         var1.addVertexData(var2.getVertexData());
+      }
+
+      var1.putSprite(var2.getSprite());
+      if (Reflector.ForgeHooksClient_putQuadColor.exists()) {
+         Reflector.call(Reflector.ForgeHooksClient_putQuadColor, new Object[]{var1, var2, var3});
+      } else {
+         var1.putColor4(var3);
+      }
+
       this.putQuadNormal(var1, var2);
    }

    private void renderQuads(BufferBuilder var1, List<BakedQuad> var2, int var3, ItemStack var4) {
       boolean var5 = var3 == -1 && !var4.isEmpty();
       int var6 = 0;

       for (int var7 = var2.size(); var6 < var7; var6++) {
          BakedQuad var8 = (BakedQuad)var2.get(var6);
          int var9 = var3;
          if (var5 && var8.hasTintIndex()) {
             var9 = this.itemColors.colorMultiplier(var4, var8.getTintIndex());
+            if (Config.isCustomColors()) {
+               var9 = CustomColors.getColorFromItemStack(var4, var8.getTintIndex(), var9);
+            }
+
             if (EntityRenderer.anaglyphEnable) {
                var9 = TextureUtil.anaglyphColor(var9);
             }

             var9 |= -16777216;
          }
@@ -192,38 +269,51 @@

    public boolean shouldRenderItemIn3D(ItemStack var1) {
       IBakedModel var2 = this.itemModelMesher.getItemModel(var1);
       return var2 == null ? false : var2.isGui3d();
    }

-   public void renderItem(ItemStack var1, ItemCameraTransforms.TransformType var2) {
+   public void renderItem(ItemStack var1, TransformType var2) {
       if (!var1.isEmpty()) {
-         IBakedModel var3 = this.getItemModelWithOverrides(var1, null, null);
+         IBakedModel var3 = this.getItemModelWithOverrides(var1, (World)null, (EntityLivingBase)null);
          this.renderItemModel(var1, var3, var2, false);
       }
    }

    public IBakedModel getItemModelWithOverrides(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
       IBakedModel var4 = this.itemModelMesher.getItemModel(var1);
       Item var5 = var1.getItem();
-      if (var5 != null && var5.hasCustomProperties()) {
-         ResourceLocation var6 = var4.getOverrides().applyOverride(var1, var2, var3);
-         return var6 == null ? var4 : this.itemModelMesher.getModelManager().getModel(new ModelResourceLocation(var6, "inventory"));
+      if (Config.isCustomItems()) {
+         if (var5 != null && var5.hasCustomProperties()) {
+            this.modelLocation = var4.getOverrides().applyOverride(var1, var2, var3);
+         }
+
+         IBakedModel var6 = CustomItems.getCustomItemModel(var1, var4, this.modelLocation, true);
+         if (var6 != var4) {
+            return var6;
+         }
+      }
+
+      if (Reflector.ModelLoader_getInventoryVariant.exists()) {
+         return var4.getOverrides().handleItemState(var4, var1, var2, var3);
+      } else if (var5 != null && var5.hasCustomProperties()) {
+         ResourceLocation var7 = var4.getOverrides().applyOverride(var1, var2, var3);
+         return var7 == null ? var4 : this.itemModelMesher.getModelManager().getModel(new ModelResourceLocation(var7, "inventory"));
       } else {
          return var4;
       }
    }

-   public void renderItem(ItemStack var1, EntityLivingBase var2, ItemCameraTransforms.TransformType var3, boolean var4) {
+   public void renderItem(ItemStack var1, EntityLivingBase var2, TransformType var3, boolean var4) {
       if (!var1.isEmpty() && var2 != null) {
          IBakedModel var5 = this.getItemModelWithOverrides(var1, var2.world, var2);
          this.renderItemModel(var1, var5, var3, var4);
       }
    }

-   protected void renderItemModel(ItemStack var1, IBakedModel var2, ItemCameraTransforms.TransformType var3, boolean var4) {
+   protected void renderItemModel(ItemStack var1, IBakedModel var2, TransformType var3, boolean var4) {
       if (!var1.isEmpty()) {
          this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
          this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.enableRescaleNormal();
          GlStateManager.alphaFunc(516, 0.1F);
@@ -232,19 +322,25 @@
             GlStateManager.SourceFactor.SRC_ALPHA,
             GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
             GlStateManager.SourceFactor.ONE,
             GlStateManager.DestFactor.ZERO
          );
          GlStateManager.pushMatrix();
-         ItemCameraTransforms var5 = var2.getItemCameraTransforms();
-         ItemCameraTransforms.applyTransformSide(var5.getTransform(var3), var4);
-         if (this.isThereOneNegativeScale(var5.getTransform(var3))) {
-            GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
+         if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
+            var2 = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[]{var2, var3, var4});
+         } else {
+            ItemCameraTransforms var5 = var2.getItemCameraTransforms();
+            ItemCameraTransforms.applyTransformSide(var5.getTransform(var3), var4);
+            if (this.isThereOneNegativeScale(var5.getTransform(var3))) {
+               GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
+            }
          }

+         CustomItems.setRenderOffHand(var4);
          this.renderItem(var1, var2);
+         CustomItems.setRenderOffHand(false);
          GlStateManager.cullFace(GlStateManager.CullFace.BACK);
          GlStateManager.popMatrix();
          GlStateManager.disableRescaleNormal();
          GlStateManager.disableBlend();
          this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
          this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
@@ -253,34 +349,41 @@

    private boolean isThereOneNegativeScale(ItemTransformVec3f var1) {
       return var1.scale.x < 0.0F ^ var1.scale.y < 0.0F ^ var1.scale.z < 0.0F;
    }

    public void renderItemIntoGUI(ItemStack var1, int var2, int var3) {
-      this.renderItemModelIntoGUI(var1, var2, var3, this.getItemModelWithOverrides(var1, null, null));
+      this.renderItemModelIntoGUI(var1, var2, var3, this.getItemModelWithOverrides(var1, (World)null, (EntityLivingBase)null));
    }

    protected void renderItemModelIntoGUI(ItemStack var1, int var2, int var3, IBakedModel var4) {
+      this.renderItemGui = true;
       GlStateManager.pushMatrix();
       this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
       this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
       GlStateManager.enableRescaleNormal();
       GlStateManager.enableAlpha();
       GlStateManager.alphaFunc(516, 0.1F);
       GlStateManager.enableBlend();
       GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       this.setupGuiTransform(var2, var3, var4.isGui3d());
-      var4.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
+      if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
+         var4 = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[]{var4, TransformType.GUI, false});
+      } else {
+         var4.getItemCameraTransforms().applyTransform(TransformType.GUI);
+      }
+
       this.renderItem(var1, var4);
       GlStateManager.disableAlpha();
       GlStateManager.disableRescaleNormal();
       GlStateManager.disableLighting();
       GlStateManager.popMatrix();
       this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
       this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
+      this.renderItemGui = false;
    }

    private void setupGuiTransform(int var1, int var2, boolean var3) {
       GlStateManager.translate((float)var1, (float)var2, 100.0F + this.zLevel);
       GlStateManager.translate(8.0F, 8.0F, 0.0F);
       GlStateManager.scale(1.0F, -1.0F, 1.0F);
@@ -298,21 +401,25 @@

    public void renderItemAndEffectIntoGUI(@Nullable EntityLivingBase var1, final ItemStack var2, int var3, int var4) {
       if (!var2.isEmpty()) {
          this.zLevel += 50.0F;

          try {
-            this.renderItemModelIntoGUI(var2, var3, var4, this.getItemModelWithOverrides(var2, null, var1));
+            this.renderItemModelIntoGUI(var2, var3, var4, this.getItemModelWithOverrides(var2, (World)null, var1));
          } catch (Throwable var8) {
             CrashReport var6 = CrashReport.makeCrashReport(var8, "Rendering item");
             CrashReportCategory var7 = var6.makeCategory("Item being rendered");
             var7.addDetail("Item Type", new ICrashReportDetail<String>() {
                public String call() throws Exception {
                   return String.valueOf(var2.getItem());
                }
             });
+            if (Reflector.IForgeRegistryEntry_Impl_getRegistryName.exists()) {
+               var7.addDetail("Registry Name", ReflectorForge.getDetailItemRegistryName(var2.getItem()));
+            }
+
             var7.addDetail("Item Aux", new ICrashReportDetail<String>() {
                public String call() throws Exception {
                   return String.valueOf(var2.getMetadata());
                }
             });
             var7.addDetail("Item NBT", new ICrashReportDetail<String>() {
@@ -330,58 +437,81 @@

          this.zLevel -= 50.0F;
       }
    }

    public void renderItemOverlays(FontRenderer var1, ItemStack var2, int var3, int var4) {
-      this.renderItemOverlayIntoGUI(var1, var2, var3, var4, null);
+      this.renderItemOverlayIntoGUI(var1, var2, var3, var4, (String)null);
    }

    public void renderItemOverlayIntoGUI(FontRenderer var1, ItemStack var2, int var3, int var4, @Nullable String var5) {
       if (!var2.isEmpty()) {
          if (var2.getCount() != 1 || var5 != null) {
             String var6 = var5 == null ? String.valueOf(var2.getCount()) : var5;
             GlStateManager.disableLighting();
             GlStateManager.disableDepth();
             GlStateManager.disableBlend();
             var1.drawStringWithShadow(var6, var3 + 19 - 2 - var1.getStringWidth(var6), var4 + 6 + 3, 16777215);
             GlStateManager.enableLighting();
             GlStateManager.enableDepth();
+            GlStateManager.enableBlend();
          }

-         if (var2.isItemDamaged()) {
+         if (ReflectorForge.isItemDamaged(var2)) {
             GlStateManager.disableLighting();
             GlStateManager.disableDepth();
             GlStateManager.disableTexture2D();
             GlStateManager.disableAlpha();
             GlStateManager.disableBlend();
-            Tessellator var13 = Tessellator.getInstance();
-            BufferBuilder var7 = var13.getBuffer();
+            Tessellator var16 = Tessellator.getInstance();
+            BufferBuilder var7 = var16.getBuffer();
             float var8 = var2.getItemDamage();
             float var9 = var2.getMaxDamage();
             float var10 = Math.max(0.0F, (var9 - var8) / var9);
             int var11 = Math.round(13.0F - var8 * 13.0F / var9);
             int var12 = MathHelper.hsvToRGB(var10 / 3.0F, 1.0F, 1.0F);
+            if (Reflector.ForgeItem_getDurabilityForDisplay.exists() && Reflector.ForgeItem_getRGBDurabilityForDisplay.exists()) {
+               double var13 = Reflector.callDouble(var2.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, new Object[]{var2});
+               int var15 = Reflector.callInt(var2.getItem(), Reflector.ForgeItem_getRGBDurabilityForDisplay, new Object[]{var2});
+               var11 = Math.round(13.0F - (float)var13 * 13.0F);
+               var12 = var15;
+            }
+
+            if (Config.isCustomColors()) {
+               var12 = CustomColors.getDurabilityColor(var10, var12);
+            }
+
+            if (Reflector.ForgeItem_getDurabilityForDisplay.exists() && Reflector.ForgeItem_getRGBDurabilityForDisplay.exists()) {
+               double var21 = Reflector.callDouble(var2.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, new Object[]{var2});
+               int var22 = Reflector.callInt(var2.getItem(), Reflector.ForgeItem_getRGBDurabilityForDisplay, new Object[]{var2});
+               var11 = Math.round(13.0F - (float)var21 * 13.0F);
+               var12 = var22;
+            }
+
+            if (Config.isCustomColors()) {
+               var12 = CustomColors.getDurabilityColor(var10, var12);
+            }
+
             this.draw(var7, var3 + 2, var4 + 13, 13, 2, 0, 0, 0, 255);
             this.draw(var7, var3 + 2, var4 + 13, var11, 1, var12 >> 16 & 0xFF, var12 >> 8 & 0xFF, var12 & 0xFF, 255);
             GlStateManager.enableBlend();
             GlStateManager.enableAlpha();
             GlStateManager.enableTexture2D();
             GlStateManager.enableLighting();
             GlStateManager.enableDepth();
          }

-         EntityPlayerSP var14 = Minecraft.getMinecraft().player;
-         float var15 = var14 == null ? 0.0F : var14.getCooldownTracker().getCooldown(var2.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());
-         if (var15 > 0.0F) {
+         EntityPlayerSP var17 = Minecraft.getMinecraft().player;
+         float var18 = var17 == null ? 0.0F : var17.getCooldownTracker().getCooldown(var2.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());
+         if (var18 > 0.0F) {
             GlStateManager.disableLighting();
             GlStateManager.disableDepth();
             GlStateManager.disableTexture2D();
-            Tessellator var16 = Tessellator.getInstance();
-            BufferBuilder var17 = var16.getBuffer();
-            this.draw(var17, var3, var4 + MathHelper.floor(16.0F * (1.0F - var15)), 16, MathHelper.ceil(16.0F * var15), 255, 255, 255, 127);
+            Tessellator var19 = Tessellator.getInstance();
+            BufferBuilder var20 = var19.getBuffer();
+            this.draw(var20, var3, var4 + MathHelper.floor(16.0F * (1.0F - var18)), 16, MathHelper.ceil(16.0F * var18), 255, 255, 255, 127);
             GlStateManager.enableTexture2D();
             GlStateManager.enableLighting();
             GlStateManager.enableDepth();
          }
       }
    }
@@ -412,76 +542,76 @@
       this.registerBlock(Blocks.CARPET, EnumDyeColor.PINK.getMetadata(), "pink_carpet");
       this.registerBlock(Blocks.CARPET, EnumDyeColor.PURPLE.getMetadata(), "purple_carpet");
       this.registerBlock(Blocks.CARPET, EnumDyeColor.RED.getMetadata(), "red_carpet");
       this.registerBlock(Blocks.CARPET, EnumDyeColor.SILVER.getMetadata(), "silver_carpet");
       this.registerBlock(Blocks.CARPET, EnumDyeColor.WHITE.getMetadata(), "white_carpet");
       this.registerBlock(Blocks.CARPET, EnumDyeColor.YELLOW.getMetadata(), "yellow_carpet");
-      this.registerBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
-      this.registerBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.NORMAL.getMetadata(), "cobblestone_wall");
-      this.registerBlock(Blocks.DIRT, BlockDirt.DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
-      this.registerBlock(Blocks.DIRT, BlockDirt.DirtType.DIRT.getMetadata(), "dirt");
-      this.registerBlock(Blocks.DIRT, BlockDirt.DirtType.PODZOL.getMetadata(), "podzol");
-      this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.FERN.getMeta(), "double_fern");
-      this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.GRASS.getMeta(), "double_grass");
-      this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta(), "paeonia");
-      this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.ROSE.getMeta(), "double_rose");
-      this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
-      this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta(), "syringa");
-      this.registerBlock(Blocks.LEAVES, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
-      this.registerBlock(Blocks.LEAVES, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
-      this.registerBlock(Blocks.LEAVES, BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
-      this.registerBlock(Blocks.LEAVES, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
-      this.registerBlock(Blocks.LEAVES2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
-      this.registerBlock(Blocks.LEAVES2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
-      this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
-      this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
-      this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
-      this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
-      this.registerBlock(Blocks.LOG2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
-      this.registerBlock(Blocks.LOG2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
-      this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
-      this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
-      this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
-      this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
-      this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
-      this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
-      this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
-      this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
-      this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
-      this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
-      this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
-      this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
-      this.registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
-      this.registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
-      this.registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
-      this.registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
-      this.registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
-      this.registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
-      this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ALLIUM.getMeta(), "allium");
-      this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
-      this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
-      this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
-      this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
-      this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
-      this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.POPPY.getMeta(), "poppy");
-      this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
-      this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
-      this.registerBlock(Blocks.SAND, BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
-      this.registerBlock(Blocks.SAND, BlockSand.EnumType.SAND.getMetadata(), "sand");
-      this.registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
-      this.registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
-      this.registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
-      this.registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
-      this.registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
-      this.registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
-      this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
-      this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
-      this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
-      this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
-      this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
-      this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
+      this.registerBlock(Blocks.COBBLESTONE_WALL, EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
+      this.registerBlock(Blocks.COBBLESTONE_WALL, EnumType.NORMAL.getMetadata(), "cobblestone_wall");
+      this.registerBlock(Blocks.DIRT, DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
+      this.registerBlock(Blocks.DIRT, DirtType.DIRT.getMetadata(), "dirt");
+      this.registerBlock(Blocks.DIRT, DirtType.PODZOL.getMetadata(), "podzol");
+      this.registerBlock(Blocks.DOUBLE_PLANT, EnumPlantType.FERN.getMeta(), "double_fern");
+      this.registerBlock(Blocks.DOUBLE_PLANT, EnumPlantType.GRASS.getMeta(), "double_grass");
+      this.registerBlock(Blocks.DOUBLE_PLANT, EnumPlantType.PAEONIA.getMeta(), "paeonia");
+      this.registerBlock(Blocks.DOUBLE_PLANT, EnumPlantType.ROSE.getMeta(), "double_rose");
+      this.registerBlock(Blocks.DOUBLE_PLANT, EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
+      this.registerBlock(Blocks.DOUBLE_PLANT, EnumPlantType.SYRINGA.getMeta(), "syringa");
+      this.registerBlock(Blocks.LEAVES, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
+      this.registerBlock(Blocks.LEAVES, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
+      this.registerBlock(Blocks.LEAVES, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
+      this.registerBlock(Blocks.LEAVES, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
+      this.registerBlock(Blocks.LEAVES2, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
+      this.registerBlock(Blocks.LEAVES2, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
+      this.registerBlock(Blocks.LOG, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
+      this.registerBlock(Blocks.LOG, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
+      this.registerBlock(Blocks.LOG, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
+      this.registerBlock(Blocks.LOG, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
+      this.registerBlock(Blocks.LOG2, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
+      this.registerBlock(Blocks.LOG2, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
+      this.registerBlock(Blocks.MONSTER_EGG, net.minecraft.block.BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
+      this.registerBlock(Blocks.MONSTER_EGG, net.minecraft.block.BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
+      this.registerBlock(Blocks.MONSTER_EGG, net.minecraft.block.BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
+      this.registerBlock(Blocks.MONSTER_EGG, net.minecraft.block.BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
+      this.registerBlock(Blocks.MONSTER_EGG, net.minecraft.block.BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
+      this.registerBlock(Blocks.MONSTER_EGG, net.minecraft.block.BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
+      this.registerBlock(Blocks.PLANKS, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
+      this.registerBlock(Blocks.PLANKS, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
+      this.registerBlock(Blocks.PLANKS, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
+      this.registerBlock(Blocks.PLANKS, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
+      this.registerBlock(Blocks.PLANKS, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
+      this.registerBlock(Blocks.PLANKS, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
+      this.registerBlock(Blocks.PRISMARINE, net.minecraft.block.BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
+      this.registerBlock(Blocks.PRISMARINE, net.minecraft.block.BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
+      this.registerBlock(Blocks.PRISMARINE, net.minecraft.block.BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
+      this.registerBlock(Blocks.QUARTZ_BLOCK, net.minecraft.block.BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
+      this.registerBlock(Blocks.QUARTZ_BLOCK, net.minecraft.block.BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
+      this.registerBlock(Blocks.QUARTZ_BLOCK, net.minecraft.block.BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
+      this.registerBlock(Blocks.RED_FLOWER, EnumFlowerType.ALLIUM.getMeta(), "allium");
+      this.registerBlock(Blocks.RED_FLOWER, EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
+      this.registerBlock(Blocks.RED_FLOWER, EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
+      this.registerBlock(Blocks.RED_FLOWER, EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
+      this.registerBlock(Blocks.RED_FLOWER, EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
+      this.registerBlock(Blocks.RED_FLOWER, EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
+      this.registerBlock(Blocks.RED_FLOWER, EnumFlowerType.POPPY.getMeta(), "poppy");
+      this.registerBlock(Blocks.RED_FLOWER, EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
+      this.registerBlock(Blocks.RED_FLOWER, EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
+      this.registerBlock(Blocks.SAND, net.minecraft.block.BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
+      this.registerBlock(Blocks.SAND, net.minecraft.block.BlockSand.EnumType.SAND.getMetadata(), "sand");
+      this.registerBlock(Blocks.SANDSTONE, net.minecraft.block.BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
+      this.registerBlock(Blocks.SANDSTONE, net.minecraft.block.BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
+      this.registerBlock(Blocks.SANDSTONE, net.minecraft.block.BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
+      this.registerBlock(Blocks.RED_SANDSTONE, net.minecraft.block.BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
+      this.registerBlock(Blocks.RED_SANDSTONE, net.minecraft.block.BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
+      this.registerBlock(Blocks.RED_SANDSTONE, net.minecraft.block.BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
+      this.registerBlock(Blocks.SAPLING, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
+      this.registerBlock(Blocks.SAPLING, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
+      this.registerBlock(Blocks.SAPLING, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
+      this.registerBlock(Blocks.SAPLING, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
+      this.registerBlock(Blocks.SAPLING, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
+      this.registerBlock(Blocks.SAPLING, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
       this.registerBlock(Blocks.SPONGE, 0, "sponge");
       this.registerBlock(Blocks.SPONGE, 1, "sponge_wet");
       this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass");
       this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass");
       this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass");
       this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass");
@@ -526,41 +656,41 @@
       this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PINK.getMetadata(), "pink_stained_hardened_clay");
       this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_hardened_clay");
       this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.RED.getMetadata(), "red_stained_hardened_clay");
       this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.SILVER.getMetadata(), "silver_stained_hardened_clay");
       this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.WHITE.getMetadata(), "white_stained_hardened_clay");
       this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_hardened_clay");
-      this.registerBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
-      this.registerBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
-      this.registerBlock(Blocks.STONE, BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
-      this.registerBlock(Blocks.STONE, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
-      this.registerBlock(Blocks.STONE, BlockStone.EnumType.GRANITE.getMetadata(), "granite");
-      this.registerBlock(Blocks.STONE, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
-      this.registerBlock(Blocks.STONE, BlockStone.EnumType.STONE.getMetadata(), "stone");
-      this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
-      this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
-      this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
-      this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
-      this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
-      this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
-      this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
-      this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
-      this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
-      this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
-      this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
-      this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
-      this.registerBlock(Blocks.STONE_SLAB2, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
-      this.registerBlock(Blocks.TALLGRASS, BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
-      this.registerBlock(Blocks.TALLGRASS, BlockTallGrass.EnumType.FERN.getMeta(), "fern");
-      this.registerBlock(Blocks.TALLGRASS, BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
-      this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
-      this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
-      this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
-      this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
-      this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
-      this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
+      this.registerBlock(Blocks.STONE, net.minecraft.block.BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
+      this.registerBlock(Blocks.STONE, net.minecraft.block.BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
+      this.registerBlock(Blocks.STONE, net.minecraft.block.BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
+      this.registerBlock(Blocks.STONE, net.minecraft.block.BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
+      this.registerBlock(Blocks.STONE, net.minecraft.block.BlockStone.EnumType.GRANITE.getMetadata(), "granite");
+      this.registerBlock(Blocks.STONE, net.minecraft.block.BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
+      this.registerBlock(Blocks.STONE, net.minecraft.block.BlockStone.EnumType.STONE.getMetadata(), "stone");
+      this.registerBlock(Blocks.STONEBRICK, net.minecraft.block.BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
+      this.registerBlock(Blocks.STONEBRICK, net.minecraft.block.BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
+      this.registerBlock(Blocks.STONEBRICK, net.minecraft.block.BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
+      this.registerBlock(Blocks.STONEBRICK, net.minecraft.block.BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
+      this.registerBlock(Blocks.STONE_SLAB, net.minecraft.block.BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
+      this.registerBlock(Blocks.STONE_SLAB, net.minecraft.block.BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
+      this.registerBlock(Blocks.STONE_SLAB, net.minecraft.block.BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
+      this.registerBlock(Blocks.STONE_SLAB, net.minecraft.block.BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
+      this.registerBlock(Blocks.STONE_SLAB, net.minecraft.block.BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
+      this.registerBlock(Blocks.STONE_SLAB, net.minecraft.block.BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
+      this.registerBlock(Blocks.STONE_SLAB, net.minecraft.block.BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
+      this.registerBlock(Blocks.STONE_SLAB, net.minecraft.block.BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
+      this.registerBlock(Blocks.STONE_SLAB2, net.minecraft.block.BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
+      this.registerBlock(Blocks.TALLGRASS, net.minecraft.block.BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
+      this.registerBlock(Blocks.TALLGRASS, net.minecraft.block.BlockTallGrass.EnumType.FERN.getMeta(), "fern");
+      this.registerBlock(Blocks.TALLGRASS, net.minecraft.block.BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
+      this.registerBlock(Blocks.WOODEN_SLAB, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
+      this.registerBlock(Blocks.WOODEN_SLAB, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
+      this.registerBlock(Blocks.WOODEN_SLAB, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
+      this.registerBlock(Blocks.WOODEN_SLAB, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
+      this.registerBlock(Blocks.WOODEN_SLAB, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
+      this.registerBlock(Blocks.WOODEN_SLAB, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
       this.registerBlock(Blocks.WOOL, EnumDyeColor.BLACK.getMetadata(), "black_wool");
       this.registerBlock(Blocks.WOOL, EnumDyeColor.BLUE.getMetadata(), "blue_wool");
       this.registerBlock(Blocks.WOOL, EnumDyeColor.BROWN.getMetadata(), "brown_wool");
       this.registerBlock(Blocks.WOOL, EnumDyeColor.CYAN.getMetadata(), "cyan_wool");
       this.registerBlock(Blocks.WOOL, EnumDyeColor.GRAY.getMetadata(), "gray_wool");
       this.registerBlock(Blocks.WOOL, EnumDyeColor.GREEN.getMetadata(), "green_wool");
@@ -683,13 +813,13 @@
       this.registerBlock(Blocks.TRIPWIRE_HOOK, "tripwire_hook");
       this.registerBlock(Blocks.VINE, "vine");
       this.registerBlock(Blocks.WATERLILY, "waterlily");
       this.registerBlock(Blocks.WEB, "web");
       this.registerBlock(Blocks.WOODEN_BUTTON, "wooden_button");
       this.registerBlock(Blocks.WOODEN_PRESSURE_PLATE, "wooden_pressure_plate");
-      this.registerBlock(Blocks.YELLOW_FLOWER, BlockFlower.EnumFlowerType.DANDELION.getMeta(), "dandelion");
+      this.registerBlock(Blocks.YELLOW_FLOWER, EnumFlowerType.DANDELION.getMeta(), "dandelion");
       this.registerBlock(Blocks.END_ROD, "end_rod");
       this.registerBlock(Blocks.CHORUS_PLANT, "chorus_plant");
       this.registerBlock(Blocks.CHORUS_FLOWER, "chorus_flower");
       this.registerBlock(Blocks.PURPUR_BLOCK, "purpur_block");
       this.registerBlock(Blocks.PURPUR_PILLAR, "purpur_pillar");
       this.registerBlock(Blocks.PURPUR_STAIRS, "purpur_stairs");
@@ -847,18 +977,18 @@
       this.registerItem(Items.FURNACE_MINECART, "furnace_minecart");
       this.registerItem(Items.EGG, "egg");
       this.registerItem(Items.COMPASS, "compass");
       this.registerItem(Items.FISHING_ROD, "fishing_rod");
       this.registerItem(Items.CLOCK, "clock");
       this.registerItem(Items.GLOWSTONE_DUST, "glowstone_dust");
-      this.registerItem(Items.FISH, ItemFishFood.FishType.COD.getMetadata(), "cod");
-      this.registerItem(Items.FISH, ItemFishFood.FishType.SALMON.getMetadata(), "salmon");
-      this.registerItem(Items.FISH, ItemFishFood.FishType.CLOWNFISH.getMetadata(), "clownfish");
-      this.registerItem(Items.FISH, ItemFishFood.FishType.PUFFERFISH.getMetadata(), "pufferfish");
-      this.registerItem(Items.COOKED_FISH, ItemFishFood.FishType.COD.getMetadata(), "cooked_cod");
-      this.registerItem(Items.COOKED_FISH, ItemFishFood.FishType.SALMON.getMetadata(), "cooked_salmon");
+      this.registerItem(Items.FISH, FishType.COD.getMetadata(), "cod");
+      this.registerItem(Items.FISH, FishType.SALMON.getMetadata(), "salmon");
+      this.registerItem(Items.FISH, FishType.CLOWNFISH.getMetadata(), "clownfish");
+      this.registerItem(Items.FISH, FishType.PUFFERFISH.getMetadata(), "pufferfish");
+      this.registerItem(Items.COOKED_FISH, FishType.COD.getMetadata(), "cooked_cod");
+      this.registerItem(Items.COOKED_FISH, FishType.SALMON.getMetadata(), "cooked_salmon");
       this.registerItem(Items.DYE, EnumDyeColor.BLACK.getDyeDamage(), "dye_black");
       this.registerItem(Items.DYE, EnumDyeColor.RED.getDyeDamage(), "dye_red");
       this.registerItem(Items.DYE, EnumDyeColor.GREEN.getDyeDamage(), "dye_green");
       this.registerItem(Items.DYE, EnumDyeColor.BROWN.getDyeDamage(), "dye_brown");
       this.registerItem(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), "dye_blue");
       this.registerItem(Items.DYE, EnumDyeColor.PURPLE.getDyeDamage(), "dye_purple");
@@ -1002,21 +1132,24 @@
       this.registerBlock(Blocks.COMMAND_BLOCK, "command_block");
       this.registerItem(Items.FIREWORKS, "fireworks");
       this.registerItem(Items.COMMAND_BLOCK_MINECART, "command_block_minecart");
       this.registerBlock(Blocks.BARRIER, "barrier");
       this.registerBlock(Blocks.MOB_SPAWNER, "mob_spawner");
       this.registerItem(Items.WRITTEN_BOOK, "written_book");
-      this.registerBlock(Blocks.BROWN_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
-      this.registerBlock(Blocks.RED_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
+      this.registerBlock(Blocks.BROWN_MUSHROOM_BLOCK, net.minecraft.block.BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
+      this.registerBlock(Blocks.RED_MUSHROOM_BLOCK, net.minecraft.block.BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
       this.registerBlock(Blocks.DRAGON_EGG, "dragon_egg");
       this.registerBlock(Blocks.REPEATING_COMMAND_BLOCK, "repeating_command_block");
       this.registerBlock(Blocks.CHAIN_COMMAND_BLOCK, "chain_command_block");
-      this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.SAVE.getModeId(), "structure_block");
-      this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.LOAD.getModeId(), "structure_block");
-      this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.CORNER.getModeId(), "structure_block");
-      this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.DATA.getModeId(), "structure_block");
+      this.registerBlock(Blocks.STRUCTURE_BLOCK, Mode.SAVE.getModeId(), "structure_block");
+      this.registerBlock(Blocks.STRUCTURE_BLOCK, Mode.LOAD.getModeId(), "structure_block");
+      this.registerBlock(Blocks.STRUCTURE_BLOCK, Mode.CORNER.getModeId(), "structure_block");
+      this.registerBlock(Blocks.STRUCTURE_BLOCK, Mode.DATA.getModeId(), "structure_block");
+      if (Reflector.ModelLoader_onRegisterItems.exists()) {
+         Reflector.call(Reflector.ModelLoader_onRegisterItems, new Object[]{this.itemModelMesher});
+      }
    }

    public void onResourceManagerReload(IResourceManager var1) {
       this.itemModelMesher.rebuildCache();
    }
 }
 */