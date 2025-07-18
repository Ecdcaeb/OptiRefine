package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.math.MathHelper;

public class ModelRabbit extends ModelBase {
   private final ModelRenderer rabbitLeftFoot;
   private final ModelRenderer rabbitRightFoot;
   private final ModelRenderer rabbitLeftThigh;
   private final ModelRenderer rabbitRightThigh;
   private final ModelRenderer rabbitBody;
   private final ModelRenderer rabbitLeftArm;
   private final ModelRenderer rabbitRightArm;
   private final ModelRenderer rabbitHead;
   private final ModelRenderer rabbitRightEar;
   private final ModelRenderer rabbitLeftEar;
   private final ModelRenderer rabbitTail;
   private final ModelRenderer rabbitNose;
   private float jumpRotation;

   public ModelRabbit() {
      this.setTextureOffset("head.main", 0, 0);
      this.setTextureOffset("head.nose", 0, 24);
      this.setTextureOffset("head.ear1", 0, 10);
      this.setTextureOffset("head.ear2", 6, 10);
      this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
      this.rabbitLeftFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
      this.rabbitLeftFoot.setRotationPoint(3.0F, 17.5F, 3.7F);
      this.rabbitLeftFoot.mirror = true;
      this.setRotationOffset(this.rabbitLeftFoot, 0.0F, 0.0F, 0.0F);
      this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
      this.rabbitRightFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
      this.rabbitRightFoot.setRotationPoint(-3.0F, 17.5F, 3.7F);
      this.rabbitRightFoot.mirror = true;
      this.setRotationOffset(this.rabbitRightFoot, 0.0F, 0.0F, 0.0F);
      this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
      this.rabbitLeftThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
      this.rabbitLeftThigh.setRotationPoint(3.0F, 17.5F, 3.7F);
      this.rabbitLeftThigh.mirror = true;
      this.setRotationOffset(this.rabbitLeftThigh, (float) (-Math.PI / 9), 0.0F, 0.0F);
      this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
      this.rabbitRightThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
      this.rabbitRightThigh.setRotationPoint(-3.0F, 17.5F, 3.7F);
      this.rabbitRightThigh.mirror = true;
      this.setRotationOffset(this.rabbitRightThigh, (float) (-Math.PI / 9), 0.0F, 0.0F);
      this.rabbitBody = new ModelRenderer(this, 0, 0);
      this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
      this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
      this.rabbitBody.mirror = true;
      this.setRotationOffset(this.rabbitBody, (float) (-Math.PI / 9), 0.0F, 0.0F);
      this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
      this.rabbitLeftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
      this.rabbitLeftArm.setRotationPoint(3.0F, 17.0F, -1.0F);
      this.rabbitLeftArm.mirror = true;
      this.setRotationOffset(this.rabbitLeftArm, (float) (-Math.PI / 18), 0.0F, 0.0F);
      this.rabbitRightArm = new ModelRenderer(this, 0, 15);
      this.rabbitRightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
      this.rabbitRightArm.setRotationPoint(-3.0F, 17.0F, -1.0F);
      this.rabbitRightArm.mirror = true;
      this.setRotationOffset(this.rabbitRightArm, (float) (-Math.PI / 18), 0.0F, 0.0F);
      this.rabbitHead = new ModelRenderer(this, 32, 0);
      this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
      this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
      this.rabbitHead.mirror = true;
      this.setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
      this.rabbitRightEar = new ModelRenderer(this, 52, 0);
      this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
      this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
      this.rabbitRightEar.mirror = true;
      this.setRotationOffset(this.rabbitRightEar, 0.0F, (float) (-Math.PI / 12), 0.0F);
      this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
      this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
      this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
      this.rabbitLeftEar.mirror = true;
      this.setRotationOffset(this.rabbitLeftEar, 0.0F, (float) (Math.PI / 12), 0.0F);
      this.rabbitTail = new ModelRenderer(this, 52, 6);
      this.rabbitTail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2);
      this.rabbitTail.setRotationPoint(0.0F, 20.0F, 7.0F);
      this.rabbitTail.mirror = true;
      this.setRotationOffset(this.rabbitTail, -0.3490659F, 0.0F, 0.0F);
      this.rabbitNose = new ModelRenderer(this, 32, 9);
      this.rabbitNose.addBox(-0.5F, -2.5F, -5.5F, 1, 1, 1);
      this.rabbitNose.setRotationPoint(0.0F, 16.0F, -1.0F);
      this.rabbitNose.mirror = true;
      this.setRotationOffset(this.rabbitNose, 0.0F, 0.0F, 0.0F);
   }

   private void setRotationOffset(ModelRenderer var1, float var2, float var3, float var4) {
      ☃.rotateAngleX = ☃;
      ☃.rotateAngleY = ☃;
      ☃.rotateAngleZ = ☃;
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      if (this.isChild) {
         float ☃ = 1.5F;
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.56666666F, 0.56666666F, 0.56666666F);
         GlStateManager.translate(0.0F, 22.0F * ☃, 2.0F * ☃);
         this.rabbitHead.render(☃);
         this.rabbitLeftEar.render(☃);
         this.rabbitRightEar.render(☃);
         this.rabbitNose.render(☃);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.4F, 0.4F, 0.4F);
         GlStateManager.translate(0.0F, 36.0F * ☃, 0.0F);
         this.rabbitLeftFoot.render(☃);
         this.rabbitRightFoot.render(☃);
         this.rabbitLeftThigh.render(☃);
         this.rabbitRightThigh.render(☃);
         this.rabbitBody.render(☃);
         this.rabbitLeftArm.render(☃);
         this.rabbitRightArm.render(☃);
         this.rabbitTail.render(☃);
         GlStateManager.popMatrix();
      } else {
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.6F, 0.6F, 0.6F);
         GlStateManager.translate(0.0F, 16.0F * ☃, 0.0F);
         this.rabbitLeftFoot.render(☃);
         this.rabbitRightFoot.render(☃);
         this.rabbitLeftThigh.render(☃);
         this.rabbitRightThigh.render(☃);
         this.rabbitBody.render(☃);
         this.rabbitLeftArm.render(☃);
         this.rabbitRightArm.render(☃);
         this.rabbitHead.render(☃);
         this.rabbitRightEar.render(☃);
         this.rabbitLeftEar.render(☃);
         this.rabbitTail.render(☃);
         this.rabbitNose.render(☃);
         GlStateManager.popMatrix();
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      float ☃ = ☃ - ☃.ticksExisted;
      EntityRabbit ☃x = (EntityRabbit)☃;
      this.rabbitNose.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.rabbitHead.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.rabbitRightEar.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.rabbitLeftEar.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.rabbitNose.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.rabbitHead.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.rabbitRightEar.rotateAngleY = this.rabbitNose.rotateAngleY - (float) (Math.PI / 12);
      this.rabbitLeftEar.rotateAngleY = this.rabbitNose.rotateAngleY + (float) (Math.PI / 12);
      this.jumpRotation = MathHelper.sin(☃x.getJumpCompletion(☃) * (float) Math.PI);
      this.rabbitLeftThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * (float) (Math.PI / 180.0);
      this.rabbitRightThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * (float) (Math.PI / 180.0);
      this.rabbitLeftFoot.rotateAngleX = this.jumpRotation * 50.0F * (float) (Math.PI / 180.0);
      this.rabbitRightFoot.rotateAngleX = this.jumpRotation * 50.0F * (float) (Math.PI / 180.0);
      this.rabbitLeftArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * (float) (Math.PI / 180.0);
      this.rabbitRightArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * (float) (Math.PI / 180.0);
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      super.setLivingAnimations(☃, ☃, ☃, ☃);
      this.jumpRotation = MathHelper.sin(((EntityRabbit)☃).getJumpCompletion(☃) * (float) Math.PI);
   }
}
