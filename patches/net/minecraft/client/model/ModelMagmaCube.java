package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;

public class ModelMagmaCube extends ModelBase {
   ModelRenderer[] segments = new ModelRenderer[8];
   ModelRenderer core;

   public ModelMagmaCube() {
      for (int ☃ = 0; ☃ < this.segments.length; ☃++) {
         int ☃x = 0;
         int ☃xx = ☃;
         if (☃ == 2) {
            ☃x = 24;
            ☃xx = 10;
         } else if (☃ == 3) {
            ☃x = 24;
            ☃xx = 19;
         }

         this.segments[☃] = new ModelRenderer(this, ☃x, ☃xx);
         this.segments[☃].addBox(-4.0F, 16 + ☃, -4.0F, 8, 1, 8);
      }

      this.core = new ModelRenderer(this, 0, 16);
      this.core.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      EntityMagmaCube ☃ = (EntityMagmaCube)☃;
      float ☃x = ☃.prevSquishFactor + (☃.squishFactor - ☃.prevSquishFactor) * ☃;
      if (☃x < 0.0F) {
         ☃x = 0.0F;
      }

      for (int ☃xx = 0; ☃xx < this.segments.length; ☃xx++) {
         this.segments[☃xx].rotationPointY = -(4 - ☃xx) * ☃x * 1.7F;
      }
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.core.render(☃);

      for (ModelRenderer ☃ : this.segments) {
         ☃.render(☃);
      }
   }
}
