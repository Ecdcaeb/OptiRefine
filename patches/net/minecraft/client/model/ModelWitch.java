package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelWitch extends ModelVillager {
   public boolean holdingItem;
   private final ModelRenderer mole = new ModelRenderer(this).setTextureSize(64, 128);
   private final ModelRenderer witchHat;

   public ModelWitch(float var1) {
      super(☃, 0.0F, 64, 128);
      this.mole.setRotationPoint(0.0F, -2.0F, 0.0F);
      this.mole.setTextureOffset(0, 0).addBox(0.0F, 3.0F, -6.75F, 1, 1, 1, -0.25F);
      this.villagerNose.addChild(this.mole);
      this.witchHat = new ModelRenderer(this).setTextureSize(64, 128);
      this.witchHat.setRotationPoint(-5.0F, -10.03125F, -5.0F);
      this.witchHat.setTextureOffset(0, 64).addBox(0.0F, 0.0F, 0.0F, 10, 2, 10);
      this.villagerHead.addChild(this.witchHat);
      ModelRenderer ☃ = new ModelRenderer(this).setTextureSize(64, 128);
      ☃.setRotationPoint(1.75F, -4.0F, 2.0F);
      ☃.setTextureOffset(0, 76).addBox(0.0F, 0.0F, 0.0F, 7, 4, 7);
      ☃.rotateAngleX = -0.05235988F;
      ☃.rotateAngleZ = 0.02617994F;
      this.witchHat.addChild(☃);
      ModelRenderer ☃x = new ModelRenderer(this).setTextureSize(64, 128);
      ☃x.setRotationPoint(1.75F, -4.0F, 2.0F);
      ☃x.setTextureOffset(0, 87).addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
      ☃x.rotateAngleX = -0.10471976F;
      ☃x.rotateAngleZ = 0.05235988F;
      ☃.addChild(☃x);
      ModelRenderer ☃xx = new ModelRenderer(this).setTextureSize(64, 128);
      ☃xx.setRotationPoint(1.75F, -2.0F, 2.0F);
      ☃xx.setTextureOffset(0, 95).addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.25F);
      ☃xx.rotateAngleX = (float) (-Math.PI / 15);
      ☃xx.rotateAngleZ = 0.10471976F;
      ☃x.addChild(☃xx);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.villagerNose.offsetX = 0.0F;
      this.villagerNose.offsetY = 0.0F;
      this.villagerNose.offsetZ = 0.0F;
      float ☃ = 0.01F * (☃.getEntityId() % 10);
      this.villagerNose.rotateAngleX = MathHelper.sin(☃.ticksExisted * ☃) * 4.5F * (float) (Math.PI / 180.0);
      this.villagerNose.rotateAngleY = 0.0F;
      this.villagerNose.rotateAngleZ = MathHelper.cos(☃.ticksExisted * ☃) * 2.5F * (float) (Math.PI / 180.0);
      if (this.holdingItem) {
         this.villagerNose.rotateAngleX = -0.9F;
         this.villagerNose.offsetZ = -0.09375F;
         this.villagerNose.offsetY = 0.1875F;
      }
   }
}
