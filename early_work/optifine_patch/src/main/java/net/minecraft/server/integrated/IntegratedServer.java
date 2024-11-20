/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.util.concurrent.Futures
 *  com.mojang.authlib.GameProfileRepository
 *  com.mojang.authlib.minecraft.MinecraftSessionService
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.net.InetAddress
 *  java.util.Arrays
 *  java.util.List
 *  java.util.Queue
 *  java.util.concurrent.Future
 *  java.util.concurrent.FutureTask
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ThreadLanServerPing
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.command.ServerCommandManager
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketThreadUtil
 *  net.minecraft.network.play.server.SPacketChangeGameState
 *  net.minecraft.profiler.Snooper
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.integrated.IntegratedPlayerList
 *  net.minecraft.server.integrated.IntegratedServerCommandManager
 *  net.minecraft.server.management.PlayerList
 *  net.minecraft.server.management.PlayerProfileCache
 *  net.minecraft.util.CryptManager
 *  net.minecraft.util.HttpUtil
 *  net.minecraft.util.Util
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.EnumDifficulty
 *  net.minecraft.world.GameType
 *  net.minecraft.world.IWorldEventListener
 *  net.minecraft.world.ServerWorldEventHandler
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldServer
 *  net.minecraft.world.WorldServerDemo
 *  net.minecraft.world.WorldServerMulti
 *  net.minecraft.world.WorldSettings
 *  net.minecraft.world.WorldType
 *  net.minecraft.world.storage.ISaveHandler
 *  net.minecraft.world.storage.WorldInfo
 *  net.optifine.ClearWater
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorConstructor
 *  net.optifine.reflect.ReflectorMethod
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server.integrated;

