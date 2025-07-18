package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayer extends EntityAIWatchClosest {
   private final EntityVillager villager;

   public EntityAILookAtTradePlayer(EntityVillager var1) {
      super(☃, EntityPlayer.class, 8.0F);
      this.villager = ☃;
   }

   @Override
   public boolean shouldExecute() {
      if (this.villager.isTrading()) {
         this.closestEntity = this.villager.getCustomer();
         return true;
      } else {
         return false;
      }
   }
}
