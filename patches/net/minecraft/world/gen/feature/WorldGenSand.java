package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSand extends WorldGenerator {
   private final Block block;
   private final int radius;

   public WorldGenSand(Block var1, int var2) {
      this.block = ☃;
      this.radius = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (☃.getBlockState(☃).getMaterial() != Material.WATER) {
         return false;
      } else {
         int ☃ = ☃.nextInt(this.radius - 2) + 2;
         int ☃x = 2;

         for (int ☃xx = ☃.getX() - ☃; ☃xx <= ☃.getX() + ☃; ☃xx++) {
            for (int ☃xxx = ☃.getZ() - ☃; ☃xxx <= ☃.getZ() + ☃; ☃xxx++) {
               int ☃xxxx = ☃xx - ☃.getX();
               int ☃xxxxx = ☃xxx - ☃.getZ();
               if (☃xxxx * ☃xxxx + ☃xxxxx * ☃xxxxx <= ☃ * ☃) {
                  for (int ☃xxxxxx = ☃.getY() - 2; ☃xxxxxx <= ☃.getY() + 2; ☃xxxxxx++) {
                     BlockPos ☃xxxxxxx = new BlockPos(☃xx, ☃xxxxxx, ☃xxx);
                     Block ☃xxxxxxxx = ☃.getBlockState(☃xxxxxxx).getBlock();
                     if (☃xxxxxxxx == Blocks.DIRT || ☃xxxxxxxx == Blocks.GRASS) {
                        ☃.setBlockState(☃xxxxxxx, this.block.getDefaultState(), 2);
                     }
                  }
               }
            }
         }

         return true;
      }
   }
}
