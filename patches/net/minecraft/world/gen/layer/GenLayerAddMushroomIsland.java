package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerAddMushroomIsland extends GenLayer {
   public GenLayerAddMushroomIsland(long var1, GenLayer var3) {
      super(☃);
      this.parent = ☃;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int ☃ = ☃ - 1;
      int ☃x = ☃ - 1;
      int ☃xx = ☃ + 2;
      int ☃xxx = ☃ + 2;
      int[] ☃xxxx = this.parent.getInts(☃, ☃x, ☃xx, ☃xxx);
      int[] ☃xxxxx = IntCache.getIntCache(☃ * ☃);

      for (int ☃xxxxxx = 0; ☃xxxxxx < ☃; ☃xxxxxx++) {
         for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃; ☃xxxxxxx++) {
            int ☃xxxxxxxx = ☃xxxx[☃xxxxxxx + 0 + (☃xxxxxx + 0) * ☃xx];
            int ☃xxxxxxxxx = ☃xxxx[☃xxxxxxx + 2 + (☃xxxxxx + 0) * ☃xx];
            int ☃xxxxxxxxxx = ☃xxxx[☃xxxxxxx + 0 + (☃xxxxxx + 2) * ☃xx];
            int ☃xxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 2 + (☃xxxxxx + 2) * ☃xx];
            int ☃xxxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1) * ☃xx];
            this.initChunkSeed(☃xxxxxxx + ☃, ☃xxxxxx + ☃);
            if (☃xxxxxxxxxxxx == 0 && ☃xxxxxxxx == 0 && ☃xxxxxxxxx == 0 && ☃xxxxxxxxxx == 0 && ☃xxxxxxxxxxx == 0 && this.nextInt(100) == 0) {
               ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
            } else {
               ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = ☃xxxxxxxxxxxx;
            }
         }
      }

      return ☃xxxxx;
   }
}
