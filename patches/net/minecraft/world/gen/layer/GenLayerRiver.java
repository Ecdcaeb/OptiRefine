package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerRiver extends GenLayer {
   public GenLayerRiver(long var1, GenLayer var3) {
      super(☃);
      super.parent = ☃;
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
            int ☃xxxxxxxx = this.riverFilter(☃xxxx[☃xxxxxxx + 0 + (☃xxxxxx + 1) * ☃xx]);
            int ☃xxxxxxxxx = this.riverFilter(☃xxxx[☃xxxxxxx + 2 + (☃xxxxxx + 1) * ☃xx]);
            int ☃xxxxxxxxxx = this.riverFilter(☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 0) * ☃xx]);
            int ☃xxxxxxxxxxx = this.riverFilter(☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 2) * ☃xx]);
            int ☃xxxxxxxxxxxx = this.riverFilter(☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1) * ☃xx]);
            if (☃xxxxxxxxxxxx == ☃xxxxxxxx && ☃xxxxxxxxxxxx == ☃xxxxxxxxxx && ☃xxxxxxxxxxxx == ☃xxxxxxxxx && ☃xxxxxxxxxxxx == ☃xxxxxxxxxxx) {
               ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = -1;
            } else {
               ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = Biome.getIdForBiome(Biomes.RIVER);
            }
         }
      }

      return ☃xxxxx;
   }

   private int riverFilter(int var1) {
      return ☃ >= 2 ? 2 + (☃ & 1) : ☃;
   }
}
