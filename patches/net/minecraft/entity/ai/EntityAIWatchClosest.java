package net.minecraft.entity.ai;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

public class EntityAIWatchClosest extends EntityAIBase {
   protected EntityLiving entity;
   protected Entity closestEntity;
   protected float maxDistance;
   private int lookTime;
   private final float chance;
   protected Class<? extends Entity> watchedClass;

   public EntityAIWatchClosest(EntityLiving var1, Class<? extends Entity> var2, float var3) {
      this.entity = ☃;
      this.watchedClass = ☃;
      this.maxDistance = ☃;
      this.chance = 0.02F;
      this.setMutexBits(2);
   }

   public EntityAIWatchClosest(EntityLiving var1, Class<? extends Entity> var2, float var3, float var4) {
      this.entity = ☃;
      this.watchedClass = ☃;
      this.maxDistance = ☃;
      this.chance = ☃;
      this.setMutexBits(2);
   }

   @Override
   public boolean shouldExecute() {
      if (this.entity.getRNG().nextFloat() >= this.chance) {
         return false;
      } else {
         if (this.entity.getAttackTarget() != null) {
            this.closestEntity = this.entity.getAttackTarget();
         }

         if (this.watchedClass == EntityPlayer.class) {
            this.closestEntity = this.entity
               .world
               .getClosestPlayer(
                  this.entity.posX,
                  this.entity.posY,
                  this.entity.posZ,
                  this.maxDistance,
                  Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.notRiding(this.entity))
               );
         } else {
            this.closestEntity = this.entity
               .world
               .findNearestEntityWithinAABB(this.watchedClass, this.entity.getEntityBoundingBox().grow(this.maxDistance, 3.0, this.maxDistance), this.entity);
         }

         return this.closestEntity != null;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      if (!this.closestEntity.isEntityAlive()) {
         return false;
      } else {
         return this.entity.getDistanceSq(this.closestEntity) > this.maxDistance * this.maxDistance ? false : this.lookTime > 0;
      }
   }

   @Override
   public void startExecuting() {
      this.lookTime = 40 + this.entity.getRNG().nextInt(40);
   }

   @Override
   public void resetTask() {
      this.closestEntity = null;
   }

   @Override
   public void updateTask() {
      this.entity
         .getLookHelper()
         .setLookPosition(
            this.closestEntity.posX,
            this.closestEntity.posY + this.closestEntity.getEyeHeight(),
            this.closestEntity.posZ,
            this.entity.getHorizontalFaceSpeed(),
            this.entity.getVerticalFaceSpeed()
         );
      this.lookTime--;
   }
}
