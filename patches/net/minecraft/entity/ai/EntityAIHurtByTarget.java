package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget {
   private final boolean entityCallsForHelp;
   private int revengeTimerOld;
   private final Class<?>[] excludedReinforcementTypes;

   public EntityAIHurtByTarget(EntityCreature var1, boolean var2, Class<?>... var3) {
      super(☃, true);
      this.entityCallsForHelp = ☃;
      this.excludedReinforcementTypes = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      int ☃ = this.taskOwner.getRevengeTimer();
      EntityLivingBase ☃x = this.taskOwner.getRevengeTarget();
      return ☃ != this.revengeTimerOld && ☃x != null && this.isSuitableTarget(☃x, false);
   }

   @Override
   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
      this.target = this.taskOwner.getAttackTarget();
      this.revengeTimerOld = this.taskOwner.getRevengeTimer();
      this.unseenMemoryTicks = 300;
      if (this.entityCallsForHelp) {
         this.alertOthers();
      }

      super.startExecuting();
   }

   protected void alertOthers() {
      double ☃ = this.getTargetDistance();

      for (EntityCreature ☃x : this.taskOwner
         .world
         .getEntitiesWithinAABB(
            this.taskOwner.getClass(),
            new AxisAlignedBB(
                  this.taskOwner.posX,
                  this.taskOwner.posY,
                  this.taskOwner.posZ,
                  this.taskOwner.posX + 1.0,
                  this.taskOwner.posY + 1.0,
                  this.taskOwner.posZ + 1.0
               )
               .grow(☃, 10.0, ☃)
         )) {
         if (this.taskOwner != ☃x
            && ☃x.getAttackTarget() == null
            && (!(this.taskOwner instanceof EntityTameable) || ((EntityTameable)this.taskOwner).getOwner() == ((EntityTameable)☃x).getOwner())
            && !☃x.isOnSameTeam(this.taskOwner.getRevengeTarget())) {
            boolean ☃xx = false;

            for (Class<?> ☃xxx : this.excludedReinforcementTypes) {
               if (☃x.getClass() == ☃xxx) {
                  ☃xx = true;
                  break;
               }
            }

            if (!☃xx) {
               this.setEntityAttackTarget(☃x, this.taskOwner.getRevengeTarget());
            }
         }
      }
   }

   protected void setEntityAttackTarget(EntityCreature var1, EntityLivingBase var2) {
      ☃.setAttackTarget(☃);
   }
}
