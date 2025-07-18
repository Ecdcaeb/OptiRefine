package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenLayerHills extends GenLayer {
   private static final Logger LOGGER = LogManager.getLogger();
   private final GenLayer riverLayer;

   public GenLayerHills(long var1, GenLayer var3, GenLayer var4) {
      super(☃);
      this.parent = ☃;
      this.riverLayer = ☃;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int[] ☃ = this.parent.getInts(☃ - 1, ☃ - 1, ☃ + 2, ☃ + 2);
      int[] ☃x = this.riverLayer.getInts(☃ - 1, ☃ - 1, ☃ + 2, ☃ + 2);
      int[] ☃xx = IntCache.getIntCache(☃ * ☃);

      for (int ☃xxx = 0; ☃xxx < ☃; ☃xxx++) {
         for (int ☃xxxx = 0; ☃xxxx < ☃; ☃xxxx++) {
            this.initChunkSeed(☃xxxx + ☃, ☃xxx + ☃);
            int ☃xxxxx = ☃[☃xxxx + 1 + (☃xxx + 1) * (☃ + 2)];
            int ☃xxxxxx = ☃x[☃xxxx + 1 + (☃xxx + 1) * (☃ + 2)];
            boolean ☃xxxxxxx = (☃xxxxxx - 2) % 29 == 0;
            if (☃xxxxx > 255) {
               LOGGER.debug("old! {}", ☃xxxxx);
            }

            Biome ☃xxxxxxxx = Biome.getBiomeForId(☃xxxxx);
            boolean ☃xxxxxxxxx = ☃xxxxxxxx != null && ☃xxxxxxxx.isMutation();
            if (☃xxxxx != 0 && ☃xxxxxx >= 2 && (☃xxxxxx - 2) % 29 == 1 && !☃xxxxxxxxx) {
               Biome ☃xxxxxxxxxx = Biome.getMutationForBiome(☃xxxxxxxx);
               ☃xx[☃xxxx + ☃xxx * ☃] = ☃xxxxxxxxxx == null ? ☃xxxxx : Biome.getIdForBiome(☃xxxxxxxxxx);
            } else if (this.nextInt(3) != 0 && !☃xxxxxxx) {
               ☃xx[☃xxxx + ☃xxx * ☃] = ☃xxxxx;
            } else {
               Biome ☃xxxxxxxxxx = ☃xxxxxxxx;
               if (☃xxxxxxxx == Biomes.DESERT) {
                  ☃xxxxxxxxxx = Biomes.DESERT_HILLS;
               } else if (☃xxxxxxxx == Biomes.FOREST) {
                  ☃xxxxxxxxxx = Biomes.FOREST_HILLS;
               } else if (☃xxxxxxxx == Biomes.BIRCH_FOREST) {
                  ☃xxxxxxxxxx = Biomes.BIRCH_FOREST_HILLS;
               } else if (☃xxxxxxxx == Biomes.ROOFED_FOREST) {
                  ☃xxxxxxxxxx = Biomes.PLAINS;
               } else if (☃xxxxxxxx == Biomes.TAIGA) {
                  ☃xxxxxxxxxx = Biomes.TAIGA_HILLS;
               } else if (☃xxxxxxxx == Biomes.REDWOOD_TAIGA) {
                  ☃xxxxxxxxxx = Biomes.REDWOOD_TAIGA_HILLS;
               } else if (☃xxxxxxxx == Biomes.COLD_TAIGA) {
                  ☃xxxxxxxxxx = Biomes.COLD_TAIGA_HILLS;
               } else if (☃xxxxxxxx == Biomes.PLAINS) {
                  if (this.nextInt(3) == 0) {
                     ☃xxxxxxxxxx = Biomes.FOREST_HILLS;
                  } else {
                     ☃xxxxxxxxxx = Biomes.FOREST;
                  }
               } else if (☃xxxxxxxx == Biomes.ICE_PLAINS) {
                  ☃xxxxxxxxxx = Biomes.ICE_MOUNTAINS;
               } else if (☃xxxxxxxx == Biomes.JUNGLE) {
                  ☃xxxxxxxxxx = Biomes.JUNGLE_HILLS;
               } else if (☃xxxxxxxx == Biomes.OCEAN) {
                  ☃xxxxxxxxxx = Biomes.DEEP_OCEAN;
               } else if (☃xxxxxxxx == Biomes.EXTREME_HILLS) {
                  ☃xxxxxxxxxx = Biomes.EXTREME_HILLS_WITH_TREES;
               } else if (☃xxxxxxxx == Biomes.SAVANNA) {
                  ☃xxxxxxxxxx = Biomes.SAVANNA_PLATEAU;
               } else if (biomesEqualOrMesaPlateau(☃xxxxx, Biome.getIdForBiome(Biomes.MESA_ROCK))) {
                  ☃xxxxxxxxxx = Biomes.MESA;
               } else if (☃xxxxxxxx == Biomes.DEEP_OCEAN && this.nextInt(3) == 0) {
                  int ☃xxxxxxxxxxx = this.nextInt(2);
                  if (☃xxxxxxxxxxx == 0) {
                     ☃xxxxxxxxxx = Biomes.PLAINS;
                  } else {
                     ☃xxxxxxxxxx = Biomes.FOREST;
                  }
               }

               int ☃xxxxxxxxxxx = Biome.getIdForBiome(☃xxxxxxxxxx);
               if (☃xxxxxxx && ☃xxxxxxxxxxx != ☃xxxxx) {
                  Biome ☃xxxxxxxxxxxx = Biome.getMutationForBiome(☃xxxxxxxxxx);
                  ☃xxxxxxxxxxx = ☃xxxxxxxxxxxx == null ? ☃xxxxx : Biome.getIdForBiome(☃xxxxxxxxxxxx);
               }

               if (☃xxxxxxxxxxx == ☃xxxxx) {
                  ☃xx[☃xxxx + ☃xxx * ☃] = ☃xxxxx;
               } else {
                  int ☃xxxxxxxxxxxx = ☃[☃xxxx + 1 + (☃xxx + 0) * (☃ + 2)];
                  int ☃xxxxxxxxxxxxx = ☃[☃xxxx + 2 + (☃xxx + 1) * (☃ + 2)];
                  int ☃xxxxxxxxxxxxxx = ☃[☃xxxx + 0 + (☃xxx + 1) * (☃ + 2)];
                  int ☃xxxxxxxxxxxxxxx = ☃[☃xxxx + 1 + (☃xxx + 2) * (☃ + 2)];
                  int ☃xxxxxxxxxxxxxxxx = 0;
                  if (biomesEqualOrMesaPlateau(☃xxxxxxxxxxxx, ☃xxxxx)) {
                     ☃xxxxxxxxxxxxxxxx++;
                  }

                  if (biomesEqualOrMesaPlateau(☃xxxxxxxxxxxxx, ☃xxxxx)) {
                     ☃xxxxxxxxxxxxxxxx++;
                  }

                  if (biomesEqualOrMesaPlateau(☃xxxxxxxxxxxxxx, ☃xxxxx)) {
                     ☃xxxxxxxxxxxxxxxx++;
                  }

                  if (biomesEqualOrMesaPlateau(☃xxxxxxxxxxxxxxx, ☃xxxxx)) {
                     ☃xxxxxxxxxxxxxxxx++;
                  }

                  if (☃xxxxxxxxxxxxxxxx >= 3) {
                     ☃xx[☃xxxx + ☃xxx * ☃] = ☃xxxxxxxxxxx;
                  } else {
                     ☃xx[☃xxxx + ☃xxx * ☃] = ☃xxxxx;
                  }
               }
            }
         }
      }

      return ☃xx;
   }
}
