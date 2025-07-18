package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenIceSpike extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      while (☃.isAirBlock(☃) && ☃.getY() > 2) {
         ☃ = ☃.down();
      }

      if (☃.getBlockState(☃).getBlock() != Blocks.SNOW) {
         return false;
      } else {
         ☃ = ☃.up(☃.nextInt(4));
         int ☃ = ☃.nextInt(4) + 7;
         int ☃x = ☃ / 4 + ☃.nextInt(2);
         if (☃x > 1 && ☃.nextInt(60) == 0) {
            ☃ = ☃.up(10 + ☃.nextInt(30));
         }

         for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
            float ☃xxx = (1.0F - (float)☃xx / ☃) * ☃x;
            int ☃xxxx = MathHelper.ceil(☃xxx);

            for (int ☃xxxxx = -☃xxxx; ☃xxxxx <= ☃xxxx; ☃xxxxx++) {
               float ☃xxxxxx = MathHelper.abs(☃xxxxx) - 0.25F;

               for (int ☃xxxxxxx = -☃xxxx; ☃xxxxxxx <= ☃xxxx; ☃xxxxxxx++) {
                  float ☃xxxxxxxx = MathHelper.abs(☃xxxxxxx) - 0.25F;
                  if ((☃xxxxx == 0 && ☃xxxxxxx == 0 || !(☃xxxxxx * ☃xxxxxx + ☃xxxxxxxx * ☃xxxxxxxx > ☃xxx * ☃xxx))
                     && (☃xxxxx != -☃xxxx && ☃xxxxx != ☃xxxx && ☃xxxxxxx != -☃xxxx && ☃xxxxxxx != ☃xxxx || !(☃.nextFloat() > 0.75F))) {
                     IBlockState ☃xxxxxxxxx = ☃.getBlockState(☃.add(☃xxxxx, ☃xx, ☃xxxxxxx));
                     Block ☃xxxxxxxxxx = ☃xxxxxxxxx.getBlock();
                     if (☃xxxxxxxxx.getMaterial() == Material.AIR || ☃xxxxxxxxxx == Blocks.DIRT || ☃xxxxxxxxxx == Blocks.SNOW || ☃xxxxxxxxxx == Blocks.ICE) {
                        this.setBlockAndNotifyAdequately(☃, ☃.add(☃xxxxx, ☃xx, ☃xxxxxxx), Blocks.PACKED_ICE.getDefaultState());
                     }

                     if (☃xx != 0 && ☃xxxx > 1) {
                        ☃xxxxxxxxx = ☃.getBlockState(☃.add(☃xxxxx, -☃xx, ☃xxxxxxx));
                        ☃xxxxxxxxxx = ☃xxxxxxxxx.getBlock();
                        if (☃xxxxxxxxx.getMaterial() == Material.AIR || ☃xxxxxxxxxx == Blocks.DIRT || ☃xxxxxxxxxx == Blocks.SNOW || ☃xxxxxxxxxx == Blocks.ICE) {
                           this.setBlockAndNotifyAdequately(☃, ☃.add(☃xxxxx, -☃xx, ☃xxxxxxx), Blocks.PACKED_ICE.getDefaultState());
                        }
                     }
                  }
               }
            }
         }

         int ☃xx = ☃x - 1;
         if (☃xx < 0) {
            ☃xx = 0;
         } else if (☃xx > 1) {
            ☃xx = 1;
         }

         for (int ☃xxx = -☃xx; ☃xxx <= ☃xx; ☃xxx++) {
            for (int ☃xxxx = -☃xx; ☃xxxx <= ☃xx; ☃xxxx++) {
               BlockPos ☃xxxxx = ☃.add(☃xxx, -1, ☃xxxx);
               int ☃xxxxxx = 50;
               if (Math.abs(☃xxx) == 1 && Math.abs(☃xxxx) == 1) {
                  ☃xxxxxx = ☃.nextInt(5);
               }

               while (☃xxxxx.getY() > 50) {
                  IBlockState ☃xxxxxxxx = ☃.getBlockState(☃xxxxx);
                  Block ☃xxxxxxxxxxx = ☃xxxxxxxx.getBlock();
                  if (☃xxxxxxxx.getMaterial() != Material.AIR
                     && ☃xxxxxxxxxxx != Blocks.DIRT
                     && ☃xxxxxxxxxxx != Blocks.SNOW
                     && ☃xxxxxxxxxxx != Blocks.ICE
                     && ☃xxxxxxxxxxx != Blocks.PACKED_ICE) {
                     break;
                  }

                  this.setBlockAndNotifyAdequately(☃, ☃xxxxx, Blocks.PACKED_ICE.getDefaultState());
                  ☃xxxxx = ☃xxxxx.down();
                  if (--☃xxxxxx <= 0) {
                     ☃xxxxx = ☃xxxxx.down(☃.nextInt(5) + 1);
                     ☃xxxxxx = ☃.nextInt(5);
                  }
               }
            }
         }

         return true;
      }
   }
}
