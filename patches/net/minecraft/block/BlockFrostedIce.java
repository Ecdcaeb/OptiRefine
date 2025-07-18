package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockFrostedIce extends BlockIce {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

   public BlockFrostedIce() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(AGE);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(AGE, MathHelper.clamp(☃, 0, 3));
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if ((☃.nextInt(3) == 0 || this.countNeighbors(☃, ☃) < 4) && ☃.getLightFromNeighbors(☃) > 11 - ☃.getValue(AGE) - ☃.getLightOpacity()) {
         this.slightlyMelt(☃, ☃, ☃, ☃, true);
      } else {
         ☃.scheduleUpdate(☃, this, MathHelper.getInt(☃, 20, 40));
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (☃ == this) {
         int ☃ = this.countNeighbors(☃, ☃);
         if (☃ < 2) {
            this.turnIntoWater(☃, ☃);
         }
      }
   }

   private int countNeighbors(World var1, BlockPos var2) {
      int ☃ = 0;

      for (EnumFacing ☃x : EnumFacing.values()) {
         if (☃.getBlockState(☃.offset(☃x)).getBlock() == this) {
            if (++☃ >= 4) {
               return ☃;
            }
         }
      }

      return ☃;
   }

   protected void slightlyMelt(World var1, BlockPos var2, IBlockState var3, Random var4, boolean var5) {
      int ☃ = ☃.getValue(AGE);
      if (☃ < 3) {
         ☃.setBlockState(☃, ☃.withProperty(AGE, ☃ + 1), 2);
         ☃.scheduleUpdate(☃, this, MathHelper.getInt(☃, 20, 40));
      } else {
         this.turnIntoWater(☃, ☃);
         if (☃) {
            for (EnumFacing ☃x : EnumFacing.values()) {
               BlockPos ☃xx = ☃.offset(☃x);
               IBlockState ☃xxx = ☃.getBlockState(☃xx);
               if (☃xxx.getBlock() == this) {
                  this.slightlyMelt(☃, ☃xx, ☃xxx, ☃, false);
               }
            }
         }
      }
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, AGE);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return ItemStack.EMPTY;
   }
}
