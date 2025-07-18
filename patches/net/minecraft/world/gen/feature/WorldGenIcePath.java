package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenIcePath extends WorldGenerator {
   private final Block block = Blocks.PACKED_ICE;
   private final int basePathWidth;

   public WorldGenIcePath(int var1) {
      this.basePathWidth = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      while (☃.isAirBlock(☃) && ☃.getY() > 2) {
         ☃ = ☃.down();
      }

      if (☃.getBlockState(☃).getBlock() != Blocks.SNOW) {
         return false;
      } else {
         int ☃ = ☃.nextInt(this.basePathWidth - 2) + 2;
         int ☃x = 1;

         for (int ☃xx = ☃.getX() - ☃; ☃xx <= ☃.getX() + ☃; ☃xx++) {
            for (int ☃xxx = ☃.getZ() - ☃; ☃xxx <= ☃.getZ() + ☃; ☃xxx++) {
               int ☃xxxx = ☃xx - ☃.getX();
               int ☃xxxxx = ☃xxx - ☃.getZ();
               if (☃xxxx * ☃xxxx + ☃xxxxx * ☃xxxxx <= ☃ * ☃) {
                  for (int ☃xxxxxx = ☃.getY() - 1; ☃xxxxxx <= ☃.getY() + 1; ☃xxxxxx++) {
                     BlockPos ☃xxxxxxx = new BlockPos(☃xx, ☃xxxxxx, ☃xxx);
                     Block ☃xxxxxxxx = ☃.getBlockState(☃xxxxxxx).getBlock();
                     if (☃xxxxxxxx == Blocks.DIRT || ☃xxxxxxxx == Blocks.SNOW || ☃xxxxxxxx == Blocks.ICE) {
                        ☃.setBlockState(☃xxxxxxx, this.block.getDefaultState(), 2);
                     }
                  }
               }
            }
         }

         return true;
      }
   }
}
