package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.FutureTask;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.ClearWater;
import net.optifine.reflect.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Minecraft mc;
   private final WorldSettings worldSettings;
   private boolean isGamePaused;
   private boolean isPublic;
   private ThreadLanServerPing lanServerPing;
   private long ticksSaveLast = 0L;
   public World difficultyUpdateWorld = null;
   public BlockPos difficultyUpdatePos = null;
   public DifficultyInstance difficultyLast = null;

   public IntegratedServer(
      Minecraft clientIn,
      String folderNameIn,
      String worldNameIn,
      WorldSettings worldSettingsIn,
      YggdrasilAuthenticationService authServiceIn,
      MinecraftSessionService sessionServiceIn,
      GameProfileRepository profileRepoIn,
      PlayerProfileCache profileCacheIn
   ) {
      super(new File(clientIn.gameDir, "saves"), clientIn.getProxy(), clientIn.getDataFixer(), authServiceIn, sessionServiceIn, profileRepoIn, profileCacheIn);
      this.setServerOwner(clientIn.getSession().getUsername());
      this.setFolderName(folderNameIn);
      this.setWorldName(worldNameIn);
      this.setDemo(clientIn.isDemo());
      this.canCreateBonusChest(worldSettingsIn.isBonusChestEnabled());
      this.setBuildLimit(256);
      this.setPlayerList(new IntegratedPlayerList(this));
      this.mc = clientIn;
      this.worldSettings = this.isDemo() ? WorldServerDemo.DEMO_WORLD_SETTINGS : worldSettingsIn;
      ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(folderNameIn, false);
      WorldInfo worldinfo = isavehandler.loadWorldInfo();
      if (worldinfo != null) {
         NBTTagCompound nbt = worldinfo.getPlayerNBTTagCompound();
         if (nbt != null && nbt.hasKey("Dimension")) {
            int dim = nbt.getInteger("Dimension");
            PacketThreadUtil.lastDimensionId = dim;
            this.mc.loadingScreen.setLoadingProgress(-1);
         }
      }
   }

   public ServerCommandManager createCommandManager() {
      return new IntegratedServerCommandManager(this);
   }

   public void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions) {
      this.convertMapIfNeeded(saveName);
      boolean forge = Reflector.DimensionManager.exists();
      if (!forge) {
         this.worlds = new WorldServer[3];
         this.timeOfLastDimensionTick = new long[this.worlds.length][100];
      }

      ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(saveName, true);
      this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
      WorldInfo worldinfo = isavehandler.loadWorldInfo();
      if (worldinfo == null) {
         worldinfo = new WorldInfo(this.worldSettings, worldNameIn);
      } else {
         worldinfo.setWorldName(worldNameIn);
      }

      if (forge) {
         WorldServer overWorld = this.isDemo()
            ? (WorldServer)new WorldServerDemo(this, isavehandler, worldinfo, 0, this.profiler).b()
            : (WorldServer)new WorldServer(this, isavehandler, worldinfo, 0, this.profiler).init();
         overWorld.initialize(this.worldSettings);
         Integer[] dimIds = (Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs, new Object[0]);
         Integer[] var12 = dimIds;
         int var13 = dimIds.length;

         for (int var14 = 0; var14 < var13; var14++) {
            int dim = var12[var14];
            WorldServer world = dim == 0 ? overWorld : (WorldServer)new WorldServerMulti(this, isavehandler, dim, overWorld, this.profiler).init();
            world.addEventListener(new ServerWorldEventHandler(this, world));
            if (!this.isSinglePlayer()) {
               world.getWorldInfo().setGameType(this.getGameType());
            }

            if (Reflector.EventBus.exists()) {
               Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[]{world});
            }
         }

         this.getPlayerList().setPlayerManager(new WorldServer[]{overWorld});
         if (overWorld.getWorldInfo().getDifficulty() == null) {
            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
         }
      } else {
         for (int i = 0; i < this.worlds.length; i++) {
            int j = 0;
            if (i == 1) {
               j = -1;
            }

            if (i == 2) {
               j = 1;
            }

            if (i == 0) {
               if (this.isDemo()) {
                  this.worlds[i] = (WorldServer)new WorldServerDemo(this, isavehandler, worldinfo, j, this.profiler).b();
               } else {
                  this.worlds[i] = (WorldServer)new WorldServer(this, isavehandler, worldinfo, j, this.profiler).init();
               }

               this.worlds[i].initialize(this.worldSettings);
            } else {
               this.worlds[i] = (WorldServer)new WorldServerMulti(this, isavehandler, j, this.worlds[0], this.profiler).init();
            }

            this.worlds[i].addEventListener(new ServerWorldEventHandler(this, this.worlds[i]));
         }

         this.getPlayerList().setPlayerManager(this.worlds);
         if (this.worlds[0].getWorldInfo().getDifficulty() == null) {
            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
         }
      }

      this.initialWorldChunkLoad();
   }

   public boolean init() throws IOException {
      LOGGER.info("Starting integrated minecraft server version 1.12.2");
      this.setOnlineMode(true);
      this.setCanSpawnAnimals(true);
      this.setCanSpawnNPCs(true);
      this.setAllowPvp(true);
      this.setAllowFlight(true);
      LOGGER.info("Generating keypair");
      this.setKeyPair(CryptManager.generateKeyPair());
      if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
         Object inst = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
         if (!Reflector.callBoolean(inst, Reflector.FMLCommonHandler_handleServerAboutToStart, new Object[]{this})) {
            return false;
         }
      }

      this.loadAllWorlds(
         this.getFolderName(), this.getWorldName(), this.worldSettings.getSeed(), this.worldSettings.getTerrainType(), this.worldSettings.getGeneratorOptions()
      );
      this.setMOTD(this.getServerOwner() + " - " + this.worlds[0].getWorldInfo().getWorldName());
      if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
         Object inst = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
         if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == boolean.class) {
            return Reflector.callBoolean(inst, Reflector.FMLCommonHandler_handleServerStarting, new Object[]{this});
         }

         Reflector.callVoid(inst, Reflector.FMLCommonHandler_handleServerStarting, new Object[]{this});
      }

      return true;
   }

   public void tick() {
      this.onTick();
      boolean flag = this.isGamePaused;
      this.isGamePaused = Minecraft.getMinecraft().getConnection() != null && Minecraft.getMinecraft().isGamePaused();
      if (!flag && this.isGamePaused) {
         LOGGER.info("Saving and pausing game...");
         this.getPlayerList().saveAllPlayerData();
         this.saveAllWorlds(false);
      }

      if (this.isGamePaused) {
         synchronized (this.futureTaskQueue) {
            while (!this.futureTaskQueue.isEmpty()) {
               Util.runTask((FutureTask)this.futureTaskQueue.poll(), LOGGER);
            }
         }
      } else {
         super.tick();
         if (this.mc.gameSettings.renderDistanceChunks != this.getPlayerList().getViewDistance()) {
            LOGGER.info("Changing view distance to {}, from {}", this.mc.gameSettings.renderDistanceChunks, this.getPlayerList().getViewDistance());
            this.getPlayerList().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
         }

         if (this.mc.world != null) {
            WorldInfo worldinfo1 = this.worlds[0].getWorldInfo();
            WorldInfo worldinfo = this.mc.world.getWorldInfo();
            if (!worldinfo1.isDifficultyLocked() && worldinfo.getDifficulty() != worldinfo1.getDifficulty()) {
               LOGGER.info("Changing difficulty to {}, from {}", worldinfo.getDifficulty(), worldinfo1.getDifficulty());
               this.setDifficultyForAllWorlds(worldinfo.getDifficulty());
            } else if (worldinfo.isDifficultyLocked() && !worldinfo1.isDifficultyLocked()) {
               LOGGER.info("Locking difficulty to {}", worldinfo.getDifficulty());

               for (WorldServer worldserver : this.worlds) {
                  if (worldserver != null) {
                     worldserver.getWorldInfo().setDifficultyLocked(true);
                  }
               }
            }
         }
      }
   }

   public boolean canStructuresSpawn() {
      return false;
   }

   public GameType getGameType() {
      return this.worldSettings.getGameType();
   }

   public EnumDifficulty getDifficulty() {
      return this.mc.world == null ? this.mc.gameSettings.difficulty : this.mc.world.getWorldInfo().getDifficulty();
   }

   public boolean isHardcore() {
      return this.worldSettings.getHardcoreEnabled();
   }

   public boolean shouldBroadcastRconToOps() {
      return true;
   }

   public boolean shouldBroadcastConsoleToOps() {
      return true;
   }

   public void saveAllWorlds(boolean isSilent) {
      if (isSilent) {
         int ticks = this.getTickCounter();
         int ticksSaveInterval = this.mc.gameSettings.ofAutoSaveTicks;
         if (ticks < this.ticksSaveLast + ticksSaveInterval) {
            return;
         }

         this.ticksSaveLast = ticks;
      }

      super.saveAllWorlds(isSilent);
   }

   public File getDataDirectory() {
      return this.mc.gameDir;
   }

   public boolean isDedicatedServer() {
      return false;
   }

   public boolean shouldUseNativeTransport() {
      return false;
   }

   public void finalTick(CrashReport report) {
      this.mc.crashed(report);
   }

   public CrashReport addServerInfoToCrashReport(CrashReport report) {
      report = super.addServerInfoToCrashReport(report);
      report.getCategory().addDetail("Type", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return "Integrated Server (map_client.txt)";
         }
      });
      report.getCategory()
         .addDetail(
            "Is Modded",
            new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  String s = ClientBrandRetriever.getClientModName();
                  if (!s.equals("vanilla")) {
                     return "Definitely; Client brand changed to '" + s + "'";
                  } else {
                     s = IntegratedServer.this.getServerModName();
                     if (!"vanilla".equals(s)) {
                        return "Definitely; Server brand changed to '" + s + "'";
                     } else {
                        return Minecraft.class.getSigners() == null
                           ? "Very likely; Jar signature invalidated"
                           : "Probably not. Jar signature remains and both client + server brands are untouched.";
                     }
                  }
               }
            }
         );
      return report;
   }

   public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
      super.setDifficultyForAllWorlds(difficulty);
      if (this.mc.world != null) {
         this.mc.world.getWorldInfo().setDifficulty(difficulty);
      }
   }

   public void addServerStatsToSnooper(Snooper playerSnooper) {
      super.addServerStatsToSnooper(playerSnooper);
      playerSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
   }

   public boolean isSnooperEnabled() {
      return Minecraft.getMinecraft().isSnooperEnabled();
   }

   public String shareToLAN(GameType type, boolean allowCheats) {
      try {
         int i = -1;

         try {
            i = HttpUtil.getSuitableLanPort();
         } catch (IOException var5) {
         }

         if (i <= 0) {
            i = 25564;
         }

         this.getNetworkSystem().addEndpoint((InetAddress)null, i);
         LOGGER.info("Started on {}", i);
         this.isPublic = true;
         this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), i + "");
         this.lanServerPing.start();
         this.getPlayerList().setGameType(type);
         this.getPlayerList().setCommandsAllowedForAll(allowCheats);
         this.mc.player.setPermissionLevel(allowCheats ? 4 : 0);
         return i + "";
      } catch (IOException var61) {
         return null;
      }
   }

   public void stopServer() {
      super.stopServer();
      if (this.lanServerPing != null) {
         this.lanServerPing.interrupt();
         this.lanServerPing = null;
      }
   }

   public void initiateShutdown() {
      if (!Reflector.MinecraftForge.exists() || this.isServerRunning()) {
         Futures.getUnchecked(this.addScheduledTask(new Runnable() {
            @Override
            public void run() {
               for (EntityPlayerMP entityplayermp : Lists.newArrayList(IntegratedServer.this.getPlayerList().getPlayers())) {
                  if (!entityplayermp.getUniqueID().equals(IntegratedServer.this.mc.player.getUniqueID())) {
                     IntegratedServer.this.getPlayerList().playerLoggedOut(entityplayermp);
                  }
               }
            }
         }));
      }

      super.initiateShutdown();
      if (this.lanServerPing != null) {
         this.lanServerPing.interrupt();
         this.lanServerPing = null;
      }
   }

   public boolean getPublic() {
      return this.isPublic;
   }

   public void setGameType(GameType gameMode) {
      super.setGameType(gameMode);
      this.getPlayerList().setGameType(gameMode);
   }

   public boolean isCommandBlockEnabled() {
      return true;
   }

   public int getOpPermissionLevel() {
      return 4;
   }

   private void onTick() {
      for (WorldServer ws : Arrays.asList(this.worlds)) {
         this.onTick(ws);
      }
   }

   public DifficultyInstance getDifficultyAsync(World world, BlockPos blockPos) {
      this.difficultyUpdateWorld = world;
      this.difficultyUpdatePos = blockPos;
      return this.difficultyLast;
   }

   private void onTick(WorldServer ws) {
      if (!Config.isTimeDefault()) {
         this.fixWorldTime(ws);
      }

      if (!Config.isWeatherEnabled()) {
         this.fixWorldWeather(ws);
      }

      if (Config.waterOpacityChanged) {
         Config.waterOpacityChanged = false;
         ClearWater.updateWaterOpacity(Config.getGameSettings(), ws);
      }

      if (this.difficultyUpdateWorld == ws && this.difficultyUpdatePos != null) {
         this.difficultyLast = ws.getDifficultyForLocation(this.difficultyUpdatePos);
         this.difficultyUpdateWorld = null;
         this.difficultyUpdatePos = null;
      }
   }

   private void fixWorldWeather(WorldServer ws) {
      WorldInfo worldInfo = ws.getWorldInfo();
      if (worldInfo.isRaining() || worldInfo.isThundering()) {
         worldInfo.setRainTime(0);
         worldInfo.setRaining(false);
         ws.setRainStrength(0.0F);
         worldInfo.setThunderTime(0);
         worldInfo.setThundering(false);
         ws.setThunderStrength(0.0F);
         this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(2, 0.0F));
         this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(7, 0.0F));
         this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(8, 0.0F));
      }
   }

   private void fixWorldTime(WorldServer ws) {
      WorldInfo worldInfo = ws.getWorldInfo();
      if (worldInfo.getGameType().getID() == 1) {
         long time = ws.getWorldTime();
         long timeOfDay = time % 24000L;
         if (Config.isTimeDayOnly()) {
            if (timeOfDay <= 1000L) {
               ws.setWorldTime(time - timeOfDay + 1001L);
            }

            if (timeOfDay >= 11000L) {
               ws.setWorldTime(time - timeOfDay + 24001L);
            }
         }

         if (Config.isTimeNightOnly()) {
            if (timeOfDay <= 14000L) {
               ws.setWorldTime(time - timeOfDay + 14001L);
            }

            if (timeOfDay >= 22000L) {
               ws.setWorldTime(time - timeOfDay + 24000L + 14001L);
            }
         }
      }
   }
}
