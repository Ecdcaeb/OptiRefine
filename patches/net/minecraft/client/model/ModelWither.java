package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.math.MathHelper;

public class ModelWither extends ModelBase {
   private final ModelRenderer[] upperBodyParts;
   private final ModelRenderer[] heads;

   public ModelWither(float var1) {
      this.textureWidth = 64;
      this.textureHeight = 64;
      this.upperBodyParts = new ModelRenderer[3];
      this.upperBodyParts[0] = new ModelRenderer(this, 0, 16);
      this.upperBodyParts[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, ☃);
      this.upperBodyParts[1] = new ModelRenderer(this).setTextureSize(this.textureWidth, this.textureHeight);
      this.upperBodyParts[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
      this.upperBodyParts[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3, ☃);
      this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, ☃);
      this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, ☃);
      this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, ☃);
      this.upperBodyParts[2] = new ModelRenderer(this, 12, 22);
      this.upperBodyParts[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, ☃);
      this.heads = new ModelRenderer[3];
      this.heads[0] = new ModelRenderer(this, 0, 0);
      this.heads[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, ☃);
      this.heads[1] = new ModelRenderer(this, 32, 0);
      this.heads[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, ☃);
      this.heads[1].rotationPointX = -8.0F;
      this.heads[1].rotationPointY = 4.0F;
      this.heads[2] = new ModelRenderer(this, 32, 0);
      this.heads[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, ☃);
      this.heads[2].rotationPointX = 10.0F;
      this.heads[2].rotationPointY = 4.0F;
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);

      for (ModelRenderer ☃ : this.heads) {
         ☃.render(☃);
      }

      for (ModelRenderer ☃ : this.upperBodyParts) {
         ☃.render(☃);
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      float ☃ = MathHelper.cos(☃ * 0.1F);
      this.upperBodyParts[1].rotateAngleX = (0.065F + 0.05F * ☃) * (float) Math.PI;
      this.upperBodyParts[2]
         .setRotationPoint(
            -2.0F, 6.9F + MathHelper.cos(this.upperBodyParts[1].rotateAngleX) * 10.0F, -0.5F + MathHelper.sin(this.upperBodyParts[1].rotateAngleX) * 10.0F
         );
      this.upperBodyParts[2].rotateAngleX = (0.265F + 0.1F * ☃) * (float) Math.PI;
      this.heads[0].rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.heads[0].rotateAngleX = ☃ * (float) (Math.PI / 180.0);
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      EntityWither ☃ = (EntityWither)☃;

      for (int ☃x = 1; ☃x < 3; ☃x++) {
         this.heads[☃x].rotateAngleY = (☃.getHeadYRotation(☃x - 1) - ☃.renderYawOffset) * (float) (Math.PI / 180.0);
         this.heads[☃x].rotateAngleX = ☃.getHeadXRotation(☃x - 1) * (float) (Math.PI / 180.0);
      }
   }
}
