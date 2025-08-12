package mods.Hileb.optirefine.mixin.defaults.minecraft.network;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.IThreadListener;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketThreadUtil.class)
public abstract class MixinPacketThreadUtil {
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    @Unique
    private static int lastDimensionId = Integer.MIN_VALUE;

    @Inject(method = "checkThreadAndEnqueue", at = @At("TAIL"))
    @SuppressWarnings("rawtypes")
    private static void injectCheckThreadAndEnqueue(Packet p_180031_0_, INetHandler p_180031_1_, IThreadListener p_180031_2_, CallbackInfo ci) {
        clientPreProcessPacket(p_180031_0_);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    private static void clientPreProcessPacket(Packet<?> packetIn) {
        if (packetIn instanceof SPacketPlayerPosLook) {
            _acc_RenderGlobal_onPlayerPositionSet(Config.getRenderGlobal());
        }
        if (packetIn instanceof SPacketRespawn respawn) {
            lastDimensionId = respawn.getDimensionID();
        } else if (packetIn instanceof SPacketJoinGame joinGame) {
            lastDimensionId = joinGame.getDimension();
        } else {
            lastDimensionId = Integer.MIN_VALUE;
        }
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal onPlayerPositionSet ()V")
    private static void _acc_RenderGlobal_onPlayerPositionSet(RenderGlobal renderGlobal){}
}
