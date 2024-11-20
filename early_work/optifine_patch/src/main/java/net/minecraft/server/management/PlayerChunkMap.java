/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.System
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.PriorityQueue
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
 *  net.optifine.ChunkPosComparator
 */
package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
import net.optifine.ChunkPosComparator;

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
    private final Map<EntityPlayerMP, Set<ChunkPos>> mapPlayerPendingEntries = new HashMap();

    public PlayerChunkMap(WorldServer serverWorld) {
        this.world = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getPlayerList().getViewDistance());
    }

    public WorldServer getWorldServer() {
        return this.world;
    }

    public Iterator<Chunk> getChunkIterator() {
        Iterator iterator = this.entries.iterator();
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public void tick() {
        WorldProvider worldprovider;
        Set pairs = this.mapPlayerPendingEntries.entrySet();
        Iterator it = pairs.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            Set setPending = (Set)entry.getValue();
            if (setPending.isEmpty()) continue;
            EntityPlayerMP player = (EntityPlayerMP)entry.getKey();
            if (player.getServerWorld() != this.world) {
                it.remove();
                continue;
            }
            int countUpdates = this.playerViewRadius / 3 + 1;
            if (!Config.isLazyChunkLoading()) {
                countUpdates = this.playerViewRadius * 2 + 1;
            }
            PriorityQueue<ChunkPos> queueNearest = this.getNearest((Set<ChunkPos>)setPending, player, countUpdates);
            for (ChunkPos chunkPos : queueNearest) {
                PlayerChunkMapEntry pcmr = this.getOrCreateEntry(chunkPos.x, chunkPos.z);
                if (!pcmr.containsPlayer(player)) {
                    pcmr.addPlayer(player);
                }
                setPending.remove((Object)chunkPos);
            }
        }
        long i = this.world.R();
        if (i - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = i;
            for (int j = 0; j < this.entries.size(); ++j) {
                PlayerChunkMapEntry playerchunkmapentry = (PlayerChunkMapEntry)this.entries.get(j);
                playerchunkmapentry.update();
                playerchunkmapentry.updateChunkInhabitedTime();
            }
        }
        if (!this.dirtyEntries.isEmpty()) {
            for (PlayerChunkMapEntry playerchunkmapentry2 : this.dirtyEntries) {
                playerchunkmapentry2.update();
            }
            this.dirtyEntries.clear();
        }
        if (this.sortMissingChunks && i % 4L == 0L) {
            this.sortMissingChunks = false;
            Collections.sort(this.entriesWithoutChunks, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        }
        if (this.sortSendToPlayers && i % 4L == 2L) {
            this.sortSendToPlayers = false;
            Collections.sort(this.pendingSendToPlayers, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        }
        if (!this.entriesWithoutChunks.isEmpty()) {
            long l = System.nanoTime() + 50000000L;
            int k = 49;
            Iterator iterator = this.entriesWithoutChunks.iterator();
            while (iterator.hasNext()) {
                boolean flag;
                PlayerChunkMapEntry playerchunkmapentry1 = (PlayerChunkMapEntry)iterator.next();
                if (playerchunkmapentry1.getChunk() != null || !playerchunkmapentry1.providePlayerChunk(flag = playerchunkmapentry1.hasPlayerMatching(CAN_GENERATE_CHUNKS))) continue;
                iterator.remove();
                if (playerchunkmapentry1.sendToPlayers()) {
                    this.pendingSendToPlayers.remove((Object)playerchunkmapentry1);
                }
                if (--k >= 0 && System.nanoTime() <= l) continue;
                break;
            }
        }
        if (!this.pendingSendToPlayers.isEmpty()) {
            int i1 = 81;
            Iterator iterator1 = this.pendingSendToPlayers.iterator();
            while (iterator1.hasNext()) {
                PlayerChunkMapEntry playerchunkmapentry3 = (PlayerChunkMapEntry)iterator1.next();
                if (!playerchunkmapentry3.sendToPlayers()) continue;
                iterator1.remove();
                if (--i1 >= 0) continue;
                break;
            }
        }
        if (this.players.isEmpty() && !(worldprovider = this.world.s).canRespawnHere()) {
            this.world.getChunkProvider().queueUnloadAll();
        }
    }

    public boolean contains(int chunkX, int chunkZ) {
        long i = PlayerChunkMap.getIndex(chunkX, chunkZ);
        return this.entryMap.get(i) != null;
    }

    @Nullable
    public PlayerChunkMapEntry getEntry(int x, int z) {
        return (PlayerChunkMapEntry)this.entryMap.get(PlayerChunkMap.getIndex(x, z));
    }

    private PlayerChunkMapEntry getOrCreateEntry(int chunkX, int chunkZ) {
        long i = PlayerChunkMap.getIndex(chunkX, chunkZ);
        PlayerChunkMapEntry playerchunkmapentry = (PlayerChunkMapEntry)this.entryMap.get(i);
        if (playerchunkmapentry == null) {
            playerchunkmapentry = new PlayerChunkMapEntry(this, chunkX, chunkZ);
            this.entryMap.put(i, (Object)playerchunkmapentry);
            this.entries.add((Object)playerchunkmapentry);
            if (playerchunkmapentry.getChunk() == null) {
                this.entriesWithoutChunks.add((Object)playerchunkmapentry);
            }
            if (!playerchunkmapentry.sendToPlayers()) {
                this.pendingSendToPlayers.add((Object)playerchunkmapentry);
            }
        }
        return playerchunkmapentry;
    }

    public void markBlockForUpdate(BlockPos pos) {
        int j;
        int i = pos.p() >> 4;
        PlayerChunkMapEntry playerchunkmapentry = this.getEntry(i, j = pos.r() >> 4);
        if (playerchunkmapentry != null) {
            playerchunkmapentry.blockChanged(pos.p() & 0xF, pos.q(), pos.r() & 0xF);
        }
    }

    public void addPlayer(EntityPlayerMP player) {
        int i = (int)player.p >> 4;
        int j = (int)player.r >> 4;
        player.managedPosX = player.p;
        player.managedPosZ = player.r;
        int loadRadius = Math.min((int)this.playerViewRadius, (int)8);
        int kMin = i - loadRadius;
        int kMax = i + loadRadius;
        int lMin = j - loadRadius;
        int lMax = j + loadRadius;
        Set<ChunkPos> setPendingEntries = this.getPendingEntriesSafe(player);
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                if (k < kMin || k > kMax || l < lMin || l > lMax) {
                    setPendingEntries.add((Object)new ChunkPos(k, l));
                    continue;
                }
                this.getOrCreateEntry(k, l).addPlayer(player);
            }
        }
        this.players.add((Object)player);
        this.markSortPending();
    }

    public void removePlayer(EntityPlayerMP player) {
        this.mapPlayerPendingEntries.remove((Object)player);
        int i = (int)player.managedPosX >> 4;
        int j = (int)player.managedPosZ >> 4;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                PlayerChunkMapEntry playerchunkmapentry = this.getEntry(k, l);
                if (playerchunkmapentry == null) continue;
                playerchunkmapentry.removePlayer(player);
            }
        }
        this.players.remove((Object)player);
        this.markSortPending();
    }

    private boolean overlaps(int x1, int z1, int x2, int z2, int radius) {
        int i = x1 - x2;
        int j = z1 - z2;
        if (i >= -radius && i <= radius) {
            return j >= -radius && j <= radius;
        }
        return false;
    }

    public void updateMovingPlayer(EntityPlayerMP player) {
        int i = (int)player.p >> 4;
        int j = (int)player.r >> 4;
        double d0 = player.managedPosX - player.p;
        double d1 = player.managedPosZ - player.r;
        double d2 = d0 * d0 + d1 * d1;
        if (d2 >= 64.0) {
            int k = (int)player.managedPosX >> 4;
            int l = (int)player.managedPosZ >> 4;
            int i1 = this.playerViewRadius;
            int j1 = i - k;
            int k1 = j - l;
            if (j1 != 0 || k1 != 0) {
                Set<ChunkPos> setPendingEntries = this.getPendingEntriesSafe(player);
                for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2) {
                        if (!this.overlaps(l1, i2, k, l, i1)) {
                            if (Config.isLazyChunkLoading()) {
                                setPendingEntries.add((Object)new ChunkPos(l1, i2));
                            } else {
                                this.getOrCreateEntry(l1, i2).addPlayer(player);
                            }
                        }
                        if (this.overlaps(l1 - j1, i2 - k1, i, j, i1)) continue;
                        setPendingEntries.remove((Object)new ChunkPos(l1 - j1, i2 - k1));
                        PlayerChunkMapEntry playerchunkmapentry = this.getEntry(l1 - j1, i2 - k1);
                        if (playerchunkmapentry == null) continue;
                        playerchunkmapentry.removePlayer(player);
                    }
                }
                player.managedPosX = player.p;
                player.managedPosZ = player.r;
                this.markSortPending();
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ) {
        PlayerChunkMapEntry playerchunkmapentry = this.getEntry(chunkX, chunkZ);
        return playerchunkmapentry != null && playerchunkmapentry.containsPlayer(player) && playerchunkmapentry.isSentToPlayers();
    }

    public void setPlayerViewRadius(int radius) {
        if ((radius = MathHelper.clamp((int)radius, (int)3, (int)64)) != this.playerViewRadius) {
            int i = radius - this.playerViewRadius;
            for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players)) {
                int j = (int)entityplayermp.p >> 4;
                int k = (int)entityplayermp.r >> 4;
                Set<ChunkPos> setPendingEntries = this.getPendingEntriesSafe(entityplayermp);
                if (i > 0) {
                    for (int j1 = j - radius; j1 <= j + radius; ++j1) {
                        for (int k1 = k - radius; k1 <= k + radius; ++k1) {
                            if (Config.isLazyChunkLoading()) {
                                setPendingEntries.add((Object)new ChunkPos(j1, k1));
                                continue;
                            }
                            PlayerChunkMapEntry playerchunkmapentry = this.getOrCreateEntry(j1, k1);
                            if (playerchunkmapentry.containsPlayer(entityplayermp)) continue;
                            playerchunkmapentry.addPlayer(entityplayermp);
                        }
                    }
                    continue;
                }
                for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                    for (int i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1) {
                        if (this.overlaps(l, i1, j, k, radius)) continue;
                        setPendingEntries.remove((Object)new ChunkPos(l, i1));
                        PlayerChunkMapEntry entry = this.getEntry(l, i1);
                        if (entry == null) continue;
                        entry.removePlayer(entityplayermp);
                    }
                }
            }
            this.playerViewRadius = radius;
            this.markSortPending();
        }
    }

    private void markSortPending() {
        this.sortMissingChunks = true;
        this.sortSendToPlayers = true;
    }

    public static int getFurthestViewableBlock(int distance) {
        return distance * 16 - 16;
    }

    private static long getIndex(int p_187307_0_, int p_187307_1_) {
        return (long)p_187307_0_ + Integer.MAX_VALUE | (long)p_187307_1_ + Integer.MAX_VALUE << 32;
    }

    public void entryChanged(PlayerChunkMapEntry entry) {
        this.dirtyEntries.add((Object)entry);
    }

    public void removeEntry(PlayerChunkMapEntry entry) {
        ChunkPos chunkpos = entry.getPos();
        long i = PlayerChunkMap.getIndex(chunkpos.x, chunkpos.z);
        entry.updateChunkInhabitedTime();
        this.entryMap.remove(i);
        this.entries.remove((Object)entry);
        this.dirtyEntries.remove((Object)entry);
        this.pendingSendToPlayers.remove((Object)entry);
        this.entriesWithoutChunks.remove((Object)entry);
        Chunk chunk = entry.getChunk();
        if (chunk != null) {
            this.getWorldServer().getChunkProvider().queueUnload(chunk);
        }
    }

    private PriorityQueue<ChunkPos> getNearest(Set<ChunkPos> setPending, EntityPlayerMP player, int count) {
        float playerYaw;
        for (playerYaw = player.v + 90.0f; playerYaw <= -180.0f; playerYaw += 360.0f) {
        }
        while (playerYaw > 180.0f) {
            playerYaw -= 360.0f;
        }
        double playerYawRad = (double)playerYaw * (Math.PI / 180);
        double playerPitch = player.w;
        double playerPitchRad = playerPitch * (Math.PI / 180);
        ChunkPosComparator comp = new ChunkPosComparator(player.ab, player.ad, playerYawRad, playerPitchRad);
        Comparator compRev = Collections.reverseOrder((Comparator)comp);
        PriorityQueue queue = new PriorityQueue(compRev);
        for (ChunkPos chunkPos : setPending) {
            if (queue.size() < count) {
                queue.add((Object)chunkPos);
                continue;
            }
            ChunkPos furthest = (ChunkPos)queue.peek();
            if (comp.compare(chunkPos, furthest) >= 0) continue;
            queue.remove();
            queue.add((Object)chunkPos);
        }
        return queue;
    }

    private Set<ChunkPos> getPendingEntriesSafe(EntityPlayerMP player) {
        Set setPendingEntries = (Set)this.mapPlayerPendingEntries.get((Object)player);
        if (setPendingEntries != null) {
            return setPendingEntries;
        }
        int loadRadius = Math.min((int)this.playerViewRadius, (int)8);
        int playerWidth = this.playerViewRadius * 2 + 1;
        int loadWidth = loadRadius * 2 + 1;
        int countLazyChunks = playerWidth * playerWidth - loadWidth * loadWidth;
        countLazyChunks = Math.max((int)countLazyChunks, (int)16);
        setPendingEntries = new HashSet(countLazyChunks);
        this.mapPlayerPendingEntries.put((Object)player, (Object)setPendingEntries);
        return setPendingEntries;
    }

    static /* synthetic */ Predicate access$000() {
        return NOT_SPECTATOR;
    }
}
