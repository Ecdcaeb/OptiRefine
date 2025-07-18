package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketEnchantItem implements Packet<INetHandlerPlayServer> {
   private int windowId;
   private int button;

   public CPacketEnchantItem() {
   }

   public CPacketEnchantItem(int var1, int var2) {
      this.windowId = ☃;
      this.button = ☃;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processEnchantItem(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readByte();
      this.button = ☃.readByte();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
      ☃.writeByte(this.button);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public int getButton() {
      return this.button;
   }
}
