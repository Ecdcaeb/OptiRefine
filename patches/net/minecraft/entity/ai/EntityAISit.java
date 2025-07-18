package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase {
   private final EntityTameable tameable;
   private boolean isSitting;

   public EntityAISit(EntityTameable var1) {
      this.tameable = ☃;
      this.setMutexBits(5);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.tameable.isTamed()) {
         return false;
      } else if (this.tameable.isInWater()) {
         return false;
      } else if (!this.tameable.onGround) {
         return false;
      } else {
         EntityLivingBase ☃ = this.tameable.getOwner();
         if (☃ == null) {
            return true;
         } else {
            return this.tameable.getDistanceSq(☃) < 144.0 && ☃.getRevengeTarget() != null ? false : this.isSitting;
         }
      }
   }

   @Override
   public void startExecuting() {
      this.tameable.getNavigator().clearPath();
      this.tameable.setSitting(true);
   }

   @Override
   public void resetTask() {
      this.tameable.setSitting(false);
   }

   public void setSitting(boolean var1) {
      this.isSitting = ☃;
   }
}
