package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveTowardsRestriction extends EntityAIBase {
   private final EntityCreature creature;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private final double movementSpeed;

   public EntityAIMoveTowardsRestriction(EntityCreature var1, double var2) {
      this.creature = ☃;
      this.movementSpeed = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.creature.isWithinHomeDistanceCurrentPosition()) {
         return false;
      } else {
         BlockPos ☃ = this.creature.getHomePosition();
         Vec3d ☃x = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature, 16, 7, new Vec3d(☃.getX(), ☃.getY(), ☃.getZ()));
         if (☃x == null) {
            return false;
         } else {
            this.movePosX = ☃x.x;
            this.movePosY = ☃x.y;
            this.movePosZ = ☃x.z;
            return true;
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.creature.getNavigator().noPath();
   }

   @Override
   public void startExecuting() {
      this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
   }
}
