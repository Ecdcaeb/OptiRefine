package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenGlowStone1 extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (!☃.isAirBlock(☃)) {
         return false;
      } else if (☃.getBlockState(☃.up()).getBlock() != Blocks.NETHERRACK) {
         return false;
      } else {
         ☃.setBlockState(☃, Blocks.GLOWSTONE.getDefaultState(), 2);

         for (int ☃ = 0; ☃ < 1500; ☃++) {
            BlockPos ☃x = ☃.add(☃.nextInt(8) - ☃.nextInt(8), -☃.nextInt(12), ☃.nextInt(8) - ☃.nextInt(8));
            if (☃.getBlockState(☃x).getMaterial() == Material.AIR) {
               int ☃xx = 0;

               for (EnumFacing ☃xxx : EnumFacing.values()) {
                  if (☃.getBlockState(☃x.offset(☃xxx)).getBlock() == Blocks.GLOWSTONE) {
                     ☃xx++;
                  }

                  if (☃xx > 1) {
                     break;
                  }
               }

               if (☃xx == 1) {
                  ☃.setBlockState(☃x, Blocks.GLOWSTONE.getDefaultState(), 2);
               }
            }
         }

         return true;
      }
   }
}
