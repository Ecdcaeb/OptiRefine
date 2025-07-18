package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenHugeTrees extends WorldGenAbstractTree {
   protected final int baseHeight;
   protected final IBlockState woodMetadata;
   protected final IBlockState leavesMetadata;
   protected int extraRandomHeight;

   public WorldGenHugeTrees(boolean var1, int var2, int var3, IBlockState var4, IBlockState var5) {
      super(☃);
      this.baseHeight = ☃;
      this.extraRandomHeight = ☃;
      this.woodMetadata = ☃;
      this.leavesMetadata = ☃;
   }

   protected int getHeight(Random var1) {
      int ☃ = ☃.nextInt(3) + this.baseHeight;
      if (this.extraRandomHeight > 1) {
         ☃ += ☃.nextInt(this.extraRandomHeight);
      }

      return ☃;
   }

   private boolean isSpaceAt(World var1, BlockPos var2, int var3) {
      boolean ☃ = true;
      if (☃.getY() >= 1 && ☃.getY() + ☃ + 1 <= 256) {
         for (int ☃x = 0; ☃x <= 1 + ☃; ☃x++) {
            int ☃xx = 2;
            if (☃x == 0) {
               ☃xx = 1;
            } else if (☃x >= 1 + ☃ - 2) {
               ☃xx = 2;
            }

            for (int ☃xxx = -☃xx; ☃xxx <= ☃xx && ☃; ☃xxx++) {
               for (int ☃xxxx = -☃xx; ☃xxxx <= ☃xx && ☃; ☃xxxx++) {
                  if (☃.getY() + ☃x < 0 || ☃.getY() + ☃x >= 256 || !this.canGrowInto(☃.getBlockState(☃.add(☃xxx, ☃x, ☃xxxx)).getBlock())) {
                     ☃ = false;
                  }
               }
            }
         }

         return ☃;
      } else {
         return false;
      }
   }

   private boolean ensureDirtsUnderneath(BlockPos var1, World var2) {
      BlockPos ☃ = ☃.down();
      Block ☃x = ☃.getBlockState(☃).getBlock();
      if ((☃x == Blocks.GRASS || ☃x == Blocks.DIRT) && ☃.getY() >= 2) {
         this.setDirtAt(☃, ☃);
         this.setDirtAt(☃, ☃.east());
         this.setDirtAt(☃, ☃.south());
         this.setDirtAt(☃, ☃.south().east());
         return true;
      } else {
         return false;
      }
   }

   protected boolean ensureGrowable(World var1, Random var2, BlockPos var3, int var4) {
      return this.isSpaceAt(☃, ☃, ☃) && this.ensureDirtsUnderneath(☃, ☃);
   }

   protected void growLeavesLayerStrict(World var1, BlockPos var2, int var3) {
      int ☃ = ☃ * ☃;

      for (int ☃x = -☃; ☃x <= ☃ + 1; ☃x++) {
         for (int ☃xx = -☃; ☃xx <= ☃ + 1; ☃xx++) {
            int ☃xxx = ☃x - 1;
            int ☃xxxx = ☃xx - 1;
            if (☃x * ☃x + ☃xx * ☃xx <= ☃ || ☃xxx * ☃xxx + ☃xxxx * ☃xxxx <= ☃ || ☃x * ☃x + ☃xxxx * ☃xxxx <= ☃ || ☃xxx * ☃xxx + ☃xx * ☃xx <= ☃) {
               BlockPos ☃xxxxx = ☃.add(☃x, 0, ☃xx);
               Material ☃xxxxxx = ☃.getBlockState(☃xxxxx).getMaterial();
               if (☃xxxxxx == Material.AIR || ☃xxxxxx == Material.LEAVES) {
                  this.setBlockAndNotifyAdequately(☃, ☃xxxxx, this.leavesMetadata);
               }
            }
         }
      }
   }

   protected void growLeavesLayer(World var1, BlockPos var2, int var3) {
      int ☃ = ☃ * ☃;

      for (int ☃x = -☃; ☃x <= ☃; ☃x++) {
         for (int ☃xx = -☃; ☃xx <= ☃; ☃xx++) {
            if (☃x * ☃x + ☃xx * ☃xx <= ☃) {
               BlockPos ☃xxx = ☃.add(☃x, 0, ☃xx);
               Material ☃xxxx = ☃.getBlockState(☃xxx).getMaterial();
               if (☃xxxx == Material.AIR || ☃xxxx == Material.LEAVES) {
                  this.setBlockAndNotifyAdequately(☃, ☃xxx, this.leavesMetadata);
               }
            }
         }
      }
   }
}
