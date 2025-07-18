package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenRavine extends MapGenBase {
   protected static final IBlockState FLOWING_LAVA = Blocks.FLOWING_LAVA.getDefaultState();
   protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
   private final float[] rs = new float[1024];

   protected void addTunnel(
      long var1,
      int var3,
      int var4,
      ChunkPrimer var5,
      double var6,
      double var8,
      double var10,
      float var12,
      float var13,
      float var14,
      int var15,
      int var16,
      double var17
   ) {
      Random ☃ = new Random(☃);
      double ☃x = ☃ * 16 + 8;
      double ☃xx = ☃ * 16 + 8;
      float ☃xxx = 0.0F;
      float ☃xxxx = 0.0F;
      if (☃ <= 0) {
         int ☃xxxxx = this.range * 16 - 16;
         ☃ = ☃xxxxx - ☃.nextInt(☃xxxxx / 4);
      }

      boolean ☃xxxxx = false;
      if (☃ == -1) {
         ☃ = ☃ / 2;
         ☃xxxxx = true;
      }

      float ☃xxxxxx = 1.0F;

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < 256; ☃xxxxxxx++) {
         if (☃xxxxxxx == 0 || ☃.nextInt(3) == 0) {
            ☃xxxxxx = 1.0F + ☃.nextFloat() * ☃.nextFloat();
         }

         this.rs[☃xxxxxxx] = ☃xxxxxx * ☃xxxxxx;
      }

      for (; ☃ < ☃; ☃++) {
         double ☃xxxxxxx = 1.5 + MathHelper.sin(☃ * (float) Math.PI / ☃) * ☃;
         double ☃xxxxxxxx = ☃xxxxxxx * ☃;
         ☃xxxxxxx *= ☃.nextFloat() * 0.25 + 0.75;
         ☃xxxxxxxx *= ☃.nextFloat() * 0.25 + 0.75;
         float ☃xxxxxxxxx = MathHelper.cos(☃);
         float ☃xxxxxxxxxx = MathHelper.sin(☃);
         ☃ += MathHelper.cos(☃) * ☃xxxxxxxxx;
         ☃ += ☃xxxxxxxxxx;
         ☃ += MathHelper.sin(☃) * ☃xxxxxxxxx;
         ☃ *= 0.7F;
         ☃ += ☃xxxx * 0.05F;
         ☃ += ☃xxx * 0.05F;
         ☃xxxx *= 0.8F;
         ☃xxx *= 0.5F;
         ☃xxxx += (☃.nextFloat() - ☃.nextFloat()) * ☃.nextFloat() * 2.0F;
         ☃xxx += (☃.nextFloat() - ☃.nextFloat()) * ☃.nextFloat() * 4.0F;
         if (☃xxxxx || ☃.nextInt(4) != 0) {
            double ☃xxxxxxxxxxx = ☃ - ☃x;
            double ☃xxxxxxxxxxxx = ☃ - ☃xx;
            double ☃xxxxxxxxxxxxx = ☃ - ☃;
            double ☃xxxxxxxxxxxxxx = ☃ + 2.0F + 16.0F;
            if (☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx - ☃xxxxxxxxxxxxx * ☃xxxxxxxxxxxxx > ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx) {
               return;
            }

            if (!(☃ < ☃x - 16.0 - ☃xxxxxxx * 2.0)
               && !(☃ < ☃xx - 16.0 - ☃xxxxxxx * 2.0)
               && !(☃ > ☃x + 16.0 + ☃xxxxxxx * 2.0)
               && !(☃ > ☃xx + 16.0 + ☃xxxxxxx * 2.0)) {
               int ☃xxxxxxxxxxxxxxx = MathHelper.floor(☃ - ☃xxxxxxx) - ☃ * 16 - 1;
               int ☃xxxxxxxxxxxxxxxx = MathHelper.floor(☃ + ☃xxxxxxx) - ☃ * 16 + 1;
               int ☃xxxxxxxxxxxxxxxxx = MathHelper.floor(☃ - ☃xxxxxxxx) - 1;
               int ☃xxxxxxxxxxxxxxxxxx = MathHelper.floor(☃ + ☃xxxxxxxx) + 1;
               int ☃xxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃ - ☃xxxxxxx) - ☃ * 16 - 1;
               int ☃xxxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃ + ☃xxxxxxx) - ☃ * 16 + 1;
               if (☃xxxxxxxxxxxxxxx < 0) {
                  ☃xxxxxxxxxxxxxxx = 0;
               }

               if (☃xxxxxxxxxxxxxxxx > 16) {
                  ☃xxxxxxxxxxxxxxxx = 16;
               }

               if (☃xxxxxxxxxxxxxxxxx < 1) {
                  ☃xxxxxxxxxxxxxxxxx = 1;
               }

               if (☃xxxxxxxxxxxxxxxxxx > 248) {
                  ☃xxxxxxxxxxxxxxxxxx = 248;
               }

               if (☃xxxxxxxxxxxxxxxxxxx < 0) {
                  ☃xxxxxxxxxxxxxxxxxxx = 0;
               }

               if (☃xxxxxxxxxxxxxxxxxxxx > 16) {
                  ☃xxxxxxxxxxxxxxxxxxxx = 16;
               }

               boolean ☃xxxxxxxxxxxxxxxxxxxxx = false;

               for (int ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx;
                  !☃xxxxxxxxxxxxxxxxxxxxx && ☃xxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxx++
               ) {
                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx;
                     !☃xxxxxxxxxxxxxxxxxxxxx && ☃xxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxxxxxxxxxxxxxxxxxxxx++
                  ) {
                     for (int ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx + 1;
                        !☃xxxxxxxxxxxxxxxxxxxxx && ☃xxxxxxxxxxxxxxxxxxxxxxxx >= ☃xxxxxxxxxxxxxxxxx - 1;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxx--
                     ) {
                        if (☃xxxxxxxxxxxxxxxxxxxxxxxx >= 0 && ☃xxxxxxxxxxxxxxxxxxxxxxxx < 256) {
                           IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(
                              ☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx
                           );
                           if (☃xxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.FLOWING_WATER || ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.WATER) {
                              ☃xxxxxxxxxxxxxxxxxxxxx = true;
                           }

                           if (☃xxxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxx - 1
                              && ☃xxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxx
                              && ☃xxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxx - 1
                              && ☃xxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxxxx
                              && ☃xxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxxxxx - 1) {
                              ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx;
                           }
                        }
                     }
                  }
               }

               if (!☃xxxxxxxxxxxxxxxxxxxxx) {
                  BlockPos.MutableBlockPos ☃xxxxxxxxxxxxxxxxxxxxxx = new BlockPos.MutableBlockPos();

                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16 + 0.5 - ☃) / ☃xxxxxxx;

                     for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx++
                     ) {
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16 + 0.5 - ☃) / ☃xxxxxxx;
                        boolean ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = false;
                        if (☃xxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx < 1.0) {
                           for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx;
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx > ☃xxxxxxxxxxxxxxxxx;
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx--
                           ) {
                              double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1 + 0.5 - ☃) / ☃xxxxxxxx;
                              if ((☃xxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                                       * this.rs[☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1]
                                    + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx / 6.0
                                 < 1.0) {
                                 IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(
                                    ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
                                 );
                                 if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.GRASS) {
                                    ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = true;
                                 }

                                 if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.STONE
                                    || ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.DIRT
                                    || ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.GRASS) {
                                    if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1 < 10) {
                                       ☃.setBlockState(☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, FLOWING_LAVA);
                                    } else {
                                       ☃.setBlockState(☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, AIR);
                                       if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                          && ☃.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx)
                                                .getBlock()
                                             == Blocks.DIRT) {
                                          ☃xxxxxxxxxxxxxxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16, 0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16);
                                          ☃.setBlockState(
                                             ☃xxxxxxxxxxxxxxxxxxxxxxx,
                                             ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1,
                                             ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx,
                                             this.world.getBiome(☃xxxxxxxxxxxxxxxxxxxxxx).topBlock
                                          );
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  if (☃xxxxx) {
                     break;
                  }
               }
            }
         }
      }
   }

   @Override
   protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, ChunkPrimer var6) {
      if (this.rand.nextInt(50) == 0) {
         double ☃ = ☃ * 16 + this.rand.nextInt(16);
         double ☃x = this.rand.nextInt(this.rand.nextInt(40) + 8) + 20;
         double ☃xx = ☃ * 16 + this.rand.nextInt(16);
         int ☃xxx = 1;

         for (int ☃xxxx = 0; ☃xxxx < 1; ☃xxxx++) {
            float ☃xxxxx = this.rand.nextFloat() * (float) (Math.PI * 2);
            float ☃xxxxxx = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
            float ☃xxxxxxx = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
            this.addTunnel(this.rand.nextLong(), ☃, ☃, ☃, ☃, ☃x, ☃xx, ☃xxxxxxx, ☃xxxxx, ☃xxxxxx, 0, 0, 3.0);
         }
      }
   }
}
