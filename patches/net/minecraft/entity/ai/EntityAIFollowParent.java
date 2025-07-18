package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIFollowParent extends EntityAIBase {
   EntityAnimal childAnimal;
   EntityAnimal parentAnimal;
   double moveSpeed;
   private int delayCounter;

   public EntityAIFollowParent(EntityAnimal var1, double var2) {
      this.childAnimal = ☃;
      this.moveSpeed = ☃;
   }

   @Override
   public boolean shouldExecute() {
      if (this.childAnimal.getGrowingAge() >= 0) {
         return false;
      } else {
         List<EntityAnimal> ☃ = this.childAnimal
            .world
            .getEntitiesWithinAABB((Class<? extends EntityAnimal>)this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().grow(8.0, 4.0, 8.0));
         EntityAnimal ☃x = null;
         double ☃xx = Double.MAX_VALUE;

         for (EntityAnimal ☃xxx : ☃) {
            if (☃xxx.getGrowingAge() >= 0) {
               double ☃xxxx = this.childAnimal.getDistanceSq(☃xxx);
               if (!(☃xxxx > ☃xx)) {
                  ☃xx = ☃xxxx;
                  ☃x = ☃xxx;
               }
            }
         }

         if (☃x == null) {
            return false;
         } else if (☃xx < 9.0) {
            return false;
         } else {
            this.parentAnimal = ☃x;
            return true;
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      if (this.childAnimal.getGrowingAge() >= 0) {
         return false;
      } else if (!this.parentAnimal.isEntityAlive()) {
         return false;
      } else {
         double ☃ = this.childAnimal.getDistanceSq(this.parentAnimal);
         return !(☃ < 9.0) && !(☃ > 256.0);
      }
   }

   @Override
   public void startExecuting() {
      this.delayCounter = 0;
   }

   @Override
   public void resetTask() {
      this.parentAnimal = null;
   }

   @Override
   public void updateTask() {
      if (--this.delayCounter <= 0) {
         this.delayCounter = 10;
         this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
      }
   }
}
