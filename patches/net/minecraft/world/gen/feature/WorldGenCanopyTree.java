package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenCanopyTree extends WorldGenAbstractTree {
   private static final IBlockState DARK_OAK_LOG = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
   private static final IBlockState DARK_OAK_LEAVES = Blocks.LEAVES2
      .getDefaultState()
      .withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK)
      .withProperty(BlockLeaves.CHECK_DECAY, false);

   public WorldGenCanopyTree(boolean var1) {
      super(☃);
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = ☃.nextInt(3) + ☃.nextInt(2) + 6;
      int ☃x = ☃.getX();
      int ☃xx = ☃.getY();
      int ☃xxx = ☃.getZ();
      if (☃xx >= 1 && ☃xx + ☃ + 1 < 256) {
         BlockPos ☃xxxx = ☃.down();
         Block ☃xxxxx = ☃.getBlockState(☃xxxx).getBlock();
         if (☃xxxxx != Blocks.GRASS && ☃xxxxx != Blocks.DIRT) {
            return false;
         } else if (!this.placeTreeOfHeight(☃, ☃, ☃)) {
            return false;
         } else {
            this.setDirtAt(☃, ☃xxxx);
            this.setDirtAt(☃, ☃xxxx.east());
            this.setDirtAt(☃, ☃xxxx.south());
            this.setDirtAt(☃, ☃xxxx.south().east());
            EnumFacing ☃xxxxxx = EnumFacing.Plane.HORIZONTAL.random(☃);
            int ☃xxxxxxx = ☃ - ☃.nextInt(4);
            int ☃xxxxxxxx = 2 - ☃.nextInt(3);
            int ☃xxxxxxxxx = ☃x;
            int ☃xxxxxxxxxx = ☃xxx;
            int ☃xxxxxxxxxxx = ☃xx + ☃ - 1;

            for (int ☃xxxxxxxxxxxx = 0; ☃xxxxxxxxxxxx < ☃; ☃xxxxxxxxxxxx++) {
               if (☃xxxxxxxxxxxx >= ☃xxxxxxx && ☃xxxxxxxx > 0) {
                  ☃xxxxxxxxx += ☃xxxxxx.getXOffset();
                  ☃xxxxxxxxxx += ☃xxxxxx.getZOffset();
                  ☃xxxxxxxx--;
               }

               int ☃xxxxxxxxxxxxx = ☃xx + ☃xxxxxxxxxxxx;
               BlockPos ☃xxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxx);
               Material ☃xxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxx).getMaterial();
               if (☃xxxxxxxxxxxxxxx == Material.AIR || ☃xxxxxxxxxxxxxxx == Material.LEAVES) {
                  this.placeLogAt(☃, ☃xxxxxxxxxxxxxx);
                  this.placeLogAt(☃, ☃xxxxxxxxxxxxxx.east());
                  this.placeLogAt(☃, ☃xxxxxxxxxxxxxx.south());
                  this.placeLogAt(☃, ☃xxxxxxxxxxxxxx.east().south());
               }
            }

            for (int ☃xxxxxxxxxxxx = -2; ☃xxxxxxxxxxxx <= 0; ☃xxxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxxx = -2; ☃xxxxxxxxxxxxx <= 0; ☃xxxxxxxxxxxxx++) {
                  int ☃xxxxxxxxxxxxxx = -1;
                  this.placeLeafAt(☃, ☃xxxxxxxxx + ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxx + ☃xxxxxxxxxxxxx);
                  this.placeLeafAt(☃, 1 + ☃xxxxxxxxx - ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxx + ☃xxxxxxxxxxxxx);
                  this.placeLeafAt(☃, ☃xxxxxxxxx + ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxx, 1 + ☃xxxxxxxxxx - ☃xxxxxxxxxxxxx);
                  this.placeLeafAt(☃, 1 + ☃xxxxxxxxx - ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxx, 1 + ☃xxxxxxxxxx - ☃xxxxxxxxxxxxx);
                  if ((☃xxxxxxxxxxxx > -2 || ☃xxxxxxxxxxxxx > -1) && (☃xxxxxxxxxxxx != -1 || ☃xxxxxxxxxxxxx != -2)) {
                     int var28 = 1;
                     this.placeLeafAt(☃, ☃xxxxxxxxx + ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx + var28, ☃xxxxxxxxxx + ☃xxxxxxxxxxxxx);
                     this.placeLeafAt(☃, 1 + ☃xxxxxxxxx - ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx + var28, ☃xxxxxxxxxx + ☃xxxxxxxxxxxxx);
                     this.placeLeafAt(☃, ☃xxxxxxxxx + ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx + var28, 1 + ☃xxxxxxxxxx - ☃xxxxxxxxxxxxx);
                     this.placeLeafAt(☃, 1 + ☃xxxxxxxxx - ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx + var28, 1 + ☃xxxxxxxxxx - ☃xxxxxxxxxxxxx);
                  }
               }
            }

            if (☃.nextBoolean()) {
               this.placeLeafAt(☃, ☃xxxxxxxxx, ☃xxxxxxxxxxx + 2, ☃xxxxxxxxxx);
               this.placeLeafAt(☃, ☃xxxxxxxxx + 1, ☃xxxxxxxxxxx + 2, ☃xxxxxxxxxx);
               this.placeLeafAt(☃, ☃xxxxxxxxx + 1, ☃xxxxxxxxxxx + 2, ☃xxxxxxxxxx + 1);
               this.placeLeafAt(☃, ☃xxxxxxxxx, ☃xxxxxxxxxxx + 2, ☃xxxxxxxxxx + 1);
            }

            for (int ☃xxxxxxxxxxxx = -3; ☃xxxxxxxxxxxx <= 4; ☃xxxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxxxx = -3; ☃xxxxxxxxxxxxxx <= 4; ☃xxxxxxxxxxxxxx++) {
                  if ((☃xxxxxxxxxxxx != -3 || ☃xxxxxxxxxxxxxx != -3)
                     && (☃xxxxxxxxxxxx != -3 || ☃xxxxxxxxxxxxxx != 4)
                     && (☃xxxxxxxxxxxx != 4 || ☃xxxxxxxxxxxxxx != -3)
                     && (☃xxxxxxxxxxxx != 4 || ☃xxxxxxxxxxxxxx != 4)
                     && (Math.abs(☃xxxxxxxxxxxx) < 3 || Math.abs(☃xxxxxxxxxxxxxx) < 3)) {
                     this.placeLeafAt(☃, ☃xxxxxxxxx + ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxx + ☃xxxxxxxxxxxxxx);
                  }
               }
            }

            for (int ☃xxxxxxxxxxxx = -1; ☃xxxxxxxxxxxx <= 2; ☃xxxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxx <= 2; ☃xxxxxxxxxxxxxxx++) {
                  if ((☃xxxxxxxxxxxx < 0 || ☃xxxxxxxxxxxx > 1 || ☃xxxxxxxxxxxxxxx < 0 || ☃xxxxxxxxxxxxxxx > 1) && ☃.nextInt(3) <= 0) {
                     int ☃xxxxxxxxxxxxxxxx = ☃.nextInt(3) + 2;

                     for (int ☃xxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxx++) {
                        this.placeLogAt(☃, new BlockPos(☃x + ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxx - 1, ☃xxx + ☃xxxxxxxxxxxxxxx));
                     }

                     for (int ☃xxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxx <= 1; ☃xxxxxxxxxxxxxxxxx++) {
                        for (int ☃xxxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxxx <= 1; ☃xxxxxxxxxxxxxxxxxx++) {
                           this.placeLeafAt(
                              ☃, ☃xxxxxxxxx + ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxx + ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx
                           );
                        }
                     }

                     for (int ☃xxxxxxxxxxxxxxxxx = -2; ☃xxxxxxxxxxxxxxxxx <= 2; ☃xxxxxxxxxxxxxxxxx++) {
                        for (int ☃xxxxxxxxxxxxxxxxxx = -2; ☃xxxxxxxxxxxxxxxxxx <= 2; ☃xxxxxxxxxxxxxxxxxx++) {
                           if (Math.abs(☃xxxxxxxxxxxxxxxxx) != 2 || Math.abs(☃xxxxxxxxxxxxxxxxxx) != 2) {
                              this.placeLeafAt(
                                 ☃, ☃xxxxxxxxx + ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx - 1, ☃xxxxxxxxxx + ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx
                              );
                           }
                        }
                     }
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private boolean placeTreeOfHeight(World var1, BlockPos var2, int var3) {
      int ☃ = ☃.getX();
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ();
      BlockPos.MutableBlockPos ☃xxx = new BlockPos.MutableBlockPos();

      for (int ☃xxxx = 0; ☃xxxx <= ☃ + 1; ☃xxxx++) {
         int ☃xxxxx = 1;
         if (☃xxxx == 0) {
            ☃xxxxx = 0;
         }

         if (☃xxxx >= ☃ - 1) {
            ☃xxxxx = 2;
         }

         for (int ☃xxxxxx = -☃xxxxx; ☃xxxxxx <= ☃xxxxx; ☃xxxxxx++) {
            for (int ☃xxxxxxx = -☃xxxxx; ☃xxxxxxx <= ☃xxxxx; ☃xxxxxxx++) {
               if (!this.canGrowInto(☃.getBlockState(☃xxx.setPos(☃ + ☃xxxxxx, ☃x + ☃xxxx, ☃xx + ☃xxxxxxx)).getBlock())) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private void placeLogAt(World var1, BlockPos var2) {
      if (this.canGrowInto(☃.getBlockState(☃).getBlock())) {
         this.setBlockAndNotifyAdequately(☃, ☃, DARK_OAK_LOG);
      }
   }

   private void placeLeafAt(World var1, int var2, int var3, int var4) {
      BlockPos ☃ = new BlockPos(☃, ☃, ☃);
      Material ☃x = ☃.getBlockState(☃).getMaterial();
      if (☃x == Material.AIR) {
         this.setBlockAndNotifyAdequately(☃, ☃, DARK_OAK_LEAVES);
      }
   }
}
