package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenCavesHell extends MapGenBase {
   protected static final IBlockState AIR = Blocks.AIR.getDefaultState();

   protected void addRoom(long var1, int var3, int var4, ChunkPrimer var5, double var6, double var8, double var10) {
      this.addTunnel(☃, ☃, ☃, ☃, ☃, ☃, ☃, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5);
   }

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
      double ☃ = ☃ * 16 + 8;
      double ☃x = ☃ * 16 + 8;
      float ☃xx = 0.0F;
      float ☃xxx = 0.0F;
      Random ☃xxxx = new Random(☃);
      if (☃ <= 0) {
         int ☃xxxxx = this.range * 16 - 16;
         ☃ = ☃xxxxx - ☃xxxx.nextInt(☃xxxxx / 4);
      }

      boolean ☃xxxxx = false;
      if (☃ == -1) {
         ☃ = ☃ / 2;
         ☃xxxxx = true;
      }

      int ☃xxxxxx = ☃xxxx.nextInt(☃ / 2) + ☃ / 4;

      for (boolean ☃xxxxxxx = ☃xxxx.nextInt(6) == 0; ☃ < ☃; ☃++) {
         double ☃xxxxxxxx = 1.5 + MathHelper.sin(☃ * (float) Math.PI / ☃) * ☃;
         double ☃xxxxxxxxx = ☃xxxxxxxx * ☃;
         float ☃xxxxxxxxxx = MathHelper.cos(☃);
         float ☃xxxxxxxxxxx = MathHelper.sin(☃);
         ☃ += MathHelper.cos(☃) * ☃xxxxxxxxxx;
         ☃ += ☃xxxxxxxxxxx;
         ☃ += MathHelper.sin(☃) * ☃xxxxxxxxxx;
         if (☃xxxxxxx) {
            ☃ *= 0.92F;
         } else {
            ☃ *= 0.7F;
         }

         ☃ += ☃xxx * 0.1F;
         ☃ += ☃xx * 0.1F;
         ☃xxx *= 0.9F;
         ☃xx *= 0.75F;
         ☃xxx += (☃xxxx.nextFloat() - ☃xxxx.nextFloat()) * ☃xxxx.nextFloat() * 2.0F;
         ☃xx += (☃xxxx.nextFloat() - ☃xxxx.nextFloat()) * ☃xxxx.nextFloat() * 4.0F;
         if (!☃xxxxx && ☃ == ☃xxxxxx && ☃ > 1.0F) {
            this.addTunnel(☃xxxx.nextLong(), ☃, ☃, ☃, ☃, ☃, ☃, ☃xxxx.nextFloat() * 0.5F + 0.5F, ☃ - (float) (Math.PI / 2), ☃ / 3.0F, ☃, ☃, 1.0);
            this.addTunnel(☃xxxx.nextLong(), ☃, ☃, ☃, ☃, ☃, ☃, ☃xxxx.nextFloat() * 0.5F + 0.5F, ☃ + (float) (Math.PI / 2), ☃ / 3.0F, ☃, ☃, 1.0);
            return;
         }

         if (☃xxxxx || ☃xxxx.nextInt(4) != 0) {
            double ☃xxxxxxxxxxxx = ☃ - ☃;
            double ☃xxxxxxxxxxxxx = ☃ - ☃x;
            double ☃xxxxxxxxxxxxxx = ☃ - ☃;
            double ☃xxxxxxxxxxxxxxx = ☃ + 2.0F + 16.0F;
            if (☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxx * ☃xxxxxxxxxxxxx - ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx > ☃xxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx) {
               return;
            }

            if (!(☃ < ☃ - 16.0 - ☃xxxxxxxx * 2.0)
               && !(☃ < ☃x - 16.0 - ☃xxxxxxxx * 2.0)
               && !(☃ > ☃ + 16.0 + ☃xxxxxxxx * 2.0)
               && !(☃ > ☃x + 16.0 + ☃xxxxxxxx * 2.0)) {
               int ☃xxxxxxxxxxxxxxxx = MathHelper.floor(☃ - ☃xxxxxxxx) - ☃ * 16 - 1;
               int ☃xxxxxxxxxxxxxxxxx = MathHelper.floor(☃ + ☃xxxxxxxx) - ☃ * 16 + 1;
               int ☃xxxxxxxxxxxxxxxxxx = MathHelper.floor(☃ - ☃xxxxxxxxx) - 1;
               int ☃xxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃ + ☃xxxxxxxxx) + 1;
               int ☃xxxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃ - ☃xxxxxxxx) - ☃ * 16 - 1;
               int ☃xxxxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃ + ☃xxxxxxxx) - ☃ * 16 + 1;
               if (☃xxxxxxxxxxxxxxxx < 0) {
                  ☃xxxxxxxxxxxxxxxx = 0;
               }

               if (☃xxxxxxxxxxxxxxxxx > 16) {
                  ☃xxxxxxxxxxxxxxxxx = 16;
               }

               if (☃xxxxxxxxxxxxxxxxxx < 1) {
                  ☃xxxxxxxxxxxxxxxxxx = 1;
               }

               if (☃xxxxxxxxxxxxxxxxxxx > 120) {
                  ☃xxxxxxxxxxxxxxxxxxx = 120;
               }

               if (☃xxxxxxxxxxxxxxxxxxxx < 0) {
                  ☃xxxxxxxxxxxxxxxxxxxx = 0;
               }

               if (☃xxxxxxxxxxxxxxxxxxxxx > 16) {
                  ☃xxxxxxxxxxxxxxxxxxxxx = 16;
               }

               boolean ☃xxxxxxxxxxxxxxxxxxxxxx = false;

               for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
                  !☃xxxxxxxxxxxxxxxxxxxxxx && ☃xxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxxxxxx++
               ) {
                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx;
                     !☃xxxxxxxxxxxxxxxxxxxxxx && ☃xxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxxxxxxxxxxxxxxxxxxxxx++
                  ) {
                     for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx + 1;
                        !☃xxxxxxxxxxxxxxxxxxxxxx && ☃xxxxxxxxxxxxxxxxxxxxxxxxx >= ☃xxxxxxxxxxxxxxxxxx - 1;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxx--
                     ) {
                        if (☃xxxxxxxxxxxxxxxxxxxxxxxxx >= 0 && ☃xxxxxxxxxxxxxxxxxxxxxxxxx < 128) {
                           IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(
                              ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx
                           );
                           if (☃xxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.FLOWING_LAVA || ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.LAVA) {
                              ☃xxxxxxxxxxxxxxxxxxxxxx = true;
                           }

                           if (☃xxxxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxxx - 1
                              && ☃xxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxx
                              && ☃xxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxx - 1
                              && ☃xxxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxxxxx
                              && ☃xxxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxxxxxx - 1) {
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx;
                           }
                        }
                     }
                  }
               }

               if (!☃xxxxxxxxxxxxxxxxxxxxxx) {
                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16 + 0.5 - ☃) / ☃xxxxxxxx;

                     for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx++
                     ) {
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16 + 0.5 - ☃) / ☃xxxxxxxx;

                        for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx;
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx > ☃xxxxxxxxxxxxxxxxxx;
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx--
                        ) {
                           double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1 + 0.5 - ☃) / ☃xxxxxxxxx;
                           if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx > -0.7
                              && ☃xxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxx
                                    + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                    + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                 < 1.0) {
                              IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(
                                 ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
                              );
                              if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.NETHERRACK
                                 || ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.DIRT
                                 || ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.GRASS) {
                                 ☃.setBlockState(☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, AIR);
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
      int ☃ = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
      if (this.rand.nextInt(5) != 0) {
         ☃ = 0;
      }

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         double ☃xx = ☃ * 16 + this.rand.nextInt(16);
         double ☃xxx = this.rand.nextInt(128);
         double ☃xxxx = ☃ * 16 + this.rand.nextInt(16);
         int ☃xxxxx = 1;
         if (this.rand.nextInt(4) == 0) {
            this.addRoom(this.rand.nextLong(), ☃, ☃, ☃, ☃xx, ☃xxx, ☃xxxx);
            ☃xxxxx += this.rand.nextInt(4);
         }

         for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxxx; ☃xxxxxx++) {
            float ☃xxxxxxx = this.rand.nextFloat() * (float) (Math.PI * 2);
            float ☃xxxxxxxx = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
            float ☃xxxxxxxxx = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
            this.addTunnel(this.rand.nextLong(), ☃, ☃, ☃, ☃xx, ☃xxx, ☃xxxx, ☃xxxxxxxxx * 2.0F, ☃xxxxxxx, ☃xxxxxxxx, 0, 0, 0.5);
         }
      }
   }
}
