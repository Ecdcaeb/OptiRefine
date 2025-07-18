package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSwamp extends WorldGenAbstractTree {
   private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
   private static final IBlockState LEAF = Blocks.LEAVES
      .getDefaultState()
      .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
      .withProperty(BlockOldLeaf.CHECK_DECAY, false);

   public WorldGenSwamp() {
      super(false);
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = ☃.nextInt(4) + 5;

      while (☃.getBlockState(☃.down()).getMaterial() == Material.WATER) {
         ☃ = ☃.down();
      }

      boolean ☃x = true;
      if (☃.getY() >= 1 && ☃.getY() + ☃ + 1 <= 256) {
         for (int ☃xx = ☃.getY(); ☃xx <= ☃.getY() + 1 + ☃; ☃xx++) {
            int ☃xxx = 1;
            if (☃xx == ☃.getY()) {
               ☃xxx = 0;
            }

            if (☃xx >= ☃.getY() + 1 + ☃ - 2) {
               ☃xxx = 3;
            }

            BlockPos.MutableBlockPos ☃xxxx = new BlockPos.MutableBlockPos();

            for (int ☃xxxxx = ☃.getX() - ☃xxx; ☃xxxxx <= ☃.getX() + ☃xxx && ☃x; ☃xxxxx++) {
               for (int ☃xxxxxx = ☃.getZ() - ☃xxx; ☃xxxxxx <= ☃.getZ() + ☃xxx && ☃x; ☃xxxxxx++) {
                  if (☃xx >= 0 && ☃xx < 256) {
                     IBlockState ☃xxxxxxx = ☃.getBlockState(☃xxxx.setPos(☃xxxxx, ☃xx, ☃xxxxxx));
                     Block ☃xxxxxxxx = ☃xxxxxxx.getBlock();
                     if (☃xxxxxxx.getMaterial() != Material.AIR && ☃xxxxxxx.getMaterial() != Material.LEAVES) {
                        if (☃xxxxxxxx != Blocks.WATER && ☃xxxxxxxx != Blocks.FLOWING_WATER) {
                           ☃x = false;
                        } else if (☃xx > ☃.getY()) {
                           ☃x = false;
                        }
                     }
                  } else {
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

               for (int ☃xxxx = ☃.getY() - 3 + ☃; ☃xxxx <= ☃.getY() + ☃; ☃xxxx++) {
                  int ☃xxxxx = ☃xxxx - (☃.getY() + ☃);
                  int ☃xxxxxxx = 2 - ☃xxxxx / 2;

                  for (int ☃xxxxxxxx = ☃.getX() - ☃xxxxxxx; ☃xxxxxxxx <= ☃.getX() + ☃xxxxxxx; ☃xxxxxxxx++) {
                     int ☃xxxxxxxxx = ☃xxxxxxxx - ☃.getX();

                     for (int ☃xxxxxxxxxx = ☃.getZ() - ☃xxxxxxx; ☃xxxxxxxxxx <= ☃.getZ() + ☃xxxxxxx; ☃xxxxxxxxxx++) {
                        int ☃xxxxxxxxxxx = ☃xxxxxxxxxx - ☃.getZ();
                        if (Math.abs(☃xxxxxxxxx) != ☃xxxxxxx || Math.abs(☃xxxxxxxxxxx) != ☃xxxxxxx || ☃.nextInt(2) != 0 && ☃xxxxx != 0) {
                           BlockPos ☃xxxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxx, ☃xxxxxxxxxx);
                           if (!☃.getBlockState(☃xxxxxxxxxxxx).isFullBlock()) {
                              this.setBlockAndNotifyAdequately(☃, ☃xxxxxxxxxxxx, LEAF);
                           }
                        }
                     }
                  }
               }

               for (int ☃xxxx = 0; ☃xxxx < ☃; ☃xxxx++) {
                  IBlockState ☃xxxxx = ☃.getBlockState(☃.up(☃xxxx));
                  Block ☃xxxxxxx = ☃xxxxx.getBlock();
                  if (☃xxxxx.getMaterial() == Material.AIR
                     || ☃xxxxx.getMaterial() == Material.LEAVES
                     || ☃xxxxxxx == Blocks.FLOWING_WATER
                     || ☃xxxxxxx == Blocks.WATER) {
                     this.setBlockAndNotifyAdequately(☃, ☃.up(☃xxxx), TRUNK);
                  }
               }

               for (int ☃xxxxx = ☃.getY() - 3 + ☃; ☃xxxxx <= ☃.getY() + ☃; ☃xxxxx++) {
                  int ☃xxxxxxx = ☃xxxxx - (☃.getY() + ☃);
                  int ☃xxxxxxxx = 2 - ☃xxxxxxx / 2;
                  BlockPos.MutableBlockPos ☃xxxxxxxxx = new BlockPos.MutableBlockPos();

                  for (int ☃xxxxxxxxxxx = ☃.getX() - ☃xxxxxxxx; ☃xxxxxxxxxxx <= ☃.getX() + ☃xxxxxxxx; ☃xxxxxxxxxxx++) {
                     for (int ☃xxxxxxxxxxxx = ☃.getZ() - ☃xxxxxxxx; ☃xxxxxxxxxxxx <= ☃.getZ() + ☃xxxxxxxx; ☃xxxxxxxxxxxx++) {
                        ☃xxxxxxxxx.setPos(☃xxxxxxxxxxx, ☃xxxxx, ☃xxxxxxxxxxxx);
                        if (☃.getBlockState(☃xxxxxxxxx).getMaterial() == Material.LEAVES) {
                           BlockPos ☃xxxxxxxxxxxxx = ☃xxxxxxxxx.west();
                           BlockPos ☃xxxxxxxxxxxxxx = ☃xxxxxxxxx.east();
                           BlockPos ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxx.north();
                           BlockPos ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxx.south();
                           if (☃.nextInt(4) == 0 && ☃.getBlockState(☃xxxxxxxxxxxxx).getMaterial() == Material.AIR) {
                              this.addVine(☃, ☃xxxxxxxxxxxxx, BlockVine.EAST);
                           }

                           if (☃.nextInt(4) == 0 && ☃.getBlockState(☃xxxxxxxxxxxxxx).getMaterial() == Material.AIR) {
                              this.addVine(☃, ☃xxxxxxxxxxxxxx, BlockVine.WEST);
                           }

                           if (☃.nextInt(4) == 0 && ☃.getBlockState(☃xxxxxxxxxxxxxxx).getMaterial() == Material.AIR) {
                              this.addVine(☃, ☃xxxxxxxxxxxxxxx, BlockVine.SOUTH);
                           }

                           if (☃.nextInt(4) == 0 && ☃.getBlockState(☃xxxxxxxxxxxxxxxx).getMaterial() == Material.AIR) {
                              this.addVine(☃, ☃xxxxxxxxxxxxxxxx, BlockVine.NORTH);
                           }
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

   private void addVine(World var1, BlockPos var2, PropertyBool var3) {
      IBlockState ☃ = Blocks.VINE.getDefaultState().withProperty(☃, true);
      this.setBlockAndNotifyAdequately(☃, ☃, ☃);
      int ☃x = 4;

      for (BlockPos var6 = ☃.down(); ☃.getBlockState(var6).getMaterial() == Material.AIR && ☃x > 0; ☃x--) {
         this.setBlockAndNotifyAdequately(☃, var6, ☃);
         var6 = var6.down();
      }
   }
}
