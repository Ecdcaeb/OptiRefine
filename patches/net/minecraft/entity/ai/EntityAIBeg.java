package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIBeg extends EntityAIBase {
   private final EntityWolf wolf;
   private EntityPlayer player;
   private final World world;
   private final float minPlayerDistance;
   private int timeoutCounter;

   public EntityAIBeg(EntityWolf var1, float var2) {
      this.wolf = ☃;
      this.world = ☃.world;
      this.minPlayerDistance = ☃;
      this.setMutexBits(2);
   }

   @Override
   public boolean shouldExecute() {
      this.player = this.world.getClosestPlayerToEntity(this.wolf, this.minPlayerDistance);
      return this.player == null ? false : this.hasTemptationItemInHand(this.player);
   }

   @Override
   public boolean shouldContinueExecuting() {
      if (!this.player.isEntityAlive()) {
         return false;
      } else {
         return this.wolf.getDistanceSq(this.player) > this.minPlayerDistance * this.minPlayerDistance
            ? false
            : this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
      }
   }

   @Override
   public void startExecuting() {
      this.wolf.setBegging(true);
      this.timeoutCounter = 40 + this.wolf.getRNG().nextInt(40);
   }

   @Override
   public void resetTask() {
      this.wolf.setBegging(false);
      this.player = null;
   }

   @Override
   public void updateTask() {
      this.wolf
         .getLookHelper()
         .setLookPosition(this.player.posX, this.player.posY + this.player.getEyeHeight(), this.player.posZ, 10.0F, this.wolf.getVerticalFaceSpeed());
      this.timeoutCounter--;
   }

   private boolean hasTemptationItemInHand(EntityPlayer var1) {
      for (EnumHand ☃ : EnumHand.values()) {
         ItemStack ☃x = ☃.getHeldItem(☃);
         if (this.wolf.isTamed() && ☃x.getItem() == Items.BONE) {
            return true;
         }

         if (this.wolf.isBreedingItem(☃x)) {
            return true;
         }
      }

      return false;
   }
}
