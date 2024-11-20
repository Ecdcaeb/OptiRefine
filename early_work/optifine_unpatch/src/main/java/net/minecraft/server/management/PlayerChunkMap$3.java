/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.AbstractIterator
 *  java.lang.Object
 *  java.util.Iterator
 *  net.minecraft.server.management.PlayerChunkMap
 *  net.minecraft.server.management.PlayerChunkMapEntry
 *  net.minecraft.world.chunk.Chunk
 */
package net.minecraft.server.management;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.world.chunk.Chunk;

/*
 * Exception performing whole class analysis ignored.
 */
class PlayerChunkMap.3
extends AbstractIterator<Chunk> {
    final /* synthetic */ Iterator field_187294_a;

    PlayerChunkMap.3(Iterator iterator) {
        this.field_187294_a = iterator;
    }

    protected Chunk computeNext() {
        while (this.field_187294_a.hasNext()) {
            PlayerChunkMapEntry playerChunkMapEntry = (PlayerChunkMapEntry)this.field_187294_a.next();
            Chunk \u26032 = playerChunkMapEntry.getChunk();
            if (\u26032 == null) continue;
            if (!\u26032.isLightPopulated() && \u26032.isTerrainPopulated()) {
                return \u26032;
            }
            if (!\u26032.wasTicked()) {
                return \u26032;
            }
            if (!playerChunkMapEntry.hasPlayerMatchingInRange(128.0, PlayerChunkMap.access$000())) continue;
            return \u26032;
        }
        return (Chunk)this.endOfData();
    }

    protected /* synthetic */ Object computeNext() {
        return this.computeNext();
    }
}
