package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelLeashKnot extends ModelBase {
   public ModelRenderer knotRenderer;

   public ModelLeashKnot() {
      this(0, 0, 32, 32);
   }

   public ModelLeashKnot(int var1, int var2, int var3, int var4) {
      this.textureWidth = ☃;
      this.textureHeight = ☃;
      this.knotRenderer = new ModelRenderer(this, ☃, ☃);
      this.knotRenderer.addBox(-3.0F, -6.0F, -3.0F, 6, 8, 6, 0.0F);
      this.knotRenderer.setRotationPoint(0.0F, 0.0F, 0.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.knotRenderer.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.knotRenderer.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.knotRenderer.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
   }
}
