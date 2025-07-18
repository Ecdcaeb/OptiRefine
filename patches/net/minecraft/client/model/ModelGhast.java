package net.minecraft.client.model;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelGhast extends ModelBase {
   ModelRenderer body;
   ModelRenderer[] tentacles = new ModelRenderer[9];

   public ModelGhast() {
      int ☃ = -16;
      this.body = new ModelRenderer(this, 0, 0);
      this.body.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
      this.body.rotationPointY += 8.0F;
      Random ☃x = new Random(1660L);

      for (int ☃xx = 0; ☃xx < this.tentacles.length; ☃xx++) {
         this.tentacles[☃xx] = new ModelRenderer(this, 0, 0);
         float ☃xxx = ((☃xx % 3 - ☃xx / 3 % 2 * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
         float ☃xxxx = (☃xx / 3 / 2.0F * 2.0F - 1.0F) * 5.0F;
         int ☃xxxxx = ☃x.nextInt(7) + 8;
         this.tentacles[☃xx].addBox(-1.0F, 0.0F, -1.0F, 2, ☃xxxxx, 2);
         this.tentacles[☃xx].rotationPointX = ☃xxx;
         this.tentacles[☃xx].rotationPointZ = ☃xxxx;
         this.tentacles[☃xx].rotationPointY = 15.0F;
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      for (int ☃ = 0; ☃ < this.tentacles.length; ☃++) {
         this.tentacles[☃].rotateAngleX = 0.2F * MathHelper.sin(☃ * 0.3F + ☃) + 0.4F;
      }
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, 0.6F, 0.0F);
      this.body.render(☃);

      for (ModelRenderer ☃ : this.tentacles) {
         ☃.render(☃);
      }

      GlStateManager.popMatrix();
   }
}
