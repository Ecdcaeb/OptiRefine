package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketScoreboardObjective implements Packet<INetHandlerPlayClient> {
   private String objectiveName;
   private String objectiveValue;
   private IScoreCriteria.EnumRenderType type;
   private int action;

   public SPacketScoreboardObjective() {
   }

   public SPacketScoreboardObjective(ScoreObjective var1, int var2) {
      this.objectiveName = ☃.getName();
      this.objectiveValue = ☃.getDisplayName();
      this.type = ☃.getCriteria().getRenderType();
      this.action = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.objectiveName = ☃.readString(16);
      this.action = ☃.readByte();
      if (this.action == 0 || this.action == 2) {
         this.objectiveValue = ☃.readString(32);
         this.type = IScoreCriteria.EnumRenderType.getByName(☃.readString(16));
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.objectiveName);
      ☃.writeByte(this.action);
      if (this.action == 0 || this.action == 2) {
         ☃.writeString(this.objectiveValue);
         ☃.writeString(this.type.getRenderType());
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleScoreboardObjective(this);
   }

   public String getObjectiveName() {
      return this.objectiveName;
   }

   public String getObjectiveValue() {
      return this.objectiveValue;
   }

   public int getAction() {
      return this.action;
   }

   public IScoreCriteria.EnumRenderType getRenderType() {
      return this.type;
   }
}
