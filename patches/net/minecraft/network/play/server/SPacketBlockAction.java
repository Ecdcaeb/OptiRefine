package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketBlockAction implements Packet<INetHandlerPlayClient> {
   private BlockPos blockPosition;
   private int instrument;
   private int pitch;
   private Block block;

   public SPacketBlockAction() {
   }

   public SPacketBlockAction(BlockPos var1, Block var2, int var3, int var4) {
      this.blockPosition = ☃;
      this.instrument = ☃;
      this.pitch = ☃;
      this.block = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.blockPosition = ☃.readBlockPos();
      this.instrument = ☃.readUnsignedByte();
      this.pitch = ☃.readUnsignedByte();
      this.block = Block.getBlockById(☃.readVarInt() & 4095);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBlockPos(this.blockPosition);
      ☃.writeByte(this.instrument);
      ☃.writeByte(this.pitch);
      ☃.writeVarInt(Block.getIdFromBlock(this.block) & 4095);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleBlockAction(this);
   }

   public BlockPos getBlockPosition() {
      return this.blockPosition;
   }

   public int getData1() {
      return this.instrument;
   }

   public int getData2() {
      return this.pitch;
   }

   public Block getBlockType() {
      return this.block;
   }
}
