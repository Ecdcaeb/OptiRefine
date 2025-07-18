package net.minecraft.world.gen;

import com.google.common.base.MoreObjects;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenCaves extends MapGenBase {
   protected static final IBlockState BLK_LAVA = Blocks.LAVA.getDefaultState();
   protected static final IBlockState BLK_AIR = Blocks.AIR.getDefaultState();
   protected static final IBlockState BLK_SANDSTONE = Blocks.SANDSTONE.getDefaultState();
   protected static final IBlockState BLK_RED_SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();

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
         if (!☃xxxxx && ☃ == ☃xxxxxx && ☃ > 1.0F && ☃ > 0) {
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

               if (☃xxxxxxxxxxxxxxxxxxx > 248) {
                  ☃xxxxxxxxxxxxxxxxxxx = 248;
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
                        if (☃xxxxxxxxxxxxxxxxxxxxxxxxx >= 0 && ☃xxxxxxxxxxxxxxxxxxxxxxxxx < 256) {
                           IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(
                              ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx
                           );
                           if (☃xxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.FLOWING_WATER || ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.WATER) {
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
                  BlockPos.MutableBlockPos ☃xxxxxxxxxxxxxxxxxxxxxxx = new BlockPos.MutableBlockPos();

                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxxx++) {
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16 + 0.5 - ☃) / ☃xxxxxxxx;

                     for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx++
                     ) {
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16 + 0.5 - ☃) / ☃xxxxxxxx;
                        boolean ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = false;
                        if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx < 1.0
                           )
                         {
                           for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx;
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx > ☃xxxxxxxxxxxxxxxxxx;
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx--
                           ) {
                              double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1 + 0.5 - ☃) / ☃xxxxxxxxx;
                              if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx > -0.7
                                 && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
                                       + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                       + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                    < 1.0) {
                                 IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(
                                    ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                 );
                                 IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (IBlockState)MoreObjects.firstNonNull(
                                    ☃.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 1, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx), BLK_AIR
                                 );
                                 if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.GRASS
                                    || ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.getBlock() == Blocks.MYCELIUM) {
                                    ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = true;
                                 }

                                 if (this.canReplaceBlock(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)) {
                                    if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1 < 10) {
                                       ☃.setBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, BLK_LAVA);
                                    } else {
                                       ☃.setBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, BLK_AIR);
                                       if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                          && ☃.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                                                .getBlock()
                                             == Blocks.DIRT) {
                                          ☃xxxxxxxxxxxxxxxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16, 0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃ * 16);
                                          ☃.setBlockState(
                                             ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                                             ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - 1,
                                             ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                                             this.world.getBiome(☃xxxxxxxxxxxxxxxxxxxxxxx).topBlock.getBlock().getDefaultState()
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

   protected boolean canReplaceBlock(IBlockState var1, IBlockState var2) {
      if (☃.getBlock() == Blocks.STONE) {
         return true;
      } else if (☃.getBlock() == Blocks.DIRT) {
         return true;
      } else if (☃.getBlock() == Blocks.GRASS) {
         return true;
      } else if (☃.getBlock() == Blocks.HARDENED_CLAY) {
         return true;
      } else if (☃.getBlock() == Blocks.STAINED_HARDENED_CLAY) {
         return true;
      } else if (☃.getBlock() == Blocks.SANDSTONE) {
         return true;
      } else if (☃.getBlock() == Blocks.RED_SANDSTONE) {
         return true;
      } else if (☃.getBlock() == Blocks.MYCELIUM) {
         return true;
      } else {
         return ☃.getBlock() == Blocks.SNOW_LAYER ? true : (☃.getBlock() == Blocks.SAND || ☃.getBlock() == Blocks.GRAVEL) && ☃.getMaterial() != Material.WATER;
      }
   }

   @Override
   protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, ChunkPrimer var6) {
      int ☃ = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);
      if (this.rand.nextInt(7) != 0) {
         ☃ = 0;
      }

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         double ☃xx = ☃ * 16 + this.rand.nextInt(16);
         double ☃xxx = this.rand.nextInt(this.rand.nextInt(120) + 8);
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
            if (this.rand.nextInt(10) == 0) {
               ☃xxxxxxxxx *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
            }

            this.addTunnel(this.rand.nextLong(), ☃, ☃, ☃, ☃xx, ☃xxx, ☃xxxx, ☃xxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0, 0, 1.0);
         }
      }
   }
}
