package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;

public abstract class PhaseSittingBase extends PhaseBase {
   public PhaseSittingBase(EntityDragon var1) {
      super(☃);
   }

   @Override
   public boolean getIsStationary() {
      return true;
   }

   @Override
   public float getAdjustedDamage(MultiPartEntityPart var1, DamageSource var2, float var3) {
      if (☃.getImmediateSource() instanceof EntityArrow) {
         ☃.getImmediateSource().setFire(1);
         return 0.0F;
      } else {
         return super.getAdjustedDamage(☃, ☃, ☃);
      }
   }
}
