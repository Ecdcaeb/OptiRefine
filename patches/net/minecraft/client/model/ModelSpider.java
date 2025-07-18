package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSpider extends ModelBase {
   public ModelRenderer spiderHead;
   public ModelRenderer spiderNeck;
   public ModelRenderer spiderBody;
   public ModelRenderer spiderLeg1;
   public ModelRenderer spiderLeg2;
   public ModelRenderer spiderLeg3;
   public ModelRenderer spiderLeg4;
   public ModelRenderer spiderLeg5;
   public ModelRenderer spiderLeg6;
   public ModelRenderer spiderLeg7;
   public ModelRenderer spiderLeg8;

   public ModelSpider() {
      float ☃ = 0.0F;
      int ☃x = 15;
      this.spiderHead = new ModelRenderer(this, 32, 4);
      this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
      this.spiderHead.setRotationPoint(0.0F, 15.0F, -3.0F);
      this.spiderNeck = new ModelRenderer(this, 0, 0);
      this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
      this.spiderNeck.setRotationPoint(0.0F, 15.0F, 0.0F);
      this.spiderBody = new ModelRenderer(this, 0, 12);
      this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, 0.0F);
      this.spiderBody.setRotationPoint(0.0F, 15.0F, 9.0F);
      this.spiderLeg1 = new ModelRenderer(this, 18, 0);
      this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg1.setRotationPoint(-4.0F, 15.0F, 2.0F);
      this.spiderLeg2 = new ModelRenderer(this, 18, 0);
      this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg2.setRotationPoint(4.0F, 15.0F, 2.0F);
      this.spiderLeg3 = new ModelRenderer(this, 18, 0);
      this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg3.setRotationPoint(-4.0F, 15.0F, 1.0F);
      this.spiderLeg4 = new ModelRenderer(this, 18, 0);
      this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg4.setRotationPoint(4.0F, 15.0F, 1.0F);
      this.spiderLeg5 = new ModelRenderer(this, 18, 0);
      this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg5.setRotationPoint(-4.0F, 15.0F, 0.0F);
      this.spiderLeg6 = new ModelRenderer(this, 18, 0);
      this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg6.setRotationPoint(4.0F, 15.0F, 0.0F);
      this.spiderLeg7 = new ModelRenderer(this, 18, 0);
      this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg7.setRotationPoint(-4.0F, 15.0F, -1.0F);
      this.spiderLeg8 = new ModelRenderer(this, 18, 0);
      this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg8.setRotationPoint(4.0F, 15.0F, -1.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.spiderHead.render(☃);
      this.spiderNeck.render(☃);
      this.spiderBody.render(☃);
      this.spiderLeg1.render(☃);
      this.spiderLeg2.render(☃);
      this.spiderLeg3.render(☃);
      this.spiderLeg4.render(☃);
      this.spiderLeg5.render(☃);
      this.spiderLeg6.render(☃);
      this.spiderLeg7.render(☃);
      this.spiderLeg8.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.spiderHead.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.spiderHead.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      float ☃ = (float) (Math.PI / 4);
      this.spiderLeg1.rotateAngleZ = (float) (-Math.PI / 4);
      this.spiderLeg2.rotateAngleZ = (float) (Math.PI / 4);
      this.spiderLeg3.rotateAngleZ = -0.58119464F;
      this.spiderLeg4.rotateAngleZ = 0.58119464F;
      this.spiderLeg5.rotateAngleZ = -0.58119464F;
      this.spiderLeg6.rotateAngleZ = 0.58119464F;
      this.spiderLeg7.rotateAngleZ = (float) (-Math.PI / 4);
      this.spiderLeg8.rotateAngleZ = (float) (Math.PI / 4);
      float ☃x = -0.0F;
      float ☃xx = (float) (Math.PI / 8);
      this.spiderLeg1.rotateAngleY = (float) (Math.PI / 4);
      this.spiderLeg2.rotateAngleY = (float) (-Math.PI / 4);
      this.spiderLeg3.rotateAngleY = (float) (Math.PI / 8);
      this.spiderLeg4.rotateAngleY = (float) (-Math.PI / 8);
      this.spiderLeg5.rotateAngleY = (float) (-Math.PI / 8);
      this.spiderLeg6.rotateAngleY = (float) (Math.PI / 8);
      this.spiderLeg7.rotateAngleY = (float) (-Math.PI / 4);
      this.spiderLeg8.rotateAngleY = (float) (Math.PI / 4);
      float ☃xxx = -(MathHelper.cos(☃ * 0.6662F * 2.0F + 0.0F) * 0.4F) * ☃;
      float ☃xxxx = -(MathHelper.cos(☃ * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * ☃;
      float ☃xxxxx = -(MathHelper.cos(☃ * 0.6662F * 2.0F + (float) (Math.PI / 2)) * 0.4F) * ☃;
      float ☃xxxxxx = -(MathHelper.cos(☃ * 0.6662F * 2.0F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * ☃;
      float ☃xxxxxxx = Math.abs(MathHelper.sin(☃ * 0.6662F + 0.0F) * 0.4F) * ☃;
      float ☃xxxxxxxx = Math.abs(MathHelper.sin(☃ * 0.6662F + (float) Math.PI) * 0.4F) * ☃;
      float ☃xxxxxxxxx = Math.abs(MathHelper.sin(☃ * 0.6662F + (float) (Math.PI / 2)) * 0.4F) * ☃;
      float ☃xxxxxxxxxx = Math.abs(MathHelper.sin(☃ * 0.6662F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * ☃;
      this.spiderLeg1.rotateAngleY += ☃xxx;
      this.spiderLeg2.rotateAngleY += -☃xxx;
      this.spiderLeg3.rotateAngleY += ☃xxxx;
      this.spiderLeg4.rotateAngleY += -☃xxxx;
      this.spiderLeg5.rotateAngleY += ☃xxxxx;
      this.spiderLeg6.rotateAngleY += -☃xxxxx;
      this.spiderLeg7.rotateAngleY += ☃xxxxxx;
      this.spiderLeg8.rotateAngleY += -☃xxxxxx;
      this.spiderLeg1.rotateAngleZ += ☃xxxxxxx;
      this.spiderLeg2.rotateAngleZ += -☃xxxxxxx;
      this.spiderLeg3.rotateAngleZ += ☃xxxxxxxx;
      this.spiderLeg4.rotateAngleZ += -☃xxxxxxxx;
      this.spiderLeg5.rotateAngleZ += ☃xxxxxxxxx;
      this.spiderLeg6.rotateAngleZ += -☃xxxxxxxxx;
      this.spiderLeg7.rotateAngleZ += ☃xxxxxxxxxx;
      this.spiderLeg8.rotateAngleZ += -☃xxxxxxxxxx;
   }
}
