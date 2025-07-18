package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga1 extends WorldGenAbstractTree {
   private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
   private static final IBlockState LEAF = Blocks.LEAVES
      .getDefaultState()
      .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)
      .withProperty(BlockLeaves.CHECK_DECAY, false);

   public WorldGenTaiga1() {
      super(false);
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = ☃.nextInt(5) + 7;
      int ☃x = ☃ - ☃.nextInt(2) - 3;
      int ☃xx = ☃ - ☃x;
      int ☃xxx = 1 + ☃.nextInt(☃xx + 1);
      if (☃.getY() >= 1 && ☃.getY() + ☃ + 1 <= 256) {
         boolean ☃xxxx = true;

         for (int ☃xxxxx = ☃.getY(); ☃xxxxx <= ☃.getY() + 1 + ☃ && ☃xxxx; ☃xxxxx++) {
            int ☃xxxxxx = 1;
            if (☃xxxxx - ☃.getY() < ☃x) {
               ☃xxxxxx = 0;
            } else {
               ☃xxxxxx = ☃xxx;
            }

            BlockPos.MutableBlockPos ☃xxxxxxx = new BlockPos.MutableBlockPos();

            for (int ☃xxxxxxxx = ☃.getX() - ☃xxxxxx; ☃xxxxxxxx <= ☃.getX() + ☃xxxxxx && ☃xxxx; ☃xxxxxxxx++) {
               for (int ☃xxxxxxxxx = ☃.getZ() - ☃xxxxxx; ☃xxxxxxxxx <= ☃.getZ() + ☃xxxxxx && ☃xxxx; ☃xxxxxxxxx++) {
                  if (☃xxxxx < 0 || ☃xxxxx >= 256) {
                     ☃xxxx = false;
                  } else if (!this.canGrowInto(☃.getBlockState(☃xxxxxxx.setPos(☃xxxxxxxx, ☃xxxxx, ☃xxxxxxxxx)).getBlock())) {
                     ☃xxxx = false;
                  }
               }
            }
         }

         if (!☃xxxx) {
            return false;
         } else {
            Block ☃xxxxx = ☃.getBlockState(☃.down()).getBlock();
            if ((☃xxxxx == Blocks.GRASS || ☃xxxxx == Blocks.DIRT) && ☃.getY() < 256 - ☃ - 1) {
               this.setDirtAt(☃, ☃.down());
               int ☃xxxxxx = 0;

               for (int ☃xxxxxxx = ☃.getY() + ☃; ☃xxxxxxx >= ☃.getY() + ☃x; ☃xxxxxxx--) {
                  for (int ☃xxxxxxxx = ☃.getX() - ☃xxxxxx; ☃xxxxxxxx <= ☃.getX() + ☃xxxxxx; ☃xxxxxxxx++) {
                     int ☃xxxxxxxxxx = ☃xxxxxxxx - ☃.getX();

                     for (int ☃xxxxxxxxxxx = ☃.getZ() - ☃xxxxxx; ☃xxxxxxxxxxx <= ☃.getZ() + ☃xxxxxx; ☃xxxxxxxxxxx++) {
                        int ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx - ☃.getZ();
                        if (Math.abs(☃xxxxxxxxxx) != ☃xxxxxx || Math.abs(☃xxxxxxxxxxxx) != ☃xxxxxx || ☃xxxxxx <= 0) {
                           BlockPos ☃xxxxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxxxx);
                           if (!☃.getBlockState(☃xxxxxxxxxxxxx).isFullBlock()) {
                              this.setBlockAndNotifyAdequately(☃, ☃xxxxxxxxxxxxx, LEAF);
                           }
                        }
                     }
                  }

                  if (☃xxxxxx >= 1 && ☃xxxxxxx == ☃.getY() + ☃x + 1) {
                     ☃xxxxxx--;
                  } else if (☃xxxxxx < ☃xxx) {
                     ☃xxxxxx++;
                  }
               }

               for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃ - 1; ☃xxxxxxx++) {
                  Material ☃xxxxxxxx = ☃.getBlockState(☃.up(☃xxxxxxx)).getMaterial();
                  if (☃xxxxxxxx == Material.AIR || ☃xxxxxxxx == Material.LEAVES) {
                     this.setBlockAndNotifyAdequately(☃, ☃.up(☃xxxxxxx), TRUNK);
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
