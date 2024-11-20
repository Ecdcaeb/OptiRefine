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

class PlayerChunkMap.5
implements Comparator<PlayerChunkMapEntry> {
    PlayerChunkMap.5() {
    }

    public int compare(PlayerChunkMapEntry p_compare_1_, PlayerChunkMapEntry p_compare_2_) {
        return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
    }
}
