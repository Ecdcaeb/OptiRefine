package net.minecraft.scoreboard;

import java.util.Comparator;

public class Score {
   public static final Comparator<Score> SCORE_COMPARATOR = new Comparator<Score>() {
      public int compare(Score var1, Score var2) {
         if (☃.getScorePoints() > ☃.getScorePoints()) {
            return 1;
         } else {
            return ☃.getScorePoints() < ☃.getScorePoints() ? -1 : ☃.getPlayerName().compareToIgnoreCase(☃.getPlayerName());
         }
      }
   };
   private final Scoreboard scoreboard;
   private final ScoreObjective objective;
   private final String scorePlayerName;
   private int scorePoints;
   private boolean locked;
   private boolean forceUpdate;

   public Score(Scoreboard var1, ScoreObjective var2, String var3) {
      this.scoreboard = ☃;
      this.objective = ☃;
      this.scorePlayerName = ☃;
      this.forceUpdate = true;
   }

   public void increaseScore(int var1) {
      if (this.objective.getCriteria().isReadOnly()) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         this.setScorePoints(this.getScorePoints() + ☃);
      }
   }

   public void decreaseScore(int var1) {
      if (this.objective.getCriteria().isReadOnly()) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         this.setScorePoints(this.getScorePoints() - ☃);
      }
   }

   public void incrementScore() {
      if (this.objective.getCriteria().isReadOnly()) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         this.increaseScore(1);
      }
   }

   public int getScorePoints() {
      return this.scorePoints;
   }

   public void setScorePoints(int var1) {
      int ☃ = this.scorePoints;
      this.scorePoints = ☃;
      if (☃ != ☃ || this.forceUpdate) {
         this.forceUpdate = false;
         this.getScoreScoreboard().onScoreUpdated(this);
      }
   }

   public ScoreObjective getObjective() {
      return this.objective;
   }

   public String getPlayerName() {
      return this.scorePlayerName;
   }

   public Scoreboard getScoreScoreboard() {
      return this.scoreboard;
   }

   public boolean isLocked() {
      return this.locked;
   }

   public void setLocked(boolean var1) {
      this.locked = ☃;
   }
}
