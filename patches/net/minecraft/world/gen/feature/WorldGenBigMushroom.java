package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBigMushroom extends WorldGenerator {
   private final Block mushroomType;

   public WorldGenBigMushroom(Block var1) {
      super(true);
      this.mushroomType = ☃;
   }

   public WorldGenBigMushroom() {
      super(false);
      this.mushroomType = null;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      Block ☃ = this.mushroomType;
      if (☃ == null) {
         ☃ = ☃.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK;
      }

      int ☃x = ☃.nextInt(3) + 4;
      if (☃.nextInt(12) == 0) {
         ☃x *= 2;
      }

      boolean ☃xx = true;
      if (☃.getY() >= 1 && ☃.getY() + ☃x + 1 < 256) {
         for (int ☃xxx = ☃.getY(); ☃xxx <= ☃.getY() + 1 + ☃x; ☃xxx++) {
            int ☃xxxx = 3;
            if (☃xxx <= ☃.getY() + 3) {
               ☃xxxx = 0;
            }

            BlockPos.MutableBlockPos ☃xxxxx = new BlockPos.MutableBlockPos();

            for (int ☃xxxxxx = ☃.getX() - ☃xxxx; ☃xxxxxx <= ☃.getX() + ☃xxxx && ☃xx; ☃xxxxxx++) {
               for (int ☃xxxxxxx = ☃.getZ() - ☃xxxx; ☃xxxxxxx <= ☃.getZ() + ☃xxxx && ☃xx; ☃xxxxxxx++) {
                  if (☃xxx >= 0 && ☃xxx < 256) {
                     Material ☃xxxxxxxx = ☃.getBlockState(☃xxxxx.setPos(☃xxxxxx, ☃xxx, ☃xxxxxxx)).getMaterial();
                     if (☃xxxxxxxx != Material.AIR && ☃xxxxxxxx != Material.LEAVES) {
                        ☃xx = false;
                     }
                  } else {
                     ☃xx = false;
                  }
               }
            }
         }

         if (!☃xx) {
            return false;
         } else {
            Block ☃xxx = ☃.getBlockState(☃.down()).getBlock();
            if (☃xxx != Blocks.DIRT && ☃xxx != Blocks.GRASS && ☃xxx != Blocks.MYCELIUM) {
               return false;
            } else {
               int ☃xxxx = ☃.getY() + ☃x;
               if (☃ == Blocks.RED_MUSHROOM_BLOCK) {
                  ☃xxxx = ☃.getY() + ☃x - 3;
               }

               for (int ☃xxxxx = ☃xxxx; ☃xxxxx <= ☃.getY() + ☃x; ☃xxxxx++) {
                  int ☃xxxxxx = 1;
                  if (☃xxxxx < ☃.getY() + ☃x) {
                     ☃xxxxxx++;
                  }

                  if (☃ == Blocks.BROWN_MUSHROOM_BLOCK) {
                     ☃xxxxxx = 3;
                  }

                  int ☃xxxxxxxx = ☃.getX() - ☃xxxxxx;
                  int ☃xxxxxxxxx = ☃.getX() + ☃xxxxxx;
                  int ☃xxxxxxxxxx = ☃.getZ() - ☃xxxxxx;
                  int ☃xxxxxxxxxxx = ☃.getZ() + ☃xxxxxx;

                  for (int ☃xxxxxxxxxxxx = ☃xxxxxxxx; ☃xxxxxxxxxxxx <= ☃xxxxxxxxx; ☃xxxxxxxxxxxx++) {
                     for (int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx; ☃xxxxxxxxxxxxx <= ☃xxxxxxxxxxx; ☃xxxxxxxxxxxxx++) {
                        int ☃xxxxxxxxxxxxxx = 5;
                        if (☃xxxxxxxxxxxx == ☃xxxxxxxx) {
                           ☃xxxxxxxxxxxxxx--;
                        } else if (☃xxxxxxxxxxxx == ☃xxxxxxxxx) {
                           ☃xxxxxxxxxxxxxx++;
                        }

                        if (☃xxxxxxxxxxxxx == ☃xxxxxxxxxx) {
                           ☃xxxxxxxxxxxxxx -= 3;
                        } else if (☃xxxxxxxxxxxxx == ☃xxxxxxxxxxx) {
                           ☃xxxxxxxxxxxxxx += 3;
                        }

                        BlockHugeMushroom.EnumType ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.byMetadata(☃xxxxxxxxxxxxxx);
                        if (☃ == Blocks.BROWN_MUSHROOM_BLOCK || ☃xxxxx < ☃.getY() + ☃x) {
                           if ((☃xxxxxxxxxxxx == ☃xxxxxxxx || ☃xxxxxxxxxxxx == ☃xxxxxxxxx) && (☃xxxxxxxxxxxxx == ☃xxxxxxxxxx || ☃xxxxxxxxxxxxx == ☃xxxxxxxxxxx)
                              )
                            {
                              continue;
                           }

                           if (☃xxxxxxxxxxxx == ☃.getX() - (☃xxxxxx - 1) && ☃xxxxxxxxxxxxx == ☃xxxxxxxxxx) {
                              ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.NORTH_WEST;
                           }

                           if (☃xxxxxxxxxxxx == ☃xxxxxxxx && ☃xxxxxxxxxxxxx == ☃.getZ() - (☃xxxxxx - 1)) {
                              ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.NORTH_WEST;
                           }

                           if (☃xxxxxxxxxxxx == ☃.getX() + (☃xxxxxx - 1) && ☃xxxxxxxxxxxxx == ☃xxxxxxxxxx) {
                              ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.NORTH_EAST;
                           }

                           if (☃xxxxxxxxxxxx == ☃xxxxxxxxx && ☃xxxxxxxxxxxxx == ☃.getZ() - (☃xxxxxx - 1)) {
                              ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.NORTH_EAST;
                           }

                           if (☃xxxxxxxxxxxx == ☃.getX() - (☃xxxxxx - 1) && ☃xxxxxxxxxxxxx == ☃xxxxxxxxxxx) {
                              ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.SOUTH_WEST;
                           }

                           if (☃xxxxxxxxxxxx == ☃xxxxxxxx && ☃xxxxxxxxxxxxx == ☃.getZ() + (☃xxxxxx - 1)) {
                              ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.SOUTH_WEST;
                           }

                           if (☃xxxxxxxxxxxx == ☃.getX() + (☃xxxxxx - 1) && ☃xxxxxxxxxxxxx == ☃xxxxxxxxxxx) {
                              ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.SOUTH_EAST;
                           }

                           if (☃xxxxxxxxxxxx == ☃xxxxxxxxx && ☃xxxxxxxxxxxxx == ☃.getZ() + (☃xxxxxx - 1)) {
                              ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.SOUTH_EAST;
                           }
                        }

                        if (☃xxxxxxxxxxxxxxx == BlockHugeMushroom.EnumType.CENTER && ☃xxxxx < ☃.getY() + ☃x) {
                           ☃xxxxxxxxxxxxxxx = BlockHugeMushroom.EnumType.ALL_INSIDE;
                        }

                        if (☃.getY() >= ☃.getY() + ☃x - 1 || ☃xxxxxxxxxxxxxxx != BlockHugeMushroom.EnumType.ALL_INSIDE) {
                           BlockPos ☃xxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxxx, ☃xxxxx, ☃xxxxxxxxxxxxx);
                           if (!☃.getBlockState(☃xxxxxxxxxxxxxxxx).isFullBlock()) {
                              this.setBlockAndNotifyAdequately(
                                 ☃, ☃xxxxxxxxxxxxxxxx, ☃.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, ☃xxxxxxxxxxxxxxx)
                              );
                           }
                        }
                     }
                  }
               }

               for (int ☃xxxxx = 0; ☃xxxxx < ☃x; ☃xxxxx++) {
                  IBlockState ☃xxxxxxxx = ☃.getBlockState(☃.up(☃xxxxx));
                  if (!☃xxxxxxxx.isFullBlock()) {
                     this.setBlockAndNotifyAdequately(
                        ☃, ☃.up(☃xxxxx), ☃.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM)
                     );
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }
}
