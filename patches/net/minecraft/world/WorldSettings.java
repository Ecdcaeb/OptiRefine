package net.minecraft.world;

import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings {
   private final long seed;
   private final GameType gameType;
   private final boolean mapFeaturesEnabled;
   private final boolean hardcoreEnabled;
   private final WorldType terrainType;
   private boolean commandsAllowed;
   private boolean bonusChestEnabled;
   private String generatorOptions = "";

   public WorldSettings(long var1, GameType var3, boolean var4, boolean var5, WorldType var6) {
      this.seed = ☃;
      this.gameType = ☃;
      this.mapFeaturesEnabled = ☃;
      this.hardcoreEnabled = ☃;
      this.terrainType = ☃;
   }

   public WorldSettings(WorldInfo var1) {
      this(☃.getSeed(), ☃.getGameType(), ☃.isMapFeaturesEnabled(), ☃.isHardcoreModeEnabled(), ☃.getTerrainType());
   }

   public WorldSettings enableBonusChest() {
      this.bonusChestEnabled = true;
      return this;
   }

   public WorldSettings enableCommands() {
      this.commandsAllowed = true;
      return this;
   }

   public WorldSettings setGeneratorOptions(String var1) {
      this.generatorOptions = ☃;
      return this;
   }

   public boolean isBonusChestEnabled() {
      return this.bonusChestEnabled;
   }

   public long getSeed() {
      return this.seed;
   }

   public GameType getGameType() {
      return this.gameType;
   }

   public boolean getHardcoreEnabled() {
      return this.hardcoreEnabled;
   }

   public boolean isMapFeaturesEnabled() {
      return this.mapFeaturesEnabled;
   }

   public WorldType getTerrainType() {
      return this.terrainType;
   }

   public boolean areCommandsAllowed() {
      return this.commandsAllowed;
   }

   public static GameType getGameTypeById(int var0) {
      return GameType.getByID(☃);
   }

   public String getGeneratorOptions() {
      return this.generatorOptions;
   }
}
