package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSnowMan extends ModelBase {
   public ModelRenderer body;
   public ModelRenderer bottomBody;
   public ModelRenderer head;
   public ModelRenderer rightHand;
   public ModelRenderer leftHand;

   public ModelSnowMan() {
      float ☃ = 4.0F;
      float ☃x = 0.0F;
      this.head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, -0.5F);
      this.head.setRotationPoint(0.0F, 4.0F, 0.0F);
      this.rightHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
      this.rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
      this.rightHand.setRotationPoint(0.0F, 6.0F, 0.0F);
      this.leftHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
      this.leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
      this.leftHand.setRotationPoint(0.0F, 6.0F, 0.0F);
      this.body = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
      this.body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, -0.5F);
      this.body.setRotationPoint(0.0F, 13.0F, 0.0F);
      this.bottomBody = new ModelRenderer(this, 0, 36).setTextureSize(64, 64);
      this.bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, -0.5F);
      this.bottomBody.setRotationPoint(0.0F, 24.0F, 0.0F);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.head.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.head.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.body.rotateAngleY = ☃ * (float) (Math.PI / 180.0) * 0.25F;
      float ☃ = MathHelper.sin(this.body.rotateAngleY);
      float ☃x = MathHelper.cos(this.body.rotateAngleY);
      this.rightHand.rotateAngleZ = 1.0F;
      this.leftHand.rotateAngleZ = -1.0F;
      this.rightHand.rotateAngleY = 0.0F + this.body.rotateAngleY;
      this.leftHand.rotateAngleY = (float) Math.PI + this.body.rotateAngleY;
      this.rightHand.rotationPointX = ☃x * 5.0F;
      this.rightHand.rotationPointZ = -☃ * 5.0F;
      this.leftHand.rotationPointX = -☃x * 5.0F;
      this.leftHand.rotationPointZ = ☃ * 5.0F;
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.body.render(☃);
      this.bottomBody.render(☃);
      this.head.render(☃);
      this.rightHand.render(☃);
      this.leftHand.render(☃);
   }
}
