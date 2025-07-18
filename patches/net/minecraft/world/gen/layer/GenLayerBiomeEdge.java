package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerBiomeEdge extends GenLayer {
   public GenLayerBiomeEdge(long var1, GenLayer var3) {
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
            if (!this.replaceBiomeEdgeIfNecessary(
                  ☃, ☃x, ☃xxx, ☃xx, ☃, ☃xxxx, Biome.getIdForBiome(Biomes.EXTREME_HILLS), Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE)
               )
               && !this.replaceBiomeEdge(☃, ☃x, ☃xxx, ☃xx, ☃, ☃xxxx, Biome.getIdForBiome(Biomes.MESA_ROCK), Biome.getIdForBiome(Biomes.MESA))
               && !this.replaceBiomeEdge(☃, ☃x, ☃xxx, ☃xx, ☃, ☃xxxx, Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK), Biome.getIdForBiome(Biomes.MESA))
               && !this.replaceBiomeEdge(☃, ☃x, ☃xxx, ☃xx, ☃, ☃xxxx, Biome.getIdForBiome(Biomes.REDWOOD_TAIGA), Biome.getIdForBiome(Biomes.TAIGA))) {
               if (☃xxxx == Biome.getIdForBiome(Biomes.DESERT)) {
                  int ☃xxxxx = ☃[☃xxx + 1 + (☃xx + 1 - 1) * (☃ + 2)];
                  int ☃xxxxxx = ☃[☃xxx + 1 + 1 + (☃xx + 1) * (☃ + 2)];
                  int ☃xxxxxxx = ☃[☃xxx + 1 - 1 + (☃xx + 1) * (☃ + 2)];
                  int ☃xxxxxxxx = ☃[☃xxx + 1 + (☃xx + 1 + 1) * (☃ + 2)];
                  if (☃xxxxx != Biome.getIdForBiome(Biomes.ICE_PLAINS)
                     && ☃xxxxxx != Biome.getIdForBiome(Biomes.ICE_PLAINS)
                     && ☃xxxxxxx != Biome.getIdForBiome(Biomes.ICE_PLAINS)
                     && ☃xxxxxxxx != Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                     ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
                  } else {
                     ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES);
                  }
               } else if (☃xxxx == Biome.getIdForBiome(Biomes.SWAMPLAND)) {
                  int ☃xxxxx = ☃[☃xxx + 1 + (☃xx + 1 - 1) * (☃ + 2)];
                  int ☃xxxxxx = ☃[☃xxx + 1 + 1 + (☃xx + 1) * (☃ + 2)];
                  int ☃xxxxxxx = ☃[☃xxx + 1 - 1 + (☃xx + 1) * (☃ + 2)];
                  int ☃xxxxxxxx = ☃[☃xxx + 1 + (☃xx + 1 + 1) * (☃ + 2)];
                  if (☃xxxxx == Biome.getIdForBiome(Biomes.DESERT)
                     || ☃xxxxxx == Biome.getIdForBiome(Biomes.DESERT)
                     || ☃xxxxxxx == Biome.getIdForBiome(Biomes.DESERT)
                     || ☃xxxxxxxx == Biome.getIdForBiome(Biomes.DESERT)
                     || ☃xxxxx == Biome.getIdForBiome(Biomes.COLD_TAIGA)
                     || ☃xxxxxx == Biome.getIdForBiome(Biomes.COLD_TAIGA)
                     || ☃xxxxxxx == Biome.getIdForBiome(Biomes.COLD_TAIGA)
                     || ☃xxxxxxxx == Biome.getIdForBiome(Biomes.COLD_TAIGA)
                     || ☃xxxxx == Biome.getIdForBiome(Biomes.ICE_PLAINS)
                     || ☃xxxxxx == Biome.getIdForBiome(Biomes.ICE_PLAINS)
                     || ☃xxxxxxx == Biome.getIdForBiome(Biomes.ICE_PLAINS)
                     || ☃xxxxxxxx == Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                     ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.PLAINS);
                  } else if (☃xxxxx != Biome.getIdForBiome(Biomes.JUNGLE)
                     && ☃xxxxxxxx != Biome.getIdForBiome(Biomes.JUNGLE)
                     && ☃xxxxxx != Biome.getIdForBiome(Biomes.JUNGLE)
                     && ☃xxxxxxx != Biome.getIdForBiome(Biomes.JUNGLE)) {
                     ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
                  } else {
                     ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
                  }
               } else {
                  ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
               }
            }
         }
      }

      return ☃x;
   }

   private boolean replaceBiomeEdgeIfNecessary(int[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      if (!biomesEqualOrMesaPlateau(☃, ☃)) {
         return false;
      } else {
         int ☃ = ☃[☃ + 1 + (☃ + 1 - 1) * (☃ + 2)];
         int ☃x = ☃[☃ + 1 + 1 + (☃ + 1) * (☃ + 2)];
         int ☃xx = ☃[☃ + 1 - 1 + (☃ + 1) * (☃ + 2)];
         int ☃xxx = ☃[☃ + 1 + (☃ + 1 + 1) * (☃ + 2)];
         if (this.canBiomesBeNeighbors(☃, ☃) && this.canBiomesBeNeighbors(☃x, ☃) && this.canBiomesBeNeighbors(☃xx, ☃) && this.canBiomesBeNeighbors(☃xxx, ☃)) {
            ☃[☃ + ☃ * ☃] = ☃;
         } else {
            ☃[☃ + ☃ * ☃] = ☃;
         }

         return true;
      }
   }

   private boolean replaceBiomeEdge(int[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      if (☃ != ☃) {
         return false;
      } else {
         int ☃ = ☃[☃ + 1 + (☃ + 1 - 1) * (☃ + 2)];
         int ☃x = ☃[☃ + 1 + 1 + (☃ + 1) * (☃ + 2)];
         int ☃xx = ☃[☃ + 1 - 1 + (☃ + 1) * (☃ + 2)];
         int ☃xxx = ☃[☃ + 1 + (☃ + 1 + 1) * (☃ + 2)];
         if (biomesEqualOrMesaPlateau(☃, ☃) && biomesEqualOrMesaPlateau(☃x, ☃) && biomesEqualOrMesaPlateau(☃xx, ☃) && biomesEqualOrMesaPlateau(☃xxx, ☃)) {
            ☃[☃ + ☃ * ☃] = ☃;
         } else {
            ☃[☃ + ☃ * ☃] = ☃;
         }

         return true;
      }
   }

   private boolean canBiomesBeNeighbors(int var1, int var2) {
      if (biomesEqualOrMesaPlateau(☃, ☃)) {
         return true;
      } else {
         Biome ☃ = Biome.getBiome(☃);
         Biome ☃x = Biome.getBiome(☃);
         if (☃ != null && ☃x != null) {
            Biome.TempCategory ☃xx = ☃.getTempCategory();
            Biome.TempCategory ☃xxx = ☃x.getTempCategory();
            return ☃xx == ☃xxx || ☃xx == Biome.TempCategory.MEDIUM || ☃xxx == Biome.TempCategory.MEDIUM;
         } else {
            return false;
         }
      }
   }
}
