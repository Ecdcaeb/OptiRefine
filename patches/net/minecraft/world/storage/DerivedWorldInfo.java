package net.minecraft.world.storage;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class DerivedWorldInfo extends WorldInfo {
   private final WorldInfo delegate;

   public DerivedWorldInfo(WorldInfo var1) {
      this.delegate = ☃;
   }

   @Override
   public NBTTagCompound cloneNBTCompound(@Nullable NBTTagCompound var1) {
      return this.delegate.cloneNBTCompound(☃);
   }

   @Override
   public long getSeed() {
      return this.delegate.getSeed();
   }

   @Override
   public int getSpawnX() {
      return this.delegate.getSpawnX();
   }

   @Override
   public int getSpawnY() {
      return this.delegate.getSpawnY();
   }

   @Override
   public int getSpawnZ() {
      return this.delegate.getSpawnZ();
   }

   @Override
   public long getWorldTotalTime() {
      return this.delegate.getWorldTotalTime();
   }

   @Override
   public long getWorldTime() {
      return this.delegate.getWorldTime();
   }

   @Override
   public long getSizeOnDisk() {
      return this.delegate.getSizeOnDisk();
   }

   @Override
   public NBTTagCompound getPlayerNBTTagCompound() {
      return this.delegate.getPlayerNBTTagCompound();
   }

   @Override
   public String getWorldName() {
      return this.delegate.getWorldName();
   }

   @Override
   public int getSaveVersion() {
      return this.delegate.getSaveVersion();
   }

   @Override
   public long getLastTimePlayed() {
      return this.delegate.getLastTimePlayed();
   }

   @Override
   public boolean isThundering() {
      return this.delegate.isThundering();
   }

   @Override
   public int getThunderTime() {
      return this.delegate.getThunderTime();
   }

   @Override
   public boolean isRaining() {
      return this.delegate.isRaining();
   }

   @Override
   public int getRainTime() {
      return this.delegate.getRainTime();
   }

   @Override
   public GameType getGameType() {
      return this.delegate.getGameType();
   }

   @Override
   public void setSpawnX(int var1) {
   }

   @Override
   public void setSpawnY(int var1) {
   }

   @Override
   public void setSpawnZ(int var1) {
   }

   @Override
   public void setWorldTotalTime(long var1) {
   }

   @Override
   public void setWorldTime(long var1) {
   }

   @Override
   public void setSpawn(BlockPos var1) {
   }

   @Override
   public void setWorldName(String var1) {
   }

   @Override
   public void setSaveVersion(int var1) {
   }

   @Override
   public void setThundering(boolean var1) {
   }

   @Override
   public void setThunderTime(int var1) {
   }

   @Override
   public void setRaining(boolean var1) {
   }

   @Override
   public void setRainTime(int var1) {
   }

   @Override
   public boolean isMapFeaturesEnabled() {
      return this.delegate.isMapFeaturesEnabled();
   }

   @Override
   public boolean isHardcoreModeEnabled() {
      return this.delegate.isHardcoreModeEnabled();
   }

   @Override
   public WorldType getTerrainType() {
      return this.delegate.getTerrainType();
   }

   @Override
   public void setTerrainType(WorldType var1) {
   }

   @Override
   public boolean areCommandsAllowed() {
      return this.delegate.areCommandsAllowed();
   }

   @Override
   public void setAllowCommands(boolean var1) {
   }

   @Override
   public boolean isInitialized() {
      return this.delegate.isInitialized();
   }

   @Override
   public void setServerInitialized(boolean var1) {
   }

   @Override
   public GameRules getGameRulesInstance() {
      return this.delegate.getGameRulesInstance();
   }

   @Override
   public EnumDifficulty getDifficulty() {
      return this.delegate.getDifficulty();
   }

   @Override
   public void setDifficulty(EnumDifficulty var1) {
   }

   @Override
   public boolean isDifficultyLocked() {
      return this.delegate.isDifficultyLocked();
   }

   @Override
   public void setDifficultyLocked(boolean var1) {
   }

   @Override
   public void setDimensionData(DimensionType var1, NBTTagCompound var2) {
      this.delegate.setDimensionData(☃, ☃);
   }

   @Override
   public NBTTagCompound getDimensionData(DimensionType var1) {
      return this.delegate.getDimensionData(☃);
   }
}
