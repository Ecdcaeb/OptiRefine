package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class EntityLookHelper {
   private final EntityLiving entity;
   private float deltaLookYaw;
   private float deltaLookPitch;
   private boolean isLooking;
   private double posX;
   private double posY;
   private double posZ;

   public EntityLookHelper(EntityLiving var1) {
      this.entity = ☃;
   }

   public void setLookPositionWithEntity(Entity var1, float var2, float var3) {
      this.posX = ☃.posX;
      if (☃ instanceof EntityLivingBase) {
         this.posY = ☃.posY + ☃.getEyeHeight();
      } else {
         this.posY = (☃.getEntityBoundingBox().minY + ☃.getEntityBoundingBox().maxY) / 2.0;
      }

      this.posZ = ☃.posZ;
      this.deltaLookYaw = ☃;
      this.deltaLookPitch = ☃;
      this.isLooking = true;
   }

   public void setLookPosition(double var1, double var3, double var5, float var7, float var8) {
      this.posX = ☃;
      this.posY = ☃;
      this.posZ = ☃;
      this.deltaLookYaw = ☃;
      this.deltaLookPitch = ☃;
      this.isLooking = true;
   }

   public void onUpdateLook() {
      this.entity.rotationPitch = 0.0F;
      if (this.isLooking) {
         this.isLooking = false;
         double ☃ = this.posX - this.entity.posX;
         double ☃x = this.posY - (this.entity.posY + this.entity.getEyeHeight());
         double ☃xx = this.posZ - this.entity.posZ;
         double ☃xxx = MathHelper.sqrt(☃ * ☃ + ☃xx * ☃xx);
         float ☃xxxx = (float)(MathHelper.atan2(☃xx, ☃) * 180.0F / (float)Math.PI) - 90.0F;
         float ☃xxxxx = (float)(-(MathHelper.atan2(☃x, ☃xxx) * 180.0F / (float)Math.PI));
         this.entity.rotationPitch = this.updateRotation(this.entity.rotationPitch, ☃xxxxx, this.deltaLookPitch);
         this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, ☃xxxx, this.deltaLookYaw);
      } else {
         this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
      }

      float ☃ = MathHelper.wrapDegrees(this.entity.rotationYawHead - this.entity.renderYawOffset);
      if (!this.entity.getNavigator().noPath()) {
         if (☃ < -75.0F) {
            this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0F;
         }

         if (☃ > 75.0F) {
            this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0F;
         }
      }
   }

   private float updateRotation(float var1, float var2, float var3) {
      float ☃ = MathHelper.wrapDegrees(☃ - ☃);
      if (☃ > ☃) {
         ☃ = ☃;
      }

      if (☃ < -☃) {
         ☃ = -☃;
      }

      return ☃ + ☃;
   }

   public boolean getIsLooking() {
      return this.isLooking;
   }

   public double getLookPosX() {
      return this.posX;
   }

   public double getLookPosY() {
      return this.posY;
   }

   public double getLookPosZ() {
      return this.posZ;
   }
}
