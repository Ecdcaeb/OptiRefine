package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityAIOcelotAttack extends EntityAIBase {
   World world;
   EntityLiving entity;
   EntityLivingBase target;
   int attackCountdown;

   public EntityAIOcelotAttack(EntityLiving var1) {
      this.entity = ☃;
      this.world = ☃.world;
      this.setMutexBits(3);
   }

   @Override
   public boolean shouldExecute() {
      EntityLivingBase ☃ = this.entity.getAttackTarget();
      if (☃ == null) {
         return false;
      } else {
         this.target = ☃;
         return true;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      if (!this.target.isEntityAlive()) {
         return false;
      } else {
         return this.entity.getDistanceSq(this.target) > 225.0 ? false : !this.entity.getNavigator().noPath() || this.shouldExecute();
      }
   }

   @Override
   public void resetTask() {
      this.target = null;
      this.entity.getNavigator().clearPath();
   }

   @Override
   public void updateTask() {
      this.entity.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
      double ☃ = this.entity.width * 2.0F * (this.entity.width * 2.0F);
      double ☃x = this.entity.getDistanceSq(this.target.posX, this.target.getEntityBoundingBox().minY, this.target.posZ);
      double ☃xx = 0.8;
      if (☃x > ☃ && ☃x < 16.0) {
         ☃xx = 1.33;
      } else if (☃x < 225.0) {
         ☃xx = 0.6;
      }

      this.entity.getNavigator().tryMoveToEntityLiving(this.target, ☃xx);
      this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
      if (!(☃x > ☃)) {
         if (this.attackCountdown <= 0) {
            this.attackCountdown = 20;
            this.entity.attackEntityAsMob(this.target);
         }
      }
   }
}
