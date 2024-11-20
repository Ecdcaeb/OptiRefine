/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.Futures
 *  com.mojang.authlib.GameProfileRepository
 *  com.mojang.authlib.minecraft.MinecraftSessionService
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.net.InetAddress
 *  java.util.Queue
 *  java.util.concurrent.Future
 *  java.util.concurrent.FutureTask
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ThreadLanServerPing
 *  net.minecraft.command.ServerCommandManager
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.profiler.Snooper
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.integrated.IntegratedPlayerList
 *  net.minecraft.server.integrated.IntegratedServerCommandManager
 *  net.minecraft.server.management.PlayerList
 *  net.minecraft.server.management.PlayerProfileCache
 *  net.minecraft.util.CryptManager
 *  net.minecraft.util.HttpUtil
 *  net.minecraft.util.Util
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
 *  net.minecraftforge.common.DimensionManager
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
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
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedPlayerList;
import net.minecraft.server.integrated.IntegratedServerCommandManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
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
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(value=Side.CLIENT)
public class IntegratedServer
extends MinecraftServer {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft mc;
    private final WorldSettings worldSettings;
    private boolean isGamePaused;
    private boolean isPublic;
    private ThreadLanServerPing lanServerPing;

    public IntegratedServer(Minecraft clientIn, String folderNameIn, String worldNameIn, WorldSettings worldSettingsIn, YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn, GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn) {
        super(new File(clientIn.gameDir, "saves"), clientIn.getProxy(), clientIn.getDataFixer(), authServiceIn, sessionServiceIn, profileRepoIn, profileCacheIn);
        this.setServerOwner(clientIn.getSession().getUsername());
        this.setFolderName(folderNameIn);
        this.setWorldName(worldNameIn);
        this.setDemo(clientIn.isDemo());
        this.canCreateBonusChest(worldSettingsIn.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setPlayerList((PlayerList)new IntegratedPlayerList(this));
        this.mc = clientIn;
        this.worldSettings = this.isDemo() ? WorldServerDemo.DEMO_WORLD_SETTINGS : worldSettingsIn;
    }

    public ServerCommandManager createCommandManager() {
        return new IntegratedServerCommandManager(this);
    }

    public void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions) {
        this.convertMapIfNeeded(saveName);
        ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(saveName, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo == null) {
            worldinfo = new WorldInfo(this.worldSettings, worldNameIn);
        } else {
            worldinfo.setWorldName(worldNameIn);
        }
        WorldServer overWorld = this.isDemo() ? (WorldServer)new WorldServerDemo((MinecraftServer)this, isavehandler, worldinfo, 0, this.profiler).b() : (WorldServer)new WorldServer((MinecraftServer)this, isavehandler, worldinfo, 0, this.profiler).init();
        overWorld.initialize(this.worldSettings);
        Integer[] integerArray = DimensionManager.getStaticDimensionIDs();
        int n = integerArray.length;
        for (int i = 0; i < n; ++i) {
            int dim = integerArray[i];
            WorldServer world = dim == 0 ? overWorld : (WorldServer)new WorldServerMulti((MinecraftServer)this, isavehandler, dim, overWorld, this.profiler).init();
            world.a((IWorldEventListener)new ServerWorldEventHandler((MinecraftServer)this, world));
            if (!this.isSinglePlayer()) {
                world.V().setGameType(this.getGameType());
            }
            MinecraftForge.EVENT_BUS.post((Event)new WorldEvent.Load((World)world));
        }
        this.getPlayerList().setPlayerManager(new WorldServer[]{overWorld});
        if (overWorld.V().getDifficulty() == null) {
            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
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
        if (!FMLCommonHandler.instance().handleServerAboutToStart((MinecraftServer)this)) {
            return false;
        }
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.worldSettings.getSeed(), this.worldSettings.getTerrainType(), this.worldSettings.getGeneratorOptions());
        this.setMOTD(this.getServerOwner() + " - " + this.worlds[0].V().getWorldName());
        return FMLCommonHandler.instance().handleServerStarting((MinecraftServer)this);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void tick() {
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
            this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), "" + i);
            this.lanServerPing.start();
            this.getPlayerList().setGameType(type);
            this.getPlayerList().setCommandsAllowedForAll(allowCheats);
            this.mc.player.setPermissionLevel(allowCheats ? 4 : 0);
            return "" + i;
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
        if (this.isServerRunning()) {
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
}
