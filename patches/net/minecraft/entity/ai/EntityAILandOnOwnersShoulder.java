package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILandOnOwnersShoulder extends EntityAIBase {
   private final EntityShoulderRiding entity;
   private EntityPlayer owner;
   private boolean isSittingOnShoulder;

   public EntityAILandOnOwnersShoulder(EntityShoulderRiding var1) {
      this.entity = ☃;
   }

   @Override
   public boolean shouldExecute() {
      EntityLivingBase ☃ = this.entity.getOwner();
      boolean ☃x = ☃ != null && !((EntityPlayer)☃).isSpectator() && !((EntityPlayer)☃).capabilities.isFlying && !☃.isInWater();
      return !this.entity.isSitting() && ☃x && this.entity.canSitOnShoulder();
   }

   @Override
   public boolean isInterruptible() {
      return !this.isSittingOnShoulder;
   }

   @Override
   public void startExecuting() {
      this.owner = (EntityPlayer)this.entity.getOwner();
      this.isSittingOnShoulder = false;
   }

   @Override
   public void updateTask() {
      if (!this.isSittingOnShoulder && !this.entity.isSitting() && !this.entity.getLeashed()) {
         if (this.entity.getEntityBoundingBox().intersects(this.owner.getEntityBoundingBox())) {
            this.isSittingOnShoulder = this.entity.setEntityOnShoulder(this.owner);
         }
      }
   }
}
