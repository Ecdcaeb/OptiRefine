package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenEndIsland extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      float ☃ = ☃.nextInt(3) + 4;

      for (int ☃x = 0; ☃ > 0.5F; ☃x--) {
         for (int ☃xx = MathHelper.floor(-☃); ☃xx <= MathHelper.ceil(☃); ☃xx++) {
            for (int ☃xxx = MathHelper.floor(-☃); ☃xxx <= MathHelper.ceil(☃); ☃xxx++) {
               if (☃xx * ☃xx + ☃xxx * ☃xxx <= (☃ + 1.0F) * (☃ + 1.0F)) {
                  this.setBlockAndNotifyAdequately(☃, ☃.add(☃xx, ☃x, ☃xxx), Blocks.END_STONE.getDefaultState());
               }
            }
         }

         ☃ = (float)(☃ - (☃.nextInt(2) + 0.5));
      }

      return true;
   }
}
