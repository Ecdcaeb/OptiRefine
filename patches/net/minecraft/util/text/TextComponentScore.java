package net.minecraft.util.text;

import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public class TextComponentScore extends TextComponentBase {
   private final String name;
   private final String objective;
   private String value = "";

   public TextComponentScore(String var1, String var2) {
      this.name = ☃;
      this.objective = ☃;
   }

   public String getName() {
      return this.name;
   }

   public String getObjective() {
      return this.objective;
   }

   public void setValue(String var1) {
      this.value = ☃;
   }

   @Override
   public String getUnformattedComponentText() {
      return this.value;
   }

   public void resolve(ICommandSender var1) {
      MinecraftServer ☃ = ☃.getServer();
      if (☃ != null && ☃.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value)) {
         Scoreboard ☃x = ☃.getWorld(0).getScoreboard();
         ScoreObjective ☃xx = ☃x.getObjective(this.objective);
         if (☃x.entityHasObjective(this.name, ☃xx)) {
            Score ☃xxx = ☃x.getOrCreateScore(this.name, ☃xx);
            this.setValue(String.format("%d", ☃xxx.getScorePoints()));
         } else {
            this.value = "";
         }
      }
   }

   public TextComponentScore createCopy() {
      TextComponentScore ☃ = new TextComponentScore(this.name, this.objective);
      ☃.setValue(this.value);
      ☃.setStyle(this.getStyle().createShallowCopy());

      for (ITextComponent ☃x : this.getSiblings()) {
         ☃.appendSibling(☃x.createCopy());
      }

      return ☃;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof TextComponentScore)) {
         return false;
      } else {
         TextComponentScore ☃ = (TextComponentScore)☃;
         return this.name.equals(☃.name) && this.objective.equals(☃.objective) && super.equals(☃);
      }
   }

   @Override
   public String toString() {
      return "ScoreComponent{name='"
         + this.name
         + '\''
         + "objective='"
         + this.objective
         + '\''
         + ", siblings="
         + this.siblings
         + ", style="
         + this.getStyle()
         + '}';
   }
}
