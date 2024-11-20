/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Runnable
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.Packet
 *  net.minecraft.network.ThreadQuickExitException
 *  net.minecraft.network.play.server.SPacketJoinGame
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketRespawn
 *  net.minecraft.util.IThreadListener
 */
package net.minecraft.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {
    public static int lastDimensionId = Integer.MIN_VALUE;

    public static <T extends INetHandler> void checkThreadAndEnqueue(Packet<T> packetIn, T processor, IThreadListener scheduler) throws ThreadQuickExitException {
        if (!scheduler.isCallingFromMinecraftThread()) {
            scheduler.addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */);
            throw ThreadQuickExitException.INSTANCE;
        }
        PacketThreadUtil.clientPreProcessPacket(packetIn);
    }

    protected static void clientPreProcessPacket(Packet packetIn) {
        if (packetIn instanceof SPacketPlayerPosLook) {
            Config.getRenderGlobal().onPlayerPositionSet();
        }
        if (packetIn instanceof SPacketRespawn) {
            SPacketRespawn respawn = (SPacketRespawn)packetIn;
            lastDimensionId = respawn.getDimensionID();
        } else if (packetIn instanceof SPacketJoinGame) {
            SPacketJoinGame joinGame = (SPacketJoinGame)packetIn;
            lastDimensionId = joinGame.getDimension();
        } else {
            lastDimensionId = Integer.MIN_VALUE;
        }
    }
}
