package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenShrub extends WorldGenTrees {
   private final IBlockState leavesMetadata;
   private final IBlockState woodMetadata;

   public WorldGenShrub(IBlockState var1, IBlockState var2) {
      super(false);
      this.woodMetadata = ☃;
      this.leavesMetadata = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (IBlockState ☃ = ☃.getBlockState(☃); (☃.getMaterial() == Material.AIR || ☃.getMaterial() == Material.LEAVES) && ☃.getY() > 0; ☃ = ☃.getBlockState(☃)) {
         ☃ = ☃.down();
      }

      Block ☃ = ☃.getBlockState(☃).getBlock();
      if (☃ == Blocks.DIRT || ☃ == Blocks.GRASS) {
         ☃ = ☃.up();
         this.setBlockAndNotifyAdequately(☃, ☃, this.woodMetadata);

         for (int ☃x = ☃.getY(); ☃x <= ☃.getY() + 2; ☃x++) {
            int ☃xx = ☃x - ☃.getY();
            int ☃xxx = 2 - ☃xx;

            for (int ☃xxxx = ☃.getX() - ☃xxx; ☃xxxx <= ☃.getX() + ☃xxx; ☃xxxx++) {
               int ☃xxxxx = ☃xxxx - ☃.getX();

               for (int ☃xxxxxx = ☃.getZ() - ☃xxx; ☃xxxxxx <= ☃.getZ() + ☃xxx; ☃xxxxxx++) {
                  int ☃xxxxxxx = ☃xxxxxx - ☃.getZ();
                  if (Math.abs(☃xxxxx) != ☃xxx || Math.abs(☃xxxxxxx) != ☃xxx || ☃.nextInt(2) != 0) {
                     BlockPos ☃xxxxxxxx = new BlockPos(☃xxxx, ☃x, ☃xxxxxx);
                     Material ☃xxxxxxxxx = ☃.getBlockState(☃xxxxxxxx).getMaterial();
                     if (☃xxxxxxxxx == Material.AIR || ☃xxxxxxxxx == Material.LEAVES) {
                        this.setBlockAndNotifyAdequately(☃, ☃xxxxxxxx, this.leavesMetadata);
                     }
                  }
               }
            }
         }
      }

      return true;
   }
}
