package net.minecraft.block;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStandingSign extends BlockSign {
   public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

   public BlockStandingSign() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, 0));
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.getBlockState(☃.down()).getMaterial().isSolid()) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
      }

      super.neighborChanged(☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(ROTATION, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(ROTATION);
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(ROTATION, ☃.rotate(☃.getValue(ROTATION), 16));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withProperty(ROTATION, ☃.mirrorRotation(☃.getValue(ROTATION), 16));
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, ROTATION);
   }
}
