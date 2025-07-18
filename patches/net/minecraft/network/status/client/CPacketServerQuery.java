package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class CPacketServerQuery implements Packet<INetHandlerStatusServer> {
   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
   }

   public void processPacket(INetHandlerStatusServer var1) {
      ☃.processServerQuery(this);
   }
}
