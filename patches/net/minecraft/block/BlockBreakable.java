package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBreakable extends Block {
   private final boolean ignoreSimilarity;

   protected BlockBreakable(Material var1, boolean var2) {
      this(☃, ☃, ☃.getMaterialMapColor());
   }

   protected BlockBreakable(Material var1, boolean var2, MapColor var3) {
      super(☃, ☃);
      this.ignoreSimilarity = ☃;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      IBlockState ☃ = ☃.getBlockState(☃.offset(☃));
      Block ☃x = ☃.getBlock();
      if (this == Blocks.GLASS || this == Blocks.STAINED_GLASS) {
         if (☃ != ☃) {
            return true;
         }

         if (☃x == this) {
            return false;
         }
      }

      return !this.ignoreSimilarity && ☃x == this ? false : super.shouldSideBeRendered(☃, ☃, ☃, ☃);
   }
}
