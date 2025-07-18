package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.MathHelper;

public class ModelWolf extends ModelBase {
   public ModelRenderer wolfHeadMain;
   public ModelRenderer wolfBody;
   public ModelRenderer wolfLeg1;
   public ModelRenderer wolfLeg2;
   public ModelRenderer wolfLeg3;
   public ModelRenderer wolfLeg4;
   ModelRenderer wolfTail;
   ModelRenderer wolfMane;

   public ModelWolf() {
      float ☃ = 0.0F;
      float ☃x = 13.5F;
      this.wolfHeadMain = new ModelRenderer(this, 0, 0);
      this.wolfHeadMain.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, 0.0F);
      this.wolfHeadMain.setRotationPoint(-1.0F, 13.5F, -7.0F);
      this.wolfBody = new ModelRenderer(this, 18, 14);
      this.wolfBody.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F);
      this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
      this.wolfMane = new ModelRenderer(this, 21, 0);
      this.wolfMane.addBox(-3.0F, -3.0F, -3.0F, 8, 6, 7, 0.0F);
      this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
      this.wolfLeg1 = new ModelRenderer(this, 0, 18);
      this.wolfLeg1.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
      this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
      this.wolfLeg2 = new ModelRenderer(this, 0, 18);
      this.wolfLeg2.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
      this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
      this.wolfLeg3 = new ModelRenderer(this, 0, 18);
      this.wolfLeg3.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
      this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
      this.wolfLeg4 = new ModelRenderer(this, 0, 18);
      this.wolfLeg4.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
      this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
      this.wolfTail = new ModelRenderer(this, 9, 18);
      this.wolfTail.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
      this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
      this.wolfHeadMain.setTextureOffset(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
      this.wolfHeadMain.setTextureOffset(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
      this.wolfHeadMain.setTextureOffset(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, 0.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      super.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      if (this.isChild) {
         float ☃ = 2.0F;
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, 5.0F * ☃, 2.0F * ☃);
         this.wolfHeadMain.renderWithRotation(☃);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         GlStateManager.translate(0.0F, 24.0F * ☃, 0.0F);
         this.wolfBody.render(☃);
         this.wolfLeg1.render(☃);
         this.wolfLeg2.render(☃);
         this.wolfLeg3.render(☃);
         this.wolfLeg4.render(☃);
         this.wolfTail.renderWithRotation(☃);
         this.wolfMane.render(☃);
         GlStateManager.popMatrix();
      } else {
         this.wolfHeadMain.renderWithRotation(☃);
         this.wolfBody.render(☃);
         this.wolfLeg1.render(☃);
         this.wolfLeg2.render(☃);
         this.wolfLeg3.render(☃);
         this.wolfLeg4.render(☃);
         this.wolfTail.renderWithRotation(☃);
         this.wolfMane.render(☃);
      }
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      EntityWolf ☃ = (EntityWolf)☃;
      if (☃.isAngry()) {
         this.wolfTail.rotateAngleY = 0.0F;
      } else {
         this.wolfTail.rotateAngleY = MathHelper.cos(☃ * 0.6662F) * 1.4F * ☃;
      }

      if (☃.isSitting()) {
         this.wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
         this.wolfMane.rotateAngleX = (float) (Math.PI * 2.0 / 5.0);
         this.wolfMane.rotateAngleY = 0.0F;
         this.wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
         this.wolfBody.rotateAngleX = (float) (Math.PI / 4);
         this.wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
         this.wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
         this.wolfLeg1.rotateAngleX = (float) (Math.PI * 3.0 / 2.0);
         this.wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
         this.wolfLeg2.rotateAngleX = (float) (Math.PI * 3.0 / 2.0);
         this.wolfLeg3.rotateAngleX = 5.811947F;
         this.wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
         this.wolfLeg4.rotateAngleX = 5.811947F;
         this.wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
      } else {
         this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
         this.wolfBody.rotateAngleX = (float) (Math.PI / 2);
         this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
         this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
         this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
         this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
         this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
         this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
         this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
         this.wolfLeg1.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 1.4F * ☃;
         this.wolfLeg2.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * 1.4F * ☃;
         this.wolfLeg3.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * 1.4F * ☃;
         this.wolfLeg4.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 1.4F * ☃;
      }

      this.wolfHeadMain.rotateAngleZ = ☃.getInterestedAngle(☃) + ☃.getShakeAngle(☃, 0.0F);
      this.wolfMane.rotateAngleZ = ☃.getShakeAngle(☃, -0.08F);
      this.wolfBody.rotateAngleZ = ☃.getShakeAngle(☃, -0.16F);
      this.wolfTail.rotateAngleZ = ☃.getShakeAngle(☃, -0.2F);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.wolfHeadMain.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.wolfHeadMain.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.wolfTail.rotateAngleX = ☃;
   }
}
