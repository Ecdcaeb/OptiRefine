package net.minecraft.dispenser;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IBlockSource extends ILocatableSource {
   @Override
   double getX();

   @Override
   double getY();

   @Override
   double getZ();

   BlockPos getBlockPos();

   IBlockState getBlockState();

   <T extends TileEntity> T getBlockTileEntity();
}
