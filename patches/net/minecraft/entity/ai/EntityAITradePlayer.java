package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITradePlayer extends EntityAIBase {
   private final EntityVillager villager;

   public EntityAITradePlayer(EntityVillager var1) {
      this.villager = ☃;
      this.setMutexBits(5);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.villager.isEntityAlive()) {
         return false;
      } else if (this.villager.isInWater()) {
         return false;
      } else if (!this.villager.onGround) {
         return false;
      } else if (this.villager.velocityChanged) {
         return false;
      } else {
         EntityPlayer ☃ = this.villager.getCustomer();
         if (☃ == null) {
            return false;
         } else {
            return this.villager.getDistanceSq(☃) > 16.0 ? false : ☃.openContainer != null;
         }
      }
   }

   @Override
   public void startExecuting() {
      this.villager.getNavigator().clearPath();
   }

   @Override
   public void resetTask() {
      this.villager.setCustomer(null);
   }
}
