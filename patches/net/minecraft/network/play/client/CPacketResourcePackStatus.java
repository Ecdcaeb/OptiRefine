package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketResourcePackStatus implements Packet<INetHandlerPlayServer> {
   private CPacketResourcePackStatus.Action action;

   public CPacketResourcePackStatus() {
   }

   public CPacketResourcePackStatus(CPacketResourcePackStatus.Action var1) {
      this.action = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.action = ☃.readEnumValue(CPacketResourcePackStatus.Action.class);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.action);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.handleResourcePackStatus(this);
   }

   public static enum Action {
      SUCCESSFULLY_LOADED,
      DECLINED,
      FAILED_DOWNLOAD,
      ACCEPTED;
   }
}
