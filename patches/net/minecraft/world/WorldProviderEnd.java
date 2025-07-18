package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderEnd extends WorldProvider {
   private DragonFightManager dragonFightManager;

   @Override
   public void init() {
      this.biomeProvider = new BiomeProviderSingle(Biomes.SKY);
      NBTTagCompound ☃ = this.world.getWorldInfo().getDimensionData(DimensionType.THE_END);
      this.dragonFightManager = this.world instanceof WorldServer ? new DragonFightManager((WorldServer)this.world, ☃.getCompoundTag("DragonFight")) : null;
   }

   @Override
   public IChunkGenerator createChunkGenerator() {
      return new ChunkGeneratorEnd(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed(), this.getSpawnCoordinate());
   }

   @Override
   public float calculateCelestialAngle(long var1, float var3) {
      return 0.0F;
   }

   @Nullable
   @Override
   public float[] calcSunriseSunsetColors(float var1, float var2) {
      return null;
   }

   @Override
   public Vec3d getFogColor(float var1, float var2) {
      int ☃ = 10518688;
      float ☃x = MathHelper.cos(☃ * (float) (Math.PI * 2)) * 2.0F + 0.5F;
      ☃x = MathHelper.clamp(☃x, 0.0F, 1.0F);
      float ☃xx = 0.627451F;
      float ☃xxx = 0.5019608F;
      float ☃xxxx = 0.627451F;
      ☃xx *= ☃x * 0.0F + 0.15F;
      ☃xxx *= ☃x * 0.0F + 0.15F;
      ☃xxxx *= ☃x * 0.0F + 0.15F;
      return new Vec3d(☃xx, ☃xxx, ☃xxxx);
   }

   @Override
   public boolean isSkyColored() {
      return false;
   }

   @Override
   public boolean canRespawnHere() {
      return false;
   }

   @Override
   public boolean isSurfaceWorld() {
      return false;
   }

   @Override
   public float getCloudHeight() {
      return 8.0F;
   }

   @Override
   public boolean canCoordinateBeSpawn(int var1, int var2) {
      return this.world.getGroundAboveSeaLevel(new BlockPos(☃, 0, ☃)).getMaterial().blocksMovement();
   }

   @Override
   public BlockPos getSpawnCoordinate() {
      return new BlockPos(100, 50, 0);
   }

   @Override
   public int getAverageGroundLevel() {
      return 50;
   }

   @Override
   public boolean doesXZShowFog(int var1, int var2) {
      return false;
   }

   @Override
   public DimensionType getDimensionType() {
      return DimensionType.THE_END;
   }

   @Override
   public void onWorldSave() {
      NBTTagCompound ☃ = new NBTTagCompound();
      if (this.dragonFightManager != null) {
         ☃.setTag("DragonFight", this.dragonFightManager.getCompound());
      }

      this.world.getWorldInfo().setDimensionData(DimensionType.THE_END, ☃);
   }

   @Override
   public void onWorldUpdateEntities() {
      if (this.dragonFightManager != null) {
         this.dragonFightManager.tick();
      }
   }

   @Nullable
   public DragonFightManager getDragonFightManager() {
      return this.dragonFightManager;
   }
}
