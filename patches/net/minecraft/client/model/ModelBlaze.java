package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelBlaze extends ModelBase {
   private final ModelRenderer[] blazeSticks = new ModelRenderer[12];
   private final ModelRenderer blazeHead;

   public ModelBlaze() {
      for (int ☃ = 0; ☃ < this.blazeSticks.length; ☃++) {
         this.blazeSticks[☃] = new ModelRenderer(this, 0, 16);
         this.blazeSticks[☃].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
      }

      this.blazeHead = new ModelRenderer(this, 0, 0);
      this.blazeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.blazeHead.render(☃);

      for (ModelRenderer ☃ : this.blazeSticks) {
         ☃.render(☃);
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      float ☃ = ☃ * (float) Math.PI * -0.1F;

      for (int ☃x = 0; ☃x < 4; ☃x++) {
         this.blazeSticks[☃x].rotationPointY = -2.0F + MathHelper.cos((☃x * 2 + ☃) * 0.25F);
         this.blazeSticks[☃x].rotationPointX = MathHelper.cos(☃) * 9.0F;
         this.blazeSticks[☃x].rotationPointZ = MathHelper.sin(☃) * 9.0F;
         ☃++;
      }

      ☃ = (float) (Math.PI / 4) + ☃ * (float) Math.PI * 0.03F;

      for (int ☃x = 4; ☃x < 8; ☃x++) {
         this.blazeSticks[☃x].rotationPointY = 2.0F + MathHelper.cos((☃x * 2 + ☃) * 0.25F);
         this.blazeSticks[☃x].rotationPointX = MathHelper.cos(☃) * 7.0F;
         this.blazeSticks[☃x].rotationPointZ = MathHelper.sin(☃) * 7.0F;
         ☃++;
      }

      ☃ = 0.47123894F + ☃ * (float) Math.PI * -0.05F;

      for (int ☃x = 8; ☃x < 12; ☃x++) {
         this.blazeSticks[☃x].rotationPointY = 11.0F + MathHelper.cos((☃x * 1.5F + ☃) * 0.5F);
         this.blazeSticks[☃x].rotationPointX = MathHelper.cos(☃) * 5.0F;
         this.blazeSticks[☃x].rotationPointZ = MathHelper.sin(☃) * 5.0F;
         ☃++;
      }

      this.blazeHead.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.blazeHead.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
   }
}
