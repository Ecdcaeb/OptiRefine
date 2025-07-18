package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;

public class EntityAIDefendVillage extends EntityAITarget {
   EntityIronGolem irongolem;
   EntityLivingBase villageAgressorTarget;

   public EntityAIDefendVillage(EntityIronGolem var1) {
      super(☃, false, true);
      this.irongolem = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      Village ☃ = this.irongolem.getVillage();
      if (☃ == null) {
         return false;
      } else {
         this.villageAgressorTarget = ☃.findNearestVillageAggressor(this.irongolem);
         if (this.villageAgressorTarget instanceof EntityCreeper) {
            return false;
         } else if (this.isSuitableTarget(this.villageAgressorTarget, false)) {
            return true;
         } else if (this.taskOwner.getRNG().nextInt(20) == 0) {
            this.villageAgressorTarget = ☃.getNearestTargetPlayer(this.irongolem);
            return this.isSuitableTarget(this.villageAgressorTarget, false);
         } else {
            return false;
         }
      }
   }

   @Override
   public void startExecuting() {
      this.irongolem.setAttackTarget(this.villageAgressorTarget);
      super.startExecuting();
   }
}
