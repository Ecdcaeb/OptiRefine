package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;

public class GenLayerShore extends GenLayer {
   public GenLayerShore(long var1, GenLayer var3) {
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
            Biome ☃xxxxx = Biome.getBiome(☃xxxx);
            if (☃xxxx == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
               int ☃xxxxxx = ☃[☃xxx + 1 + (☃xx + 1 - 1) * (☃ + 2)];
               int ☃xxxxxxx = ☃[☃xxx + 1 + 1 + (☃xx + 1) * (☃ + 2)];
               int ☃xxxxxxxx = ☃[☃xxx + 1 - 1 + (☃xx + 1) * (☃ + 2)];
               int ☃xxxxxxxxx = ☃[☃xxx + 1 + (☃xx + 1 + 1) * (☃ + 2)];
               if (☃xxxxxx != Biome.getIdForBiome(Biomes.OCEAN)
                  && ☃xxxxxxx != Biome.getIdForBiome(Biomes.OCEAN)
                  && ☃xxxxxxxx != Biome.getIdForBiome(Biomes.OCEAN)
                  && ☃xxxxxxxxx != Biome.getIdForBiome(Biomes.OCEAN)) {
                  ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
               } else {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
               }
            } else if (☃xxxxx != null && ☃xxxxx.getBiomeClass() == BiomeJungle.class) {
               int ☃xxxxxx = ☃[☃xxx + 1 + (☃xx + 1 - 1) * (☃ + 2)];
               int ☃xxxxxxx = ☃[☃xxx + 1 + 1 + (☃xx + 1) * (☃ + 2)];
               int ☃xxxxxxxx = ☃[☃xxx + 1 - 1 + (☃xx + 1) * (☃ + 2)];
               int ☃xxxxxxxxx = ☃[☃xxx + 1 + (☃xx + 1 + 1) * (☃ + 2)];
               if (!this.isJungleCompatible(☃xxxxxx)
                  || !this.isJungleCompatible(☃xxxxxxx)
                  || !this.isJungleCompatible(☃xxxxxxxx)
                  || !this.isJungleCompatible(☃xxxxxxxxx)) {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
               } else if (!isBiomeOceanic(☃xxxxxx) && !isBiomeOceanic(☃xxxxxxx) && !isBiomeOceanic(☃xxxxxxxx) && !isBiomeOceanic(☃xxxxxxxxx)) {
                  ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
               } else {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.BEACH);
               }
            } else if (☃xxxx == Biome.getIdForBiome(Biomes.EXTREME_HILLS)
               || ☃xxxx == Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES)
               || ☃xxxx == Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE)) {
               this.replaceIfNeighborOcean(☃, ☃x, ☃xxx, ☃xx, ☃, ☃xxxx, Biome.getIdForBiome(Biomes.STONE_BEACH));
            } else if (☃xxxxx != null && ☃xxxxx.isSnowyBiome()) {
               this.replaceIfNeighborOcean(☃, ☃x, ☃xxx, ☃xx, ☃, ☃xxxx, Biome.getIdForBiome(Biomes.COLD_BEACH));
            } else if (☃xxxx == Biome.getIdForBiome(Biomes.MESA) || ☃xxxx == Biome.getIdForBiome(Biomes.MESA_ROCK)) {
               int ☃xxxxxx = ☃[☃xxx + 1 + (☃xx + 1 - 1) * (☃ + 2)];
               int ☃xxxxxxx = ☃[☃xxx + 1 + 1 + (☃xx + 1) * (☃ + 2)];
               int ☃xxxxxxxx = ☃[☃xxx + 1 - 1 + (☃xx + 1) * (☃ + 2)];
               int ☃xxxxxxxxx = ☃[☃xxx + 1 + (☃xx + 1 + 1) * (☃ + 2)];
               if (isBiomeOceanic(☃xxxxxx) || isBiomeOceanic(☃xxxxxxx) || isBiomeOceanic(☃xxxxxxxx) || isBiomeOceanic(☃xxxxxxxxx)) {
                  ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
               } else if (this.isMesa(☃xxxxxx) && this.isMesa(☃xxxxxxx) && this.isMesa(☃xxxxxxxx) && this.isMesa(☃xxxxxxxxx)) {
                  ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
               } else {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.DESERT);
               }
            } else if (☃xxxx != Biome.getIdForBiome(Biomes.OCEAN)
               && ☃xxxx != Biome.getIdForBiome(Biomes.DEEP_OCEAN)
               && ☃xxxx != Biome.getIdForBiome(Biomes.RIVER)
               && ☃xxxx != Biome.getIdForBiome(Biomes.SWAMPLAND)) {
               int ☃xxxxxx = ☃[☃xxx + 1 + (☃xx + 1 - 1) * (☃ + 2)];
               int ☃xxxxxxx = ☃[☃xxx + 1 + 1 + (☃xx + 1) * (☃ + 2)];
               int ☃xxxxxxxx = ☃[☃xxx + 1 - 1 + (☃xx + 1) * (☃ + 2)];
               int ☃xxxxxxxxx = ☃[☃xxx + 1 + (☃xx + 1 + 1) * (☃ + 2)];
               if (!isBiomeOceanic(☃xxxxxx) && !isBiomeOceanic(☃xxxxxxx) && !isBiomeOceanic(☃xxxxxxxx) && !isBiomeOceanic(☃xxxxxxxxx)) {
                  ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
               } else {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.BEACH);
               }
            } else {
               ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
            }
         }
      }

      return ☃x;
   }

   private void replaceIfNeighborOcean(int[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7) {
      if (isBiomeOceanic(☃)) {
         ☃[☃ + ☃ * ☃] = ☃;
      } else {
         int ☃ = ☃[☃ + 1 + (☃ + 1 - 1) * (☃ + 2)];
         int ☃x = ☃[☃ + 1 + 1 + (☃ + 1) * (☃ + 2)];
         int ☃xx = ☃[☃ + 1 - 1 + (☃ + 1) * (☃ + 2)];
         int ☃xxx = ☃[☃ + 1 + (☃ + 1 + 1) * (☃ + 2)];
         if (!isBiomeOceanic(☃) && !isBiomeOceanic(☃x) && !isBiomeOceanic(☃xx) && !isBiomeOceanic(☃xxx)) {
            ☃[☃ + ☃ * ☃] = ☃;
         } else {
            ☃[☃ + ☃ * ☃] = ☃;
         }
      }
   }

   private boolean isJungleCompatible(int var1) {
      return Biome.getBiome(☃) != null && Biome.getBiome(☃).getBiomeClass() == BiomeJungle.class
         ? true
         : ☃ == Biome.getIdForBiome(Biomes.JUNGLE_EDGE)
            || ☃ == Biome.getIdForBiome(Biomes.JUNGLE)
            || ☃ == Biome.getIdForBiome(Biomes.JUNGLE_HILLS)
            || ☃ == Biome.getIdForBiome(Biomes.FOREST)
            || ☃ == Biome.getIdForBiome(Biomes.TAIGA)
            || isBiomeOceanic(☃);
   }

   private boolean isMesa(int var1) {
      return Biome.getBiome(☃) instanceof BiomeMesa;
   }
}
