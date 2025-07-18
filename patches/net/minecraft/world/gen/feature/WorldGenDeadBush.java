package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDeadBush extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (IBlockState ☃ = ☃.getBlockState(☃); (☃.getMaterial() == Material.AIR || ☃.getMaterial() == Material.LEAVES) && ☃.getY() > 0; ☃ = ☃.getBlockState(☃)) {
         ☃ = ☃.down();
      }

      for (int ☃ = 0; ☃ < 4; ☃++) {
         BlockPos ☃x = ☃.add(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
         if (☃.isAirBlock(☃x) && Blocks.DEADBUSH.canBlockStay(☃, ☃x, Blocks.DEADBUSH.getDefaultState())) {
            ☃.setBlockState(☃x, Blocks.DEADBUSH.getDefaultState(), 2);
         }
      }

      return true;
   }
}
