package net.minecraft.stats;

import net.minecraft.util.text.ITextComponent;

public class StatBasic extends StatBase {
   public StatBasic(String var1, ITextComponent var2, IStatType var3) {
      super(☃, ☃, ☃);
   }

   public StatBasic(String var1, ITextComponent var2) {
      super(☃, ☃);
   }

   @Override
   public StatBase registerStat() {
      super.registerStat();
      StatList.BASIC_STATS.add(this);
      return this;
   }
}
