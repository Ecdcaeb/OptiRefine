package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveTowardsTarget extends EntityAIBase {
   private final EntityCreature creature;
   private EntityLivingBase targetEntity;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private final double speed;
   private final float maxTargetDistance;

   public EntityAIMoveTowardsTarget(EntityCreature var1, double var2, float var4) {
      this.creature = ☃;
      this.speed = ☃;
      this.maxTargetDistance = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      this.targetEntity = this.creature.getAttackTarget();
      if (this.targetEntity == null) {
         return false;
      } else if (this.targetEntity.getDistanceSq(this.creature) > this.maxTargetDistance * this.maxTargetDistance) {
         return false;
      } else {
         Vec3d ☃ = RandomPositionGenerator.findRandomTargetBlockTowards(
            this.creature, 16, 7, new Vec3d(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ)
         );
         if (☃ == null) {
            return false;
         } else {
            this.movePosX = ☃.x;
            this.movePosY = ☃.y;
            this.movePosZ = ☃.z;
            return true;
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.creature.getNavigator().noPath()
         && this.targetEntity.isEntityAlive()
         && this.targetEntity.getDistanceSq(this.creature) < this.maxTargetDistance * this.maxTargetDistance;
   }

   @Override
   public void resetTask() {
      this.targetEntity = null;
   }

   @Override
   public void startExecuting() {
      this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
   }
}
