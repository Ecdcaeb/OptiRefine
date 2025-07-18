package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class CPacketPing implements Packet<INetHandlerStatusServer> {
   private long clientTime;

   public CPacketPing() {
   }

   public CPacketPing(long var1) {
      this.clientTime = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.clientTime = ☃.readLong();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeLong(this.clientTime);
   }

   public void processPacket(INetHandlerStatusServer var1) {
      ☃.processPing(this);
   }

   public long getClientTime() {
      return this.clientTime;
   }
}
