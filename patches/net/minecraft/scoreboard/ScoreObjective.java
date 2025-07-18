package net.minecraft.scoreboard;

public class ScoreObjective {
   private final Scoreboard scoreboard;
   private final String name;
   private final IScoreCriteria objectiveCriteria;
   private IScoreCriteria.EnumRenderType renderType;
   private String displayName;

   public ScoreObjective(Scoreboard var1, String var2, IScoreCriteria var3) {
      this.scoreboard = ☃;
      this.name = ☃;
      this.objectiveCriteria = ☃;
      this.displayName = ☃;
      this.renderType = ☃.getRenderType();
   }

   public Scoreboard getScoreboard() {
      return this.scoreboard;
   }

   public String getName() {
      return this.name;
   }

   public IScoreCriteria getCriteria() {
      return this.objectiveCriteria;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String var1) {
      this.displayName = ☃;
      this.scoreboard.onObjectiveDisplayNameChanged(this);
   }

   public IScoreCriteria.EnumRenderType getRenderType() {
      return this.renderType;
   }

   public void setRenderType(IScoreCriteria.EnumRenderType var1) {
      this.renderType = ☃;
      this.scoreboard.onObjectiveDisplayNameChanged(this);
   }
}
