package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;

public class GenLayerBiome extends GenLayer {
   private Biome[] warmBiomes = new Biome[]{Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA, Biomes.PLAINS};
   private final Biome[] mediumBiomes = new Biome[]{
      Biomes.FOREST, Biomes.ROOFED_FOREST, Biomes.EXTREME_HILLS, Biomes.PLAINS, Biomes.BIRCH_FOREST, Biomes.SWAMPLAND
   };
   private final Biome[] coldBiomes = new Biome[]{Biomes.FOREST, Biomes.EXTREME_HILLS, Biomes.TAIGA, Biomes.PLAINS};
   private final Biome[] iceBiomes = new Biome[]{Biomes.ICE_PLAINS, Biomes.ICE_PLAINS, Biomes.ICE_PLAINS, Biomes.COLD_TAIGA};
   private final ChunkGeneratorSettings settings;

   public GenLayerBiome(long var1, GenLayer var3, WorldType var4, ChunkGeneratorSettings var5) {
      super(☃);
      this.parent = ☃;
      if (☃ == WorldType.DEFAULT_1_1) {
         this.warmBiomes = new Biome[]{Biomes.DESERT, Biomes.FOREST, Biomes.EXTREME_HILLS, Biomes.SWAMPLAND, Biomes.PLAINS, Biomes.TAIGA};
         this.settings = null;
      } else {
         this.settings = ☃;
      }
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int[] ☃ = this.parent.getInts(☃, ☃, ☃, ☃);
      int[] ☃x = IntCache.getIntCache(☃ * ☃);

      for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < ☃; ☃xxx++) {
            this.initChunkSeed(☃xxx + ☃, ☃xx + ☃);
            int ☃xxxx = ☃[☃xxx + ☃xx * ☃];
            int ☃xxxxx = (☃xxxx & 3840) >> 8;
            ☃xxxx &= -3841;
            if (this.settings != null && this.settings.fixedBiome >= 0) {
               ☃x[☃xxx + ☃xx * ☃] = this.settings.fixedBiome;
            } else if (isBiomeOceanic(☃xxxx)) {
               ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
            } else if (☃xxxx == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
               ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
            } else if (☃xxxx == 1) {
               if (☃xxxxx > 0) {
                  if (this.nextInt(3) == 0) {
                     ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK);
                  } else {
                     ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.MESA_ROCK);
                  }
               } else {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(this.warmBiomes[this.nextInt(this.warmBiomes.length)]);
               }
            } else if (☃xxxx == 2) {
               if (☃xxxxx > 0) {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.JUNGLE);
               } else {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(this.mediumBiomes[this.nextInt(this.mediumBiomes.length)]);
               }
            } else if (☃xxxx == 3) {
               if (☃xxxxx > 0) {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.REDWOOD_TAIGA);
               } else {
                  ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(this.coldBiomes[this.nextInt(this.coldBiomes.length)]);
               }
            } else if (☃xxxx == 4) {
               ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(this.iceBiomes[this.nextInt(this.iceBiomes.length)]);
            } else {
               ☃x[☃xxx + ☃xx * ☃] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
            }
         }
      }

      return ☃x;
   }
}
