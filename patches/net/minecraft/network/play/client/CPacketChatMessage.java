package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketChatMessage implements Packet<INetHandlerPlayServer> {
   private String message;

   public CPacketChatMessage() {
   }

   public CPacketChatMessage(String var1) {
      if (☃.length() > 256) {
         ☃ = ☃.substring(0, 256);
      }

      this.message = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.message = ☃.readString(256);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.message);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processChatMessage(this);
   }

   public String getMessage() {
      return this.message;
   }
}
