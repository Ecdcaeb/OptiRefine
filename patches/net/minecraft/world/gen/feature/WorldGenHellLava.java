package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenHellLava extends WorldGenerator {
   private final Block block;
   private final boolean insideRock;

   public WorldGenHellLava(Block var1, boolean var2) {
      this.block = ☃;
      this.insideRock = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (☃.getBlockState(☃.up()).getBlock() != Blocks.NETHERRACK) {
         return false;
      } else if (☃.getBlockState(☃).getMaterial() != Material.AIR && ☃.getBlockState(☃).getBlock() != Blocks.NETHERRACK) {
         return false;
      } else {
         int ☃ = 0;
         if (☃.getBlockState(☃.west()).getBlock() == Blocks.NETHERRACK) {
            ☃++;
         }

         if (☃.getBlockState(☃.east()).getBlock() == Blocks.NETHERRACK) {
            ☃++;
         }

         if (☃.getBlockState(☃.north()).getBlock() == Blocks.NETHERRACK) {
            ☃++;
         }

         if (☃.getBlockState(☃.south()).getBlock() == Blocks.NETHERRACK) {
            ☃++;
         }

         if (☃.getBlockState(☃.down()).getBlock() == Blocks.NETHERRACK) {
            ☃++;
         }

         int ☃x = 0;
         if (☃.isAirBlock(☃.west())) {
            ☃x++;
         }

         if (☃.isAirBlock(☃.east())) {
            ☃x++;
         }

         if (☃.isAirBlock(☃.north())) {
            ☃x++;
         }

         if (☃.isAirBlock(☃.south())) {
            ☃x++;
         }

         if (☃.isAirBlock(☃.down())) {
            ☃x++;
         }

         if (!this.insideRock && ☃ == 4 && ☃x == 1 || ☃ == 5) {
            IBlockState ☃xx = this.block.getDefaultState();
            ☃.setBlockState(☃, ☃xx, 2);
            ☃.immediateBlockTick(☃, ☃xx, ☃);
         }

         return true;
      }
   }
}
