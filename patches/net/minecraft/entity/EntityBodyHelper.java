package net.minecraft.entity;

import net.minecraft.util.math.MathHelper;

public class EntityBodyHelper {
   private final EntityLivingBase living;
   private int rotationTickCounter;
   private float prevRenderYawHead;

   public EntityBodyHelper(EntityLivingBase var1) {
      this.living = ☃;
   }

   public void updateRenderAngles() {
      double ☃ = this.living.posX - this.living.prevPosX;
      double ☃x = this.living.posZ - this.living.prevPosZ;
      if (☃ * ☃ + ☃x * ☃x > 2.5000003E-7F) {
         this.living.renderYawOffset = this.living.rotationYaw;
         this.living.rotationYawHead = this.computeAngleWithBound(this.living.renderYawOffset, this.living.rotationYawHead, 75.0F);
         this.prevRenderYawHead = this.living.rotationYawHead;
         this.rotationTickCounter = 0;
      } else {
         if (this.living.getPassengers().isEmpty() || !(this.living.getPassengers().get(0) instanceof EntityLiving)) {
            float ☃xx = 75.0F;
            if (Math.abs(this.living.rotationYawHead - this.prevRenderYawHead) > 15.0F) {
               this.rotationTickCounter = 0;
               this.prevRenderYawHead = this.living.rotationYawHead;
            } else {
               this.rotationTickCounter++;
               int ☃xxx = 10;
               if (this.rotationTickCounter > 10) {
                  ☃xx = Math.max(1.0F - (this.rotationTickCounter - 10) / 10.0F, 0.0F) * 75.0F;
               }
            }

            this.living.renderYawOffset = this.computeAngleWithBound(this.living.rotationYawHead, this.living.renderYawOffset, ☃xx);
         }
      }
   }

   private float computeAngleWithBound(float var1, float var2, float var3) {
      float ☃ = MathHelper.wrapDegrees(☃ - ☃);
      if (☃ < -☃) {
         ☃ = -☃;
      }

      if (☃ >= ☃) {
         ☃ = ☃;
      }

      return ☃ - ☃;
   }
}
