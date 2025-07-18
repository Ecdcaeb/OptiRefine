package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIAttackMelee extends EntityAIBase {
   World world;
   protected EntityCreature attacker;
   protected int attackTick;
   double speedTowardsTarget;
   boolean longMemory;
   Path path;
   private int delayCounter;
   private double targetX;
   private double targetY;
   private double targetZ;
   protected final int attackInterval = 20;

   public EntityAIAttackMelee(EntityCreature var1, double var2, boolean var4) {
      this.attacker = ☃;
      this.world = ☃.world;
      this.speedTowardsTarget = ☃;
      this.longMemory = ☃;
      this.setMutexBits(3);
   }

   @Override
   public boolean shouldExecute() {
      EntityLivingBase ☃ = this.attacker.getAttackTarget();
      if (☃ == null) {
         return false;
      } else if (!☃.isEntityAlive()) {
         return false;
      } else {
         this.path = this.attacker.getNavigator().getPathToEntityLiving(☃);
         return this.path != null ? true : this.getAttackReachSqr(☃) >= this.attacker.getDistanceSq(☃.posX, ☃.getEntityBoundingBox().minY, ☃.posZ);
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      EntityLivingBase ☃ = this.attacker.getAttackTarget();
      if (☃ == null) {
         return false;
      } else if (!☃.isEntityAlive()) {
         return false;
      } else if (!this.longMemory) {
         return !this.attacker.getNavigator().noPath();
      } else {
         return !this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(☃))
            ? false
            : !(☃ instanceof EntityPlayer) || !((EntityPlayer)☃).isSpectator() && !((EntityPlayer)☃).isCreative();
      }
   }

   @Override
   public void startExecuting() {
      this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
      this.delayCounter = 0;
   }

   @Override
   public void resetTask() {
      EntityLivingBase ☃ = this.attacker.getAttackTarget();
      if (☃ instanceof EntityPlayer && (((EntityPlayer)☃).isSpectator() || ((EntityPlayer)☃).isCreative())) {
         this.attacker.setAttackTarget(null);
      }

      this.attacker.getNavigator().clearPath();
   }

   @Override
   public void updateTask() {
      EntityLivingBase ☃ = this.attacker.getAttackTarget();
      this.attacker.getLookHelper().setLookPositionWithEntity(☃, 30.0F, 30.0F);
      double ☃x = this.attacker.getDistanceSq(☃.posX, ☃.getEntityBoundingBox().minY, ☃.posZ);
      this.delayCounter--;
      if ((this.longMemory || this.attacker.getEntitySenses().canSee(☃))
         && this.delayCounter <= 0
         && (
            this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0
               || ☃.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0
               || this.attacker.getRNG().nextFloat() < 0.05F
         )) {
         this.targetX = ☃.posX;
         this.targetY = ☃.getEntityBoundingBox().minY;
         this.targetZ = ☃.posZ;
         this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
         if (☃x > 1024.0) {
            this.delayCounter += 10;
         } else if (☃x > 256.0) {
            this.delayCounter += 5;
         }

         if (!this.attacker.getNavigator().tryMoveToEntityLiving(☃, this.speedTowardsTarget)) {
            this.delayCounter += 15;
         }
      }

      this.attackTick = Math.max(this.attackTick - 1, 0);
      this.checkAndPerformAttack(☃, ☃x);
   }

   protected void checkAndPerformAttack(EntityLivingBase var1, double var2) {
      double ☃ = this.getAttackReachSqr(☃);
      if (☃ <= ☃ && this.attackTick <= 0) {
         this.attackTick = 20;
         this.attacker.swingArm(EnumHand.MAIN_HAND);
         this.attacker.attackEntityAsMob(☃);
      }
   }

   protected double getAttackReachSqr(EntityLivingBase var1) {
      return this.attacker.width * 2.0F * (this.attacker.width * 2.0F) + ☃.width;
   }
}
