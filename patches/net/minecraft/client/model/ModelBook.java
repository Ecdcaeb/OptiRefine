package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelBook extends ModelBase {
   public ModelRenderer coverRight = new ModelRenderer(this).setTextureOffset(0, 0).addBox(-6.0F, -5.0F, 0.0F, 6, 10, 0);
   public ModelRenderer coverLeft = new ModelRenderer(this).setTextureOffset(16, 0).addBox(0.0F, -5.0F, 0.0F, 6, 10, 0);
   public ModelRenderer pagesRight;
   public ModelRenderer pagesLeft;
   public ModelRenderer flippingPageRight;
   public ModelRenderer flippingPageLeft;
   public ModelRenderer bookSpine = new ModelRenderer(this).setTextureOffset(12, 0).addBox(-1.0F, -5.0F, 0.0F, 2, 10, 0);

   public ModelBook() {
      this.pagesRight = new ModelRenderer(this).setTextureOffset(0, 10).addBox(0.0F, -4.0F, -0.99F, 5, 8, 1);
      this.pagesLeft = new ModelRenderer(this).setTextureOffset(12, 10).addBox(0.0F, -4.0F, -0.01F, 5, 8, 1);
      this.flippingPageRight = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0F, -4.0F, 0.0F, 5, 8, 0);
      this.flippingPageLeft = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0F, -4.0F, 0.0F, 5, 8, 0);
      this.coverRight.setRotationPoint(0.0F, 0.0F, -1.0F);
      this.coverLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
      this.bookSpine.rotateAngleY = (float) (Math.PI / 2);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.coverRight.render(☃);
      this.coverLeft.render(☃);
      this.bookSpine.render(☃);
      this.pagesRight.render(☃);
      this.pagesLeft.render(☃);
      this.flippingPageRight.render(☃);
      this.flippingPageLeft.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      float ☃ = (MathHelper.sin(☃ * 0.02F) * 0.1F + 1.25F) * ☃;
      this.coverRight.rotateAngleY = (float) Math.PI + ☃;
      this.coverLeft.rotateAngleY = -☃;
      this.pagesRight.rotateAngleY = ☃;
      this.pagesLeft.rotateAngleY = -☃;
      this.flippingPageRight.rotateAngleY = ☃ - ☃ * 2.0F * ☃;
      this.flippingPageLeft.rotateAngleY = ☃ - ☃ * 2.0F * ☃;
      this.pagesRight.rotationPointX = MathHelper.sin(☃);
      this.pagesLeft.rotationPointX = MathHelper.sin(☃);
      this.flippingPageRight.rotationPointX = MathHelper.sin(☃);
      this.flippingPageLeft.rotationPointX = MathHelper.sin(☃);
   }
}
