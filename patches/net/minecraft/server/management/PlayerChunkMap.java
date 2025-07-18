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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class PlayerChunkMap {
   private static final Predicate<EntityPlayerMP> NOT_SPECTATOR = new Predicate<EntityPlayerMP>() {
      public boolean apply(@Nullable EntityPlayerMP var1) {
         return ☃ != null && !☃.isSpectator();
      }
   };
   private static final Predicate<EntityPlayerMP> CAN_GENERATE_CHUNKS = new Predicate<EntityPlayerMP>() {
      public boolean apply(@Nullable EntityPlayerMP var1) {
         return ☃ != null && (!☃.isSpectator() || ☃.getServerWorld().getGameRules().getBoolean("spectatorsGenerateChunks"));
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

   public PlayerChunkMap(WorldServer var1) {
      this.world = ☃;
      this.setPlayerViewRadius(☃.getMinecraftServer().getPlayerList().getViewDistance());
   }

   public WorldServer getWorldServer() {
      return this.world;
   }

   public Iterator<Chunk> getChunkIterator() {
      final Iterator<PlayerChunkMapEntry> ☃ = this.entries.iterator();
      return new AbstractIterator<Chunk>() {
         protected Chunk computeNext() {
            while (☃.hasNext()) {
               PlayerChunkMapEntry ☃x = ☃.next();
               Chunk ☃x = ☃x.getChunk();
               if (☃x != null) {
                  if (!☃x.isLightPopulated() && ☃x.isTerrainPopulated()) {
                     return ☃x;
                  }

                  if (!☃x.wasTicked()) {
                     return ☃x;
                  }

                  if (☃x.hasPlayerMatchingInRange(128.0, PlayerChunkMap.NOT_SPECTATOR)) {
                     return ☃x;
                  }
               }
            }

            return (Chunk)this.endOfData();
         }
      };
   }

   public void tick() {
      long ☃ = this.world.getTotalWorldTime();
      if (☃ - this.previousTotalWorldTime > 8000L) {
         this.previousTotalWorldTime = ☃;

         for (int ☃x = 0; ☃x < this.entries.size(); ☃x++) {
            PlayerChunkMapEntry ☃xx = this.entries.get(☃x);
            ☃xx.update();
            ☃xx.updateChunkInhabitedTime();
         }
      }

      if (!this.dirtyEntries.isEmpty()) {
         for (PlayerChunkMapEntry ☃x : this.dirtyEntries) {
            ☃x.update();
         }

         this.dirtyEntries.clear();
      }

      if (this.sortMissingChunks && ☃ % 4L == 0L) {
         this.sortMissingChunks = false;
         Collections.sort(this.entriesWithoutChunks, new Comparator<PlayerChunkMapEntry>() {
            public int compare(PlayerChunkMapEntry var1, PlayerChunkMapEntry var2) {
               return ComparisonChain.start().compare(☃.getClosestPlayerDistance(), ☃.getClosestPlayerDistance()).result();
            }
         });
      }

      if (this.sortSendToPlayers && ☃ % 4L == 2L) {
         this.sortSendToPlayers = false;
         Collections.sort(this.pendingSendToPlayers, new Comparator<PlayerChunkMapEntry>() {
            public int compare(PlayerChunkMapEntry var1, PlayerChunkMapEntry var2) {
               return ComparisonChain.start().compare(☃.getClosestPlayerDistance(), ☃.getClosestPlayerDistance()).result();
            }
         });
      }

      if (!this.entriesWithoutChunks.isEmpty()) {
         long ☃x = System.nanoTime() + 50000000L;
         int ☃xx = 49;
         Iterator<PlayerChunkMapEntry> ☃xxx = this.entriesWithoutChunks.iterator();

         while (☃xxx.hasNext()) {
            PlayerChunkMapEntry ☃xxxx = ☃xxx.next();
            if (☃xxxx.getChunk() == null) {
               boolean ☃xxxxx = ☃xxxx.hasPlayerMatching(CAN_GENERATE_CHUNKS);
               if (☃xxxx.providePlayerChunk(☃xxxxx)) {
                  ☃xxx.remove();
                  if (☃xxxx.sendToPlayers()) {
                     this.pendingSendToPlayers.remove(☃xxxx);
                  }

                  if (--☃xx < 0 || System.nanoTime() > ☃x) {
                     break;
                  }
               }
            }
         }
      }

      if (!this.pendingSendToPlayers.isEmpty()) {
         int ☃x = 81;
         Iterator<PlayerChunkMapEntry> ☃xx = this.pendingSendToPlayers.iterator();

         while (☃xx.hasNext()) {
            PlayerChunkMapEntry ☃xxx = ☃xx.next();
            if (☃xxx.sendToPlayers()) {
               ☃xx.remove();
               if (--☃x < 0) {
                  break;
               }
            }
         }
      }

      if (this.players.isEmpty()) {
         WorldProvider ☃x = this.world.provider;
         if (!☃x.canRespawnHere()) {
            this.world.getChunkProvider().queueUnloadAll();
         }
      }
   }

   public boolean contains(int var1, int var2) {
      long ☃ = getIndex(☃, ☃);
      return this.entryMap.get(☃) != null;
   }

   @Nullable
   public PlayerChunkMapEntry getEntry(int var1, int var2) {
      return (PlayerChunkMapEntry)this.entryMap.get(getIndex(☃, ☃));
   }

   private PlayerChunkMapEntry getOrCreateEntry(int var1, int var2) {
      long ☃ = getIndex(☃, ☃);
      PlayerChunkMapEntry ☃x = (PlayerChunkMapEntry)this.entryMap.get(☃);
      if (☃x == null) {
         ☃x = new PlayerChunkMapEntry(this, ☃, ☃);
         this.entryMap.put(☃, ☃x);
         this.entries.add(☃x);
         if (☃x.getChunk() == null) {
            this.entriesWithoutChunks.add(☃x);
         }

         if (!☃x.sendToPlayers()) {
            this.pendingSendToPlayers.add(☃x);
         }
      }

      return ☃x;
   }

   public void markBlockForUpdate(BlockPos var1) {
      int ☃ = ☃.getX() >> 4;
      int ☃x = ☃.getZ() >> 4;
      PlayerChunkMapEntry ☃xx = this.getEntry(☃, ☃x);
      if (☃xx != null) {
         ☃xx.blockChanged(☃.getX() & 15, ☃.getY(), ☃.getZ() & 15);
      }
   }

   public void addPlayer(EntityPlayerMP var1) {
      int ☃ = (int)☃.posX >> 4;
      int ☃x = (int)☃.posZ >> 4;
      ☃.managedPosX = ☃.posX;
      ☃.managedPosZ = ☃.posZ;

      for (int ☃xx = ☃ - this.playerViewRadius; ☃xx <= ☃ + this.playerViewRadius; ☃xx++) {
         for (int ☃xxx = ☃x - this.playerViewRadius; ☃xxx <= ☃x + this.playerViewRadius; ☃xxx++) {
            this.getOrCreateEntry(☃xx, ☃xxx).addPlayer(☃);
         }
      }

      this.players.add(☃);
      this.markSortPending();
   }

   public void removePlayer(EntityPlayerMP var1) {
      int ☃ = (int)☃.managedPosX >> 4;
      int ☃x = (int)☃.managedPosZ >> 4;

      for (int ☃xx = ☃ - this.playerViewRadius; ☃xx <= ☃ + this.playerViewRadius; ☃xx++) {
         for (int ☃xxx = ☃x - this.playerViewRadius; ☃xxx <= ☃x + this.playerViewRadius; ☃xxx++) {
            PlayerChunkMapEntry ☃xxxx = this.getEntry(☃xx, ☃xxx);
            if (☃xxxx != null) {
               ☃xxxx.removePlayer(☃);
            }
         }
      }

      this.players.remove(☃);
      this.markSortPending();
   }

   private boolean overlaps(int var1, int var2, int var3, int var4, int var5) {
      int ☃ = ☃ - ☃;
      int ☃x = ☃ - ☃;
      return ☃ < -☃ || ☃ > ☃ ? false : ☃x >= -☃ && ☃x <= ☃;
   }

   public void updateMovingPlayer(EntityPlayerMP var1) {
      int ☃ = (int)☃.posX >> 4;
      int ☃x = (int)☃.posZ >> 4;
      double ☃xx = ☃.managedPosX - ☃.posX;
      double ☃xxx = ☃.managedPosZ - ☃.posZ;
      double ☃xxxx = ☃xx * ☃xx + ☃xxx * ☃xxx;
      if (!(☃xxxx < 64.0)) {
         int ☃xxxxx = (int)☃.managedPosX >> 4;
         int ☃xxxxxx = (int)☃.managedPosZ >> 4;
         int ☃xxxxxxx = this.playerViewRadius;
         int ☃xxxxxxxx = ☃ - ☃xxxxx;
         int ☃xxxxxxxxx = ☃x - ☃xxxxxx;
         if (☃xxxxxxxx != 0 || ☃xxxxxxxxx != 0) {
            for (int ☃xxxxxxxxxx = ☃ - ☃xxxxxxx; ☃xxxxxxxxxx <= ☃ + ☃xxxxxxx; ☃xxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxx = ☃x - ☃xxxxxxx; ☃xxxxxxxxxxx <= ☃x + ☃xxxxxxx; ☃xxxxxxxxxxx++) {
                  if (!this.overlaps(☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx)) {
                     this.getOrCreateEntry(☃xxxxxxxxxx, ☃xxxxxxxxxxx).addPlayer(☃);
                  }

                  if (!this.overlaps(☃xxxxxxxxxx - ☃xxxxxxxx, ☃xxxxxxxxxxx - ☃xxxxxxxxx, ☃, ☃x, ☃xxxxxxx)) {
                     PlayerChunkMapEntry ☃xxxxxxxxxxxx = this.getEntry(☃xxxxxxxxxx - ☃xxxxxxxx, ☃xxxxxxxxxxx - ☃xxxxxxxxx);
                     if (☃xxxxxxxxxxxx != null) {
                        ☃xxxxxxxxxxxx.removePlayer(☃);
                     }
                  }
               }
            }

            ☃.managedPosX = ☃.posX;
            ☃.managedPosZ = ☃.posZ;
            this.markSortPending();
         }
      }
   }

   public boolean isPlayerWatchingChunk(EntityPlayerMP var1, int var2, int var3) {
      PlayerChunkMapEntry ☃ = this.getEntry(☃, ☃);
      return ☃ != null && ☃.containsPlayer(☃) && ☃.isSentToPlayers();
   }

   public void setPlayerViewRadius(int var1) {
      ☃ = MathHelper.clamp(☃, 3, 32);
      if (☃ != this.playerViewRadius) {
         int ☃ = ☃ - this.playerViewRadius;

         for (EntityPlayerMP ☃x : Lists.newArrayList(this.players)) {
            int ☃xx = (int)☃x.posX >> 4;
            int ☃xxx = (int)☃x.posZ >> 4;
            if (☃ > 0) {
               for (int ☃xxxx = ☃xx - ☃; ☃xxxx <= ☃xx + ☃; ☃xxxx++) {
                  for (int ☃xxxxx = ☃xxx - ☃; ☃xxxxx <= ☃xxx + ☃; ☃xxxxx++) {
                     PlayerChunkMapEntry ☃xxxxxx = this.getOrCreateEntry(☃xxxx, ☃xxxxx);
                     if (!☃xxxxxx.containsPlayer(☃x)) {
                        ☃xxxxxx.addPlayer(☃x);
                     }
                  }
               }
            } else {
               for (int ☃xxxx = ☃xx - this.playerViewRadius; ☃xxxx <= ☃xx + this.playerViewRadius; ☃xxxx++) {
                  for (int ☃xxxxxx = ☃xxx - this.playerViewRadius; ☃xxxxxx <= ☃xxx + this.playerViewRadius; ☃xxxxxx++) {
                     if (!this.overlaps(☃xxxx, ☃xxxxxx, ☃xx, ☃xxx, ☃)) {
                        this.getOrCreateEntry(☃xxxx, ☃xxxxxx).removePlayer(☃x);
                     }
                  }
               }
            }
         }

         this.playerViewRadius = ☃;
         this.markSortPending();
      }
   }

   private void markSortPending() {
      this.sortMissingChunks = true;
      this.sortSendToPlayers = true;
   }

   public static int getFurthestViewableBlock(int var0) {
      return ☃ * 16 - 16;
   }

   private static long getIndex(int var0, int var1) {
      return ☃ + 2147483647L | ☃ + 2147483647L << 32;
   }

   public void entryChanged(PlayerChunkMapEntry var1) {
      this.dirtyEntries.add(☃);
   }

   public void removeEntry(PlayerChunkMapEntry var1) {
      ChunkPos ☃ = ☃.getPos();
      long ☃x = getIndex(☃.x, ☃.z);
      ☃.updateChunkInhabitedTime();
      this.entryMap.remove(☃x);
      this.entries.remove(☃);
      this.dirtyEntries.remove(☃);
      this.pendingSendToPlayers.remove(☃);
      this.entriesWithoutChunks.remove(☃);
      Chunk ☃xx = ☃.getChunk();
      if (☃xx != null) {
         this.getWorldServer().getChunkProvider().queueUnload(☃xx);
      }
   }
}
