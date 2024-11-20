/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Runnable
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.Packet
 *  net.minecraft.network.ThreadQuickExitException
 *  net.minecraft.util.IThreadListener
 */
package net.minecraft.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {
    public static <T extends INetHandler> void checkThreadAndEnqueue(Packet<T> packet, T t, IThreadListener iThreadListener) throws ThreadQuickExitException {
        if (!iThreadListener.isCallingFromMinecraftThread()) {
            iThreadListener.addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */);
            throw ThreadQuickExitException.INSTANCE;
        }
    }
}
