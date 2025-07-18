package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelCreeper extends ModelBase {
   public ModelRenderer head;
   public ModelRenderer creeperArmor;
   public ModelRenderer body;
   public ModelRenderer leg1;
   public ModelRenderer leg2;
   public ModelRenderer leg3;
   public ModelRenderer leg4;

   public ModelCreeper() {
      this(0.0F);
   }

   public ModelCreeper(float var1) {
      int ☃ = 6;
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, ☃);
      this.head.setRotationPoint(0.0F, 6.0F, 0.0F);
      this.creeperArmor = new ModelRenderer(this, 32, 0);
      this.creeperArmor.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, ☃ + 0.5F);
      this.creeperArmor.setRotationPoint(0.0F, 6.0F, 0.0F);
      this.body = new ModelRenderer(this, 16, 16);
      this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, ☃);
      this.body.setRotationPoint(0.0F, 6.0F, 0.0F);
      this.leg1 = new ModelRenderer(this, 0, 16);
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, ☃);
      this.leg1.setRotationPoint(-2.0F, 18.0F, 4.0F);
      this.leg2 = new ModelRenderer(this, 0, 16);
      this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, ☃);
      this.leg2.setRotationPoint(2.0F, 18.0F, 4.0F);
      this.leg3 = new ModelRenderer(this, 0, 16);
      this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, ☃);
      this.leg3.setRotationPoint(-2.0F, 18.0F, -4.0F);
      this.leg4 = new ModelRenderer(this, 0, 16);
      this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, ☃);
      this.leg4.setRotationPoint(2.0F, 18.0F, -4.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.head.render(☃);
      this.body.render(☃);
      this.leg1.render(☃);
      this.leg2.render(☃);
      this.leg3.render(☃);
      this.leg4.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.head.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.head.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.leg1.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 1.4F * ☃;
      this.leg2.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * 1.4F * ☃;
      this.leg3.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * 1.4F * ☃;
      this.leg4.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 1.4F * ☃;
   }
}
