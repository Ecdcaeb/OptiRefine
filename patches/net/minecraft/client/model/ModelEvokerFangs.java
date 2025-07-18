package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelEvokerFangs extends ModelBase {
   private final ModelRenderer base = new ModelRenderer(this, 0, 0);
   private final ModelRenderer upperJaw;
   private final ModelRenderer lowerJaw;

   public ModelEvokerFangs() {
      this.base.setRotationPoint(-5.0F, 22.0F, -5.0F);
      this.base.addBox(0.0F, 0.0F, 0.0F, 10, 12, 10);
      this.upperJaw = new ModelRenderer(this, 40, 0);
      this.upperJaw.setRotationPoint(1.5F, 22.0F, -4.0F);
      this.upperJaw.addBox(0.0F, 0.0F, 0.0F, 4, 14, 8);
      this.lowerJaw = new ModelRenderer(this, 40, 0);
      this.lowerJaw.setRotationPoint(-1.5F, 22.0F, 4.0F);
      this.lowerJaw.addBox(0.0F, 0.0F, 0.0F, 4, 14, 8);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float ☃ = ☃ * 2.0F;
      if (☃ > 1.0F) {
         ☃ = 1.0F;
      }

      ☃ = 1.0F - ☃ * ☃ * ☃;
      this.upperJaw.rotateAngleZ = (float) Math.PI - ☃ * 0.35F * (float) Math.PI;
      this.lowerJaw.rotateAngleZ = (float) Math.PI + ☃ * 0.35F * (float) Math.PI;
      this.lowerJaw.rotateAngleY = (float) Math.PI;
      float ☃x = (☃ + MathHelper.sin(☃ * 2.7F)) * 0.6F * 12.0F;
      this.upperJaw.rotationPointY = 24.0F - ☃x;
      this.lowerJaw.rotationPointY = this.upperJaw.rotationPointY;
      this.base.rotationPointY = this.upperJaw.rotationPointY;
      this.base.render(☃);
      this.upperJaw.render(☃);
      this.lowerJaw.render(☃);
   }
}
