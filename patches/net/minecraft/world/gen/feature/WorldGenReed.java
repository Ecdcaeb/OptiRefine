package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenReed extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (int ☃ = 0; ☃ < 20; ☃++) {
         BlockPos ☃x = ☃.add(☃.nextInt(4) - ☃.nextInt(4), 0, ☃.nextInt(4) - ☃.nextInt(4));
         if (☃.isAirBlock(☃x)) {
            BlockPos ☃xx = ☃x.down();
            if (☃.getBlockState(☃xx.west()).getMaterial() == Material.WATER
               || ☃.getBlockState(☃xx.east()).getMaterial() == Material.WATER
               || ☃.getBlockState(☃xx.north()).getMaterial() == Material.WATER
               || ☃.getBlockState(☃xx.south()).getMaterial() == Material.WATER) {
               int ☃xxx = 2 + ☃.nextInt(☃.nextInt(3) + 1);

               for (int ☃xxxx = 0; ☃xxxx < ☃xxx; ☃xxxx++) {
                  if (Blocks.REEDS.canBlockStay(☃, ☃x)) {
                     ☃.setBlockState(☃x.up(☃xxxx), Blocks.REEDS.getDefaultState(), 2);
                  }
               }
            }
         }
      }

      return true;
   }
}
