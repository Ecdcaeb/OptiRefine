package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSilverfish extends ModelBase {
   private final ModelRenderer[] silverfishBodyParts;
   private final ModelRenderer[] silverfishWings;
   private final float[] zPlacement = new float[7];
   private static final int[][] SILVERFISH_BOX_LENGTH = new int[][]{{3, 2, 2}, {4, 3, 2}, {6, 4, 3}, {3, 3, 3}, {2, 2, 3}, {2, 1, 2}, {1, 1, 2}};
   private static final int[][] SILVERFISH_TEXTURE_POSITIONS = new int[][]{{0, 0}, {0, 4}, {0, 9}, {0, 16}, {0, 22}, {11, 0}, {13, 4}};

   public ModelSilverfish() {
      this.silverfishBodyParts = new ModelRenderer[7];
      float ☃ = -3.5F;

      for (int ☃x = 0; ☃x < this.silverfishBodyParts.length; ☃x++) {
         this.silverfishBodyParts[☃x] = new ModelRenderer(this, SILVERFISH_TEXTURE_POSITIONS[☃x][0], SILVERFISH_TEXTURE_POSITIONS[☃x][1]);
         this.silverfishBodyParts[☃x]
            .addBox(
               SILVERFISH_BOX_LENGTH[☃x][0] * -0.5F,
               0.0F,
               SILVERFISH_BOX_LENGTH[☃x][2] * -0.5F,
               SILVERFISH_BOX_LENGTH[☃x][0],
               SILVERFISH_BOX_LENGTH[☃x][1],
               SILVERFISH_BOX_LENGTH[☃x][2]
            );
         this.silverfishBodyParts[☃x].setRotationPoint(0.0F, 24 - SILVERFISH_BOX_LENGTH[☃x][1], ☃);
         this.zPlacement[☃x] = ☃;
         if (☃x < this.silverfishBodyParts.length - 1) {
            ☃ += (SILVERFISH_BOX_LENGTH[☃x][2] + SILVERFISH_BOX_LENGTH[☃x + 1][2]) * 0.5F;
         }
      }

      this.silverfishWings = new ModelRenderer[3];
      this.silverfishWings[0] = new ModelRenderer(this, 20, 0);
      this.silverfishWings[0].addBox(-5.0F, 0.0F, SILVERFISH_BOX_LENGTH[2][2] * -0.5F, 10, 8, SILVERFISH_BOX_LENGTH[2][2]);
      this.silverfishWings[0].setRotationPoint(0.0F, 16.0F, this.zPlacement[2]);
      this.silverfishWings[1] = new ModelRenderer(this, 20, 11);
      this.silverfishWings[1].addBox(-3.0F, 0.0F, SILVERFISH_BOX_LENGTH[4][2] * -0.5F, 6, 4, SILVERFISH_BOX_LENGTH[4][2]);
      this.silverfishWings[1].setRotationPoint(0.0F, 20.0F, this.zPlacement[4]);
      this.silverfishWings[2] = new ModelRenderer(this, 20, 18);
      this.silverfishWings[2].addBox(-3.0F, 0.0F, SILVERFISH_BOX_LENGTH[4][2] * -0.5F, 6, 5, SILVERFISH_BOX_LENGTH[1][2]);
      this.silverfishWings[2].setRotationPoint(0.0F, 19.0F, this.zPlacement[1]);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);

      for (ModelRenderer ☃ : this.silverfishBodyParts) {
         ☃.render(☃);
      }

      for (ModelRenderer ☃ : this.silverfishWings) {
         ☃.render(☃);
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      for (int ☃ = 0; ☃ < this.silverfishBodyParts.length; ☃++) {
         this.silverfishBodyParts[☃].rotateAngleY = MathHelper.cos(☃ * 0.9F + ☃ * 0.15F * (float) Math.PI) * (float) Math.PI * 0.05F * (1 + Math.abs(☃ - 2));
         this.silverfishBodyParts[☃].rotationPointX = MathHelper.sin(☃ * 0.9F + ☃ * 0.15F * (float) Math.PI) * (float) Math.PI * 0.2F * Math.abs(☃ - 2);
      }

      this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
      this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
      this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
      this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
      this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
   }
}
