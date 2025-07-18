package net.minecraft.world;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderHell extends WorldProvider {
   @Override
   public void init() {
      this.biomeProvider = new BiomeProviderSingle(Biomes.HELL);
      this.doesWaterVaporize = true;
      this.nether = true;
   }

   @Override
   public Vec3d getFogColor(float var1, float var2) {
      return new Vec3d(0.2F, 0.03F, 0.03F);
   }

   @Override
   protected void generateLightBrightnessTable() {
      float ☃ = 0.1F;

      for (int ☃x = 0; ☃x <= 15; ☃x++) {
         float ☃xx = 1.0F - ☃x / 15.0F;
         this.lightBrightnessTable[☃x] = (1.0F - ☃xx) / (☃xx * 3.0F + 1.0F) * 0.9F + 0.1F;
      }
   }

   @Override
   public IChunkGenerator createChunkGenerator() {
      return new ChunkGeneratorHell(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed());
   }

   @Override
   public boolean isSurfaceWorld() {
      return false;
   }

   @Override
   public boolean canCoordinateBeSpawn(int var1, int var2) {
      return false;
   }

   @Override
   public float calculateCelestialAngle(long var1, float var3) {
      return 0.5F;
   }

   @Override
   public boolean canRespawnHere() {
      return false;
   }

   @Override
   public boolean doesXZShowFog(int var1, int var2) {
      return true;
   }

   @Override
   public WorldBorder createWorldBorder() {
      return new WorldBorder() {
         @Override
         public double getCenterX() {
            return super.getCenterX() / 8.0;
         }

         @Override
         public double getCenterZ() {
            return super.getCenterZ() / 8.0;
         }
      };
   }

   @Override
   public DimensionType getDimensionType() {
      return DimensionType.NETHER;
   }
}
