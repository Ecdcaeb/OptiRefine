package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCloseWindow implements Packet<INetHandlerPlayClient> {
   private int windowId;

   public SPacketCloseWindow() {
   }

   public SPacketCloseWindow(int var1) {
      this.windowId = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleCloseWindow(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readUnsignedByte();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
   }
}
