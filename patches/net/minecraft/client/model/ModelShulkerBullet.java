package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelShulkerBullet extends ModelBase {
   public ModelRenderer renderer;

   public ModelShulkerBullet() {
      this.textureWidth = 64;
      this.textureHeight = 32;
      this.renderer = new ModelRenderer(this);
      this.renderer.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -1.0F, 8, 8, 2, 0.0F);
      this.renderer.setTextureOffset(0, 10).addBox(-1.0F, -4.0F, -4.0F, 2, 8, 8, 0.0F);
      this.renderer.setTextureOffset(20, 0).addBox(-4.0F, -1.0F, -4.0F, 8, 2, 8, 0.0F);
      this.renderer.setRotationPoint(0.0F, 0.0F, 0.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.renderer.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.renderer.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.renderer.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
   }
}
