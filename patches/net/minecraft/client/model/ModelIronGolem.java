package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;

public class ModelIronGolem extends ModelBase {
   public ModelRenderer ironGolemHead;
   public ModelRenderer ironGolemBody;
   public ModelRenderer ironGolemRightArm;
   public ModelRenderer ironGolemLeftArm;
   public ModelRenderer ironGolemLeftLeg;
   public ModelRenderer ironGolemRightLeg;

   public ModelIronGolem() {
      this(0.0F);
   }

   public ModelIronGolem(float var1) {
      this(☃, -7.0F);
   }

   public ModelIronGolem(float var1, float var2) {
      int ☃ = 128;
      int ☃x = 128;
      this.ironGolemHead = new ModelRenderer(this).setTextureSize(128, 128);
      this.ironGolemHead.setRotationPoint(0.0F, 0.0F + ☃, -2.0F);
      this.ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, ☃);
      this.ironGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, ☃);
      this.ironGolemBody = new ModelRenderer(this).setTextureSize(128, 128);
      this.ironGolemBody.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
      this.ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, ☃);
      this.ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, ☃ + 0.5F);
      this.ironGolemRightArm = new ModelRenderer(this).setTextureSize(128, 128);
      this.ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
      this.ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, ☃);
      this.ironGolemLeftArm = new ModelRenderer(this).setTextureSize(128, 128);
      this.ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
      this.ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, ☃);
      this.ironGolemLeftLeg = new ModelRenderer(this, 0, 22).setTextureSize(128, 128);
      this.ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + ☃, 0.0F);
      this.ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, ☃);
      this.ironGolemRightLeg = new ModelRenderer(this, 0, 22).setTextureSize(128, 128);
      this.ironGolemRightLeg.mirror = true;
      this.ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + ☃, 0.0F);
      this.ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, ☃);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.ironGolemHead.render(☃);
      this.ironGolemBody.render(☃);
      this.ironGolemLeftLeg.render(☃);
      this.ironGolemRightLeg.render(☃);
      this.ironGolemRightArm.render(☃);
      this.ironGolemLeftArm.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.ironGolemHead.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.ironGolemHead.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.ironGolemLeftLeg.rotateAngleX = -1.5F * this.triangleWave(☃, 13.0F) * ☃;
      this.ironGolemRightLeg.rotateAngleX = 1.5F * this.triangleWave(☃, 13.0F) * ☃;
      this.ironGolemLeftLeg.rotateAngleY = 0.0F;
      this.ironGolemRightLeg.rotateAngleY = 0.0F;
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      EntityIronGolem ☃ = (EntityIronGolem)☃;
      int ☃x = ☃.getAttackTimer();
      if (☃x > 0) {
         this.ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave(☃x - ☃, 10.0F);
         this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave(☃x - ☃, 10.0F);
      } else {
         int ☃xx = ☃.getHoldRoseTick();
         if (☃xx > 0) {
            this.ironGolemRightArm.rotateAngleX = -0.8F + 0.025F * this.triangleWave(☃xx, 70.0F);
            this.ironGolemLeftArm.rotateAngleX = 0.0F;
         } else {
            this.ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * this.triangleWave(☃, 13.0F)) * ☃;
            this.ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * this.triangleWave(☃, 13.0F)) * ☃;
         }
      }
   }

   private float triangleWave(float var1, float var2) {
      return (Math.abs(☃ % ☃ - ☃ * 0.5F) - ☃ * 0.25F) / (☃ * 0.25F);
   }
}
