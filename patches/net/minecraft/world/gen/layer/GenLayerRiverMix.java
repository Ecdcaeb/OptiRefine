package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerRiverMix extends GenLayer {
   private final GenLayer biomePatternGeneratorChain;
   private final GenLayer riverPatternGeneratorChain;

   public GenLayerRiverMix(long var1, GenLayer var3, GenLayer var4) {
      super(☃);
      this.biomePatternGeneratorChain = ☃;
      this.riverPatternGeneratorChain = ☃;
   }

   @Override
   public void initWorldGenSeed(long var1) {
      this.biomePatternGeneratorChain.initWorldGenSeed(☃);
      this.riverPatternGeneratorChain.initWorldGenSeed(☃);
      super.initWorldGenSeed(☃);
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int[] ☃ = this.biomePatternGeneratorChain.getInts(☃, ☃, ☃, ☃);
      int[] ☃x = this.riverPatternGeneratorChain.getInts(☃, ☃, ☃, ☃);
      int[] ☃xx = IntCache.getIntCache(☃ * ☃);

      for (int ☃xxx = 0; ☃xxx < ☃ * ☃; ☃xxx++) {
         if (☃[☃xxx] == Biome.getIdForBiome(Biomes.OCEAN) || ☃[☃xxx] == Biome.getIdForBiome(Biomes.DEEP_OCEAN)) {
            ☃xx[☃xxx] = ☃[☃xxx];
         } else if (☃x[☃xxx] == Biome.getIdForBiome(Biomes.RIVER)) {
            if (☃[☃xxx] == Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
               ☃xx[☃xxx] = Biome.getIdForBiome(Biomes.FROZEN_RIVER);
            } else if (☃[☃xxx] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND) && ☃[☃xxx] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE)) {
               ☃xx[☃xxx] = ☃x[☃xxx] & 0xFF;
            } else {
               ☃xx[☃xxx] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
            }
         } else {
            ☃xx[☃xxx] = ☃[☃xxx];
         }
      }

      return ☃xx;
   }
}
