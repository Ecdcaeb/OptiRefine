package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BiomeVoidDecorator extends BiomeDecorator {
   @Override
   public void decorate(World var1, Random var2, Biome var3, BlockPos var4) {
      BlockPos ☃ = ☃.getSpawnPoint();
      int ☃x = 16;
      double ☃xx = ☃.distanceSq(☃.add(8, ☃.getY(), 8));
      if (!(☃xx > 1024.0)) {
         BlockPos ☃xxx = new BlockPos(☃.getX() - 16, ☃.getY() - 1, ☃.getZ() - 16);
         BlockPos ☃xxxx = new BlockPos(☃.getX() + 16, ☃.getY() - 1, ☃.getZ() + 16);
         BlockPos.MutableBlockPos ☃xxxxx = new BlockPos.MutableBlockPos(☃xxx);

         for (int ☃xxxxxx = ☃.getZ(); ☃xxxxxx < ☃.getZ() + 16; ☃xxxxxx++) {
            for (int ☃xxxxxxx = ☃.getX(); ☃xxxxxxx < ☃.getX() + 16; ☃xxxxxxx++) {
               if (☃xxxxxx >= ☃xxx.getZ() && ☃xxxxxx <= ☃xxxx.getZ() && ☃xxxxxxx >= ☃xxx.getX() && ☃xxxxxxx <= ☃xxxx.getX()) {
                  ☃xxxxx.setPos(☃xxxxxxx, ☃xxxxx.getY(), ☃xxxxxx);
                  if (☃.getX() == ☃xxxxxxx && ☃.getZ() == ☃xxxxxx) {
                     ☃.setBlockState(☃xxxxx, Blocks.COBBLESTONE.getDefaultState(), 2);
                  } else {
                     ☃.setBlockState(☃xxxxx, Blocks.STONE.getDefaultState(), 2);
                  }
               }
            }
         }
      }
   }
}
