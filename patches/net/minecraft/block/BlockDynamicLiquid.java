package net.minecraft.block;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDynamicLiquid extends BlockLiquid {
   int adjacentSourceBlocks;

   protected BlockDynamicLiquid(Material var1) {
      super(☃);
   }

   private void placeStaticBlock(World var1, BlockPos var2, IBlockState var3) {
      ☃.setBlockState(☃, getStaticBlock(this.material).getDefaultState().withProperty(LEVEL, ☃.getValue(LEVEL)), 2);
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      int ☃ = ☃.getValue(LEVEL);
      int ☃x = 1;
      if (this.material == Material.LAVA && !☃.provider.doesWaterVaporize()) {
         ☃x = 2;
      }

      int ☃xx = this.tickRate(☃);
      if (☃ > 0) {
         int ☃xxx = -100;
         this.adjacentSourceBlocks = 0;

         for (EnumFacing ☃xxxx : EnumFacing.Plane.HORIZONTAL) {
            ☃xxx = this.checkAdjacentBlock(☃, ☃.offset(☃xxxx), ☃xxx);
         }

         int ☃xxxx = ☃xxx + ☃x;
         if (☃xxxx >= 8 || ☃xxx < 0) {
            ☃xxxx = -1;
         }

         int ☃xxxxx = this.getDepth(☃.getBlockState(☃.up()));
         if (☃xxxxx >= 0) {
            if (☃xxxxx >= 8) {
               ☃xxxx = ☃xxxxx;
            } else {
               ☃xxxx = ☃xxxxx + 8;
            }
         }

         if (this.adjacentSourceBlocks >= 2 && this.material == Material.WATER) {
            IBlockState ☃xxxxxx = ☃.getBlockState(☃.down());
            if (☃xxxxxx.getMaterial().isSolid()) {
               ☃xxxx = 0;
            } else if (☃xxxxxx.getMaterial() == this.material && ☃xxxxxx.getValue(LEVEL) == 0) {
               ☃xxxx = 0;
            }
         }

         if (this.material == Material.LAVA && ☃ < 8 && ☃xxxx < 8 && ☃xxxx > ☃ && ☃.nextInt(4) != 0) {
            ☃xx *= 4;
         }

         if (☃xxxx == ☃) {
            this.placeStaticBlock(☃, ☃, ☃);
         } else {
            ☃ = ☃xxxx;
            if (☃xxxx < 0) {
               ☃.setBlockToAir(☃);
            } else {
               ☃ = ☃.withProperty(LEVEL, ☃xxxx);
               ☃.setBlockState(☃, ☃, 2);
               ☃.scheduleUpdate(☃, this, ☃xx);
               ☃.notifyNeighborsOfStateChange(☃, this, false);
            }
         }
      } else {
         this.placeStaticBlock(☃, ☃, ☃);
      }

      IBlockState ☃xxx = ☃.getBlockState(☃.down());
      if (this.canFlowInto(☃, ☃.down(), ☃xxx)) {
         if (this.material == Material.LAVA && ☃.getBlockState(☃.down()).getMaterial() == Material.WATER) {
            ☃.setBlockState(☃.down(), Blocks.STONE.getDefaultState());
            this.triggerMixEffects(☃, ☃.down());
            return;
         }

         if (☃ >= 8) {
            this.tryFlowInto(☃, ☃.down(), ☃xxx, ☃);
         } else {
            this.tryFlowInto(☃, ☃.down(), ☃xxx, ☃ + 8);
         }
      } else if (☃ >= 0 && (☃ == 0 || this.isBlocked(☃, ☃.down(), ☃xxx))) {
         Set<EnumFacing> ☃xxxxxx = this.getPossibleFlowDirections(☃, ☃);
         int ☃xxxxxxx = ☃ + ☃x;
         if (☃ >= 8) {
            ☃xxxxxxx = 1;
         }

         if (☃xxxxxxx >= 8) {
            return;
         }

         for (EnumFacing ☃xxxxxxxx : ☃xxxxxx) {
            this.tryFlowInto(☃, ☃.offset(☃xxxxxxxx), ☃.getBlockState(☃.offset(☃xxxxxxxx)), ☃xxxxxxx);
         }
      }
   }

   private void tryFlowInto(World var1, BlockPos var2, IBlockState var3, int var4) {
      if (this.canFlowInto(☃, ☃, ☃)) {
         if (☃.getMaterial() != Material.AIR) {
            if (this.material == Material.LAVA) {
               this.triggerMixEffects(☃, ☃);
            } else {
               ☃.getBlock().dropBlockAsItem(☃, ☃, ☃, 0);
            }
         }

         ☃.setBlockState(☃, this.getDefaultState().withProperty(LEVEL, ☃), 3);
      }
   }

   private int getSlopeDistance(World var1, BlockPos var2, int var3, EnumFacing var4) {
      int ☃ = 1000;

      for (EnumFacing ☃x : EnumFacing.Plane.HORIZONTAL) {
         if (☃x != ☃) {
            BlockPos ☃xx = ☃.offset(☃x);
            IBlockState ☃xxx = ☃.getBlockState(☃xx);
            if (!this.isBlocked(☃, ☃xx, ☃xxx) && (☃xxx.getMaterial() != this.material || ☃xxx.getValue(LEVEL) > 0)) {
               if (!this.isBlocked(☃, ☃xx.down(), ☃xxx)) {
                  return ☃;
               }

               if (☃ < this.getSlopeFindDistance(☃)) {
                  int ☃xxxx = this.getSlopeDistance(☃, ☃xx, ☃ + 1, ☃x.getOpposite());
                  if (☃xxxx < ☃) {
                     ☃ = ☃xxxx;
                  }
               }
            }
         }
      }

      return ☃;
   }

   private int getSlopeFindDistance(World var1) {
      return this.material == Material.LAVA && !☃.provider.doesWaterVaporize() ? 2 : 4;
   }

   private Set<EnumFacing> getPossibleFlowDirections(World var1, BlockPos var2) {
      int ☃ = 1000;
      Set<EnumFacing> ☃x = EnumSet.noneOf(EnumFacing.class);

      for (EnumFacing ☃xx : EnumFacing.Plane.HORIZONTAL) {
         BlockPos ☃xxx = ☃.offset(☃xx);
         IBlockState ☃xxxx = ☃.getBlockState(☃xxx);
         if (!this.isBlocked(☃, ☃xxx, ☃xxxx) && (☃xxxx.getMaterial() != this.material || ☃xxxx.getValue(LEVEL) > 0)) {
            int ☃xxxxx;
            if (this.isBlocked(☃, ☃xxx.down(), ☃.getBlockState(☃xxx.down()))) {
               ☃xxxxx = this.getSlopeDistance(☃, ☃xxx, 1, ☃xx.getOpposite());
            } else {
               ☃xxxxx = 0;
            }

            if (☃xxxxx < ☃) {
               ☃x.clear();
            }

            if (☃xxxxx <= ☃) {
               ☃x.add(☃xx);
               ☃ = ☃xxxxx;
            }
         }
      }

      return ☃x;
   }

   private boolean isBlocked(World var1, BlockPos var2, IBlockState var3) {
      Block ☃ = ☃.getBlockState(☃).getBlock();
      if (☃ instanceof BlockDoor || ☃ == Blocks.STANDING_SIGN || ☃ == Blocks.LADDER || ☃ == Blocks.REEDS) {
         return true;
      } else {
         return ☃.material != Material.PORTAL && ☃.material != Material.STRUCTURE_VOID ? ☃.material.blocksMovement() : true;
      }
   }

   protected int checkAdjacentBlock(World var1, BlockPos var2, int var3) {
      int ☃ = this.getDepth(☃.getBlockState(☃));
      if (☃ < 0) {
         return ☃;
      } else {
         if (☃ == 0) {
            this.adjacentSourceBlocks++;
         }

         if (☃ >= 8) {
            ☃ = 0;
         }

         return ☃ >= 0 && ☃ >= ☃ ? ☃ : ☃;
      }
   }

   private boolean canFlowInto(World var1, BlockPos var2, IBlockState var3) {
      Material ☃ = ☃.getMaterial();
      return ☃ != this.material && ☃ != Material.LAVA && !this.isBlocked(☃, ☃, ☃);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!this.checkForMixing(☃, ☃, ☃)) {
         ☃.scheduleUpdate(☃, this, this.tickRate(☃));
      }
   }
}
