package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketKeepAlive implements Packet<INetHandlerPlayClient> {
   private long id;

   public SPacketKeepAlive() {
   }

   public SPacketKeepAlive(long var1) {
      this.id = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleKeepAlive(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.id = ☃.readLong();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeLong(this.id);
   }

   public long getId() {
      return this.id;
   }
}
