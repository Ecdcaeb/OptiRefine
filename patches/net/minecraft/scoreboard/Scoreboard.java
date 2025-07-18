package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

public class Scoreboard {
   private final Map<String, ScoreObjective> scoreObjectives = Maps.newHashMap();
   private final Map<IScoreCriteria, List<ScoreObjective>> scoreObjectiveCriterias = Maps.newHashMap();
   private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives = Maps.newHashMap();
   private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
   private final Map<String, ScorePlayerTeam> teams = Maps.newHashMap();
   private final Map<String, ScorePlayerTeam> teamMemberships = Maps.newHashMap();
   private static String[] displaySlots;

   @Nullable
   public ScoreObjective getObjective(String var1) {
      return this.scoreObjectives.get(☃);
   }

   public ScoreObjective addScoreObjective(String var1, IScoreCriteria var2) {
      if (☃.length() > 16) {
         throw new IllegalArgumentException("The objective name '" + ☃ + "' is too long!");
      } else {
         ScoreObjective ☃ = this.getObjective(☃);
         if (☃ != null) {
            throw new IllegalArgumentException("An objective with the name '" + ☃ + "' already exists!");
         } else {
            ☃ = new ScoreObjective(this, ☃, ☃);
            List<ScoreObjective> ☃x = this.scoreObjectiveCriterias.get(☃);
            if (☃x == null) {
               ☃x = Lists.newArrayList();
               this.scoreObjectiveCriterias.put(☃, ☃x);
            }

            ☃x.add(☃);
            this.scoreObjectives.put(☃, ☃);
            this.onScoreObjectiveAdded(☃);
            return ☃;
         }
      }
   }

   public Collection<ScoreObjective> getObjectivesFromCriteria(IScoreCriteria var1) {
      Collection<ScoreObjective> ☃ = this.scoreObjectiveCriterias.get(☃);
      return ☃ == null ? Lists.newArrayList() : Lists.newArrayList(☃);
   }

   public boolean entityHasObjective(String var1, ScoreObjective var2) {
      Map<ScoreObjective, Score> ☃ = this.entitiesScoreObjectives.get(☃);
      if (☃ == null) {
         return false;
      } else {
         Score ☃x = ☃.get(☃);
         return ☃x != null;
      }
   }

   public Score getOrCreateScore(String var1, ScoreObjective var2) {
      if (☃.length() > 40) {
         throw new IllegalArgumentException("The player name '" + ☃ + "' is too long!");
      } else {
         Map<ScoreObjective, Score> ☃ = this.entitiesScoreObjectives.get(☃);
         if (☃ == null) {
            ☃ = Maps.newHashMap();
            this.entitiesScoreObjectives.put(☃, ☃);
         }

         Score ☃x = ☃.get(☃);
         if (☃x == null) {
            ☃x = new Score(this, ☃, ☃);
            ☃.put(☃, ☃x);
         }

         return ☃x;
      }
   }

   public Collection<Score> getSortedScores(ScoreObjective var1) {
      List<Score> ☃ = Lists.newArrayList();

      for (Map<ScoreObjective, Score> ☃x : this.entitiesScoreObjectives.values()) {
         Score ☃xx = ☃x.get(☃);
         if (☃xx != null) {
            ☃.add(☃xx);
         }
      }

      Collections.sort(☃, Score.SCORE_COMPARATOR);
      return ☃;
   }

   public Collection<ScoreObjective> getScoreObjectives() {
      return this.scoreObjectives.values();
   }

   public Collection<String> getObjectiveNames() {
      return this.entitiesScoreObjectives.keySet();
   }

   public void removeObjectiveFromEntity(String var1, ScoreObjective var2) {
      if (☃ == null) {
         Map<ScoreObjective, Score> ☃ = this.entitiesScoreObjectives.remove(☃);
         if (☃ != null) {
            this.broadcastScoreUpdate(☃);
         }
      } else {
         Map<ScoreObjective, Score> ☃ = this.entitiesScoreObjectives.get(☃);
         if (☃ != null) {
            Score ☃x = ☃.remove(☃);
            if (☃.size() < 1) {
               Map<ScoreObjective, Score> ☃xx = this.entitiesScoreObjectives.remove(☃);
               if (☃xx != null) {
                  this.broadcastScoreUpdate(☃);
               }
            } else if (☃x != null) {
               this.broadcastScoreUpdate(☃, ☃);
            }
         }
      }
   }

   public Collection<Score> getScores() {
      Collection<Map<ScoreObjective, Score>> ☃ = this.entitiesScoreObjectives.values();
      List<Score> ☃x = Lists.newArrayList();

      for (Map<ScoreObjective, Score> ☃xx : ☃) {
         ☃x.addAll(☃xx.values());
      }

      return ☃x;
   }

   public Map<ScoreObjective, Score> getObjectivesForEntity(String var1) {
      Map<ScoreObjective, Score> ☃ = this.entitiesScoreObjectives.get(☃);
      if (☃ == null) {
         ☃ = Maps.newHashMap();
      }

      return ☃;
   }

   public void removeObjective(ScoreObjective var1) {
      this.scoreObjectives.remove(☃.getName());

      for (int ☃ = 0; ☃ < 19; ☃++) {
         if (this.getObjectiveInDisplaySlot(☃) == ☃) {
            this.setObjectiveInDisplaySlot(☃, null);
         }
      }

      List<ScoreObjective> ☃x = this.scoreObjectiveCriterias.get(☃.getCriteria());
      if (☃x != null) {
         ☃x.remove(☃);
      }

      for (Map<ScoreObjective, Score> ☃xx : this.entitiesScoreObjectives.values()) {
         ☃xx.remove(☃);
      }

      this.onScoreObjectiveRemoved(☃);
   }

