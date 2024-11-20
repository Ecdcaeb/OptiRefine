/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Runnable
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.Packet
 */
package net.minecraft.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

static final class PacketThreadUtil.1
implements Runnable {
    final /* synthetic */ Packet field_180030_a;
    final /* synthetic */ INetHandler field_180029_b;

    PacketThreadUtil.1(Packet packet, INetHandler iNetHandler) {
        this.field_180030_a = packet;
        this.field_180029_b = iNetHandler;
    }

    public void run() {
        this.field_180030_a.processPacket(this.field_180029_b);
    }
}
