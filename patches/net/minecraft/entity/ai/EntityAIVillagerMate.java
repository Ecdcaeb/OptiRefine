package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityAIVillagerMate extends EntityAIBase {
   private final EntityVillager villager;
   private EntityVillager mate;
   private final World world;
   private int matingTimeout;
   Village village;

   public EntityAIVillagerMate(EntityVillager var1) {
      this.villager = ☃;
      this.world = ☃.world;
      this.setMutexBits(3);
   }

   @Override
   public boolean shouldExecute() {
      if (this.villager.getGrowingAge() != 0) {
         return false;
      } else if (this.villager.getRNG().nextInt(500) != 0) {
         return false;
      } else {
         this.village = this.world.getVillageCollection().getNearestVillage(new BlockPos(this.villager), 0);
         if (this.village == null) {
            return false;
         } else if (this.checkSufficientDoorsPresentForNewVillager() && this.villager.getIsWillingToMate(true)) {
            Entity ☃ = this.world.findNearestEntityWithinAABB(EntityVillager.class, this.villager.getEntityBoundingBox().grow(8.0, 3.0, 8.0), this.villager);
            if (☃ == null) {
               return false;
            } else {
               this.mate = (EntityVillager)☃;
               return this.mate.getGrowingAge() == 0 && this.mate.getIsWillingToMate(true);
            }
         } else {
            return false;
         }
      }
   }

   @Override
   public void startExecuting() {
      this.matingTimeout = 300;
      this.villager.setMating(true);
   }

   @Override
   public void resetTask() {
      this.village = null;
      this.mate = null;
      this.villager.setMating(false);
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.matingTimeout >= 0
         && this.checkSufficientDoorsPresentForNewVillager()
         && this.villager.getGrowingAge() == 0
         && this.villager.getIsWillingToMate(false);
   }

   @Override
   public void updateTask() {
      this.matingTimeout--;
      this.villager.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
      if (this.villager.getDistanceSq(this.mate) > 2.25) {
         this.villager.getNavigator().tryMoveToEntityLiving(this.mate, 0.25);
      } else if (this.matingTimeout == 0 && this.mate.isMating()) {
         this.giveBirth();
      }

      if (this.villager.getRNG().nextInt(35) == 0) {
         this.world.setEntityState(this.villager, (byte)12);
      }
   }

   private boolean checkSufficientDoorsPresentForNewVillager() {
      if (!this.village.isMatingSeason()) {
         return false;
      } else {
         int ☃ = (int)(this.village.getNumVillageDoors() * 0.35);
         return this.village.getNumVillagers() < ☃;
      }
   }

   private void giveBirth() {
      EntityVillager ☃ = this.villager.createChild(this.mate);
      this.mate.setGrowingAge(6000);
      this.villager.setGrowingAge(6000);
      this.mate.setIsWillingToMate(false);
      this.villager.setIsWillingToMate(false);
      ☃.setGrowingAge(-24000);
      ☃.setLocationAndAngles(this.villager.posX, this.villager.posY, this.villager.posZ, 0.0F, 0.0F);
      this.world.spawnEntity(☃);
      this.world.setEntityState(☃, (byte)12);
   }
}
