package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerDeepOcean extends GenLayer {
   public GenLayerDeepOcean(long var1, GenLayer var3) {
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
            int ☃xxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1 - 1) * (☃ + 2)];
            int ☃xxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + 1 + (☃xxxxxx + 1) * (☃ + 2)];
            int ☃xxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 - 1 + (☃xxxxxx + 1) * (☃ + 2)];
            int ☃xxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1 + 1) * (☃ + 2)];
            int ☃xxxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1) * ☃xx];
            int ☃xxxxxxxxxxxxx = 0;
            if (☃xxxxxxxx == 0) {
               ☃xxxxxxxxxxxxx++;
            }

            if (☃xxxxxxxxx == 0) {
               ☃xxxxxxxxxxxxx++;
            }

            if (☃xxxxxxxxxx == 0) {
               ☃xxxxxxxxxxxxx++;
            }

            if (☃xxxxxxxxxxx == 0) {
               ☃xxxxxxxxxxxxx++;
            }

            if (☃xxxxxxxxxxxx == 0 && ☃xxxxxxxxxxxxx > 3) {
               ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = Biome.getIdForBiome(Biomes.DEEP_OCEAN);
            } else {
               ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = ☃xxxxxxxxxxxx;
            }
         }
      }

      return ☃xxxxx;
   }
}
