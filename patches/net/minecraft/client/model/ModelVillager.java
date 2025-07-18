package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelVillager extends ModelBase {
   public ModelRenderer villagerHead;
   public ModelRenderer villagerBody;
   public ModelRenderer villagerArms;
   public ModelRenderer rightVillagerLeg;
   public ModelRenderer leftVillagerLeg;
   public ModelRenderer villagerNose;

   public ModelVillager(float var1) {
      this(☃, 0.0F, 64, 64);
   }

   public ModelVillager(float var1, float var2, int var3, int var4) {
      this.villagerHead = new ModelRenderer(this).setTextureSize(☃, ☃);
      this.villagerHead.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
      this.villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, ☃);
      this.villagerNose = new ModelRenderer(this).setTextureSize(☃, ☃);
      this.villagerNose.setRotationPoint(0.0F, ☃ - 2.0F, 0.0F);
      this.villagerNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, ☃);
      this.villagerHead.addChild(this.villagerNose);
      this.villagerBody = new ModelRenderer(this).setTextureSize(☃, ☃);
      this.villagerBody.setRotationPoint(0.0F, 0.0F + ☃, 0.0F);
      this.villagerBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, ☃);
      this.villagerBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, ☃ + 0.5F);
      this.villagerArms = new ModelRenderer(this).setTextureSize(☃, ☃);
      this.villagerArms.setRotationPoint(0.0F, 0.0F + ☃ + 2.0F, 0.0F);
      this.villagerArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, ☃);
      this.villagerArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, ☃);
      this.villagerArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, ☃);
      this.rightVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(☃, ☃);
      this.rightVillagerLeg.setRotationPoint(-2.0F, 12.0F + ☃, 0.0F);
      this.rightVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃);
      this.leftVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(☃, ☃);
      this.leftVillagerLeg.mirror = true;
      this.leftVillagerLeg.setRotationPoint(2.0F, 12.0F + ☃, 0.0F);
      this.leftVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, ☃);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.villagerHead.render(☃);
      this.villagerBody.render(☃);
      this.rightVillagerLeg.render(☃);
      this.leftVillagerLeg.render(☃);
      this.villagerArms.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.villagerHead.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.villagerHead.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.villagerArms.rotationPointY = 3.0F;
      this.villagerArms.rotationPointZ = -1.0F;
      this.villagerArms.rotateAngleX = -0.75F;
      this.rightVillagerLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F) * 1.4F * ☃ * 0.5F;
      this.leftVillagerLeg.rotateAngleX = MathHelper.cos(☃ * 0.6662F + (float) Math.PI) * 1.4F * ☃ * 0.5F;
      this.rightVillagerLeg.rotateAngleY = 0.0F;
      this.leftVillagerLeg.rotateAngleY = 0.0F;
   }
}
