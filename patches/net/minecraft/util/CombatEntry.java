package net.minecraft.util;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.ITextComponent;

public class CombatEntry {
   private final DamageSource damageSrc;
   private final int time;
   private final float damage;
   private final float health;
   private final String fallSuffix;
   private final float fallDistance;

   public CombatEntry(DamageSource var1, int var2, float var3, float var4, String var5, float var6) {
      this.damageSrc = ☃;
      this.time = ☃;
      this.damage = ☃;
      this.health = ☃;
      this.fallSuffix = ☃;
      this.fallDistance = ☃;
   }

   public DamageSource getDamageSrc() {
      return this.damageSrc;
   }

   public float getDamage() {
      return this.damage;
   }

   public boolean isLivingDamageSrc() {
      return this.damageSrc.getTrueSource() instanceof EntityLivingBase;
   }

   @Nullable
   public String getFallSuffix() {
      return this.fallSuffix;
   }

   @Nullable
   public ITextComponent getDamageSrcDisplayName() {
      return this.getDamageSrc().getTrueSource() == null ? null : this.getDamageSrc().getTrueSource().getDisplayName();
   }

   public float getDamageAmount() {
      return this.damageSrc == DamageSource.OUT_OF_WORLD ? Float.MAX_VALUE : this.fallDistance;
   }
}
