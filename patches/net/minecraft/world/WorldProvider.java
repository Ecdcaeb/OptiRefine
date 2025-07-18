package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.IChunkGenerator;

public abstract class WorldProvider {
   public static final float[] MOON_PHASE_FACTORS = new float[]{1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
   protected World world;
   private WorldType terrainType;
   private String generatorSettings;
   protected BiomeProvider biomeProvider;
   protected boolean doesWaterVaporize;
   protected boolean nether;
   protected boolean hasSkyLight;
   protected final float[] lightBrightnessTable = new float[16];
   private final float[] colorsSunriseSunset = new float[4];

   public final void setWorld(World var1) {
      this.world = ☃;
      this.terrainType = ☃.getWorldInfo().getTerrainType();
      this.generatorSettings = ☃.getWorldInfo().getGeneratorOptions();
      this.init();
      this.generateLightBrightnessTable();
   }

   protected void generateLightBrightnessTable() {
      float ☃ = 0.0F;

      for (int ☃x = 0; ☃x <= 15; ☃x++) {
         float ☃xx = 1.0F - ☃x / 15.0F;
         this.lightBrightnessTable[☃x] = (1.0F - ☃xx) / (☃xx * 3.0F + 1.0F) * 1.0F + 0.0F;
      }
   }

   protected void init() {
      this.hasSkyLight = true;
      WorldType ☃ = this.world.getWorldInfo().getTerrainType();
      if (☃ == WorldType.FLAT) {
         FlatGeneratorInfo ☃x = FlatGeneratorInfo.createFlatGeneratorFromString(this.world.getWorldInfo().getGeneratorOptions());
         this.biomeProvider = new BiomeProviderSingle(Biome.getBiome(☃x.getBiome(), Biomes.DEFAULT));
      } else if (☃ == WorldType.DEBUG_ALL_BLOCK_STATES) {
         this.biomeProvider = new BiomeProviderSingle(Biomes.PLAINS);
      } else {
         this.biomeProvider = new BiomeProvider(this.world.getWorldInfo());
      }
   }

   public IChunkGenerator createChunkGenerator() {
      if (this.terrainType == WorldType.FLAT) {
         return new ChunkGeneratorFlat(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings);
      } else if (this.terrainType == WorldType.DEBUG_ALL_BLOCK_STATES) {
         return new ChunkGeneratorDebug(this.world);
      } else {
         return this.terrainType == WorldType.CUSTOMIZED
            ? new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings)
            : new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings);
      }
   }

   public boolean canCoordinateBeSpawn(int var1, int var2) {
      BlockPos ☃ = new BlockPos(☃, 0, ☃);
      return this.world.getBiome(☃).ignorePlayerSpawnSuitability() ? true : this.world.getGroundAboveSeaLevel(☃).getBlock() == Blocks.GRASS;
   }

   public float calculateCelestialAngle(long var1, float var3) {
      int ☃ = (int)(☃ % 24000L);
      float ☃x = (☃ + ☃) / 24000.0F - 0.25F;
      if (☃x < 0.0F) {
         ☃x++;
      }

      if (☃x > 1.0F) {
         ☃x--;
      }

      float var7 = 1.0F - (float)((Math.cos(☃x * Math.PI) + 1.0) / 2.0);
      return ☃x + (var7 - ☃x) / 3.0F;
   }

   public int getMoonPhase(long var1) {
      return (int)(☃ / 24000L % 8L + 8L) % 8;
   }

   public boolean isSurfaceWorld() {
      return true;
   }

   @Nullable
   public float[] calcSunriseSunsetColors(float var1, float var2) {
      float ☃ = 0.4F;
      float ☃x = MathHelper.cos(☃ * (float) (Math.PI * 2)) - 0.0F;
      float ☃xx = -0.0F;
      if (☃x >= -0.4F && ☃x <= 0.4F) {
         float ☃xxx = (☃x - -0.0F) / 0.4F * 0.5F + 0.5F;
         float ☃xxxx = 1.0F - (1.0F - MathHelper.sin(☃xxx * (float) Math.PI)) * 0.99F;
         ☃xxxx *= ☃xxxx;
         this.colorsSunriseSunset[0] = ☃xxx * 0.3F + 0.7F;
         this.colorsSunriseSunset[1] = ☃xxx * ☃xxx * 0.7F + 0.2F;
         this.colorsSunriseSunset[2] = ☃xxx * ☃xxx * 0.0F + 0.2F;
         this.colorsSunriseSunset[3] = ☃xxxx;
         return this.colorsSunriseSunset;
      } else {
         return null;
      }
   }

   public Vec3d getFogColor(float var1, float var2) {
      float ☃ = MathHelper.cos(☃ * (float) (Math.PI * 2)) * 2.0F + 0.5F;
      ☃ = MathHelper.clamp(☃, 0.0F, 1.0F);
      float ☃x = 0.7529412F;
      float ☃xx = 0.84705883F;
      float ☃xxx = 1.0F;
      ☃x *= ☃ * 0.94F + 0.06F;
      ☃xx *= ☃ * 0.94F + 0.06F;
      ☃xxx *= ☃ * 0.91F + 0.09F;
      return new Vec3d(☃x, ☃xx, ☃xxx);
   }

   public boolean canRespawnHere() {
      return true;
   }

   public float getCloudHeight() {
      return 128.0F;
   }

   public boolean isSkyColored() {
      return true;
   }

   @Nullable
   public BlockPos getSpawnCoordinate() {
      return null;
   }

   public int getAverageGroundLevel() {
      return this.terrainType == WorldType.FLAT ? 4 : this.world.getSeaLevel() + 1;
   }

   public double getVoidFogYFactor() {
      return this.terrainType == WorldType.FLAT ? 1.0 : 0.03125;
   }

   public boolean doesXZShowFog(int var1, int var2) {
      return false;
   }

   public BiomeProvider getBiomeProvider() {
      return this.biomeProvider;
   }

   public boolean doesWaterVaporize() {
      return this.doesWaterVaporize;
   }

   public boolean hasSkyLight() {
      return this.hasSkyLight;
   }

   public boolean isNether() {
      return this.nether;
   }

   public float[] getLightBrightnessTable() {
      return this.lightBrightnessTable;
   }

   public WorldBorder createWorldBorder() {
      return new WorldBorder();
   }

   public void onPlayerAdded(EntityPlayerMP var1) {
   }

   public void onPlayerRemoved(EntityPlayerMP var1) {
   }

   public abstract DimensionType getDimensionType();

   public void onWorldSave() {
   }

   public void onWorldUpdateEntities() {
   }

   public boolean canDropChunk(int var1, int var2) {
      return true;
   }
}
