package mods.Hileb.optirefine.mixin.defaults.minecraft.network;

import com.google.common.util.concurrent.ListenableFuture;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.IThreadListener;
import net.optifine.gui.GuiButtonOF;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;
import java.util.List;

@Checked
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

/*
--- net/minecraft/network/PacketThreadUtil.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/network/PacketThreadUtil.java	Tue Aug 19 14:59:58 2025
@@ -1,16 +1,40 @@
 package net.minecraft.network;

+import net.minecraft.network.play.server.SPacketJoinGame;
+import net.minecraft.network.play.server.SPacketPlayerPosLook;
+import net.minecraft.network.play.server.SPacketRespawn;
 import net.minecraft.util.IThreadListener;

 public class PacketThreadUtil {
+   public static int lastDimensionId = Integer.MIN_VALUE;
+
    public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> var0, final T var1, IThreadListener var2) throws ThreadQuickExitException {
       if (!var2.isCallingFromMinecraftThread()) {
          var2.addScheduledTask(new Runnable() {
             public void run() {
+               PacketThreadUtil.clientPreProcessPacket(var0);
                var0.processPacket(var1);
             }
          });
          throw ThreadQuickExitException.INSTANCE;
+      } else {
+         clientPreProcessPacket(var0);
+      }
+   }
+
+   protected static void clientPreProcessPacket(Packet var0) {
+      if (var0 instanceof SPacketPlayerPosLook) {
+         Config.getRenderGlobal().onPlayerPositionSet();
+      }
+
+      if (var0 instanceof SPacketRespawn) {
+         SPacketRespawn var1 = (SPacketRespawn)var0;
+         lastDimensionId = var1.getDimensionID();
+      } else if (var0 instanceof SPacketJoinGame) {
+         SPacketJoinGame var2 = (SPacketJoinGame)var0;
+         lastDimensionId = var2.getDimension();
+      } else {
+         lastDimensionId = Integer.MIN_VALUE;
       }
    }
 }
 */
