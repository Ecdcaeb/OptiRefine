package net.minecraft.stats;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreCriteriaStat;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class StatBase {
   public final String statId;
   private final ITextComponent statName;
   public boolean isIndependent;
   private final IStatType formatter;
   private final IScoreCriteria objectiveCriteria;
   private Class<? extends IJsonSerializable> serializableClazz;
   private static final NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
   public static IStatType simpleStatType = new IStatType() {
      @Override
      public String format(int var1) {
         return StatBase.numberFormat.format((long)☃);
      }
   };
   private static final DecimalFormat decimalFormat = new DecimalFormat("########0.00");
   public static IStatType timeStatType = new IStatType() {
      @Override
      public String format(int var1) {
         double ☃ = ☃ / 20.0;
         double ☃x = ☃ / 60.0;
         double ☃xx = ☃x / 60.0;
         double ☃xxx = ☃xx / 24.0;
         double ☃xxxx = ☃xxx / 365.0;
         if (☃xxxx > 0.5) {
            return StatBase.decimalFormat.format(☃xxxx) + " y";
         } else if (☃xxx > 0.5) {
            return StatBase.decimalFormat.format(☃xxx) + " d";
         } else if (☃xx > 0.5) {
            return StatBase.decimalFormat.format(☃xx) + " h";
         } else {
            return ☃x > 0.5 ? StatBase.decimalFormat.format(☃x) + " m" : ☃ + " s";
         }
      }
   };
   public static IStatType distanceStatType = new IStatType() {
      @Override
      public String format(int var1) {
         double ☃ = ☃ / 100.0;
         double ☃x = ☃ / 1000.0;
         if (☃x > 0.5) {
            return StatBase.decimalFormat.format(☃x) + " km";
         } else {
            return ☃ > 0.5 ? StatBase.decimalFormat.format(☃) + " m" : ☃ + " cm";
         }
      }
   };
   public static IStatType divideByTen = new IStatType() {
      @Override
      public String format(int var1) {
         return StatBase.decimalFormat.format(☃ * 0.1);
      }
   };

   public StatBase(String var1, ITextComponent var2, IStatType var3) {
      this.statId = ☃;
      this.statName = ☃;
      this.formatter = ☃;
      this.objectiveCriteria = new ScoreCriteriaStat(this);
      IScoreCriteria.INSTANCES.put(this.objectiveCriteria.getName(), this.objectiveCriteria);
   }

   public StatBase(String var1, ITextComponent var2) {
      this(☃, ☃, simpleStatType);
   }

   public StatBase initIndependentStat() {
      this.isIndependent = true;
      return this;
   }

   public StatBase registerStat() {
      if (StatList.ID_TO_STAT_MAP.containsKey(this.statId)) {
         throw new RuntimeException(
            "Duplicate stat id: \"" + StatList.ID_TO_STAT_MAP.get(this.statId).statName + "\" and \"" + this.statName + "\" at id " + this.statId
         );
      } else {
         StatList.ALL_STATS.add(this);
         StatList.ID_TO_STAT_MAP.put(this.statId, this);
         return this;
      }
   }

   public String format(int var1) {
      return this.formatter.format(☃);
   }

   public ITextComponent getStatName() {
      ITextComponent ☃ = this.statName.createCopy();
      ☃.getStyle().setColor(TextFormatting.GRAY);
      return ☃;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ != null && this.getClass() == ☃.getClass()) {
         StatBase ☃ = (StatBase)☃;
         return this.statId.equals(☃.statId);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.statId.hashCode();
   }

   @Override
   public String toString() {
      return "Stat{id="
         + this.statId
         + ", nameId="
         + this.statName
         + ", awardLocallyOnly="
         + this.isIndependent
         + ", formatter="
         + this.formatter
         + ", objectiveCriteria="
         + this.objectiveCriteria
         + '}';
   }

   public IScoreCriteria getCriteria() {
      return this.objectiveCriteria;
   }

   public Class<? extends IJsonSerializable> getSerializableClazz() {
      return this.serializableClazz;
   }
}
