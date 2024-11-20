/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.lang.Runnable
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.server.integrated.IntegratedServer
 */
package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.integrated.IntegratedServer;

/*
 * Exception performing whole class analysis ignored.
 */
class IntegratedServer.3
implements Runnable {
    IntegratedServer.3() {
    }

    public void run() {
        for (EntityPlayerMP entityplayermp : Lists.newArrayList((Iterable)IntegratedServer.this.getPlayerList().getPlayers())) {
            if (entityplayermp.bm().equals((Object)IntegratedServer.access$000((IntegratedServer)IntegratedServer.this).player.bm())) continue;
            IntegratedServer.this.getPlayerList().playerLoggedOut(entityplayermp);
        }
    }
}
