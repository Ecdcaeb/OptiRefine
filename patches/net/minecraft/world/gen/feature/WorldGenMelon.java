package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenMelon extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (int ☃ = 0; ☃ < 64; ☃++) {
         BlockPos ☃x = ☃.add(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
         if (Blocks.MELON_BLOCK.canPlaceBlockAt(☃, ☃x) && ☃.getBlockState(☃x.down()).getBlock() == Blocks.GRASS) {
            ☃.setBlockState(☃x, Blocks.MELON_BLOCK.getDefaultState(), 2);
         }
      }

      return true;
   }
}
