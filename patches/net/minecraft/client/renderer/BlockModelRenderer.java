package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;

public class BlockModelRenderer {
   private final BlockColors blockColors;

   public BlockModelRenderer(BlockColors var1) {
      this.blockColors = ☃;
   }

   public boolean renderModel(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, BufferBuilder var5, boolean var6) {
      return this.renderModel(☃, ☃, ☃, ☃, ☃, ☃, MathHelper.getPositionRandom(☃));
   }

   public boolean renderModel(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, BufferBuilder var5, boolean var6, long var7) {
      boolean ☃ = Minecraft.isAmbientOcclusionEnabled() && ☃.getLightValue() == 0 && ☃.isAmbientOcclusion();

      try {
         return ☃ ? this.renderModelSmooth(☃, ☃, ☃, ☃, ☃, ☃, ☃) : this.renderModelFlat(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } catch (Throwable var13) {
         CrashReport ☃x = CrashReport.makeCrashReport(var13, "Tesselating block model");
         CrashReportCategory ☃xx = ☃x.makeCategory("Block model being tesselated");
         CrashReportCategory.addBlockInfo(☃xx, ☃, ☃);
         ☃xx.addCrashSection("Using AO", ☃);
         throw new ReportedException(☃x);
      }
   }

   public boolean renderModelSmooth(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, BufferBuilder var5, boolean var6, long var7) {
      boolean ☃ = false;
      float[] ☃x = new float[EnumFacing.values().length * 2];
      BitSet ☃xx = new BitSet(3);
      BlockModelRenderer.AmbientOcclusionFace ☃xxx = new BlockModelRenderer.AmbientOcclusionFace();

      for (EnumFacing ☃xxxx : EnumFacing.values()) {
         List<BakedQuad> ☃xxxxx = ☃.getQuads(☃, ☃xxxx, ☃);
         if (!☃xxxxx.isEmpty() && (!☃ || ☃.shouldSideBeRendered(☃, ☃, ☃xxxx))) {
            this.renderQuadsSmooth(☃, ☃, ☃, ☃, ☃xxxxx, ☃x, ☃xx, ☃xxx);
            ☃ = true;
         }
      }

      List<BakedQuad> ☃xxxxx = ☃.getQuads(☃, null, ☃);
      if (!☃xxxxx.isEmpty()) {
         this.renderQuadsSmooth(☃, ☃, ☃, ☃, ☃xxxxx, ☃x, ☃xx, ☃xxx);
         ☃ = true;
      }

      return ☃;
   }

   public boolean renderModelFlat(IBlockAccess var1, IBakedModel var2, IBlockState var3, BlockPos var4, BufferBuilder var5, boolean var6, long var7) {
      boolean ☃ = false;
      BitSet ☃x = new BitSet(3);

      for (EnumFacing ☃xx : EnumFacing.values()) {
         List<BakedQuad> ☃xxx = ☃.getQuads(☃, ☃xx, ☃);
         if (!☃xxx.isEmpty() && (!☃ || ☃.shouldSideBeRendered(☃, ☃, ☃xx))) {
            int ☃xxxx = ☃.getPackedLightmapCoords(☃, ☃.offset(☃xx));
            this.renderQuadsFlat(☃, ☃, ☃, ☃xxxx, false, ☃, ☃xxx, ☃x);
            ☃ = true;
         }
      }

      List<BakedQuad> ☃xxx = ☃.getQuads(☃, null, ☃);
      if (!☃xxx.isEmpty()) {
         this.renderQuadsFlat(☃, ☃, ☃, -1, true, ☃, ☃xxx, ☃x);
         ☃ = true;
      }

      return ☃;
   }

   private void renderQuadsSmooth(
      IBlockAccess var1,
      IBlockState var2,
      BlockPos var3,
      BufferBuilder var4,
      List<BakedQuad> var5,
      float[] var6,
      BitSet var7,
      BlockModelRenderer.AmbientOcclusionFace var8
   ) {
      Vec3d ☃ = ☃.getOffset(☃, ☃);
      double ☃x = ☃.getX() + ☃.x;
      double ☃xx = ☃.getY() + ☃.y;
      double ☃xxx = ☃.getZ() + ☃.z;
      int ☃xxxx = 0;

      for (int ☃xxxxx = ☃.size(); ☃xxxx < ☃xxxxx; ☃xxxx++) {
         BakedQuad ☃xxxxxx = ☃.get(☃xxxx);
         this.fillQuadBounds(☃, ☃xxxxxx.getVertexData(), ☃xxxxxx.getFace(), ☃, ☃);
         ☃.updateVertexBrightness(☃, ☃, ☃, ☃xxxxxx.getFace(), ☃, ☃);
         ☃.addVertexData(☃xxxxxx.getVertexData());
         ☃.putBrightness4(☃.vertexBrightness[0], ☃.vertexBrightness[1], ☃.vertexBrightness[2], ☃.vertexBrightness[3]);
         if (☃xxxxxx.hasTintIndex()) {
            int ☃xxxxxxx = this.blockColors.colorMultiplier(☃, ☃, ☃, ☃xxxxxx.getTintIndex());
            if (EntityRenderer.anaglyphEnable) {
               ☃xxxxxxx = TextureUtil.anaglyphColor(☃xxxxxxx);
            }

            float ☃xxxxxxxx = (☃xxxxxxx >> 16 & 0xFF) / 255.0F;
            float ☃xxxxxxxxx = (☃xxxxxxx >> 8 & 0xFF) / 255.0F;
            float ☃xxxxxxxxxx = (☃xxxxxxx & 0xFF) / 255.0F;
            ☃.putColorMultiplier(☃.vertexColorMultiplier[0] * ☃xxxxxxxx, ☃.vertexColorMultiplier[0] * ☃xxxxxxxxx, ☃.vertexColorMultiplier[0] * ☃xxxxxxxxxx, 4);
            ☃.putColorMultiplier(☃.vertexColorMultiplier[1] * ☃xxxxxxxx, ☃.vertexColorMultiplier[1] * ☃xxxxxxxxx, ☃.vertexColorMultiplier[1] * ☃xxxxxxxxxx, 3);
            ☃.putColorMultiplier(☃.vertexColorMultiplier[2] * ☃xxxxxxxx, ☃.vertexColorMultiplier[2] * ☃xxxxxxxxx, ☃.vertexColorMultiplier[2] * ☃xxxxxxxxxx, 2);
            ☃.putColorMultiplier(☃.vertexColorMultiplier[3] * ☃xxxxxxxx, ☃.vertexColorMultiplier[3] * ☃xxxxxxxxx, ☃.vertexColorMultiplier[3] * ☃xxxxxxxxxx, 1);
         } else {
            ☃.putColorMultiplier(☃.vertexColorMultiplier[0], ☃.vertexColorMultiplier[0], ☃.vertexColorMultiplier[0], 4);
            ☃.putColorMultiplier(☃.vertexColorMultiplier[1], ☃.vertexColorMultiplier[1], ☃.vertexColorMultiplier[1], 3);
            ☃.putColorMultiplier(☃.vertexColorMultiplier[2], ☃.vertexColorMultiplier[2], ☃.vertexColorMultiplier[2], 2);
            ☃.putColorMultiplier(☃.vertexColorMultiplier[3], ☃.vertexColorMultiplier[3], ☃.vertexColorMultiplier[3], 1);
         }

         ☃.putPosition(☃x, ☃xx, ☃xxx);
      }
   }

   private void fillQuadBounds(IBlockState var1, int[] var2, EnumFacing var3, @Nullable float[] var4, BitSet var5) {
      float ☃ = 32.0F;
      float ☃x = 32.0F;
      float ☃xx = 32.0F;
      float ☃xxx = -32.0F;
      float ☃xxxx = -32.0F;
      float ☃xxxxx = -32.0F;

      for (int ☃xxxxxx = 0; ☃xxxxxx < 4; ☃xxxxxx++) {
         float ☃xxxxxxx = Float.intBitsToFloat(☃[☃xxxxxx * 7]);
         float ☃xxxxxxxx = Float.intBitsToFloat(☃[☃xxxxxx * 7 + 1]);
         float ☃xxxxxxxxx = Float.intBitsToFloat(☃[☃xxxxxx * 7 + 2]);
         ☃ = Math.min(☃, ☃xxxxxxx);
         ☃x = Math.min(☃x, ☃xxxxxxxx);
         ☃xx = Math.min(☃xx, ☃xxxxxxxxx);
         ☃xxx = Math.max(☃xxx, ☃xxxxxxx);
         ☃xxxx = Math.max(☃xxxx, ☃xxxxxxxx);
         ☃xxxxx = Math.max(☃xxxxx, ☃xxxxxxxxx);
      }

      if (☃ != null) {
         ☃[EnumFacing.WEST.getIndex()] = ☃;
         ☃[EnumFacing.EAST.getIndex()] = ☃xxx;
         ☃[EnumFacing.DOWN.getIndex()] = ☃x;
         ☃[EnumFacing.UP.getIndex()] = ☃xxxx;
         ☃[EnumFacing.NORTH.getIndex()] = ☃xx;
         ☃[EnumFacing.SOUTH.getIndex()] = ☃xxxxx;
         int ☃xxxxxx = EnumFacing.values().length;
         ☃[EnumFacing.WEST.getIndex() + ☃xxxxxx] = 1.0F - ☃;
         ☃[EnumFacing.EAST.getIndex() + ☃xxxxxx] = 1.0F - ☃xxx;
         ☃[EnumFacing.DOWN.getIndex() + ☃xxxxxx] = 1.0F - ☃x;
         ☃[EnumFacing.UP.getIndex() + ☃xxxxxx] = 1.0F - ☃xxxx;
         ☃[EnumFacing.NORTH.getIndex() + ☃xxxxxx] = 1.0F - ☃xx;
         ☃[EnumFacing.SOUTH.getIndex() + ☃xxxxxx] = 1.0F - ☃xxxxx;
      }

      float ☃xxxxxx = 1.0E-4F;
      float ☃xxxxxxx = 0.9999F;
      switch (☃) {
         case DOWN:
            ☃.set(1, ☃ >= 1.0E-4F || ☃xx >= 1.0E-4F || ☃xxx <= 0.9999F || ☃xxxxx <= 0.9999F);
            ☃.set(0, (☃x < 1.0E-4F || ☃.isFullCube()) && ☃x == ☃xxxx);
            break;
         case UP:
            ☃.set(1, ☃ >= 1.0E-4F || ☃xx >= 1.0E-4F || ☃xxx <= 0.9999F || ☃xxxxx <= 0.9999F);
            ☃.set(0, (☃xxxx > 0.9999F || ☃.isFullCube()) && ☃x == ☃xxxx);
            break;
         case NORTH:
            ☃.set(1, ☃ >= 1.0E-4F || ☃x >= 1.0E-4F || ☃xxx <= 0.9999F || ☃xxxx <= 0.9999F);
            ☃.set(0, (☃xx < 1.0E-4F || ☃.isFullCube()) && ☃xx == ☃xxxxx);
            break;
         case SOUTH:
            ☃.set(1, ☃ >= 1.0E-4F || ☃x >= 1.0E-4F || ☃xxx <= 0.9999F || ☃xxxx <= 0.9999F);
            ☃.set(0, (☃xxxxx > 0.9999F || ☃.isFullCube()) && ☃xx == ☃xxxxx);
            break;
         case WEST:
            ☃.set(1, ☃x >= 1.0E-4F || ☃xx >= 1.0E-4F || ☃xxxx <= 0.9999F || ☃xxxxx <= 0.9999F);
            ☃.set(0, (☃ < 1.0E-4F || ☃.isFullCube()) && ☃ == ☃xxx);
            break;
         case EAST:
            ☃.set(1, ☃x >= 1.0E-4F || ☃xx >= 1.0E-4F || ☃xxxx <= 0.9999F || ☃xxxxx <= 0.9999F);
            ☃.set(0, (☃xxx > 0.9999F || ☃.isFullCube()) && ☃ == ☃xxx);
      }
   }

   private void renderQuadsFlat(
      IBlockAccess var1, IBlockState var2, BlockPos var3, int var4, boolean var5, BufferBuilder var6, List<BakedQuad> var7, BitSet var8
   ) {
      Vec3d ☃ = ☃.getOffset(☃, ☃);
      double ☃x = ☃.getX() + ☃.x;
      double ☃xx = ☃.getY() + ☃.y;
      double ☃xxx = ☃.getZ() + ☃.z;
      int ☃xxxx = 0;

      for (int ☃xxxxx = ☃.size(); ☃xxxx < ☃xxxxx; ☃xxxx++) {
         BakedQuad ☃xxxxxx = ☃.get(☃xxxx);
         if (☃) {
            this.fillQuadBounds(☃, ☃xxxxxx.getVertexData(), ☃xxxxxx.getFace(), null, ☃);
            BlockPos ☃xxxxxxx = ☃.get(0) ? ☃.offset(☃xxxxxx.getFace()) : ☃;
            ☃ = ☃.getPackedLightmapCoords(☃, ☃xxxxxxx);
         }

         ☃.addVertexData(☃xxxxxx.getVertexData());
         ☃.putBrightness4(☃, ☃, ☃, ☃);
         if (☃xxxxxx.hasTintIndex()) {
            int ☃xxxxxxx = this.blockColors.colorMultiplier(☃, ☃, ☃, ☃xxxxxx.getTintIndex());
            if (EntityRenderer.anaglyphEnable) {
               ☃xxxxxxx = TextureUtil.anaglyphColor(☃xxxxxxx);
            }

            float ☃xxxxxxxx = (☃xxxxxxx >> 16 & 0xFF) / 255.0F;
            float ☃xxxxxxxxx = (☃xxxxxxx >> 8 & 0xFF) / 255.0F;
            float ☃xxxxxxxxxx = (☃xxxxxxx & 0xFF) / 255.0F;
            ☃.putColorMultiplier(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, 4);
            ☃.putColorMultiplier(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, 3);
            ☃.putColorMultiplier(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, 2);
            ☃.putColorMultiplier(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, 1);
         }

         ☃.putPosition(☃x, ☃xx, ☃xxx);
      }
   }

   public void renderModelBrightnessColor(IBakedModel var1, float var2, float var3, float var4, float var5) {
      this.renderModelBrightnessColor(null, ☃, ☃, ☃, ☃, ☃);
   }

   public void renderModelBrightnessColor(IBlockState var1, IBakedModel var2, float var3, float var4, float var5, float var6) {
      for (EnumFacing ☃ : EnumFacing.values()) {
         this.renderModelBrightnessColorQuads(☃, ☃, ☃, ☃, ☃.getQuads(☃, ☃, 0L));
      }

      this.renderModelBrightnessColorQuads(☃, ☃, ☃, ☃, ☃.getQuads(☃, null, 0L));
   }

   public void renderModelBrightness(IBakedModel var1, IBlockState var2, float var3, boolean var4) {
      Block ☃ = ☃.getBlock();
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      int ☃x = this.blockColors.colorMultiplier(☃, null, null, 0);
      if (EntityRenderer.anaglyphEnable) {
         ☃x = TextureUtil.anaglyphColor(☃x);
      }

      float ☃xx = (☃x >> 16 & 0xFF) / 255.0F;
      float ☃xxx = (☃x >> 8 & 0xFF) / 255.0F;
      float ☃xxxx = (☃x & 0xFF) / 255.0F;
      if (!☃) {
         GlStateManager.color(☃, ☃, ☃, 1.0F);
      }

      this.renderModelBrightnessColor(☃, ☃, ☃, ☃xx, ☃xxx, ☃xxxx);
   }

   private void renderModelBrightnessColorQuads(float var1, float var2, float var3, float var4, List<BakedQuad> var5) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      int ☃xx = 0;

      for (int ☃xxx = ☃.size(); ☃xx < ☃xxx; ☃xx++) {
         BakedQuad ☃xxxx = ☃.get(☃xx);
         ☃x.begin(7, DefaultVertexFormats.ITEM);
         ☃x.addVertexData(☃xxxx.getVertexData());
         if (☃xxxx.hasTintIndex()) {
            ☃x.putColorRGB_F4(☃ * ☃, ☃ * ☃, ☃ * ☃);
         } else {
            ☃x.putColorRGB_F4(☃, ☃, ☃);
         }

         Vec3i ☃xxxxx = ☃xxxx.getFace().getDirectionVec();
         ☃x.putNormal(☃xxxxx.getX(), ☃xxxxx.getY(), ☃xxxxx.getZ());
         ☃.draw();
      }
   }

