package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ComparisonChain;
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
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.optifine.ChunkPosComparator;

public class PlayerChunkMap {
   private static final Predicate<EntityPlayerMP> NOT_SPECTATOR = new Predicate<EntityPlayerMP>() {
      public boolean apply(@Nullable EntityPlayerMP p_apply_1_) {
         return p_apply_1_ != null && !p_apply_1_.isSpectator();
      }
   };
   private static final Predicate<EntityPlayerMP> CAN_GENERATE_CHUNKS = new Predicate<EntityPlayerMP>() {
      public boolean apply(@Nullable EntityPlayerMP p_apply_1_) {
         return p_apply_1_ != null && (!p_apply_1_.isSpectator() || p_apply_1_.getServerWorld().getGameRules().getBoolean("spectatorsGenerateChunks"));
      }
   };
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
   private final Map<EntityPlayerMP, Set<ChunkPos>> mapPlayerPendingEntries = new HashMap<>();

   public PlayerChunkMap(WorldServer serverWorld) {
      this.world = serverWorld;
      this.setPlayerViewRadius(serverWorld.getMinecraftServer().getPlayerList().getViewDistance());
   }

   public WorldServer getWorldServer() {
      return this.world;
   }

   public Iterator<Chunk> getChunkIterator() {
      final Iterator<PlayerChunkMapEntry> iterator = this.entries.iterator();
      return new AbstractIterator<Chunk>() {
         protected Chunk computeNext() {
            while (iterator.hasNext()) {
               PlayerChunkMapEntry playerchunkmapentry = iterator.next();
               Chunk chunk = playerchunkmapentry.getChunk();
               if (chunk != null) {
                  if (!chunk.isLightPopulated() && chunk.isTerrainPopulated()) {
                     return chunk;
                  }

                  if (!chunk.wasTicked()) {
                     return chunk;
                  }

                  if (playerchunkmapentry.hasPlayerMatchingInRange(128.0, PlayerChunkMap.NOT_SPECTATOR)) {
                     return chunk;
                  }
               }
            }

            return (Chunk)this.endOfData();
         }
      };
   }

