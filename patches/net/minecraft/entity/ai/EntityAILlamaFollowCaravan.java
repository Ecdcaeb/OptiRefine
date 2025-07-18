package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.math.Vec3d;

public class EntityAILlamaFollowCaravan extends EntityAIBase {
   public EntityLlama llama;
   private double speedModifier;
   private int distCheckCounter;

   public EntityAILlamaFollowCaravan(EntityLlama var1, double var2) {
      this.llama = ☃;
      this.speedModifier = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.llama.getLeashed() && !this.llama.inCaravan()) {
         List<EntityLlama> ☃ = this.llama
            .world
            .getEntitiesWithinAABB((Class<? extends EntityLlama>)this.llama.getClass(), this.llama.getEntityBoundingBox().grow(9.0, 4.0, 9.0));
         EntityLlama ☃x = null;
         double ☃xx = Double.MAX_VALUE;

         for (EntityLlama ☃xxx : ☃) {
            if (☃xxx.inCaravan() && !☃xxx.hasCaravanTrail()) {
               double ☃xxxx = this.llama.getDistanceSq(☃xxx);
               if (!(☃xxxx > ☃xx)) {
                  ☃xx = ☃xxxx;
                  ☃x = ☃xxx;
               }
            }
         }

         if (☃x == null) {
            for (EntityLlama ☃xxxx : ☃) {
               if (☃xxxx.getLeashed() && !☃xxxx.hasCaravanTrail()) {
                  double ☃xxxxx = this.llama.getDistanceSq(☃xxxx);
                  if (!(☃xxxxx > ☃xx)) {
                     ☃xx = ☃xxxxx;
                     ☃x = ☃xxxx;
                  }
               }
            }
         }

         if (☃x == null) {
            return false;
         } else if (☃xx < 4.0) {
            return false;
         } else if (!☃x.getLeashed() && !this.firstIsLeashed(☃x, 1)) {
            return false;
         } else {
            this.llama.joinCaravan(☃x);
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      if (this.llama.inCaravan() && this.llama.getCaravanHead().isEntityAlive() && this.firstIsLeashed(this.llama, 0)) {
         double ☃ = this.llama.getDistanceSq(this.llama.getCaravanHead());
         if (☃ > 676.0) {
            if (this.speedModifier <= 3.0) {
               this.speedModifier *= 1.2;
               this.distCheckCounter = 40;
               return true;
            }

            if (this.distCheckCounter == 0) {
               return false;
            }
         }

         if (this.distCheckCounter > 0) {
            this.distCheckCounter--;
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void resetTask() {
      this.llama.leaveCaravan();
      this.speedModifier = 2.1;
   }

   @Override
   public void updateTask() {
      if (this.llama.inCaravan()) {
         EntityLlama ☃ = this.llama.getCaravanHead();
         double ☃x = this.llama.getDistance(☃);
         float ☃xx = 2.0F;
         Vec3d ☃xxx = new Vec3d(☃.posX - this.llama.posX, ☃.posY - this.llama.posY, ☃.posZ - this.llama.posZ).normalize().scale(Math.max(☃x - 2.0, 0.0));
         this.llama.getNavigator().tryMoveToXYZ(this.llama.posX + ☃xxx.x, this.llama.posY + ☃xxx.y, this.llama.posZ + ☃xxx.z, this.speedModifier);
      }
   }

   private boolean firstIsLeashed(EntityLlama var1, int var2) {
      if (☃ > 8) {
         return false;
      } else if (☃.inCaravan()) {
         return ☃.getCaravanHead().getLeashed() ? true : this.firstIsLeashed(☃.getCaravanHead(), ++☃);
      } else {
         return false;
      }
   }
}
