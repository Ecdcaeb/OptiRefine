package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelMinecart extends ModelBase {
   public ModelRenderer[] sideModels = new ModelRenderer[7];

   public ModelMinecart() {
      this.sideModels[0] = new ModelRenderer(this, 0, 10);
      this.sideModels[1] = new ModelRenderer(this, 0, 0);
      this.sideModels[2] = new ModelRenderer(this, 0, 0);
      this.sideModels[3] = new ModelRenderer(this, 0, 0);
      this.sideModels[4] = new ModelRenderer(this, 0, 0);
      this.sideModels[5] = new ModelRenderer(this, 44, 10);
      int ☃ = 20;
      int ☃x = 8;
      int ☃xx = 16;
      int ☃xxx = 4;
      this.sideModels[0].addBox(-10.0F, -8.0F, -1.0F, 20, 16, 2, 0.0F);
      this.sideModels[0].setRotationPoint(0.0F, 4.0F, 0.0F);
      this.sideModels[5].addBox(-9.0F, -7.0F, -1.0F, 18, 14, 1, 0.0F);
      this.sideModels[5].setRotationPoint(0.0F, 4.0F, 0.0F);
      this.sideModels[1].addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
      this.sideModels[1].setRotationPoint(-9.0F, 4.0F, 0.0F);
      this.sideModels[2].addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
      this.sideModels[2].setRotationPoint(9.0F, 4.0F, 0.0F);
      this.sideModels[3].addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
      this.sideModels[3].setRotationPoint(0.0F, 4.0F, -7.0F);
      this.sideModels[4].addBox(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
      this.sideModels[4].setRotationPoint(0.0F, 4.0F, 7.0F);
      this.sideModels[0].rotateAngleX = (float) (Math.PI / 2);
      this.sideModels[1].rotateAngleY = (float) (Math.PI * 3.0 / 2.0);
      this.sideModels[2].rotateAngleY = (float) (Math.PI / 2);
      this.sideModels[3].rotateAngleY = (float) Math.PI;
      this.sideModels[5].rotateAngleX = (float) (-Math.PI / 2);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.sideModels[5].rotationPointY = 4.0F - ☃;

      for (int ☃ = 0; ☃ < 6; ☃++) {
         this.sideModels[☃].render(☃);
      }
   }
}
