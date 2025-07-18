package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.MathHelper;

public class ModelZombie extends ModelBiped {
   public ModelZombie() {
      this(0.0F, false);
   }

   public ModelZombie(float var1, boolean var2) {
      super(☃, 0.0F, 64, ☃ ? 32 : 64);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      boolean ☃ = ☃ instanceof EntityZombie && ((EntityZombie)☃).isArmsRaised();
      float ☃x = MathHelper.sin(this.swingProgress * (float) Math.PI);
      float ☃xx = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightArm.rotateAngleY = -(0.1F - ☃x * 0.6F);
      this.bipedLeftArm.rotateAngleY = 0.1F - ☃x * 0.6F;
      float ☃xxx = (float) -Math.PI / (☃ ? 1.5F : 2.25F);
      this.bipedRightArm.rotateAngleX = ☃xxx;
      this.bipedLeftArm.rotateAngleX = ☃xxx;
      this.bipedRightArm.rotateAngleX += ☃x * 1.2F - ☃xx * 0.4F;
      this.bipedLeftArm.rotateAngleX += ☃x * 1.2F - ☃xx * 0.4F;
      this.bipedRightArm.rotateAngleZ = this.bipedRightArm.rotateAngleZ + (MathHelper.cos(☃ * 0.09F) * 0.05F + 0.05F);
      this.bipedLeftArm.rotateAngleZ = this.bipedLeftArm.rotateAngleZ - (MathHelper.cos(☃ * 0.09F) * 0.05F + 0.05F);
      this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX + MathHelper.sin(☃ * 0.067F) * 0.05F;
      this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX - MathHelper.sin(☃ * 0.067F) * 0.05F;
   }
}
