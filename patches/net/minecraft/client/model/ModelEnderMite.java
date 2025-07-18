package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelEnderMite extends ModelBase {
   private static final int[][] BODY_SIZES = new int[][]{{4, 3, 2}, {6, 4, 5}, {3, 3, 1}, {1, 2, 1}};
   private static final int[][] BODY_TEXS = new int[][]{{0, 0}, {0, 5}, {0, 14}, {0, 18}};
   private static final int BODY_COUNT = BODY_SIZES.length;
   private final ModelRenderer[] bodyParts = new ModelRenderer[BODY_COUNT];

   public ModelEnderMite() {
      float ☃ = -3.5F;

      for (int ☃x = 0; ☃x < this.bodyParts.length; ☃x++) {
         this.bodyParts[☃x] = new ModelRenderer(this, BODY_TEXS[☃x][0], BODY_TEXS[☃x][1]);
         this.bodyParts[☃x].addBox(BODY_SIZES[☃x][0] * -0.5F, 0.0F, BODY_SIZES[☃x][2] * -0.5F, BODY_SIZES[☃x][0], BODY_SIZES[☃x][1], BODY_SIZES[☃x][2]);
         this.bodyParts[☃x].setRotationPoint(0.0F, 24 - BODY_SIZES[☃x][1], ☃);
         if (☃x < this.bodyParts.length - 1) {
            ☃ += (BODY_SIZES[☃x][2] + BODY_SIZES[☃x + 1][2]) * 0.5F;
         }
      }
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);

      for (ModelRenderer ☃ : this.bodyParts) {
         ☃.render(☃);
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      for (int ☃ = 0; ☃ < this.bodyParts.length; ☃++) {
         this.bodyParts[☃].rotateAngleY = MathHelper.cos(☃ * 0.9F + ☃ * 0.15F * (float) Math.PI) * (float) Math.PI * 0.01F * (1 + Math.abs(☃ - 2));
         this.bodyParts[☃].rotationPointX = MathHelper.sin(☃ * 0.9F + ☃ * 0.15F * (float) Math.PI) * (float) Math.PI * 0.1F * Math.abs(☃ - 2);
      }
   }
}
