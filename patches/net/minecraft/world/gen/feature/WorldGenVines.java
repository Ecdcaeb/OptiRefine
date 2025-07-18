package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenVines extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      while (☃.getY() < 128) {
         if (☃.isAirBlock(☃)) {
            for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL.facings()) {
               if (Blocks.VINE.canPlaceBlockOnSide(☃, ☃, ☃)) {
                  IBlockState ☃x = Blocks.VINE
                     .getDefaultState()
                     .withProperty(BlockVine.NORTH, ☃ == EnumFacing.NORTH)
                     .withProperty(BlockVine.EAST, ☃ == EnumFacing.EAST)
                     .withProperty(BlockVine.SOUTH, ☃ == EnumFacing.SOUTH)
                     .withProperty(BlockVine.WEST, ☃ == EnumFacing.WEST);
                  ☃.setBlockState(☃, ☃x, 2);
                  break;
               }
            }
         } else {
            ☃ = ☃.add(☃.nextInt(4) - ☃.nextInt(4), 0, ☃.nextInt(4) - ☃.nextInt(4));
         }

         ☃ = ☃.up();
      }

      return true;
   }
}
