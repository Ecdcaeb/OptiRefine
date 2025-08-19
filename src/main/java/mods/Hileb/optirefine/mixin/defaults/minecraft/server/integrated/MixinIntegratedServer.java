package mods.Hileb.optirefine.mixin.defaults.minecraft.server.integrated;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.ClearWater;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.net.Proxy;

@Mixin(IntegratedServer.class)
public abstract class MixinIntegratedServer extends MinecraftServer {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private long ticksSaveLast = 0L;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public World difficultyUpdateWorld = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public BlockPos difficultyUpdatePos = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public DifficultyInstance difficultyLast = null;

    @Shadow @Final
    private Minecraft mc;

    @Shadow public abstract void tick();

    @SuppressWarnings("unused")
    public MixinIntegratedServer(File p_i47054_1_, Proxy p_i47054_2_, DataFixer p_i47054_3_, YggdrasilAuthenticationService p_i47054_4_, MinecraftSessionService p_i47054_5_, GameProfileRepository p_i47054_6_, PlayerProfileCache p_i47054_7_) {
        super(p_i47054_1_, p_i47054_2_, p_i47054_3_, p_i47054_4_, p_i47054_5_, p_i47054_6_, p_i47054_7_);
    }

    @SuppressWarnings("unused")
    @AccessibleOperation(opcode = Opcodes.PUTSTATIC, desc = "net.minecraft.network.PacketThreadUtil lastDimensionId I")
    private static native void _set_PacketThreadUtil_lastDimensionId_(int dim);

    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectInit(Minecraft clientIn, String folderNameIn, String worldNameIn, WorldSettings worldSettingsIn, YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn, GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn, CallbackInfo ci){
        NBTTagCompound nbt;
        ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(folderNameIn, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo != null && (nbt = worldinfo.getPlayerNBTTagCompound()) != null && nbt.hasKey("Dimension")) {
            _set_PacketThreadUtil_lastDimensionId_(nbt.getInteger("Dimension"));
            this.mc.loadingScreen.setLoadingProgress(-1);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void injectTick(CallbackInfo ci) {
        this.onTick();
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofAutoSaveTicks I")
    private static int _acc_GameSettings_ofAutoSaveTicks_(GameSettings settings) {
        throw new AbstractMethodError();
    }

    @Inject(method = "saveAllWorlds", at = @At("HEAD"))
    public void injectSaveAllWorlds(boolean isSilent, CallbackInfo ci) {
        if (isSilent) {
            int ticks = this.getTickCounter();
            if (ticks < (this.ticksSaveLast + _acc_GameSettings_ofAutoSaveTicks_(this.mc.gameSettings))) {
                return;
            }
            this.ticksSaveLast = ticks;
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    private void onTick() {
        for (WorldServer ws : this.worlds) {
            this.onTick(ws);
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    public DifficultyInstance getDifficultyAsync(World world, BlockPos blockPos) {
        this.difficultyUpdateWorld = world;
        this.difficultyUpdatePos = blockPos;
        return this.difficultyLast;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private void fixWorldWeather(WorldServer ws) {
        WorldInfo worldInfo = ws.getWorldInfo();
        if (worldInfo.isRaining() || worldInfo.isThundering()) {
            worldInfo.setRainTime(0);
            worldInfo.setRaining(false);
            ws.setRainStrength(0.0f);
            worldInfo.setThunderTime(0);
            worldInfo.setThundering(false);
            ws.setThunderStrength(0.0f);
            this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(2, 0.0f));
            this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(7, 0.0f));
            this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(8, 0.0f));
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private void fixWorldTime(WorldServer ws) {
        WorldInfo worldInfo = ws.getWorldInfo();
        if (worldInfo.getGameType().getID() != 1) {
            return;
        }
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
/*
--- net/minecraft/server/integrated/IntegratedServer.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/server/integrated/IntegratedServer.java	Tue Aug 19 14:59:58 2025
@@ -4,45 +4,60 @@
 import com.google.common.util.concurrent.Futures;
 import com.mojang.authlib.GameProfileRepository;
 import com.mojang.authlib.minecraft.MinecraftSessionService;
 import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
 import java.io.File;
 import java.io.IOException;
+import java.net.InetAddress;
+import java.util.Arrays;
+import java.util.concurrent.FutureTask;
 import net.minecraft.client.ClientBrandRetriever;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.multiplayer.ThreadLanServerPing;
 import net.minecraft.command.ServerCommandManager;
 import net.minecraft.crash.CrashReport;
 import net.minecraft.crash.ICrashReportDetail;
 import net.minecraft.entity.player.EntityPlayerMP;
+import net.minecraft.nbt.NBTTagCompound;
+import net.minecraft.network.PacketThreadUtil;
+import net.minecraft.network.play.server.SPacketChangeGameState;
 import net.minecraft.profiler.Snooper;
 import net.minecraft.server.MinecraftServer;
 import net.minecraft.server.management.PlayerProfileCache;
 import net.minecraft.util.CryptManager;
 import net.minecraft.util.HttpUtil;
 import net.minecraft.util.Util;
+import net.minecraft.util.math.BlockPos;
+import net.minecraft.world.DifficultyInstance;
 import net.minecraft.world.EnumDifficulty;
 import net.minecraft.world.GameType;
 import net.minecraft.world.ServerWorldEventHandler;
+import net.minecraft.world.World;
 import net.minecraft.world.WorldServer;
 import net.minecraft.world.WorldServerDemo;
 import net.minecraft.world.WorldServerMulti;
 import net.minecraft.world.WorldSettings;
 import net.minecraft.world.WorldType;
 import net.minecraft.world.storage.ISaveHandler;
 import net.minecraft.world.storage.WorldInfo;
+import net.optifine.ClearWater;
+import net.optifine.reflect.Reflector;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class IntegratedServer extends MinecraftServer {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft mc;
    private final WorldSettings worldSettings;
    private boolean isGamePaused;
    private boolean isPublic;
    private ThreadLanServerPing lanServerPing;
+   private long ticksSaveLast = 0L;
+   public World difficultyUpdateWorld = null;
+   public BlockPos difficultyUpdatePos = null;
+   public DifficultyInstance difficultyLast = null;

    public IntegratedServer(
       Minecraft var1,
       String var2,
       String var3,
       WorldSettings var4,
@@ -58,93 +73,152 @@
       this.setDemo(var1.isDemo());
       this.canCreateBonusChest(var4.isBonusChestEnabled());
       this.setBuildLimit(256);
       this.setPlayerList(new IntegratedPlayerList(this));
       this.mc = var1;
       this.worldSettings = this.isDemo() ? WorldServerDemo.DEMO_WORLD_SETTINGS : var4;
+      ISaveHandler var9 = this.getActiveAnvilConverter().getSaveLoader(var2, false);
+      WorldInfo var10 = var9.loadWorldInfo();
+      if (var10 != null) {
+         NBTTagCompound var11 = var10.getPlayerNBTTagCompound();
+         if (var11 != null && var11.hasKey("Dimension")) {
+            int var12 = var11.getInteger("Dimension");
+            PacketThreadUtil.lastDimensionId = var12;
+            this.mc.loadingScreen.setLoadingProgress(-1);
+         }
+      }
    }

-   protected ServerCommandManager createCommandManager() {
+   public ServerCommandManager createCommandManager() {
       return new IntegratedServerCommandManager(this);
    }

-   protected void loadAllWorlds(String var1, String var2, long var3, WorldType var5, String var6) {
+   public void loadAllWorlds(String var1, String var2, long var3, WorldType var5, String var6) {
       this.convertMapIfNeeded(var1);
-      this.worlds = new WorldServer[3];
-      this.timeOfLastDimensionTick = new long[this.worlds.length][100];
-      ISaveHandler var7 = this.getActiveAnvilConverter().getSaveLoader(var1, true);
-      this.setResourcePackFromWorld(this.getFolderName(), var7);
-      WorldInfo var8 = var7.loadWorldInfo();
-      if (var8 == null) {
-         var8 = new WorldInfo(this.worldSettings, var2);
+      boolean var7 = Reflector.DimensionManager.exists();
+      if (!var7) {
+         this.worlds = new WorldServer[3];
+         this.timeOfLastDimensionTick = new long[this.worlds.length][100];
+      }
+
+      ISaveHandler var8 = this.getActiveAnvilConverter().getSaveLoader(var1, true);
+      this.setResourcePackFromWorld(this.getFolderName(), var8);
+      WorldInfo var9 = var8.loadWorldInfo();
+      if (var9 == null) {
+         var9 = new WorldInfo(this.worldSettings, var2);
       } else {
-         var8.setWorldName(var2);
+         var9.setWorldName(var2);
       }

-      for (int var9 = 0; var9 < this.worlds.length; var9++) {
-         byte var10 = 0;
-         if (var9 == 1) {
-            var10 = -1;
+      if (var7) {
+         WorldServer var10 = this.isDemo()
+            ? (WorldServer)new WorldServerDemo(this, var8, var9, 0, this.profiler).b()
+            : (WorldServer)new WorldServer(this, var8, var9, 0, this.profiler).init();
+         var10.initialize(this.worldSettings);
+         Integer[] var11 = (Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs, new Object[0]);
+         Integer[] var12 = var11;
+         int var13 = var11.length;
+
+         for (int var14 = 0; var14 < var13; var14++) {
+            int var15 = var12[var14];
+            WorldServer var16 = var15 == 0 ? var10 : (WorldServer)new WorldServerMulti(this, var8, var15, var10, this.profiler).init();
+            var16.addEventListener(new ServerWorldEventHandler(this, var16));
+            if (!this.isSinglePlayer()) {
+               var16.getWorldInfo().setGameType(this.getGameType());
+            }
+
+            if (Reflector.EventBus.exists()) {
+               Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[]{var16});
+            }
          }

-         if (var9 == 2) {
-            var10 = 1;
+         this.getPlayerList().setPlayerManager(new WorldServer[]{var10});
+         if (var10.getWorldInfo().getDifficulty() == null) {
+            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
          }
+      } else {
+         for (int var17 = 0; var17 < this.worlds.length; var17++) {
+            byte var18 = 0;
+            if (var17 == 1) {
+               var18 = -1;
+            }
+
+            if (var17 == 2) {
+               var18 = 1;
+            }

-         if (var9 == 0) {
-            if (this.isDemo()) {
-               this.worlds[var9] = (WorldServer)new WorldServerDemo(this, var7, var8, var10, this.profiler).init();
+            if (var17 == 0) {
+               if (this.isDemo()) {
+                  this.worlds[var17] = (WorldServer)new WorldServerDemo(this, var8, var9, var18, this.profiler).b();
+               } else {
+                  this.worlds[var17] = (WorldServer)new WorldServer(this, var8, var9, var18, this.profiler).init();
+               }
+
+               this.worlds[var17].initialize(this.worldSettings);
             } else {
-               this.worlds[var9] = (WorldServer)new WorldServer(this, var7, var8, var10, this.profiler).init();
+               this.worlds[var17] = (WorldServer)new WorldServerMulti(this, var8, var18, this.worlds[0], this.profiler).init();
             }

-            this.worlds[var9].initialize(this.worldSettings);
-         } else {
-            this.worlds[var9] = (WorldServer)new WorldServerMulti(this, var7, var10, this.worlds[0], this.profiler).init();
+            this.worlds[var17].addEventListener(new ServerWorldEventHandler(this, this.worlds[var17]));
          }

-         this.worlds[var9].addEventListener(new ServerWorldEventHandler(this, this.worlds[var9]));
-      }
-
-      this.getPlayerList().setPlayerManager(this.worlds);
-      if (this.worlds[0].getWorldInfo().getDifficulty() == null) {
-         this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
+         this.getPlayerList().setPlayerManager(this.worlds);
+         if (this.worlds[0].getWorldInfo().getDifficulty() == null) {
+            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
+         }
       }

       this.initialWorldChunkLoad();
    }

-   protected boolean init() throws IOException {
+   public boolean init() throws IOException {
       LOGGER.info("Starting integrated minecraft server version 1.12.2");
       this.setOnlineMode(true);
       this.setCanSpawnAnimals(true);
       this.setCanSpawnNPCs(true);
       this.setAllowPvp(true);
       this.setAllowFlight(true);
       LOGGER.info("Generating keypair");
       this.setKeyPair(CryptManager.generateKeyPair());
+      if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
+         Object var1 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
+         if (!Reflector.callBoolean(var1, Reflector.FMLCommonHandler_handleServerAboutToStart, new Object[]{this})) {
+            return false;
+         }
+      }
+
       this.loadAllWorlds(
          this.getFolderName(), this.getWorldName(), this.worldSettings.getSeed(), this.worldSettings.getTerrainType(), this.worldSettings.getGeneratorOptions()
       );
       this.setMOTD(this.getServerOwner() + " - " + this.worlds[0].getWorldInfo().getWorldName());
+      if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
+         Object var2 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
+         if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == boolean.class) {
+            return Reflector.callBoolean(var2, Reflector.FMLCommonHandler_handleServerStarting, new Object[]{this});
+         }
+
+         Reflector.callVoid(var2, Reflector.FMLCommonHandler_handleServerStarting, new Object[]{this});
+      }
+
       return true;
    }

-   protected void tick() {
+   public void tick() {
+      this.onTick();
       boolean var1 = this.isGamePaused;
       this.isGamePaused = Minecraft.getMinecraft().getConnection() != null && Minecraft.getMinecraft().isGamePaused();
       if (!var1 && this.isGamePaused) {
          LOGGER.info("Saving and pausing game...");
          this.getPlayerList().saveAllPlayerData();
          this.saveAllWorlds(false);
       }

       if (this.isGamePaused) {
          synchronized (this.futureTaskQueue) {
             while (!this.futureTaskQueue.isEmpty()) {
-               Util.runTask(this.futureTaskQueue.poll(), LOGGER);
+               Util.runTask((FutureTask)this.futureTaskQueue.poll(), LOGGER);
             }
          }
       } else {
          super.tick();
          if (this.mc.gameSettings.renderDistanceChunks != this.getPlayerList().getViewDistance()) {
             LOGGER.info("Changing view distance to {}, from {}", this.mc.gameSettings.renderDistanceChunks, this.getPlayerList().getViewDistance());
@@ -176,13 +250,13 @@

    public GameType getGameType() {
       return this.worldSettings.getGameType();
    }

    public EnumDifficulty getDifficulty() {
-      return this.mc.world.getWorldInfo().getDifficulty();
+      return this.mc.world == null ? this.mc.gameSettings.difficulty : this.mc.world.getWorldInfo().getDifficulty();
    }

    public boolean isHardcore() {
       return this.worldSettings.getHardcoreEnabled();
    }

@@ -191,13 +265,23 @@
    }

    public boolean shouldBroadcastConsoleToOps() {
       return true;
    }

-   protected void saveAllWorlds(boolean var1) {
+   public void saveAllWorlds(boolean var1) {
+      if (var1) {
+         int var2 = this.getTickCounter();
+         int var3 = this.mc.gameSettings.ofAutoSaveTicks;
+         if (var2 < this.ticksSaveLast + var3) {
+            return;
+         }
+
+         this.ticksSaveLast = var2;
+      }
+
       super.saveAllWorlds(var1);
    }

    public File getDataDirectory() {
       return this.mc.gameDir;
    }
@@ -207,13 +291,13 @@
    }

    public boolean shouldUseNativeTransport() {
       return false;
    }

-   protected void finalTick(CrashReport var1) {
+   public void finalTick(CrashReport var1) {
       this.mc.crashed(var1);
    }

    public CrashReport addServerInfoToCrashReport(CrashReport var1) {
       var1 = super.addServerInfoToCrashReport(var1);
       var1.getCategory().addDetail("Type", new ICrashReportDetail<String>() {
@@ -271,13 +355,13 @@
          }

          if (var3 <= 0) {
             var3 = 25564;
          }

-         this.getNetworkSystem().addEndpoint(null, var3);
+         this.getNetworkSystem().addEndpoint((InetAddress)null, var3);
          LOGGER.info("Started on {}", var3);
          this.isPublic = true;
          this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), var3 + "");
          this.lanServerPing.start();
          this.getPlayerList().setGameType(var1);
          this.getPlayerList().setCommandsAllowedForAll(var2);
@@ -294,21 +378,24 @@
          this.lanServerPing.interrupt();
          this.lanServerPing = null;
       }
    }

    public void initiateShutdown() {
-      Futures.getUnchecked(this.addScheduledTask(new Runnable() {
-         public void run() {
-            for (EntityPlayerMP var3 : Lists.newArrayList(IntegratedServer.this.getPlayerList().getPlayers())) {
-               if (!var3.getUniqueID().equals(IntegratedServer.this.mc.player.getUniqueID())) {
-                  IntegratedServer.this.getPlayerList().playerLoggedOut(var3);
+      if (!Reflector.MinecraftForge.exists() || this.isServerRunning()) {
+         Futures.getUnchecked(this.addScheduledTask(new Runnable() {
+            public void run() {
+               for (EntityPlayerMP var2 : Lists.newArrayList(IntegratedServer.this.getPlayerList().getPlayers())) {
+                  if (!var2.getUniqueID().equals(IntegratedServer.this.mc.player.getUniqueID())) {
+                     IntegratedServer.this.getPlayerList().playerLoggedOut(var2);
+                  }
                }
             }
-         }
-      }));
+         }));
+      }
+
       super.initiateShutdown();
       if (this.lanServerPing != null) {
          this.lanServerPing.interrupt();
          this.lanServerPing = null;
       }
    }
@@ -325,8 +412,83 @@
    public boolean isCommandBlockEnabled() {
       return true;
    }

    public int getOpPermissionLevel() {
       return 4;
+   }
+
+   private void onTick() {
+      for (WorldServer var3 : Arrays.asList(this.worlds)) {
+         this.onTick(var3);
+      }
+   }
+
+   public DifficultyInstance getDifficultyAsync(World var1, BlockPos var2) {
+      this.difficultyUpdateWorld = var1;
+      this.difficultyUpdatePos = var2;
+      return this.difficultyLast;
+   }
+
+   private void onTick(WorldServer var1) {
+      if (!Config.isTimeDefault()) {
+         this.fixWorldTime(var1);
+      }
+
+      if (!Config.isWeatherEnabled()) {
+         this.fixWorldWeather(var1);
+      }
+
+      if (Config.waterOpacityChanged) {
+         Config.waterOpacityChanged = false;
+         ClearWater.updateWaterOpacity(Config.getGameSettings(), var1);
+      }
+
+      if (this.difficultyUpdateWorld == var1 && this.difficultyUpdatePos != null) {
+         this.difficultyLast = var1.getDifficultyForLocation(this.difficultyUpdatePos);
+         this.difficultyUpdateWorld = null;
+         this.difficultyUpdatePos = null;
+      }
+   }
+
+   private void fixWorldWeather(WorldServer var1) {
+      WorldInfo var2 = var1.getWorldInfo();
+      if (var2.isRaining() || var2.isThundering()) {
+         var2.setRainTime(0);
+         var2.setRaining(false);
+         var1.setRainStrength(0.0F);
+         var2.setThunderTime(0);
+         var2.setThundering(false);
+         var1.setThunderStrength(0.0F);
+         this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(2, 0.0F));
+         this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(7, 0.0F));
+         this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(8, 0.0F));
+      }
+   }
+
+   private void fixWorldTime(WorldServer var1) {
+      WorldInfo var2 = var1.getWorldInfo();
+      if (var2.getGameType().getID() == 1) {
+         long var3 = var1.getWorldTime();
+         long var5 = var3 % 24000L;
+         if (Config.isTimeDayOnly()) {
+            if (var5 <= 1000L) {
+               var1.setWorldTime(var3 - var5 + 1001L);
+            }
+
+            if (var5 >= 11000L) {
+               var1.setWorldTime(var3 - var5 + 24001L);
+            }
+         }
+
+         if (Config.isTimeNightOnly()) {
+            if (var5 <= 14000L) {
+               var1.setWorldTime(var3 - var5 + 14001L);
+            }
+
+            if (var5 >= 22000L) {
+               var1.setWorldTime(var3 - var5 + 24000L + 14001L);
+            }
+         }
+      }
    }
 }
 */
