package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaPineTree extends WorldGenHugeTrees {
   private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
   private static final IBlockState LEAF = Blocks.LEAVES
      .getDefaultState()
      .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)
      .withProperty(BlockLeaves.CHECK_DECAY, false);
   private static final IBlockState PODZOL = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
   private final boolean useBaseHeight;

   public WorldGenMegaPineTree(boolean var1, boolean var2) {
      super(☃, 13, 15, TRUNK, LEAF);
      this.useBaseHeight = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = this.getHeight(☃);
      if (!this.ensureGrowable(☃, ☃, ☃, ☃)) {
         return false;
      } else {
         this.createCrown(☃, ☃.getX(), ☃.getZ(), ☃.getY() + ☃, 0, ☃);

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            IBlockState ☃xx = ☃.getBlockState(☃.up(☃x));
            if (☃xx.getMaterial() == Material.AIR || ☃xx.getMaterial() == Material.LEAVES) {
               this.setBlockAndNotifyAdequately(☃, ☃.up(☃x), this.woodMetadata);
            }

            if (☃x < ☃ - 1) {
               ☃xx = ☃.getBlockState(☃.add(1, ☃x, 0));
               if (☃xx.getMaterial() == Material.AIR || ☃xx.getMaterial() == Material.LEAVES) {
                  this.setBlockAndNotifyAdequately(☃, ☃.add(1, ☃x, 0), this.woodMetadata);
               }

               ☃xx = ☃.getBlockState(☃.add(1, ☃x, 1));
               if (☃xx.getMaterial() == Material.AIR || ☃xx.getMaterial() == Material.LEAVES) {
                  this.setBlockAndNotifyAdequately(☃, ☃.add(1, ☃x, 1), this.woodMetadata);
               }

               ☃xx = ☃.getBlockState(☃.add(0, ☃x, 1));
               if (☃xx.getMaterial() == Material.AIR || ☃xx.getMaterial() == Material.LEAVES) {
                  this.setBlockAndNotifyAdequately(☃, ☃.add(0, ☃x, 1), this.woodMetadata);
               }
            }
         }

         return true;
      }
   }

   private void createCrown(World var1, int var2, int var3, int var4, int var5, Random var6) {
      int ☃ = ☃.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
      int ☃x = 0;

      for (int ☃xx = ☃ - ☃; ☃xx <= ☃; ☃xx++) {
         int ☃xxx = ☃ - ☃xx;
         int ☃xxxx = ☃ + MathHelper.floor((float)☃xxx / ☃ * 3.5F);
         this.growLeavesLayerStrict(☃, new BlockPos(☃, ☃xx, ☃), ☃xxxx + (☃xxx > 0 && ☃xxxx == ☃x && (☃xx & 1) == 0 ? 1 : 0));
         ☃x = ☃xxxx;
      }
   }

   @Override
   public void generateSaplings(World var1, Random var2, BlockPos var3) {
      this.placePodzolCircle(☃, ☃.west().north());
      this.placePodzolCircle(☃, ☃.east(2).north());
      this.placePodzolCircle(☃, ☃.west().south(2));
      this.placePodzolCircle(☃, ☃.east(2).south(2));

      for (int ☃ = 0; ☃ < 5; ☃++) {
         int ☃x = ☃.nextInt(64);
         int ☃xx = ☃x % 8;
         int ☃xxx = ☃x / 8;
         if (☃xx == 0 || ☃xx == 7 || ☃xxx == 0 || ☃xxx == 7) {
            this.placePodzolCircle(☃, ☃.add(-3 + ☃xx, 0, -3 + ☃xxx));
         }
      }
   }

   private void placePodzolCircle(World var1, BlockPos var2) {
      for (int ☃ = -2; ☃ <= 2; ☃++) {
         for (int ☃x = -2; ☃x <= 2; ☃x++) {
            if (Math.abs(☃) != 2 || Math.abs(☃x) != 2) {
               this.placePodzolAt(☃, ☃.add(☃, 0, ☃x));
            }
         }
      }
   }

   private void placePodzolAt(World var1, BlockPos var2) {
      for (int ☃ = 2; ☃ >= -3; ☃--) {
         BlockPos ☃x = ☃.up(☃);
         IBlockState ☃xx = ☃.getBlockState(☃x);
         Block ☃xxx = ☃xx.getBlock();
         if (☃xxx == Blocks.GRASS || ☃xxx == Blocks.DIRT) {
            this.setBlockAndNotifyAdequately(☃, ☃x, PODZOL);
            break;
         }

         if (☃xx.getMaterial() != Material.AIR && ☃ < 0) {
            break;
         }
      }
   }
}
