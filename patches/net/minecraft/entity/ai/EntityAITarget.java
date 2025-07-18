package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public abstract class EntityAITarget extends EntityAIBase {
   protected final EntityCreature taskOwner;
   protected boolean shouldCheckSight;
   private final boolean nearbyOnly;
   private int targetSearchStatus;
   private int targetSearchDelay;
   private int targetUnseenTicks;
   protected EntityLivingBase target;
   protected int unseenMemoryTicks = 60;

   public EntityAITarget(EntityCreature var1, boolean var2) {
      this(☃, ☃, false);
   }

   public EntityAITarget(EntityCreature var1, boolean var2, boolean var3) {
      this.taskOwner = ☃;
      this.shouldCheckSight = ☃;
      this.nearbyOnly = ☃;
   }

   @Override
   public boolean shouldContinueExecuting() {
      EntityLivingBase ☃ = this.taskOwner.getAttackTarget();
      if (☃ == null) {
         ☃ = this.target;
      }

      if (☃ == null) {
         return false;
      } else if (!☃.isEntityAlive()) {
         return false;
      } else {
         Team ☃x = this.taskOwner.getTeam();
         Team ☃xx = ☃.getTeam();
         if (☃x != null && ☃xx == ☃x) {
            return false;
         } else {
            double ☃xxx = this.getTargetDistance();
            if (this.taskOwner.getDistanceSq(☃) > ☃xxx * ☃xxx) {
               return false;
            } else {
               if (this.shouldCheckSight) {
                  if (this.taskOwner.getEntitySenses().canSee(☃)) {
                     this.targetUnseenTicks = 0;
                  } else if (++this.targetUnseenTicks > this.unseenMemoryTicks) {
                     return false;
                  }
               }

               if (☃ instanceof EntityPlayer && ((EntityPlayer)☃).capabilities.disableDamage) {
                  return false;
               } else {
                  this.taskOwner.setAttackTarget(☃);
                  return true;
               }
            }
         }
      }
   }

   protected double getTargetDistance() {
      IAttributeInstance ☃ = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
      return ☃ == null ? 16.0 : ☃.getAttributeValue();
   }

   @Override
   public void startExecuting() {
      this.targetSearchStatus = 0;
      this.targetSearchDelay = 0;
      this.targetUnseenTicks = 0;
   }

   @Override
   public void resetTask() {
      this.taskOwner.setAttackTarget(null);
      this.target = null;
   }

   public static boolean isSuitableTarget(EntityLiving var0, @Nullable EntityLivingBase var1, boolean var2, boolean var3) {
      if (☃ == null) {
         return false;
      } else if (☃ == ☃) {
         return false;
      } else if (!☃.isEntityAlive()) {
         return false;
      } else if (!☃.canAttackClass((Class<? extends EntityLivingBase>)☃.getClass())) {
         return false;
      } else if (☃.isOnSameTeam(☃)) {
         return false;
      } else {
         if (☃ instanceof IEntityOwnable && ((IEntityOwnable)☃).getOwnerId() != null) {
            if (☃ instanceof IEntityOwnable && ((IEntityOwnable)☃).getOwnerId().equals(((IEntityOwnable)☃).getOwnerId())) {
               return false;
            }

            if (☃ == ((IEntityOwnable)☃).getOwner()) {
               return false;
            }
         } else if (☃ instanceof EntityPlayer && !☃ && ((EntityPlayer)☃).capabilities.disableDamage) {
            return false;
         }

         return !☃ || ☃.getEntitySenses().canSee(☃);
      }
   }

   protected boolean isSuitableTarget(@Nullable EntityLivingBase var1, boolean var2) {
      if (!isSuitableTarget(this.taskOwner, ☃, ☃, this.shouldCheckSight)) {
         return false;
      } else if (!this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(☃))) {
         return false;
      } else {
         if (this.nearbyOnly) {
            if (--this.targetSearchDelay <= 0) {
               this.targetSearchStatus = 0;
            }

            if (this.targetSearchStatus == 0) {
               this.targetSearchStatus = this.canEasilyReach(☃) ? 1 : 2;
            }

            if (this.targetSearchStatus == 2) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean canEasilyReach(EntityLivingBase var1) {
      this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
      Path ☃ = this.taskOwner.getNavigator().getPathToEntityLiving(☃);
      if (☃ == null) {
         return false;
      } else {
         PathPoint ☃x = ☃.getFinalPathPoint();
         if (☃x == null) {
            return false;
         } else {
            int ☃xx = ☃x.x - MathHelper.floor(☃.posX);
            int ☃xxx = ☃x.z - MathHelper.floor(☃.posZ);
            return ☃xx * ☃xx + ☃xxx * ☃xxx <= 2.25;
         }
      }
   }

   public EntityAITarget setUnseenMemoryTicks(int var1) {
      this.unseenMemoryTicks = ☃;
      return this;
   }
}
