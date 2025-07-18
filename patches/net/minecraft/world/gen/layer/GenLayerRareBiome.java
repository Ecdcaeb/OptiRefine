package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerRareBiome extends GenLayer {
   public GenLayerRareBiome(long var1, GenLayer var3) {
      super(☃);
      this.parent = ☃;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int[] ☃ = this.parent.getInts(☃ - 1, ☃ - 1, ☃ + 2, ☃ + 2);
      int[] ☃x = IntCache.getIntCache(☃ * ☃);

      for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < ☃; ☃xxx++) {
            this.initChunkSeed(☃xxx + ☃, ☃xx + ☃);
            int ☃xxxx = ☃[☃xxx + 1 + (☃xx + 1) * (☃ + 2)];
            if (this.nextInt(57) == 0) {
               if (☃xxxx == Biome.getIdForBiome(Biomes.PLAINS)) {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.MUTATED_PLAINS);
               } else {
                  ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
               }
            } else {
               ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
            }
         }
      }

      return ☃x;
   }
}
