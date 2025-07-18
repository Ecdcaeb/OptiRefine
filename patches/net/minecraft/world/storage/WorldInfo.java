package net.minecraft.world.storage;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class WorldInfo {
   private String versionName;
   private int versionId;
   private boolean versionSnapshot;
   public static final EnumDifficulty DEFAULT_DIFFICULTY = EnumDifficulty.NORMAL;
   private long randomSeed;
   private WorldType terrainType = WorldType.DEFAULT;
   private String generatorOptions = "";
   private int spawnX;
   private int spawnY;
   private int spawnZ;
   private long totalTime;
   private long worldTime;
   private long lastTimePlayed;
   private long sizeOnDisk;
   private NBTTagCompound playerTag;
   private int dimension;
   private String levelName;
   private int saveVersion;
   private int cleanWeatherTime;
   private boolean raining;
   private int rainTime;
   private boolean thundering;
   private int thunderTime;
   private GameType gameType;
   private boolean mapFeaturesEnabled;
   private boolean hardcore;
   private boolean allowCommands;
   private boolean initialized;
   private EnumDifficulty difficulty;
   private boolean difficultyLocked;
   private double borderCenterX;
   private double borderCenterZ;
   private double borderSize = 6.0E7;
   private long borderSizeLerpTime;
   private double borderSizeLerpTarget;
   private double borderSafeZone = 5.0;
   private double borderDamagePerBlock = 0.2;
   private int borderWarningDistance = 5;
   private int borderWarningTime = 15;
   private final Map<DimensionType, NBTTagCompound> dimensionData = Maps.newEnumMap(DimensionType.class);
   private GameRules gameRules = new GameRules();

   protected WorldInfo() {
   }

   public static void registerFixes(DataFixer var0) {
      ☃.registerWalker(FixTypes.LEVEL, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            if (☃.hasKey("Player", 10)) {
               ☃.setTag("Player", ☃.process(FixTypes.PLAYER, ☃.getCompoundTag("Player"), ☃));
            }

            return ☃;
         }
      });
   }

   public WorldInfo(NBTTagCompound var1) {
      if (☃.hasKey("Version", 10)) {
         NBTTagCompound ☃ = ☃.getCompoundTag("Version");
         this.versionName = ☃.getString("Name");
         this.versionId = ☃.getInteger("Id");
         this.versionSnapshot = ☃.getBoolean("Snapshot");
      }

      this.randomSeed = ☃.getLong("RandomSeed");
      if (☃.hasKey("generatorName", 8)) {
         String ☃ = ☃.getString("generatorName");
         this.terrainType = WorldType.byName(☃);
         if (this.terrainType == null) {
            this.terrainType = WorldType.DEFAULT;
         } else if (this.terrainType.isVersioned()) {
            int ☃x = 0;
            if (☃.hasKey("generatorVersion", 99)) {
               ☃x = ☃.getInteger("generatorVersion");
            }

            this.terrainType = this.terrainType.getWorldTypeForGeneratorVersion(☃x);
         }

         if (☃.hasKey("generatorOptions", 8)) {
            this.generatorOptions = ☃.getString("generatorOptions");
         }
      }

      this.gameType = GameType.getByID(☃.getInteger("GameType"));
      if (☃.hasKey("MapFeatures", 99)) {
         this.mapFeaturesEnabled = ☃.getBoolean("MapFeatures");
      } else {
         this.mapFeaturesEnabled = true;
      }

      this.spawnX = ☃.getInteger("SpawnX");
      this.spawnY = ☃.getInteger("SpawnY");
      this.spawnZ = ☃.getInteger("SpawnZ");
      this.totalTime = ☃.getLong("Time");
      if (☃.hasKey("DayTime", 99)) {
         this.worldTime = ☃.getLong("DayTime");
      } else {
         this.worldTime = this.totalTime;
      }

      this.lastTimePlayed = ☃.getLong("LastPlayed");
      this.sizeOnDisk = ☃.getLong("SizeOnDisk");
      this.levelName = ☃.getString("LevelName");
      this.saveVersion = ☃.getInteger("version");
      this.cleanWeatherTime = ☃.getInteger("clearWeatherTime");
      this.rainTime = ☃.getInteger("rainTime");
      this.raining = ☃.getBoolean("raining");
      this.thunderTime = ☃.getInteger("thunderTime");
      this.thundering = ☃.getBoolean("thundering");
      this.hardcore = ☃.getBoolean("hardcore");
      if (☃.hasKey("initialized", 99)) {
         this.initialized = ☃.getBoolean("initialized");
      } else {
         this.initialized = true;
      }

      if (☃.hasKey("allowCommands", 99)) {
         this.allowCommands = ☃.getBoolean("allowCommands");
      } else {
         this.allowCommands = this.gameType == GameType.CREATIVE;
      }

      if (☃.hasKey("Player", 10)) {
         this.playerTag = ☃.getCompoundTag("Player");
         this.dimension = this.playerTag.getInteger("Dimension");
      }

      if (☃.hasKey("GameRules", 10)) {
         this.gameRules.readFromNBT(☃.getCompoundTag("GameRules"));
      }

      if (☃.hasKey("Difficulty", 99)) {
         this.difficulty = EnumDifficulty.byId(☃.getByte("Difficulty"));
      }

      if (☃.hasKey("DifficultyLocked", 1)) {
         this.difficultyLocked = ☃.getBoolean("DifficultyLocked");
      }

      if (☃.hasKey("BorderCenterX", 99)) {
         this.borderCenterX = ☃.getDouble("BorderCenterX");
      }

      if (☃.hasKey("BorderCenterZ", 99)) {
         this.borderCenterZ = ☃.getDouble("BorderCenterZ");
      }

      if (☃.hasKey("BorderSize", 99)) {
         this.borderSize = ☃.getDouble("BorderSize");
      }

      if (☃.hasKey("BorderSizeLerpTime", 99)) {
         this.borderSizeLerpTime = ☃.getLong("BorderSizeLerpTime");
      }

      if (☃.hasKey("BorderSizeLerpTarget", 99)) {
         this.borderSizeLerpTarget = ☃.getDouble("BorderSizeLerpTarget");
      }

      if (☃.hasKey("BorderSafeZone", 99)) {
         this.borderSafeZone = ☃.getDouble("BorderSafeZone");
      }

      if (☃.hasKey("BorderDamagePerBlock", 99)) {
         this.borderDamagePerBlock = ☃.getDouble("BorderDamagePerBlock");
      }

      if (☃.hasKey("BorderWarningBlocks", 99)) {
         this.borderWarningDistance = ☃.getInteger("BorderWarningBlocks");
      }

      if (☃.hasKey("BorderWarningTime", 99)) {
         this.borderWarningTime = ☃.getInteger("BorderWarningTime");
      }

      if (☃.hasKey("DimensionData", 10)) {
         NBTTagCompound ☃x = ☃.getCompoundTag("DimensionData");

         for (String ☃xx : ☃x.getKeySet()) {
            this.dimensionData.put(DimensionType.getById(Integer.parseInt(☃xx)), ☃x.getCompoundTag(☃xx));
         }
      }
   }

   public WorldInfo(WorldSettings var1, String var2) {
      this.populateFromWorldSettings(☃);
      this.levelName = ☃;
      this.difficulty = DEFAULT_DIFFICULTY;
      this.initialized = false;
   }

   public void populateFromWorldSettings(WorldSettings var1) {
      this.randomSeed = ☃.getSeed();
      this.gameType = ☃.getGameType();
      this.mapFeaturesEnabled = ☃.isMapFeaturesEnabled();
      this.hardcore = ☃.getHardcoreEnabled();
      this.terrainType = ☃.getTerrainType();
      this.generatorOptions = ☃.getGeneratorOptions();
      this.allowCommands = ☃.areCommandsAllowed();
   }

   public WorldInfo(WorldInfo var1) {
      this.randomSeed = ☃.randomSeed;
      this.terrainType = ☃.terrainType;
      this.generatorOptions = ☃.generatorOptions;
      this.gameType = ☃.gameType;
      this.mapFeaturesEnabled = ☃.mapFeaturesEnabled;
      this.spawnX = ☃.spawnX;
      this.spawnY = ☃.spawnY;
      this.spawnZ = ☃.spawnZ;
      this.totalTime = ☃.totalTime;
      this.worldTime = ☃.worldTime;
      this.lastTimePlayed = ☃.lastTimePlayed;
      this.sizeOnDisk = ☃.sizeOnDisk;
      this.playerTag = ☃.playerTag;
      this.dimension = ☃.dimension;
      this.levelName = ☃.levelName;
      this.saveVersion = ☃.saveVersion;
      this.rainTime = ☃.rainTime;
      this.raining = ☃.raining;
      this.thunderTime = ☃.thunderTime;
      this.thundering = ☃.thundering;
      this.hardcore = ☃.hardcore;
      this.allowCommands = ☃.allowCommands;
      this.initialized = ☃.initialized;
      this.gameRules = ☃.gameRules;
      this.difficulty = ☃.difficulty;
      this.difficultyLocked = ☃.difficultyLocked;
      this.borderCenterX = ☃.borderCenterX;
      this.borderCenterZ = ☃.borderCenterZ;
      this.borderSize = ☃.borderSize;
      this.borderSizeLerpTime = ☃.borderSizeLerpTime;
      this.borderSizeLerpTarget = ☃.borderSizeLerpTarget;
      this.borderSafeZone = ☃.borderSafeZone;
      this.borderDamagePerBlock = ☃.borderDamagePerBlock;
      this.borderWarningTime = ☃.borderWarningTime;
      this.borderWarningDistance = ☃.borderWarningDistance;
   }

   public NBTTagCompound cloneNBTCompound(@Nullable NBTTagCompound var1) {
      if (☃ == null) {
         ☃ = this.playerTag;
      }

      NBTTagCompound ☃ = new NBTTagCompound();
      this.updateTagCompound(☃, ☃);
      return ☃;
   }

   private void updateTagCompound(NBTTagCompound var1, NBTTagCompound var2) {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setString("Name", "1.12.2");
      ☃.setInteger("Id", 1343);
      ☃.setBoolean("Snapshot", false);
      ☃.setTag("Version", ☃);
      ☃.setInteger("DataVersion", 1343);
      ☃.setLong("RandomSeed", this.randomSeed);
      ☃.setString("generatorName", this.terrainType.getName());
      ☃.setInteger("generatorVersion", this.terrainType.getVersion());
      ☃.setString("generatorOptions", this.generatorOptions);
      ☃.setInteger("GameType", this.gameType.getID());
      ☃.setBoolean("MapFeatures", this.mapFeaturesEnabled);
      ☃.setInteger("SpawnX", this.spawnX);
      ☃.setInteger("SpawnY", this.spawnY);
      ☃.setInteger("SpawnZ", this.spawnZ);
      ☃.setLong("Time", this.totalTime);
      ☃.setLong("DayTime", this.worldTime);
      ☃.setLong("SizeOnDisk", this.sizeOnDisk);
      ☃.setLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());
      ☃.setString("LevelName", this.levelName);
      ☃.setInteger("version", this.saveVersion);
      ☃.setInteger("clearWeatherTime", this.cleanWeatherTime);
      ☃.setInteger("rainTime", this.rainTime);
      ☃.setBoolean("raining", this.raining);
      ☃.setInteger("thunderTime", this.thunderTime);
      ☃.setBoolean("thundering", this.thundering);
      ☃.setBoolean("hardcore", this.hardcore);
      ☃.setBoolean("allowCommands", this.allowCommands);
      ☃.setBoolean("initialized", this.initialized);
      ☃.setDouble("BorderCenterX", this.borderCenterX);
      ☃.setDouble("BorderCenterZ", this.borderCenterZ);
      ☃.setDouble("BorderSize", this.borderSize);
      ☃.setLong("BorderSizeLerpTime", this.borderSizeLerpTime);
      ☃.setDouble("BorderSafeZone", this.borderSafeZone);
      ☃.setDouble("BorderDamagePerBlock", this.borderDamagePerBlock);
      ☃.setDouble("BorderSizeLerpTarget", this.borderSizeLerpTarget);
      ☃.setDouble("BorderWarningBlocks", this.borderWarningDistance);
      ☃.setDouble("BorderWarningTime", this.borderWarningTime);
      if (this.difficulty != null) {
         ☃.setByte("Difficulty", (byte)this.difficulty.getId());
      }

      ☃.setBoolean("DifficultyLocked", this.difficultyLocked);
      ☃.setTag("GameRules", this.gameRules.writeToNBT());
      NBTTagCompound ☃x = new NBTTagCompound();

      for (Entry<DimensionType, NBTTagCompound> ☃xx : this.dimensionData.entrySet()) {
         ☃x.setTag(String.valueOf(☃xx.getKey().getId()), ☃xx.getValue());
      }

      ☃.setTag("DimensionData", ☃x);
      if (☃ != null) {
         ☃.setTag("Player", ☃);
      }
   }

   public long getSeed() {
      return this.randomSeed;
   }

   public int getSpawnX() {
      return this.spawnX;
   }

   public int getSpawnY() {
      return this.spawnY;
   }

   public int getSpawnZ() {
      return this.spawnZ;
   }

   public long getWorldTotalTime() {
      return this.totalTime;
   }

   public long getWorldTime() {
      return this.worldTime;
   }

   public long getSizeOnDisk() {
      return this.sizeOnDisk;
   }

   public NBTTagCompound getPlayerNBTTagCompound() {
      return this.playerTag;
   }

   public void setSpawnX(int var1) {
      this.spawnX = ☃;
   }

   public void setSpawnY(int var1) {
      this.spawnY = ☃;
   }

   public void setSpawnZ(int var1) {
      this.spawnZ = ☃;
   }

   public void setWorldTotalTime(long var1) {
      this.totalTime = ☃;
   }

   public void setWorldTime(long var1) {
      this.worldTime = ☃;
   }

   public void setSpawn(BlockPos var1) {
      this.spawnX = ☃.getX();
      this.spawnY = ☃.getY();
      this.spawnZ = ☃.getZ();
   }

   public String getWorldName() {
      return this.levelName;
   }

   public void setWorldName(String var1) {
      this.levelName = ☃;
   }

   public int getSaveVersion() {
      return this.saveVersion;
   }

   public void setSaveVersion(int var1) {
      this.saveVersion = ☃;
   }

   public long getLastTimePlayed() {
      return this.lastTimePlayed;
   }

   public int getCleanWeatherTime() {
      return this.cleanWeatherTime;
   }

   public void setCleanWeatherTime(int var1) {
      this.cleanWeatherTime = ☃;
   }

   public boolean isThundering() {
      return this.thundering;
   }

   public void setThundering(boolean var1) {
      this.thundering = ☃;
   }

   public int getThunderTime() {
      return this.thunderTime;
   }

   public void setThunderTime(int var1) {
      this.thunderTime = ☃;
   }

   public boolean isRaining() {
      return this.raining;
   }

   public void setRaining(boolean var1) {
      this.raining = ☃;
   }

   public int getRainTime() {
      return this.rainTime;
   }

   public void setRainTime(int var1) {
      this.rainTime = ☃;
   }

   public GameType getGameType() {
      return this.gameType;
   }

   public boolean isMapFeaturesEnabled() {
      return this.mapFeaturesEnabled;
   }

   public void setMapFeaturesEnabled(boolean var1) {
      this.mapFeaturesEnabled = ☃;
   }

   public void setGameType(GameType var1) {
      this.gameType = ☃;
   }

   public boolean isHardcoreModeEnabled() {
      return this.hardcore;
   }

   public void setHardcore(boolean var1) {
      this.hardcore = ☃;
   }

   public WorldType getTerrainType() {
      return this.terrainType;
   }

   public void setTerrainType(WorldType var1) {
      this.terrainType = ☃;
   }

   public String getGeneratorOptions() {
      return this.generatorOptions == null ? "" : this.generatorOptions;
   }

   public boolean areCommandsAllowed() {
      return this.allowCommands;
   }

   public void setAllowCommands(boolean var1) {
      this.allowCommands = ☃;
   }

   public boolean isInitialized() {
      return this.initialized;
   }

   public void setServerInitialized(boolean var1) {
      this.initialized = ☃;
   }

   public GameRules getGameRulesInstance() {
      return this.gameRules;
   }

   public double getBorderCenterX() {
      return this.borderCenterX;
   }

   public double getBorderCenterZ() {
      return this.borderCenterZ;
   }

   public double getBorderSize() {
      return this.borderSize;
   }

   public void setBorderSize(double var1) {
      this.borderSize = ☃;
   }

   public long getBorderLerpTime() {
      return this.borderSizeLerpTime;
   }

   public void setBorderLerpTime(long var1) {
      this.borderSizeLerpTime = ☃;
   }

   public double getBorderLerpTarget() {
      return this.borderSizeLerpTarget;
   }

   public void setBorderLerpTarget(double var1) {
      this.borderSizeLerpTarget = ☃;
   }

   public void getBorderCenterZ(double var1) {
      this.borderCenterZ = ☃;
   }

   public void getBorderCenterX(double var1) {
      this.borderCenterX = ☃;
   }

   public double getBorderSafeZone() {
      return this.borderSafeZone;
   }

   public void setBorderSafeZone(double var1) {
      this.borderSafeZone = ☃;
   }

   public double getBorderDamagePerBlock() {
      return this.borderDamagePerBlock;
   }

   public void setBorderDamagePerBlock(double var1) {
      this.borderDamagePerBlock = ☃;
   }

   public int getBorderWarningDistance() {
      return this.borderWarningDistance;
   }

   public int getBorderWarningTime() {
      return this.borderWarningTime;
   }

   public void setBorderWarningDistance(int var1) {
      this.borderWarningDistance = ☃;
   }

   public void setBorderWarningTime(int var1) {
      this.borderWarningTime = ☃;
   }

   public EnumDifficulty getDifficulty() {
      return this.difficulty;
   }

   public void setDifficulty(EnumDifficulty var1) {
      this.difficulty = ☃;
   }

   public boolean isDifficultyLocked() {
      return this.difficultyLocked;
   }

   public void setDifficultyLocked(boolean var1) {
      this.difficultyLocked = ☃;
   }

   public void addToCrashReport(CrashReportCategory var1) {
      ☃.addDetail("Level seed", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return String.valueOf(WorldInfo.this.getSeed());
         }
      });
      ☃.addDetail(
         "Level generator",
         new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return String.format(
                  "ID %02d - %s, ver %d. Features enabled: %b",
                  WorldInfo.this.terrainType.getId(),
                  WorldInfo.this.terrainType.getName(),
                  WorldInfo.this.terrainType.getVersion(),
                  WorldInfo.this.mapFeaturesEnabled
               );
            }
         }
      );
      ☃.addDetail("Level generator options", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return WorldInfo.this.generatorOptions;
         }
      });
      ☃.addDetail("Level spawn location", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo(WorldInfo.this.spawnX, WorldInfo.this.spawnY, WorldInfo.this.spawnZ);
         }
      });
      ☃.addDetail("Level time", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return String.format("%d game time, %d day time", WorldInfo.this.totalTime, WorldInfo.this.worldTime);
         }
      });
      ☃.addDetail("Level dimension", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return String.valueOf(WorldInfo.this.dimension);
         }
      });
      ☃.addDetail("Level storage version", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            String ☃ = "Unknown?";

            try {
               switch (WorldInfo.this.saveVersion) {
                  case 19132:
                     ☃ = "McRegion";
                     break;
                  case 19133:
                     ☃ = "Anvil";
               }
            } catch (Throwable var3) {
            }

            return String.format("0x%05X - %s", WorldInfo.this.saveVersion, ☃);
         }
      });
      ☃.addDetail(
         "Level weather",
         new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return String.format(
                  "Rain time: %d (now: %b), thunder time: %d (now: %b)",
                  WorldInfo.this.rainTime,
                  WorldInfo.this.raining,
                  WorldInfo.this.thunderTime,
                  WorldInfo.this.thundering
               );
            }
         }
      );
      ☃.addDetail(
         "Level game mode",
         new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return String.format(
                  "Game mode: %s (ID %d). Hardcore: %b. Cheats: %b",
                  WorldInfo.this.gameType.getName(),
                  WorldInfo.this.gameType.getID(),
                  WorldInfo.this.hardcore,
                  WorldInfo.this.allowCommands
               );
            }
         }
      );
   }

   public NBTTagCompound getDimensionData(DimensionType var1) {
      NBTTagCompound ☃ = this.dimensionData.get(☃);
      return ☃ == null ? new NBTTagCompound() : ☃;
   }

   public void setDimensionData(DimensionType var1, NBTTagCompound var2) {
      this.dimensionData.put(☃, ☃);
   }

   public int getVersionId() {
      return this.versionId;
   }

   public boolean isVersionSnapshot() {
      return this.versionSnapshot;
   }

   public String getVersionName() {
      return this.versionName;
   }
}
