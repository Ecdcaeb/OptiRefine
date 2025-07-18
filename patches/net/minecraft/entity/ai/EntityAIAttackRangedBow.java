package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRangedBow<T extends EntityMob & IRangedAttackMob> extends EntityAIBase {
   private final T entity;
   private final double moveSpeedAmp;
   private int attackCooldown;
   private final float maxAttackDistance;
   private int attackTime = -1;
   private int seeTime;
   private boolean strafingClockwise;
   private boolean strafingBackwards;
   private int strafingTime = -1;

   public EntityAIAttackRangedBow(T var1, double var2, int var4, float var5) {
      this.entity = ☃;
      this.moveSpeedAmp = ☃;
      this.attackCooldown = ☃;
      this.maxAttackDistance = ☃ * ☃;
      this.setMutexBits(3);
   }

   public void setAttackCooldown(int var1) {
      this.attackCooldown = ☃;
   }

   @Override
   public boolean shouldExecute() {
      return this.entity.getAttackTarget() == null ? false : this.isBowInMainhand();
   }

   protected boolean isBowInMainhand() {
      return !this.entity.getHeldItemMainhand().isEmpty() && this.entity.getHeldItemMainhand().getItem() == Items.BOW;
   }

   @Override
   public boolean shouldContinueExecuting() {
      return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isBowInMainhand();
   }

   @Override
   public void startExecuting() {
      super.startExecuting();
      this.entity.setSwingingArms(true);
   }

   @Override
   public void resetTask() {
      super.resetTask();
      this.entity.setSwingingArms(false);
      this.seeTime = 0;
      this.attackTime = -1;
      this.entity.resetActiveHand();
   }

   @Override
   public void updateTask() {
      EntityLivingBase ☃ = this.entity.getAttackTarget();
      if (☃ != null) {
         double ☃x = this.entity.getDistanceSq(☃.posX, ☃.getEntityBoundingBox().minY, ☃.posZ);
         boolean ☃xx = this.entity.getEntitySenses().canSee(☃);
         boolean ☃xxx = this.seeTime > 0;
         if (☃xx != ☃xxx) {
            this.seeTime = 0;
         }

         if (☃xx) {
            this.seeTime++;
         } else {
            this.seeTime--;
         }

         if (!(☃x > this.maxAttackDistance) && this.seeTime >= 20) {
            this.entity.getNavigator().clearPath();
            this.strafingTime++;
         } else {
            this.entity.getNavigator().tryMoveToEntityLiving(☃, this.moveSpeedAmp);
            this.strafingTime = -1;
         }

         if (this.strafingTime >= 20) {
            if (this.entity.getRNG().nextFloat() < 0.3) {
               this.strafingClockwise = !this.strafingClockwise;
            }

            if (this.entity.getRNG().nextFloat() < 0.3) {
               this.strafingBackwards = !this.strafingBackwards;
            }

            this.strafingTime = 0;
         }

         if (this.strafingTime > -1) {
            if (☃x > this.maxAttackDistance * 0.75F) {
               this.strafingBackwards = false;
            } else if (☃x < this.maxAttackDistance * 0.25F) {
               this.strafingBackwards = true;
            }

            this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
            this.entity.faceEntity(☃, 30.0F, 30.0F);
         } else {
            this.entity.getLookHelper().setLookPositionWithEntity(☃, 30.0F, 30.0F);
         }

         if (this.entity.isHandActive()) {
            if (!☃xx && this.seeTime < -60) {
               this.entity.resetActiveHand();
            } else if (☃xx) {
               int ☃xxxx = this.entity.getItemInUseMaxCount();
               if (☃xxxx >= 20) {
                  this.entity.resetActiveHand();
                  this.entity.attackEntityWithRangedAttack(☃, ItemBow.getArrowVelocity(☃xxxx));
                  this.attackTime = this.attackCooldown;
               }
            }
         } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
            this.entity.setActiveHand(EnumHand.MAIN_HAND);
         }
      }
   }
}
