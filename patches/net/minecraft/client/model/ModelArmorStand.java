package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumHandSide;

public class ModelArmorStand extends ModelArmorStandArmor {
   public ModelRenderer standRightSide;
   public ModelRenderer standLeftSide;
   public ModelRenderer standWaist;
   public ModelRenderer standBase;

   public ModelArmorStand() {
      this(0.0F);
   }

   public ModelArmorStand(float var1) {
      super(☃, 64, 64);
      this.bipedHead = new ModelRenderer(this, 0, 0);
      this.bipedHead.addBox(-1.0F, -7.0F, -1.0F, 2, 7, 2, ☃);
      this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedBody = new ModelRenderer(this, 0, 26);
      this.bipedBody.addBox(-6.0F, 0.0F, -1.5F, 12, 3, 3, ☃);
      this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedRightArm = new ModelRenderer(this, 24, 0);
      this.bipedRightArm.addBox(-2.0F, -2.0F, -1.0F, 2, 12, 2, ☃);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
      this.bipedLeftArm = new ModelRenderer(this, 32, 16);
      this.bipedLeftArm.mirror = true;
      this.bipedLeftArm.addBox(0.0F, -2.0F, -1.0F, 2, 12, 2, ☃);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
      this.bipedRightLeg = new ModelRenderer(this, 8, 0);
      this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, ☃);
      this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
      this.bipedLeftLeg = new ModelRenderer(this, 40, 16);
      this.bipedLeftLeg.mirror = true;
      this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, ☃);
      this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
      this.standRightSide = new ModelRenderer(this, 16, 0);
      this.standRightSide.addBox(-3.0F, 3.0F, -1.0F, 2, 7, 2, ☃);
      this.standRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.standRightSide.showModel = true;
      this.standLeftSide = new ModelRenderer(this, 48, 16);
      this.standLeftSide.addBox(1.0F, 3.0F, -1.0F, 2, 7, 2, ☃);
      this.standLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.standWaist = new ModelRenderer(this, 0, 48);
      this.standWaist.addBox(-4.0F, 10.0F, -1.0F, 8, 2, 2, ☃);
      this.standWaist.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.standBase = new ModelRenderer(this, 0, 32);
      this.standBase.addBox(-6.0F, 11.0F, -6.0F, 12, 1, 12, ☃);
      this.standBase.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.bipedHeadwear.showModel = false;
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      if (☃ instanceof EntityArmorStand) {
         EntityArmorStand ☃ = (EntityArmorStand)☃;
         this.bipedLeftArm.showModel = ☃.getShowArms();
         this.bipedRightArm.showModel = ☃.getShowArms();
         this.standBase.showModel = !☃.hasNoBasePlate();
         this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
         this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
         this.standRightSide.rotateAngleX = (float) (Math.PI / 180.0) * ☃.getBodyRotation().getX();
         this.standRightSide.rotateAngleY = (float) (Math.PI / 180.0) * ☃.getBodyRotation().getY();
         this.standRightSide.rotateAngleZ = (float) (Math.PI / 180.0) * ☃.getBodyRotation().getZ();
         this.standLeftSide.rotateAngleX = (float) (Math.PI / 180.0) * ☃.getBodyRotation().getX();
         this.standLeftSide.rotateAngleY = (float) (Math.PI / 180.0) * ☃.getBodyRotation().getY();
         this.standLeftSide.rotateAngleZ = (float) (Math.PI / 180.0) * ☃.getBodyRotation().getZ();
         this.standWaist.rotateAngleX = (float) (Math.PI / 180.0) * ☃.getBodyRotation().getX();
         this.standWaist.rotateAngleY = (float) (Math.PI / 180.0) * ☃.getBodyRotation().getY();
         this.standWaist.rotateAngleZ = (float) (Math.PI / 180.0) * ☃.getBodyRotation().getZ();
         this.standBase.rotateAngleX = 0.0F;
         this.standBase.rotateAngleY = (float) (Math.PI / 180.0) * -☃.rotationYaw;
         this.standBase.rotateAngleZ = 0.0F;
      }
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      super.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      GlStateManager.pushMatrix();
      if (this.isChild) {
         float ☃ = 2.0F;
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         GlStateManager.translate(0.0F, 24.0F * ☃, 0.0F);
         this.standRightSide.render(☃);
         this.standLeftSide.render(☃);
         this.standWaist.render(☃);
         this.standBase.render(☃);
      } else {
         if (☃.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
         }

         this.standRightSide.render(☃);
         this.standLeftSide.render(☃);
         this.standWaist.render(☃);
         this.standBase.render(☃);
      }

      GlStateManager.popMatrix();
   }

   @Override
   public void postRenderArm(float var1, EnumHandSide var2) {
      ModelRenderer ☃ = this.getArmForSide(☃);
      boolean ☃x = ☃.showModel;
      ☃.showModel = true;
      super.postRenderArm(☃, ☃);
      ☃.showModel = ☃x;
   }
}
