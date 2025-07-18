package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.math.MathHelper;

public class ModelOcelot extends ModelBase {
   private final ModelRenderer ocelotBackLeftLeg;
   private final ModelRenderer ocelotBackRightLeg;
   private final ModelRenderer ocelotFrontLeftLeg;
   private final ModelRenderer ocelotFrontRightLeg;
   private final ModelRenderer ocelotTail;
   private final ModelRenderer ocelotTail2;
   private final ModelRenderer ocelotHead;
   private final ModelRenderer ocelotBody;
   private int state = 1;

   public ModelOcelot() {
      this.setTextureOffset("head.main", 0, 0);
      this.setTextureOffset("head.nose", 0, 24);
      this.setTextureOffset("head.ear1", 0, 10);
      this.setTextureOffset("head.ear2", 6, 10);
      this.ocelotHead = new ModelRenderer(this, "head");
      this.ocelotHead.addBox("main", -2.5F, -2.0F, -3.0F, 5, 4, 5);
      this.ocelotHead.addBox("nose", -1.5F, 0.0F, -4.0F, 3, 2, 2);
      this.ocelotHead.addBox("ear1", -2.0F, -3.0F, 0.0F, 1, 1, 2);
      this.ocelotHead.addBox("ear2", 1.0F, -3.0F, 0.0F, 1, 1, 2);
      this.ocelotHead.setRotationPoint(0.0F, 15.0F, -9.0F);
      this.ocelotBody = new ModelRenderer(this, 20, 0);
      this.ocelotBody.addBox(-2.0F, 3.0F, -8.0F, 4, 16, 6, 0.0F);
      this.ocelotBody.setRotationPoint(0.0F, 12.0F, -10.0F);
      this.ocelotTail = new ModelRenderer(this, 0, 15);
      this.ocelotTail.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
      this.ocelotTail.rotateAngleX = 0.9F;
      this.ocelotTail.setRotationPoint(0.0F, 15.0F, 8.0F);
      this.ocelotTail2 = new ModelRenderer(this, 4, 15);
      this.ocelotTail2.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
      this.ocelotTail2.setRotationPoint(0.0F, 20.0F, 14.0F);
      this.ocelotBackLeftLeg = new ModelRenderer(this, 8, 13);
      this.ocelotBackLeftLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
      this.ocelotBackLeftLeg.setRotationPoint(1.1F, 18.0F, 5.0F);
      this.ocelotBackRightLeg = new ModelRenderer(this, 8, 13);
      this.ocelotBackRightLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
      this.ocelotBackRightLeg.setRotationPoint(-1.1F, 18.0F, 5.0F);
      this.ocelotFrontLeftLeg = new ModelRenderer(this, 40, 0);
      this.ocelotFrontLeftLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
      this.ocelotFrontLeftLeg.setRotationPoint(1.2F, 13.8F, -5.0F);
      this.ocelotFrontRightLeg = new ModelRenderer(this, 40, 0);
      this.ocelotFrontRightLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
      this.ocelotFrontRightLeg.setRotationPoint(-1.2F, 13.8F, -5.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      if (this.isChild) {
         float ☃ = 2.0F;
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.75F, 0.75F, 0.75F);
         GlStateManager.translate(0.0F, 10.0F * ☃, 4.0F * ☃);
         this.ocelotHead.render(☃);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         GlStateManager.translate(0.0F, 24.0F * ☃, 0.0F);
         this.ocelotBody.render(☃);
         this.ocelotBackLeftLeg.render(☃);
         this.ocelotBackRightLeg.render(☃);
         this.ocelotFrontLeftLeg.render(☃);
         this.ocelotFrontRightLeg.render(☃);
         this.ocelotTail.render(☃);
         this.ocelotTail2.render(☃);
         GlStateManager.popMatrix();
      } else {
         this.ocelotHead.render(☃);
         this.ocelotBody.render(☃);
         this.ocelotTail.render(☃);
         this.ocelotTail2.render(☃);
         this.ocelotBackLeftLeg.render(☃);
         this.ocelotBackRightLeg.render(☃);
         this.ocelotFrontLeftLeg.render(☃);
         this.ocelotFrontRightLeg.render(☃);
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.ocelotHead.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.ocelotHead.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      if (this.state != 3) {
         this.ocelotBody.rotateAngleX = (float) (Math.PI / 2);
         if (this.state == 2) {
            this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * ☃;
            this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F + 0.3F) * ☃;
            this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI + 0.3F) * ☃;
            this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * ☃;
            this.ocelotTail2.rotateAngleX = 1.7278761F + (float) (Math.PI / 10) * MathHelper.cos(☃) * ☃;
         } else {
            this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * ☃;
            this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * ☃;
            this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * ☃;
            this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * ☃;
            if (this.state == 1) {
               this.ocelotTail2.rotateAngleX = 1.7278761F + (float) (Math.PI / 4) * MathHelper.cos(☃) * ☃;
            } else {
               this.ocelotTail2.rotateAngleX = 1.7278761F + 0.47123894F * MathHelper.cos(☃) * ☃;
            }
         }
      }
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      EntityOcelot ☃ = (EntityOcelot)☃;
      this.ocelotBody.rotationPointY = 12.0F;
      this.ocelotBody.rotationPointZ = -10.0F;
      this.ocelotHead.rotationPointY = 15.0F;
      this.ocelotHead.rotationPointZ = -9.0F;
      this.ocelotTail.rotationPointY = 15.0F;
      this.ocelotTail.rotationPointZ = 8.0F;
      this.ocelotTail2.rotationPointY = 20.0F;
      this.ocelotTail2.rotationPointZ = 14.0F;
      this.ocelotFrontLeftLeg.rotationPointY = 13.8F;
      this.ocelotFrontLeftLeg.rotationPointZ = -5.0F;
      this.ocelotFrontRightLeg.rotationPointY = 13.8F;
      this.ocelotFrontRightLeg.rotationPointZ = -5.0F;
      this.ocelotBackLeftLeg.rotationPointY = 18.0F;
      this.ocelotBackLeftLeg.rotationPointZ = 5.0F;
      this.ocelotBackRightLeg.rotationPointY = 18.0F;
      this.ocelotBackRightLeg.rotationPointZ = 5.0F;
      this.ocelotTail.rotateAngleX = 0.9F;
      if (☃.isSneaking()) {
         this.ocelotBody.rotationPointY++;
         this.ocelotHead.rotationPointY += 2.0F;
         this.ocelotTail.rotationPointY++;
         this.ocelotTail2.rotationPointY += -4.0F;
         this.ocelotTail2.rotationPointZ += 2.0F;
         this.ocelotTail.rotateAngleX = (float) (Math.PI / 2);
         this.ocelotTail2.rotateAngleX = (float) (Math.PI / 2);
         this.state = 0;
      } else if (☃.isSprinting()) {
         this.ocelotTail2.rotationPointY = this.ocelotTail.rotationPointY;
         this.ocelotTail2.rotationPointZ += 2.0F;
         this.ocelotTail.rotateAngleX = (float) (Math.PI / 2);
         this.ocelotTail2.rotateAngleX = (float) (Math.PI / 2);
         this.state = 2;
      } else if (☃.isSitting()) {
         this.ocelotBody.rotateAngleX = (float) (Math.PI / 4);
         this.ocelotBody.rotationPointY += -4.0F;
         this.ocelotBody.rotationPointZ += 5.0F;
         this.ocelotHead.rotationPointY += -3.3F;
         this.ocelotHead.rotationPointZ++;
         this.ocelotTail.rotationPointY += 8.0F;
         this.ocelotTail.rotationPointZ += -2.0F;
         this.ocelotTail2.rotationPointY += 2.0F;
         this.ocelotTail2.rotationPointZ += -0.8F;
         this.ocelotTail.rotateAngleX = 1.7278761F;
         this.ocelotTail2.rotateAngleX = 2.670354F;
         this.ocelotFrontLeftLeg.rotateAngleX = (float) (-Math.PI / 20);
         this.ocelotFrontLeftLeg.rotationPointY = 15.8F;
         this.ocelotFrontLeftLeg.rotationPointZ = -7.0F;
         this.ocelotFrontRightLeg.rotateAngleX = (float) (-Math.PI / 20);
         this.ocelotFrontRightLeg.rotationPointY = 15.8F;
         this.ocelotFrontRightLeg.rotationPointZ = -7.0F;
         this.ocelotBackLeftLeg.rotateAngleX = (float) (-Math.PI / 2);
         this.ocelotBackLeftLeg.rotationPointY = 21.0F;
         this.ocelotBackLeftLeg.rotationPointZ = 1.0F;
         this.ocelotBackRightLeg.rotateAngleX = (float) (-Math.PI / 2);
         this.ocelotBackRightLeg.rotationPointY = 21.0F;
         this.ocelotBackRightLeg.rotationPointZ = 1.0F;
         this.state = 3;
      } else {
         this.state = 1;
      }
   }
}
