package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator {
   private final Block block;
   private final int startRadius;

   public WorldGenBlockBlob(Block var1, int var2) {
      super(false);
      this.block = ☃;
      this.startRadius = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      while (☃.getY() > 3) {
         if (!☃.isAirBlock(☃.down())) {
            Block ☃ = ☃.getBlockState(☃.down()).getBlock();
            if (☃ == Blocks.GRASS || ☃ == Blocks.DIRT || ☃ == Blocks.STONE) {
               break;
            }
         }

         ☃ = ☃.down();
      }

      if (☃.getY() <= 3) {
         return false;
      } else {
         int ☃ = this.startRadius;

         for (int ☃x = 0; ☃ >= 0 && ☃x < 3; ☃x++) {
            int ☃xx = ☃ + ☃.nextInt(2);
            int ☃xxx = ☃ + ☃.nextInt(2);
            int ☃xxxx = ☃ + ☃.nextInt(2);
            float ☃xxxxx = (☃xx + ☃xxx + ☃xxxx) * 0.333F + 0.5F;

            for (BlockPos ☃xxxxxx : BlockPos.getAllInBox(☃.add(-☃xx, -☃xxx, -☃xxxx), ☃.add(☃xx, ☃xxx, ☃xxxx))) {
               if (☃xxxxxx.distanceSq(☃) <= ☃xxxxx * ☃xxxxx) {
                  ☃.setBlockState(☃xxxxxx, this.block.getDefaultState(), 4);
               }
            }

            ☃ = ☃.add(-(☃ + 1) + ☃.nextInt(2 + ☃ * 2), 0 - ☃.nextInt(2), -(☃ + 1) + ☃.nextInt(2 + ☃ * 2));
         }

         return true;
      }
   }
}
