package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

public class SPacketTeams implements Packet<INetHandlerPlayClient> {
   private String name = "";
   private String displayName = "";
   private String prefix = "";
   private String suffix = "";
   private String nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
   private String collisionRule = Team.CollisionRule.ALWAYS.name;
   private int color = -1;
   private final Collection<String> players = Lists.newArrayList();
   private int action;
   private int friendlyFlags;

   public SPacketTeams() {
   }

   public SPacketTeams(ScorePlayerTeam var1, int var2) {
      this.name = ☃.getName();
      this.action = ☃;
      if (☃ == 0 || ☃ == 2) {
         this.displayName = ☃.getDisplayName();
         this.prefix = ☃.getPrefix();
         this.suffix = ☃.getSuffix();
         this.friendlyFlags = ☃.getFriendlyFlags();
         this.nameTagVisibility = ☃.getNameTagVisibility().internalName;
         this.collisionRule = ☃.getCollisionRule().name;
         this.color = ☃.getColor().getColorIndex();
      }

      if (☃ == 0) {
         this.players.addAll(☃.getMembershipCollection());
      }
   }

   public SPacketTeams(ScorePlayerTeam var1, Collection<String> var2, int var3) {
      if (☃ != 3 && ☃ != 4) {
         throw new IllegalArgumentException("Method must be join or leave for player constructor");
      } else if (☃ != null && !☃.isEmpty()) {
         this.action = ☃;
         this.name = ☃.getName();
         this.players.addAll(☃);
      } else {
         throw new IllegalArgumentException("Players cannot be null/empty");
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.name = ☃.readString(16);
      this.action = ☃.readByte();
      if (this.action == 0 || this.action == 2) {
         this.displayName = ☃.readString(32);
         this.prefix = ☃.readString(16);
         this.suffix = ☃.readString(16);
         this.friendlyFlags = ☃.readByte();
         this.nameTagVisibility = ☃.readString(32);
         this.collisionRule = ☃.readString(32);
         this.color = ☃.readByte();
      }

      if (this.action == 0 || this.action == 3 || this.action == 4) {
         int ☃ = ☃.readVarInt();

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            this.players.add(☃.readString(40));
         }
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.name);
      ☃.writeByte(this.action);
      if (this.action == 0 || this.action == 2) {
         ☃.writeString(this.displayName);
         ☃.writeString(this.prefix);
         ☃.writeString(this.suffix);
         ☃.writeByte(this.friendlyFlags);
         ☃.writeString(this.nameTagVisibility);
         ☃.writeString(this.collisionRule);
         ☃.writeByte(this.color);
      }

      if (this.action == 0 || this.action == 3 || this.action == 4) {
         ☃.writeVarInt(this.players.size());

         for (String ☃ : this.players) {
            ☃.writeString(☃);
         }
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleTeams(this);
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public Collection<String> getPlayers() {
      return this.players;
   }

   public int getAction() {
      return this.action;
   }

   public int getFriendlyFlags() {
      return this.friendlyFlags;
   }

   public int getColor() {
      return this.color;
   }

   public String getNameTagVisibility() {
      return this.nameTagVisibility;
   }

   public String getCollisionRule() {
      return this.collisionRule;
   }
}
