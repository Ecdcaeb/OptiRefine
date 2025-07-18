package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGravel extends BlockFalling {
   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      if (☃ > 3) {
         ☃ = 3;
      }

      return ☃.nextInt(10 - ☃ * 3) == 0 ? Items.FLINT : super.getItemDropped(☃, ☃, ☃);
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.STONE;
   }

   @Override
   public int getDustColor(IBlockState var1) {
      return -8356741;
   }
}
