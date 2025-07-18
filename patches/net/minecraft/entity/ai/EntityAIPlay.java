package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.Vec3d;

public class EntityAIPlay extends EntityAIBase {
   private final EntityVillager villager;
   private EntityLivingBase targetVillager;
   private final double speed;
   private int playTime;

   public EntityAIPlay(EntityVillager var1, double var2) {
      this.villager = ☃;
      this.speed = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.villager.getGrowingAge() >= 0) {
         return false;
      } else if (this.villager.getRNG().nextInt(400) != 0) {
         return false;
      } else {
         List<EntityVillager> ☃ = this.villager.world.getEntitiesWithinAABB(EntityVillager.class, this.villager.getEntityBoundingBox().grow(6.0, 3.0, 6.0));
         double ☃x = Double.MAX_VALUE;

         for (EntityVillager ☃xx : ☃) {
            if (☃xx != this.villager && !☃xx.isPlaying() && ☃xx.getGrowingAge() < 0) {
               double ☃xxx = ☃xx.getDistanceSq(this.villager);
               if (!(☃xxx > ☃x)) {
                  ☃x = ☃xxx;
                  this.targetVillager = ☃xx;
               }
            }
         }

         if (this.targetVillager == null) {
            Vec3d ☃xxx = RandomPositionGenerator.findRandomTarget(this.villager, 16, 3);
            if (☃xxx == null) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.playTime > 0;
   }

   @Override
   public void startExecuting() {
      if (this.targetVillager != null) {
         this.villager.setPlaying(true);
      }

      this.playTime = 1000;
   }

   @Override
   public void resetTask() {
      this.villager.setPlaying(false);
      this.targetVillager = null;
   }

   @Override
   public void updateTask() {
      this.playTime--;
      if (this.targetVillager != null) {
         if (this.villager.getDistanceSq(this.targetVillager) > 4.0) {
            this.villager.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.speed);
         }
      } else if (this.villager.getNavigator().noPath()) {
         Vec3d ☃ = RandomPositionGenerator.findRandomTarget(this.villager, 16, 3);
         if (☃ == null) {
            return;
         }

         this.villager.getNavigator().tryMoveToXYZ(☃.x, ☃.y, ☃.z, this.speed);
      }
   }
}
