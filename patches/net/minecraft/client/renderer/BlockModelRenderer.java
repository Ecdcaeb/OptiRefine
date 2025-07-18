package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.BetterSnow;
import net.optifine.CustomColors;
import net.optifine.model.BlockModelCustomizer;
import net.optifine.model.ListQuadsOverlay;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import net.optifine.shaders.Shaders;

public class BlockModelRenderer {
   private final BlockColors blockColors;
   private static float aoLightValueOpaque = 0.2F;
   private static boolean separateAoLightValue = false;
   private static final BlockRenderLayer[] OVERLAY_LAYERS = new BlockRenderLayer[]{
      BlockRenderLayer.CUTOUT, BlockRenderLayer.CUTOUT_MIPPED, BlockRenderLayer.TRANSLUCENT
   };

   public BlockModelRenderer(BlockColors blockColorsIn) {
      this.blockColors = blockColorsIn;
      if (Reflector.ForgeModContainer_forgeLightPipelineEnabled.exists()) {
         Reflector.setFieldValue(Reflector.ForgeModContainer_forgeLightPipelineEnabled, false);
      }
   }

   public boolean renderModel(
      IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder buffer, boolean checkSides
   ) {
      return this.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, buffer, checkSides, MathHelper.getPositionRandom(blockPosIn));
   }

   public boolean renderModel(
      IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand
   ) {
      boolean flag = Minecraft.isAmbientOcclusionEnabled()
         && ReflectorForge.getLightValue(stateIn, worldIn, posIn) == 0
         && ReflectorForge.isAmbientOcclusion(modelIn, stateIn);

      try {
         if (Config.isShaders()) {
            SVertexBuilder.pushEntity(stateIn, posIn, worldIn, buffer);
         }

         if (!Config.isAlternateBlocks()) {
            rand = 0L;
         }

         RenderEnv renderEnv = buffer.getRenderEnv(stateIn, posIn);
         modelIn = BlockModelCustomizer.getRenderModel(modelIn, stateIn, renderEnv);
         boolean rendered = flag
            ? this.renderModelSmooth(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand)
            : this.renderModelFlat(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand);
         if (rendered) {
            this.renderOverlayModels(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand, renderEnv, flag);
         }

         if (Config.isShaders()) {
            SVertexBuilder.popEntity(buffer);
         }

         return rendered;
      } catch (Throwable var13) {
         CrashReport crashreport = CrashReport.makeCrashReport(var13, "Tesselating block model");
         CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
         CrashReportCategory.addBlockInfo(crashreportcategory, posIn, stateIn);
         crashreportcategory.addCrashSection("Using AO", flag);
         throw new ReportedException(crashreport);
      }
   }

   public boolean renderModelSmooth(
      IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand
   ) {
      boolean flag = false;
      RenderEnv renderEnv = buffer.getRenderEnv(stateIn, posIn);
      BlockRenderLayer layer = buffer.getBlockLayer();

      for (EnumFacing enumfacing : EnumFacing.VALUES) {
         List<BakedQuad> list = modelIn.getQuads(stateIn, enumfacing, rand);
         if (!list.isEmpty() && (!checkSides || stateIn.c(worldIn, posIn, enumfacing))) {
            list = BlockModelCustomizer.getRenderQuads(list, worldIn, stateIn, posIn, enumfacing, layer, rand, renderEnv);
            this.renderQuadsSmooth(worldIn, stateIn, posIn, buffer, list, renderEnv);
            flag = true;
         }
      }

      List<BakedQuad> list1 = modelIn.getQuads(stateIn, (EnumFacing)null, rand);
      if (!list1.isEmpty()) {
         list1 = BlockModelCustomizer.getRenderQuads(list1, worldIn, stateIn, posIn, null, layer, rand, renderEnv);
         this.renderQuadsSmooth(worldIn, stateIn, posIn, buffer, list1, renderEnv);
         flag = true;
      }

      return flag;
   }

   public boolean renderModelFlat(
      IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand
   ) {
      boolean flag = false;
      RenderEnv renderEnv = buffer.getRenderEnv(stateIn, posIn);
      BlockRenderLayer layer = buffer.getBlockLayer();

      for (EnumFacing enumfacing : EnumFacing.VALUES) {
         List<BakedQuad> list = modelIn.getQuads(stateIn, enumfacing, rand);
         if (!list.isEmpty() && (!checkSides || stateIn.c(worldIn, posIn, enumfacing))) {
            int i = stateIn.b(worldIn, posIn.offset(enumfacing));
            list = BlockModelCustomizer.getRenderQuads(list, worldIn, stateIn, posIn, enumfacing, layer, rand, renderEnv);
            this.renderQuadsFlat(worldIn, stateIn, posIn, i, false, buffer, list, renderEnv);
            flag = true;
         }
      }

      List<BakedQuad> list1 = modelIn.getQuads(stateIn, (EnumFacing)null, rand);
      if (!list1.isEmpty()) {
         list1 = BlockModelCustomizer.getRenderQuads(list1, worldIn, stateIn, posIn, null, layer, rand, renderEnv);
         this.renderQuadsFlat(worldIn, stateIn, posIn, -1, true, buffer, list1, renderEnv);
         flag = true;
      }

      return flag;
   }

   private void renderQuadsSmooth(
      IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, List<BakedQuad> list, RenderEnv renderEnv
   ) {
      float[] quadBounds = renderEnv.getQuadBounds();
      BitSet bitSet = renderEnv.getBoundsFlags();
      BlockModelRenderer.AmbientOcclusionFace aoFace = renderEnv.getAoFace();
      Vec3d vec3d = stateIn.f(blockAccessIn, posIn);
      double d0 = posIn.getX() + vec3d.x;
      double d1 = posIn.getY() + vec3d.y;
      double d2 = posIn.getZ() + vec3d.z;
      int i = 0;

      for (int j = list.size(); i < j; i++) {
         BakedQuad bakedquad = list.get(i);
         this.fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), quadBounds, bitSet);
         aoFace.updateVertexBrightness(blockAccessIn, stateIn, posIn, bakedquad.getFace(), quadBounds, bitSet);
         if (bakedquad.getSprite().isEmissive) {
            aoFace.setMaxBlockLight();
         }

         if (buffer.isMultiTexture()) {
            buffer.addVertexData(bakedquad.getVertexDataSingle());
         } else {
            buffer.addVertexData(bakedquad.getVertexData());
         }

         buffer.putSprite(bakedquad.getSprite());
         buffer.putBrightness4(aoFace.vertexBrightness[0], aoFace.vertexBrightness[1], aoFace.vertexBrightness[2], aoFace.vertexBrightness[3]);
         if (bakedquad.shouldApplyDiffuseLighting()) {
            float diffuse = FaceBakery.getFaceBrightness(bakedquad.getFace());
            aoFace.vertexColorMultiplier[0] *= diffuse;
            aoFace.vertexColorMultiplier[1] *= diffuse;
            aoFace.vertexColorMultiplier[2] *= diffuse;
            aoFace.vertexColorMultiplier[3] *= diffuse;
         }

         int colorMultiplier = CustomColors.getColorMultiplier(bakedquad, stateIn, blockAccessIn, posIn, renderEnv);
         if (bakedquad.hasTintIndex() || colorMultiplier != -1) {
            int k = colorMultiplier;
            if (colorMultiplier == -1) {
               k = this.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());
            }

            if (EntityRenderer.anaglyphEnable) {
               k = TextureUtil.anaglyphColor(k);
            }

            float f = (k >> 16 & 0xFF) / 255.0F;
            float f1 = (k >> 8 & 0xFF) / 255.0F;
            float f2 = (k & 0xFF) / 255.0F;
            if (separateAoLightValue) {
               buffer.putColorMultiplierRgba(f, f1, f2, aoFace.vertexColorMultiplier[0], 4);
               buffer.putColorMultiplierRgba(f, f1, f2, aoFace.vertexColorMultiplier[1], 3);
               buffer.putColorMultiplierRgba(f, f1, f2, aoFace.vertexColorMultiplier[2], 2);
               buffer.putColorMultiplierRgba(f, f1, f2, aoFace.vertexColorMultiplier[3], 1);
            } else {
               buffer.putColorMultiplier(aoFace.vertexColorMultiplier[0] * f, aoFace.vertexColorMultiplier[0] * f1, aoFace.vertexColorMultiplier[0] * f2, 4);
               buffer.putColorMultiplier(aoFace.vertexColorMultiplier[1] * f, aoFace.vertexColorMultiplier[1] * f1, aoFace.vertexColorMultiplier[1] * f2, 3);
               buffer.putColorMultiplier(aoFace.vertexColorMultiplier[2] * f, aoFace.vertexColorMultiplier[2] * f1, aoFace.vertexColorMultiplier[2] * f2, 2);
               buffer.putColorMultiplier(aoFace.vertexColorMultiplier[3] * f, aoFace.vertexColorMultiplier[3] * f1, aoFace.vertexColorMultiplier[3] * f2, 1);
            }
         } else if (separateAoLightValue) {
            buffer.putColorMultiplierRgba(1.0F, 1.0F, 1.0F, aoFace.vertexColorMultiplier[0], 4);
            buffer.putColorMultiplierRgba(1.0F, 1.0F, 1.0F, aoFace.vertexColorMultiplier[1], 3);
            buffer.putColorMultiplierRgba(1.0F, 1.0F, 1.0F, aoFace.vertexColorMultiplier[2], 2);
            buffer.putColorMultiplierRgba(1.0F, 1.0F, 1.0F, aoFace.vertexColorMultiplier[3], 1);
         } else {
            buffer.putColorMultiplier(aoFace.vertexColorMultiplier[0], aoFace.vertexColorMultiplier[0], aoFace.vertexColorMultiplier[0], 4);
            buffer.putColorMultiplier(aoFace.vertexColorMultiplier[1], aoFace.vertexColorMultiplier[1], aoFace.vertexColorMultiplier[1], 3);
            buffer.putColorMultiplier(aoFace.vertexColorMultiplier[2], aoFace.vertexColorMultiplier[2], aoFace.vertexColorMultiplier[2], 2);
            buffer.putColorMultiplier(aoFace.vertexColorMultiplier[3], aoFace.vertexColorMultiplier[3], aoFace.vertexColorMultiplier[3], 1);
         }

         buffer.putPosition(d0, d1, d2);
      }
   }

   private void fillQuadBounds(IBlockState stateIn, int[] vertexData, EnumFacing face, @Nullable float[] quadBounds, BitSet boundsFlags) {
      float f = 32.0F;
      float f1 = 32.0F;
      float f2 = 32.0F;
      float f3 = -32.0F;
      float f4 = -32.0F;
      float f5 = -32.0F;
      int step = vertexData.length / 4;

      for (int i = 0; i < 4; i++) {
         float f6 = Float.intBitsToFloat(vertexData[i * step]);
         float f7 = Float.intBitsToFloat(vertexData[i * step + 1]);
         float f8 = Float.intBitsToFloat(vertexData[i * step + 2]);
         f = Math.min(f, f6);
         f1 = Math.min(f1, f7);
         f2 = Math.min(f2, f8);
         f3 = Math.max(f3, f6);
         f4 = Math.max(f4, f7);
         f5 = Math.max(f5, f8);
      }

      if (quadBounds != null) {
         quadBounds[EnumFacing.WEST.getIndex()] = f;
         quadBounds[EnumFacing.EAST.getIndex()] = f3;
         quadBounds[EnumFacing.DOWN.getIndex()] = f1;
         quadBounds[EnumFacing.UP.getIndex()] = f4;
         quadBounds[EnumFacing.NORTH.getIndex()] = f2;
         quadBounds[EnumFacing.SOUTH.getIndex()] = f5;
         int j = EnumFacing.VALUES.length;
         quadBounds[EnumFacing.WEST.getIndex() + j] = 1.0F - f;
         quadBounds[EnumFacing.EAST.getIndex() + j] = 1.0F - f3;
         quadBounds[EnumFacing.DOWN.getIndex() + j] = 1.0F - f1;
         quadBounds[EnumFacing.UP.getIndex() + j] = 1.0F - f4;
         quadBounds[EnumFacing.NORTH.getIndex() + j] = 1.0F - f2;
         quadBounds[EnumFacing.SOUTH.getIndex() + j] = 1.0F - f5;
      }

      float f9 = 1.0E-4F;
      float f10 = 0.9999F;
      switch (face) {
         case DOWN:
            boundsFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
            boundsFlags.set(0, (f1 < 1.0E-4F || stateIn.g()) && f1 == f4);
            break;
         case UP:
            boundsFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
            boundsFlags.set(0, (f4 > 0.9999F || stateIn.g()) && f1 == f4);
            break;
         case NORTH:
            boundsFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
            boundsFlags.set(0, (f2 < 1.0E-4F || stateIn.g()) && f2 == f5);
            break;
         case SOUTH:
            boundsFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
            boundsFlags.set(0, (f5 > 0.9999F || stateIn.g()) && f2 == f5);
            break;
         case WEST:
            boundsFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
            boundsFlags.set(0, (f < 1.0E-4F || stateIn.g()) && f == f3);
            break;
         case EAST:
            boundsFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
            boundsFlags.set(0, (f3 > 0.9999F || stateIn.g()) && f == f3);
      }
   }

   private void renderQuadsFlat(
      IBlockAccess blockAccessIn,
      IBlockState stateIn,
      BlockPos posIn,
      int brightnessIn,
      boolean ownBrightness,
      BufferBuilder buffer,
      List<BakedQuad> list,
      RenderEnv renderEnv
   ) {
      BitSet bitSet = renderEnv.getBoundsFlags();
      Vec3d vec3d = stateIn.f(blockAccessIn, posIn);
      double d0 = posIn.getX() + vec3d.x;
      double d1 = posIn.getY() + vec3d.y;
      double d2 = posIn.getZ() + vec3d.z;
      int i = 0;

      for (int j = list.size(); i < j; i++) {
         BakedQuad bakedquad = list.get(i);
         if (ownBrightness) {
            this.fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), (float[])null, bitSet);
            BlockPos blockpos = bitSet.get(0) ? posIn.offset(bakedquad.getFace()) : posIn;
            brightnessIn = stateIn.b(blockAccessIn, blockpos);
         }

         if (bakedquad.getSprite().isEmissive) {
            brightnessIn |= 240;
         }

         if (buffer.isMultiTexture()) {
            buffer.addVertexData(bakedquad.getVertexDataSingle());
         } else {
            buffer.addVertexData(bakedquad.getVertexData());
         }

         buffer.putSprite(bakedquad.getSprite());
         buffer.putBrightness4(brightnessIn, brightnessIn, brightnessIn, brightnessIn);
         int colorMultiplier = CustomColors.getColorMultiplier(bakedquad, stateIn, blockAccessIn, posIn, renderEnv);
         if (bakedquad.hasTintIndex() || colorMultiplier != -1) {
            int k = colorMultiplier;
            if (colorMultiplier == -1) {
               k = this.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());
            }

            if (EntityRenderer.anaglyphEnable) {
               k = TextureUtil.anaglyphColor(k);
            }

            float f = (k >> 16 & 0xFF) / 255.0F;
            float f1 = (k >> 8 & 0xFF) / 255.0F;
            float f2 = (k & 0xFF) / 255.0F;
            if (bakedquad.shouldApplyDiffuseLighting()) {
               float diffuse = FaceBakery.getFaceBrightness(bakedquad.getFace());
               f *= diffuse;
               f1 *= diffuse;
               f2 *= diffuse;
            }

            buffer.putColorMultiplier(f, f1, f2, 4);
            buffer.putColorMultiplier(f, f1, f2, 3);
            buffer.putColorMultiplier(f, f1, f2, 2);
            buffer.putColorMultiplier(f, f1, f2, 1);
         } else if (bakedquad.shouldApplyDiffuseLighting()) {
            float diffuse = FaceBakery.getFaceBrightness(bakedquad.getFace());
            buffer.putColorMultiplier(diffuse, diffuse, diffuse, 4);
            buffer.putColorMultiplier(diffuse, diffuse, diffuse, 3);
            buffer.putColorMultiplier(diffuse, diffuse, diffuse, 2);
            buffer.putColorMultiplier(diffuse, diffuse, diffuse, 1);
         }

         buffer.putPosition(d0, d1, d2);
      }
   }

   public void renderModelBrightnessColor(IBakedModel bakedModel, float p_178262_2_, float red, float green, float blue) {
      this.renderModelBrightnessColor((IBlockState)null, bakedModel, p_178262_2_, red, green, blue);
   }

   public void renderModelBrightnessColor(
      IBlockState state, IBakedModel p_187495_2_, float p_187495_3_, float p_187495_4_, float p_187495_5_, float p_187495_6_
   ) {
      for (EnumFacing enumfacing : EnumFacing.VALUES) {
         this.renderModelBrightnessColorQuads(p_187495_3_, p_187495_4_, p_187495_5_, p_187495_6_, p_187495_2_.getQuads(state, enumfacing, 0L));
      }

      this.renderModelBrightnessColorQuads(p_187495_3_, p_187495_4_, p_187495_5_, p_187495_6_, p_187495_2_.getQuads(state, (EnumFacing)null, 0L));
   }

   public void renderModelBrightness(IBakedModel model, IBlockState state, float brightness, boolean p_178266_4_) {
      Block block = state.getBlock();
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      int i = this.blockColors.colorMultiplier(state, (IBlockAccess)null, (BlockPos)null, 0);
      if (EntityRenderer.anaglyphEnable) {
         i = TextureUtil.anaglyphColor(i);
      }

      float f = (i >> 16 & 0xFF) / 255.0F;
      float f1 = (i >> 8 & 0xFF) / 255.0F;
      float f2 = (i & 0xFF) / 255.0F;
      if (!p_178266_4_) {
         GlStateManager.color(brightness, brightness, brightness, 1.0F);
      }

      this.renderModelBrightnessColor(state, model, brightness, f, f1, f2);
   }

   private void renderModelBrightnessColorQuads(float brightness, float red, float green, float blue, List<BakedQuad> listQuads) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      int i = 0;

      for (int j = listQuads.size(); i < j; i++) {
         BakedQuad bakedquad = listQuads.get(i);
         bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
         bufferbuilder.addVertexData(bakedquad.getVertexData());
         bufferbuilder.putSprite(bakedquad.getSprite());
         if (bakedquad.hasTintIndex()) {
            bufferbuilder.putColorRGB_F4(red * brightness, green * brightness, blue * brightness);
         } else {
            bufferbuilder.putColorRGB_F4(brightness, brightness, brightness);
         }

         Vec3i vec3i = bakedquad.getFace().getDirectionVec();
         bufferbuilder.putNormal(vec3i.getX(), vec3i.getY(), vec3i.getZ());
         tessellator.draw();
      }
   }

   public static float fixAoLightValue(float val) {
      return val == 0.2F ? aoLightValueOpaque : val;
   }

   public static void updateAoLightValue() {
      aoLightValueOpaque = 1.0F - Config.getAmbientOcclusionLevel() * 0.8F;
      separateAoLightValue = Config.isShaders() && Shaders.isSeparateAo();
   }

   private void renderOverlayModels(
      IBlockAccess worldIn,
      IBakedModel modelIn,
      IBlockState stateIn,
      BlockPos posIn,
      BufferBuilder buffer,
      boolean checkSides,
      long rand,
      RenderEnv renderEnv,
      boolean smooth
   ) {
      if (renderEnv.isOverlaysRendered()) {
         for (int l = 0; l < OVERLAY_LAYERS.length; l++) {
            BlockRenderLayer layer = OVERLAY_LAYERS[l];
            ListQuadsOverlay listQuadsOverlay = renderEnv.getListQuadsOverlay(layer);
            if (listQuadsOverlay.size() > 0) {
               RegionRenderCacheBuilder rrcb = renderEnv.getRegionRenderCacheBuilder();
               if (rrcb != null) {
                  BufferBuilder overlayBuffer = rrcb.getWorldRendererByLayer(layer);
                  if (!overlayBuffer.isDrawing()) {
                     overlayBuffer.begin(7, DefaultVertexFormats.BLOCK);
                     overlayBuffer.setTranslation(buffer.getXOffset(), buffer.getYOffset(), buffer.getZOffset());
                  }

                  for (int q = 0; q < listQuadsOverlay.size(); q++) {
                     BakedQuad quad = listQuadsOverlay.getQuad(q);
                     List<BakedQuad> listQuadSingle = listQuadsOverlay.getListQuadsSingle(quad);
                     IBlockState quadBlockState = listQuadsOverlay.getBlockState(q);
                     if (quad.getQuadEmissive() != null) {
                        listQuadsOverlay.addQuad(quad.getQuadEmissive(), quadBlockState);
                     }

                     renderEnv.reset(quadBlockState, posIn);
                     if (smooth) {
                        this.renderQuadsSmooth(worldIn, quadBlockState, posIn, overlayBuffer, listQuadSingle, renderEnv);
                     } else {
                        int col = quadBlockState.b(worldIn, posIn.offset(quad.getFace()));
                        this.renderQuadsFlat(worldIn, quadBlockState, posIn, col, false, overlayBuffer, listQuadSingle, renderEnv);
                     }
                  }
               }

               listQuadsOverlay.clear();
            }
         }
      }

      if (Config.isBetterSnow() && !renderEnv.isBreakingAnimation() && BetterSnow.shouldRender(worldIn, stateIn, posIn)) {
         IBakedModel modelSnow = BetterSnow.getModelSnowLayer();
         IBlockState stateSnow = BetterSnow.getStateSnowLayer();
         this.renderModel(worldIn, modelSnow, stateSnow, posIn, buffer, checkSides, rand);
      }
   }

   public static class AmbientOcclusionFace {
      private final float[] vertexColorMultiplier = new float[4];
      private final int[] vertexBrightness = new int[4];
      private MutableBlockPos[] blockPosArr = new MutableBlockPos[5];

      public AmbientOcclusionFace() {
         this(null);
      }

      public AmbientOcclusionFace(BlockModelRenderer bmr) {
         for (int i = 0; i < this.blockPosArr.length; i++) {
            this.blockPosArr[i] = new MutableBlockPos();
         }
      }

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

      public void updateVertexBrightness(
         IBlockAccess worldIn, IBlockState state, BlockPos centerPos, EnumFacing direction, float[] faceShape, BitSet shapeState
      ) {
         BlockPos blockpos = shapeState.get(0) ? centerPos.offset(direction) : centerPos;
         MutableBlockPos blockpos$pooledmutableblockpos = this.blockPosArr[0].setPos(0, 0, 0);
         BlockModelRenderer.EnumNeighborInfo blockmodelrenderer$enumneighborinfo = BlockModelRenderer.EnumNeighborInfo.getNeighbourInfo(direction);
         MutableBlockPos blockpos$pooledmutableblockpos1 = this.blockPosArr[1].setPos(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[0]);
         MutableBlockPos blockpos$pooledmutableblockpos2 = this.blockPosArr[2].setPos(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[1]);
         MutableBlockPos blockpos$pooledmutableblockpos3 = this.blockPosArr[3].setPos(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[2]);
         MutableBlockPos blockpos$pooledmutableblockpos4 = this.blockPosArr[4].setPos(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[3]);
         int i = state.b(worldIn, blockpos$pooledmutableblockpos1);
         int j = state.b(worldIn, blockpos$pooledmutableblockpos2);
         int k = state.b(worldIn, blockpos$pooledmutableblockpos3);
         int l = state.b(worldIn, blockpos$pooledmutableblockpos4);
         float f = worldIn.getBlockState(blockpos$pooledmutableblockpos1).j();
         float f1 = worldIn.getBlockState(blockpos$pooledmutableblockpos2).j();
         float f2 = worldIn.getBlockState(blockpos$pooledmutableblockpos3).j();
         float f3 = worldIn.getBlockState(blockpos$pooledmutableblockpos4).j();
         f = BlockModelRenderer.fixAoLightValue(f);
         f1 = BlockModelRenderer.fixAoLightValue(f1);
         f2 = BlockModelRenderer.fixAoLightValue(f2);
         f3 = BlockModelRenderer.fixAoLightValue(f3);
         boolean flag = worldIn.getBlockState(blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos1).move(direction)).e();
         boolean flag1 = worldIn.getBlockState(blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos2).move(direction)).e();
         boolean flag2 = worldIn.getBlockState(blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos3).move(direction)).e();
         boolean flag3 = worldIn.getBlockState(blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos4).move(direction)).e();
         int i1;
         float f4;
         if (!flag2 && !flag) {
            f4 = f;
            i1 = i;
         } else {
            BlockPos blockpos1 = blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos1).move(blockmodelrenderer$enumneighborinfo.corners[2]);
            f4 = worldIn.getBlockState(blockpos1).j();
            f4 = BlockModelRenderer.fixAoLightValue(f4);
            i1 = state.b(worldIn, blockpos1);
         }

         int j1;
         float f5;
         if (!flag3 && !flag) {
            f5 = f;
            j1 = i;
         } else {
            BlockPos blockpos2 = blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos1).move(blockmodelrenderer$enumneighborinfo.corners[3]);
            f5 = worldIn.getBlockState(blockpos2).j();
            f5 = BlockModelRenderer.fixAoLightValue(f5);
            j1 = state.b(worldIn, blockpos2);
         }

         int k1;
         float f6;
         if (!flag2 && !flag1) {
            f6 = f1;
            k1 = j;
         } else {
            BlockPos blockpos3 = blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos2).move(blockmodelrenderer$enumneighborinfo.corners[2]);
            f6 = worldIn.getBlockState(blockpos3).j();
            f6 = BlockModelRenderer.fixAoLightValue(f6);
            k1 = state.b(worldIn, blockpos3);
         }

         int l1;
         float f7;
         if (!flag3 && !flag1) {
            f7 = f1;
            l1 = j;
         } else {
            BlockPos blockpos4 = blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos2).move(blockmodelrenderer$enumneighborinfo.corners[3]);
            f7 = worldIn.getBlockState(blockpos4).j();
            f7 = BlockModelRenderer.fixAoLightValue(f7);
            l1 = state.b(worldIn, blockpos4);
         }

         int i3 = state.b(worldIn, centerPos);
         if (shapeState.get(0) || !worldIn.getBlockState(centerPos.offset(direction)).p()) {
            i3 = state.b(worldIn, centerPos.offset(direction));
         }

         float f8 = shapeState.get(0) ? worldIn.getBlockState(blockpos).j() : worldIn.getBlockState(centerPos).j();
         f8 = BlockModelRenderer.fixAoLightValue(f8);
         BlockModelRenderer.VertexTranslations blockmodelrenderer$vertextranslations = BlockModelRenderer.VertexTranslations.getVertexTranslations(direction);
         if (shapeState.get(1) && blockmodelrenderer$enumneighborinfo.doNonCubicWeight) {
            float f29 = (f3 + f + f5 + f8) * 0.25F;
            float f30 = (f2 + f + f4 + f8) * 0.25F;
            float f31 = (f2 + f1 + f6 + f8) * 0.25F;
            float f32 = (f3 + f1 + f7 + f8) * 0.25F;
            float f13 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[0].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[1].shape];
            float f14 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[2].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[3].shape];
            float f15 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[4].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[5].shape];
            float f16 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[6].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[7].shape];
            float f17 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[0].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[1].shape];
            float f18 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[2].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[3].shape];
            float f19 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[4].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[5].shape];
            float f20 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[6].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[7].shape];
            float f21 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[0].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[1].shape];
            float f22 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[2].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[3].shape];
            float f23 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[4].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[5].shape];
            float f24 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[6].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[7].shape];
            float f25 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[0].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[1].shape];
            float f26 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[2].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[3].shape];
            float f27 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[4].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[5].shape];
            float f28 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[6].shape]
               * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[7].shape];
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert0] = f29 * f13 + f30 * f14 + f31 * f15 + f32 * f16;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert1] = f29 * f17 + f30 * f18 + f31 * f19 + f32 * f20;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert2] = f29 * f21 + f30 * f22 + f31 * f23 + f32 * f24;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert3] = f29 * f25 + f30 * f26 + f31 * f27 + f32 * f28;
            int i2 = this.getAoBrightness(l, i, j1, i3);
            int j2 = this.getAoBrightness(k, i, i1, i3);
            int k2 = this.getAoBrightness(k, j, k1, i3);
            int l2 = this.getAoBrightness(l, j, l1, i3);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert0] = this.getVertexBrightness(i2, j2, k2, l2, f13, f14, f15, f16);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert1] = this.getVertexBrightness(i2, j2, k2, l2, f17, f18, f19, f20);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert2] = this.getVertexBrightness(i2, j2, k2, l2, f21, f22, f23, f24);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert3] = this.getVertexBrightness(i2, j2, k2, l2, f25, f26, f27, f28);
         } else {
            float f9 = (f3 + f + f5 + f8) * 0.25F;
            float f10 = (f2 + f + f4 + f8) * 0.25F;
            float f11 = (f2 + f1 + f6 + f8) * 0.25F;
            float f12 = (f3 + f1 + f7 + f8) * 0.25F;
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert0] = this.getAoBrightness(l, i, j1, i3);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert1] = this.getAoBrightness(k, i, i1, i3);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert2] = this.getAoBrightness(k, j, k1, i3);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert3] = this.getAoBrightness(l, j, l1, i3);
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert0] = f9;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert1] = f10;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert2] = f11;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert3] = f12;
         }
      }

      private int getAoBrightness(int br1, int br2, int br3, int br4) {
         if (br1 == 0) {
            br1 = br4;
         }

         if (br2 == 0) {
            br2 = br4;
         }

         if (br3 == 0) {
            br3 = br4;
         }

         return br1 + br2 + br3 + br4 >> 2 & 16711935;
      }

      private int getVertexBrightness(
         int p_178203_1_, int p_178203_2_, int p_178203_3_, int p_178203_4_, float p_178203_5_, float p_178203_6_, float p_178203_7_, float p_178203_8_
      ) {
         int i = (int)(
               (p_178203_1_ >> 16 & 0xFF) * p_178203_5_
                  + (p_178203_2_ >> 16 & 0xFF) * p_178203_6_
                  + (p_178203_3_ >> 16 & 0xFF) * p_178203_7_
                  + (p_178203_4_ >> 16 & 0xFF) * p_178203_8_
            )
            & 0xFF;
         int j = (int)(
               (p_178203_1_ & 0xFF) * p_178203_5_
                  + (p_178203_2_ & 0xFF) * p_178203_6_
                  + (p_178203_3_ & 0xFF) * p_178203_7_
                  + (p_178203_4_ & 0xFF) * p_178203_8_
            )
            & 0xFF;
         return i << 16 | j;
      }
   }

   public static enum EnumNeighborInfo {
      DOWN(
         new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH},
         0.5F,
         true,
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.SOUTH,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.SOUTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.NORTH,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.NORTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.NORTH,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.NORTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.SOUTH,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.SOUTH
         }
      ),
      UP(
         new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH},
         1.0F,
         true,
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.SOUTH,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.SOUTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.NORTH,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.NORTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.NORTH,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.NORTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.SOUTH,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.SOUTH
         }
      ),
      NORTH(
         new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST},
         0.8F,
         true,
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.FLIP_WEST
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.FLIP_EAST
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.FLIP_EAST
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.FLIP_WEST
         }
      ),
      SOUTH(
         new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP},
         0.8F,
         true,
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.WEST
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.FLIP_WEST,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.WEST,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.WEST
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.EAST
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.FLIP_EAST,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.EAST,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.EAST
         }
      ),
      WEST(
         new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH},
         0.6F,
         true,
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.SOUTH,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.SOUTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.NORTH,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.NORTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.NORTH,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.NORTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.SOUTH,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.SOUTH
         }
      ),
      EAST(
         new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH},
         0.6F,
         true,
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.SOUTH,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.SOUTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.NORTH,
            BlockModelRenderer.Orientation.FLIP_DOWN,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.DOWN,
            BlockModelRenderer.Orientation.NORTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.NORTH,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.FLIP_NORTH,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.NORTH
         },
         new BlockModelRenderer.Orientation[]{
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.SOUTH,
            BlockModelRenderer.Orientation.FLIP_UP,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.FLIP_SOUTH,
            BlockModelRenderer.Orientation.UP,
            BlockModelRenderer.Orientation.SOUTH
         }
      );

      private final EnumFacing[] corners;
      private final float shadeWeight;
      private final boolean doNonCubicWeight;
      private final BlockModelRenderer.Orientation[] vert0Weights;
      private final BlockModelRenderer.Orientation[] vert1Weights;
      private final BlockModelRenderer.Orientation[] vert2Weights;
      private final BlockModelRenderer.Orientation[] vert3Weights;
      private static final BlockModelRenderer.EnumNeighborInfo[] VALUES = new BlockModelRenderer.EnumNeighborInfo[6];

      private EnumNeighborInfo(
         EnumFacing[] p_i46236_3_,
         float p_i46236_4_,
         boolean p_i46236_5_,
         BlockModelRenderer.Orientation[] p_i46236_6_,
         BlockModelRenderer.Orientation[] p_i46236_7_,
         BlockModelRenderer.Orientation[] p_i46236_8_,
         BlockModelRenderer.Orientation[] p_i46236_9_
      ) {
         this.corners = p_i46236_3_;
         this.shadeWeight = p_i46236_4_;
         this.doNonCubicWeight = p_i46236_5_;
         this.vert0Weights = p_i46236_6_;
         this.vert1Weights = p_i46236_7_;
         this.vert2Weights = p_i46236_8_;
         this.vert3Weights = p_i46236_9_;
      }

      public static BlockModelRenderer.EnumNeighborInfo getNeighbourInfo(EnumFacing p_178273_0_) {
         return VALUES[p_178273_0_.getIndex()];
      }

      static {
         VALUES[EnumFacing.DOWN.getIndex()] = DOWN;
         VALUES[EnumFacing.UP.getIndex()] = UP;
         VALUES[EnumFacing.NORTH.getIndex()] = NORTH;
         VALUES[EnumFacing.SOUTH.getIndex()] = SOUTH;
         VALUES[EnumFacing.WEST.getIndex()] = WEST;
         VALUES[EnumFacing.EAST.getIndex()] = EAST;
      }
   }

   public static enum Orientation {
      DOWN(EnumFacing.DOWN, false),
      UP(EnumFacing.UP, false),
      NORTH(EnumFacing.NORTH, false),
      SOUTH(EnumFacing.SOUTH, false),
      WEST(EnumFacing.WEST, false),
      EAST(EnumFacing.EAST, false),
      FLIP_DOWN(EnumFacing.DOWN, true),
      FLIP_UP(EnumFacing.UP, true),
      FLIP_NORTH(EnumFacing.NORTH, true),
      FLIP_SOUTH(EnumFacing.SOUTH, true),
      FLIP_WEST(EnumFacing.WEST, true),
      FLIP_EAST(EnumFacing.EAST, true);

      private final int shape;

      private Orientation(EnumFacing p_i46233_3_, boolean p_i46233_4_) {
         this.shape = p_i46233_3_.getIndex() + (p_i46233_4_ ? EnumFacing.values().length : 0);
      }
   }

   static enum VertexTranslations {
      DOWN(0, 1, 2, 3),
      UP(2, 3, 0, 1),
      NORTH(3, 0, 1, 2),
      SOUTH(0, 1, 2, 3),
      WEST(3, 0, 1, 2),
      EAST(1, 2, 3, 0);

      private final int vert0;
      private final int vert1;
      private final int vert2;
      private final int vert3;
      private static final BlockModelRenderer.VertexTranslations[] VALUES = new BlockModelRenderer.VertexTranslations[6];

      private VertexTranslations(int p_i46234_3_, int p_i46234_4_, int p_i46234_5_, int p_i46234_6_) {
         this.vert0 = p_i46234_3_;
         this.vert1 = p_i46234_4_;
         this.vert2 = p_i46234_5_;
         this.vert3 = p_i46234_6_;
      }

      public static BlockModelRenderer.VertexTranslations getVertexTranslations(EnumFacing p_178184_0_) {
         return VALUES[p_178184_0_.getIndex()];
      }

      static {
         VALUES[EnumFacing.DOWN.getIndex()] = DOWN;
         VALUES[EnumFacing.UP.getIndex()] = UP;
         VALUES[EnumFacing.NORTH.getIndex()] = NORTH;
         VALUES[EnumFacing.SOUTH.getIndex()] = SOUTH;
         VALUES[EnumFacing.WEST.getIndex()] = WEST;
         VALUES[EnumFacing.EAST.getIndex()] = EAST;
      }
   }
}
