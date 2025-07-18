package net.minecraft.stats;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatisticsManager {
   protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newConcurrentMap();

   public void increaseStat(EntityPlayer var1, StatBase var2, int var3) {
      this.unlockAchievement(☃, ☃, this.readStat(☃) + ☃);
   }

   public void unlockAchievement(EntityPlayer var1, StatBase var2, int var3) {
      TupleIntJsonSerializable ☃ = this.statsData.get(☃);
      if (☃ == null) {
         ☃ = new TupleIntJsonSerializable();
         this.statsData.put(☃, ☃);
      }

      ☃.setIntegerValue(☃);
   }

   public int readStat(StatBase var1) {
      TupleIntJsonSerializable ☃ = this.statsData.get(☃);
      return ☃ == null ? 0 : ☃.getIntegerValue();
   }
}
