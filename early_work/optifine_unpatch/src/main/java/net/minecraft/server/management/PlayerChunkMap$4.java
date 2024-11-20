/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 *  java.lang.Object
 *  java.util.Comparator
 *  net.minecraft.server.management.PlayerChunkMapEntry
 */
package net.minecraft.server.management;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.server.management.PlayerChunkMapEntry;

class PlayerChunkMap.4
implements Comparator<PlayerChunkMapEntry> {
    PlayerChunkMap.4() {
    }

    public int compare(PlayerChunkMapEntry playerChunkMapEntry, PlayerChunkMapEntry playerChunkMapEntry2) {
        return ComparisonChain.start().compare(playerChunkMapEntry.getClosestPlayerDistance(), playerChunkMapEntry2.getClosestPlayerDistance()).result();
    }

    public /* synthetic */ int compare(Object object, Object object2) {
        return this.compare((PlayerChunkMapEntry)object, (PlayerChunkMapEntry)object2);
    }
}