   public void tick() {
      Set<Entry<EntityPlayerMP, Set<ChunkPos>>> pairs = this.mapPlayerPendingEntries.entrySet();
      Iterator it = pairs.iterator();

      while (it.hasNext()) {
         Entry<EntityPlayerMP, Set<ChunkPos>> entry = (Entry<EntityPlayerMP, Set<ChunkPos>>)it.next();
         Set<ChunkPos> setPending = entry.getValue();
         if (!setPending.isEmpty()) {
            EntityPlayerMP player = entry.getKey();
            if (player.getServerWorld() != this.world) {
               it.remove();
            } else {
               int countUpdates = this.playerViewRadius / 3 + 1;
               if (!Config.isLazyChunkLoading()) {
                  countUpdates = this.playerViewRadius * 2 + 1;
               }

               for (ChunkPos chunkPos : this.getNearest(setPending, player, countUpdates)) {
                  PlayerChunkMapEntry pcmr = this.getOrCreateEntry(chunkPos.x, chunkPos.z);
                  if (!pcmr.containsPlayer(player)) {
                     pcmr.addPlayer(player);
                  }

                  setPending.remove(chunkPos);
               }
            }
         }
      }

      long i = this.world.getTotalWorldTime();
      if (i - this.previousTotalWorldTime > 8000L) {
         this.previousTotalWorldTime = i;

         for (int j = 0; j < this.entries.size(); j++) {
            PlayerChunkMapEntry playerchunkmapentry = this.entries.get(j);
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
         Collections.sort(this.entriesWithoutChunks, new Comparator<PlayerChunkMapEntry>() {
            public int compare(PlayerChunkMapEntry p_compare_1_, PlayerChunkMapEntry p_compare_2_) {
               return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
            }
         });
      }

      if (this.sortSendToPlayers && i % 4L == 2L) {
         this.sortSendToPlayers = false;
         Collections.sort(this.pendingSendToPlayers, new Comparator<PlayerChunkMapEntry>() {
            public int compare(PlayerChunkMapEntry p_compare_1_, PlayerChunkMapEntry p_compare_2_) {
               return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
            }
         });
      }

      if (!this.entriesWithoutChunks.isEmpty()) {
         long l = System.nanoTime() + 50000000L;
         int k = 49;
         Iterator<PlayerChunkMapEntry> iterator = this.entriesWithoutChunks.iterator();

         while (iterator.hasNext()) {
            PlayerChunkMapEntry playerchunkmapentry1 = iterator.next();
            if (playerchunkmapentry1.getChunk() == null) {
               boolean flag = playerchunkmapentry1.hasPlayerMatching(CAN_GENERATE_CHUNKS);
               if (playerchunkmapentry1.providePlayerChunk(flag)) {
                  iterator.remove();
                  if (playerchunkmapentry1.sendToPlayers()) {
                     this.pendingSendToPlayers.remove(playerchunkmapentry1);
                  }

                  if (--k < 0 || System.nanoTime() > l) {
                     break;
                  }
               }
            }
         }
      }

      if (!this.pendingSendToPlayers.isEmpty()) {
         int i1 = 81;
         Iterator<PlayerChunkMapEntry> iterator1 = this.pendingSendToPlayers.iterator();

         while (iterator1.hasNext()) {
            PlayerChunkMapEntry playerchunkmapentry3 = iterator1.next();
            if (playerchunkmapentry3.sendToPlayers()) {
               iterator1.remove();
               if (--i1 < 0) {
                  break;
               }
            }
         }
      }

      if (this.players.isEmpty()) {
         WorldProvider worldprovider = this.world.provider;
         if (!worldprovider.canRespawnHere()) {
            this.world.getChunkProvider().queueUnloadAll();
         }
      }
   }

   public boolean contains(int chunkX, int chunkZ) {
      long i = getIndex(chunkX, chunkZ);
      return this.entryMap.get(i) != null;
   }

   @Nullable
   public PlayerChunkMapEntry getEntry(int x, int z) {
      return (PlayerChunkMapEntry)this.entryMap.get(getIndex(x, z));
   }

   private PlayerChunkMapEntry getOrCreateEntry(int chunkX, int chunkZ) {
      long i = getIndex(chunkX, chunkZ);
      PlayerChunkMapEntry playerchunkmapentry = (PlayerChunkMapEntry)this.entryMap.get(i);
      if (playerchunkmapentry == null) {
         playerchunkmapentry = new PlayerChunkMapEntry(this, chunkX, chunkZ);
         this.entryMap.put(i, playerchunkmapentry);
         this.entries.add(playerchunkmapentry);
         if (playerchunkmapentry.getChunk() == null) {
            this.entriesWithoutChunks.add(playerchunkmapentry);
         }

         if (!playerchunkmapentry.sendToPlayers()) {
            this.pendingSendToPlayers.add(playerchunkmapentry);
         }
      }

      return playerchunkmapentry;
   }

   public void markBlockForUpdate(BlockPos pos) {
      int i = pos.getX() >> 4;
      int j = pos.getZ() >> 4;
      PlayerChunkMapEntry playerchunkmapentry = this.getEntry(i, j);
      if (playerchunkmapentry != null) {
         playerchunkmapentry.blockChanged(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
      }
   }

   public void addPlayer(EntityPlayerMP player) {
      int i = (int)player.posX >> 4;
      int j = (int)player.posZ >> 4;
      player.managedPosX = player.posX;
      player.managedPosZ = player.posZ;
      int loadRadius = Math.min(this.playerViewRadius, 8);
      int kMin = i - loadRadius;
      int kMax = i + loadRadius;
      int lMin = j - loadRadius;
      int lMax = j + loadRadius;
      Set<ChunkPos> setPendingEntries = this.getPendingEntriesSafe(player);

      for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; k++) {
         for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; l++) {
            if (k >= kMin && k <= kMax && l >= lMin && l <= lMax) {
               this.getOrCreateEntry(k, l).addPlayer(player);
            } else {
               setPendingEntries.add(new ChunkPos(k, l));
            }
         }
      }

      this.players.add(player);
      this.markSortPending();
   }

