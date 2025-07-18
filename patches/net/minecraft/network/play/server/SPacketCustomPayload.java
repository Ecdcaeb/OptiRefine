package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCustomPayload implements Packet<INetHandlerPlayClient> {
   private String channel;
   private PacketBuffer data;

   public SPacketCustomPayload() {
   }

   public SPacketCustomPayload(String var1, PacketBuffer var2) {
      this.channel = ☃;
      this.data = ☃;
      if (☃.writerIndex() > 1048576) {
         throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.channel = ☃.readString(20);
      int ☃ = ☃.readableBytes();
      if (☃ >= 0 && ☃ <= 1048576) {
         this.data = new PacketBuffer(☃.readBytes(☃));
      } else {
         throw new IOException("Payload may not be larger than 1048576 bytes");
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.channel);
      ☃.writeBytes(this.data);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleCustomPayload(this);
   }

   public String getChannelName() {
      return this.channel;
   }

   public PacketBuffer getBufferData() {
      return this.data;
   }
}
