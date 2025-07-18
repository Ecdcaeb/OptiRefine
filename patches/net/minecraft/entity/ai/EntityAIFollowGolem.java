package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFollowGolem extends EntityAIBase {
   private final EntityVillager villager;
   private EntityIronGolem ironGolem;
   private int takeGolemRoseTick;
   private boolean tookGolemRose;

   public EntityAIFollowGolem(EntityVillager var1) {
      this.villager = ☃;
      this.setMutexBits(3);
   }

   @Override
   public boolean shouldExecute() {
      if (this.villager.getGrowingAge() >= 0) {
         return false;
      } else if (!this.villager.world.isDaytime()) {
         return false;
      } else {
         List<EntityIronGolem> ☃ = this.villager.world.getEntitiesWithinAABB(EntityIronGolem.class, this.villager.getEntityBoundingBox().grow(6.0, 2.0, 6.0));
         if (☃.isEmpty()) {
            return false;
         } else {
            for (EntityIronGolem ☃x : ☃) {
               if (☃x.getHoldRoseTick() > 0) {
                  this.ironGolem = ☃x;
                  break;
               }
            }

            return this.ironGolem != null;
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.ironGolem.getHoldRoseTick() > 0;
   }

   @Override
   public void startExecuting() {
      this.takeGolemRoseTick = this.villager.getRNG().nextInt(320);
      this.tookGolemRose = false;
      this.ironGolem.getNavigator().clearPath();
   }

   @Override
   public void resetTask() {
      this.ironGolem = null;
      this.villager.getNavigator().clearPath();
   }

   @Override
   public void updateTask() {
      this.villager.getLookHelper().setLookPositionWithEntity(this.ironGolem, 30.0F, 30.0F);
      if (this.ironGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
         this.villager.getNavigator().tryMoveToEntityLiving(this.ironGolem, 0.5);
         this.tookGolemRose = true;
      }

      if (this.tookGolemRose && this.villager.getDistanceSq(this.ironGolem) < 4.0) {
         this.ironGolem.setHoldingRose(false);
         this.villager.getNavigator().clearPath();
      }
   }
}
