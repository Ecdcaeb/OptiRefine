package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTrees extends WorldGenAbstractTree {
   private static final IBlockState DEFAULT_TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
   private static final IBlockState DEFAULT_LEAF = Blocks.LEAVES
      .getDefaultState()
      .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
      .withProperty(BlockLeaves.CHECK_DECAY, false);
   private final int minTreeHeight;
   private final boolean vinesGrow;
   private final IBlockState metaWood;
   private final IBlockState metaLeaves;

   public WorldGenTrees(boolean var1) {
      this(☃, 4, DEFAULT_TRUNK, DEFAULT_LEAF, false);
   }

   public WorldGenTrees(boolean var1, int var2, IBlockState var3, IBlockState var4, boolean var5) {
      super(☃);
      this.minTreeHeight = ☃;
      this.metaWood = ☃;
      this.metaLeaves = ☃;
      this.vinesGrow = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = ☃.nextInt(3) + this.minTreeHeight;
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
               int ☃xxxx = 3;
               int ☃xxxxx = 0;

               for (int ☃xxxxxxx = ☃.getY() - 3 + ☃; ☃xxxxxxx <= ☃.getY() + ☃; ☃xxxxxxx++) {
                  int ☃xxxxxxxx = ☃xxxxxxx - (☃.getY() + ☃);
                  int ☃xxxxxxxxx = 1 - ☃xxxxxxxx / 2;

                  for (int ☃xxxxxxxxxx = ☃.getX() - ☃xxxxxxxxx; ☃xxxxxxxxxx <= ☃.getX() + ☃xxxxxxxxx; ☃xxxxxxxxxx++) {
                     int ☃xxxxxxxxxxx = ☃xxxxxxxxxx - ☃.getX();

                     for (int ☃xxxxxxxxxxxx = ☃.getZ() - ☃xxxxxxxxx; ☃xxxxxxxxxxxx <= ☃.getZ() + ☃xxxxxxxxx; ☃xxxxxxxxxxxx++) {
                        int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx - ☃.getZ();
                        if (Math.abs(☃xxxxxxxxxxx) != ☃xxxxxxxxx || Math.abs(☃xxxxxxxxxxxxx) != ☃xxxxxxxxx || ☃.nextInt(2) != 0 && ☃xxxxxxxx != 0) {
                           BlockPos ☃xxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxxxxx);
                           Material ☃xxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxx).getMaterial();
                           if (☃xxxxxxxxxxxxxxx == Material.AIR || ☃xxxxxxxxxxxxxxx == Material.LEAVES || ☃xxxxxxxxxxxxxxx == Material.VINE) {
                              this.setBlockAndNotifyAdequately(☃, ☃xxxxxxxxxxxxxx, this.metaLeaves);
                           }
                        }
                     }
                  }
               }

               for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃; ☃xxxxxxx++) {
                  Material ☃xxxxxxxx = ☃.getBlockState(☃.up(☃xxxxxxx)).getMaterial();
                  if (☃xxxxxxxx == Material.AIR || ☃xxxxxxxx == Material.LEAVES || ☃xxxxxxxx == Material.VINE) {
                     this.setBlockAndNotifyAdequately(☃, ☃.up(☃xxxxxxx), this.metaWood);
                     if (this.vinesGrow && ☃xxxxxxx > 0) {
                        if (☃.nextInt(3) > 0 && ☃.isAirBlock(☃.add(-1, ☃xxxxxxx, 0))) {
                           this.addVine(☃, ☃.add(-1, ☃xxxxxxx, 0), BlockVine.EAST);
                        }

                        if (☃.nextInt(3) > 0 && ☃.isAirBlock(☃.add(1, ☃xxxxxxx, 0))) {
                           this.addVine(☃, ☃.add(1, ☃xxxxxxx, 0), BlockVine.WEST);
                        }

                        if (☃.nextInt(3) > 0 && ☃.isAirBlock(☃.add(0, ☃xxxxxxx, -1))) {
                           this.addVine(☃, ☃.add(0, ☃xxxxxxx, -1), BlockVine.SOUTH);
                        }

                        if (☃.nextInt(3) > 0 && ☃.isAirBlock(☃.add(0, ☃xxxxxxx, 1))) {
                           this.addVine(☃, ☃.add(0, ☃xxxxxxx, 1), BlockVine.NORTH);
                        }
                     }
                  }
               }

               if (this.vinesGrow) {
                  for (int ☃xxxxxxxx = ☃.getY() - 3 + ☃; ☃xxxxxxxx <= ☃.getY() + ☃; ☃xxxxxxxx++) {
                     int ☃xxxxxxxxx = ☃xxxxxxxx - (☃.getY() + ☃);
                     int ☃xxxxxxxxxx = 2 - ☃xxxxxxxxx / 2;
                     BlockPos.MutableBlockPos ☃xxxxxxxxxxx = new BlockPos.MutableBlockPos();

                     for (int ☃xxxxxxxxxxxxx = ☃.getX() - ☃xxxxxxxxxx; ☃xxxxxxxxxxxxx <= ☃.getX() + ☃xxxxxxxxxx; ☃xxxxxxxxxxxxx++) {
                        for (int ☃xxxxxxxxxxxxxx = ☃.getZ() - ☃xxxxxxxxxx; ☃xxxxxxxxxxxxxx <= ☃.getZ() + ☃xxxxxxxxxx; ☃xxxxxxxxxxxxxx++) {
                           ☃xxxxxxxxxxx.setPos(☃xxxxxxxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxxxxxx);
                           if (☃.getBlockState(☃xxxxxxxxxxx).getMaterial() == Material.LEAVES) {
                              BlockPos ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxx.west();
                              BlockPos ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx.east();
                              BlockPos ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx.north();
                              BlockPos ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx.south();
                              if (☃.nextInt(4) == 0 && ☃.getBlockState(☃xxxxxxxxxxxxxxx).getMaterial() == Material.AIR) {
                                 this.addHangingVine(☃, ☃xxxxxxxxxxxxxxx, BlockVine.EAST);
                              }

                              if (☃.nextInt(4) == 0 && ☃.getBlockState(☃xxxxxxxxxxxxxxxx).getMaterial() == Material.AIR) {
                                 this.addHangingVine(☃, ☃xxxxxxxxxxxxxxxx, BlockVine.WEST);
                              }

                              if (☃.nextInt(4) == 0 && ☃.getBlockState(☃xxxxxxxxxxxxxxxxx).getMaterial() == Material.AIR) {
                                 this.addHangingVine(☃, ☃xxxxxxxxxxxxxxxxx, BlockVine.SOUTH);
                              }

                              if (☃.nextInt(4) == 0 && ☃.getBlockState(☃xxxxxxxxxxxxxxxxxx).getMaterial() == Material.AIR) {
                                 this.addHangingVine(☃, ☃xxxxxxxxxxxxxxxxxx, BlockVine.NORTH);
                              }
                           }
                        }
                     }
                  }

                  if (☃.nextInt(5) == 0 && ☃ > 5) {
                     for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 2; ☃xxxxxxxx++) {
                        for (EnumFacing ☃xxxxxxxxx : EnumFacing.Plane.HORIZONTAL) {
                           if (☃.nextInt(4 - ☃xxxxxxxx) == 0) {
                              EnumFacing ☃xxxxxxxxxx = ☃xxxxxxxxx.getOpposite();
                              this.placeCocoa(☃, ☃.nextInt(3), ☃.add(☃xxxxxxxxxx.getXOffset(), ☃ - 5 + ☃xxxxxxxx, ☃xxxxxxxxxx.getZOffset()), ☃xxxxxxxxx);
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

   private void placeCocoa(World var1, int var2, BlockPos var3, EnumFacing var4) {
      this.setBlockAndNotifyAdequately(☃, ☃, Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.AGE, ☃).withProperty(BlockCocoa.FACING, ☃));
   }

   private void addVine(World var1, BlockPos var2, PropertyBool var3) {
      this.setBlockAndNotifyAdequately(☃, ☃, Blocks.VINE.getDefaultState().withProperty(☃, true));
   }

   private void addHangingVine(World var1, BlockPos var2, PropertyBool var3) {
      this.addVine(☃, ☃, ☃);
      int ☃ = 4;

      for (BlockPos var5 = ☃.down(); ☃.getBlockState(var5).getMaterial() == Material.AIR && ☃ > 0; ☃--) {
         this.addVine(☃, var5, ☃);
         var5 = var5.down();
      }
   }
}
