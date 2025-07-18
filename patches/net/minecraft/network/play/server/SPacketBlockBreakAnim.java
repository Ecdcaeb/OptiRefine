package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketBlockBreakAnim implements Packet<INetHandlerPlayClient> {
   private int breakerId;
   private BlockPos position;
   private int progress;

   public SPacketBlockBreakAnim() {
   }

   public SPacketBlockBreakAnim(int var1, BlockPos var2, int var3) {
      this.breakerId = ☃;
      this.position = ☃;
      this.progress = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.breakerId = ☃.readVarInt();
      this.position = ☃.readBlockPos();
      this.progress = ☃.readUnsignedByte();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.breakerId);
      ☃.writeBlockPos(this.position);
      ☃.writeByte(this.progress);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleBlockBreakAnim(this);
   }

   public int getBreakerId() {
      return this.breakerId;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public int getProgress() {
      return this.progress;
   }
}
