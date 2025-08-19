package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.util.math.BlockPos;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Mixin(targets = "net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace")
@SuppressWarnings("unused")
public abstract class MixinBlockModelRender$AmbientOcclusionFace {

    @AccessTransformer(name = "<class>", access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
    public abstract void access();

    @Shadow @Final
    private float[] vertexColorMultiplier;

    @Shadow @Final
    private int[] vertexBrightness;

    @Unique
    private final BlockPos.MutableBlockPos[] blockPosArr = new BlockPos.MutableBlockPos[5];

    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectConstructor(BlockModelRenderer p_i46235_1, CallbackInfo ci){
        for (int i = 0; i < this.blockPosArr.length; i++) {
            this.blockPosArr[i] = new BlockPos.MutableBlockPos();
        }
    }

    @NewConstructor
    public void AmbientOcclusionFace(BlockModelRenderer bmr) {
        for (int i = 0; i < this.blockPosArr.length; i++) {
            this.blockPosArr[i] = new BlockPos.MutableBlockPos();
        }
    }

    @Unique
    public void setMaxBlockLight() {
        int maxBlockLight = 240;
        this.vertexBrightness[0] = this.vertexBrightness[0] | maxBlockLight;
        this.vertexBrightness[1] = this.vertexBrightness[1] | maxBlockLight;
        this.vertexBrightness[2] = this.vertexBrightness[2] | maxBlockLight;
        this.vertexBrightness[3] = this.vertexBrightness[3] | maxBlockLight;
        this.vertexColorMultiplier[0] = 1.0F;
        this.vertexColorMultiplier[1] = 1.0F;
        this.vertexColorMultiplier[2] = 1.0F;
        this.vertexColorMultiplier[3] = 1.0F;
    }

    //TODO
    //MutableBlockPos blockpos$pooledmutableblockpos1 = this.blockPosArr[1].setPos(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[0]);
    //BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = PooledMutableBlockPos.retain(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[0]);


}
/*
--- net/minecraft/client/renderer/BlockModelRenderer.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/BlockModelRenderer.java	Tue Aug 19 14:59:58 2025
@@ -4,261 +4,367 @@
 import java.util.List;
 import javax.annotation.Nullable;
 import net.minecraft.block.Block;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.block.model.BakedQuad;
+import net.minecraft.client.renderer.block.model.FaceBakery;
 import net.minecraft.client.renderer.block.model.IBakedModel;
 import net.minecraft.client.renderer.color.BlockColors;
 import net.minecraft.client.renderer.texture.TextureUtil;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.crash.CrashReport;
 import net.minecraft.crash.CrashReportCategory;
+import net.minecraft.util.BlockRenderLayer;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.ReportedException;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.Vec3d;
 import net.minecraft.util.math.Vec3i;
+import net.minecraft.util.math.BlockPos.MutableBlockPos;
 import net.minecraft.world.IBlockAccess;
+import net.optifine.BetterSnow;
+import net.optifine.CustomColors;
+import net.optifine.model.BlockModelCustomizer;
+import net.optifine.model.ListQuadsOverlay;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;
+import net.optifine.render.RenderEnv;
+import net.optifine.shaders.SVertexBuilder;
+import net.optifine.shaders.Shaders;

 public class BlockModelRenderer {
    private final BlockColors blockColors;
+   private static float aoLightValueOpaque = 0.2F;
+   private static boolean separateAoLightValue = false;
+   private static final BlockRenderLayer[] OVERLAY_LAYERS = new BlockRenderLayer[]{
+      BlockRenderLayer.CUTOUT, BlockRenderLayer.CUTOUT_MIPPED, BlockRenderLayer.TRANSLUCENT
+   };

    public BlockModelRenderer(BlockColors var1) {
       this.blockColors = var1;
+      if (Reflector.ForgeModContainer_forgeLightPipelineEnabled.exists()) {
+         Reflector.setFieldValue(Reflector.ForgeModContainer_forgeLightPipelineEnabled, false);
+      }
    }

    public boolean renderModel(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, BufferBuilder var5, boolean var6) {
       return this.renderModel(var1, var2, var3, var4, var5, var6, MathHelper.getPositionRandom(var4));
    }

    public boolean renderModel(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, BufferBuilder var5, boolean var6, long var7) {
-      boolean var9 = Minecraft.isAmbientOcclusionEnabled() && var3.getLightValue() == 0 && var2.isAmbientOcclusion();
+      boolean var9 = Minecraft.isAmbientOcclusionEnabled()
+         && ReflectorForge.getLightValue(var3, var1, var4) == 0
+         && ReflectorForge.isAmbientOcclusion(var2, var3);

       try {
-         return var9 ? this.renderModelSmooth(var1, var2, var3, var4, var5, var6, var7) : this.renderModelFlat(var1, var2, var3, var4, var5, var6, var7);
+         if (Config.isShaders()) {
+            SVertexBuilder.pushEntity(var3, var4, var1, var5);
+         }
+
+         if (!Config.isAlternateBlocks()) {
+            var7 = 0L;
+         }
+
+         RenderEnv var10 = var5.getRenderEnv(var3, var4);
+         var2 = BlockModelCustomizer.getRenderModel(var2, var3, var10);
+         boolean var15 = var9
+            ? this.renderModelSmooth(var1, var2, var3, var4, var5, var6, var7)
+            : this.renderModelFlat(var1, var2, var3, var4, var5, var6, var7);
+         if (var15) {
+            this.renderOverlayModels(var1, var2, var3, var4, var5, var6, var7, var10, var9);
+         }
+
+         if (Config.isShaders()) {
+            SVertexBuilder.popEntity(var5);
+         }
+
+         return var15;
       } catch (Throwable var13) {
          CrashReport var11 = CrashReport.makeCrashReport(var13, "Tesselating block model");
          CrashReportCategory var12 = var11.makeCategory("Block model being tesselated");
          CrashReportCategory.addBlockInfo(var12, var4, var3);
          var12.addCrashSection("Using AO", var9);
          throw new ReportedException(var11);
       }
    }

    public boolean renderModelSmooth(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, BufferBuilder var5, boolean var6, long var7) {
       boolean var9 = false;
-      float[] var10 = new float[EnumFacing.values().length * 2];
-      BitSet var11 = new BitSet(3);
-      BlockModelRenderer.AmbientOcclusionFace var12 = new BlockModelRenderer.AmbientOcclusionFace();
-
-      for (EnumFacing var16 : EnumFacing.values()) {
-         List var17 = var2.getQuads(var3, var16, var7);
-         if (!var17.isEmpty() && (!var6 || var3.shouldSideBeRendered(var1, var4, var16))) {
-            this.renderQuadsSmooth(var1, var3, var4, var5, var17, var10, var11, var12);
+      RenderEnv var10 = var5.getRenderEnv(var3, var4);
+      BlockRenderLayer var11 = var5.getBlockLayer();
+
+      for (EnumFacing var15 : EnumFacing.VALUES) {
+         List var16 = var2.getQuads(var3, var15, var7);
+         if (!var16.isEmpty() && (!var6 || var3.c(var1, var4, var15))) {
+            var16 = BlockModelCustomizer.getRenderQuads(var16, var1, var3, var4, var15, var11, var7, var10);
+            this.renderQuadsSmooth(var1, var3, var4, var5, var16, var10);
             var9 = true;
          }
       }

-      List var18 = var2.getQuads(var3, null, var7);
-      if (!var18.isEmpty()) {
-         this.renderQuadsSmooth(var1, var3, var4, var5, var18, var10, var11, var12);
+      List var17 = var2.getQuads(var3, (EnumFacing)null, var7);
+      if (!var17.isEmpty()) {
+         var17 = BlockModelCustomizer.getRenderQuads(var17, var1, var3, var4, null, var11, var7, var10);
+         this.renderQuadsSmooth(var1, var3, var4, var5, var17, var10);
          var9 = true;
       }

       return var9;
    }

    public boolean renderModelFlat(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, BufferBuilder var5, boolean var6, long var7) {
       boolean var9 = false;
-      BitSet var10 = new BitSet(3);
+      RenderEnv var10 = var5.getRenderEnv(var3, var4);
+      BlockRenderLayer var11 = var5.getBlockLayer();

-      for (EnumFacing var14 : EnumFacing.values()) {
-         List var15 = var2.getQuads(var3, var14, var7);
-         if (!var15.isEmpty() && (!var6 || var3.shouldSideBeRendered(var1, var4, var14))) {
-            int var16 = var3.getPackedLightmapCoords(var1, var4.offset(var14));
-            this.renderQuadsFlat(var1, var3, var4, var16, false, var5, var15, var10);
+      for (EnumFacing var15 : EnumFacing.VALUES) {
+         List var16 = var2.getQuads(var3, var15, var7);
+         if (!var16.isEmpty() && (!var6 || var3.c(var1, var4, var15))) {
+            int var17 = var3.b(var1, var4.offset(var15));
+            var16 = BlockModelCustomizer.getRenderQuads(var16, var1, var3, var4, var15, var11, var7, var10);
+            this.renderQuadsFlat(var1, var3, var4, var17, false, var5, var16, var10);
             var9 = true;
          }
       }

-      List var17 = var2.getQuads(var3, null, var7);
-      if (!var17.isEmpty()) {
-         this.renderQuadsFlat(var1, var3, var4, -1, true, var5, var17, var10);
+      List var18 = var2.getQuads(var3, (EnumFacing)null, var7);
+      if (!var18.isEmpty()) {
+         var18 = BlockModelCustomizer.getRenderQuads(var18, var1, var3, var4, null, var11, var7, var10);
+         this.renderQuadsFlat(var1, var3, var4, -1, true, var5, var18, var10);
          var9 = true;
       }

       return var9;
    }

-   private void renderQuadsSmooth(
-      IBlockAccess var1,
-      IBlockState var2,
-      BlockPos var3,
-      BufferBuilder var4,
-      List<BakedQuad> var5,
-      float[] var6,
-      BitSet var7,
-      BlockModelRenderer.AmbientOcclusionFace var8
-   ) {
-      Vec3d var9 = var2.getOffset(var1, var3);
-      double var10 = var3.getX() + var9.x;
-      double var12 = var3.getY() + var9.y;
-      double var14 = var3.getZ() + var9.z;
-      int var16 = 0;
-
-      for (int var17 = var5.size(); var16 < var17; var16++) {
-         BakedQuad var18 = (BakedQuad)var5.get(var16);
-         this.fillQuadBounds(var2, var18.getVertexData(), var18.getFace(), var6, var7);
-         var8.updateVertexBrightness(var1, var2, var3, var18.getFace(), var6, var7);
-         var4.addVertexData(var18.getVertexData());
-         var4.putBrightness4(var8.vertexBrightness[0], var8.vertexBrightness[1], var8.vertexBrightness[2], var8.vertexBrightness[3]);
-         if (var18.hasTintIndex()) {
-            int var19 = this.blockColors.colorMultiplier(var2, var1, var3, var18.getTintIndex());
+   private void renderQuadsSmooth(IBlockAccess var1, IBlockState var2, BlockPos var3, BufferBuilder var4, List<BakedQuad> var5, RenderEnv var6) {
+      float[] var7 = var6.getQuadBounds();
+      BitSet var8 = var6.getBoundsFlags();
+      BlockModelRenderer.AmbientOcclusionFace var9 = var6.getAoFace();
+      Vec3d var10 = var2.f(var1, var3);
+      double var11 = var3.getX() + var10.x;
+      double var13 = var3.getY() + var10.y;
+      double var15 = var3.getZ() + var10.z;
+      int var17 = 0;
+
+      for (int var18 = var5.size(); var17 < var18; var17++) {
+         BakedQuad var19 = (BakedQuad)var5.get(var17);
+         this.fillQuadBounds(var2, var19.getVertexData(), var19.getFace(), var7, var8);
+         var9.updateVertexBrightness(var1, var2, var3, var19.getFace(), var7, var8);
+         if (var19.getSprite().isEmissive) {
+            var9.setMaxBlockLight();
+         }
+
+         if (var4.isMultiTexture()) {
+            var4.addVertexData(var19.getVertexDataSingle());
+         } else {
+            var4.addVertexData(var19.getVertexData());
+         }
+
+         var4.putSprite(var19.getSprite());
+         var4.putBrightness4(var9.vertexBrightness[0], var9.vertexBrightness[1], var9.vertexBrightness[2], var9.vertexBrightness[3]);
+         if (var19.shouldApplyDiffuseLighting()) {
+            float var20 = FaceBakery.getFaceBrightness(var19.getFace());
+            var9.vertexColorMultiplier[0] *= var20;
+            var9.vertexColorMultiplier[1] *= var20;
+            var9.vertexColorMultiplier[2] *= var20;
+            var9.vertexColorMultiplier[3] *= var20;
+         }
+
+         int var25 = CustomColors.getColorMultiplier(var19, var2, var1, var3, var6);
+         if (var19.hasTintIndex() || var25 != -1) {
+            int var21 = var25;
+            if (var25 == -1) {
+               var21 = this.blockColors.colorMultiplier(var2, var1, var3, var19.getTintIndex());
+            }
+
             if (EntityRenderer.anaglyphEnable) {
-               var19 = TextureUtil.anaglyphColor(var19);
+               var21 = TextureUtil.anaglyphColor(var21);
             }

-            float var20 = (var19 >> 16 & 0xFF) / 255.0F;
-            float var21 = (var19 >> 8 & 0xFF) / 255.0F;
-            float var22 = (var19 & 0xFF) / 255.0F;
-            var4.putColorMultiplier(var8.vertexColorMultiplier[0] * var20, var8.vertexColorMultiplier[0] * var21, var8.vertexColorMultiplier[0] * var22, 4);
-            var4.putColorMultiplier(var8.vertexColorMultiplier[1] * var20, var8.vertexColorMultiplier[1] * var21, var8.vertexColorMultiplier[1] * var22, 3);
-            var4.putColorMultiplier(var8.vertexColorMultiplier[2] * var20, var8.vertexColorMultiplier[2] * var21, var8.vertexColorMultiplier[2] * var22, 2);
-            var4.putColorMultiplier(var8.vertexColorMultiplier[3] * var20, var8.vertexColorMultiplier[3] * var21, var8.vertexColorMultiplier[3] * var22, 1);
+            float var22 = (var21 >> 16 & 0xFF) / 255.0F;
+            float var23 = (var21 >> 8 & 0xFF) / 255.0F;
+            float var24 = (var21 & 0xFF) / 255.0F;
+            if (separateAoLightValue) {
+               var4.putColorMultiplierRgba(var22, var23, var24, var9.vertexColorMultiplier[0], 4);
+               var4.putColorMultiplierRgba(var22, var23, var24, var9.vertexColorMultiplier[1], 3);
+               var4.putColorMultiplierRgba(var22, var23, var24, var9.vertexColorMultiplier[2], 2);
+               var4.putColorMultiplierRgba(var22, var23, var24, var9.vertexColorMultiplier[3], 1);
+            } else {
+               var4.putColorMultiplier(var9.vertexColorMultiplier[0] * var22, var9.vertexColorMultiplier[0] * var23, var9.vertexColorMultiplier[0] * var24, 4);
+               var4.putColorMultiplier(var9.vertexColorMultiplier[1] * var22, var9.vertexColorMultiplier[1] * var23, var9.vertexColorMultiplier[1] * var24, 3);
+               var4.putColorMultiplier(var9.vertexColorMultiplier[2] * var22, var9.vertexColorMultiplier[2] * var23, var9.vertexColorMultiplier[2] * var24, 2);
+               var4.putColorMultiplier(var9.vertexColorMultiplier[3] * var22, var9.vertexColorMultiplier[3] * var23, var9.vertexColorMultiplier[3] * var24, 1);
+            }
+         } else if (separateAoLightValue) {
+            var4.putColorMultiplierRgba(1.0F, 1.0F, 1.0F, var9.vertexColorMultiplier[0], 4);
+            var4.putColorMultiplierRgba(1.0F, 1.0F, 1.0F, var9.vertexColorMultiplier[1], 3);
+            var4.putColorMultiplierRgba(1.0F, 1.0F, 1.0F, var9.vertexColorMultiplier[2], 2);
+            var4.putColorMultiplierRgba(1.0F, 1.0F, 1.0F, var9.vertexColorMultiplier[3], 1);
          } else {
-            var4.putColorMultiplier(var8.vertexColorMultiplier[0], var8.vertexColorMultiplier[0], var8.vertexColorMultiplier[0], 4);
-            var4.putColorMultiplier(var8.vertexColorMultiplier[1], var8.vertexColorMultiplier[1], var8.vertexColorMultiplier[1], 3);
-            var4.putColorMultiplier(var8.vertexColorMultiplier[2], var8.vertexColorMultiplier[2], var8.vertexColorMultiplier[2], 2);
-            var4.putColorMultiplier(var8.vertexColorMultiplier[3], var8.vertexColorMultiplier[3], var8.vertexColorMultiplier[3], 1);
+            var4.putColorMultiplier(var9.vertexColorMultiplier[0], var9.vertexColorMultiplier[0], var9.vertexColorMultiplier[0], 4);
+            var4.putColorMultiplier(var9.vertexColorMultiplier[1], var9.vertexColorMultiplier[1], var9.vertexColorMultiplier[1], 3);
+            var4.putColorMultiplier(var9.vertexColorMultiplier[2], var9.vertexColorMultiplier[2], var9.vertexColorMultiplier[2], 2);
+            var4.putColorMultiplier(var9.vertexColorMultiplier[3], var9.vertexColorMultiplier[3], var9.vertexColorMultiplier[3], 1);
          }

-         var4.putPosition(var10, var12, var14);
+         var4.putPosition(var11, var13, var15);
       }
    }

    private void fillQuadBounds(IBlockState var1, int[] var2, EnumFacing var3, @Nullable float[] var4, BitSet var5) {
       float var6 = 32.0F;
       float var7 = 32.0F;
       float var8 = 32.0F;
       float var9 = -32.0F;
       float var10 = -32.0F;
       float var11 = -32.0F;
+      int var12 = var2.length / 4;

-      for (int var12 = 0; var12 < 4; var12++) {
-         float var13 = Float.intBitsToFloat(var2[var12 * 7]);
-         float var14 = Float.intBitsToFloat(var2[var12 * 7 + 1]);
-         float var15 = Float.intBitsToFloat(var2[var12 * 7 + 2]);
-         var6 = Math.min(var6, var13);
-         var7 = Math.min(var7, var14);
-         var8 = Math.min(var8, var15);
-         var9 = Math.max(var9, var13);
-         var10 = Math.max(var10, var14);
-         var11 = Math.max(var11, var15);
+      for (int var13 = 0; var13 < 4; var13++) {
+         float var14 = Float.intBitsToFloat(var2[var13 * var12]);
+         float var15 = Float.intBitsToFloat(var2[var13 * var12 + 1]);
+         float var16 = Float.intBitsToFloat(var2[var13 * var12 + 2]);
+         var6 = Math.min(var6, var14);
+         var7 = Math.min(var7, var15);
+         var8 = Math.min(var8, var16);
+         var9 = Math.max(var9, var14);
+         var10 = Math.max(var10, var15);
+         var11 = Math.max(var11, var16);
       }

       if (var4 != null) {
          var4[EnumFacing.WEST.getIndex()] = var6;
          var4[EnumFacing.EAST.getIndex()] = var9;
          var4[EnumFacing.DOWN.getIndex()] = var7;
          var4[EnumFacing.UP.getIndex()] = var10;
          var4[EnumFacing.NORTH.getIndex()] = var8;
          var4[EnumFacing.SOUTH.getIndex()] = var11;
-         int var16 = EnumFacing.values().length;
-         var4[EnumFacing.WEST.getIndex() + var16] = 1.0F - var6;
-         var4[EnumFacing.EAST.getIndex() + var16] = 1.0F - var9;
-         var4[EnumFacing.DOWN.getIndex() + var16] = 1.0F - var7;
-         var4[EnumFacing.UP.getIndex() + var16] = 1.0F - var10;
-         var4[EnumFacing.NORTH.getIndex() + var16] = 1.0F - var8;
-         var4[EnumFacing.SOUTH.getIndex() + var16] = 1.0F - var11;
+         int var17 = EnumFacing.VALUES.length;
+         var4[EnumFacing.WEST.getIndex() + var17] = 1.0F - var6;
+         var4[EnumFacing.EAST.getIndex() + var17] = 1.0F - var9;
+         var4[EnumFacing.DOWN.getIndex() + var17] = 1.0F - var7;
+         var4[EnumFacing.UP.getIndex() + var17] = 1.0F - var10;
+         var4[EnumFacing.NORTH.getIndex() + var17] = 1.0F - var8;
+         var4[EnumFacing.SOUTH.getIndex() + var17] = 1.0F - var11;
       }

-      float var17 = 1.0E-4F;
-      float var18 = 0.9999F;
+      float var18 = 1.0E-4F;
+      float var19 = 0.9999F;
       switch (var3) {
          case DOWN:
             var5.set(1, var6 >= 1.0E-4F || var8 >= 1.0E-4F || var9 <= 0.9999F || var11 <= 0.9999F);
-            var5.set(0, (var7 < 1.0E-4F || var1.isFullCube()) && var7 == var10);
+            var5.set(0, (var7 < 1.0E-4F || var1.g()) && var7 == var10);
             break;
          case UP:
             var5.set(1, var6 >= 1.0E-4F || var8 >= 1.0E-4F || var9 <= 0.9999F || var11 <= 0.9999F);
-            var5.set(0, (var10 > 0.9999F || var1.isFullCube()) && var7 == var10);
+            var5.set(0, (var10 > 0.9999F || var1.g()) && var7 == var10);
             break;
          case NORTH:
             var5.set(1, var6 >= 1.0E-4F || var7 >= 1.0E-4F || var9 <= 0.9999F || var10 <= 0.9999F);
-            var5.set(0, (var8 < 1.0E-4F || var1.isFullCube()) && var8 == var11);
+            var5.set(0, (var8 < 1.0E-4F || var1.g()) && var8 == var11);
             break;
          case SOUTH:
             var5.set(1, var6 >= 1.0E-4F || var7 >= 1.0E-4F || var9 <= 0.9999F || var10 <= 0.9999F);
-            var5.set(0, (var11 > 0.9999F || var1.isFullCube()) && var8 == var11);
+            var5.set(0, (var11 > 0.9999F || var1.g()) && var8 == var11);
             break;
          case WEST:
             var5.set(1, var7 >= 1.0E-4F || var8 >= 1.0E-4F || var10 <= 0.9999F || var11 <= 0.9999F);
-            var5.set(0, (var6 < 1.0E-4F || var1.isFullCube()) && var6 == var9);
+            var5.set(0, (var6 < 1.0E-4F || var1.g()) && var6 == var9);
             break;
          case EAST:
             var5.set(1, var7 >= 1.0E-4F || var8 >= 1.0E-4F || var10 <= 0.9999F || var11 <= 0.9999F);
-            var5.set(0, (var9 > 0.9999F || var1.isFullCube()) && var6 == var9);
+            var5.set(0, (var9 > 0.9999F || var1.g()) && var6 == var9);
       }
    }

    private void renderQuadsFlat(
-      IBlockAccess var1, IBlockState var2, BlockPos var3, int var4, boolean var5, BufferBuilder var6, List<BakedQuad> var7, BitSet var8
+      IBlockAccess var1, IBlockState var2, BlockPos var3, int var4, boolean var5, BufferBuilder var6, List<BakedQuad> var7, RenderEnv var8
    ) {
-      Vec3d var9 = var2.getOffset(var1, var3);
-      double var10 = var3.getX() + var9.x;
-      double var12 = var3.getY() + var9.y;
-      double var14 = var3.getZ() + var9.z;
-      int var16 = 0;
+      BitSet var9 = var8.getBoundsFlags();
+      Vec3d var10 = var2.f(var1, var3);
+      double var11 = var3.getX() + var10.x;
+      double var13 = var3.getY() + var10.y;
+      double var15 = var3.getZ() + var10.z;
+      int var17 = 0;

-      for (int var17 = var7.size(); var16 < var17; var16++) {
-         BakedQuad var18 = (BakedQuad)var7.get(var16);
+      for (int var18 = var7.size(); var17 < var18; var17++) {
+         BakedQuad var19 = (BakedQuad)var7.get(var17);
          if (var5) {
-            this.fillQuadBounds(var2, var18.getVertexData(), var18.getFace(), null, var8);
-            BlockPos var19 = var8.get(0) ? var3.offset(var18.getFace()) : var3;
-            var4 = var2.getPackedLightmapCoords(var1, var19);
+            this.fillQuadBounds(var2, var19.getVertexData(), var19.getFace(), (float[])null, var9);
+            BlockPos var20 = var9.get(0) ? var3.offset(var19.getFace()) : var3;
+            var4 = var2.b(var1, var20);
+         }
+
+         if (var19.getSprite().isEmissive) {
+            var4 |= 240;
+         }
+
+         if (var6.isMultiTexture()) {
+            var6.addVertexData(var19.getVertexDataSingle());
+         } else {
+            var6.addVertexData(var19.getVertexData());
          }

-         var6.addVertexData(var18.getVertexData());
+         var6.putSprite(var19.getSprite());
          var6.putBrightness4(var4, var4, var4, var4);
-         if (var18.hasTintIndex()) {
-            int var23 = this.blockColors.colorMultiplier(var2, var1, var3, var18.getTintIndex());
+         int var26 = CustomColors.getColorMultiplier(var19, var2, var1, var3, var8);
+         if (var19.hasTintIndex() || var26 != -1) {
+            int var27 = var26;
+            if (var26 == -1) {
+               var27 = this.blockColors.colorMultiplier(var2, var1, var3, var19.getTintIndex());
+            }
+
             if (EntityRenderer.anaglyphEnable) {
-               var23 = TextureUtil.anaglyphColor(var23);
+               var27 = TextureUtil.anaglyphColor(var27);
             }

-            float var20 = (var23 >> 16 & 0xFF) / 255.0F;
-            float var21 = (var23 >> 8 & 0xFF) / 255.0F;
-            float var22 = (var23 & 0xFF) / 255.0F;
-            var6.putColorMultiplier(var20, var21, var22, 4);
-            var6.putColorMultiplier(var20, var21, var22, 3);
-            var6.putColorMultiplier(var20, var21, var22, 2);
-            var6.putColorMultiplier(var20, var21, var22, 1);
+            float var22 = (var27 >> 16 & 0xFF) / 255.0F;
+            float var23 = (var27 >> 8 & 0xFF) / 255.0F;
+            float var24 = (var27 & 0xFF) / 255.0F;
+            if (var19.shouldApplyDiffuseLighting()) {
+               float var25 = FaceBakery.getFaceBrightness(var19.getFace());
+               var22 *= var25;
+               var23 *= var25;
+               var24 *= var25;
+            }
+
+            var6.putColorMultiplier(var22, var23, var24, 4);
+            var6.putColorMultiplier(var22, var23, var24, 3);
+            var6.putColorMultiplier(var22, var23, var24, 2);
+            var6.putColorMultiplier(var22, var23, var24, 1);
+         } else if (var19.shouldApplyDiffuseLighting()) {
+            float var21 = FaceBakery.getFaceBrightness(var19.getFace());
+            var6.putColorMultiplier(var21, var21, var21, 4);
+            var6.putColorMultiplier(var21, var21, var21, 3);
+            var6.putColorMultiplier(var21, var21, var21, 2);
+            var6.putColorMultiplier(var21, var21, var21, 1);
          }

-         var6.putPosition(var10, var12, var14);
+         var6.putPosition(var11, var13, var15);
       }
    }

    public void renderModelBrightnessColor(IBakedModel var1, float var2, float var3, float var4, float var5) {
-      this.renderModelBrightnessColor(null, var1, var2, var3, var4, var5);
+      this.renderModelBrightnessColor((IBlockState)null, var1, var2, var3, var4, var5);
    }

    public void renderModelBrightnessColor(IBlockState var1, IBakedModel var2, float var3, float var4, float var5, float var6) {
-      for (EnumFacing var10 : EnumFacing.values()) {
+      for (EnumFacing var10 : EnumFacing.VALUES) {
          this.renderModelBrightnessColorQuads(var3, var4, var5, var6, var2.getQuads(var1, var10, 0L));
       }

-      this.renderModelBrightnessColorQuads(var3, var4, var5, var6, var2.getQuads(var1, null, 0L));
+      this.renderModelBrightnessColorQuads(var3, var4, var5, var6, var2.getQuads(var1, (EnumFacing)null, 0L));
    }

    public void renderModelBrightness(IBakedModel var1, IBlockState var2, float var3, boolean var4) {
       Block var5 = var2.getBlock();
       GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
-      int var6 = this.blockColors.colorMultiplier(var2, null, null, 0);
+      int var6 = this.blockColors.colorMultiplier(var2, (IBlockAccess)null, (BlockPos)null, 0);
       if (EntityRenderer.anaglyphEnable) {
          var6 = TextureUtil.anaglyphColor(var6);
       }

       float var7 = (var6 >> 16 & 0xFF) / 255.0F;
       float var8 = (var6 >> 8 & 0xFF) / 255.0F;
@@ -276,112 +382,192 @@
       int var8 = 0;

       for (int var9 = var5.size(); var8 < var9; var8++) {
          BakedQuad var10 = (BakedQuad)var5.get(var8);
          var7.begin(7, DefaultVertexFormats.ITEM);
          var7.addVertexData(var10.getVertexData());
+         var7.putSprite(var10.getSprite());
          if (var10.hasTintIndex()) {
             var7.putColorRGB_F4(var2 * var1, var3 * var1, var4 * var1);
          } else {
             var7.putColorRGB_F4(var1, var1, var1);
          }

          Vec3i var11 = var10.getFace().getDirectionVec();
          var7.putNormal(var11.getX(), var11.getY(), var11.getZ());
          var6.draw();
       }
    }

-   class AmbientOcclusionFace {
+   public static float fixAoLightValue(float var0) {
+      return var0 == 0.2F ? aoLightValueOpaque : var0;
+   }
+
+   public static void updateAoLightValue() {
+      aoLightValueOpaque = 1.0F - Config.getAmbientOcclusionLevel() * 0.8F;
+      separateAoLightValue = Config.isShaders() && Shaders.isSeparateAo();
+   }
+
+   private void renderOverlayModels(
+      IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, BufferBuilder var5, boolean var6, long var7, RenderEnv var9, boolean var10
+   ) {
+      if (var9.isOverlaysRendered()) {
+         for (int var11 = 0; var11 < OVERLAY_LAYERS.length; var11++) {
+            BlockRenderLayer var12 = OVERLAY_LAYERS[var11];
+            ListQuadsOverlay var13 = var9.getListQuadsOverlay(var12);
+            if (var13.size() > 0) {
+               RegionRenderCacheBuilder var14 = var9.getRegionRenderCacheBuilder();
+               if (var14 != null) {
+                  BufferBuilder var15 = var14.getWorldRendererByLayer(var12);
+                  if (!var15.isDrawing()) {
+                     var15.begin(7, DefaultVertexFormats.BLOCK);
+                     var15.setTranslation(var5.getXOffset(), var5.getYOffset(), var5.getZOffset());
+                  }
+
+                  for (int var16 = 0; var16 < var13.size(); var16++) {
+                     BakedQuad var17 = var13.getQuad(var16);
+                     List var18 = var13.getListQuadsSingle(var17);
+                     IBlockState var19 = var13.getBlockState(var16);
+                     if (var17.getQuadEmissive() != null) {
+                        var13.addQuad(var17.getQuadEmissive(), var19);
+                     }
+
+                     var9.reset(var19, var4);
+                     if (var10) {
+                        this.renderQuadsSmooth(var1, var19, var4, var15, var18, var9);
+                     } else {
+                        int var20 = var19.b(var1, var4.offset(var17.getFace()));
+                        this.renderQuadsFlat(var1, var19, var4, var20, false, var15, var18, var9);
+                     }
+                  }
+               }
+
+               var13.clear();
+            }
+         }
+      }
+
+      if (Config.isBetterSnow() && !var9.isBreakingAnimation() && BetterSnow.shouldRender(var1, var3, var4)) {
+         IBakedModel var21 = BetterSnow.getModelSnowLayer();
+         IBlockState var22 = BetterSnow.getStateSnowLayer();
+         this.renderModel(var1, var21, var22, var4, var5, var6, var7);
+      }
+   }
+
+   public static class AmbientOcclusionFace {
       private final float[] vertexColorMultiplier = new float[4];
       private final int[] vertexBrightness = new int[4];
+      private MutableBlockPos[] blockPosArr = new MutableBlockPos[5];

       public AmbientOcclusionFace() {
+         this(null);
+      }
+
+      public AmbientOcclusionFace(BlockModelRenderer var1) {
+         for (int var2 = 0; var2 < this.blockPosArr.length; var2++) {
+            this.blockPosArr[var2] = new MutableBlockPos();
+         }
+      }
+
+      public void setMaxBlockLight() {
+         short var1 = 240;
+         this.vertexBrightness[0] = this.vertexBrightness[0] | var1;
+         this.vertexBrightness[1] = this.vertexBrightness[1] | var1;
+         this.vertexBrightness[2] = this.vertexBrightness[2] | var1;
+         this.vertexBrightness[3] = this.vertexBrightness[3] | var1;
+         this.vertexColorMultiplier[0] = 1.0F;
+         this.vertexColorMultiplier[1] = 1.0F;
+         this.vertexColorMultiplier[2] = 1.0F;
+         this.vertexColorMultiplier[3] = 1.0F;
       }

       public void updateVertexBrightness(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4, float[] var5, BitSet var6) {
          BlockPos var7 = var6.get(0) ? var3.offset(var4) : var3;
-         BlockPos.PooledMutableBlockPos var8 = BlockPos.PooledMutableBlockPos.retain();
+         MutableBlockPos var8 = this.blockPosArr[0].setPos(0, 0, 0);
          BlockModelRenderer.EnumNeighborInfo var9 = BlockModelRenderer.EnumNeighborInfo.getNeighbourInfo(var4);
-         BlockPos.PooledMutableBlockPos var10 = BlockPos.PooledMutableBlockPos.retain(var7).move(var9.corners[0]);
-         BlockPos.PooledMutableBlockPos var11 = BlockPos.PooledMutableBlockPos.retain(var7).move(var9.corners[1]);
-         BlockPos.PooledMutableBlockPos var12 = BlockPos.PooledMutableBlockPos.retain(var7).move(var9.corners[2]);
-         BlockPos.PooledMutableBlockPos var13 = BlockPos.PooledMutableBlockPos.retain(var7).move(var9.corners[3]);
-         int var14 = var2.getPackedLightmapCoords(var1, var10);
-         int var15 = var2.getPackedLightmapCoords(var1, var11);
-         int var16 = var2.getPackedLightmapCoords(var1, var12);
-         int var17 = var2.getPackedLightmapCoords(var1, var13);
-         float var18 = var1.getBlockState(var10).getAmbientOcclusionLightValue();
-         float var19 = var1.getBlockState(var11).getAmbientOcclusionLightValue();
-         float var20 = var1.getBlockState(var12).getAmbientOcclusionLightValue();
-         float var21 = var1.getBlockState(var13).getAmbientOcclusionLightValue();
-         boolean var22 = var1.getBlockState(var8.setPos(var10).move(var4)).isTranslucent();
-         boolean var23 = var1.getBlockState(var8.setPos(var11).move(var4)).isTranslucent();
-         boolean var24 = var1.getBlockState(var8.setPos(var12).move(var4)).isTranslucent();
-         boolean var25 = var1.getBlockState(var8.setPos(var13).move(var4)).isTranslucent();
-         float var26;
-         int var30;
+         MutableBlockPos var10 = this.blockPosArr[1].setPos(var7).move(var9.corners[0]);
+         MutableBlockPos var11 = this.blockPosArr[2].setPos(var7).move(var9.corners[1]);
+         MutableBlockPos var12 = this.blockPosArr[3].setPos(var7).move(var9.corners[2]);
+         MutableBlockPos var13 = this.blockPosArr[4].setPos(var7).move(var9.corners[3]);
+         int var14 = var2.b(var1, var10);
+         int var15 = var2.b(var1, var11);
+         int var16 = var2.b(var1, var12);
+         int var17 = var2.b(var1, var13);
+         float var18 = var1.getBlockState(var10).j();
+         float var19 = var1.getBlockState(var11).j();
+         float var20 = var1.getBlockState(var12).j();
+         float var21 = var1.getBlockState(var13).j();
+         var18 = BlockModelRenderer.fixAoLightValue(var18);
+         var19 = BlockModelRenderer.fixAoLightValue(var19);
+         var20 = BlockModelRenderer.fixAoLightValue(var20);
+         var21 = BlockModelRenderer.fixAoLightValue(var21);
+         boolean var22 = var1.getBlockState(var8.setPos(var10).move(var4)).e();
+         boolean var23 = var1.getBlockState(var8.setPos(var11).move(var4)).e();
+         boolean var24 = var1.getBlockState(var8.setPos(var12).move(var4)).e();
+         boolean var25 = var1.getBlockState(var8.setPos(var13).move(var4)).e();
+         int var27;
+         float var65;
          if (!var24 && !var22) {
-            var26 = var18;
-            var30 = var14;
+            var65 = var18;
+            var27 = var14;
          } else {
-            BlockPos.PooledMutableBlockPos var34 = var8.setPos(var10).move(var9.corners[2]);
-            var26 = var1.getBlockState(var34).getAmbientOcclusionLightValue();
-            var30 = var2.getPackedLightmapCoords(var1, var34);
+            MutableBlockPos var28 = var8.setPos(var10).move(var9.corners[2]);
+            var65 = var1.getBlockState(var28).j();
+            var65 = BlockModelRenderer.fixAoLightValue(var65);
+            var27 = var2.b(var1, var28);
          }

-         float var27;
-         int var31;
+         int var29;
+         float var67;
          if (!var25 && !var22) {
-            var27 = var18;
-            var31 = var14;
+            var67 = var18;
+            var29 = var14;
          } else {
-            BlockPos.PooledMutableBlockPos var61 = var8.setPos(var10).move(var9.corners[3]);
-            var27 = var1.getBlockState(var61).getAmbientOcclusionLightValue();
-            var31 = var2.getPackedLightmapCoords(var1, var61);
+            MutableBlockPos var30 = var8.setPos(var10).move(var9.corners[3]);
+            var67 = var1.getBlockState(var30).j();
+            var67 = BlockModelRenderer.fixAoLightValue(var67);
+            var29 = var2.b(var1, var30);
          }

-         float var28;
-         int var32;
+         int var31;
+         float var69;
          if (!var24 && !var23) {
-            var28 = var19;
-            var32 = var15;
+            var69 = var19;
+            var31 = var15;
          } else {
-            BlockPos.PooledMutableBlockPos var62 = var8.setPos(var11).move(var9.corners[2]);
-            var28 = var1.getBlockState(var62).getAmbientOcclusionLightValue();
-            var32 = var2.getPackedLightmapCoords(var1, var62);
+            MutableBlockPos var32 = var8.setPos(var11).move(var9.corners[2]);
+            var69 = var1.getBlockState(var32).j();
+            var69 = BlockModelRenderer.fixAoLightValue(var69);
+            var31 = var2.b(var1, var32);
          }

-         float var29;
          int var33;
+         float var71;
          if (!var25 && !var23) {
-            var29 = var19;
+            var71 = var19;
             var33 = var15;
          } else {
-            BlockPos.PooledMutableBlockPos var63 = var8.setPos(var11).move(var9.corners[3]);
-            var29 = var1.getBlockState(var63).getAmbientOcclusionLightValue();
-            var33 = var2.getPackedLightmapCoords(var1, var63);
+            MutableBlockPos var34 = var8.setPos(var11).move(var9.corners[3]);
+            var71 = var1.getBlockState(var34).j();
+            var71 = BlockModelRenderer.fixAoLightValue(var71);
+            var33 = var2.b(var1, var34);
          }

-         int var64 = var2.getPackedLightmapCoords(var1, var3);
-         if (var6.get(0) || !var1.getBlockState(var3.offset(var4)).isOpaqueCube()) {
-            var64 = var2.getPackedLightmapCoords(var1, var3.offset(var4));
+         int var72 = var2.b(var1, var3);
+         if (var6.get(0) || !var1.getBlockState(var3.offset(var4)).p()) {
+            var72 = var2.b(var1, var3.offset(var4));
          }

-         float var35 = var6.get(0) ? var1.getBlockState(var7).getAmbientOcclusionLightValue() : var1.getBlockState(var3).getAmbientOcclusionLightValue();
+         float var35 = var6.get(0) ? var1.getBlockState(var7).j() : var1.getBlockState(var3).j();
+         var35 = BlockModelRenderer.fixAoLightValue(var35);
          BlockModelRenderer.VertexTranslations var36 = BlockModelRenderer.VertexTranslations.getVertexTranslations(var4);
-         var8.release();
-         var10.release();
-         var11.release();
-         var12.release();
-         var13.release();
          if (var6.get(1) && var9.doNonCubicWeight) {
-            float var65 = (var21 + var18 + var27 + var35) * 0.25F;
-            float var66 = (var20 + var18 + var26 + var35) * 0.25F;
-            float var67 = (var20 + var19 + var28 + var35) * 0.25F;
-            float var68 = (var21 + var19 + var29 + var35) * 0.25F;
+            float var74 = (var21 + var18 + var67 + var35) * 0.25F;
+            float var75 = (var20 + var18 + var65 + var35) * 0.25F;
+            float var76 = (var20 + var19 + var69 + var35) * 0.25F;
+            float var77 = (var21 + var19 + var71 + var35) * 0.25F;
             float var41 = var5[var9.vert0Weights[0].shape] * var5[var9.vert0Weights[1].shape];
             float var42 = var5[var9.vert0Weights[2].shape] * var5[var9.vert0Weights[3].shape];
             float var43 = var5[var9.vert0Weights[4].shape] * var5[var9.vert0Weights[5].shape];
             float var44 = var5[var9.vert0Weights[6].shape] * var5[var9.vert0Weights[7].shape];
             float var45 = var5[var9.vert1Weights[0].shape] * var5[var9.vert1Weights[1].shape];
             float var46 = var5[var9.vert1Weights[2].shape] * var5[var9.vert1Weights[3].shape];
@@ -392,33 +578,33 @@
             float var51 = var5[var9.vert2Weights[4].shape] * var5[var9.vert2Weights[5].shape];
             float var52 = var5[var9.vert2Weights[6].shape] * var5[var9.vert2Weights[7].shape];
             float var53 = var5[var9.vert3Weights[0].shape] * var5[var9.vert3Weights[1].shape];
             float var54 = var5[var9.vert3Weights[2].shape] * var5[var9.vert3Weights[3].shape];
             float var55 = var5[var9.vert3Weights[4].shape] * var5[var9.vert3Weights[5].shape];
             float var56 = var5[var9.vert3Weights[6].shape] * var5[var9.vert3Weights[7].shape];
-            this.vertexColorMultiplier[var36.vert0] = var65 * var41 + var66 * var42 + var67 * var43 + var68 * var44;
-            this.vertexColorMultiplier[var36.vert1] = var65 * var45 + var66 * var46 + var67 * var47 + var68 * var48;
-            this.vertexColorMultiplier[var36.vert2] = var65 * var49 + var66 * var50 + var67 * var51 + var68 * var52;
-            this.vertexColorMultiplier[var36.vert3] = var65 * var53 + var66 * var54 + var67 * var55 + var68 * var56;
-            int var57 = this.getAoBrightness(var17, var14, var31, var64);
-            int var58 = this.getAoBrightness(var16, var14, var30, var64);
-            int var59 = this.getAoBrightness(var16, var15, var32, var64);
-            int var60 = this.getAoBrightness(var17, var15, var33, var64);
+            this.vertexColorMultiplier[var36.vert0] = var74 * var41 + var75 * var42 + var76 * var43 + var77 * var44;
+            this.vertexColorMultiplier[var36.vert1] = var74 * var45 + var75 * var46 + var76 * var47 + var77 * var48;
+            this.vertexColorMultiplier[var36.vert2] = var74 * var49 + var75 * var50 + var76 * var51 + var77 * var52;
+            this.vertexColorMultiplier[var36.vert3] = var74 * var53 + var75 * var54 + var76 * var55 + var77 * var56;
+            int var57 = this.getAoBrightness(var17, var14, var29, var72);
+            int var58 = this.getAoBrightness(var16, var14, var27, var72);
+            int var59 = this.getAoBrightness(var16, var15, var31, var72);
+            int var60 = this.getAoBrightness(var17, var15, var33, var72);
             this.vertexBrightness[var36.vert0] = this.getVertexBrightness(var57, var58, var59, var60, var41, var42, var43, var44);
             this.vertexBrightness[var36.vert1] = this.getVertexBrightness(var57, var58, var59, var60, var45, var46, var47, var48);
             this.vertexBrightness[var36.vert2] = this.getVertexBrightness(var57, var58, var59, var60, var49, var50, var51, var52);
             this.vertexBrightness[var36.vert3] = this.getVertexBrightness(var57, var58, var59, var60, var53, var54, var55, var56);
          } else {
-            float var37 = (var21 + var18 + var27 + var35) * 0.25F;
-            float var38 = (var20 + var18 + var26 + var35) * 0.25F;
-            float var39 = (var20 + var19 + var28 + var35) * 0.25F;
-            float var40 = (var21 + var19 + var29 + var35) * 0.25F;
-            this.vertexBrightness[var36.vert0] = this.getAoBrightness(var17, var14, var31, var64);
-            this.vertexBrightness[var36.vert1] = this.getAoBrightness(var16, var14, var30, var64);
-            this.vertexBrightness[var36.vert2] = this.getAoBrightness(var16, var15, var32, var64);
-            this.vertexBrightness[var36.vert3] = this.getAoBrightness(var17, var15, var33, var64);
+            float var37 = (var21 + var18 + var67 + var35) * 0.25F;
+            float var38 = (var20 + var18 + var65 + var35) * 0.25F;
+            float var39 = (var20 + var19 + var69 + var35) * 0.25F;
+            float var40 = (var21 + var19 + var71 + var35) * 0.25F;
+            this.vertexBrightness[var36.vert0] = this.getAoBrightness(var17, var14, var29, var72);
+            this.vertexBrightness[var36.vert1] = this.getAoBrightness(var16, var14, var27, var72);
+            this.vertexBrightness[var36.vert2] = this.getAoBrightness(var16, var15, var31, var72);
+            this.vertexBrightness[var36.vert3] = this.getAoBrightness(var17, var15, var33, var72);
             this.vertexColorMultiplier[var36.vert0] = var37;
             this.vertexColorMultiplier[var36.vert1] = var38;
             this.vertexColorMultiplier[var36.vert2] = var39;
             this.vertexColorMultiplier[var36.vert3] = var40;
          }
       }
 */
