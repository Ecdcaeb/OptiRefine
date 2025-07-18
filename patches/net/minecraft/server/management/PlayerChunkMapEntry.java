package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerChunkMapEntry {
   private static final Logger LOGGER = LogManager.getLogger();
   private final PlayerChunkMap playerChunkMap;
   private final List<EntityPlayerMP> players = Lists.newArrayList();
   private final ChunkPos pos;
   private final short[] changedBlocks = new short[64];
   @Nullable
   private Chunk chunk;
   private int changes;
   private int changedSectionFilter;
   private long lastUpdateInhabitedTime;
   private boolean sentToPlayers;

   public PlayerChunkMapEntry(PlayerChunkMap var1, int var2, int var3) {
      this.playerChunkMap = ☃;
      this.pos = new ChunkPos(☃, ☃);
      this.chunk = ☃.getWorldServer().getChunkProvider().loadChunk(☃, ☃);
   }

   public ChunkPos getPos() {
      return this.pos;
   }

   public void addPlayer(EntityPlayerMP var1) {
      if (this.players.contains(☃)) {
         LOGGER.debug("Failed to add player. {} already is in chunk {}, {}", ☃, this.pos.x, this.pos.z);
      } else {
         if (this.players.isEmpty()) {
            this.lastUpdateInhabitedTime = this.playerChunkMap.getWorldServer().getTotalWorldTime();
         }

         this.players.add(☃);
         if (this.sentToPlayers) {
            this.sendToPlayer(☃);
         }
      }
   }

   public void removePlayer(EntityPlayerMP var1) {
      if (this.players.contains(☃)) {
         if (this.sentToPlayers) {
            ☃.connection.sendPacket(new SPacketUnloadChunk(this.pos.x, this.pos.z));
         }

         this.players.remove(☃);
         if (this.players.isEmpty()) {
            this.playerChunkMap.removeEntry(this);
         }
      }
   }

   public boolean providePlayerChunk(boolean var1) {
      if (this.chunk != null) {
         return true;
      } else {
         if (☃) {
            this.chunk = this.playerChunkMap.getWorldServer().getChunkProvider().provideChunk(this.pos.x, this.pos.z);
         } else {
            this.chunk = this.playerChunkMap.getWorldServer().getChunkProvider().loadChunk(this.pos.x, this.pos.z);
         }

         return this.chunk != null;
      }
   }

   public boolean sendToPlayers() {
      if (this.sentToPlayers) {
         return true;
      } else if (this.chunk == null) {
         return false;
      } else if (!this.chunk.isPopulated()) {
         return false;
      } else {
         this.changes = 0;
         this.changedSectionFilter = 0;
         this.sentToPlayers = true;
         Packet<?> ☃ = new SPacketChunkData(this.chunk, 65535);

         for (EntityPlayerMP ☃x : this.players) {
            ☃x.connection.sendPacket(☃);
            this.playerChunkMap.getWorldServer().getEntityTracker().sendLeashedEntitiesInChunk(☃x, this.chunk);
         }

         return true;
      }
   }

   public void sendToPlayer(EntityPlayerMP var1) {
      if (this.sentToPlayers) {
         ☃.connection.sendPacket(new SPacketChunkData(this.chunk, 65535));
         this.playerChunkMap.getWorldServer().getEntityTracker().sendLeashedEntitiesInChunk(☃, this.chunk);
      }
   }

   public void updateChunkInhabitedTime() {
      long ☃ = this.playerChunkMap.getWorldServer().getTotalWorldTime();
      if (this.chunk != null) {
         this.chunk.setInhabitedTime(this.chunk.getInhabitedTime() + ☃ - this.lastUpdateInhabitedTime);
      }

      this.lastUpdateInhabitedTime = ☃;
   }

   public void blockChanged(int var1, int var2, int var3) {
      if (this.sentToPlayers) {
         if (this.changes == 0) {
            this.playerChunkMap.entryChanged(this);
         }

         this.changedSectionFilter |= 1 << (☃ >> 4);
         if (this.changes < 64) {
            short ☃ = (short)(☃ << 12 | ☃ << 8 | ☃);

            for (int ☃x = 0; ☃x < this.changes; ☃x++) {
               if (this.changedBlocks[☃x] == ☃) {
                  return;
               }
            }

            this.changedBlocks[this.changes++] = ☃;
         }
      }
   }

   public void sendPacket(Packet<?> var1) {
      if (this.sentToPlayers) {
         for (int ☃ = 0; ☃ < this.players.size(); ☃++) {
            this.players.get(☃).connection.sendPacket(☃);
         }
      }
   }

   public void update() {
      if (this.sentToPlayers && this.chunk != null) {
         if (this.changes != 0) {
            if (this.changes == 1) {
               int ☃ = (this.changedBlocks[0] >> 12 & 15) + this.pos.x * 16;
               int ☃x = this.changedBlocks[0] & 255;
               int ☃xx = (this.changedBlocks[0] >> 8 & 15) + this.pos.z * 16;
               BlockPos ☃xxx = new BlockPos(☃, ☃x, ☃xx);
               this.sendPacket(new SPacketBlockChange(this.playerChunkMap.getWorldServer(), ☃xxx));
               if (this.playerChunkMap.getWorldServer().getBlockState(☃xxx).getBlock().hasTileEntity()) {
                  this.sendBlockEntity(this.playerChunkMap.getWorldServer().getTileEntity(☃xxx));
               }
            } else if (this.changes == 64) {
               this.sendPacket(new SPacketChunkData(this.chunk, this.changedSectionFilter));
            } else {
               this.sendPacket(new SPacketMultiBlockChange(this.changes, this.changedBlocks, this.chunk));

               for (int ☃ = 0; ☃ < this.changes; ☃++) {
                  int ☃x = (this.changedBlocks[☃] >> 12 & 15) + this.pos.x * 16;
                  int ☃xx = this.changedBlocks[☃] & 255;
                  int ☃xxx = (this.changedBlocks[☃] >> 8 & 15) + this.pos.z * 16;
                  BlockPos ☃xxxx = new BlockPos(☃x, ☃xx, ☃xxx);
                  if (this.playerChunkMap.getWorldServer().getBlockState(☃xxxx).getBlock().hasTileEntity()) {
                     this.sendBlockEntity(this.playerChunkMap.getWorldServer().getTileEntity(☃xxxx));
                  }
               }
            }

            this.changes = 0;
            this.changedSectionFilter = 0;
         }
      }
   }

   private void sendBlockEntity(@Nullable TileEntity var1) {
      if (☃ != null) {
         SPacketUpdateTileEntity ☃ = ☃.getUpdatePacket();
         if (☃ != null) {
            this.sendPacket(☃);
         }
      }
   }

   public boolean containsPlayer(EntityPlayerMP var1) {
      return this.players.contains(☃);
   }

   public boolean hasPlayerMatching(Predicate<EntityPlayerMP> var1) {
      return Iterables.tryFind(this.players, ☃).isPresent();
   }

   public boolean hasPlayerMatchingInRange(double var1, Predicate<EntityPlayerMP> var3) {
      int ☃ = 0;

      for (int ☃x = this.players.size(); ☃ < ☃x; ☃++) {
         EntityPlayerMP ☃xx = this.players.get(☃);
         if (☃.apply(☃xx) && this.pos.getDistanceSq(☃xx) < ☃ * ☃) {
            return true;
         }
      }

      return false;
   }

   public boolean isSentToPlayers() {
      return this.sentToPlayers;
   }

   @Nullable
   public Chunk getChunk() {
      return this.chunk;
   }

   public double getClosestPlayerDistance() {
      double ☃ = Double.MAX_VALUE;

      for (EntityPlayerMP ☃x : this.players) {
         double ☃xx = this.pos.getDistanceSq(☃x);
         if (☃xx < ☃) {
            ☃ = ☃xx;
         }
      }

      return ☃;
   }
}
