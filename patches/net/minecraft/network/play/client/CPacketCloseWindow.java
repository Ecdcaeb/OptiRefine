package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCloseWindow implements Packet<INetHandlerPlayServer> {
   private int windowId;

   public CPacketCloseWindow() {
   }

   public CPacketCloseWindow(int var1) {
      this.windowId = ☃;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processCloseWindow(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readByte();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
   }
}
