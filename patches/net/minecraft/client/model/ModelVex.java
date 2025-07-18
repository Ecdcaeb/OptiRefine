package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelVex extends ModelBiped {
   protected ModelRenderer leftWing;
   protected ModelRenderer rightWing;

   public ModelVex() {
      this(0.0F);
   }

   public ModelVex(float var1) {
      super(☃, 0.0F, 64, 64);
      this.bipedLeftLeg.showModel = false;
      this.bipedHeadwear.showModel = false;
      this.bipedRightLeg = new ModelRenderer(this, 32, 0);
      this.bipedRightLeg.addBox(-1.0F, -1.0F, -2.0F, 6, 10, 4, 0.0F);
      this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
      this.rightWing = new ModelRenderer(this, 0, 32);
      this.rightWing.addBox(-20.0F, 0.0F, 0.0F, 20, 12, 1);
      this.leftWing = new ModelRenderer(this, 0, 32);
      this.leftWing.mirror = true;
      this.leftWing.addBox(0.0F, 0.0F, 0.0F, 20, 12, 1);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      super.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.rightWing.render(☃);
      this.leftWing.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      EntityVex ☃ = (EntityVex)☃;
      if (☃.isCharging()) {
         if (☃.getPrimaryHand() == EnumHandSide.RIGHT) {
            this.bipedRightArm.rotateAngleX = 3.7699115F;
         } else {
            this.bipedLeftArm.rotateAngleX = 3.7699115F;
         }
      }

      this.bipedRightLeg.rotateAngleX += (float) (Math.PI / 5);
      this.rightWing.rotationPointZ = 2.0F;
      this.leftWing.rotationPointZ = 2.0F;
      this.rightWing.rotationPointY = 1.0F;
      this.leftWing.rotationPointY = 1.0F;
      this.rightWing.rotateAngleY = 0.47123894F + MathHelper.cos(☃ * 0.8F) * (float) Math.PI * 0.05F;
      this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
      this.leftWing.rotateAngleZ = -0.47123894F;
      this.leftWing.rotateAngleX = 0.47123894F;
      this.rightWing.rotateAngleX = 0.47123894F;
      this.rightWing.rotateAngleZ = 0.47123894F;
   }

   public int getModelVersion() {
      return 23;
   }
}
