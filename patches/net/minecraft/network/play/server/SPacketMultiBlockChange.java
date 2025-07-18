package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class SPacketMultiBlockChange implements Packet<INetHandlerPlayClient> {
   private ChunkPos chunkPos;
   private SPacketMultiBlockChange.BlockUpdateData[] changedBlocks;

   public SPacketMultiBlockChange() {
   }

   public SPacketMultiBlockChange(int var1, short[] var2, Chunk var3) {
      this.chunkPos = new ChunkPos(☃.x, ☃.z);
      this.changedBlocks = new SPacketMultiBlockChange.BlockUpdateData[☃];

      for (int ☃ = 0; ☃ < this.changedBlocks.length; ☃++) {
         this.changedBlocks[☃] = new SPacketMultiBlockChange.BlockUpdateData(☃[☃], ☃);
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.chunkPos = new ChunkPos(☃.readInt(), ☃.readInt());
      this.changedBlocks = new SPacketMultiBlockChange.BlockUpdateData[☃.readVarInt()];

      for (int ☃ = 0; ☃ < this.changedBlocks.length; ☃++) {
         this.changedBlocks[☃] = new SPacketMultiBlockChange.BlockUpdateData(☃.readShort(), Block.BLOCK_STATE_IDS.getByValue(☃.readVarInt()));
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeInt(this.chunkPos.x);
      ☃.writeInt(this.chunkPos.z);
      ☃.writeVarInt(this.changedBlocks.length);

      for (SPacketMultiBlockChange.BlockUpdateData ☃ : this.changedBlocks) {
         ☃.writeShort(☃.getOffset());
         ☃.writeVarInt(Block.BLOCK_STATE_IDS.get(☃.getBlockState()));
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleMultiBlockChange(this);
   }

   public SPacketMultiBlockChange.BlockUpdateData[] getChangedBlocks() {
      return this.changedBlocks;
   }

   public class BlockUpdateData {
      private final short offset;
      private final IBlockState blockState;

      public BlockUpdateData(short var2, IBlockState var3) {
         this.offset = ☃;
         this.blockState = ☃;
      }

      public BlockUpdateData(short var2, Chunk var3) {
         this.offset = ☃;
         this.blockState = ☃.getBlockState(this.getPos());
      }

      public BlockPos getPos() {
         return new BlockPos(SPacketMultiBlockChange.this.chunkPos.getBlock(this.offset >> 12 & 15, this.offset & 255, this.offset >> 8 & 15));
      }

      public short getOffset() {
         return this.offset;
      }

      public IBlockState getBlockState() {
         return this.blockState;
      }
   }
}
