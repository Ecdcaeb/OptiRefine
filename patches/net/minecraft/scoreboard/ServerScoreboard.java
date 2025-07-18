package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.server.MinecraftServer;

public class ServerScoreboard extends Scoreboard {
   private final MinecraftServer server;
   private final Set<ScoreObjective> addedObjectives = Sets.newHashSet();
   private Runnable[] dirtyRunnables = new Runnable[0];

   public ServerScoreboard(MinecraftServer var1) {
      this.server = ☃;
   }

   @Override
   public void onScoreUpdated(Score var1) {
      super.onScoreUpdated(☃);
      if (this.addedObjectives.contains(☃.getObjective())) {
         this.server.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(☃));
      }

      this.markSaveDataDirty();
   }

   @Override
   public void broadcastScoreUpdate(String var1) {
      super.broadcastScoreUpdate(☃);
      this.server.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(☃));
      this.markSaveDataDirty();
   }

   @Override
   public void broadcastScoreUpdate(String var1, ScoreObjective var2) {
      super.broadcastScoreUpdate(☃, ☃);
      this.server.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(☃, ☃));
      this.markSaveDataDirty();
   }

   @Override
   public void setObjectiveInDisplaySlot(int var1, ScoreObjective var2) {
      ScoreObjective ☃ = this.getObjectiveInDisplaySlot(☃);
      super.setObjectiveInDisplaySlot(☃, ☃);
      if (☃ != ☃ && ☃ != null) {
         if (this.getObjectiveDisplaySlotCount(☃) > 0) {
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(☃, ☃));
         } else {
            this.sendDisplaySlotRemovalPackets(☃);
         }
      }

      if (☃ != null) {
         if (this.addedObjectives.contains(☃)) {
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(☃, ☃));
         } else {
            this.addObjective(☃);
         }
      }

      this.markSaveDataDirty();
   }

   @Override
   public boolean addPlayerToTeam(String var1, String var2) {
      if (super.addPlayerToTeam(☃, ☃)) {
         ScorePlayerTeam ☃ = this.getTeam(☃);
         this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(☃, Arrays.asList(☃), 3));
         this.markSaveDataDirty();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void removePlayerFromTeam(String var1, ScorePlayerTeam var2) {
      super.removePlayerFromTeam(☃, ☃);
      this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(☃, Arrays.asList(☃), 4));
      this.markSaveDataDirty();
   }

   @Override
   public void onScoreObjectiveAdded(ScoreObjective var1) {
      super.onScoreObjectiveAdded(☃);
      this.markSaveDataDirty();
   }

   @Override
   public void onObjectiveDisplayNameChanged(ScoreObjective var1) {
      super.onObjectiveDisplayNameChanged(☃);
      if (this.addedObjectives.contains(☃)) {
         this.server.getPlayerList().sendPacketToAllPlayers(new SPacketScoreboardObjective(☃, 2));
      }

      this.markSaveDataDirty();
   }

   @Override
   public void onScoreObjectiveRemoved(ScoreObjective var1) {
      super.onScoreObjectiveRemoved(☃);
      if (this.addedObjectives.contains(☃)) {
         this.sendDisplaySlotRemovalPackets(☃);
      }

      this.markSaveDataDirty();
   }

   @Override
   public void broadcastTeamCreated(ScorePlayerTeam var1) {
      super.broadcastTeamCreated(☃);
      this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(☃, 0));
      this.markSaveDataDirty();
   }

   @Override
   public void broadcastTeamInfoUpdate(ScorePlayerTeam var1) {
      super.broadcastTeamInfoUpdate(☃);
      this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(☃, 2));
      this.markSaveDataDirty();
   }

   @Override
   public void broadcastTeamRemove(ScorePlayerTeam var1) {
      super.broadcastTeamRemove(☃);
      this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(☃, 1));
      this.markSaveDataDirty();
   }

   public void addDirtyRunnable(Runnable var1) {
      this.dirtyRunnables = Arrays.copyOf(this.dirtyRunnables, this.dirtyRunnables.length + 1);
      this.dirtyRunnables[this.dirtyRunnables.length - 1] = ☃;
   }

   protected void markSaveDataDirty() {
      for (Runnable ☃ : this.dirtyRunnables) {
         ☃.run();
      }
   }

   public List<Packet<?>> getCreatePackets(ScoreObjective var1) {
      List<Packet<?>> ☃ = Lists.newArrayList();
      ☃.add(new SPacketScoreboardObjective(☃, 0));

      for (int ☃x = 0; ☃x < 19; ☃x++) {
         if (this.getObjectiveInDisplaySlot(☃x) == ☃) {
            ☃.add(new SPacketDisplayObjective(☃x, ☃));
         }
      }

      for (Score ☃xx : this.getSortedScores(☃)) {
         ☃.add(new SPacketUpdateScore(☃xx));
      }

      return ☃;
   }

   public void addObjective(ScoreObjective var1) {
      List<Packet<?>> ☃ = this.getCreatePackets(☃);

      for (EntityPlayerMP ☃x : this.server.getPlayerList().getPlayers()) {
         for (Packet<?> ☃xx : ☃) {
            ☃x.connection.sendPacket(☃xx);
         }
      }

      this.addedObjectives.add(☃);
   }

   public List<Packet<?>> getDestroyPackets(ScoreObjective var1) {
      List<Packet<?>> ☃ = Lists.newArrayList();
      ☃.add(new SPacketScoreboardObjective(☃, 1));

      for (int ☃x = 0; ☃x < 19; ☃x++) {
         if (this.getObjectiveInDisplaySlot(☃x) == ☃) {
            ☃.add(new SPacketDisplayObjective(☃x, ☃));
         }
      }

      return ☃;
   }

   public void sendDisplaySlotRemovalPackets(ScoreObjective var1) {
      List<Packet<?>> ☃ = this.getDestroyPackets(☃);

      for (EntityPlayerMP ☃x : this.server.getPlayerList().getPlayers()) {
         for (Packet<?> ☃xx : ☃) {
            ☃x.connection.sendPacket(☃xx);
         }
      }

      this.addedObjectives.remove(☃);
   }

   public int getObjectiveDisplaySlotCount(ScoreObjective var1) {
      int ☃ = 0;

      for (int ☃x = 0; ☃x < 19; ☃x++) {
         if (this.getObjectiveInDisplaySlot(☃x) == ☃) {
            ☃++;
         }
      }

      return ☃;
   }
}
