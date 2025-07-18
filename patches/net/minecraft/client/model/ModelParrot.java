package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.util.math.MathHelper;

public class ModelParrot extends ModelBase {
   ModelRenderer body;
   ModelRenderer tail;
   ModelRenderer wingLeft;
   ModelRenderer wingRight;
   ModelRenderer head;
   ModelRenderer head2;
   ModelRenderer beak1;
   ModelRenderer beak2;
   ModelRenderer feather;
   ModelRenderer legLeft;
   ModelRenderer legRight;
   private ModelParrot.State state = ModelParrot.State.STANDING;

   public ModelParrot() {
      this.textureWidth = 32;
      this.textureHeight = 32;
      this.body = new ModelRenderer(this, 2, 8);
      this.body.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3);
      this.body.setRotationPoint(0.0F, 16.5F, -3.0F);
      this.tail = new ModelRenderer(this, 22, 1);
      this.tail.addBox(-1.5F, -1.0F, -1.0F, 3, 4, 1);
      this.tail.setRotationPoint(0.0F, 21.07F, 1.16F);
      this.wingLeft = new ModelRenderer(this, 19, 8);
      this.wingLeft.addBox(-0.5F, 0.0F, -1.5F, 1, 5, 3);
      this.wingLeft.setRotationPoint(1.5F, 16.94F, -2.76F);
      this.wingRight = new ModelRenderer(this, 19, 8);
      this.wingRight.addBox(-0.5F, 0.0F, -1.5F, 1, 5, 3);
      this.wingRight.setRotationPoint(-1.5F, 16.94F, -2.76F);
      this.head = new ModelRenderer(this, 2, 2);
      this.head.addBox(-1.0F, -1.5F, -1.0F, 2, 3, 2);
      this.head.setRotationPoint(0.0F, 15.69F, -2.76F);
      this.head2 = new ModelRenderer(this, 10, 0);
      this.head2.addBox(-1.0F, -0.5F, -2.0F, 2, 1, 4);
      this.head2.setRotationPoint(0.0F, -2.0F, -1.0F);
      this.head.addChild(this.head2);
      this.beak1 = new ModelRenderer(this, 11, 7);
      this.beak1.addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1);
      this.beak1.setRotationPoint(0.0F, -0.5F, -1.5F);
      this.head.addChild(this.beak1);
      this.beak2 = new ModelRenderer(this, 16, 7);
      this.beak2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1);
      this.beak2.setRotationPoint(0.0F, -1.75F, -2.45F);
      this.head.addChild(this.beak2);
      this.feather = new ModelRenderer(this, 2, 18);
      this.feather.addBox(0.0F, -4.0F, -2.0F, 0, 5, 4);
      this.feather.setRotationPoint(0.0F, -2.15F, 0.15F);
      this.head.addChild(this.feather);
      this.legLeft = new ModelRenderer(this, 14, 18);
      this.legLeft.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1);
      this.legLeft.setRotationPoint(1.0F, 22.0F, -1.05F);
      this.legRight = new ModelRenderer(this, 14, 18);
      this.legRight.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1);
      this.legRight.setRotationPoint(-1.0F, 22.0F, -1.05F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.body.render(☃);
      this.wingLeft.render(☃);
      this.wingRight.render(☃);
      this.tail.render(☃);
      this.head.render(☃);
      this.legLeft.render(☃);
      this.legRight.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      float ☃ = ☃ * 0.3F;
      this.head.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.head.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.head.rotateAngleZ = 0.0F;
      this.head.rotationPointX = 0.0F;
      this.body.rotationPointX = 0.0F;
      this.tail.rotationPointX = 0.0F;
      this.wingRight.rotationPointX = -1.5F;
      this.wingLeft.rotationPointX = 1.5F;
      if (this.state != ModelParrot.State.FLYING) {
         if (this.state == ModelParrot.State.SITTING) {
            return;
         }

         if (this.state == ModelParrot.State.PARTY) {
            float ☃x = MathHelper.cos(☃.ticksExisted);
            float ☃xx = MathHelper.sin(☃.ticksExisted);
            this.head.rotationPointX = ☃x;
            this.head.rotationPointY = 15.69F + ☃xx;
            this.head.rotateAngleX = 0.0F;
            this.head.rotateAngleY = 0.0F;
            this.head.rotateAngleZ = MathHelper.sin(☃.ticksExisted) * 0.4F;
            this.body.rotationPointX = ☃x;
            this.body.rotationPointY = 16.5F + ☃xx;
            this.wingLeft.rotateAngleZ = -0.0873F - ☃;
            this.wingLeft.rotationPointX = 1.5F + ☃x;
            this.wingLeft.rotationPointY = 16.94F + ☃xx;
            this.wingRight.rotateAngleZ = 0.0873F + ☃;
            this.wingRight.rotationPointX = -1.5F + ☃x;
            this.wingRight.rotationPointY = 16.94F + ☃xx;
            this.tail.rotationPointX = ☃x;
            this.tail.rotationPointY = 21.07F + ☃xx;
            return;
         }

         this.legLeft.rotateAngleX = this.legLeft.rotateAngleX + MathHelper.cos(☃ * 0.6662F) * 1.4F * ☃;
         this.legRight.rotateAngleX = this.legRight.rotateAngleX + MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * 1.4F * ☃;
      }

      this.head.rotationPointY = 15.69F + ☃;
      this.tail.rotateAngleX = 1.015F + MathHelper.cos(☃ * 0.6662F) * 0.3F * ☃;
      this.tail.rotationPointY = 21.07F + ☃;
      this.body.rotationPointY = 16.5F + ☃;
      this.wingLeft.rotateAngleZ = -0.0873F - ☃;
      this.wingLeft.rotationPointY = 16.94F + ☃;
      this.wingRight.rotateAngleZ = 0.0873F + ☃;
      this.wingRight.rotationPointY = 16.94F + ☃;
      this.legLeft.rotationPointY = 22.0F + ☃;
      this.legRight.rotationPointY = 22.0F + ☃;
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      this.feather.rotateAngleX = -0.2214F;
      this.body.rotateAngleX = 0.4937F;
      this.wingLeft.rotateAngleX = -0.6981F;
      this.wingLeft.rotateAngleY = (float) -Math.PI;
      this.wingRight.rotateAngleX = -0.6981F;
      this.wingRight.rotateAngleY = (float) -Math.PI;
      this.legLeft.rotateAngleX = -0.0299F;
      this.legRight.rotateAngleX = -0.0299F;
      this.legLeft.rotationPointY = 22.0F;
      this.legRight.rotationPointY = 22.0F;
      if (☃ instanceof EntityParrot) {
         EntityParrot ☃ = (EntityParrot)☃;
         if (☃.isPartying()) {
            this.legLeft.rotateAngleZ = (float) (-Math.PI / 9);
            this.legRight.rotateAngleZ = (float) (Math.PI / 9);
            this.state = ModelParrot.State.PARTY;
            return;
         }

         if (☃.isSitting()) {
            float ☃x = 1.9F;
            this.head.rotationPointY = 17.59F;
            this.tail.rotateAngleX = 1.5388988F;
            this.tail.rotationPointY = 22.97F;
            this.body.rotationPointY = 18.4F;
            this.wingLeft.rotateAngleZ = -0.0873F;
            this.wingLeft.rotationPointY = 18.84F;
            this.wingRight.rotateAngleZ = 0.0873F;
            this.wingRight.rotationPointY = 18.84F;
            this.legLeft.rotationPointY++;
            this.legRight.rotationPointY++;
            this.legLeft.rotateAngleX++;
            this.legRight.rotateAngleX++;
            this.state = ModelParrot.State.SITTING;
         } else if (☃.isFlying()) {
            this.legLeft.rotateAngleX += (float) (Math.PI * 2.0 / 9.0);
            this.legRight.rotateAngleX += (float) (Math.PI * 2.0 / 9.0);
            this.state = ModelParrot.State.FLYING;
         } else {
            this.state = ModelParrot.State.STANDING;
         }

         this.legLeft.rotateAngleZ = 0.0F;
         this.legRight.rotateAngleZ = 0.0F;
      }
   }

   static enum State {
      FLYING,
      STANDING,
      SITTING,
      PARTY;
   }
}
