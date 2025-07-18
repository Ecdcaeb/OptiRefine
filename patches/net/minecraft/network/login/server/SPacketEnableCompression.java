package net.minecraft.network.login.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class SPacketEnableCompression implements Packet<INetHandlerLoginClient> {
   private int compressionThreshold;

   public SPacketEnableCompression() {
   }

   public SPacketEnableCompression(int var1) {
      this.compressionThreshold = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.compressionThreshold = ☃.readVarInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.compressionThreshold);
   }

   public void processPacket(INetHandlerLoginClient var1) {
      ☃.handleEnableCompression(this);
   }

   public int getCompressionThreshold() {
      return this.compressionThreshold;
   }
}
