package net.minecraft.scoreboard;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScoreboardSaveData extends WorldSavedData {
   private static final Logger LOGGER = LogManager.getLogger();
   private Scoreboard scoreboard;
   private NBTTagCompound delayedInitNbt;

   public ScoreboardSaveData() {
      this("scoreboard");
   }

   public ScoreboardSaveData(String var1) {
      super(☃);
   }

   public void setScoreboard(Scoreboard var1) {
      this.scoreboard = ☃;
      if (this.delayedInitNbt != null) {
         this.readFromNBT(this.delayedInitNbt);
      }
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      if (this.scoreboard == null) {
         this.delayedInitNbt = ☃;
      } else {
         this.readObjectives(☃.getTagList("Objectives", 10));
         this.readScores(☃.getTagList("PlayerScores", 10));
         if (☃.hasKey("DisplaySlots", 10)) {
            this.readDisplayConfig(☃.getCompoundTag("DisplaySlots"));
         }

         if (☃.hasKey("Teams", 9)) {
            this.readTeams(☃.getTagList("Teams", 10));
         }
      }
   }

   protected void readTeams(NBTTagList var1) {
      for (int ☃ = 0; ☃ < ☃.tagCount(); ☃++) {
         NBTTagCompound ☃x = ☃.getCompoundTagAt(☃);
         String ☃xx = ☃x.getString("Name");
         if (☃xx.length() > 16) {
            ☃xx = ☃xx.substring(0, 16);
         }

         ScorePlayerTeam ☃xxx = this.scoreboard.createTeam(☃xx);
         String ☃xxxx = ☃x.getString("DisplayName");
         if (☃xxxx.length() > 32) {
            ☃xxxx = ☃xxxx.substring(0, 32);
         }

         ☃xxx.setDisplayName(☃xxxx);
         if (☃x.hasKey("TeamColor", 8)) {
            ☃xxx.setColor(TextFormatting.getValueByName(☃x.getString("TeamColor")));
         }

         ☃xxx.setPrefix(☃x.getString("Prefix"));
         ☃xxx.setSuffix(☃x.getString("Suffix"));
         if (☃x.hasKey("AllowFriendlyFire", 99)) {
            ☃xxx.setAllowFriendlyFire(☃x.getBoolean("AllowFriendlyFire"));
         }

         if (☃x.hasKey("SeeFriendlyInvisibles", 99)) {
            ☃xxx.setSeeFriendlyInvisiblesEnabled(☃x.getBoolean("SeeFriendlyInvisibles"));
         }

         if (☃x.hasKey("NameTagVisibility", 8)) {
            Team.EnumVisible ☃xxxxx = Team.EnumVisible.getByName(☃x.getString("NameTagVisibility"));
            if (☃xxxxx != null) {
               ☃xxx.setNameTagVisibility(☃xxxxx);
            }
         }

         if (☃x.hasKey("DeathMessageVisibility", 8)) {
            Team.EnumVisible ☃xxxxx = Team.EnumVisible.getByName(☃x.getString("DeathMessageVisibility"));
            if (☃xxxxx != null) {
               ☃xxx.setDeathMessageVisibility(☃xxxxx);
            }
         }

         if (☃x.hasKey("CollisionRule", 8)) {
            Team.CollisionRule ☃xxxxx = Team.CollisionRule.getByName(☃x.getString("CollisionRule"));
            if (☃xxxxx != null) {
               ☃xxx.setCollisionRule(☃xxxxx);
            }
         }

         this.loadTeamPlayers(☃xxx, ☃x.getTagList("Players", 8));
      }
   }

   protected void loadTeamPlayers(ScorePlayerTeam var1, NBTTagList var2) {
      for (int ☃ = 0; ☃ < ☃.tagCount(); ☃++) {
         this.scoreboard.addPlayerToTeam(☃.getStringTagAt(☃), ☃.getName());
      }
   }

   protected void readDisplayConfig(NBTTagCompound var1) {
      for (int ☃ = 0; ☃ < 19; ☃++) {
         if (☃.hasKey("slot_" + ☃, 8)) {
            String ☃x = ☃.getString("slot_" + ☃);
            ScoreObjective ☃xx = this.scoreboard.getObjective(☃x);
            this.scoreboard.setObjectiveInDisplaySlot(☃, ☃xx);
         }
      }
   }

   protected void readObjectives(NBTTagList var1) {
      for (int ☃ = 0; ☃ < ☃.tagCount(); ☃++) {
         NBTTagCompound ☃x = ☃.getCompoundTagAt(☃);
         IScoreCriteria ☃xx = IScoreCriteria.INSTANCES.get(☃x.getString("CriteriaName"));
         if (☃xx != null) {
            String ☃xxx = ☃x.getString("Name");
            if (☃xxx.length() > 16) {
               ☃xxx = ☃xxx.substring(0, 16);
            }

            ScoreObjective ☃xxxx = this.scoreboard.addScoreObjective(☃xxx, ☃xx);
            ☃xxxx.setDisplayName(☃x.getString("DisplayName"));
            ☃xxxx.setRenderType(IScoreCriteria.EnumRenderType.getByName(☃x.getString("RenderType")));
         }
      }
   }

   protected void readScores(NBTTagList var1) {
      for (int ☃ = 0; ☃ < ☃.tagCount(); ☃++) {
         NBTTagCompound ☃x = ☃.getCompoundTagAt(☃);
         ScoreObjective ☃xx = this.scoreboard.getObjective(☃x.getString("Objective"));
         String ☃xxx = ☃x.getString("Name");
         if (☃xxx.length() > 40) {
            ☃xxx = ☃xxx.substring(0, 40);
         }

         Score ☃xxxx = this.scoreboard.getOrCreateScore(☃xxx, ☃xx);
         ☃xxxx.setScorePoints(☃x.getInteger("Score"));
         if (☃x.hasKey("Locked")) {
            ☃xxxx.setLocked(☃x.getBoolean("Locked"));
         }
      }
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      if (this.scoreboard == null) {
         LOGGER.warn("Tried to save scoreboard without having a scoreboard...");
         return ☃;
      } else {
         ☃.setTag("Objectives", this.objectivesToNbt());
         ☃.setTag("PlayerScores", this.scoresToNbt());
         ☃.setTag("Teams", this.teamsToNbt());
         this.fillInDisplaySlots(☃);
         return ☃;
      }
   }

   protected NBTTagList teamsToNbt() {
      NBTTagList ☃ = new NBTTagList();

      for (ScorePlayerTeam ☃x : this.scoreboard.getTeams()) {
         NBTTagCompound ☃xx = new NBTTagCompound();
         ☃xx.setString("Name", ☃x.getName());
         ☃xx.setString("DisplayName", ☃x.getDisplayName());
         if (☃x.getColor().getColorIndex() >= 0) {
            ☃xx.setString("TeamColor", ☃x.getColor().getFriendlyName());
         }

         ☃xx.setString("Prefix", ☃x.getPrefix());
         ☃xx.setString("Suffix", ☃x.getSuffix());
         ☃xx.setBoolean("AllowFriendlyFire", ☃x.getAllowFriendlyFire());
         ☃xx.setBoolean("SeeFriendlyInvisibles", ☃x.getSeeFriendlyInvisiblesEnabled());
         ☃xx.setString("NameTagVisibility", ☃x.getNameTagVisibility().internalName);
         ☃xx.setString("DeathMessageVisibility", ☃x.getDeathMessageVisibility().internalName);
         ☃xx.setString("CollisionRule", ☃x.getCollisionRule().name);
         NBTTagList ☃xxx = new NBTTagList();

         for (String ☃xxxx : ☃x.getMembershipCollection()) {
            ☃xxx.appendTag(new NBTTagString(☃xxxx));
         }

         ☃xx.setTag("Players", ☃xxx);
         ☃.appendTag(☃xx);
      }

      return ☃;
   }

   protected void fillInDisplaySlots(NBTTagCompound var1) {
      NBTTagCompound ☃ = new NBTTagCompound();
      boolean ☃x = false;

      for (int ☃xx = 0; ☃xx < 19; ☃xx++) {
         ScoreObjective ☃xxx = this.scoreboard.getObjectiveInDisplaySlot(☃xx);
         if (☃xxx != null) {
            ☃.setString("slot_" + ☃xx, ☃xxx.getName());
            ☃x = true;
         }
      }

      if (☃x) {
         ☃.setTag("DisplaySlots", ☃);
      }
   }

   protected NBTTagList objectivesToNbt() {
      NBTTagList ☃ = new NBTTagList();

      for (ScoreObjective ☃x : this.scoreboard.getScoreObjectives()) {
         if (☃x.getCriteria() != null) {
            NBTTagCompound ☃xx = new NBTTagCompound();
            ☃xx.setString("Name", ☃x.getName());
            ☃xx.setString("CriteriaName", ☃x.getCriteria().getName());
            ☃xx.setString("DisplayName", ☃x.getDisplayName());
            ☃xx.setString("RenderType", ☃x.getRenderType().getRenderType());
            ☃.appendTag(☃xx);
         }
      }

      return ☃;
   }

   protected NBTTagList scoresToNbt() {
      NBTTagList ☃ = new NBTTagList();

      for (Score ☃x : this.scoreboard.getScores()) {
         if (☃x.getObjective() != null) {
            NBTTagCompound ☃xx = new NBTTagCompound();
            ☃xx.setString("Name", ☃x.getPlayerName());
            ☃xx.setString("Objective", ☃x.getObjective().getName());
            ☃xx.setInteger("Score", ☃x.getScorePoints());
            ☃xx.setBoolean("Locked", ☃x.isLocked());
            ☃.appendTag(☃xx);
         }
      }

      return ☃;
   }
}
