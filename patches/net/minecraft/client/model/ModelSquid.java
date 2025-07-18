package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSquid extends ModelBase {
   ModelRenderer squidBody;
   ModelRenderer[] squidTentacles = new ModelRenderer[8];

   public ModelSquid() {
      int ☃ = -16;
      this.squidBody = new ModelRenderer(this, 0, 0);
      this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
      this.squidBody.rotationPointY += 8.0F;

      for (int ☃x = 0; ☃x < this.squidTentacles.length; ☃x++) {
         this.squidTentacles[☃x] = new ModelRenderer(this, 48, 0);
         double ☃xx = ☃x * Math.PI * 2.0 / this.squidTentacles.length;
         float ☃xxx = (float)Math.cos(☃xx) * 5.0F;
         float ☃xxxx = (float)Math.sin(☃xx) * 5.0F;
         this.squidTentacles[☃x].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
         this.squidTentacles[☃x].rotationPointX = ☃xxx;
         this.squidTentacles[☃x].rotationPointZ = ☃xxxx;
         this.squidTentacles[☃x].rotationPointY = 15.0F;
         ☃xx = ☃x * Math.PI * -2.0 / this.squidTentacles.length + (Math.PI / 2);
         this.squidTentacles[☃x].rotateAngleY = (float)☃xx;
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      for (ModelRenderer ☃ : this.squidTentacles) {
         ☃.rotateAngleX = ☃;
      }
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.squidBody.render(☃);

      for (ModelRenderer ☃ : this.squidTentacles) {
         ☃.render(☃);
      }
   }
}
