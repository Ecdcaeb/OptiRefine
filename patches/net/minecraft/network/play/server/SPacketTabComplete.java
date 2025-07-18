package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketTabComplete implements Packet<INetHandlerPlayClient> {
   private String[] matches;

   public SPacketTabComplete() {
   }

   public SPacketTabComplete(String[] var1) {
      this.matches = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.matches = new String[☃.readVarInt()];

      for (int ☃ = 0; ☃ < this.matches.length; ☃++) {
         this.matches[☃] = ☃.readString(32767);
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.matches.length);

      for (String ☃ : this.matches) {
         ☃.writeString(☃);
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleTabComplete(this);
   }

   public String[] getMatches() {
      return this.matches;
   }
}
