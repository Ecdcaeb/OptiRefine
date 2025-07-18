package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketClientStatus implements Packet<INetHandlerPlayServer> {
   private CPacketClientStatus.State status;

   public CPacketClientStatus() {
   }

   public CPacketClientStatus(CPacketClientStatus.State var1) {
      this.status = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.status = ☃.readEnumValue(CPacketClientStatus.State.class);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.status);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processClientStatus(this);
   }

   public CPacketClientStatus.State getStatus() {
      return this.status;
   }

   public static enum State {
      PERFORM_RESPAWN,
      REQUEST_STATS;
   }
}
