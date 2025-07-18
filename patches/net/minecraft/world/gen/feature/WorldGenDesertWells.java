package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;
import java.util.Random;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator {
   private static final BlockStateMatcher IS_SAND = BlockStateMatcher.forBlock(Blocks.SAND)
      .where(BlockSand.VARIANT, Predicates.equalTo(BlockSand.EnumType.SAND));
   private final IBlockState sandSlab = Blocks.STONE_SLAB
      .getDefaultState()
      .withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND)
      .withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
   private final IBlockState sandstone = Blocks.SANDSTONE.getDefaultState();
   private final IBlockState water = Blocks.FLOWING_WATER.getDefaultState();

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      while (☃.isAirBlock(☃) && ☃.getY() > 2) {
         ☃ = ☃.down();
      }

      if (!IS_SAND.apply(☃.getBlockState(☃))) {
         return false;
      } else {
         for (int ☃ = -2; ☃ <= 2; ☃++) {
            for (int ☃x = -2; ☃x <= 2; ☃x++) {
               if (☃.isAirBlock(☃.add(☃, -1, ☃x)) && ☃.isAirBlock(☃.add(☃, -2, ☃x))) {
                  return false;
               }
            }
         }

         for (int ☃ = -1; ☃ <= 0; ☃++) {
            for (int ☃xx = -2; ☃xx <= 2; ☃xx++) {
               for (int ☃xxx = -2; ☃xxx <= 2; ☃xxx++) {
                  ☃.setBlockState(☃.add(☃xx, ☃, ☃xxx), this.sandstone, 2);
               }
            }
         }

         ☃.setBlockState(☃, this.water, 2);

         for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
            ☃.setBlockState(☃.offset(☃), this.water, 2);
         }

         for (int ☃ = -2; ☃ <= 2; ☃++) {
            for (int ☃xx = -2; ☃xx <= 2; ☃xx++) {
               if (☃ == -2 || ☃ == 2 || ☃xx == -2 || ☃xx == 2) {
                  ☃.setBlockState(☃.add(☃, 1, ☃xx), this.sandstone, 2);
               }
            }
         }

         ☃.setBlockState(☃.add(2, 1, 0), this.sandSlab, 2);
         ☃.setBlockState(☃.add(-2, 1, 0), this.sandSlab, 2);
         ☃.setBlockState(☃.add(0, 1, 2), this.sandSlab, 2);
         ☃.setBlockState(☃.add(0, 1, -2), this.sandSlab, 2);

         for (int ☃ = -1; ☃ <= 1; ☃++) {
            for (int ☃xxx = -1; ☃xxx <= 1; ☃xxx++) {
               if (☃ == 0 && ☃xxx == 0) {
                  ☃.setBlockState(☃.add(☃, 4, ☃xxx), this.sandstone, 2);
               } else {
                  ☃.setBlockState(☃.add(☃, 4, ☃xxx), this.sandSlab, 2);
               }
            }
         }

         for (int ☃ = 1; ☃ <= 3; ☃++) {
            ☃.setBlockState(☃.add(-1, ☃, -1), this.sandstone, 2);
            ☃.setBlockState(☃.add(-1, ☃, 1), this.sandstone, 2);
            ☃.setBlockState(☃.add(1, ☃, -1), this.sandstone, 2);
            ☃.setBlockState(☃.add(1, ☃, 1), this.sandstone, 2);
         }

         return true;
      }
   }
}
