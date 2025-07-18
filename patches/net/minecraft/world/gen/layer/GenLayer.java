package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;

public abstract class GenLayer {
   private long worldGenSeed;
   protected GenLayer parent;
   private long chunkSeed;
   protected long baseSeed;

   public static GenLayer[] initializeAllBiomeGenerators(long var0, WorldType var2, ChunkGeneratorSettings var3) {
      GenLayer ☃ = new GenLayerIsland(1L);
      ☃ = new GenLayerFuzzyZoom(2000L, ☃);
      ☃ = new GenLayerAddIsland(1L, ☃);
      ☃ = new GenLayerZoom(2001L, ☃);
      ☃ = new GenLayerAddIsland(2L, ☃);
      ☃ = new GenLayerAddIsland(50L, ☃);
      ☃ = new GenLayerAddIsland(70L, ☃);
      ☃ = new GenLayerRemoveTooMuchOcean(2L, ☃);
      ☃ = new GenLayerAddSnow(2L, ☃);
      ☃ = new GenLayerAddIsland(3L, ☃);
      ☃ = new GenLayerEdge(2L, ☃, GenLayerEdge.Mode.COOL_WARM);
      ☃ = new GenLayerEdge(2L, ☃, GenLayerEdge.Mode.HEAT_ICE);
      ☃ = new GenLayerEdge(3L, ☃, GenLayerEdge.Mode.SPECIAL);
      ☃ = new GenLayerZoom(2002L, ☃);
      ☃ = new GenLayerZoom(2003L, ☃);
      ☃ = new GenLayerAddIsland(4L, ☃);
      ☃ = new GenLayerAddMushroomIsland(5L, ☃);
      ☃ = new GenLayerDeepOcean(4L, ☃);
      ☃ = GenLayerZoom.magnify(1000L, ☃, 0);
      int ☃x = 4;
      int ☃xx = ☃x;
      if (☃ != null) {
         ☃x = ☃.biomeSize;
         ☃xx = ☃.riverSize;
      }

      if (☃ == WorldType.LARGE_BIOMES) {
         ☃x = 6;
      }

      GenLayer var7 = GenLayerZoom.magnify(1000L, ☃, 0);
      GenLayer var30 = new GenLayerRiverInit(100L, var7);
      GenLayer var8 = new GenLayerBiome(200L, ☃, ☃, ☃);
      GenLayer var35 = GenLayerZoom.magnify(1000L, var8, 2);
      GenLayer var36 = new GenLayerBiomeEdge(1000L, var35);
      GenLayer var9 = GenLayerZoom.magnify(1000L, var30, 2);
      GenLayer var37 = new GenLayerHills(1000L, var36, var9);
      var7 = GenLayerZoom.magnify(1000L, var30, 2);
      var7 = GenLayerZoom.magnify(1000L, var7, ☃xx);
      GenLayer var33 = new GenLayerRiver(1L, var7);
      GenLayer var34 = new GenLayerSmooth(1000L, var33);
      var8 = new GenLayerRareBiome(1001L, var37);

      for (int ☃xxx = 0; ☃xxx < ☃x; ☃xxx++) {
         var8 = new GenLayerZoom(1000 + ☃xxx, var8);
         if (☃xxx == 0) {
            var8 = new GenLayerAddIsland(3L, var8);
         }

         if (☃xxx == 1 || ☃x == 1) {
            var8 = new GenLayerShore(1000L, var8);
         }
      }

      GenLayer var39 = new GenLayerSmooth(1000L, var8);
      GenLayer var40 = new GenLayerRiverMix(100L, var39, var34);
      GenLayer ☃xxx = new GenLayerVoronoiZoom(10L, var40);
      var40.initWorldGenSeed(☃);
      ☃xxx.initWorldGenSeed(☃);
      return new GenLayer[]{var40, ☃xxx, var40};
   }

   public GenLayer(long var1) {
      this.baseSeed = ☃;
      this.baseSeed = this.baseSeed * (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
      this.baseSeed += ☃;
      this.baseSeed = this.baseSeed * (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
      this.baseSeed += ☃;
      this.baseSeed = this.baseSeed * (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
      this.baseSeed += ☃;
   }

   public void initWorldGenSeed(long var1) {
      this.worldGenSeed = ☃;
      if (this.parent != null) {
         this.parent.initWorldGenSeed(☃);
      }

      this.worldGenSeed = this.worldGenSeed * (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
      this.worldGenSeed = this.worldGenSeed + this.baseSeed;
      this.worldGenSeed = this.worldGenSeed * (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
      this.worldGenSeed = this.worldGenSeed + this.baseSeed;
      this.worldGenSeed = this.worldGenSeed * (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
      this.worldGenSeed = this.worldGenSeed + this.baseSeed;
   }

   public void initChunkSeed(long var1, long var3) {
      this.chunkSeed = this.worldGenSeed;
      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed += ☃;
      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed += ☃;
      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed += ☃;
      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed += ☃;
   }

   protected int nextInt(int var1) {
      int ☃ = (int)((this.chunkSeed >> 24) % ☃);
      if (☃ < 0) {
         ☃ += ☃;
      }

      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed = this.chunkSeed + this.worldGenSeed;
      return ☃;
   }

   public abstract int[] getInts(int var1, int var2, int var3, int var4);

   protected static boolean biomesEqualOrMesaPlateau(int var0, int var1) {
      if (☃ == ☃) {
         return true;
      } else {
         Biome ☃ = Biome.getBiome(☃);
         Biome ☃x = Biome.getBiome(☃);
         if (☃ == null || ☃x == null) {
            return false;
         } else {
            return ☃ != Biomes.MESA_ROCK && ☃ != Biomes.MESA_CLEAR_ROCK
               ? ☃ == ☃x || ☃.getBiomeClass() == ☃x.getBiomeClass()
               : ☃x == Biomes.MESA_ROCK || ☃x == Biomes.MESA_CLEAR_ROCK;
         }
      }
   }

   protected static boolean isBiomeOceanic(int var0) {
      Biome ☃ = Biome.getBiome(☃);
      return ☃ == Biomes.OCEAN || ☃ == Biomes.DEEP_OCEAN || ☃ == Biomes.FROZEN_OCEAN;
   }

   protected int selectRandom(int... var1) {
      return ☃[this.nextInt(☃.length)];
   }

   protected int selectModeOrRandom(int var1, int var2, int var3, int var4) {
      if (☃ == ☃ && ☃ == ☃) {
         return ☃;
      } else if (☃ == ☃ && ☃ == ☃) {
         return ☃;
      } else if (☃ == ☃ && ☃ == ☃) {
         return ☃;
      } else if (☃ == ☃ && ☃ == ☃) {
         return ☃;
      } else if (☃ == ☃ && ☃ != ☃) {
         return ☃;
      } else if (☃ == ☃ && ☃ != ☃) {
         return ☃;
      } else if (☃ == ☃ && ☃ != ☃) {
         return ☃;
      } else if (☃ == ☃ && ☃ != ☃) {
         return ☃;
      } else if (☃ == ☃ && ☃ != ☃) {
         return ☃;
      } else {
         return ☃ == ☃ && ☃ != ☃ ? ☃ : this.selectRandom(☃, ☃, ☃, ☃);
      }
   }
}