   class AmbientOcclusionFace {
      private final float[] vertexColorMultiplier = new float[4];
      private final int[] vertexBrightness = new int[4];

      public AmbientOcclusionFace() {
      }

      public void updateVertexBrightness(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4, float[] var5, BitSet var6) {
         BlockPos ☃ = ☃.get(0) ? ☃.offset(☃) : ☃;
         BlockPos.PooledMutableBlockPos ☃x = BlockPos.PooledMutableBlockPos.retain();
         BlockModelRenderer.EnumNeighborInfo ☃xx = BlockModelRenderer.EnumNeighborInfo.getNeighbourInfo(☃);
         BlockPos.PooledMutableBlockPos ☃xxx = BlockPos.PooledMutableBlockPos.retain(☃).move(☃xx.corners[0]);
         BlockPos.PooledMutableBlockPos ☃xxxx = BlockPos.PooledMutableBlockPos.retain(☃).move(☃xx.corners[1]);
         BlockPos.PooledMutableBlockPos ☃xxxxx = BlockPos.PooledMutableBlockPos.retain(☃).move(☃xx.corners[2]);
         BlockPos.PooledMutableBlockPos ☃xxxxxx = BlockPos.PooledMutableBlockPos.retain(☃).move(☃xx.corners[3]);
         int ☃xxxxxxx = ☃.getPackedLightmapCoords(☃, ☃xxx);
         int ☃xxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃xxxx);
         int ☃xxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃xxxxx);
         int ☃xxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃xxxxxx);
         float ☃xxxxxxxxxxx = ☃.getBlockState(☃xxx).getAmbientOcclusionLightValue();
         float ☃xxxxxxxxxxxx = ☃.getBlockState(☃xxxx).getAmbientOcclusionLightValue();
         float ☃xxxxxxxxxxxxx = ☃.getBlockState(☃xxxxx).getAmbientOcclusionLightValue();
         float ☃xxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxx).getAmbientOcclusionLightValue();
         boolean ☃xxxxxxxxxxxxxxx = ☃.getBlockState(☃x.setPos(☃xxx).move(☃)).isTranslucent();
         boolean ☃xxxxxxxxxxxxxxxx = ☃.getBlockState(☃x.setPos(☃xxxx).move(☃)).isTranslucent();
         boolean ☃xxxxxxxxxxxxxxxxx = ☃.getBlockState(☃x.setPos(☃xxxxx).move(☃)).isTranslucent();
         boolean ☃xxxxxxxxxxxxxxxxxx = ☃.getBlockState(☃x.setPos(☃xxxxxx).move(☃)).isTranslucent();
         float ☃xxxxxxxxxxxxxxxxxxx;
         int ☃xxxxxxxxxxxxxxxxxxxx;
         if (!☃xxxxxxxxxxxxxxxxx && !☃xxxxxxxxxxxxxxx) {
            ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx;
            ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxx;
         } else {
            BlockPos ☃xxxxxxxxxxxxxxxxxxxxx = ☃x.setPos(☃xxx).move(☃xx.corners[2]);
            ☃xxxxxxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxxxxxxxxx).getAmbientOcclusionLightValue();
            ☃xxxxxxxxxxxxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃xxxxxxxxxxxxxxxxxxxxx);
         }

         float ☃xxxxxxxxxxxxxxxxxxxxx;
         int ☃xxxxxxxxxxxxxxxxxxxxxx;
         if (!☃xxxxxxxxxxxxxxxxxx && !☃xxxxxxxxxxxxxxx) {
            ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx;
            ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxx;
         } else {
            BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃x.setPos(☃xxx).move(☃xx.corners[3]);
            ☃xxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxx).getAmbientOcclusionLightValue();
            ☃xxxxxxxxxxxxxxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃xxxxxxxxxxxxxxxxxxxxxxx);
         }

         float ☃xxxxxxxxxxxxxxxxxxxxxxx;
         int ☃xxxxxxxxxxxxxxxxxxxxxxxx;
         if (!☃xxxxxxxxxxxxxxxxx && !☃xxxxxxxxxxxxxxxx) {
            ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx;
            ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxx;
         } else {
            BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃x.setPos(☃xxxx).move(☃xx.corners[2]);
            ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxxx).getAmbientOcclusionLightValue();
            ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃xxxxxxxxxxxxxxxxxxxxxxxxx);
         }

         float ☃xxxxxxxxxxxxxxxxxxxxxxxxx;
         int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
         if (!☃xxxxxxxxxxxxxxxxxx && !☃xxxxxxxxxxxxxxxx) {
            ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx;
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxx;
         } else {
            BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃x.setPos(☃xxxx).move(☃xx.corners[3]);
            ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx).getAmbientOcclusionLightValue();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx);
         }

         int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃);
         if (☃.get(0) || !☃.getBlockState(☃.offset(☃)).isOpaqueCube()) {
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃.offset(☃));
         }

         float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.get(0)
            ? ☃.getBlockState(☃).getAmbientOcclusionLightValue()
            : ☃.getBlockState(☃).getAmbientOcclusionLightValue();
         BlockModelRenderer.VertexTranslations ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = BlockModelRenderer.VertexTranslations.getVertexTranslations(☃);
         ☃x.release();
         ☃xxx.release();
         ☃xxxx.release();
         ☃xxxxx.release();
         ☃xxxxxx.release();
         if (☃.get(1) && ☃xx.doNonCubicWeight) {
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxx + ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxx + ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert0Weights[0].shape] * ☃[☃xx.vert0Weights[1].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert0Weights[2].shape] * ☃[☃xx.vert0Weights[3].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert0Weights[4].shape] * ☃[☃xx.vert0Weights[5].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert0Weights[6].shape] * ☃[☃xx.vert0Weights[7].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert1Weights[0].shape] * ☃[☃xx.vert1Weights[1].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert1Weights[2].shape] * ☃[☃xx.vert1Weights[3].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert1Weights[4].shape] * ☃[☃xx.vert1Weights[5].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert1Weights[6].shape] * ☃[☃xx.vert1Weights[7].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert2Weights[0].shape] * ☃[☃xx.vert2Weights[1].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert2Weights[2].shape] * ☃[☃xx.vert2Weights[3].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert2Weights[4].shape] * ☃[☃xx.vert2Weights[5].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert2Weights[6].shape] * ☃[☃xx.vert2Weights[7].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert3Weights[0].shape] * ☃[☃xx.vert3Weights[1].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert3Weights[2].shape] * ☃[☃xx.vert3Weights[3].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert3Weights[4].shape] * ☃[☃xx.vert3Weights[5].shape];
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃[☃xx.vert3Weights[6].shape] * ☃[☃xx.vert3Weights[7].shape];
            this.vertexColorMultiplier[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert0] = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            this.vertexColorMultiplier[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert1] = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            this.vertexColorMultiplier[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert2] = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            this.vertexColorMultiplier[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert3] = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                  * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.getAoBrightness(
               ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.getAoBrightness(
               ☃xxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.getAoBrightness(
               ☃xxxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.getAoBrightness(
               ☃xxxxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            this.vertexBrightness[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert0] = this.getVertexBrightness(
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            this.vertexBrightness[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert1] = this.getVertexBrightness(
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            this.vertexBrightness[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert2] = this.getVertexBrightness(
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            this.vertexBrightness[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert3] = this.getVertexBrightness(
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
         } else {
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxx + ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxx + ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
            this.vertexBrightness[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert0] = this.getAoBrightness(
               ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            this.vertexBrightness[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert1] = this.getAoBrightness(
               ☃xxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            this.vertexBrightness[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert2] = this.getAoBrightness(
               ☃xxxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            this.vertexBrightness[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert3] = this.getAoBrightness(
               ☃xxxxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
            );
            this.vertexColorMultiplier[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert0] = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            this.vertexColorMultiplier[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert1] = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            this.vertexColorMultiplier[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert2] = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            this.vertexColorMultiplier[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx.vert3] = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
         }
      }

      private int getAoBrightness(int var1, int var2, int var3, int var4) {
         if (☃ == 0) {
            ☃ = ☃;
         }

         if (☃ == 0) {
            ☃ = ☃;
         }

         if (☃ == 0) {
            ☃ = ☃;
         }

         return ☃ + ☃ + ☃ + ☃ >> 2 & 16711935;
      }

      private int getVertexBrightness(int var1, int var2, int var3, int var4, float var5, float var6, float var7, float var8) {
         int ☃ = (int)((☃ >> 16 & 0xFF) * ☃ + (☃ >> 16 & 0xFF) * ☃ + (☃ >> 16 & 0xFF) * ☃ + (☃ >> 16 & 0xFF) * ☃) & 0xFF;
         int ☃x = (int)((☃ & 0xFF) * ☃ + (☃ & 0xFF) * ☃ + (☃ & 0xFF) * ☃ + (☃ & 0xFF) * ☃) & 0xFF;
         return ☃ << 16 | ☃x;
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
         EnumFacing[] var3,
         float var4,
         boolean var5,
         BlockModelRenderer.Orientation[] var6,
         BlockModelRenderer.Orientation[] var7,
         BlockModelRenderer.Orientation[] var8,
         BlockModelRenderer.Orientation[] var9
      ) {
         this.corners = ☃;
         this.shadeWeight = ☃;
         this.doNonCubicWeight = ☃;
         this.vert0Weights = ☃;
         this.vert1Weights = ☃;
         this.vert2Weights = ☃;
         this.vert3Weights = ☃;
      }

      public static BlockModelRenderer.EnumNeighborInfo getNeighbourInfo(EnumFacing var0) {
         return VALUES[☃.getIndex()];
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

      private Orientation(EnumFacing var3, boolean var4) {
         this.shape = ☃.getIndex() + (☃ ? EnumFacing.values().length : 0);
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

      private VertexTranslations(int var3, int var4, int var5, int var6) {
         this.vert0 = ☃;
         this.vert1 = ☃;
         this.vert2 = ☃;
         this.vert3 = ☃;
      }

      public static BlockModelRenderer.VertexTranslations getVertexTranslations(EnumFacing var0) {
         return VALUES[☃.getIndex()];
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
