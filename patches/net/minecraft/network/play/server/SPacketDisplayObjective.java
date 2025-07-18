package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketDisplayObjective implements Packet<INetHandlerPlayClient> {
   private int position;
   private String scoreName;

   public SPacketDisplayObjective() {
   }

   public SPacketDisplayObjective(int var1, ScoreObjective var2) {
      this.position = ☃;
      if (☃ == null) {
         this.scoreName = "";
      } else {
         this.scoreName = ☃.getName();
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.position = ☃.readByte();
      this.scoreName = ☃.readString(16);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.position);
      ☃.writeString(this.scoreName);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleDisplayObjective(this);
   }

   public int getPosition() {
      return this.position;
   }

   public String getName() {
      return this.scoreName;
   }
}
