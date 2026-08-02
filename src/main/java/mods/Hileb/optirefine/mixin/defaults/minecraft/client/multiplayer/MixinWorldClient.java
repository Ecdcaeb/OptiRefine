package mods.Hileb.optirefine.mixin.defaults.minecraft.client.multiplayer;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.CustomGuis;
import net.optifine.DynamicLights;
import net.optifine.override.PlayerControllerOF;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import java.util.Set;
@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends World {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] baseline member mc exists in target class
    @Shadow @Final
    private Minecraft mc;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline
    private int playerChunkX = Integer.MIN_VALUE;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private int playerChunkY = Integer.MIN_VALUE;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private boolean playerUpdate = false;

    // Ignored
// [AUDIT-NOTE] ctor only satisfies mixin-extends-World hierarchy; not applied at runtime
    protected MixinWorldClient(ISaveHandler p_i45749_1_, WorldInfo p_i45749_2_, WorldProvider p_i45749_3_, Profiler p_i45749_4_, boolean p_i45749_5_) {
        super(p_i45749_1_, p_i45749_2_, p_i45749_3_, p_i45749_4_, p_i45749_5_);
    }

// [AUDIT-OK] target ctor (Lnet/minecraft/client/network/NetHandlerPlayClient;Lnet/minecraft/world/WorldSettings;ILnet/minecraft/world/EnumDifficulty;Lnet/minecraft/profiler/Profiler;)V matches baseline
    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectInit(NetHandlerPlayClient netHandler, WorldSettings settings, int dimension, EnumDifficulty difficulty, Profiler profilerIn, CallbackInfo ci){
        if (this.mc.playerController != null && this.mc.playerController.getClass() == PlayerControllerMP.class) {
            this.mc.playerController = new PlayerControllerOF(this.mc, netHandler);
            CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.playerController);
        }
    }

// [AUDIT-OK] target refreshVisibleChunks()V declared in baseline; replicates OF chunk-dirty check
    @WrapMethod(method = "refreshVisibleChunks")
    public void injectRefreshVisibleChunks(Operation<Void> original){
        int cx = MathHelper.floor(this.mc.player.posX / 16.0);
        int cy = MathHelper.floor(this.mc.player.posZ / 16.0);
        if (cx != this.playerChunkX || cy != this.playerChunkY) {
            this.playerChunkX = cx;
            this.playerChunkY = cy;
            original.call();
        }
    }

    @Definition(id = "ambienceTicks", field = "Lnet/minecraft/client/multiplayer/WorldClient;ambienceTicks:I")
    @Expression("this.ambienceTicks == 0")
// [AUDIT-OK] target playMoodSoundAndCheckLight(IILnet/minecraft/world/chunk/Chunk;)V matches baseline; ambienceTicks==0 expression present; folds OF player-null/distance guards
    @ModifyExpressionValue(method = "playMoodSoundAndCheckLight", at = @At("MIXINEXTRAS:EXPRESSION")) // TODO
    public boolean injectPlayMoodSoundAndCheckLight(boolean ambienceTicksIs0, @Local(argsOnly = true) Chunk chunkIn){
        if (ambienceTicksIs0) {
            EntityPlayerSP player = this.mc.player;
            return !((player == null) || (Math.abs(player.chunkCoordX - chunkIn.x) > 1 || Math.abs(player.chunkCoordZ - chunkIn.z) > 1));
        } else return false;
    }

// [AUDIT-OK] OF-added override (inherited from World, not declared in baseline WorldClient), not in baseline; @Public for OF-jar access
    @Public
    @Override
    public int getCombinedLight(@Nonnull BlockPos pos, int lightValue) {
        int combinedLight = super.getCombinedLight(pos, lightValue);
        if (Config.isDynamicLights()) {
            combinedLight = DynamicLights.getCombinedLight(pos, combinedLight);
        }

        return combinedLight;
    }

// [AUDIT-OK] OF-added override (inherited from World), not in baseline; @Public for OF-jar access
    @Public
    @Override
    public boolean setBlockState(@Nonnull BlockPos pos, @Nonnull  IBlockState newState, int flags) {
        this.playerUpdate = this.isPlayerActing();
        boolean res = super.setBlockState(pos, newState, flags);
        this.playerUpdate = false;
        return res;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member (OF private isPlayerActing), not in baseline; @Public informational
    @Public
    private boolean isPlayerActing() {
        if (this.mc.playerController instanceof PlayerControllerOF controlOF) {
            return controlOF.isActing();
        } else {
            return false;
        }
    }

// [AUDIT-OK] OF-added API (OF public isPlayerUpdate), not in baseline; @Public for OF-jar access
    @Public
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }

}
