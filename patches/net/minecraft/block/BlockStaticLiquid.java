package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStaticLiquid extends BlockLiquid {
   protected BlockStaticLiquid(Material var1) {
      super(☃);
      this.setTickRandomly(false);
      if (☃ == Material.LAVA) {
         this.setTickRandomly(true);
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!this.checkForMixing(☃, ☃, ☃)) {
         this.updateLiquid(☃, ☃, ☃);
      }
   }

   private void updateLiquid(World var1, BlockPos var2, IBlockState var3) {
      BlockDynamicLiquid ☃ = getFlowingBlock(this.material);
      ☃.setBlockState(☃, ☃.getDefaultState().withProperty(LEVEL, ☃.getValue(LEVEL)), 2);
      ☃.scheduleUpdate(☃, ☃, this.tickRate(☃));
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (this.material == Material.LAVA) {
         if (☃.getGameRules().getBoolean("doFireTick")) {
            int ☃ = ☃.nextInt(3);
            if (☃ > 0) {
               BlockPos ☃x = ☃;

               for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
                  ☃x = ☃x.add(☃.nextInt(3) - 1, 1, ☃.nextInt(3) - 1);
                  if (☃x.getY() >= 0 && ☃x.getY() < 256 && !☃.isBlockLoaded(☃x)) {
                     return;
                  }

                  Block ☃xxx = ☃.getBlockState(☃x).getBlock();
                  if (☃xxx.material == Material.AIR) {
                     if (this.isSurroundingBlockFlammable(☃, ☃x)) {
                        ☃.setBlockState(☃x, Blocks.FIRE.getDefaultState());
                        return;
                     }
                  } else if (☃xxx.material.blocksMovement()) {
                     return;
                  }
               }
            } else {
               for (int ☃x = 0; ☃x < 3; ☃x++) {
                  BlockPos ☃xx = ☃.add(☃.nextInt(3) - 1, 0, ☃.nextInt(3) - 1);
                  if (☃xx.getY() >= 0 && ☃xx.getY() < 256 && !☃.isBlockLoaded(☃xx)) {
                     return;
                  }

                  if (☃.isAirBlock(☃xx.up()) && this.getCanBlockBurn(☃, ☃xx)) {
                     ☃.setBlockState(☃xx.up(), Blocks.FIRE.getDefaultState());
                  }
               }
            }
         }
      }
   }

   protected boolean isSurroundingBlockFlammable(World var1, BlockPos var2) {
      for (EnumFacing ☃ : EnumFacing.values()) {
         if (this.getCanBlockBurn(☃, ☃.offset(☃))) {
            return true;
         }
      }

      return false;
   }

   private boolean getCanBlockBurn(World var1, BlockPos var2) {
      return ☃.getY() >= 0 && ☃.getY() < 256 && !☃.isBlockLoaded(☃) ? false : ☃.getBlockState(☃).getMaterial().getCanBurn();
   }
}