   public void setObjectiveInDisplaySlot(int var1, ScoreObjective var2) {
      this.objectiveDisplaySlots[☃] = ☃;
   }

   @Nullable
   public ScoreObjective getObjectiveInDisplaySlot(int var1) {
      return this.objectiveDisplaySlots[☃];
   }

   public ScorePlayerTeam getTeam(String var1) {
      return this.teams.get(☃);
   }

   public ScorePlayerTeam createTeam(String var1) {
      if (☃.length() > 16) {
         throw new IllegalArgumentException("The team name '" + ☃ + "' is too long!");
      } else {
         ScorePlayerTeam ☃ = this.getTeam(☃);
         if (☃ != null) {
            throw new IllegalArgumentException("A team with the name '" + ☃ + "' already exists!");
         } else {
            ☃ = new ScorePlayerTeam(this, ☃);
            this.teams.put(☃, ☃);
            this.broadcastTeamCreated(☃);
            return ☃;
         }
      }
   }

   public void removeTeam(ScorePlayerTeam var1) {
      this.teams.remove(☃.getName());

      for (String ☃ : ☃.getMembershipCollection()) {
         this.teamMemberships.remove(☃);
      }

      this.broadcastTeamRemove(☃);
   }

   public boolean addPlayerToTeam(String var1, String var2) {
      if (☃.length() > 40) {
         throw new IllegalArgumentException("The player name '" + ☃ + "' is too long!");
      } else if (!this.teams.containsKey(☃)) {
         return false;
      } else {
         ScorePlayerTeam ☃ = this.getTeam(☃);
         if (this.getPlayersTeam(☃) != null) {
            this.removePlayerFromTeams(☃);
         }

         this.teamMemberships.put(☃, ☃);
         ☃.getMembershipCollection().add(☃);
         return true;
      }
   }

   public boolean removePlayerFromTeams(String var1) {
      ScorePlayerTeam ☃ = this.getPlayersTeam(☃);
      if (☃ != null) {
         this.removePlayerFromTeam(☃, ☃);
         return true;
      } else {
         return false;
      }
   }

   public void removePlayerFromTeam(String var1, ScorePlayerTeam var2) {
      if (this.getPlayersTeam(☃) != ☃) {
         throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + ☃.getName() + "'.");
      } else {
         this.teamMemberships.remove(☃);
         ☃.getMembershipCollection().remove(☃);
      }
   }

   public Collection<String> getTeamNames() {
      return this.teams.keySet();
   }

   public Collection<ScorePlayerTeam> getTeams() {
      return this.teams.values();
   }

   @Nullable
   public ScorePlayerTeam getPlayersTeam(String var1) {
      return this.teamMemberships.get(☃);
   }

   public void onScoreObjectiveAdded(ScoreObjective var1) {
   }

   public void onObjectiveDisplayNameChanged(ScoreObjective var1) {
   }

   public void onScoreObjectiveRemoved(ScoreObjective var1) {
   }

   public void onScoreUpdated(Score var1) {
   }

   public void broadcastScoreUpdate(String var1) {
   }

   public void broadcastScoreUpdate(String var1, ScoreObjective var2) {
   }

   public void broadcastTeamCreated(ScorePlayerTeam var1) {
   }

   public void broadcastTeamInfoUpdate(ScorePlayerTeam var1) {
   }

   public void broadcastTeamRemove(ScorePlayerTeam var1) {
   }

   public static String getObjectiveDisplaySlot(int var0) {
      switch (☃) {
         case 0:
            return "list";
         case 1:
            return "sidebar";
         case 2:
            return "belowName";
         default:
            if (☃ >= 3 && ☃ <= 18) {
               TextFormatting ☃ = TextFormatting.fromColorIndex(☃ - 3);
               if (☃ != null && ☃ != TextFormatting.RESET) {
                  return "sidebar.team." + ☃.getFriendlyName();
               }
            }

            return null;
      }
   }

   public static int getObjectiveDisplaySlotNumber(String var0) {
      if ("list".equalsIgnoreCase(☃)) {
         return 0;
      } else if ("sidebar".equalsIgnoreCase(☃)) {
         return 1;
      } else if ("belowName".equalsIgnoreCase(☃)) {
         return 2;
      } else {
         if (☃.startsWith("sidebar.team.")) {
            String ☃ = ☃.substring("sidebar.team.".length());
            TextFormatting ☃x = TextFormatting.getValueByName(☃);
            if (☃x != null && ☃x.getColorIndex() >= 0) {
               return ☃x.getColorIndex() + 3;
            }
         }

         return -1;
      }
   }

   public static String[] getDisplaySlotStrings() {
      if (displaySlots == null) {
         displaySlots = new String[19];

         for (int ☃ = 0; ☃ < 19; ☃++) {
            displaySlots[☃] = getObjectiveDisplaySlot(☃);
         }
      }

      return displaySlots;
   }

   public void removeEntity(Entity var1) {
      if (☃ != null && !(☃ instanceof EntityPlayer) && !☃.isEntityAlive()) {
         String ☃ = ☃.getCachedUniqueIdString();
         this.removeObjectiveFromEntity(☃, null);
         this.removePlayerFromTeams(☃);
      }
   }
}
