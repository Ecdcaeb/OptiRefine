package net.minecraft.scoreboard;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.util.text.TextFormatting;

public class ScorePlayerTeam extends Team {
   private final Scoreboard scoreboard;
   private final String name;
   private final Set<String> membershipSet = Sets.newHashSet();
   private String displayName;
   private String prefix = "";
   private String suffix = "";
   private boolean allowFriendlyFire = true;
   private boolean canSeeFriendlyInvisibles = true;
   private Team.EnumVisible nameTagVisibility = Team.EnumVisible.ALWAYS;
   private Team.EnumVisible deathMessageVisibility = Team.EnumVisible.ALWAYS;
   private TextFormatting color = TextFormatting.RESET;
   private Team.CollisionRule collisionRule = Team.CollisionRule.ALWAYS;

   public ScorePlayerTeam(Scoreboard var1, String var2) {
      this.scoreboard = ☃;
      this.name = ☃;
      this.displayName = ☃;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String var1) {
      if (☃ == null) {
         throw new IllegalArgumentException("Name cannot be null");
      } else {
         this.displayName = ☃;
         this.scoreboard.broadcastTeamInfoUpdate(this);
      }
   }

   @Override
   public Collection<String> getMembershipCollection() {
      return this.membershipSet;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String var1) {
      if (☃ == null) {
         throw new IllegalArgumentException("Prefix cannot be null");
      } else {
         this.prefix = ☃;
         this.scoreboard.broadcastTeamInfoUpdate(this);
      }
   }

   public String getSuffix() {
      return this.suffix;
   }

   public void setSuffix(String var1) {
      this.suffix = ☃;
      this.scoreboard.broadcastTeamInfoUpdate(this);
   }

   @Override
   public String formatString(String var1) {
      return this.getPrefix() + ☃ + this.getSuffix();
   }

   public static String formatPlayerName(@Nullable Team var0, String var1) {
      return ☃ == null ? ☃ : ☃.formatString(☃);
   }

   @Override
   public boolean getAllowFriendlyFire() {
      return this.allowFriendlyFire;
   }

   public void setAllowFriendlyFire(boolean var1) {
      this.allowFriendlyFire = ☃;
      this.scoreboard.broadcastTeamInfoUpdate(this);
   }

   @Override
   public boolean getSeeFriendlyInvisiblesEnabled() {
      return this.canSeeFriendlyInvisibles;
   }

   public void setSeeFriendlyInvisiblesEnabled(boolean var1) {
      this.canSeeFriendlyInvisibles = ☃;
      this.scoreboard.broadcastTeamInfoUpdate(this);
   }

   @Override
   public Team.EnumVisible getNameTagVisibility() {
      return this.nameTagVisibility;
   }

   @Override
   public Team.EnumVisible getDeathMessageVisibility() {
      return this.deathMessageVisibility;
   }

   public void setNameTagVisibility(Team.EnumVisible var1) {
      this.nameTagVisibility = ☃;
      this.scoreboard.broadcastTeamInfoUpdate(this);
   }

   public void setDeathMessageVisibility(Team.EnumVisible var1) {
      this.deathMessageVisibility = ☃;
      this.scoreboard.broadcastTeamInfoUpdate(this);
   }

   @Override
   public Team.CollisionRule getCollisionRule() {
      return this.collisionRule;
   }

   public void setCollisionRule(Team.CollisionRule var1) {
      this.collisionRule = ☃;
      this.scoreboard.broadcastTeamInfoUpdate(this);
   }

   public int getFriendlyFlags() {
      int ☃ = 0;
      if (this.getAllowFriendlyFire()) {
         ☃ |= 1;
      }

      if (this.getSeeFriendlyInvisiblesEnabled()) {
         ☃ |= 2;
      }

      return ☃;
   }

   public void setFriendlyFlags(int var1) {
      this.setAllowFriendlyFire((☃ & 1) > 0);
      this.setSeeFriendlyInvisiblesEnabled((☃ & 2) > 0);
   }

   public void setColor(TextFormatting var1) {
      this.color = ☃;
   }

   @Override
   public TextFormatting getColor() {
      return this.color;
   }
}
