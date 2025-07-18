package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPolarBear;

public class ModelPolarBear extends ModelQuadruped {
   public ModelPolarBear() {
      super(12, 0.0F);
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-3.5F, -3.0F, -3.0F, 7, 7, 7, 0.0F);
      this.head.setRotationPoint(0.0F, 10.0F, -16.0F);
      this.head.setTextureOffset(0, 44).addBox(-2.5F, 1.0F, -6.0F, 5, 3, 3, 0.0F);
      this.head.setTextureOffset(26, 0).addBox(-4.5F, -4.0F, -1.0F, 2, 2, 1, 0.0F);
      ModelRenderer ☃ = this.head.setTextureOffset(26, 0);
      ☃.mirror = true;
      ☃.addBox(2.5F, -4.0F, -1.0F, 2, 2, 1, 0.0F);
      this.body = new ModelRenderer(this);
      this.body.setTextureOffset(0, 19).addBox(-5.0F, -13.0F, -7.0F, 14, 14, 11, 0.0F);
      this.body.setTextureOffset(39, 0).addBox(-4.0F, -25.0F, -7.0F, 12, 12, 10, 0.0F);
      this.body.setRotationPoint(-2.0F, 9.0F, 12.0F);
      int ☃x = 10;
      this.leg1 = new ModelRenderer(this, 50, 22);
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 8, 0.0F);
      this.leg1.setRotationPoint(-3.5F, 14.0F, 6.0F);
      this.leg2 = new ModelRenderer(this, 50, 22);
      this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 8, 0.0F);
      this.leg2.setRotationPoint(3.5F, 14.0F, 6.0F);
      this.leg3 = new ModelRenderer(this, 50, 40);
      this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 6, 0.0F);
      this.leg3.setRotationPoint(-2.5F, 14.0F, -7.0F);
      this.leg4 = new ModelRenderer(this, 50, 40);
      this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 6, 0.0F);
      this.leg4.setRotationPoint(2.5F, 14.0F, -7.0F);
      this.leg1.rotationPointX--;
      this.leg2.rotationPointX++;
      this.leg1.rotationPointZ += 0.0F;
      this.leg2.rotationPointZ += 0.0F;
      this.leg3.rotationPointX--;
      this.leg4.rotationPointX++;
      this.leg3.rotationPointZ--;
      this.leg4.rotationPointZ--;
      this.childZOffset += 2.0F;
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      if (this.isChild) {
         float ☃ = 2.0F;
         this.childYOffset = 16.0F;
         this.childZOffset = 4.0F;
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.6666667F, 0.6666667F, 0.6666667F);
         GlStateManager.translate(0.0F, this.childYOffset * ☃, this.childZOffset * ☃);
         this.head.render(☃);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         GlStateManager.translate(0.0F, 24.0F * ☃, 0.0F);
         this.body.render(☃);
         this.leg1.render(☃);
         this.leg2.render(☃);
         this.leg3.render(☃);
         this.leg4.render(☃);
         GlStateManager.popMatrix();
      } else {
         this.head.render(☃);
         this.body.render(☃);
         this.leg1.render(☃);
         this.leg2.render(☃);
         this.leg3.render(☃);
         this.leg4.render(☃);
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      float ☃ = ☃ - ☃.ticksExisted;
      float ☃x = ((EntityPolarBear)☃).getStandingAnimationScale(☃);
      ☃x *= ☃x;
      float ☃xx = 1.0F - ☃x;
      this.body.rotateAngleX = (float) (Math.PI / 2) - ☃x * (float) Math.PI * 0.35F;
      this.body.rotationPointY = 9.0F * ☃xx + 11.0F * ☃x;
      this.leg3.rotationPointY = 14.0F * ☃xx + -6.0F * ☃x;
      this.leg3.rotationPointZ = -8.0F * ☃xx + -4.0F * ☃x;
      this.leg3.rotateAngleX -= ☃x * (float) Math.PI * 0.45F;
      this.leg4.rotationPointY = this.leg3.rotationPointY;
      this.leg4.rotationPointZ = this.leg3.rotationPointZ;
      this.leg4.rotateAngleX -= ☃x * (float) Math.PI * 0.45F;
      this.head.rotationPointY = 10.0F * ☃xx + -12.0F * ☃x;
      this.head.rotationPointZ = -16.0F * ☃xx + -3.0F * ☃x;
      this.head.rotateAngleX += ☃x * (float) Math.PI * 0.15F;
   }
}
