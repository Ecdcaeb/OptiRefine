package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenWaterlily extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (int ☃ = 0; ☃ < 10; ☃++) {
         int ☃x = ☃.getX() + ☃.nextInt(8) - ☃.nextInt(8);
         int ☃xx = ☃.getY() + ☃.nextInt(4) - ☃.nextInt(4);
         int ☃xxx = ☃.getZ() + ☃.nextInt(8) - ☃.nextInt(8);
         if (☃.isAirBlock(new BlockPos(☃x, ☃xx, ☃xxx)) && Blocks.WATERLILY.canPlaceBlockAt(☃, new BlockPos(☃x, ☃xx, ☃xxx))) {
            ☃.setBlockState(new BlockPos(☃x, ☃xx, ☃xxx), Blocks.WATERLILY.getDefaultState(), 2);
         }
      }

      return true;
   }
}
