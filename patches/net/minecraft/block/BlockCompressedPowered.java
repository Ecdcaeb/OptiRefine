package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCompressedPowered extends Block {
   public BlockCompressedPowered(Material var1, MapColor var2) {
      super(☃, ☃);
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return 15;
   }
}
