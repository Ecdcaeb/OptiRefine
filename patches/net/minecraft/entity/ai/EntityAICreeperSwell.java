package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;

public class EntityAICreeperSwell extends EntityAIBase {
   EntityCreeper swellingCreeper;
   EntityLivingBase creeperAttackTarget;

   public EntityAICreeperSwell(EntityCreeper var1) {
      this.swellingCreeper = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      EntityLivingBase ☃ = this.swellingCreeper.getAttackTarget();
      return this.swellingCreeper.getCreeperState() > 0 || ☃ != null && this.swellingCreeper.getDistanceSq(☃) < 9.0;
   }

   @Override
   public void startExecuting() {
      this.swellingCreeper.getNavigator().clearPath();
      this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
   }

   @Override
   public void resetTask() {
      this.creeperAttackTarget = null;
   }

   @Override
   public void updateTask() {
      if (this.creeperAttackTarget == null) {
         this.swellingCreeper.setCreeperState(-1);
      } else if (this.swellingCreeper.getDistanceSq(this.creeperAttackTarget) > 49.0) {
         this.swellingCreeper.setCreeperState(-1);
      } else if (!this.swellingCreeper.getEntitySenses().canSee(this.creeperAttackTarget)) {
         this.swellingCreeper.setCreeperState(-1);
      } else {
         this.swellingCreeper.setCreeperState(1);
      }
   }
}
