package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCustomPayload implements Packet<INetHandlerPlayServer> {
   private String channel;
   private PacketBuffer data;

   public CPacketCustomPayload() {
   }

   public CPacketCustomPayload(String var1, PacketBuffer var2) {
      this.channel = ☃;
      this.data = ☃;
      if (☃.writerIndex() > 32767) {
         throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.channel = ☃.readString(20);
      int ☃ = ☃.readableBytes();
      if (☃ >= 0 && ☃ <= 32767) {
         this.data = new PacketBuffer(☃.readBytes(☃));
      } else {
         throw new IOException("Payload may not be larger than 32767 bytes");
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.channel);
      ☃.writeBytes(this.data);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processCustomPayload(this);
      if (this.data != null) {
         this.data.release();
      }
   }

   public String getChannelName() {
      return this.channel;
   }

   public PacketBuffer getBufferData() {
      return this.data;
   }
}
