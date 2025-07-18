package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelBiped extends ModelBase {
   public ModelRenderer bipedHead;
   public ModelRenderer bipedHeadwear;
   public ModelRenderer bipedBody;
   public ModelRenderer bipedRightArm;
   public ModelRenderer bipedLeftArm;
   public ModelRenderer bipedRightLeg;
   public ModelRenderer bipedLeftLeg;
   public ModelBiped.ArmPose leftArmPose = ModelBiped.ArmPose.EMPTY;
   public ModelBiped.ArmPose rightArmPose = ModelBiped.ArmPose.EMPTY;
   public boolean isSneak;

   public ModelBiped() {
      this(0.0F);
   }

   public ModelBiped(float var1) {
      this(☃, 0.0F, 64, 32);
   }

   public ModelBiped(float var1, float var2, int var3, int var4) {
      this.textureWidth = ☃;
      this.textureHeight = ☃;
      this.bipedHead = new ModelRenderer(this, 0, 0);
      this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, ☃);
      this.bipedHead.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
      this.bipedHeadwear = new ModelRenderer(this, 32, 0);
      this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, ☃ + 0.5F);
      this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
      this.bipedBody = new ModelRenderer(this, 16, 16);
      this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, ☃);
      this.bipedBody.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, ☃);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + ☃, 0.0F);
      this.bipedLeftArm = new ModelRenderer(this, 40, 16);
      this.bipedLeftArm.mirror = true;
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, ☃);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + ☃, 0.0F);
      this.bipedRightLeg = new ModelRenderer(this, 0, 16);
      this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃);
      this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + ☃, 0.0F);
      this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
      this.bipedLeftLeg.mirror = true;
      this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃);
      this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + ☃, 0.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      GlStateManager.pushMatrix();
      if (this.isChild) {
         float ☃ = 2.0F;
         GlStateManager.scale(0.75F, 0.75F, 0.75F);
         GlStateManager.translate(0.0F, 16.0F * ☃, 0.0F);
         this.bipedHead.render(☃);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         GlStateManager.translate(0.0F, 24.0F * ☃, 0.0F);
         this.bipedBody.render(☃);
         this.bipedRightArm.render(☃);
         this.bipedLeftArm.render(☃);
         this.bipedRightLeg.render(☃);
         this.bipedLeftLeg.render(☃);
         this.bipedHeadwear.render(☃);
      } else {
         if (☃.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
         }

         this.bipedHead.render(☃);
         this.bipedBody.render(☃);
         this.bipedRightArm.render(☃);
         this.bipedLeftArm.render(☃);
         this.bipedRightLeg.render(☃);
         this.bipedLeftLeg.render(☃);
         this.bipedHeadwear.render(☃);
      }

      GlStateManager.popMatrix();
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      boolean ☃ = ☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).getTicksElytraFlying() > 4;
      this.bipedHead.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      if (☃) {
         this.bipedHead.rotateAngleX = (float) (-Math.PI / 4);
      } else {
         this.bipedHead.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      }

      this.bipedBody.rotateAngleY = 0.0F;
      this.bipedRightArm.rotationPointZ = 0.0F;
      this.bipedRightArm.rotationPointX = -5.0F;
      this.bipedLeftArm.rotationPointZ = 0.0F;
      this.bipedLeftArm.rotationPointX = 5.0F;
      float ☃x = 1.0F;
      if (☃) {
         ☃x = (float)(☃.motionX * ☃.motionX + ☃.motionY * ☃.motionY + ☃.motionZ * ☃.motionZ);
         ☃x /= 0.2F;
         ☃x *= ☃x * ☃x;
      }

      if (☃x < 1.0F) {
         ☃x = 1.0F;
      }

      this.bipedRightArm.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * 2.0F * ☃ * 0.5F / ☃x;
      this.bipedLeftArm.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 2.0F * ☃ * 0.5F / ☃x;
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 1.4F * ☃ / ☃x;
      this.bipedLeftLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * 1.4F * ☃ / ☃x;
      this.bipedRightLeg.rotateAngleY = 0.0F;
      this.bipedLeftLeg.rotateAngleY = 0.0F;
      this.bipedRightLeg.rotateAngleZ = 0.0F;
      this.bipedLeftLeg.rotateAngleZ = 0.0F;
      if (this.isRiding) {
         this.bipedRightArm.rotateAngleX += (float) (-Math.PI / 5);
         this.bipedLeftArm.rotateAngleX += (float) (-Math.PI / 5);
         this.bipedRightLeg.rotateAngleX = -1.4137167F;
         this.bipedRightLeg.rotateAngleY = (float) (Math.PI / 10);
         this.bipedRightLeg.rotateAngleZ = 0.07853982F;
         this.bipedLeftLeg.rotateAngleX = -1.4137167F;
         this.bipedLeftLeg.rotateAngleY = (float) (-Math.PI / 10);
         this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
      }

      this.bipedRightArm.rotateAngleY = 0.0F;
      this.bipedRightArm.rotateAngleZ = 0.0F;
      switch (this.leftArmPose) {
         case EMPTY:
            this.bipedLeftArm.rotateAngleY = 0.0F;
            break;
         case BLOCK:
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.9424779F;
            this.bipedLeftArm.rotateAngleY = (float) (Math.PI / 6);
            break;
         case ITEM:
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - (float) (Math.PI / 10);
            this.bipedLeftArm.rotateAngleY = 0.0F;
      }

      switch (this.rightArmPose) {
         case EMPTY:
            this.bipedRightArm.rotateAngleY = 0.0F;
            break;
         case BLOCK:
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.9424779F;
            this.bipedRightArm.rotateAngleY = (float) (-Math.PI / 6);
            break;
         case ITEM:
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - (float) (Math.PI / 10);
            this.bipedRightArm.rotateAngleY = 0.0F;
      }

      if (this.swingProgress > 0.0F) {
         EnumHandSide ☃xx = this.getMainHand(☃);
         ModelRenderer ☃xxx = this.getArmForSide(☃xx);
         float ☃xxxx = this.swingProgress;
         this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(☃xxxx) * (float) (Math.PI * 2)) * 0.2F;
         if (☃xx == EnumHandSide.LEFT) {
            this.bipedBody.rotateAngleY *= -1.0F;
         }

         this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedRightArm.rotateAngleY = this.bipedRightArm.rotateAngleY + this.bipedBody.rotateAngleY;
         this.bipedLeftArm.rotateAngleY = this.bipedLeftArm.rotateAngleY + this.bipedBody.rotateAngleY;
         this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX + this.bipedBody.rotateAngleY;
         ☃xxxx = 1.0F - this.swingProgress;
         ☃xxxx *= ☃xxxx;
         ☃xxxx *= ☃xxxx;
         ☃xxxx = 1.0F - ☃xxxx;
         float ☃xxxxx = MathHelper.sin(☃xxxx * (float) Math.PI);
         float ☃xxxxxx = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
         ☃xxx.rotateAngleX = (float)(☃xxx.rotateAngleX - (☃xxxxx * 1.2 + ☃xxxxxx));
         ☃xxx.rotateAngleY = ☃xxx.rotateAngleY + this.bipedBody.rotateAngleY * 2.0F;
         ☃xxx.rotateAngleZ = ☃xxx.rotateAngleZ + MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
      }

      if (this.isSneak) {
         this.bipedBody.rotateAngleX = 0.5F;
         this.bipedRightArm.rotateAngleX += 0.4F;
         this.bipedLeftArm.rotateAngleX += 0.4F;
         this.bipedRightLeg.rotationPointZ = 4.0F;
         this.bipedLeftLeg.rotationPointZ = 4.0F;
         this.bipedRightLeg.rotationPointY = 9.0F;
         this.bipedLeftLeg.rotationPointY = 9.0F;
         this.bipedHead.rotationPointY = 1.0F;
      } else {
         this.bipedBody.rotateAngleX = 0.0F;
         this.bipedRightLeg.rotationPointZ = 0.1F;
         this.bipedLeftLeg.rotationPointZ = 0.1F;
         this.bipedRightLeg.rotationPointY = 12.0F;
         this.bipedLeftLeg.rotationPointY = 12.0F;
         this.bipedHead.rotationPointY = 0.0F;
      }

      this.bipedRightArm.rotateAngleZ = this.bipedRightArm.rotateAngleZ + (MathHelper.cos(☃ * 0.09F) * 0.05F + 0.05F);
      this.bipedLeftArm.rotateAngleZ = this.bipedLeftArm.rotateAngleZ - (MathHelper.cos(☃ * 0.09F) * 0.05F + 0.05F);
      this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX + MathHelper.sin(☃ * 0.067F) * 0.05F;
      this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX - MathHelper.sin(☃ * 0.067F) * 0.05F;
      if (this.rightArmPose == ModelBiped.ArmPose.BOW_AND_ARROW) {
         this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
         this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY + 0.4F;
         this.bipedRightArm.rotateAngleX = (float) (-Math.PI / 2) + this.bipedHead.rotateAngleX;
         this.bipedLeftArm.rotateAngleX = (float) (-Math.PI / 2) + this.bipedHead.rotateAngleX;
      } else if (this.leftArmPose == ModelBiped.ArmPose.BOW_AND_ARROW) {
         this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY - 0.4F;
         this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY;
         this.bipedRightArm.rotateAngleX = (float) (-Math.PI / 2) + this.bipedHead.rotateAngleX;
         this.bipedLeftArm.rotateAngleX = (float) (-Math.PI / 2) + this.bipedHead.rotateAngleX;
      }

      copyModelAngles(this.bipedHead, this.bipedHeadwear);
   }

   @Override
   public void setModelAttributes(ModelBase var1) {
      super.setModelAttributes(☃);
      if (☃ instanceof ModelBiped) {
         ModelBiped ☃ = (ModelBiped)☃;
         this.leftArmPose = ☃.leftArmPose;
         this.rightArmPose = ☃.rightArmPose;
         this.isSneak = ☃.isSneak;
      }
   }

   public void setVisible(boolean var1) {
      this.bipedHead.showModel = ☃;
      this.bipedHeadwear.showModel = ☃;
      this.bipedBody.showModel = ☃;
      this.bipedRightArm.showModel = ☃;
      this.bipedLeftArm.showModel = ☃;
      this.bipedRightLeg.showModel = ☃;
      this.bipedLeftLeg.showModel = ☃;
   }

   public void postRenderArm(float var1, EnumHandSide var2) {
      this.getArmForSide(☃).postRender(☃);
   }

   protected ModelRenderer getArmForSide(EnumHandSide var1) {
      return ☃ == EnumHandSide.LEFT ? this.bipedLeftArm : this.bipedRightArm;
   }

   protected EnumHandSide getMainHand(Entity var1) {
      if (☃ instanceof EntityLivingBase) {
         EntityLivingBase ☃ = (EntityLivingBase)☃;
         EnumHandSide ☃x = ☃.getPrimaryHand();
         return ☃.swingingHand == EnumHand.MAIN_HAND ? ☃x : ☃x.opposite();
      } else {
         return EnumHandSide.RIGHT;
      }
   }

   public static enum ArmPose {
      EMPTY,
      ITEM,
      BLOCK,
      BOW_AND_ARROW;
   }
}
