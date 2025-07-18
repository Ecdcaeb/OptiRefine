package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelSkeleton extends ModelBiped {
   public ModelSkeleton() {
      this(0.0F, false);
   }

   public ModelSkeleton(float var1, boolean var2) {
      super(☃, 0.0F, 64, 32);
      if (!☃) {
         this.bipedRightArm = new ModelRenderer(this, 40, 16);
         this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, ☃);
         this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
         this.bipedLeftArm = new ModelRenderer(this, 40, 16);
         this.bipedLeftArm.mirror = true;
         this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, ☃);
         this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
         this.bipedRightLeg = new ModelRenderer(this, 0, 16);
         this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, ☃);
         this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
         this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
         this.bipedLeftLeg.mirror = true;
         this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, ☃);
         this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
      }
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      this.rightArmPose = ModelBiped.ArmPose.EMPTY;
      this.leftArmPose = ModelBiped.ArmPose.EMPTY;
      ItemStack ☃ = ☃.getHeldItem(EnumHand.MAIN_HAND);
      if (☃.getItem() == Items.BOW && ((AbstractSkeleton)☃).isSwingingArms()) {
         if (☃.getPrimaryHand() == EnumHandSide.RIGHT) {
            this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
         } else {
            this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
         }
      }

      super.setLivingAnimations(☃, ☃, ☃, ☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      ItemStack ☃ = ((EntityLivingBase)☃).getHeldItemMainhand();
      AbstractSkeleton ☃x = (AbstractSkeleton)☃;
      if (☃x.isSwingingArms() && (☃.isEmpty() || ☃.getItem() != Items.BOW)) {
         float ☃xx = MathHelper.sin(this.swingProgress * (float) Math.PI);
         float ☃xxx = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
         this.bipedRightArm.rotateAngleZ = 0.0F;
         this.bipedLeftArm.rotateAngleZ = 0.0F;
         this.bipedRightArm.rotateAngleY = -(0.1F - ☃xx * 0.6F);
         this.bipedLeftArm.rotateAngleY = 0.1F - ☃xx * 0.6F;
         this.bipedRightArm.rotateAngleX = (float) (-Math.PI / 2);
         this.bipedLeftArm.rotateAngleX = (float) (-Math.PI / 2);
         this.bipedRightArm.rotateAngleX -= ☃xx * 1.2F - ☃xxx * 0.4F;
         this.bipedLeftArm.rotateAngleX -= ☃xx * 1.2F - ☃xxx * 0.4F;
         this.bipedRightArm.rotateAngleZ = this.bipedRightArm.rotateAngleZ + (MathHelper.cos(☃ * 0.09F) * 0.05F + 0.05F);
         this.bipedLeftArm.rotateAngleZ = this.bipedLeftArm.rotateAngleZ - (MathHelper.cos(☃ * 0.09F) * 0.05F + 0.05F);
         this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX + MathHelper.sin(☃ * 0.067F) * 0.05F;
         this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX - MathHelper.sin(☃ * 0.067F) * 0.05F;
      }
   }

   @Override
   public void postRenderArm(float var1, EnumHandSide var2) {
      float ☃ = ☃ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
      ModelRenderer ☃x = this.getArmForSide(☃);
      ☃x.rotationPointX += ☃;
      ☃x.postRender(☃);
      ☃x.rotationPointX -= ☃;
   }
}
