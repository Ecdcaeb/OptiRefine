package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketKeepAlive implements Packet<INetHandlerPlayServer> {
   private long key;

   public CPacketKeepAlive() {
   }

   public CPacketKeepAlive(long var1) {
      this.key = ☃;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processKeepAlive(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.key = ☃.readLong();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeLong(this.key);
   }

   public long getKey() {
      return this.key;
   }
}
