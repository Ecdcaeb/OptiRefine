package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketHeldItemChange implements Packet<INetHandlerPlayServer> {
   private int slotId;

   public CPacketHeldItemChange() {
   }

   public CPacketHeldItemChange(int var1) {
      this.slotId = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.slotId = ☃.readShort();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeShort(this.slotId);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processHeldItemChange(this);
   }

   public int getSlotId() {
      return this.slotId;
   }
}
