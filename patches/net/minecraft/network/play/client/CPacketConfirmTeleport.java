package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketConfirmTeleport implements Packet<INetHandlerPlayServer> {
   private int telportId;

   public CPacketConfirmTeleport() {
   }

   public CPacketConfirmTeleport(int var1) {
      this.telportId = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.telportId = ☃.readVarInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.telportId);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processConfirmTeleport(this);
   }

   public int getTeleportId() {
      return this.telportId;
   }
}
