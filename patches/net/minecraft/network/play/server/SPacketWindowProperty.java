package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketWindowProperty implements Packet<INetHandlerPlayClient> {
   private int windowId;
   private int property;
   private int value;

   public SPacketWindowProperty() {
   }

   public SPacketWindowProperty(int var1, int var2, int var3) {
      this.windowId = ☃;
      this.property = ☃;
      this.value = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleWindowProperty(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readUnsignedByte();
      this.property = ☃.readShort();
      this.value = ☃.readShort();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
      ☃.writeShort(this.property);
      ☃.writeShort(this.value);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public int getProperty() {
      return this.property;
   }

   public int getValue() {
      return this.value;
   }
}
