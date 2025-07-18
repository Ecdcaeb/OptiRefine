package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBush extends WorldGenerator {
   private final BlockBush block;

   public WorldGenBush(BlockBush var1) {
      this.block = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (int ☃ = 0; ☃ < 64; ☃++) {
         BlockPos ☃x = ☃.add(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
         if (☃.isAirBlock(☃x) && (!☃.provider.isNether() || ☃x.getY() < 255) && this.block.canBlockStay(☃, ☃x, this.block.getDefaultState())) {
            ☃.setBlockState(☃x, this.block.getDefaultState(), 2);
         }
      }

      return true;
   }
}
