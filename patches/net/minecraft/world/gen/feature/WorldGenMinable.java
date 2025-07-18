package net.minecraft.world.gen.feature;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMinable extends WorldGenerator {
   private final IBlockState oreBlock;
   private final int numberOfBlocks;
   private final Predicate<IBlockState> predicate;

   public WorldGenMinable(IBlockState var1, int var2) {
      this(☃, ☃, new WorldGenMinable.StonePredicate());
   }

   public WorldGenMinable(IBlockState var1, int var2, Predicate<IBlockState> var3) {
      this.oreBlock = ☃;
      this.numberOfBlocks = ☃;
      this.predicate = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      float ☃ = ☃.nextFloat() * (float) Math.PI;
      double ☃x = ☃.getX() + 8 + MathHelper.sin(☃) * this.numberOfBlocks / 8.0F;
      double ☃xx = ☃.getX() + 8 - MathHelper.sin(☃) * this.numberOfBlocks / 8.0F;
      double ☃xxx = ☃.getZ() + 8 + MathHelper.cos(☃) * this.numberOfBlocks / 8.0F;
      double ☃xxxx = ☃.getZ() + 8 - MathHelper.cos(☃) * this.numberOfBlocks / 8.0F;
      double ☃xxxxx = ☃.getY() + ☃.nextInt(3) - 2;
      double ☃xxxxxx = ☃.getY() + ☃.nextInt(3) - 2;

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < this.numberOfBlocks; ☃xxxxxxx++) {
         float ☃xxxxxxxx = (float)☃xxxxxxx / this.numberOfBlocks;
         double ☃xxxxxxxxx = ☃x + (☃xx - ☃x) * ☃xxxxxxxx;
         double ☃xxxxxxxxxx = ☃xxxxx + (☃xxxxxx - ☃xxxxx) * ☃xxxxxxxx;
         double ☃xxxxxxxxxxx = ☃xxx + (☃xxxx - ☃xxx) * ☃xxxxxxxx;
         double ☃xxxxxxxxxxxx = ☃.nextDouble() * this.numberOfBlocks / 16.0;
         double ☃xxxxxxxxxxxxx = (MathHelper.sin((float) Math.PI * ☃xxxxxxxx) + 1.0F) * ☃xxxxxxxxxxxx + 1.0;
         double ☃xxxxxxxxxxxxxx = (MathHelper.sin((float) Math.PI * ☃xxxxxxxx) + 1.0F) * ☃xxxxxxxxxxxx + 1.0;
         int ☃xxxxxxxxxxxxxxx = MathHelper.floor(☃xxxxxxxxx - ☃xxxxxxxxxxxxx / 2.0);
         int ☃xxxxxxxxxxxxxxxx = MathHelper.floor(☃xxxxxxxxxx - ☃xxxxxxxxxxxxxx / 2.0);
         int ☃xxxxxxxxxxxxxxxxx = MathHelper.floor(☃xxxxxxxxxxx - ☃xxxxxxxxxxxxx / 2.0);
         int ☃xxxxxxxxxxxxxxxxxx = MathHelper.floor(☃xxxxxxxxx + ☃xxxxxxxxxxxxx / 2.0);
         int ☃xxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃xxxxxxxxxx + ☃xxxxxxxxxxxxxx / 2.0);
         int ☃xxxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃xxxxxxxxxxx + ☃xxxxxxxxxxxxx / 2.0);

         for (int ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxx <= ☃xxxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxx++) {
            double ☃xxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxx + 0.5 - ☃xxxxxxxxx) / (☃xxxxxxxxxxxxx / 2.0);
            if (☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx < 1.0) {
               for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxx <= ☃xxxxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxx + 0.5 - ☃xxxxxxxxxx) / (☃xxxxxxxxxxxxxx / 2.0);
                  if (☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxx < 1.0) {
                     for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxx <= ☃xxxxxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxx++
                     ) {
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxxxx + 0.5 - ☃xxxxxxxxxxx) / (☃xxxxxxxxxxxxx / 2.0);
                        if (☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx
                              + ☃xxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxx
                              + ☃xxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxx
                           < 1.0) {
                           BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxx);
                           if (this.predicate.apply(☃.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx))) {
                              ☃.setBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, this.oreBlock, 2);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return true;
   }

   static class StonePredicate implements Predicate<IBlockState> {
      private StonePredicate() {
      }

      public boolean apply(IBlockState var1) {
         if (☃ != null && ☃.getBlock() == Blocks.STONE) {
            BlockStone.EnumType ☃ = ☃.getValue(BlockStone.VARIANT);
            return ☃.isNatural();
         } else {
            return false;
         }
      }
   }
}
