package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EntityAIRunAroundLikeCrazy extends EntityAIBase {
   private final AbstractHorse horseHost;
   private final double speed;
   private double targetX;
   private double targetY;
   private double targetZ;

   public EntityAIRunAroundLikeCrazy(AbstractHorse var1, double var2) {
      this.horseHost = ☃;
      this.speed = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.horseHost.isTame() && this.horseHost.isBeingRidden()) {
         Vec3d ☃ = RandomPositionGenerator.findRandomTarget(this.horseHost, 5, 4);
         if (☃ == null) {
            return false;
         } else {
            this.targetX = ☃.x;
            this.targetY = ☃.y;
            this.targetZ = ☃.z;
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public void startExecuting() {
      this.horseHost.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, this.speed);
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.horseHost.isTame() && !this.horseHost.getNavigator().noPath() && this.horseHost.isBeingRidden();
   }

   @Override
   public void updateTask() {
      if (!this.horseHost.isTame() && this.horseHost.getRNG().nextInt(50) == 0) {
         Entity ☃ = this.horseHost.getPassengers().get(0);
         if (☃ == null) {
            return;
         }

         if (☃ instanceof EntityPlayer) {
            int ☃x = this.horseHost.getTemper();
            int ☃xx = this.horseHost.getMaxTemper();
            if (☃xx > 0 && this.horseHost.getRNG().nextInt(☃xx) < ☃x) {
               this.horseHost.setTamedBy((EntityPlayer)☃);
               return;
            }

            this.horseHost.increaseTemper(5);
         }

         this.horseHost.removePassengers();
         this.horseHost.makeMad();
         this.horseHost.world.setEntityState(this.horseHost, (byte)6);
      }
   }
}
