package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.util.math.MathHelper;

public class EntityAIAttackRanged extends EntityAIBase {
   private final EntityLiving entityHost;
   private final IRangedAttackMob rangedAttackEntityHost;
   private EntityLivingBase attackTarget;
   private int rangedAttackTime = -1;
   private final double entityMoveSpeed;
   private int seeTime;
   private final int attackIntervalMin;
   private final int maxRangedAttackTime;
   private final float attackRadius;
   private final float maxAttackDistance;

   public EntityAIAttackRanged(IRangedAttackMob var1, double var2, int var4, float var5) {
      this(☃, ☃, ☃, ☃, ☃);
   }

   public EntityAIAttackRanged(IRangedAttackMob var1, double var2, int var4, int var5, float var6) {
      if (!(☃ instanceof EntityLivingBase)) {
         throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
      } else {
         this.rangedAttackEntityHost = ☃;
         this.entityHost = (EntityLiving)☃;
         this.entityMoveSpeed = ☃;
         this.attackIntervalMin = ☃;
         this.maxRangedAttackTime = ☃;
         this.attackRadius = ☃;
         this.maxAttackDistance = ☃ * ☃;
         this.setMutexBits(3);
      }
   }

   @Override
   public boolean shouldExecute() {
      EntityLivingBase ☃ = this.entityHost.getAttackTarget();
      if (☃ == null) {
         return false;
      } else {
         this.attackTarget = ☃;
         return true;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
   }

   @Override
   public void resetTask() {
      this.attackTarget = null;
      this.seeTime = 0;
      this.rangedAttackTime = -1;
   }

   @Override
   public void updateTask() {
      double ☃ = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
      boolean ☃x = this.entityHost.getEntitySenses().canSee(this.attackTarget);
      if (☃x) {
         this.seeTime++;
      } else {
         this.seeTime = 0;
      }

      if (!(☃ > this.maxAttackDistance) && this.seeTime >= 20) {
         this.entityHost.getNavigator().clearPath();
      } else {
         this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
      }

      this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      if (--this.rangedAttackTime == 0) {
         if (!☃x) {
            return;
         }

         float ☃xx = MathHelper.sqrt(☃) / this.attackRadius;
         float var5 = MathHelper.clamp(☃xx, 0.1F, 1.0F);
         this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, var5);
         this.rangedAttackTime = MathHelper.floor(☃xx * (this.maxRangedAttackTime - this.attackIntervalMin) + this.attackIntervalMin);
      } else if (this.rangedAttackTime < 0) {
         float ☃xx = MathHelper.sqrt(☃) / this.attackRadius;
         this.rangedAttackTime = MathHelper.floor(☃xx * (this.maxRangedAttackTime - this.attackIntervalMin) + this.attackIntervalMin);
      }
   }
}
