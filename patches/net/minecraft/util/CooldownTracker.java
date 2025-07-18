package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

public class CooldownTracker {
   private final Map<Item, CooldownTracker.Cooldown> cooldowns = Maps.newHashMap();
   private int ticks;

   public boolean hasCooldown(Item var1) {
      return this.getCooldown(☃, 0.0F) > 0.0F;
   }

   public float getCooldown(Item var1, float var2) {
      CooldownTracker.Cooldown ☃ = this.cooldowns.get(☃);
      if (☃ != null) {
         float ☃x = ☃.expireTicks - ☃.createTicks;
         float ☃xx = ☃.expireTicks - (this.ticks + ☃);
         return MathHelper.clamp(☃xx / ☃x, 0.0F, 1.0F);
      } else {
         return 0.0F;
      }
   }

   public void tick() {
      this.ticks++;
      if (!this.cooldowns.isEmpty()) {
         Iterator<Entry<Item, CooldownTracker.Cooldown>> ☃ = this.cooldowns.entrySet().iterator();

         while (☃.hasNext()) {
            Entry<Item, CooldownTracker.Cooldown> ☃x = ☃.next();
            if (☃x.getValue().expireTicks <= this.ticks) {
               ☃.remove();
               this.notifyOnRemove(☃x.getKey());
            }
         }
      }
   }

   public void setCooldown(Item var1, int var2) {
      this.cooldowns.put(☃, new CooldownTracker.Cooldown(this.ticks, this.ticks + ☃));
      this.notifyOnSet(☃, ☃);
   }

   public void removeCooldown(Item var1) {
      this.cooldowns.remove(☃);
      this.notifyOnRemove(☃);
   }

   protected void notifyOnSet(Item var1, int var2) {
   }

   protected void notifyOnRemove(Item var1) {
   }

   class Cooldown {
      final int createTicks;
      final int expireTicks;

      private Cooldown(int var2, int var3) {
         this.createTicks = ☃;
         this.expireTicks = ☃;
      }
   }
}
