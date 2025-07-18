package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtTarget extends EntityAITarget {
   EntityTameable tameable;
   EntityLivingBase attacker;
   private int timestamp;

   public EntityAIOwnerHurtTarget(EntityTameable var1) {
      super(☃, false);
      this.tameable = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.tameable.isTamed()) {
         return false;
      } else {
         EntityLivingBase ☃ = this.tameable.getOwner();
         if (☃ == null) {
            return false;
         } else {
            this.attacker = ☃.getLastAttackedEntity();
            int ☃x = ☃.getLastAttackedEntityTime();
            return ☃x != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, ☃);
         }
      }
   }

   @Override
   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.attacker);
      EntityLivingBase ☃ = this.tameable.getOwner();
      if (☃ != null) {
         this.timestamp = ☃.getLastAttackedEntityTime();
      }

      super.startExecuting();
   }
}
