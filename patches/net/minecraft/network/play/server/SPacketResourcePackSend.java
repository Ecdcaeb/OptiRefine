package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketResourcePackSend implements Packet<INetHandlerPlayClient> {
   private String url;
   private String hash;

   public SPacketResourcePackSend() {
   }

   public SPacketResourcePackSend(String var1, String var2) {
      this.url = ☃;
      this.hash = ☃;
      if (☃.length() > 40) {
         throw new IllegalArgumentException("Hash is too long (max 40, was " + ☃.length() + ")");
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.url = ☃.readString(32767);
      this.hash = ☃.readString(40);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.url);
      ☃.writeString(this.hash);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleResourcePack(this);
   }

   public String getURL() {
      return this.url;
   }

   public String getHash() {
      return this.hash;
   }
}
