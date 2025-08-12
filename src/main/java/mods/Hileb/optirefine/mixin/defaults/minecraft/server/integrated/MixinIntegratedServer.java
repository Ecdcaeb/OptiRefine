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
