package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class SPacketChunkData implements Packet<INetHandlerPlayClient> {
   private int chunkX;
   private int chunkZ;
   private int availableSections;
   private byte[] buffer;
   private List<NBTTagCompound> tileEntityTags;
   private boolean fullChunk;

   public SPacketChunkData() {
   }

   public SPacketChunkData(Chunk var1, int var2) {
      this.chunkX = ☃.x;
      this.chunkZ = ☃.z;
      this.fullChunk = ☃ == 65535;
      boolean ☃ = ☃.getWorld().provider.hasSkyLight();
      this.buffer = new byte[this.calculateChunkSize(☃, ☃, ☃)];
      this.availableSections = this.extractChunkData(new PacketBuffer(this.getWriteBuffer()), ☃, ☃, ☃);
      this.tileEntityTags = Lists.newArrayList();

      for (Entry<BlockPos, TileEntity> ☃x : ☃.getTileEntityMap().entrySet()) {
         BlockPos ☃xx = ☃x.getKey();
         TileEntity ☃xxx = ☃x.getValue();
         int ☃xxxx = ☃xx.getY() >> 4;
         if (this.isFullChunk() || (☃ & 1 << ☃xxxx) != 0) {
            NBTTagCompound ☃xxxxx = ☃xxx.getUpdateTag();
            this.tileEntityTags.add(☃xxxxx);
         }
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.chunkX = ☃.readInt();
      this.chunkZ = ☃.readInt();
      this.fullChunk = ☃.readBoolean();
      this.availableSections = ☃.readVarInt();
      int ☃ = ☃.readVarInt();
      if (☃ > 2097152) {
         throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
      } else {
         this.buffer = new byte[☃];
         ☃.readBytes(this.buffer);
         int ☃x = ☃.readVarInt();
         this.tileEntityTags = Lists.newArrayList();

         for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
            this.tileEntityTags.add(☃.readCompoundTag());
         }
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeInt(this.chunkX);
      ☃.writeInt(this.chunkZ);
      ☃.writeBoolean(this.fullChunk);
      ☃.writeVarInt(this.availableSections);
      ☃.writeVarInt(this.buffer.length);
      ☃.writeBytes(this.buffer);
      ☃.writeVarInt(this.tileEntityTags.size());

      for (NBTTagCompound ☃ : this.tileEntityTags) {
         ☃.writeCompoundTag(☃);
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleChunkData(this);
   }

   public PacketBuffer getReadBuffer() {
      return new PacketBuffer(Unpooled.wrappedBuffer(this.buffer));
   }

   private ByteBuf getWriteBuffer() {
      ByteBuf ☃ = Unpooled.wrappedBuffer(this.buffer);
      ☃.writerIndex(0);
      return ☃;
   }

   public int extractChunkData(PacketBuffer var1, Chunk var2, boolean var3, int var4) {
      int ☃ = 0;
      ExtendedBlockStorage[] ☃x = ☃.getBlockStorageArray();
      int ☃xx = 0;

      for (int ☃xxx = ☃x.length; ☃xx < ☃xxx; ☃xx++) {
         ExtendedBlockStorage ☃xxxx = ☃x[☃xx];
         if (☃xxxx != Chunk.NULL_BLOCK_STORAGE && (!this.isFullChunk() || !☃xxxx.isEmpty()) && (☃ & 1 << ☃xx) != 0) {
            ☃ |= 1 << ☃xx;
            ☃xxxx.getData().write(☃);
            ☃.writeBytes(☃xxxx.getBlockLight().getData());
            if (☃) {
               ☃.writeBytes(☃xxxx.getSkyLight().getData());
            }
         }
      }

      if (this.isFullChunk()) {
         ☃.writeBytes(☃.getBiomeArray());
      }

      return ☃;
   }

   protected int calculateChunkSize(Chunk var1, boolean var2, int var3) {
      int ☃ = 0;
      ExtendedBlockStorage[] ☃x = ☃.getBlockStorageArray();
      int ☃xx = 0;

      for (int ☃xxx = ☃x.length; ☃xx < ☃xxx; ☃xx++) {
         ExtendedBlockStorage ☃xxxx = ☃x[☃xx];
         if (☃xxxx != Chunk.NULL_BLOCK_STORAGE && (!this.isFullChunk() || !☃xxxx.isEmpty()) && (☃ & 1 << ☃xx) != 0) {
            ☃ += ☃xxxx.getData().getSerializedSize();
            ☃ += ☃xxxx.getBlockLight().getData().length;
            if (☃) {
               ☃ += ☃xxxx.getSkyLight().getData().length;
            }
         }
      }

      if (this.isFullChunk()) {
         ☃ += ☃.getBiomeArray().length;
      }

      return ☃;
   }

   public int getChunkX() {
      return this.chunkX;
   }

   public int getChunkZ() {
      return this.chunkZ;
   }

   public int getExtractedSize() {
      return this.availableSections;
   }

   public boolean isFullChunk() {
      return this.fullChunk;
   }

   public List<NBTTagCompound> getTileEntityTags() {
      return this.tileEntityTags;
   }
}
