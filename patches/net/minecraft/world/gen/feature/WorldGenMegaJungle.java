package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaJungle extends WorldGenHugeTrees {
   public WorldGenMegaJungle(boolean var1, int var2, int var3, IBlockState var4, IBlockState var5) {
      super(☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = this.getHeight(☃);
      if (!this.ensureGrowable(☃, ☃, ☃, ☃)) {
         return false;
      } else {
         this.createCrown(☃, ☃.up(☃), 2);

         for (int ☃x = ☃.getY() + ☃ - 2 - ☃.nextInt(4); ☃x > ☃.getY() + ☃ / 2; ☃x -= 2 + ☃.nextInt(4)) {
            float ☃xx = ☃.nextFloat() * (float) (Math.PI * 2);
            int ☃xxx = ☃.getX() + (int)(0.5F + MathHelper.cos(☃xx) * 4.0F);
            int ☃xxxx = ☃.getZ() + (int)(0.5F + MathHelper.sin(☃xx) * 4.0F);

            for (int ☃xxxxx = 0; ☃xxxxx < 5; ☃xxxxx++) {
               ☃xxx = ☃.getX() + (int)(1.5F + MathHelper.cos(☃xx) * ☃xxxxx);
               ☃xxxx = ☃.getZ() + (int)(1.5F + MathHelper.sin(☃xx) * ☃xxxxx);
               this.setBlockAndNotifyAdequately(☃, new BlockPos(☃xxx, ☃x - 3 + ☃xxxxx / 2, ☃xxxx), this.woodMetadata);
            }

            int ☃xxxxx = 1 + ☃.nextInt(2);
            int ☃xxxxxx = ☃x;

            for (int ☃xxxxxxx = ☃x - ☃xxxxx; ☃xxxxxxx <= ☃xxxxxx; ☃xxxxxxx++) {
               int ☃xxxxxxxx = ☃xxxxxxx - ☃xxxxxx;
               this.growLeavesLayer(☃, new BlockPos(☃xxx, ☃xxxxxxx, ☃xxxx), 1 - ☃xxxxxxxx);
            }
         }

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            BlockPos ☃xx = ☃.up(☃x);
            if (this.canGrowInto(☃.getBlockState(☃xx).getBlock())) {
               this.setBlockAndNotifyAdequately(☃, ☃xx, this.woodMetadata);
               if (☃x > 0) {
                  this.placeVine(☃, ☃, ☃xx.west(), BlockVine.EAST);
                  this.placeVine(☃, ☃, ☃xx.north(), BlockVine.SOUTH);
               }
            }

            if (☃x < ☃ - 1) {
               BlockPos ☃xxx = ☃xx.east();
               if (this.canGrowInto(☃.getBlockState(☃xxx).getBlock())) {
                  this.setBlockAndNotifyAdequately(☃, ☃xxx, this.woodMetadata);
                  if (☃x > 0) {
                     this.placeVine(☃, ☃, ☃xxx.east(), BlockVine.WEST);
                     this.placeVine(☃, ☃, ☃xxx.north(), BlockVine.SOUTH);
                  }
               }

               BlockPos ☃xxxx = ☃xx.south().east();
               if (this.canGrowInto(☃.getBlockState(☃xxxx).getBlock())) {
                  this.setBlockAndNotifyAdequately(☃, ☃xxxx, this.woodMetadata);
                  if (☃x > 0) {
                     this.placeVine(☃, ☃, ☃xxxx.east(), BlockVine.WEST);
                     this.placeVine(☃, ☃, ☃xxxx.south(), BlockVine.NORTH);
                  }
               }

               BlockPos ☃xxxxx = ☃xx.south();
               if (this.canGrowInto(☃.getBlockState(☃xxxxx).getBlock())) {
                  this.setBlockAndNotifyAdequately(☃, ☃xxxxx, this.woodMetadata);
                  if (☃x > 0) {
                     this.placeVine(☃, ☃, ☃xxxxx.west(), BlockVine.EAST);
                     this.placeVine(☃, ☃, ☃xxxxx.south(), BlockVine.NORTH);
                  }
               }
            }
         }

         return true;
      }
   }

   private void placeVine(World var1, Random var2, BlockPos var3, PropertyBool var4) {
      if (☃.nextInt(3) > 0 && ☃.isAirBlock(☃)) {
         this.setBlockAndNotifyAdequately(☃, ☃, Blocks.VINE.getDefaultState().withProperty(☃, true));
      }
   }

   private void createCrown(World var1, BlockPos var2, int var3) {
      int ☃ = 2;

      for (int ☃x = -2; ☃x <= 0; ☃x++) {
         this.growLeavesLayerStrict(☃, ☃.up(☃x), ☃ + 1 - ☃x);
      }
   }
}
