package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketUpdateScore implements Packet<INetHandlerPlayClient> {
   private String name = "";
   private String objective = "";
   private int value;
   private SPacketUpdateScore.Action action;

   public SPacketUpdateScore() {
   }

   public SPacketUpdateScore(Score var1) {
      this.name = ☃.getPlayerName();
      this.objective = ☃.getObjective().getName();
      this.value = ☃.getScorePoints();
      this.action = SPacketUpdateScore.Action.CHANGE;
   }

   public SPacketUpdateScore(String var1) {
      this.name = ☃;
      this.objective = "";
      this.value = 0;
      this.action = SPacketUpdateScore.Action.REMOVE;
   }

   public SPacketUpdateScore(String var1, ScoreObjective var2) {
      this.name = ☃;
      this.objective = ☃.getName();
      this.value = 0;
      this.action = SPacketUpdateScore.Action.REMOVE;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.name = ☃.readString(40);
      this.action = ☃.readEnumValue(SPacketUpdateScore.Action.class);
      this.objective = ☃.readString(16);
      if (this.action != SPacketUpdateScore.Action.REMOVE) {
         this.value = ☃.readVarInt();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.name);
      ☃.writeEnumValue(this.action);
      ☃.writeString(this.objective);
      if (this.action != SPacketUpdateScore.Action.REMOVE) {
         ☃.writeVarInt(this.value);
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleUpdateScore(this);
   }

   public String getPlayerName() {
      return this.name;
   }

   public String getObjectiveName() {
      return this.objective;
   }

   public int getScoreValue() {
      return this.value;
   }

   public SPacketUpdateScore.Action getScoreAction() {
      return this.action;
   }

   public static enum Action {
      CHANGE,
      REMOVE;
   }
}
