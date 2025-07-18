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

public class WorldGenSavannaTree extends WorldGenAbstractTree {
   private static final IBlockState TRUNK = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
   private static final IBlockState LEAF = Blocks.LEAVES2
      .getDefaultState()
      .withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA)
      .withProperty(BlockLeaves.CHECK_DECAY, false);

   public WorldGenSavannaTree(boolean var1) {
      super(☃);
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = ☃.nextInt(3) + ☃.nextInt(3) + 5;
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
            if ((☃xx == Blocks.GRASS || ☃xx == Blocks.DIRT) && ☃.getY() < 256 - ☃ - 1) {
               this.setDirtAt(☃, ☃.down());
               EnumFacing ☃xxxx = EnumFacing.Plane.HORIZONTAL.random(☃);
               int ☃xxxxx = ☃ - ☃.nextInt(4) - 1;
               int ☃xxxxxxx = 3 - ☃.nextInt(3);
               int ☃xxxxxxxx = ☃.getX();
               int ☃xxxxxxxxx = ☃.getZ();
               int ☃xxxxxxxxxx = 0;

               for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < ☃; ☃xxxxxxxxxxx++) {
                  int ☃xxxxxxxxxxxx = ☃.getY() + ☃xxxxxxxxxxx;
                  if (☃xxxxxxxxxxx >= ☃xxxxx && ☃xxxxxxx > 0) {
                     ☃xxxxxxxx += ☃xxxx.getXOffset();
                     ☃xxxxxxxxx += ☃xxxx.getZOffset();
                     ☃xxxxxxx--;
                  }

                  BlockPos ☃xxxxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxx);
                  Material ☃xxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxx).getMaterial();
                  if (☃xxxxxxxxxxxxxx == Material.AIR || ☃xxxxxxxxxxxxxx == Material.LEAVES) {
                     this.placeLogAt(☃, ☃xxxxxxxxxxxxx);
                     ☃xxxxxxxxxx = ☃xxxxxxxxxxxx;
                  }
               }

               BlockPos ☃xxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxx);

               for (int ☃xxxxxxxxxxxxx = -3; ☃xxxxxxxxxxxxx <= 3; ☃xxxxxxxxxxxxx++) {
                  for (int ☃xxxxxxxxxxxxxx = -3; ☃xxxxxxxxxxxxxx <= 3; ☃xxxxxxxxxxxxxx++) {
                     if (Math.abs(☃xxxxxxxxxxxxx) != 3 || Math.abs(☃xxxxxxxxxxxxxx) != 3) {
                        this.placeLeafAt(☃, ☃xxxxxxxxxxx.add(☃xxxxxxxxxxxxx, 0, ☃xxxxxxxxxxxxxx));
                     }
                  }
               }

               ☃xxxxxxxxxxx = ☃xxxxxxxxxxx.up();

               for (int ☃xxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxx <= 1; ☃xxxxxxxxxxxxx++) {
                  for (int ☃xxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxx <= 1; ☃xxxxxxxxxxxxxxx++) {
                     this.placeLeafAt(☃, ☃xxxxxxxxxxx.add(☃xxxxxxxxxxxxx, 0, ☃xxxxxxxxxxxxxxx));
                  }
               }

               this.placeLeafAt(☃, ☃xxxxxxxxxxx.east(2));
               this.placeLeafAt(☃, ☃xxxxxxxxxxx.west(2));
               this.placeLeafAt(☃, ☃xxxxxxxxxxx.south(2));
               this.placeLeafAt(☃, ☃xxxxxxxxxxx.north(2));
               ☃xxxxxxxx = ☃.getX();
               ☃xxxxxxxxx = ☃.getZ();
               EnumFacing ☃xxxxxxxxxxxxx = EnumFacing.Plane.HORIZONTAL.random(☃);
               if (☃xxxxxxxxxxxxx != ☃xxxx) {
                  int ☃xxxxxxxxxxxxxxx = ☃xxxxx - ☃.nextInt(2) - 1;
                  int ☃xxxxxxxxxxxxxxxx = 1 + ☃.nextInt(3);
                  ☃xxxxxxxxxx = 0;

                  for (int ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxx < ☃ && ☃xxxxxxxxxxxxxxxx > 0; ☃xxxxxxxxxxxxxxxx--) {
                     if (☃xxxxxxxxxxxxxxxxx >= 1) {
                        int ☃xxxxxxxxxxxxxxxxxx = ☃.getY() + ☃xxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxx += ☃xxxxxxxxxxxxx.getXOffset();
                        ☃xxxxxxxxx += ☃xxxxxxxxxxxxx.getZOffset();
                        BlockPos ☃xxxxxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxx);
                        Material ☃xxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxxxxxxx).getMaterial();
                        if (☃xxxxxxxxxxxxxxxxxxxx == Material.AIR || ☃xxxxxxxxxxxxxxxxxxxx == Material.LEAVES) {
                           this.placeLogAt(☃, ☃xxxxxxxxxxxxxxxxxxx);
                           ☃xxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx;
                        }
                     }

                     ☃xxxxxxxxxxxxxxxxx++;
                  }

                  if (☃xxxxxxxxxx > 0) {
                     BlockPos ☃xxxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxx);

                     for (int ☃xxxxxxxxxxxxxxxxxx = -2; ☃xxxxxxxxxxxxxxxxxx <= 2; ☃xxxxxxxxxxxxxxxxxx++) {
                        for (int ☃xxxxxxxxxxxxxxxxxxx = -2; ☃xxxxxxxxxxxxxxxxxxx <= 2; ☃xxxxxxxxxxxxxxxxxxx++) {
                           if (Math.abs(☃xxxxxxxxxxxxxxxxxx) != 2 || Math.abs(☃xxxxxxxxxxxxxxxxxxx) != 2) {
                              this.placeLeafAt(☃, ☃xxxxxxxxxxxxxxxxx.add(☃xxxxxxxxxxxxxxxxxx, 0, ☃xxxxxxxxxxxxxxxxxxx));
                           }
                        }
                     }

                     ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx.up();

                     for (int ☃xxxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxxx <= 1; ☃xxxxxxxxxxxxxxxxxx++) {
                        for (int ☃xxxxxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxxxxx <= 1; ☃xxxxxxxxxxxxxxxxxxxx++) {
                           this.placeLeafAt(☃, ☃xxxxxxxxxxxxxxxxx.add(☃xxxxxxxxxxxxxxxxxx, 0, ☃xxxxxxxxxxxxxxxxxxxx));
                        }
                     }
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

   private void placeLogAt(World var1, BlockPos var2) {
      this.setBlockAndNotifyAdequately(☃, ☃, TRUNK);
   }

   private void placeLeafAt(World var1, BlockPos var2) {
      Material ☃ = ☃.getBlockState(☃).getMaterial();
      if (☃ == Material.AIR || ☃ == Material.LEAVES) {
         this.setBlockAndNotifyAdequately(☃, ☃, LEAF);
      }
   }
}
