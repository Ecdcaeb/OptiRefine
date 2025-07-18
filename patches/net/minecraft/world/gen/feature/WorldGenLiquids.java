package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenLiquids extends WorldGenerator {
   private final Block block;

   public WorldGenLiquids(Block var1) {
      this.block = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (☃.getBlockState(☃.up()).getBlock() != Blocks.STONE) {
         return false;
      } else if (☃.getBlockState(☃.down()).getBlock() != Blocks.STONE) {
         return false;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃);
         if (☃.getMaterial() != Material.AIR && ☃.getBlock() != Blocks.STONE) {
            return false;
         } else {
            int ☃x = 0;
            if (☃.getBlockState(☃.west()).getBlock() == Blocks.STONE) {
               ☃x++;
            }

            if (☃.getBlockState(☃.east()).getBlock() == Blocks.STONE) {
               ☃x++;
            }

            if (☃.getBlockState(☃.north()).getBlock() == Blocks.STONE) {
               ☃x++;
            }

            if (☃.getBlockState(☃.south()).getBlock() == Blocks.STONE) {
               ☃x++;
            }

            int ☃xx = 0;
            if (☃.isAirBlock(☃.west())) {
               ☃xx++;
            }

            if (☃.isAirBlock(☃.east())) {
               ☃xx++;
            }

            if (☃.isAirBlock(☃.north())) {
               ☃xx++;
            }

            if (☃.isAirBlock(☃.south())) {
               ☃xx++;
            }

            if (☃x == 3 && ☃xx == 1) {
               IBlockState ☃xxx = this.block.getDefaultState();
               ☃.setBlockState(☃, ☃xxx, 2);
               ☃.immediateBlockTick(☃, ☃xxx, ☃);
            }

            return true;
         }
      }
   }
}
