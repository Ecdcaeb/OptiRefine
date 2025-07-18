package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBirchTree extends WorldGenAbstractTree {
   private static final IBlockState LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
   private static final IBlockState LEAF = Blocks.LEAVES
      .getDefaultState()
      .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH)
      .withProperty(BlockOldLeaf.CHECK_DECAY, false);
   private final boolean useExtraRandomHeight;

   public WorldGenBirchTree(boolean var1, boolean var2) {
      super(☃);
      this.useExtraRandomHeight = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = ☃.nextInt(3) + 5;
      if (this.useExtraRandomHeight) {
         ☃ += ☃.nextInt(7);
      }

      boolean ☃x = true;
      if (☃.getY() >= 1 && ☃.getY() + ☃ + 1 <= 256) {
         for (int ☃xx = ☃.getY(); ☃xx <= ☃.getY() + 1 + ☃; ☃xx++) {
            int ☃xxx = 1;
            if (☃xx == ☃.getY()) {
               ☃xxx = 0;
            }

            if (☃xx >= ☃.getY() + 1 + ☃ - 2) {
               ☃xxx = 2;
            }

            BlockPos.MutableBlockPos ☃xxxx = new BlockPos.MutableBlockPos();

            for (int ☃xxxxx = ☃.getX() - ☃xxx; ☃xxxxx <= ☃.getX() + ☃xxx && ☃x; ☃xxxxx++) {
               for (int ☃xxxxxx = ☃.getZ() - ☃xxx; ☃xxxxxx <= ☃.getZ() + ☃xxx && ☃x; ☃xxxxxx++) {
                  if (☃xx < 0 || ☃xx >= 256) {
                     ☃x = false;
                  } else if (!this.canGrowInto(☃.getBlockState(☃xxxx.setPos(☃xxxxx, ☃xx, ☃xxxxxx)).getBlock())) {
                     ☃x = false;
                  }
               }
            }
         }

         if (!☃x) {
            return false;
         } else {
            Block ☃xx = ☃.getBlockState(☃.down()).getBlock();
            if ((☃xx == Blocks.GRASS || ☃xx == Blocks.DIRT || ☃xx == Blocks.FARMLAND) && ☃.getY() < 256 - ☃ - 1) {
               this.setDirtAt(☃, ☃.down());

               for (int ☃xxxx = ☃.getY() - 3 + ☃; ☃xxxx <= ☃.getY() + ☃; ☃xxxx++) {
                  int ☃xxxxx = ☃xxxx - (☃.getY() + ☃);
                  int ☃xxxxxxx = 1 - ☃xxxxx / 2;

                  for (int ☃xxxxxxxx = ☃.getX() - ☃xxxxxxx; ☃xxxxxxxx <= ☃.getX() + ☃xxxxxxx; ☃xxxxxxxx++) {
                     int ☃xxxxxxxxx = ☃xxxxxxxx - ☃.getX();

                     for (int ☃xxxxxxxxxx = ☃.getZ() - ☃xxxxxxx; ☃xxxxxxxxxx <= ☃.getZ() + ☃xxxxxxx; ☃xxxxxxxxxx++) {
                        int ☃xxxxxxxxxxx = ☃xxxxxxxxxx - ☃.getZ();
                        if (Math.abs(☃xxxxxxxxx) != ☃xxxxxxx || Math.abs(☃xxxxxxxxxxx) != ☃xxxxxxx || ☃.nextInt(2) != 0 && ☃xxxxx != 0) {
                           BlockPos ☃xxxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxx, ☃xxxxxxxxxx);
                           Material ☃xxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxx).getMaterial();
                           if (☃xxxxxxxxxxxxx == Material.AIR || ☃xxxxxxxxxxxxx == Material.LEAVES) {
                              this.setBlockAndNotifyAdequately(☃, ☃xxxxxxxxxxxx, LEAF);
                           }
                        }
                     }
                  }
               }

               for (int ☃xxxx = 0; ☃xxxx < ☃; ☃xxxx++) {
                  Material ☃xxxxx = ☃.getBlockState(☃.up(☃xxxx)).getMaterial();
                  if (☃xxxxx == Material.AIR || ☃xxxxx == Material.LEAVES) {
                     this.setBlockAndNotifyAdequately(☃, ☃.up(☃xxxx), LOG);
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }
}
