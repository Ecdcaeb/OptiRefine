package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelIllager extends ModelBase {
   public ModelRenderer head;
   public ModelRenderer hat;
   public ModelRenderer body;
   public ModelRenderer arms;
   public ModelRenderer leg0;
   public ModelRenderer leg1;
   public ModelRenderer nose;
   public ModelRenderer rightArm;
   public ModelRenderer leftArm;

   public ModelIllager(float var1, float var2, int var3, int var4) {
      this.head = new ModelRenderer(this).setTextureSize(☃, ☃);
      this.head.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
      this.head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, ☃);
      this.hat = new ModelRenderer(this, 32, 0).setTextureSize(☃, ☃);
      this.hat.addBox(-4.0F, -10.0F, -4.0F, 8, 12, 8, ☃ + 0.45F);
      this.head.addChild(this.hat);
      this.hat.showModel = false;
      this.nose = new ModelRenderer(this).setTextureSize(☃, ☃);
      this.nose.setRotationPoint(0.0F, ☃ - 2.0F, 0.0F);
      this.nose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, ☃);
      this.head.addChild(this.nose);
      this.body = new ModelRenderer(this).setTextureSize(☃, ☃);
      this.body.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
      this.body.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, ☃);
      this.body.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, ☃ + 0.5F);
      this.arms = new ModelRenderer(this).setTextureSize(☃, ☃);
      this.arms.setRotationPoint(0.0F, 0.0F + ☃ + 2.0F, 0.0F);
      this.arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, ☃);
      ModelRenderer ☃ = new ModelRenderer(this, 44, 22).setTextureSize(☃, ☃);
      ☃.mirror = true;
      ☃.addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, ☃);
      this.arms.addChild(☃);
      this.arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, ☃);
      this.leg0 = new ModelRenderer(this, 0, 22).setTextureSize(☃, ☃);
      this.leg0.setRotationPoint(-2.0F, 12.0F + ☃, 0.0F);
      this.leg0.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃);
      this.leg1 = new ModelRenderer(this, 0, 22).setTextureSize(☃, ☃);
      this.leg1.mirror = true;
      this.leg1.setRotationPoint(2.0F, 12.0F + ☃, 0.0F);
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃);
      this.rightArm = new ModelRenderer(this, 40, 46).setTextureSize(☃, ☃);
      this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, ☃);
      this.rightArm.setRotationPoint(-5.0F, 2.0F + ☃, 0.0F);
      this.leftArm = new ModelRenderer(this, 40, 46).setTextureSize(☃, ☃);
      this.leftArm.mirror = true;
      this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, ☃);
      this.leftArm.setRotationPoint(5.0F, 2.0F + ☃, 0.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.head.render(☃);
      this.body.render(☃);
      this.leg0.render(☃);
      this.leg1.render(☃);
      AbstractIllager ☃ = (AbstractIllager)☃;
      if (☃.getArmPose() == AbstractIllager.IllagerArmPose.CROSSED) {
         this.arms.render(☃);
      } else {
         this.rightArm.render(☃);
         this.leftArm.render(☃);
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.head.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.head.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.arms.rotationPointY = 3.0F;
      this.arms.rotationPointZ = -1.0F;
      this.arms.rotateAngleX = -0.75F;
      this.leg0.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 1.4F * ☃ * 0.5F;
      this.leg1.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * 1.4F * ☃ * 0.5F;
      this.leg0.rotateAngleY = 0.0F;
      this.leg1.rotateAngleY = 0.0F;
      AbstractIllager.IllagerArmPose ☃ = ((AbstractIllager)☃).getArmPose();
      if (☃ == AbstractIllager.IllagerArmPose.ATTACKING) {
         float ☃x = MathHelper.sin(this.swingProgress * (float) Math.PI);
         float ☃xx = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
         this.rightArm.rotateAngleZ = 0.0F;
         this.leftArm.rotateAngleZ = 0.0F;
         this.rightArm.rotateAngleY = (float) (Math.PI / 20);
         this.leftArm.rotateAngleY = (float) (-Math.PI / 20);
         if (((EntityLivingBase)☃).getPrimaryHand() == EnumHandSide.RIGHT) {
            this.rightArm.rotateAngleX = -1.8849558F + MathHelper.cos(☃ * 0.09F) * 0.15F;
            this.leftArm.rotateAngleX = -0.0F + MathHelper.cos(☃ * 0.19F) * 0.5F;
            this.rightArm.rotateAngleX += ☃x * 2.2F - ☃xx * 0.4F;
            this.leftArm.rotateAngleX += ☃x * 1.2F - ☃xx * 0.4F;
         } else {
            this.rightArm.rotateAngleX = -0.0F + MathHelper.cos(☃ * 0.19F) * 0.5F;
            this.leftArm.rotateAngleX = -1.8849558F + MathHelper.cos(☃ * 0.09F) * 0.15F;
            this.rightArm.rotateAngleX += ☃x * 1.2F - ☃xx * 0.4F;
            this.leftArm.rotateAngleX += ☃x * 2.2F - ☃xx * 0.4F;
         }

         this.rightArm.rotateAngleZ = this.rightArm.rotateAngleZ + (MathHelper.cos(☃ * 0.09F) * 0.05F + 0.05F);
         this.leftArm.rotateAngleZ = this.leftArm.rotateAngleZ - (MathHelper.cos(☃ * 0.09F) * 0.05F + 0.05F);
         this.rightArm.rotateAngleX = this.rightArm.rotateAngleX + MathHelper.sin(☃ * 0.067F) * 0.05F;
         this.leftArm.rotateAngleX = this.leftArm.rotateAngleX - MathHelper.sin(☃ * 0.067F) * 0.05F;
      } else if (☃ == AbstractIllager.IllagerArmPose.SPELLCASTING) {
         this.rightArm.rotationPointZ = 0.0F;
         this.rightArm.rotationPointX = -5.0F;
         this.leftArm.rotationPointZ = 0.0F;
         this.leftArm.rotationPointX = 5.0F;
         this.rightArm.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 0.25F;
         this.leftArm.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 0.25F;
         this.rightArm.rotateAngleZ = (float) (Math.PI * 3.0 / 4.0);
         this.leftArm.rotateAngleZ = (float) (-Math.PI * 3.0 / 4.0);
         this.rightArm.rotateAngleY = 0.0F;
         this.leftArm.rotateAngleY = 0.0F;
      } else if (☃ == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
         this.rightArm.rotateAngleY = -0.1F + this.head.rotateAngleY;
         this.rightArm.rotateAngleX = (float) (-Math.PI / 2) + this.head.rotateAngleX;
         this.leftArm.rotateAngleX = -0.9424779F + this.head.rotateAngleX;
         this.leftArm.rotateAngleY = this.head.rotateAngleY - 0.4F;
         this.leftArm.rotateAngleZ = (float) (Math.PI / 2);
      }
   }

   public ModelRenderer getArm(EnumHandSide var1) {
      return ☃ == EnumHandSide.LEFT ? this.leftArm : this.rightArm;
   }
}
