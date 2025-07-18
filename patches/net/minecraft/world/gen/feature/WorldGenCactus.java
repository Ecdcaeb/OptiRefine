package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (int ☃ = 0; ☃ < 10; ☃++) {
         BlockPos ☃x = ☃.add(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
         if (☃.isAirBlock(☃x)) {
            int ☃xx = 1 + ☃.nextInt(☃.nextInt(3) + 1);

            for (int ☃xxx = 0; ☃xxx < ☃xx; ☃xxx++) {
               if (Blocks.CACTUS.canBlockStay(☃, ☃x)) {
                  ☃.setBlockState(☃x.up(☃xxx), Blocks.CACTUS.getDefaultState(), 2);
               }
            }
         }
      }

      return true;
   }
}
