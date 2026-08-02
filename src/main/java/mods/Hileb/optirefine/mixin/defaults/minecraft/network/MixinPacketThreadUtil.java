package mods.Hileb.optirefine.mixin.defaults.minecraft.network;

import com.google.common.util.concurrent.ListenableFuture;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.RenderGlobal;
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
@Mixin(PacketThreadUtil.class)
public abstract class MixinPacketThreadUtil {
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    @Unique
    private static int lastDimensionId = Integer.MIN_VALUE;

    @WrapOperation(method = "checkThreadAndEnqueue", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/IThreadListener;isCallingFromMinecraftThread()Z"))
    private static boolean injectCheckThreadAndEnqueue(IThreadListener instance, Operation<Boolean> original, @Local(argsOnly = true) Packet<?> packet) {
        boolean value = original.call(instance);
        if (value) {
            clientPreProcessPacket(packet);
            return true;
        } else return false;
    }

    @WrapOperation(method = "checkThreadAndEnqueue", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/IThreadListener;addScheduledTask(Ljava/lang/Runnable;)Lcom/google/common/util/concurrent/ListenableFuture;"))
    private static ListenableFuture<Object> injectCheckThreadAndEnqueue(IThreadListener instance, Runnable runnable, Operation<ListenableFuture<Object>> original, @Local(argsOnly = true) Packet<?> packet) {
        return original.call(instance, (Runnable) () -> {
            clientPreProcessPacket(packet);
            runnable.run();
        });
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
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
