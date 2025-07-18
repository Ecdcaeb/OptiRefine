package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenAbstractTree extends WorldGenerator {
   public WorldGenAbstractTree(boolean var1) {
      super(☃);
   }

   protected boolean canGrowInto(Block var1) {
      Material ☃ = ☃.getDefaultState().getMaterial();
      return ☃ == Material.AIR
         || ☃ == Material.LEAVES
         || ☃ == Blocks.GRASS
         || ☃ == Blocks.DIRT
         || ☃ == Blocks.LOG
         || ☃ == Blocks.LOG2
         || ☃ == Blocks.SAPLING
         || ☃ == Blocks.VINE;
   }

   public void generateSaplings(World var1, Random var2, BlockPos var3) {
   }

   protected void setDirtAt(World var1, BlockPos var2) {
      if (☃.getBlockState(☃).getBlock() != Blocks.DIRT) {
         this.setBlockAndNotifyAdequately(☃, ☃, Blocks.DIRT.getDefaultState());
      }
   }
}
