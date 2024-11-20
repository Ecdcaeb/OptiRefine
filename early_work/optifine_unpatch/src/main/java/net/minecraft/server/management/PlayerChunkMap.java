/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 *  javax.annotation.Nullable
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.server.management.PlayerChunkMapEntry
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.ChunkPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.WorldProvider
 *  net.minecraft.world.WorldServer
 *  net.minecraft.world.chunk.Chunk
 */
package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class PlayerChunkMap {
    private static final Predicate<EntityPlayerMP> NOT_SPECTATOR = new /* Unavailable Anonymous Inner Class!! */;
    private static final Predicate<EntityPlayerMP> CAN_GENERATE_CHUNKS = new /* Unavailable Anonymous Inner Class!! */;
    private final WorldServer world;
    private final List<EntityPlayerMP> players = Lists.newArrayList();
    private final Long2ObjectMap<PlayerChunkMapEntry> entryMap = new Long2ObjectOpenHashMap(4096);
    private final Set<PlayerChunkMapEntry> dirtyEntries = Sets.newHashSet();
    private final List<PlayerChunkMapEntry> pendingSendToPlayers = Lists.newLinkedList();
    private final List<PlayerChunkMapEntry> entriesWithoutChunks = Lists.newLinkedList();
    private final List<PlayerChunkMapEntry> entries = Lists.newArrayList();
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private boolean sortMissingChunks = true;
    private boolean sortSendToPlayers = true;

    public PlayerChunkMap(WorldServer worldServer) {
        this.world = worldServer;
        this.setPlayerViewRadius(worldServer.getMinecraftServer().getPlayerList().getViewDistance());
    }

    public WorldServer getWorldServer() {
        return this.world;
    }

    public Iterator<Chunk> getChunkIterator() {
        Iterator iterator = this.entries.iterator();
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public void tick() {
        WorldProvider worldProvider;
        PlayerChunkMapEntry \u260332;
        int n;
        long l = this.world.R();
        if (l - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = l;
            for (n = 0; n < this.entries.size(); ++n) {
                \u260332 = (PlayerChunkMapEntry)this.entries.get(n);
                \u260332.update();
                \u260332.updateChunkInhabitedTime();
            }
        }
        if (!this.dirtyEntries.isEmpty()) {
            for (PlayerChunkMapEntry \u260332 : this.dirtyEntries) {
                \u260332.update();
            }
            this.dirtyEntries.clear();
        }
        if (this.sortMissingChunks && l % 4L == 0L) {
            this.sortMissingChunks = false;
            Collections.sort(this.entriesWithoutChunks, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        }
        if (this.sortSendToPlayers && l % 4L == 2L) {
            this.sortSendToPlayers = false;
            Collections.sort(this.pendingSendToPlayers, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        }
        if (!this.entriesWithoutChunks.isEmpty()) {
            \u2603 = System.nanoTime() + 50000000L;
            int n2 = 49;
            Iterator \u26032 = this.entriesWithoutChunks.iterator();
            while (\u26032.hasNext()) {
                PlayerChunkMapEntry playerChunkMapEntry = (PlayerChunkMapEntry)\u26032.next();
                if (playerChunkMapEntry.getChunk() != null || !playerChunkMapEntry.providePlayerChunk(\u2603 = playerChunkMapEntry.hasPlayerMatching(CAN_GENERATE_CHUNKS))) continue;
                \u26032.remove();
                if (playerChunkMapEntry.sendToPlayers()) {
                    this.pendingSendToPlayers.remove((Object)playerChunkMapEntry);
                }
                if (--n2 >= 0 && System.nanoTime() <= \u2603) continue;
                break;
            }
        }
        if (!this.pendingSendToPlayers.isEmpty()) {
            n = 81;
            \u260332 = this.pendingSendToPlayers.iterator();
            while (\u260332.hasNext()) {
                PlayerChunkMapEntry playerChunkMapEntry = (PlayerChunkMapEntry)\u260332.next();
                if (!playerChunkMapEntry.sendToPlayers()) continue;
                \u260332.remove();
                if (--n >= 0) continue;
                break;
            }
        }
        if (this.players.isEmpty() && !(worldProvider = this.world.s).canRespawnHere()) {
            this.world.getChunkProvider().queueUnloadAll();
        }
    }

    public boolean contains(int n, int n2) {
        long l = PlayerChunkMap.getIndex(n, n2);
        return this.entryMap.get(l) != null;
    }

    @Nullable
    public PlayerChunkMapEntry getEntry(int n, int n2) {
        return (PlayerChunkMapEntry)this.entryMap.get(PlayerChunkMap.getIndex(n, n2));
    }

    private PlayerChunkMapEntry getOrCreateEntry(int n, int n2) {
        long l = PlayerChunkMap.getIndex(n, n2);
        PlayerChunkMapEntry \u26032 = (PlayerChunkMapEntry)this.entryMap.get(l);
        if (\u26032 == null) {
            \u26032 = new PlayerChunkMapEntry(this, n, n2);
            this.entryMap.put(l, (Object)\u26032);
            this.entries.add((Object)\u26032);
            if (\u26032.getChunk() == null) {
                this.entriesWithoutChunks.add((Object)\u26032);
            }
            if (!\u26032.sendToPlayers()) {
                this.pendingSendToPlayers.add((Object)\u26032);
            }
        }
        return \u26032;
    }

    public void markBlockForUpdate(BlockPos blockPos) {
        int n = blockPos.p() >> 4;
        PlayerChunkMapEntry \u26032 = this.getEntry(n, \u2603 = blockPos.r() >> 4);
        if (\u26032 != null) {
            \u26032.blockChanged(blockPos.p() & 0xF, blockPos.q(), blockPos.r() & 0xF);
        }
    }

    public void addPlayer(EntityPlayerMP entityPlayerMP) {
        int n = (int)entityPlayerMP.p >> 4;
        \u2603 = (int)entityPlayerMP.r >> 4;
        entityPlayerMP.managedPosX = entityPlayerMP.p;
        entityPlayerMP.managedPosZ = entityPlayerMP.r;
        for (\u2603 = n - this.playerViewRadius; \u2603 <= n + this.playerViewRadius; ++\u2603) {
            for (\u2603 = \u2603 - this.playerViewRadius; \u2603 <= \u2603 + this.playerViewRadius; ++\u2603) {
                this.getOrCreateEntry(\u2603, \u2603).addPlayer(entityPlayerMP);
            }
        }
        this.players.add((Object)entityPlayerMP);
        this.markSortPending();
    }

    public void removePlayer(EntityPlayerMP entityPlayerMP2) {
        EntityPlayerMP entityPlayerMP2;
        int n = (int)entityPlayerMP2.managedPosX >> 4;
        \u2603 = (int)entityPlayerMP2.managedPosZ >> 4;
        for (\u2603 = n - this.playerViewRadius; \u2603 <= n + this.playerViewRadius; ++\u2603) {
            for (\u2603 = \u2603 - this.playerViewRadius; \u2603 <= \u2603 + this.playerViewRadius; ++\u2603) {
                PlayerChunkMapEntry playerChunkMapEntry = this.getEntry(\u2603, \u2603);
                if (playerChunkMapEntry == null) continue;
                playerChunkMapEntry.removePlayer(entityPlayerMP2);
            }
        }
        this.players.remove((Object)entityPlayerMP2);
        this.markSortPending();
    }

    private boolean overlaps(int n, int n2, int n3, int n4, int n5) {
        \u2603 = n - n3;
        \u2603 = n2 - n4;
        if (\u2603 < -n5 || \u2603 > n5) {
            return false;
        }
        return \u2603 >= -n5 && \u2603 <= n5;
    }

    public void updateMovingPlayer(EntityPlayerMP entityPlayerMP) {
        int n = (int)entityPlayerMP.p >> 4;
        \u2603 = (int)entityPlayerMP.r >> 4;
        double \u26032 = entityPlayerMP.managedPosX - entityPlayerMP.p;
        double \u26033 = entityPlayerMP.managedPosZ - entityPlayerMP.r;
        double \u26034 = \u26032 * \u26032 + \u26033 * \u26033;
        if (\u26034 < 64.0) {
            return;
        }
        \u2603 = (int)entityPlayerMP.managedPosX >> 4;
        \u2603 = (int)entityPlayerMP.managedPosZ >> 4;
        \u2603 = this.playerViewRadius;
        \u2603 = n - \u2603;
        \u2603 = \u2603 - \u2603;
        if (\u2603 == 0 && \u2603 == 0) {
            return;
        }
        for (\u2603 = n - \u2603; \u2603 <= n + \u2603; ++\u2603) {
            for (\u2603 = \u2603 - \u2603; \u2603 <= \u2603 + \u2603; ++\u2603) {
                if (!this.overlaps(\u2603, \u2603, \u2603, \u2603, \u2603)) {
                    this.getOrCreateEntry(\u2603, \u2603).addPlayer(entityPlayerMP);
                }
                if (this.overlaps(\u2603 - \u2603, \u2603 - \u2603, n, \u2603, \u2603) || (\u2603 = this.getEntry(\u2603 - \u2603, \u2603 - \u2603)) == null) continue;
                \u2603.removePlayer(entityPlayerMP);
            }
        }
        entityPlayerMP.managedPosX = entityPlayerMP.p;
        entityPlayerMP.managedPosZ = entityPlayerMP.r;
        this.markSortPending();
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP entityPlayerMP, int n, int n2) {
        PlayerChunkMapEntry playerChunkMapEntry = this.getEntry(n, n2);
        return playerChunkMapEntry != null && playerChunkMapEntry.containsPlayer(entityPlayerMP) && playerChunkMapEntry.isSentToPlayers();
    }

    public void setPlayerViewRadius(int n2) {
        int n2;
        if ((n2 = MathHelper.clamp((int)n2, (int)3, (int)32)) == this.playerViewRadius) {
            return;
        }
        \u2603 = n2 - this.playerViewRadius;
        ArrayList arrayList = Lists.newArrayList(this.players);
        for (EntityPlayerMP entityPlayerMP : arrayList) {
            int n3 = (int)entityPlayerMP.p >> 4;
            \u2603 = (int)entityPlayerMP.r >> 4;
            if (\u2603 > 0) {
                for (i = n3 - n2; i <= n3 + n2; ++i) {
                    for (\u2603 = \u2603 - n2; \u2603 <= \u2603 + n2; ++\u2603) {
                        PlayerChunkMapEntry playerChunkMapEntry = this.getOrCreateEntry(i, \u2603);
                        if (playerChunkMapEntry.containsPlayer(entityPlayerMP)) continue;
                        playerChunkMapEntry.addPlayer(entityPlayerMP);
                    }
                }
                continue;
            }
            for (int i = n3 - this.playerViewRadius; i <= n3 + this.playerViewRadius; ++i) {
                for (\u2603 = \u2603 - this.playerViewRadius; \u2603 <= \u2603 + this.playerViewRadius; ++\u2603) {
                    if (this.overlaps(i, \u2603, n3, \u2603, n2)) continue;
                    this.getOrCreateEntry(i, \u2603).removePlayer(entityPlayerMP);
                }
            }
        }
        this.playerViewRadius = n2;
        this.markSortPending();
    }

    private void markSortPending() {
        this.sortMissingChunks = true;
        this.sortSendToPlayers = true;
    }

    public static int getFurthestViewableBlock(int n) {
        return n * 16 - 16;
    }

    private static long getIndex(int n, int n2) {
        return (long)n + Integer.MAX_VALUE | (long)n2 + Integer.MAX_VALUE << 32;
    }

    public void entryChanged(PlayerChunkMapEntry playerChunkMapEntry) {
        this.dirtyEntries.add((Object)playerChunkMapEntry);
    }

    public void removeEntry(PlayerChunkMapEntry playerChunkMapEntry) {
        ChunkPos chunkPos = playerChunkMapEntry.getPos();
        long \u26032 = PlayerChunkMap.getIndex(chunkPos.x, chunkPos.z);
        playerChunkMapEntry.updateChunkInhabitedTime();
        this.entryMap.remove(\u26032);
        this.entries.remove((Object)playerChunkMapEntry);
        this.dirtyEntries.remove((Object)playerChunkMapEntry);
        this.pendingSendToPlayers.remove((Object)playerChunkMapEntry);
        this.entriesWithoutChunks.remove((Object)playerChunkMapEntry);
        Chunk \u26033 = playerChunkMapEntry.getChunk();
        if (\u26033 != null) {
            this.getWorldServer().getChunkProvider().queueUnload(\u26033);
        }
    }

    static /* synthetic */ Predicate access$000() {
        return NOT_SPECTATOR;
    }
}
