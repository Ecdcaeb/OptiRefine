/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Runnable
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketThreadUtil
 */
package net.minecraft.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;

/*
 * Exception performing whole class analysis ignored.
 */
static final class PacketThreadUtil.1
implements Runnable {
    final /* synthetic */ Packet val$packetIn;
    final /* synthetic */ INetHandler val$processor;

    PacketThreadUtil.1(Packet packet, INetHandler iNetHandler) {
        this.val$packetIn = packet;
        this.val$processor = iNetHandler;
    }

    public void run() {
        PacketThreadUtil.clientPreProcessPacket((Packet)this.val$packetIn);
        this.val$packetIn.processPacket(this.val$processor);
    }
}
