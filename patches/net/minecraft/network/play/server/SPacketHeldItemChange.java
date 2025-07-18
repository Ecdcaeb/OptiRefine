package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketHeldItemChange implements Packet<INetHandlerPlayClient> {
   private int heldItemHotbarIndex;

   public SPacketHeldItemChange() {
   }

   public SPacketHeldItemChange(int var1) {
      this.heldItemHotbarIndex = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.heldItemHotbarIndex = ☃.readByte();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.heldItemHotbarIndex);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleHeldItemChange(this);
   }

   public int getHeldItemHotbarIndex() {
      return this.heldItemHotbarIndex;
   }
}
