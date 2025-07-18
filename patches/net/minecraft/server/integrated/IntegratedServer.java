package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import java.io.IOException;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Minecraft mc;
   private final WorldSettings worldSettings;
   private boolean isGamePaused;
   private boolean isPublic;
   private ThreadLanServerPing lanServerPing;

   public IntegratedServer(
      Minecraft var1,
      String var2,
      String var3,
      WorldSettings var4,
      YggdrasilAuthenticationService var5,
      MinecraftSessionService var6,
      GameProfileRepository var7,
      PlayerProfileCache var8
   ) {
      super(new File(☃.gameDir, "saves"), ☃.getProxy(), ☃.getDataFixer(), ☃, ☃, ☃, ☃);
      this.setServerOwner(☃.getSession().getUsername());
      this.setFolderName(☃);
      this.setWorldName(☃);
      this.setDemo(☃.isDemo());
      this.canCreateBonusChest(☃.isBonusChestEnabled());
      this.setBuildLimit(256);
      this.setPlayerList(new IntegratedPlayerList(this));
      this.mc = ☃;
      this.worldSettings = this.isDemo() ? WorldServerDemo.DEMO_WORLD_SETTINGS : ☃;
   }

   @Override
   protected ServerCommandManager createCommandManager() {
      return new IntegratedServerCommandManager(this);
   }

   @Override
   protected void loadAllWorlds(String var1, String var2, long var3, WorldType var5, String var6) {
      this.convertMapIfNeeded(☃);
      this.worlds = new WorldServer[3];
      this.timeOfLastDimensionTick = new long[this.worlds.length][100];
      ISaveHandler ☃ = this.getActiveAnvilConverter().getSaveLoader(☃, true);
      this.setResourcePackFromWorld(this.getFolderName(), ☃);
      WorldInfo ☃x = ☃.loadWorldInfo();
      if (☃x == null) {
         ☃x = new WorldInfo(this.worldSettings, ☃);
      } else {
         ☃x.setWorldName(☃);
      }

      for (int ☃xx = 0; ☃xx < this.worlds.length; ☃xx++) {
         int ☃xxx = 0;
         if (☃xx == 1) {
            ☃xxx = -1;
         }

         if (☃xx == 2) {
            ☃xxx = 1;
         }

         if (☃xx == 0) {
            if (this.isDemo()) {
               this.worlds[☃xx] = (WorldServer)new WorldServerDemo(this, ☃, ☃x, ☃xxx, this.profiler).init();
            } else {
               this.worlds[☃xx] = (WorldServer)new WorldServer(this, ☃, ☃x, ☃xxx, this.profiler).init();
            }

            this.worlds[☃xx].initialize(this.worldSettings);
         } else {
            this.worlds[☃xx] = (WorldServer)new WorldServerMulti(this, ☃, ☃xxx, this.worlds[0], this.profiler).init();
         }

         this.worlds[☃xx].addEventListener(new ServerWorldEventHandler(this, this.worlds[☃xx]));
      }

      this.getPlayerList().setPlayerManager(this.worlds);
      if (this.worlds[0].getWorldInfo().getDifficulty() == null) {
         this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
      }

      this.initialWorldChunkLoad();
   }

   @Override
   protected boolean init() throws IOException {
      LOGGER.info("Starting integrated minecraft server version 1.12.2");
      this.setOnlineMode(true);
      this.setCanSpawnAnimals(true);
      this.setCanSpawnNPCs(true);
      this.setAllowPvp(true);
      this.setAllowFlight(true);
      LOGGER.info("Generating keypair");
      this.setKeyPair(CryptManager.generateKeyPair());
      this.loadAllWorlds(
         this.getFolderName(), this.getWorldName(), this.worldSettings.getSeed(), this.worldSettings.getTerrainType(), this.worldSettings.getGeneratorOptions()
      );
      this.setMOTD(this.getServerOwner() + " - " + this.worlds[0].getWorldInfo().getWorldName());
      return true;
   }

   @Override
   protected void tick() {
      boolean ☃ = this.isGamePaused;
      this.isGamePaused = Minecraft.getMinecraft().getConnection() != null && Minecraft.getMinecraft().isGamePaused();
      if (!☃ && this.isGamePaused) {
         LOGGER.info("Saving and pausing game...");
         this.getPlayerList().saveAllPlayerData();
         this.saveAllWorlds(false);
      }

      if (this.isGamePaused) {
         synchronized (this.futureTaskQueue) {
            while (!this.futureTaskQueue.isEmpty()) {
               Util.runTask(this.futureTaskQueue.poll(), LOGGER);
            }
         }
      } else {
         super.tick();
         if (this.mc.gameSettings.renderDistanceChunks != this.getPlayerList().getViewDistance()) {
            LOGGER.info("Changing view distance to {}, from {}", this.mc.gameSettings.renderDistanceChunks, this.getPlayerList().getViewDistance());
            this.getPlayerList().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
         }

         if (this.mc.world != null) {
            WorldInfo ☃x = this.worlds[0].getWorldInfo();
            WorldInfo ☃xx = this.mc.world.getWorldInfo();
            if (!☃x.isDifficultyLocked() && ☃xx.getDifficulty() != ☃x.getDifficulty()) {
               LOGGER.info("Changing difficulty to {}, from {}", ☃xx.getDifficulty(), ☃x.getDifficulty());
               this.setDifficultyForAllWorlds(☃xx.getDifficulty());
            } else if (☃xx.isDifficultyLocked() && !☃x.isDifficultyLocked()) {
               LOGGER.info("Locking difficulty to {}", ☃xx.getDifficulty());

               for (WorldServer ☃xxx : this.worlds) {
                  if (☃xxx != null) {
                     ☃xxx.getWorldInfo().setDifficultyLocked(true);
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean canStructuresSpawn() {
      return false;
   }

   @Override
   public GameType getGameType() {
      return this.worldSettings.getGameType();
   }

   @Override
   public EnumDifficulty getDifficulty() {
      return this.mc.world.getWorldInfo().getDifficulty();
   }

   @Override
   public boolean isHardcore() {
      return this.worldSettings.getHardcoreEnabled();
   }

   @Override
   public boolean shouldBroadcastRconToOps() {
      return true;
   }

   @Override
   public boolean shouldBroadcastConsoleToOps() {
      return true;
   }

   @Override
   protected void saveAllWorlds(boolean var1) {
      super.saveAllWorlds(☃);
   }

   @Override
   public File getDataDirectory() {
      return this.mc.gameDir;
   }

   @Override
   public boolean isDedicatedServer() {
      return false;
   }

   @Override
   public boolean shouldUseNativeTransport() {
      return false;
   }

   @Override
   protected void finalTick(CrashReport var1) {
      this.mc.crashed(☃);
   }

   @Override
   public CrashReport addServerInfoToCrashReport(CrashReport var1) {
      ☃ = super.addServerInfoToCrashReport(☃);
      ☃.getCategory().addDetail("Type", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return "Integrated Server (map_client.txt)";
         }
      });
      ☃.getCategory()
         .addDetail(
            "Is Modded",
            new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  String ☃ = ClientBrandRetriever.getClientModName();
                  if (!☃.equals("vanilla")) {
                     return "Definitely; Client brand changed to '" + ☃ + "'";
                  } else {
                     ☃ = IntegratedServer.this.getServerModName();
                     if (!"vanilla".equals(☃)) {
                        return "Definitely; Server brand changed to '" + ☃ + "'";
                     } else {
                        return Minecraft.class.getSigners() == null
                           ? "Very likely; Jar signature invalidated"
                           : "Probably not. Jar signature remains and both client + server brands are untouched.";
                     }
                  }
               }
            }
         );
      return ☃;
   }

   @Override
   public void setDifficultyForAllWorlds(EnumDifficulty var1) {
      super.setDifficultyForAllWorlds(☃);
      if (this.mc.world != null) {
         this.mc.world.getWorldInfo().setDifficulty(☃);
      }
   }

   @Override
   public void addServerStatsToSnooper(Snooper var1) {
      super.addServerStatsToSnooper(☃);
      ☃.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
   }

   @Override
   public boolean isSnooperEnabled() {
      return Minecraft.getMinecraft().isSnooperEnabled();
   }

   @Override
   public String shareToLAN(GameType var1, boolean var2) {
      try {
         int ☃ = -1;

         try {
            ☃ = HttpUtil.getSuitableLanPort();
         } catch (IOException var5) {
         }

         if (☃ <= 0) {
            ☃ = 25564;
         }

         this.getNetworkSystem().addEndpoint(null, ☃);
         LOGGER.info("Started on {}", ☃);
         this.isPublic = true;
         this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), ☃ + "");
         this.lanServerPing.start();
         this.getPlayerList().setGameType(☃);
         this.getPlayerList().setCommandsAllowedForAll(☃);
         this.mc.player.setPermissionLevel(☃ ? 4 : 0);
         return ☃ + "";
      } catch (IOException var6) {
         return null;
      }
   }

   @Override
   public void stopServer() {
      super.stopServer();
      if (this.lanServerPing != null) {
         this.lanServerPing.interrupt();
         this.lanServerPing = null;
      }
   }

   @Override
   public void initiateShutdown() {
      Futures.getUnchecked(this.addScheduledTask(new Runnable() {
         @Override
         public void run() {
            for (EntityPlayerMP ☃ : Lists.newArrayList(IntegratedServer.this.getPlayerList().getPlayers())) {
               if (!☃.getUniqueID().equals(IntegratedServer.this.mc.player.getUniqueID())) {
                  IntegratedServer.this.getPlayerList().playerLoggedOut(☃);
               }
            }
         }
      }));
      super.initiateShutdown();
      if (this.lanServerPing != null) {
         this.lanServerPing.interrupt();
         this.lanServerPing = null;
      }
   }

   public boolean getPublic() {
      return this.isPublic;
   }

   @Override
   public void setGameType(GameType var1) {
      super.setGameType(☃);
      this.getPlayerList().setGameType(☃);
   }

   @Override
   public boolean isCommandBlockEnabled() {
      return true;
   }

   @Override
   public int getOpPermissionLevel() {
      return 4;
   }
}
