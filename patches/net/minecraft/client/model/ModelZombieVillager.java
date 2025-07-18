package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.MathHelper;

public class ModelZombieVillager extends ModelBiped {
   public ModelZombieVillager() {
      this(0.0F, 0.0F, false);
   }

   public ModelZombieVillager(float var1, float var2, boolean var3) {
      super(☃, 0.0F, 64, ☃ ? 32 : 64);
      if (☃) {
         this.bipedHead = new ModelRenderer(this, 0, 0);
         this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 8, 8, ☃);
         this.bipedHead.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
         this.bipedBody = new ModelRenderer(this, 16, 16);
         this.bipedBody.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
         this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, ☃ + 0.1F);
         this.bipedRightLeg = new ModelRenderer(this, 0, 16);
         this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + ☃, 0.0F);
         this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃ + 0.1F);
         this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
         this.bipedLeftLeg.mirror = true;
         this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + ☃, 0.0F);
         this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃ + 0.1F);
      } else {
         this.bipedHead = new ModelRenderer(this, 0, 0);
         this.bipedHead.setRotationPoint(0.0F, ☃, 0.0F);
         this.bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, ☃);
         this.bipedHead.setTextureOffset(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, ☃);
         this.bipedBody = new ModelRenderer(this, 16, 20);
         this.bipedBody.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
         this.bipedBody.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, ☃);
         this.bipedBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, ☃ + 0.05F);
         this.bipedRightArm = new ModelRenderer(this, 44, 38);
         this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, ☃);
         this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + ☃, 0.0F);
         this.bipedLeftArm = new ModelRenderer(this, 44, 38);
         this.bipedLeftArm.mirror = true;
         this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, ☃);
         this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + ☃, 0.0F);
         this.bipedRightLeg = new ModelRenderer(this, 0, 22);
         this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + ☃, 0.0F);
         this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃);
         this.bipedLeftLeg = new ModelRenderer(this, 0, 22);
         this.bipedLeftLeg.mirror = true;
         this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + ☃, 0.0F);
         this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃);
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      EntityZombie ☃ = (EntityZombie)☃;
      float ☃x = MathHelper.sin(this.swingProgress * (float) Math.PI);
      float ☃xx = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightArm.rotateAngleY = -(0.1F - ☃x * 0.6F);
      this.bipedLeftArm.rotateAngleY = 0.1F - ☃x * 0.6F;
      float ☃xxx = (float) -Math.PI / (☃.isArmsRaised() ? 1.5F : 2.25F);
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
