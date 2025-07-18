package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldGenLakes extends WorldGenerator {
   private final Block block;

   public WorldGenLakes(Block var1) {
      this.block = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      ☃ = ☃.add(-8, 0, -8);

      while (☃.getY() > 5 && ☃.isAirBlock(☃)) {
         ☃ = ☃.down();
      }

      if (☃.getY() <= 4) {
         return false;
      } else {
         ☃ = ☃.down(4);
         boolean[] ☃ = new boolean[2048];
         int ☃x = ☃.nextInt(4) + 4;

         for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
            double ☃xxx = ☃.nextDouble() * 6.0 + 3.0;
            double ☃xxxx = ☃.nextDouble() * 4.0 + 2.0;
            double ☃xxxxx = ☃.nextDouble() * 6.0 + 3.0;
            double ☃xxxxxx = ☃.nextDouble() * (16.0 - ☃xxx - 2.0) + 1.0 + ☃xxx / 2.0;
            double ☃xxxxxxx = ☃.nextDouble() * (8.0 - ☃xxxx - 4.0) + 2.0 + ☃xxxx / 2.0;
            double ☃xxxxxxxx = ☃.nextDouble() * (16.0 - ☃xxxxx - 2.0) + 1.0 + ☃xxxxx / 2.0;

            for (int ☃xxxxxxxxx = 1; ☃xxxxxxxxx < 15; ☃xxxxxxxxx++) {
               for (int ☃xxxxxxxxxx = 1; ☃xxxxxxxxxx < 15; ☃xxxxxxxxxx++) {
                  for (int ☃xxxxxxxxxxx = 1; ☃xxxxxxxxxxx < 7; ☃xxxxxxxxxxx++) {
                     double ☃xxxxxxxxxxxx = (☃xxxxxxxxx - ☃xxxxxx) / (☃xxx / 2.0);
                     double ☃xxxxxxxxxxxxx = (☃xxxxxxxxxxx - ☃xxxxxxx) / (☃xxxx / 2.0);
                     double ☃xxxxxxxxxxxxxx = (☃xxxxxxxxxx - ☃xxxxxxxx) / (☃xxxxx / 2.0);
                     double ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxx * ☃xxxxxxxxxxxxx + ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx;
                     if (☃xxxxxxxxxxxxxxx < 1.0) {
                        ☃[(☃xxxxxxxxx * 16 + ☃xxxxxxxxxx) * 8 + ☃xxxxxxxxxxx] = true;
                     }
                  }
               }
            }
         }

         for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
            for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
               for (int ☃xxxx = 0; ☃xxxx < 8; ☃xxxx++) {
                  boolean ☃xxxxx = !☃[(☃xx * 16 + ☃xxx) * 8 + ☃xxxx]
                     && (
                        ☃xx < 15 && ☃[((☃xx + 1) * 16 + ☃xxx) * 8 + ☃xxxx]
                           || ☃xx > 0 && ☃[((☃xx - 1) * 16 + ☃xxx) * 8 + ☃xxxx]
                           || ☃xxx < 15 && ☃[(☃xx * 16 + ☃xxx + 1) * 8 + ☃xxxx]
                           || ☃xxx > 0 && ☃[(☃xx * 16 + (☃xxx - 1)) * 8 + ☃xxxx]
                           || ☃xxxx < 7 && ☃[(☃xx * 16 + ☃xxx) * 8 + ☃xxxx + 1]
                           || ☃xxxx > 0 && ☃[(☃xx * 16 + ☃xxx) * 8 + (☃xxxx - 1)]
                     );
                  if (☃xxxxx) {
                     Material ☃xxxxxx = ☃.getBlockState(☃.add(☃xx, ☃xxxx, ☃xxx)).getMaterial();
                     if (☃xxxx >= 4 && ☃xxxxxx.isLiquid()) {
                        return false;
                     }

                     if (☃xxxx < 4 && !☃xxxxxx.isSolid() && ☃.getBlockState(☃.add(☃xx, ☃xxxx, ☃xxx)).getBlock() != this.block) {
                        return false;
                     }
                  }
               }
            }
         }

         for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
            for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
               for (int ☃xxxxx = 0; ☃xxxxx < 8; ☃xxxxx++) {
                  if (☃[(☃xx * 16 + ☃xxx) * 8 + ☃xxxxx]) {
                     ☃.setBlockState(☃.add(☃xx, ☃xxxxx, ☃xxx), ☃xxxxx >= 4 ? Blocks.AIR.getDefaultState() : this.block.getDefaultState(), 2);
                  }
               }
            }
         }

         for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
            for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
               for (int ☃xxxxxxx = 4; ☃xxxxxxx < 8; ☃xxxxxxx++) {
                  if (☃[(☃xx * 16 + ☃xxx) * 8 + ☃xxxxxxx]) {
                     BlockPos ☃xxxxxxxx = ☃.add(☃xx, ☃xxxxxxx - 1, ☃xxx);
                     if (☃.getBlockState(☃xxxxxxxx).getBlock() == Blocks.DIRT && ☃.getLightFor(EnumSkyBlock.SKY, ☃.add(☃xx, ☃xxxxxxx, ☃xxx)) > 0) {
                        Biome ☃xxxxxxxxx = ☃.getBiome(☃xxxxxxxx);
                        if (☃xxxxxxxxx.topBlock.getBlock() == Blocks.MYCELIUM) {
                           ☃.setBlockState(☃xxxxxxxx, Blocks.MYCELIUM.getDefaultState(), 2);
                        } else {
                           ☃.setBlockState(☃xxxxxxxx, Blocks.GRASS.getDefaultState(), 2);
                        }
                     }
                  }
               }
            }
         }

         if (this.block.getDefaultState().getMaterial() == Material.LAVA) {
            for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
               for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
                  for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 8; ☃xxxxxxxx++) {
                     boolean ☃xxxxxxxxx = !☃[(☃xx * 16 + ☃xxx) * 8 + ☃xxxxxxxx]
                        && (
                           ☃xx < 15 && ☃[((☃xx + 1) * 16 + ☃xxx) * 8 + ☃xxxxxxxx]
                              || ☃xx > 0 && ☃[((☃xx - 1) * 16 + ☃xxx) * 8 + ☃xxxxxxxx]
                              || ☃xxx < 15 && ☃[(☃xx * 16 + ☃xxx + 1) * 8 + ☃xxxxxxxx]
                              || ☃xxx > 0 && ☃[(☃xx * 16 + (☃xxx - 1)) * 8 + ☃xxxxxxxx]
                              || ☃xxxxxxxx < 7 && ☃[(☃xx * 16 + ☃xxx) * 8 + ☃xxxxxxxx + 1]
                              || ☃xxxxxxxx > 0 && ☃[(☃xx * 16 + ☃xxx) * 8 + (☃xxxxxxxx - 1)]
                        );
                     if (☃xxxxxxxxx && (☃xxxxxxxx < 4 || ☃.nextInt(2) != 0) && ☃.getBlockState(☃.add(☃xx, ☃xxxxxxxx, ☃xxx)).getMaterial().isSolid()) {
                        ☃.setBlockState(☃.add(☃xx, ☃xxxxxxxx, ☃xxx), Blocks.STONE.getDefaultState(), 2);
                     }
                  }
               }
            }
         }

         if (this.block.getDefaultState().getMaterial() == Material.WATER) {
            for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
               for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
                  int ☃xxxxxxxxx = 4;
                  if (☃.canBlockFreezeWater(☃.add(☃xx, 4, ☃xxx))) {
                     ☃.setBlockState(☃.add(☃xx, 4, ☃xxx), Blocks.ICE.getDefaultState(), 2);
                  }
               }
            }
         }

         return true;
      }
   }
}
