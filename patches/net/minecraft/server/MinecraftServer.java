package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.profiler.Profiler;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MinecraftServer implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final File USER_CACHE_FILE = new File("usercache.json");
   private final ISaveFormat anvilConverterForAnvilFile;
   private final Snooper usageSnooper = new Snooper("server", this, getCurrentTimeMillis());
   private final File anvilFile;
   private final List<ITickable> tickables = Lists.newArrayList();
   public final ICommandManager commandManager;
   public final Profiler profiler = new Profiler();
   private final NetworkSystem networkSystem;
   private final ServerStatusResponse statusResponse = new ServerStatusResponse();
   private final Random random = new Random();
   private final DataFixer dataFixer;
   private int serverPort = -1;
   public WorldServer[] worlds;
   private PlayerList playerList;
   private boolean serverRunning = true;
   private boolean serverStopped;
   private int tickCounter;
   protected final Proxy serverProxy;
   public String currentTask;
   public int percentDone;
   private boolean onlineMode;
   private boolean preventProxyConnections;
   private boolean canSpawnAnimals;
   private boolean canSpawnNPCs;
   private boolean pvpEnabled;
   private boolean allowFlight;
   private String motd;
   private int buildLimit;
   private int maxPlayerIdleMinutes;
   public final long[] tickTimeArray = new long[100];
   public long[][] timeOfLastDimensionTick;
   private KeyPair serverKeyPair;
   private String serverOwner;
   private String folderName;
   private String worldName;
   private boolean isDemo;
   private boolean enableBonusChest;
   private String resourcePackUrl = "";
   private String resourcePackHash = "";
   private boolean serverIsRunning;
   private long timeOfLastWarning;
   private String userMessage;
   private boolean startProfiling;
   private boolean isGamemodeForced;
   private final YggdrasilAuthenticationService authService;
   private final MinecraftSessionService sessionService;
   private final GameProfileRepository profileRepo;
   private final PlayerProfileCache profileCache;
   private long nanoTimeSinceStatusRefresh;
   public final Queue<FutureTask<?>> futureTaskQueue = Queues.newArrayDeque();
   private Thread serverThread;
   private long currentTime = getCurrentTimeMillis();
   private boolean worldIconSet;

   public MinecraftServer(
      File var1,
      Proxy var2,
      DataFixer var3,
      YggdrasilAuthenticationService var4,
      MinecraftSessionService var5,
      GameProfileRepository var6,
      PlayerProfileCache var7
   ) {
      this.serverProxy = ☃;
      this.authService = ☃;
      this.sessionService = ☃;
      this.profileRepo = ☃;
      this.profileCache = ☃;
      this.anvilFile = ☃;
      this.networkSystem = new NetworkSystem(this);
      this.commandManager = this.createCommandManager();
      this.anvilConverterForAnvilFile = new AnvilSaveConverter(☃, ☃);
      this.dataFixer = ☃;
   }

   public ServerCommandManager createCommandManager() {
      return new ServerCommandManager(this);
   }

   public abstract boolean init() throws IOException;

   public void convertMapIfNeeded(String var1) {
      if (this.getActiveAnvilConverter().isOldMapFormat(☃)) {
         LOGGER.info("Converting map!");
         this.setUserMessage("menu.convertingLevel");
         this.getActiveAnvilConverter().convertMapFormat(☃, new IProgressUpdate() {
            private long startTime = MinecraftServer.getCurrentTimeMillis();

            @Override
            public void displaySavingString(String var1) {
            }

            @Override
            public void resetProgressAndMessage(String var1) {
            }

            @Override
            public void setLoadingProgress(int var1) {
               if (MinecraftServer.getCurrentTimeMillis() - this.startTime >= 1000L) {
                  this.startTime = MinecraftServer.getCurrentTimeMillis();
                  MinecraftServer.LOGGER.info("Converting... {}%", ☃);
               }
            }

            @Override
            public void setDoneWorking() {
            }

            @Override
            public void displayLoadingString(String var1) {
            }
         });
      }
   }

   protected synchronized void setUserMessage(String var1) {
      this.userMessage = ☃;
   }

   @Nullable
   public synchronized String getUserMessage() {
      return this.userMessage;
   }

   public void loadAllWorlds(String var1, String var2, long var3, WorldType var5, String var6) {
      this.convertMapIfNeeded(☃);
      this.setUserMessage("menu.loadingLevel");
      this.worlds = new WorldServer[3];
      this.timeOfLastDimensionTick = new long[this.worlds.length][100];
      ISaveHandler ☃ = this.anvilConverterForAnvilFile.getSaveLoader(☃, true);
      this.setResourcePackFromWorld(this.getFolderName(), ☃);
      WorldInfo ☃x = ☃.loadWorldInfo();
      WorldSettings ☃xx;
      if (☃x == null) {
         if (this.isDemo()) {
            ☃xx = WorldServerDemo.DEMO_WORLD_SETTINGS;
         } else {
            ☃xx = new WorldSettings(☃, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), ☃);
            ☃xx.setGeneratorOptions(☃);
            if (this.enableBonusChest) {
               ☃xx.enableBonusChest();
            }
         }

         ☃x = new WorldInfo(☃xx, ☃);
      } else {
         ☃x.setWorldName(☃);
         ☃xx = new WorldSettings(☃x);
      }

      for (int ☃xxx = 0; ☃xxx < this.worlds.length; ☃xxx++) {
         int ☃xxxx = 0;
         if (☃xxx == 1) {
            ☃xxxx = -1;
         }

         if (☃xxx == 2) {
            ☃xxxx = 1;
         }

         if (☃xxx == 0) {
            if (this.isDemo()) {
               this.worlds[☃xxx] = (WorldServer)new WorldServerDemo(this, ☃, ☃x, ☃xxxx, this.profiler).init();
            } else {
               this.worlds[☃xxx] = (WorldServer)new WorldServer(this, ☃, ☃x, ☃xxxx, this.profiler).init();
            }

            this.worlds[☃xxx].initialize(☃xx);
         } else {
            this.worlds[☃xxx] = (WorldServer)new WorldServerMulti(this, ☃, ☃xxxx, this.worlds[0], this.profiler).init();
         }

         this.worlds[☃xxx].addEventListener(new ServerWorldEventHandler(this, this.worlds[☃xxx]));
         if (!this.isSinglePlayer()) {
            this.worlds[☃xxx].getWorldInfo().setGameType(this.getGameType());
         }
      }

      this.playerList.setPlayerManager(this.worlds);
      this.setDifficultyForAllWorlds(this.getDifficulty());
      this.initialWorldChunkLoad();
   }

   public void initialWorldChunkLoad() {
      int ☃ = 16;
      int ☃x = 4;
      int ☃xx = 192;
      int ☃xxx = 625;
      int ☃xxxx = 0;
      this.setUserMessage("menu.generatingTerrain");
      int ☃xxxxx = 0;
      LOGGER.info("Preparing start region for level 0");
      WorldServer ☃xxxxxx = this.worlds[0];
      BlockPos ☃xxxxxxx = ☃xxxxxx.getSpawnPoint();
      long ☃xxxxxxxx = getCurrentTimeMillis();

      for (int ☃xxxxxxxxx = -192; ☃xxxxxxxxx <= 192 && this.isServerRunning(); ☃xxxxxxxxx += 16) {
         for (int ☃xxxxxxxxxx = -192; ☃xxxxxxxxxx <= 192 && this.isServerRunning(); ☃xxxxxxxxxx += 16) {
            long ☃xxxxxxxxxxx = getCurrentTimeMillis();
            if (☃xxxxxxxxxxx - ☃xxxxxxxx > 1000L) {
               this.outputPercentRemaining("Preparing spawn area", ☃xxxx * 100 / 625);
               ☃xxxxxxxx = ☃xxxxxxxxxxx;
            }

            ☃xxxx++;
            ☃xxxxxx.getChunkProvider().provideChunk(☃xxxxxxx.getX() + ☃xxxxxxxxx >> 4, ☃xxxxxxx.getZ() + ☃xxxxxxxxxx >> 4);
         }
      }

      this.clearCurrentTask();
   }

   public void setResourcePackFromWorld(String var1, ISaveHandler var2) {
      File ☃ = new File(☃.getWorldDirectory(), "resources.zip");
      if (☃.isFile()) {
         try {
            this.setResourcePack("level://" + URLEncoder.encode(☃, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
         } catch (UnsupportedEncodingException var5) {
            LOGGER.warn("Something went wrong url encoding {}", ☃);
         }
      }
   }

   public abstract boolean canStructuresSpawn();

   public abstract GameType getGameType();

   public abstract EnumDifficulty getDifficulty();

   public abstract boolean isHardcore();

   public abstract int getOpPermissionLevel();

   public abstract boolean shouldBroadcastRconToOps();

   public abstract boolean shouldBroadcastConsoleToOps();

   protected void outputPercentRemaining(String var1, int var2) {
      this.currentTask = ☃;
      this.percentDone = ☃;
      LOGGER.info("{}: {}%", ☃, ☃);
   }

   protected void clearCurrentTask() {
      this.currentTask = null;
      this.percentDone = 0;
   }

   public void saveAllWorlds(boolean var1) {
      for (WorldServer ☃ : this.worlds) {
         if (☃ != null) {
            if (!☃) {
               LOGGER.info("Saving chunks for level '{}'/{}", ☃.getWorldInfo().getWorldName(), ☃.provider.getDimensionType().getName());
            }

            try {
               ☃.saveAllChunks(true, null);
            } catch (MinecraftException var7) {
               LOGGER.warn(var7.getMessage());
            }
         }
      }
   }

   public void stopServer() {
      LOGGER.info("Stopping server");
      if (this.getNetworkSystem() != null) {
         this.getNetworkSystem().terminateEndpoints();
      }

      if (this.playerList != null) {
         LOGGER.info("Saving players");
         this.playerList.saveAllPlayerData();
         this.playerList.removeAllPlayers();
      }

      if (this.worlds != null) {
         LOGGER.info("Saving worlds");

         for (WorldServer ☃ : this.worlds) {
            if (☃ != null) {
               ☃.disableLevelSaving = false;
            }
         }

         this.saveAllWorlds(false);

         for (WorldServer ☃x : this.worlds) {
            if (☃x != null) {
               ☃x.flush();
            }
         }
      }

      if (this.usageSnooper.isSnooperRunning()) {
         this.usageSnooper.stopSnooper();
      }
   }

   public boolean isServerRunning() {
      return this.serverRunning;
   }

   public void initiateShutdown() {
      this.serverRunning = false;
   }

   @Override
   public void run() {
      try {
         if (this.init()) {
            this.currentTime = getCurrentTimeMillis();
            long ☃ = 0L;
            this.statusResponse.setServerDescription(new TextComponentString(this.motd));
            this.statusResponse.setVersion(new ServerStatusResponse.Version("1.12.2", 340));
            this.applyServerIconToResponse(this.statusResponse);

            while (this.serverRunning) {
               long ☃x = getCurrentTimeMillis();
               long ☃xx = ☃x - this.currentTime;
               if (☃xx > 2000L && this.currentTime - this.timeOfLastWarning >= 15000L) {
                  LOGGER.warn(
                     "Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", ☃xx, ☃xx / 50L
                  );
                  ☃xx = 2000L;
                  this.timeOfLastWarning = this.currentTime;
               }

               if (☃xx < 0L) {
                  LOGGER.warn("Time ran backwards! Did the system time change?");
                  ☃xx = 0L;
               }

               ☃ += ☃xx;
               this.currentTime = ☃x;
               if (this.worlds[0].areAllPlayersAsleep()) {
                  this.tick();
                  ☃ = 0L;
               } else {
                  while (☃ > 50L) {
                     ☃ -= 50L;
                     this.tick();
                  }
               }

               Thread.sleep(Math.max(1L, 50L - ☃));
               this.serverIsRunning = true;
            }
         } else {
            this.finalTick(null);
         }
      } catch (Throwable var46) {
         LOGGER.error("Encountered an unexpected exception", var46);
         CrashReport ☃ = null;
         if (var46 instanceof ReportedException) {
            ☃ = this.addServerInfoToCrashReport(((ReportedException)var46).getCrashReport());
         } else {
            ☃ = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var46));
         }

         File ☃xxx = new File(
            new File(this.getDataDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt"
         );
         if (☃.saveToFile(☃xxx)) {
            LOGGER.error("This crash report has been saved to: {}", ☃xxx.getAbsolutePath());
         } else {
            LOGGER.error("We were unable to save this crash report to disk.");
         }

         this.finalTick(☃);
      } finally {
         try {
            this.serverStopped = true;
            this.stopServer();
         } catch (Throwable var44) {
            LOGGER.error("Exception stopping the server", var44);
         } finally {
            this.systemExitNow();
         }
      }
   }

   public void applyServerIconToResponse(ServerStatusResponse var1) {
      File ☃ = this.getFile("server-icon.png");
      if (!☃.exists()) {
         ☃ = this.getActiveAnvilConverter().getFile(this.getFolderName(), "icon.png");
      }

      if (☃.isFile()) {
         ByteBuf ☃x = Unpooled.buffer();

         try {
            BufferedImage ☃xx = ImageIO.read(☃);
            Validate.validState(☃xx.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
            Validate.validState(☃xx.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
            ImageIO.write(☃xx, "PNG", new ByteBufOutputStream(☃x));
            ByteBuf ☃xxx = Base64.encode(☃x);
            ☃.setFavicon("data:image/png;base64," + ☃xxx.toString(StandardCharsets.UTF_8));
         } catch (Exception var9) {
            LOGGER.error("Couldn't load server icon", var9);
         } finally {
            ☃x.release();
         }
      }
   }

   public boolean isWorldIconSet() {
      this.worldIconSet = this.worldIconSet || this.getWorldIconFile().isFile();
      return this.worldIconSet;
   }

   public File getWorldIconFile() {
      return this.getActiveAnvilConverter().getFile(this.getFolderName(), "icon.png");
   }

   public File getDataDirectory() {
      return new File(".");
   }

   public void finalTick(CrashReport var1) {
   }

   public void systemExitNow() {
   }

   public void tick() {
      long ☃ = System.nanoTime();
      this.tickCounter++;
      if (this.startProfiling) {
         this.startProfiling = false;
         this.profiler.profilingEnabled = true;
         this.profiler.clearProfiling();
      }

      this.profiler.startSection("root");
      this.updateTimeLightAndEntities();
      if (☃ - this.nanoTimeSinceStatusRefresh >= 5000000000L) {
         this.nanoTimeSinceStatusRefresh = ☃;
         this.statusResponse.setPlayers(new ServerStatusResponse.Players(this.getMaxPlayers(), this.getCurrentPlayerCount()));
         GameProfile[] ☃x = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
         int ☃xx = MathHelper.getInt(this.random, 0, this.getCurrentPlayerCount() - ☃x.length);

         for (int ☃xxx = 0; ☃xxx < ☃x.length; ☃xxx++) {
            ☃x[☃xxx] = this.playerList.getPlayers().get(☃xx + ☃xxx).getGameProfile();
         }

         Collections.shuffle(Arrays.asList(☃x));
         this.statusResponse.getPlayers().setPlayers(☃x);
      }

      if (this.tickCounter % 900 == 0) {
         this.profiler.startSection("save");
         this.playerList.saveAllPlayerData();
         this.saveAllWorlds(true);
         this.profiler.endSection();
      }

      this.profiler.startSection("tallying");
      this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - ☃;
      this.profiler.endSection();
      this.profiler.startSection("snooper");
      if (!this.usageSnooper.isSnooperRunning() && this.tickCounter > 100) {
         this.usageSnooper.startSnooper();
      }

      if (this.tickCounter % 6000 == 0) {
         this.usageSnooper.addMemoryStatsToSnooper();
      }

      this.profiler.endSection();
      this.profiler.endSection();
   }

   public void updateTimeLightAndEntities() {
      this.profiler.startSection("jobs");
      synchronized (this.futureTaskQueue) {
         while (!this.futureTaskQueue.isEmpty()) {
            Util.runTask(this.futureTaskQueue.poll(), LOGGER);
         }
      }

      this.profiler.endStartSection("levels");

      for (int ☃ = 0; ☃ < this.worlds.length; ☃++) {
         long ☃x = System.nanoTime();
         if (☃ == 0 || this.getAllowNether()) {
            WorldServer ☃xx = this.worlds[☃];
            this.profiler.func_194340_a(() -> ☃.getWorldInfo().getWorldName());
            if (this.tickCounter % 20 == 0) {
               this.profiler.startSection("timeSync");
               this.playerList
                  .sendPacketToAllPlayersInDimension(
                     new SPacketTimeUpdate(☃xx.getTotalWorldTime(), ☃xx.getWorldTime(), ☃xx.getGameRules().getBoolean("doDaylightCycle")),
                     ☃xx.provider.getDimensionType().getId()
                  );
               this.profiler.endSection();
            }

            this.profiler.startSection("tick");

            try {
               ☃xx.tick();
            } catch (Throwable var8) {
               CrashReport ☃xxx = CrashReport.makeCrashReport(var8, "Exception ticking world");
               ☃xx.addWorldInfoToCrashReport(☃xxx);
               throw new ReportedException(☃xxx);
            }

            try {
               ☃xx.updateEntities();
            } catch (Throwable var7) {
               CrashReport ☃xxx = CrashReport.makeCrashReport(var7, "Exception ticking world entities");
               ☃xx.addWorldInfoToCrashReport(☃xxx);
               throw new ReportedException(☃xxx);
            }

            this.profiler.endSection();
            this.profiler.startSection("tracker");
            ☃xx.getEntityTracker().tick();
            this.profiler.endSection();
            this.profiler.endSection();
         }

         this.timeOfLastDimensionTick[☃][this.tickCounter % 100] = System.nanoTime() - ☃x;
      }

      this.profiler.endStartSection("connection");
      this.getNetworkSystem().networkTick();
      this.profiler.endStartSection("players");
      this.playerList.onTick();
      this.profiler.endStartSection("commandFunctions");
      this.getFunctionManager().update();
      this.profiler.endStartSection("tickables");

      for (int ☃ = 0; ☃ < this.tickables.size(); ☃++) {
         this.tickables.get(☃).update();
      }

      this.profiler.endSection();
   }

   public boolean getAllowNether() {
      return true;
   }

   public void startServerThread() {
      this.serverThread = new Thread(this, "Server thread");
      this.serverThread.start();
   }

   public File getFile(String var1) {
      return new File(this.getDataDirectory(), ☃);
   }

   public void logWarning(String var1) {
      LOGGER.warn(☃);
   }

   public WorldServer getWorld(int var1) {
      if (☃ == -1) {
         return this.worlds[1];
      } else {
         return ☃ == 1 ? this.worlds[2] : this.worlds[0];
      }
   }

   public String getMinecraftVersion() {
      return "1.12.2";
   }

   public int getCurrentPlayerCount() {
      return this.playerList.getCurrentPlayerCount();
   }

   public int getMaxPlayers() {
      return this.playerList.getMaxPlayers();
   }

   public String[] getOnlinePlayerNames() {
      return this.playerList.getOnlinePlayerNames();
   }

   public GameProfile[] getOnlinePlayerProfiles() {
      return this.playerList.getOnlinePlayerProfiles();
   }

   public String getServerModName() {
      return "vanilla";
   }

   public CrashReport addServerInfoToCrashReport(CrashReport var1) {
      ☃.getCategory().addDetail("Profiler Position", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return MinecraftServer.this.profiler.profilingEnabled ? MinecraftServer.this.profiler.getNameOfLastSection() : "N/A (disabled)";
         }
      });
      if (this.playerList != null) {
         ☃.getCategory()
            .addDetail(
               "Player Count",
               new ICrashReportDetail<String>() {
                  public String call() {
                     return MinecraftServer.this.playerList.getCurrentPlayerCount()
                        + " / "
                        + MinecraftServer.this.playerList.getMaxPlayers()
                        + "; "
                        + MinecraftServer.this.playerList.getPlayers();
                  }
               }
            );
      }

      return ☃;
   }

   public List<String> getTabCompletions(ICommandSender var1, String var2, @Nullable BlockPos var3, boolean var4) {
      List<String> ☃ = Lists.newArrayList();
      boolean ☃x = ☃.startsWith("/");
      if (☃x) {
         ☃ = ☃.substring(1);
      }

      if (!☃x && !☃) {
         String[] ☃xx = ☃.split(" ", -1);
         String ☃xxx = ☃xx[☃xx.length - 1];

         for (String ☃xxxx : this.playerList.getOnlinePlayerNames()) {
            if (CommandBase.doesStringStartWith(☃xxx, ☃xxxx)) {
               ☃.add(☃xxxx);
            }
         }

         return ☃;
      } else {
         boolean ☃xx = !☃.contains(" ");
         List<String> ☃xxx = this.commandManager.getTabCompletions(☃, ☃, ☃);
         if (!☃xxx.isEmpty()) {
            for (String ☃xxxxx : ☃xxx) {
               if (☃xx && !☃) {
                  ☃.add("/" + ☃xxxxx);
               } else {
                  ☃.add(☃xxxxx);
               }
            }
         }

         return ☃;
      }
   }

   public boolean isAnvilFileSet() {
      return this.anvilFile != null;
   }

   @Override
   public String getName() {
      return "Server";
   }

   @Override
   public void sendMessage(ITextComponent var1) {
      LOGGER.info(☃.getUnformattedText());
   }

   @Override
   public boolean canUseCommand(int var1, String var2) {
      return true;
   }

   public ICommandManager getCommandManager() {
      return this.commandManager;
   }

   public KeyPair getKeyPair() {
      return this.serverKeyPair;
   }

   public String getServerOwner() {
      return this.serverOwner;
   }

   public void setServerOwner(String var1) {
      this.serverOwner = ☃;
   }

   public boolean isSinglePlayer() {
      return this.serverOwner != null;
   }

   public String getFolderName() {
      return this.folderName;
   }

   public void setFolderName(String var1) {
      this.folderName = ☃;
   }

   public void setWorldName(String var1) {
      this.worldName = ☃;
   }

   public String getWorldName() {
      return this.worldName;
   }

   public void setKeyPair(KeyPair var1) {
      this.serverKeyPair = ☃;
   }

   public void setDifficultyForAllWorlds(EnumDifficulty var1) {
      for (WorldServer ☃ : this.worlds) {
         if (☃ != null) {
            if (☃.getWorldInfo().isHardcoreModeEnabled()) {
               ☃.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
               ☃.setAllowedSpawnTypes(true, true);
            } else if (this.isSinglePlayer()) {
               ☃.getWorldInfo().setDifficulty(☃);
               ☃.setAllowedSpawnTypes(☃.getDifficulty() != EnumDifficulty.PEACEFUL, true);
            } else {
               ☃.getWorldInfo().setDifficulty(☃);
               ☃.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
            }
         }
      }
   }

   public boolean allowSpawnMonsters() {
      return true;
   }

   public boolean isDemo() {
      return this.isDemo;
   }

   public void setDemo(boolean var1) {
      this.isDemo = ☃;
   }

   public void canCreateBonusChest(boolean var1) {
      this.enableBonusChest = ☃;
   }

   public ISaveFormat getActiveAnvilConverter() {
      return this.anvilConverterForAnvilFile;
   }

   public String getResourcePackUrl() {
      return this.resourcePackUrl;
   }

   public String getResourcePackHash() {
      return this.resourcePackHash;
   }

   public void setResourcePack(String var1, String var2) {
      this.resourcePackUrl = ☃;
      this.resourcePackHash = ☃;
   }

   @Override
   public void addServerStatsToSnooper(Snooper var1) {
      ☃.addClientStat("whitelist_enabled", false);
      ☃.addClientStat("whitelist_count", 0);
      if (this.playerList != null) {
         ☃.addClientStat("players_current", this.getCurrentPlayerCount());
         ☃.addClientStat("players_max", this.getMaxPlayers());
         ☃.addClientStat("players_seen", this.playerList.getAvailablePlayerDat().length);
      }

      ☃.addClientStat("uses_auth", this.onlineMode);
      ☃.addClientStat("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
      ☃.addClientStat("run_time", (getCurrentTimeMillis() - ☃.getMinecraftStartTimeMillis()) / 60L * 1000L);
      ☃.addClientStat("avg_tick_ms", (int)(MathHelper.average(this.tickTimeArray) * 1.0E-6));
      int ☃ = 0;
      if (this.worlds != null) {
         for (WorldServer ☃x : this.worlds) {
            if (☃x != null) {
               WorldInfo ☃xx = ☃x.getWorldInfo();
               ☃.addClientStat("world[" + ☃ + "][dimension]", ☃x.provider.getDimensionType().getId());
               ☃.addClientStat("world[" + ☃ + "][mode]", ☃xx.getGameType());
               ☃.addClientStat("world[" + ☃ + "][difficulty]", ☃x.getDifficulty());
               ☃.addClientStat("world[" + ☃ + "][hardcore]", ☃xx.isHardcoreModeEnabled());
               ☃.addClientStat("world[" + ☃ + "][generator_name]", ☃xx.getTerrainType().getName());
               ☃.addClientStat("world[" + ☃ + "][generator_version]", ☃xx.getTerrainType().getVersion());
               ☃.addClientStat("world[" + ☃ + "][height]", this.buildLimit);
               ☃.addClientStat("world[" + ☃ + "][chunks_loaded]", ☃x.getChunkProvider().getLoadedChunkCount());
               ☃++;
            }
         }
      }

      ☃.addClientStat("worlds", ☃);
   }

   @Override
   public void addServerTypeToSnooper(Snooper var1) {
      ☃.addStatToSnooper("singleplayer", this.isSinglePlayer());
      ☃.addStatToSnooper("server_brand", this.getServerModName());
      ☃.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
      ☃.addStatToSnooper("dedicated", this.isDedicatedServer());
   }

   @Override
   public boolean isSnooperEnabled() {
      return true;
   }

   public abstract boolean isDedicatedServer();

   public boolean isServerInOnlineMode() {
      return this.onlineMode;
   }

   public void setOnlineMode(boolean var1) {
      this.onlineMode = ☃;
   }

   public boolean getPreventProxyConnections() {
      return this.preventProxyConnections;
   }

   public boolean getCanSpawnAnimals() {
      return this.canSpawnAnimals;
   }

   public void setCanSpawnAnimals(boolean var1) {
      this.canSpawnAnimals = ☃;
   }

   public boolean getCanSpawnNPCs() {
      return this.canSpawnNPCs;
   }

   public abstract boolean shouldUseNativeTransport();

   public void setCanSpawnNPCs(boolean var1) {
      this.canSpawnNPCs = ☃;
   }

   public boolean isPVPEnabled() {
      return this.pvpEnabled;
   }

   public void setAllowPvp(boolean var1) {
      this.pvpEnabled = ☃;
   }

   public boolean isFlightAllowed() {
      return this.allowFlight;
   }

   public void setAllowFlight(boolean var1) {
      this.allowFlight = ☃;
   }

   public abstract boolean isCommandBlockEnabled();

   public String getMOTD() {
      return this.motd;
   }

   public void setMOTD(String var1) {
      this.motd = ☃;
   }

   public int getBuildLimit() {
      return this.buildLimit;
   }

   public void setBuildLimit(int var1) {
      this.buildLimit = ☃;
   }

   public boolean isServerStopped() {
      return this.serverStopped;
   }

   public PlayerList getPlayerList() {
      return this.playerList;
   }

   public void setPlayerList(PlayerList var1) {
      this.playerList = ☃;
   }

   public void setGameType(GameType var1) {
      for (WorldServer ☃ : this.worlds) {
         ☃.getWorldInfo().setGameType(☃);
      }
   }

   public NetworkSystem getNetworkSystem() {
      return this.networkSystem;
   }

   public boolean serverIsInRunLoop() {
      return this.serverIsRunning;
   }

   public boolean getGuiEnabled() {
      return false;
   }

   public abstract String shareToLAN(GameType var1, boolean var2);

   public int getTickCounter() {
      return this.tickCounter;
   }

   public void enableProfiling() {
      this.startProfiling = true;
   }

   public Snooper getPlayerUsageSnooper() {
      return this.usageSnooper;
   }

   @Override
   public World getEntityWorld() {
      return this.worlds[0];
   }

   public boolean isBlockProtected(World var1, BlockPos var2, EntityPlayer var3) {
      return false;
   }

   public boolean getForceGamemode() {
      return this.isGamemodeForced;
   }

   public Proxy getServerProxy() {
      return this.serverProxy;
   }

   public static long getCurrentTimeMillis() {
      return System.currentTimeMillis();
   }

   public int getMaxPlayerIdleMinutes() {
      return this.maxPlayerIdleMinutes;
   }

   public void setPlayerIdleTimeout(int var1) {
      this.maxPlayerIdleMinutes = ☃;
   }

   public MinecraftSessionService getMinecraftSessionService() {
      return this.sessionService;
   }

   public GameProfileRepository getGameProfileRepository() {
      return this.profileRepo;
   }

   public PlayerProfileCache getPlayerProfileCache() {
      return this.profileCache;
   }

   public ServerStatusResponse getServerStatusResponse() {
      return this.statusResponse;
   }

   public void refreshStatusNextTick() {
      this.nanoTimeSinceStatusRefresh = 0L;
   }

   @Nullable
   public Entity getEntityFromUuid(UUID var1) {
      for (WorldServer ☃ : this.worlds) {
         if (☃ != null) {
            Entity ☃x = ☃.getEntityFromUuid(☃);
            if (☃x != null) {
               return ☃x;
            }
         }
      }

      return null;
   }

   @Override
   public boolean sendCommandFeedback() {
      return this.worlds[0].getGameRules().getBoolean("sendCommandFeedback");
   }

   @Override
   public MinecraftServer getServer() {
      return this;
   }

   public int getMaxWorldSize() {
      return 29999984;
   }

   public <V> ListenableFuture<V> callFromMainThread(Callable<V> var1) {
      Validate.notNull(☃);
      if (!this.isCallingFromMinecraftThread() && !this.isServerStopped()) {
         ListenableFutureTask<V> ☃ = ListenableFutureTask.create(☃);
         synchronized (this.futureTaskQueue) {
            this.futureTaskQueue.add(☃);
            return ☃;
         }
      } else {
         try {
            return Futures.immediateFuture(☃.call());
         } catch (Exception var6) {
            return Futures.immediateFailedCheckedFuture(var6);
         }
      }
   }

   @Override
   public ListenableFuture<Object> addScheduledTask(Runnable var1) {
      Validate.notNull(☃);
      return this.callFromMainThread(Executors.callable(☃));
   }

   @Override
   public boolean isCallingFromMinecraftThread() {
      return Thread.currentThread() == this.serverThread;
   }

   public int getNetworkCompressionThreshold() {
      return 256;
   }

   public int getSpawnRadius(@Nullable WorldServer var1) {
      return ☃ != null ? ☃.getGameRules().getInt("spawnRadius") : 10;
   }

   public AdvancementManager getAdvancementManager() {
      return this.worlds[0].getAdvancementManager();
   }

   public FunctionManager getFunctionManager() {
      return this.worlds[0].getFunctionManager();
   }

   public void reload() {
      if (this.isCallingFromMinecraftThread()) {
         this.getPlayerList().saveAllPlayerData();
         this.worlds[0].getLootTableManager().reloadLootTables();
         this.getAdvancementManager().reload();
         this.getFunctionManager().reload();
         this.getPlayerList().reloadResources();
      } else {
         this.addScheduledTask(this::reload);
      }
   }
}
