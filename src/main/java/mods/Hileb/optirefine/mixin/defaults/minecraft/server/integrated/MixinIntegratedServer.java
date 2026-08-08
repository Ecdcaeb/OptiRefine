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
import net.minecraft.world.EnumDifficulty;
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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field ticksSaveLast (in OF IntegratedServer, not in baseline), MCP name matches
    @Unique
    private long ticksSaveLast;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added fields difficultyUpdateWorld/difficultyUpdatePos/difficultyLast (in OF, not in baseline; public in OF)
    @Unique
    public World difficultyUpdateWorld;
    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public BlockPos difficultyUpdatePos;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public DifficultyInstance difficultyLast;

    @Shadow @Final
// [AUDIT-OK] baseline member mc exists in target class
    private Minecraft mc;

// [AUDIT-OK] baseline member tick() exists in target class (overrides MinecraftServer)
    @Shadow public abstract void tick();

    @SuppressWarnings("unused")
    public MixinIntegratedServer(File p_i47054_1_, Proxy p_i47054_2_, DataFixer p_i47054_3_, YggdrasilAuthenticationService p_i47054_4_, MinecraftSessionService p_i47054_5_, GameProfileRepository p_i47054_6_, PlayerProfileCache p_i47054_7_) {
        super(p_i47054_1_, p_i47054_2_, p_i47054_3_, p_i47054_4_, p_i47054_5_, p_i47054_6_, p_i47054_7_);
    }

    @SuppressWarnings("unused")
    @AccessibleOperation(opcode = Opcodes.PUTSTATIC, desc = "net.minecraft.network.PacketThreadUtil lastDimensionId I")
// [AUDIT-OK] OF member PacketThreadUtil.lastDimensionId (in OF PacketThreadUtil, not in baseline; provided by MixinPacketThreadUtil), MCP name OK
    private static native void _set_PacketThreadUtil_lastDimensionId_(int dim);

    @Inject(method = "<init>", at = @At("RETURN"))
// [AUDIT-OK] <init> RETURN matches OF ctor (Dimension NBT -> lastDimensionId + loadingScreen progress -1)
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
// [AUDIT-OK] tick HEAD onTick() matches OF tick start
    public void injectTick(CallbackInfo ci) {
        this.onTick();
    }

    @Inject(method = "setDifficultyForAllWorlds", at = @At("TAIL"))
// [AUDIT-FIXED] OF:332-337 after super.setDifficultyForAllWorlds syncs the client world's difficulty (mc @Shadow already present)
    public void injectSetDifficultyForAllWorlds(EnumDifficulty difficulty, CallbackInfo ci) {
        if (this.mc.world != null) {
            this.mc.world.getWorldInfo().setDifficulty(difficulty);
        }
    }

    @SuppressWarnings("unused")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofAutoSaveTicks I")
// [AUDIT-OK] OF member GameSettings.ofAutoSaveTicks (in OF GameSettings, not in baseline; provided by MixinGameSettings), MCP name OK
    private static int _acc_GameSettings_ofAutoSaveTicks_(GameSettings settings) {
        throw new AbstractMethodError();
    }

    @Inject(method = "saveAllWorlds", at = @At("HEAD"), cancellable = true)
// [AUDIT-OK] cancellable HEAD matches OF saveAllWorlds (silent autosave throttled by ticksSaveLast + ofAutoSaveTicks, else super)
    public void injectSaveAllWorlds(boolean isSilent, CallbackInfo ci) {
        if (isSilent) {
            int ticks = this.getTickCounter();
            if (ticks < (this.ticksSaveLast + _acc_GameSettings_ofAutoSaveTicks_(this.mc.gameSettings))) {
                ci.cancel();
                return;
            }
            this.ticksSaveLast = ticks;
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added member onTick() loop, matches OF
    private void onTick() {
        for (WorldServer ws : this.worlds) {
            this.onTick(ws);
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added member getDifficultyAsync, matches OF
    public DifficultyInstance getDifficultyAsync(World world, BlockPos blockPos) {
        this.difficultyUpdateWorld = world;
        this.difficultyUpdatePos = blockPos;
        return this.difficultyLast;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added member onTick(WorldServer), matches OF (time/weather/waterOpacity/difficulty)
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
// [AUDIT-OK] OF-added member fixWorldWeather, matches OF
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
// [AUDIT-OK] OF-added member fixWorldTime, matches OF
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


    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.ticksSaveLast = 0L;
        this.difficultyUpdateWorld = null;
        this.difficultyUpdatePos = null;
        this.difficultyLast = null;
    }
}
