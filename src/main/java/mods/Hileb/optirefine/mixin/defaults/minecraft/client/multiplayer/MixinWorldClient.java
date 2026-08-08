package mods.Hileb.optirefine.mixin.defaults.minecraft.client.multiplayer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.util.SoundCategory;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import java.util.Set;
@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends World {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] baseline member mc exists in target class
    @Shadow @Final
    private Minecraft mc;

    @Shadow
// [AUDIT-OK] baseline member ambienceTicks exists in target class (not in World, which the mixin extends)
    private int ambienceTicks;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private int playerChunkX;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private int playerChunkY;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private boolean playerUpdate;

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

    @WrapMethod(method = "playMoodSoundAndCheckLight")
// [AUDIT-FIXED] full OF port: player-null/1-chunk guards folded into OF's structure, plus the cave-sound
// height bias (OF WorldClient:283-289: var8 /= 2; +128 above y=160, +64 above y=96) applied to both the
// sampled BlockPos and the distance check. Vanilla has no height bias.
    public void optiRefine$playMoodSoundAndCheckLight(int x, int z, Chunk chunkIn, Operation<Void> original) {
        super.playMoodSoundAndCheckLight(x, z, chunkIn);

        if (this.ambienceTicks == 0) {
            EntityPlayerSP player = this.mc.player;
            if (player == null) {
                return;
            }

            if (Math.abs(player.chunkCoordX - chunkIn.x) > 1 || Math.abs(player.chunkCoordZ - chunkIn.z) > 1) {
                return;
            }

            this.updateLCG = this.updateLCG * 3 + 1013904223;
            int i = this.updateLCG >> 2;
            int j = i & 15;
            int k = i >> 8 & 15;
            int l = i >> 16 & 255;
            l /= 2;
            if (player.posY > 160.0) {
                l += 128;
            } else if (player.posY > 96.0) {
                l += 64;
            }

            BlockPos blockpos = new BlockPos(j + x, l, k + z);
            IBlockState iblockstate = chunkIn.getBlockState(blockpos);
            j = j + x;
            k = k + z;
            double d0 = player.getDistanceSq((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D);

            if (d0 < 4.0D) {
                return;
            }

            if (d0 > 255.0D) {
                return;
            }

            if (iblockstate.getMaterial() == Material.AIR && this.getLight(blockpos) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, blockpos) <= 0) {
                this.playSound((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.7F, 0.8F + this.rand.nextFloat() * 0.2F, false);
                this.ambienceTicks = this.rand.nextInt(12000) + 6000;
            }
        }
    }

    @Redirect(method = "doVoidFogParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;getHeldItemMainhand()Lnet/minecraft/item/ItemStack;"))
// [AUDIT-FIXED] OF WorldClient:322-325 falls back to the offhand stack when the mainhand is not a
// BARRIER item; vanilla only checks the mainhand. (mc.player is typed EntityPlayerSP -> invoke owner)
    public ItemStack optiRefine$getHeldItemForBarrier(EntityPlayerSP player) {
        ItemStack mainhand = player.getHeldItemMainhand();
        if (mainhand == null || Block.getBlockFromItem(mainhand.getItem()) != Blocks.BARRIER) {
            return player.getHeldItemOffhand();
        }

        return mainhand;
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


    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.playerChunkX = Integer.MIN_VALUE;
        this.playerChunkY = Integer.MIN_VALUE;
        this.playerUpdate = false;
    }
}
