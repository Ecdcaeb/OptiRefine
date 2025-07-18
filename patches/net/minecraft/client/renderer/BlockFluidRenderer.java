package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockFluidRenderer {
   private final BlockColors blockColors;
   private final TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
   private final TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];
   private TextureAtlasSprite atlasSpriteWaterOverlay;

   public BlockFluidRenderer(BlockColors var1) {
      this.blockColors = ☃;
      this.initAtlasSprites();
   }

   protected void initAtlasSprites() {
      TextureMap ☃ = Minecraft.getMinecraft().getTextureMapBlocks();
      this.atlasSpritesLava[0] = ☃.getAtlasSprite("minecraft:blocks/lava_still");
      this.atlasSpritesLava[1] = ☃.getAtlasSprite("minecraft:blocks/lava_flow");
      this.atlasSpritesWater[0] = ☃.getAtlasSprite("minecraft:blocks/water_still");
      this.atlasSpritesWater[1] = ☃.getAtlasSprite("minecraft:blocks/water_flow");
      this.atlasSpriteWaterOverlay = ☃.getAtlasSprite("minecraft:blocks/water_overlay");
   }

   public boolean renderFluid(IBlockAccess var1, IBlockState var2, BlockPos var3, BufferBuilder var4) {
      BlockLiquid ☃ = (BlockLiquid)☃.getBlock();
      boolean ☃x = ☃.getMaterial() == Material.LAVA;
      TextureAtlasSprite[] ☃xx = ☃x ? this.atlasSpritesLava : this.atlasSpritesWater;
      int ☃xxx = this.blockColors.colorMultiplier(☃, ☃, ☃, 0);
      float ☃xxxx = (☃xxx >> 16 & 0xFF) / 255.0F;
      float ☃xxxxx = (☃xxx >> 8 & 0xFF) / 255.0F;
      float ☃xxxxxx = (☃xxx & 0xFF) / 255.0F;
      boolean ☃xxxxxxx = ☃.shouldSideBeRendered(☃, ☃, EnumFacing.UP);
      boolean ☃xxxxxxxx = ☃.shouldSideBeRendered(☃, ☃, EnumFacing.DOWN);
      boolean[] ☃xxxxxxxxx = new boolean[]{
         ☃.shouldSideBeRendered(☃, ☃, EnumFacing.NORTH),
         ☃.shouldSideBeRendered(☃, ☃, EnumFacing.SOUTH),
         ☃.shouldSideBeRendered(☃, ☃, EnumFacing.WEST),
         ☃.shouldSideBeRendered(☃, ☃, EnumFacing.EAST)
      };
      if (!☃xxxxxxx && !☃xxxxxxxx && !☃xxxxxxxxx[0] && !☃xxxxxxxxx[1] && !☃xxxxxxxxx[2] && !☃xxxxxxxxx[3]) {
         return false;
      } else {
         boolean ☃xxxxxxxxxx = false;
         float ☃xxxxxxxxxxx = 0.5F;
         float ☃xxxxxxxxxxxx = 1.0F;
         float ☃xxxxxxxxxxxxx = 0.8F;
         float ☃xxxxxxxxxxxxxx = 0.6F;
         Material ☃xxxxxxxxxxxxxxx = ☃.getMaterial();
         float ☃xxxxxxxxxxxxxxxx = this.getFluidHeight(☃, ☃, ☃xxxxxxxxxxxxxxx);
         float ☃xxxxxxxxxxxxxxxxx = this.getFluidHeight(☃, ☃.south(), ☃xxxxxxxxxxxxxxx);
         float ☃xxxxxxxxxxxxxxxxxx = this.getFluidHeight(☃, ☃.east().south(), ☃xxxxxxxxxxxxxxx);
         float ☃xxxxxxxxxxxxxxxxxxx = this.getFluidHeight(☃, ☃.east(), ☃xxxxxxxxxxxxxxx);
         double ☃xxxxxxxxxxxxxxxxxxxx = ☃.getX();
         double ☃xxxxxxxxxxxxxxxxxxxxx = ☃.getY();
         double ☃xxxxxxxxxxxxxxxxxxxxxx = ☃.getZ();
         float ☃xxxxxxxxxxxxxxxxxxxxxxx = 0.001F;
         if (☃xxxxxxx) {
            ☃xxxxxxxxxx = true;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxx = BlockLiquid.getSlopeAngle(☃, ☃, ☃xxxxxxxxxxxxxxx, ☃);
            TextureAtlasSprite ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxx > -999.0F ? ☃xx[1] : ☃xx[0];
            ☃xxxxxxxxxxxxxxxx -= 0.001F;
            ☃xxxxxxxxxxxxxxxxx -= 0.001F;
            ☃xxxxxxxxxxxxxxxxxx -= 0.001F;
            ☃xxxxxxxxxxxxxxxxxxx -= 0.001F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            if (☃xxxxxxxxxxxxxxxxxxxxxxxx < -999.0F) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedU(0.0);
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedV(0.0);
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedV(16.0);
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedU(16.0);
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            } else {
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = MathHelper.cos(☃xxxxxxxxxxxxxxxxxxxxxxxx) * 0.25F;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 8.0F;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedU(
                  8.0F + (-☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F
               );
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedV(
                  8.0F + (-☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F
               );
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedU(
                  8.0F + (-☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F
               );
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedV(
                  8.0F + (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F
               );
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedU(
                  8.0F + (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F
               );
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedV(
                  8.0F + (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F
               );
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedU(
                  8.0F + (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F
               );
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedV(
                  8.0F + (-☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F
               );
            }

            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃);
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx >> 16 & 65535;
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx & 65535;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 1.0F * ☃xxxx;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 1.0F * ☃xxxxx;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 1.0F * ☃xxxxxx;
            ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 0.0, ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 0.0)
               .color(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .endVertex();
            ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 0.0, ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0)
               .color(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .endVertex();
            ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 1.0, ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0)
               .color(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .endVertex();
            ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 1.0, ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 0.0)
               .color(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .endVertex();
            if (☃.shouldRenderSides(☃, ☃.up())) {
               ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 0.0, ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 0.0)
                  .color(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
                  .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .endVertex();
               ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 1.0, ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 0.0)
                  .color(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
                  .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .endVertex();
               ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 1.0, ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0)
                  .color(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
                  .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .endVertex();
               ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 0.0, ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0)
                  .color(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
                  .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .endVertex();
            }
         }

         if (☃xxxxxxxx) {
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xx[0].getMinU();
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xx[0].getMaxU();
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xx[0].getMinV();
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xx[0].getMaxV();
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃.down());
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx >> 16 & 65535;
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx & 65535;
            ☃.pos(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0)
               .color(0.5F, 0.5F, 0.5F, 1.0F)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .endVertex();
            ☃.pos(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx)
               .color(0.5F, 0.5F, 0.5F, 1.0F)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .endVertex();
            ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 1.0, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx)
               .color(0.5F, 0.5F, 0.5F, 1.0F)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .endVertex();
            ☃.pos(☃xxxxxxxxxxxxxxxxxxxx + 1.0, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0)
               .color(0.5F, 0.5F, 0.5F, 1.0F)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
               .endVertex();
            ☃xxxxxxxxxx = true;
         }

         for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0;
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0;
            if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == 0) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx--;
            }

            if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == 1) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++;
            }

            if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == 2) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx--;
            }

            if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == 3) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++;
            }

            BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.add(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
            TextureAtlasSprite ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xx[1];
            if (!☃x) {
               Block ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx).getBlock();
               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == Blocks.GLASS || ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == Blocks.STAINED_GLASS) {
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.atlasSpriteWaterOverlay;
               }
            }

            if (☃xxxxxxxxx[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx]) {
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == 0) {
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx + 1.0;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx + 0.001F;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx + 0.001F;
               } else if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == 1) {
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx + 1.0;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0 - 0.001F;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0 - 0.001F;
               } else if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == 2) {
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx + 0.001F;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx + 0.001F;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx;
               } else {
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx + 1.0 - 0.001F;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx + 1.0 - 0.001F;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx + 1.0;
               }

               ☃xxxxxxxxxx = true;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedU(0.0);
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedU(8.0);
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedV(
                  (1.0F - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F * 0.5F
               );
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedV(
                  (1.0F - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 16.0F * 0.5F
               );
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getInterpolatedV(8.0);
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getPackedLightmapCoords(☃, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx >> 16 & 65535;
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx & 65535;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < 2 ? 0.8F : 0.6F;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 1.0F * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxx;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 1.0F * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxx;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 1.0F * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxx;
               ☃.pos(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                  )
                  .color(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     1.0F
                  )
                  .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .endVertex();
               ☃.pos(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                  )
                  .color(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     1.0F
                  )
                  .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .endVertex();
               ☃.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx + 0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .color(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     1.0F
                  )
                  .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .endVertex();
               ☃.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx + 0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .color(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                     1.0F
                  )
                  .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .endVertex();
               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx != this.atlasSpriteWaterOverlay) {
                  ☃.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx + 0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .color(
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        1.0F
                     )
                     .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .endVertex();
                  ☃.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx + 0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .color(
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        1.0F
                     )
                     .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .endVertex();
                  ☃.pos(
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        1.0F
                     )
                     .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .endVertex();
                  ☃.pos(
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                        1.0F
                     )
                     .tex(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                     .endVertex();
               }
            }
         }

         return ☃xxxxxxxxxx;
      }
   }

   private float getFluidHeight(IBlockAccess var1, BlockPos var2, Material var3) {
      int ☃ = 0;
      float ☃x = 0.0F;

      for (int ☃xx = 0; ☃xx < 4; ☃xx++) {
         BlockPos ☃xxx = ☃.add(-(☃xx & 1), 0, -(☃xx >> 1 & 1));
         if (☃.getBlockState(☃xxx.up()).getMaterial() == ☃) {
            return 1.0F;
         }

         IBlockState ☃xxxx = ☃.getBlockState(☃xxx);
         Material ☃xxxxx = ☃xxxx.getMaterial();
         if (☃xxxxx == ☃) {
            int ☃xxxxxx = ☃xxxx.getValue(BlockLiquid.LEVEL);
            if (☃xxxxxx >= 8 || ☃xxxxxx == 0) {
               ☃x += BlockLiquid.getLiquidHeightPercent(☃xxxxxx) * 10.0F;
               ☃ += 10;
            }

            ☃x += BlockLiquid.getLiquidHeightPercent(☃xxxxxx);
            ☃++;
         } else if (!☃xxxxx.isSolid()) {
            ☃x++;
            ☃++;
         }
      }

      return 1.0F - ☃x / ☃;
   }
}
