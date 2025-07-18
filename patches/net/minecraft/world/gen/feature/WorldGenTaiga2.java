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

public class WorldGenTaiga2 extends WorldGenAbstractTree {
   private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
   private static final IBlockState LEAF = Blocks.LEAVES
      .getDefaultState()
      .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)
      .withProperty(BlockLeaves.CHECK_DECAY, false);

   public WorldGenTaiga2(boolean var1) {
      super(☃);
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = ☃.nextInt(4) + 6;
      int ☃x = 1 + ☃.nextInt(2);
      int ☃xx = ☃ - ☃x;
      int ☃xxx = 2 + ☃.nextInt(2);
      boolean ☃xxxx = true;
      if (☃.getY() >= 1 && ☃.getY() + ☃ + 1 <= 256) {
         for (int ☃xxxxx = ☃.getY(); ☃xxxxx <= ☃.getY() + 1 + ☃ && ☃xxxx; ☃xxxxx++) {
            int ☃xxxxxx;
            if (☃xxxxx - ☃.getY() < ☃x) {
               ☃xxxxxx = 0;
            } else {
               ☃xxxxxx = ☃xxx;
            }

            BlockPos.MutableBlockPos ☃xxxxxxx = new BlockPos.MutableBlockPos();

            for (int ☃xxxxxxxx = ☃.getX() - ☃xxxxxx; ☃xxxxxxxx <= ☃.getX() + ☃xxxxxx && ☃xxxx; ☃xxxxxxxx++) {
               for (int ☃xxxxxxxxx = ☃.getZ() - ☃xxxxxx; ☃xxxxxxxxx <= ☃.getZ() + ☃xxxxxx && ☃xxxx; ☃xxxxxxxxx++) {
                  if (☃xxxxx >= 0 && ☃xxxxx < 256) {
                     Material ☃xxxxxxxxxx = ☃.getBlockState(☃xxxxxxx.setPos(☃xxxxxxxx, ☃xxxxx, ☃xxxxxxxxx)).getMaterial();
                     if (☃xxxxxxxxxx != Material.AIR && ☃xxxxxxxxxx != Material.LEAVES) {
                        ☃xxxx = false;
                     }
                  } else {
                     ☃xxxx = false;
                  }
               }
            }
         }

         if (!☃xxxx) {
            return false;
         } else {
            Block ☃xxxxx = ☃.getBlockState(☃.down()).getBlock();
            if ((☃xxxxx == Blocks.GRASS || ☃xxxxx == Blocks.DIRT || ☃xxxxx == Blocks.FARMLAND) && ☃.getY() < 256 - ☃ - 1) {
               this.setDirtAt(☃, ☃.down());
               int ☃xxxxxx = ☃.nextInt(2);
               int ☃xxxxxxx = 1;
               int ☃xxxxxxxx = 0;

               for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx <= ☃xx; ☃xxxxxxxxxx++) {
                  int ☃xxxxxxxxxxx = ☃.getY() + ☃ - ☃xxxxxxxxxx;

                  for (int ☃xxxxxxxxxxxx = ☃.getX() - ☃xxxxxx; ☃xxxxxxxxxxxx <= ☃.getX() + ☃xxxxxx; ☃xxxxxxxxxxxx++) {
                     int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx - ☃.getX();

                     for (int ☃xxxxxxxxxxxxxx = ☃.getZ() - ☃xxxxxx; ☃xxxxxxxxxxxxxx <= ☃.getZ() + ☃xxxxxx; ☃xxxxxxxxxxxxxx++) {
                        int ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx - ☃.getZ();
                        if (Math.abs(☃xxxxxxxxxxxxx) != ☃xxxxxx || Math.abs(☃xxxxxxxxxxxxxxx) != ☃xxxxxx || ☃xxxxxx <= 0) {
                           BlockPos ☃xxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxx);
                           if (!☃.getBlockState(☃xxxxxxxxxxxxxxxx).isFullBlock()) {
                              this.setBlockAndNotifyAdequately(☃, ☃xxxxxxxxxxxxxxxx, LEAF);
                           }
                        }
                     }
                  }

                  if (☃xxxxxx >= ☃xxxxxxx) {
                     ☃xxxxxx = ☃xxxxxxxx;
                     ☃xxxxxxxx = 1;
                     if (++☃xxxxxxx > ☃xxx) {
                        ☃xxxxxxx = ☃xxx;
                     }
                  } else {
                     ☃xxxxxx++;
                  }
               }

               int ☃xxxxxxxxxx = ☃.nextInt(3);

               for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < ☃ - ☃xxxxxxxxxx; ☃xxxxxxxxxxx++) {
                  Material ☃xxxxxxxxxxxx = ☃.getBlockState(☃.up(☃xxxxxxxxxxx)).getMaterial();
                  if (☃xxxxxxxxxxxx == Material.AIR || ☃xxxxxxxxxxxx == Material.LEAVES) {
                     this.setBlockAndNotifyAdequately(☃, ☃.up(☃xxxxxxxxxxx), TRUNK);
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
