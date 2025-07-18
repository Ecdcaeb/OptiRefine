package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class EntityAILeapAtTarget extends EntityAIBase {
   EntityLiving leaper;
   EntityLivingBase leapTarget;
   float leapMotionY;

   public EntityAILeapAtTarget(EntityLiving var1, float var2) {
      this.leaper = ☃;
      this.leapMotionY = ☃;
      this.setMutexBits(5);
   }

   @Override
   public boolean shouldExecute() {
      this.leapTarget = this.leaper.getAttackTarget();
      if (this.leapTarget == null) {
         return false;
      } else {
         double ☃ = this.leaper.getDistanceSq(this.leapTarget);
         if (☃ < 4.0 || ☃ > 16.0) {
            return false;
         } else {
            return !this.leaper.onGround ? false : this.leaper.getRNG().nextInt(5) == 0;
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.leaper.onGround;
   }

   @Override
   public void startExecuting() {
      double ☃ = this.leapTarget.posX - this.leaper.posX;
      double ☃x = this.leapTarget.posZ - this.leaper.posZ;
      float ☃xx = MathHelper.sqrt(☃ * ☃ + ☃x * ☃x);
      if (☃xx >= 1.0E-4) {
         this.leaper.motionX = this.leaper.motionX + (☃ / ☃xx * 0.5 * 0.8F + this.leaper.motionX * 0.2F);
         this.leaper.motionZ = this.leaper.motionZ + (☃x / ☃xx * 0.5 * 0.8F + this.leaper.motionZ * 0.2F);
      }

      this.leaper.motionY = this.leapMotionY;
   }
}