   public void removePlayer(EntityPlayerMP player) {
      this.mapPlayerPendingEntries.remove(player);
      int i = (int)player.managedPosX >> 4;
      int j = (int)player.managedPosZ >> 4;

      for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; k++) {
         for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; l++) {
            PlayerChunkMapEntry playerchunkmapentry = this.getEntry(k, l);
            if (playerchunkmapentry != null) {
               playerchunkmapentry.removePlayer(player);
            }
         }
      }

      this.players.remove(player);
      this.markSortPending();
   }

   private boolean overlaps(int x1, int z1, int x2, int z2, int radius) {
      int i = x1 - x2;
      int j = z1 - z2;
      return i >= -radius && i <= radius ? j >= -radius && j <= radius : false;
   }

   public void updateMovingPlayer(EntityPlayerMP player) {
      int i = (int)player.posX >> 4;
      int j = (int)player.posZ >> 4;
      double d0 = player.managedPosX - player.posX;
      double d1 = player.managedPosZ - player.posZ;
      double d2 = d0 * d0 + d1 * d1;
      if (d2 >= 64.0) {
         int k = (int)player.managedPosX >> 4;
         int l = (int)player.managedPosZ >> 4;
         int i1 = this.playerViewRadius;
         int j1 = i - k;
         int k1 = j - l;
         if (j1 != 0 || k1 != 0) {
            Set<ChunkPos> setPendingEntries = this.getPendingEntriesSafe(player);

            for (int l1 = i - i1; l1 <= i + i1; l1++) {
               for (int i2 = j - i1; i2 <= j + i1; i2++) {
                  if (!this.overlaps(l1, i2, k, l, i1)) {
                     if (Config.isLazyChunkLoading()) {
                        setPendingEntries.add(new ChunkPos(l1, i2));
                     } else {
                        this.getOrCreateEntry(l1, i2).addPlayer(player);
                     }
                  }

                  if (!this.overlaps(l1 - j1, i2 - k1, i, j, i1)) {
                     setPendingEntries.remove(new ChunkPos(l1 - j1, i2 - k1));
                     PlayerChunkMapEntry playerchunkmapentry = this.getEntry(l1 - j1, i2 - k1);
                     if (playerchunkmapentry != null) {
                        playerchunkmapentry.removePlayer(player);
                     }
                  }
               }
            }

            player.managedPosX = player.posX;
            player.managedPosZ = player.posZ;
            this.markSortPending();
         }
      }
   }

   public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ) {
      PlayerChunkMapEntry playerchunkmapentry = this.getEntry(chunkX, chunkZ);
      return playerchunkmapentry != null && playerchunkmapentry.containsPlayer(player) && playerchunkmapentry.isSentToPlayers();
   }

   public void setPlayerViewRadius(int radius) {
      radius = MathHelper.clamp(radius, 3, 64);
      if (radius != this.playerViewRadius) {
         int i = radius - this.playerViewRadius;

         for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players)) {
            int j = (int)entityplayermp.posX >> 4;
            int k = (int)entityplayermp.posZ >> 4;
            Set<ChunkPos> setPendingEntries = this.getPendingEntriesSafe(entityplayermp);
            if (i > 0) {
               for (int j1 = j - radius; j1 <= j + radius; j1++) {
                  for (int k1 = k - radius; k1 <= k + radius; k1++) {
                     if (Config.isLazyChunkLoading()) {
                        setPendingEntries.add(new ChunkPos(j1, k1));
                     } else {
                        PlayerChunkMapEntry playerchunkmapentry = this.getOrCreateEntry(j1, k1);
                        if (!playerchunkmapentry.containsPlayer(entityplayermp)) {
                           playerchunkmapentry.addPlayer(entityplayermp);
                        }
                     }
                  }
               }
            } else {
               for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; l++) {
                  for (int i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; i1++) {
                     if (!this.overlaps(l, i1, j, k, radius)) {
                        setPendingEntries.remove(new ChunkPos(l, i1));
                        PlayerChunkMapEntry entry = this.getEntry(l, i1);
                        if (entry != null) {
                           entry.removePlayer(entityplayermp);
                        }
                     }
                  }
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
      return p_187307_0_ + 2147483647L | p_187307_1_ + 2147483647L << 32;
   }

   public void entryChanged(PlayerChunkMapEntry entry) {
      this.dirtyEntries.add(entry);
   }

   public void removeEntry(PlayerChunkMapEntry entry) {
      ChunkPos chunkpos = entry.getPos();
      long i = getIndex(chunkpos.x, chunkpos.z);
      entry.updateChunkInhabitedTime();
      this.entryMap.remove(i);
      this.entries.remove(entry);
      this.dirtyEntries.remove(entry);
      this.pendingSendToPlayers.remove(entry);
      this.entriesWithoutChunks.remove(entry);
      Chunk chunk = entry.getChunk();
      if (chunk != null) {
         this.getWorldServer().getChunkProvider().queueUnload(chunk);
      }
   }

   private PriorityQueue<ChunkPos> getNearest(Set<ChunkPos> setPending, EntityPlayerMP player, int count) {
      float playerYaw = player.rotationYaw + 90.0F;

      while (playerYaw <= -180.0F) {
         playerYaw += 360.0F;
      }

      while (playerYaw > 180.0F) {
         playerYaw -= 360.0F;
      }

      double playerYawRad = playerYaw * (Math.PI / 180.0);
      double playerPitch = player.rotationPitch;
      double playerPitchRad = playerPitch * (Math.PI / 180.0);
      ChunkPosComparator comp = new ChunkPosComparator(player.chunkCoordX, player.chunkCoordZ, playerYawRad, playerPitchRad);
      Comparator<ChunkPos> compRev = Collections.reverseOrder(comp);
      PriorityQueue<ChunkPos> queue = new PriorityQueue<>(compRev);

      for (ChunkPos chunkPos : setPending) {
         if (queue.size() < count) {
            queue.add(chunkPos);
         } else {
            ChunkPos furthest = queue.peek();
            if (comp.compare(chunkPos, furthest) < 0) {
               queue.remove();
               queue.add(chunkPos);
            }
         }
      }

      return queue;
   }

   private Set<ChunkPos> getPendingEntriesSafe(EntityPlayerMP player) {
      Set<ChunkPos> setPendingEntries = this.mapPlayerPendingEntries.get(player);
      if (setPendingEntries != null) {
         return setPendingEntries;
      } else {
         int loadRadius = Math.min(this.playerViewRadius, 8);
         int playerWidth = this.playerViewRadius * 2 + 1;
         int loadWidth = loadRadius * 2 + 1;
         int countLazyChunks = playerWidth * playerWidth - loadWidth * loadWidth;
         countLazyChunks = Math.max(countLazyChunks, 16);
         Set<ChunkPos> var7 = new HashSet(countLazyChunks);
         this.mapPlayerPendingEntries.put(player, var7);
         return var7;
      }
   }
}
