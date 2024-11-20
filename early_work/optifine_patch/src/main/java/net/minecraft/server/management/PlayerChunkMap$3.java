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
    final /* synthetic */ Iterator val$iterator;

    PlayerChunkMap.3(Iterator iterator) {
        this.val$iterator = iterator;
    }

    protected Chunk computeNext() {
        while (this.val$iterator.hasNext()) {
            PlayerChunkMapEntry playerchunkmapentry = (PlayerChunkMapEntry)this.val$iterator.next();
            Chunk chunk = playerchunkmapentry.getChunk();
            if (chunk == null) continue;
            if (!chunk.isLightPopulated() && chunk.isTerrainPopulated()) {
                return chunk;
            }
            if (!chunk.wasTicked()) {
                return chunk;
            }
            if (!playerchunkmapentry.hasPlayerMatchingInRange(128.0, PlayerChunkMap.access$000())) continue;
            return chunk;
        }
        return (Chunk)this.endOfData();
    }
}