import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedPlayerList;
import net.minecraft.server.integrated.IntegratedServerCommandManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.IWorldEventListener;
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
import net.optifine.reflect.ReflectorConstructor;
import net.optifine.reflect.ReflectorMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer
extends MinecraftServer {
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

    public IntegratedServer(Minecraft clientIn, String folderNameIn, String worldNameIn, WorldSettings worldSettingsIn, YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn, GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn) {
        super(new File(clientIn.gameDir, "saves"), clientIn.getProxy(), clientIn.getDataFixer(), authServiceIn, sessionServiceIn, profileRepoIn, profileCacheIn);
        NBTTagCompound nbt;
        this.setServerOwner(clientIn.getSession().getUsername());
        this.setFolderName(folderNameIn);
        this.setWorldName(worldNameIn);
        this.setDemo(clientIn.isDemo());
        this.canCreateBonusChest(worldSettingsIn.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setPlayerList((PlayerList)new IntegratedPlayerList(this));
        this.mc = clientIn;
        this.worldSettings = this.isDemo() ? WorldServerDemo.DEMO_WORLD_SETTINGS : worldSettingsIn;
        ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(folderNameIn, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo != null && (nbt = worldinfo.getPlayerNBTTagCompound()) != null && nbt.hasKey("Dimension")) {
            int dim;
            PacketThreadUtil.lastDimensionId = dim = nbt.getInteger("Dimension");
            this.mc.loadingScreen.setLoadingProgress(-1);
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
            Integer[] dimIds;
            WorldServer overWorld = this.isDemo() ? (WorldServer)new WorldServerDemo((MinecraftServer)this, isavehandler, worldinfo, 0, this.profiler).b() : (WorldServer)new WorldServer((MinecraftServer)this, isavehandler, worldinfo, 0, this.profiler).init();
            overWorld.initialize(this.worldSettings);
            Integer[] integerArray = dimIds = (Integer[])Reflector.call((ReflectorMethod)Reflector.DimensionManager_getStaticDimensionIDs, (Object[])new Object[0]);
            int n = integerArray.length;
            for (int i = 0; i < n; ++i) {
                int dim = integerArray[i];
                WorldServer world = dim == 0 ? overWorld : (WorldServer)new WorldServerMulti((MinecraftServer)this, isavehandler, dim, overWorld, this.profiler).init();
                world.a((IWorldEventListener)new ServerWorldEventHandler((MinecraftServer)this, world));
                if (!this.isSinglePlayer()) {
                    world.V().setGameType(this.getGameType());
                }
                if (!Reflector.EventBus.exists()) continue;
                Reflector.postForgeBusEvent((ReflectorConstructor)Reflector.WorldEvent_Load_Constructor, (Object[])new Object[]{world});
            }
            this.getPlayerList().setPlayerManager(new WorldServer[]{overWorld});
            if (overWorld.V().getDifficulty() == null) {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        } else {
            for (int i = 0; i < this.worlds.length; ++i) {
                int j = 0;
                if (i == 1) {
                    j = -1;
                }
                if (i == 2) {
                    j = 1;
                }
                if (i == 0) {
                    this.worlds[i] = this.isDemo() ? (WorldServer)new WorldServerDemo((MinecraftServer)this, isavehandler, worldinfo, j, this.profiler).b() : (WorldServer)new WorldServer((MinecraftServer)this, isavehandler, worldinfo, j, this.profiler).init();
                    this.worlds[i].initialize(this.worldSettings);
                } else {
                    this.worlds[i] = (WorldServer)new WorldServerMulti((MinecraftServer)this, isavehandler, j, this.worlds[0], this.profiler).init();
                }
                this.worlds[i].a((IWorldEventListener)new ServerWorldEventHandler((MinecraftServer)this, this.worlds[i]));
            }
            this.getPlayerList().setPlayerManager(this.worlds);
            if (this.worlds[0].V().getDifficulty() == null) {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        }
        this.initialWorldChunkLoad();
    }

    public boolean init() throws IOException {
        Object inst;
        LOGGER.info("Starting integrated minecraft server version 1.12.2");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        LOGGER.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
            inst = Reflector.call((ReflectorMethod)Reflector.FMLCommonHandler_instance, (Object[])new Object[0]);
            if (!Reflector.callBoolean((Object)inst, (ReflectorMethod)Reflector.FMLCommonHandler_handleServerAboutToStart, (Object[])new Object[]{this})) {
                return false;
            }
        }
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.worldSettings.getSeed(), this.worldSettings.getTerrainType(), this.worldSettings.getGeneratorOptions());
        this.setMOTD(this.getServerOwner() + " - " + this.worlds[0].V().getWorldName());
        if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
            inst = Reflector.call((ReflectorMethod)Reflector.FMLCommonHandler_instance, (Object[])new Object[0]);
            if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE) {
                return Reflector.callBoolean((Object)inst, (ReflectorMethod)Reflector.FMLCommonHandler_handleServerStarting, (Object[])new Object[]{this});
            }
            Reflector.callVoid((Object)inst, (ReflectorMethod)Reflector.FMLCommonHandler_handleServerStarting, (Object[])new Object[]{this});
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void tick() {
        this.onTick();
        boolean flag = this.isGamePaused;
        boolean bl = this.isGamePaused = Minecraft.getMinecraft().getConnection() != null && Minecraft.getMinecraft().isGamePaused();
        if (!flag && this.isGamePaused) {
            LOGGER.info("Saving and pausing game...");
            this.getPlayerList().saveAllPlayerData();
            this.saveAllWorlds(false);
        }
        if (this.isGamePaused) {
            Queue queue = this.futureTaskQueue;
            synchronized (queue) {
                while (!this.futureTaskQueue.isEmpty()) {
                    Util.runTask((FutureTask)((FutureTask)this.futureTaskQueue.poll()), (Logger)LOGGER);
                }
            }
        } else {
            super.tick();
            if (this.mc.gameSettings.renderDistanceChunks != this.getPlayerList().getViewDistance()) {
                LOGGER.info("Changing view distance to {}, from {}", (Object)this.mc.gameSettings.renderDistanceChunks, (Object)this.getPlayerList().getViewDistance());
                this.getPlayerList().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
            }
            if (this.mc.world != null) {
                WorldInfo worldinfo1 = this.worlds[0].V();
                WorldInfo worldinfo = this.mc.world.getWorldInfo();
                if (!worldinfo1.isDifficultyLocked() && worldinfo.getDifficulty() != worldinfo1.getDifficulty()) {
                    LOGGER.info("Changing difficulty to {}, from {}", (Object)worldinfo.getDifficulty(), (Object)worldinfo1.getDifficulty());
                    this.setDifficultyForAllWorlds(worldinfo.getDifficulty());
                } else if (worldinfo.isDifficultyLocked() && !worldinfo1.isDifficultyLocked()) {
                    LOGGER.info("Locking difficulty to {}", (Object)worldinfo.getDifficulty());
                    for (WorldServer worldserver : this.worlds) {
                        if (worldserver == null) continue;
                        worldserver.V().setDifficultyLocked(true);
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
        if (this.mc.world == null) {
            return this.mc.gameSettings.difficulty;
        }
        return this.mc.world.getWorldInfo().getDifficulty();
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
            int ticksSaveInterval;
            int ticks = this.getTickCounter();
            if ((long)ticks < this.ticksSaveLast + (long)(ticksSaveInterval = this.mc.gameSettings.ofAutoSaveTicks)) {
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
        report.getCategory().addDetail("Type", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        report.getCategory().addDetail("Is Modded", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
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
        playerSnooper.addClientStat("snooper_partner", (Object)this.mc.getPlayerUsageSnooper().getUniqueID());
    }

    public boolean isSnooperEnabled() {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    public String shareToLAN(GameType type, boolean allowCheats) {
        try {
            int i = -1;
            try {
                i = HttpUtil.getSuitableLanPort();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (i <= 0) {
                i = 25564;
            }
            this.getNetworkSystem().addEndpoint((InetAddress)null, i);
            LOGGER.info("Started on {}", (Object)i);
            this.isPublic = true;
            this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), i + "");
            this.lanServerPing.start();
            this.getPlayerList().setGameType(type);
            this.getPlayerList().setCommandsAllowedForAll(allowCheats);
            this.mc.player.setPermissionLevel(allowCheats ? 4 : 0);
            return i + "";
        }
        catch (IOException var6) {
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
            Futures.getUnchecked((Future)this.addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */));
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
        List iws = Arrays.asList((Object[])this.worlds);
        for (WorldServer ws : iws) {
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
            ClearWater.updateWaterOpacity((GameSettings)Config.getGameSettings(), (World)ws);
        }
        if (this.difficultyUpdateWorld == ws && this.difficultyUpdatePos != null) {
            this.difficultyLast = ws.D(this.difficultyUpdatePos);
            this.difficultyUpdateWorld = null;
            this.difficultyUpdatePos = null;
        }
    }

    private void fixWorldWeather(WorldServer ws) {
        WorldInfo worldInfo = ws.V();
        if (worldInfo.isRaining() || worldInfo.isThundering()) {
            worldInfo.setRainTime(0);
            worldInfo.setRaining(false);
            ws.k(0.0f);
            worldInfo.setThunderTime(0);
            worldInfo.setThundering(false);
            ws.i(0.0f);
            this.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketChangeGameState(2, 0.0f));
            this.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketChangeGameState(7, 0.0f));
            this.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketChangeGameState(8, 0.0f));
        }
    }

    private void fixWorldTime(WorldServer ws) {
        WorldInfo worldInfo = ws.V();
        if (worldInfo.getGameType().getID() != 1) {
            return;
        }
        long time = ws.S();
        long timeOfDay = time % 24000L;
        if (Config.isTimeDayOnly()) {
            if (timeOfDay <= 1000L) {
                ws.b(time - timeOfDay + 1001L);
            }
            if (timeOfDay >= 11000L) {
                ws.b(time - timeOfDay + 24001L);
            }
        }
        if (Config.isTimeNightOnly()) {
            if (timeOfDay <= 14000L) {
                ws.b(time - timeOfDay + 14001L);
            }
            if (timeOfDay >= 22000L) {
                ws.b(time - timeOfDay + 24000L + 14001L);
            }
        }
    }

    static /* synthetic */ Minecraft access$000(IntegratedServer x0) {
        return x0.mc;
    }
}
