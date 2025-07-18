package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.math.MathHelper;

public class ModelHorse extends ModelBase {
   private final ModelRenderer head;
   private final ModelRenderer upperMouth;
   private final ModelRenderer lowerMouth;
   private final ModelRenderer horseLeftEar;
   private final ModelRenderer horseRightEar;
   private final ModelRenderer muleLeftEar;
   private final ModelRenderer muleRightEar;
   private final ModelRenderer neck;
   private final ModelRenderer horseFaceRopes;
   private final ModelRenderer mane;
   private final ModelRenderer body;
   private final ModelRenderer tailBase;
   private final ModelRenderer tailMiddle;
   private final ModelRenderer tailTip;
   private final ModelRenderer backLeftLeg;
   private final ModelRenderer backLeftShin;
   private final ModelRenderer backLeftHoof;
   private final ModelRenderer backRightLeg;
   private final ModelRenderer backRightShin;
   private final ModelRenderer backRightHoof;
   private final ModelRenderer frontLeftLeg;
   private final ModelRenderer frontLeftShin;
   private final ModelRenderer frontLeftHoof;
   private final ModelRenderer frontRightLeg;
   private final ModelRenderer frontRightShin;
   private final ModelRenderer frontRightHoof;
   private final ModelRenderer muleLeftChest;
   private final ModelRenderer muleRightChest;
   private final ModelRenderer horseSaddleBottom;
   private final ModelRenderer horseSaddleFront;
   private final ModelRenderer horseSaddleBack;
   private final ModelRenderer horseLeftSaddleRope;
   private final ModelRenderer horseLeftSaddleMetal;
   private final ModelRenderer horseRightSaddleRope;
   private final ModelRenderer horseRightSaddleMetal;
   private final ModelRenderer horseLeftFaceMetal;
   private final ModelRenderer horseRightFaceMetal;
   private final ModelRenderer horseLeftRein;
   private final ModelRenderer horseRightRein;

   public ModelHorse() {
      this.textureWidth = 128;
      this.textureHeight = 128;
      this.body = new ModelRenderer(this, 0, 34);
      this.body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24);
      this.body.setRotationPoint(0.0F, 11.0F, 9.0F);
      this.tailBase = new ModelRenderer(this, 44, 0);
      this.tailBase.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3);
      this.tailBase.setRotationPoint(0.0F, 3.0F, 14.0F);
      this.tailBase.rotateAngleX = -1.134464F;
      this.tailMiddle = new ModelRenderer(this, 38, 7);
      this.tailMiddle.addBox(-1.5F, -2.0F, 3.0F, 3, 4, 7);
      this.tailMiddle.setRotationPoint(0.0F, 3.0F, 14.0F);
      this.tailMiddle.rotateAngleX = -1.134464F;
      this.tailTip = new ModelRenderer(this, 24, 3);
      this.tailTip.addBox(-1.5F, -4.5F, 9.0F, 3, 4, 7);
      this.tailTip.setRotationPoint(0.0F, 3.0F, 14.0F);
      this.tailTip.rotateAngleX = (float) (-Math.PI * 4.0 / 9.0);
      this.backLeftLeg = new ModelRenderer(this, 78, 29);
      this.backLeftLeg.addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5);
      this.backLeftLeg.setRotationPoint(4.0F, 9.0F, 11.0F);
      this.backLeftShin = new ModelRenderer(this, 78, 43);
      this.backLeftShin.addBox(-2.0F, 0.0F, -1.5F, 3, 5, 3);
      this.backLeftShin.setRotationPoint(4.0F, 16.0F, 11.0F);
      this.backLeftHoof = new ModelRenderer(this, 78, 51);
      this.backLeftHoof.addBox(-2.5F, 5.1F, -2.0F, 4, 3, 4);
      this.backLeftHoof.setRotationPoint(4.0F, 16.0F, 11.0F);
      this.backRightLeg = new ModelRenderer(this, 96, 29);
      this.backRightLeg.addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5);
      this.backRightLeg.setRotationPoint(-4.0F, 9.0F, 11.0F);
      this.backRightShin = new ModelRenderer(this, 96, 43);
      this.backRightShin.addBox(-1.0F, 0.0F, -1.5F, 3, 5, 3);
      this.backRightShin.setRotationPoint(-4.0F, 16.0F, 11.0F);
      this.backRightHoof = new ModelRenderer(this, 96, 51);
      this.backRightHoof.addBox(-1.5F, 5.1F, -2.0F, 4, 3, 4);
      this.backRightHoof.setRotationPoint(-4.0F, 16.0F, 11.0F);
      this.frontLeftLeg = new ModelRenderer(this, 44, 29);
      this.frontLeftLeg.addBox(-1.9F, -1.0F, -2.1F, 3, 8, 4);
      this.frontLeftLeg.setRotationPoint(4.0F, 9.0F, -8.0F);
      this.frontLeftShin = new ModelRenderer(this, 44, 41);
      this.frontLeftShin.addBox(-1.9F, 0.0F, -1.6F, 3, 5, 3);
      this.frontLeftShin.setRotationPoint(4.0F, 16.0F, -8.0F);
      this.frontLeftHoof = new ModelRenderer(this, 44, 51);
      this.frontLeftHoof.addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4);
      this.frontLeftHoof.setRotationPoint(4.0F, 16.0F, -8.0F);
      this.frontRightLeg = new ModelRenderer(this, 60, 29);
      this.frontRightLeg.addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4);
      this.frontRightLeg.setRotationPoint(-4.0F, 9.0F, -8.0F);
      this.frontRightShin = new ModelRenderer(this, 60, 41);
      this.frontRightShin.addBox(-1.1F, 0.0F, -1.6F, 3, 5, 3);
      this.frontRightShin.setRotationPoint(-4.0F, 16.0F, -8.0F);
      this.frontRightHoof = new ModelRenderer(this, 60, 51);
      this.frontRightHoof.addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4);
      this.frontRightHoof.setRotationPoint(-4.0F, 16.0F, -8.0F);
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-2.5F, -10.0F, -1.5F, 5, 5, 7);
      this.head.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.head.rotateAngleX = (float) (Math.PI / 6);
      this.upperMouth = new ModelRenderer(this, 24, 18);
      this.upperMouth.addBox(-2.0F, -10.0F, -7.0F, 4, 3, 6);
      this.upperMouth.setRotationPoint(0.0F, 3.95F, -10.0F);
      this.upperMouth.rotateAngleX = (float) (Math.PI / 6);
      this.lowerMouth = new ModelRenderer(this, 24, 27);
      this.lowerMouth.addBox(-2.0F, -7.0F, -6.5F, 4, 2, 5);
      this.lowerMouth.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.lowerMouth.rotateAngleX = (float) (Math.PI / 6);
      this.head.addChild(this.upperMouth);
      this.head.addChild(this.lowerMouth);
      this.horseLeftEar = new ModelRenderer(this, 0, 0);
      this.horseLeftEar.addBox(0.45F, -12.0F, 4.0F, 2, 3, 1);
      this.horseLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.horseLeftEar.rotateAngleX = (float) (Math.PI / 6);
      this.horseRightEar = new ModelRenderer(this, 0, 0);
      this.horseRightEar.addBox(-2.45F, -12.0F, 4.0F, 2, 3, 1);
      this.horseRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.horseRightEar.rotateAngleX = (float) (Math.PI / 6);
      this.muleLeftEar = new ModelRenderer(this, 0, 12);
      this.muleLeftEar.addBox(-2.0F, -16.0F, 4.0F, 2, 7, 1);
      this.muleLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.muleLeftEar.rotateAngleX = (float) (Math.PI / 6);
      this.muleLeftEar.rotateAngleZ = (float) (Math.PI / 12);
      this.muleRightEar = new ModelRenderer(this, 0, 12);
      this.muleRightEar.addBox(0.0F, -16.0F, 4.0F, 2, 7, 1);
      this.muleRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.muleRightEar.rotateAngleX = (float) (Math.PI / 6);
      this.muleRightEar.rotateAngleZ = (float) (-Math.PI / 12);
      this.neck = new ModelRenderer(this, 0, 12);
      this.neck.addBox(-2.05F, -9.8F, -2.0F, 4, 14, 8);
      this.neck.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.neck.rotateAngleX = (float) (Math.PI / 6);
      this.muleLeftChest = new ModelRenderer(this, 0, 34);
      this.muleLeftChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
      this.muleLeftChest.setRotationPoint(-7.5F, 3.0F, 10.0F);
      this.muleLeftChest.rotateAngleY = (float) (Math.PI / 2);
      this.muleRightChest = new ModelRenderer(this, 0, 47);
      this.muleRightChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
      this.muleRightChest.setRotationPoint(4.5F, 3.0F, 10.0F);
      this.muleRightChest.rotateAngleY = (float) (Math.PI / 2);
      this.horseSaddleBottom = new ModelRenderer(this, 80, 0);
      this.horseSaddleBottom.addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8);
      this.horseSaddleBottom.setRotationPoint(0.0F, 2.0F, 2.0F);
      this.horseSaddleFront = new ModelRenderer(this, 106, 9);
      this.horseSaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2);
      this.horseSaddleFront.setRotationPoint(0.0F, 2.0F, 2.0F);
      this.horseSaddleBack = new ModelRenderer(this, 80, 9);
      this.horseSaddleBack.addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2);
      this.horseSaddleBack.setRotationPoint(0.0F, 2.0F, 2.0F);
      this.horseLeftSaddleMetal = new ModelRenderer(this, 74, 0);
      this.horseLeftSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
      this.horseLeftSaddleMetal.setRotationPoint(5.0F, 3.0F, 2.0F);
      this.horseLeftSaddleRope = new ModelRenderer(this, 70, 0);
      this.horseLeftSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
      this.horseLeftSaddleRope.setRotationPoint(5.0F, 3.0F, 2.0F);
      this.horseRightSaddleMetal = new ModelRenderer(this, 74, 4);
      this.horseRightSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
      this.horseRightSaddleMetal.setRotationPoint(-5.0F, 3.0F, 2.0F);
      this.horseRightSaddleRope = new ModelRenderer(this, 80, 0);
      this.horseRightSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
      this.horseRightSaddleRope.setRotationPoint(-5.0F, 3.0F, 2.0F);
      this.horseLeftFaceMetal = new ModelRenderer(this, 74, 13);
      this.horseLeftFaceMetal.addBox(1.5F, -8.0F, -4.0F, 1, 2, 2);
      this.horseLeftFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.horseLeftFaceMetal.rotateAngleX = (float) (Math.PI / 6);
      this.horseRightFaceMetal = new ModelRenderer(this, 74, 13);
      this.horseRightFaceMetal.addBox(-2.5F, -8.0F, -4.0F, 1, 2, 2);
      this.horseRightFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.horseRightFaceMetal.rotateAngleX = (float) (Math.PI / 6);
      this.horseLeftRein = new ModelRenderer(this, 44, 10);
      this.horseLeftRein.addBox(2.6F, -6.0F, -6.0F, 0, 3, 16);
      this.horseLeftRein.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.horseRightRein = new ModelRenderer(this, 44, 5);
      this.horseRightRein.addBox(-2.6F, -6.0F, -6.0F, 0, 3, 16);
      this.horseRightRein.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.mane = new ModelRenderer(this, 58, 0);
      this.mane.addBox(-1.0F, -11.5F, 5.0F, 2, 16, 4);
      this.mane.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.mane.rotateAngleX = (float) (Math.PI / 6);
      this.horseFaceRopes = new ModelRenderer(this, 80, 12);
      this.horseFaceRopes.addBox(-2.5F, -10.1F, -7.0F, 5, 5, 12, 0.2F);
      this.horseFaceRopes.setRotationPoint(0.0F, 4.0F, -10.0F);
      this.horseFaceRopes.rotateAngleX = (float) (Math.PI / 6);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      AbstractHorse ☃ = (AbstractHorse)☃;
      float ☃x = ☃.getGrassEatingAmount(0.0F);
      boolean ☃xx = ☃.isChild();
      boolean ☃xxx = !☃xx && ☃.isHorseSaddled();
      boolean ☃xxxx = ☃ instanceof AbstractChestHorse;
      boolean ☃xxxxx = !☃xx && ☃xxxx && ((AbstractChestHorse)☃).hasChest();
      float ☃xxxxxx = ☃.getHorseSize();
      boolean ☃xxxxxxx = ☃.isBeingRidden();
      if (☃xxx) {
         this.horseFaceRopes.render(☃);
         this.horseSaddleBottom.render(☃);
         this.horseSaddleFront.render(☃);
         this.horseSaddleBack.render(☃);
         this.horseLeftSaddleRope.render(☃);
         this.horseLeftSaddleMetal.render(☃);
         this.horseRightSaddleRope.render(☃);
         this.horseRightSaddleMetal.render(☃);
         this.horseLeftFaceMetal.render(☃);
         this.horseRightFaceMetal.render(☃);
         if (☃xxxxxxx) {
            this.horseLeftRein.render(☃);
            this.horseRightRein.render(☃);
         }
      }

      if (☃xx) {
         GlStateManager.pushMatrix();
         GlStateManager.scale(☃xxxxxx, 0.5F + ☃xxxxxx * 0.5F, ☃xxxxxx);
         GlStateManager.translate(0.0F, 0.95F * (1.0F - ☃xxxxxx), 0.0F);
      }

      this.backLeftLeg.render(☃);
      this.backLeftShin.render(☃);
      this.backLeftHoof.render(☃);
      this.backRightLeg.render(☃);
      this.backRightShin.render(☃);
      this.backRightHoof.render(☃);
      this.frontLeftLeg.render(☃);
      this.frontLeftShin.render(☃);
      this.frontLeftHoof.render(☃);
      this.frontRightLeg.render(☃);
      this.frontRightShin.render(☃);
      this.frontRightHoof.render(☃);
      if (☃xx) {
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(☃xxxxxx, ☃xxxxxx, ☃xxxxxx);
         GlStateManager.translate(0.0F, 1.35F * (1.0F - ☃xxxxxx), 0.0F);
      }

      this.body.render(☃);
      this.tailBase.render(☃);
      this.tailMiddle.render(☃);
      this.tailTip.render(☃);
      this.neck.render(☃);
      this.mane.render(☃);
      if (☃xx) {
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         float ☃xxxxxxxx = 0.5F + ☃xxxxxx * ☃xxxxxx * 0.5F;
         GlStateManager.scale(☃xxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxx);
         if (☃x <= 0.0F) {
            GlStateManager.translate(0.0F, 1.35F * (1.0F - ☃xxxxxx), 0.0F);
         } else {
            GlStateManager.translate(0.0F, 0.9F * (1.0F - ☃xxxxxx) * ☃x + 1.35F * (1.0F - ☃xxxxxx) * (1.0F - ☃x), 0.15F * (1.0F - ☃xxxxxx) * ☃x);
         }
      }

      if (☃xxxx) {
         this.muleLeftEar.render(☃);
         this.muleRightEar.render(☃);
      } else {
         this.horseLeftEar.render(☃);
         this.horseRightEar.render(☃);
      }

      this.head.render(☃);
      if (☃xx) {
         GlStateManager.popMatrix();
      }

      if (☃xxxxx) {
         this.muleLeftChest.render(☃);
         this.muleRightChest.render(☃);
      }
   }

   private float updateHorseRotation(float var1, float var2, float var3) {
      float ☃ = ☃ - ☃;

      while (☃ < -180.0F) {
         ☃ += 360.0F;
      }

      while (☃ >= 180.0F) {
         ☃ -= 360.0F;
      }

      return ☃ + ☃ * ☃;
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      super.setLivingAnimations(☃, ☃, ☃, ☃);
      float ☃ = this.updateHorseRotation(☃.prevRenderYawOffset, ☃.renderYawOffset, ☃);
      float ☃x = this.updateHorseRotation(☃.prevRotationYawHead, ☃.rotationYawHead, ☃);
      float ☃xx = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
      float ☃xxx = ☃x - ☃;
      float ☃xxxx = ☃xx * (float) (Math.PI / 180.0);
      if (☃xxx > 20.0F) {
         ☃xxx = 20.0F;
      }

      if (☃xxx < -20.0F) {
         ☃xxx = -20.0F;
      }

      if (☃ > 0.2F) {
         ☃xxxx += MathHelper.cos(☃ * 0.4F) * 0.15F * ☃;
      }

      AbstractHorse ☃xxxxx = (AbstractHorse)☃;
      float ☃xxxxxx = ☃xxxxx.getGrassEatingAmount(☃);
      float ☃xxxxxxx = ☃xxxxx.getRearingAmount(☃);
      float ☃xxxxxxxx = 1.0F - ☃xxxxxxx;
      float ☃xxxxxxxxx = ☃xxxxx.getMouthOpennessAngle(☃);
      boolean ☃xxxxxxxxxx = ☃xxxxx.tailCounter != 0;
      boolean ☃xxxxxxxxxxx = ☃xxxxx.isHorseSaddled();
      boolean ☃xxxxxxxxxxxx = ☃xxxxx.isBeingRidden();
      float ☃xxxxxxxxxxxxx = ☃.ticksExisted + ☃;
      float ☃xxxxxxxxxxxxxx = MathHelper.cos(☃ * 0.6662F + (float) Math.PI);
      float ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx * 0.8F * ☃;
      this.head.rotationPointY = 4.0F;
      this.head.rotationPointZ = -10.0F;
      this.tailBase.rotationPointY = 3.0F;
      this.tailMiddle.rotationPointZ = 14.0F;
      this.muleRightChest.rotationPointY = 3.0F;
      this.muleRightChest.rotationPointZ = 10.0F;
      this.body.rotateAngleX = 0.0F;
      this.head.rotateAngleX = (float) (Math.PI / 6) + ☃xxxx;
      this.head.rotateAngleY = ☃xxx * (float) (Math.PI / 180.0);
      this.head.rotateAngleX = ☃xxxxxxx * ((float) (Math.PI / 12) + ☃xxxx)
         + ☃xxxxxx * 2.1816616F
         + (1.0F - Math.max(☃xxxxxxx, ☃xxxxxx)) * this.head.rotateAngleX;
      this.head.rotateAngleY = ☃xxxxxxx * ☃xxx * (float) (Math.PI / 180.0) + (1.0F - Math.max(☃xxxxxxx, ☃xxxxxx)) * this.head.rotateAngleY;
      this.head.rotationPointY = ☃xxxxxxx * -6.0F + ☃xxxxxx * 11.0F + (1.0F - Math.max(☃xxxxxxx, ☃xxxxxx)) * this.head.rotationPointY;
      this.head.rotationPointZ = ☃xxxxxxx * -1.0F + ☃xxxxxx * -10.0F + (1.0F - Math.max(☃xxxxxxx, ☃xxxxxx)) * this.head.rotationPointZ;
      this.tailBase.rotationPointY = ☃xxxxxxx * 9.0F + ☃xxxxxxxx * this.tailBase.rotationPointY;
      this.tailMiddle.rotationPointZ = ☃xxxxxxx * 18.0F + ☃xxxxxxxx * this.tailMiddle.rotationPointZ;
      this.muleRightChest.rotationPointY = ☃xxxxxxx * 5.5F + ☃xxxxxxxx * this.muleRightChest.rotationPointY;
      this.muleRightChest.rotationPointZ = ☃xxxxxxx * 15.0F + ☃xxxxxxxx * this.muleRightChest.rotationPointZ;
      this.body.rotateAngleX = ☃xxxxxxx * (float) (-Math.PI / 4) + ☃xxxxxxxx * this.body.rotateAngleX;
      this.horseLeftEar.rotationPointY = this.head.rotationPointY;
      this.horseRightEar.rotationPointY = this.head.rotationPointY;
      this.muleLeftEar.rotationPointY = this.head.rotationPointY;
      this.muleRightEar.rotationPointY = this.head.rotationPointY;
      this.neck.rotationPointY = this.head.rotationPointY;
      this.upperMouth.rotationPointY = 0.02F;
      this.lowerMouth.rotationPointY = 0.0F;
      this.mane.rotationPointY = this.head.rotationPointY;
      this.horseLeftEar.rotationPointZ = this.head.rotationPointZ;
      this.horseRightEar.rotationPointZ = this.head.rotationPointZ;
      this.muleLeftEar.rotationPointZ = this.head.rotationPointZ;
      this.muleRightEar.rotationPointZ = this.head.rotationPointZ;
      this.neck.rotationPointZ = this.head.rotationPointZ;
      this.upperMouth.rotationPointZ = 0.02F - ☃xxxxxxxxx;
      this.lowerMouth.rotationPointZ = ☃xxxxxxxxx;
      this.mane.rotationPointZ = this.head.rotationPointZ;
      this.horseLeftEar.rotateAngleX = this.head.rotateAngleX;
      this.horseRightEar.rotateAngleX = this.head.rotateAngleX;
      this.muleLeftEar.rotateAngleX = this.head.rotateAngleX;
      this.muleRightEar.rotateAngleX = this.head.rotateAngleX;
      this.neck.rotateAngleX = this.head.rotateAngleX;
      this.upperMouth.rotateAngleX = -0.09424778F * ☃xxxxxxxxx;
      this.lowerMouth.rotateAngleX = (float) (Math.PI / 20) * ☃xxxxxxxxx;
      this.mane.rotateAngleX = this.head.rotateAngleX;
      this.horseLeftEar.rotateAngleY = this.head.rotateAngleY;
      this.horseRightEar.rotateAngleY = this.head.rotateAngleY;
      this.muleLeftEar.rotateAngleY = this.head.rotateAngleY;
      this.muleRightEar.rotateAngleY = this.head.rotateAngleY;
      this.neck.rotateAngleY = this.head.rotateAngleY;
      this.upperMouth.rotateAngleY = 0.0F;
      this.lowerMouth.rotateAngleY = 0.0F;
      this.mane.rotateAngleY = this.head.rotateAngleY;
      this.muleLeftChest.rotateAngleX = ☃xxxxxxxxxxxxxxx / 5.0F;
      this.muleRightChest.rotateAngleX = -☃xxxxxxxxxxxxxxx / 5.0F;
      float ☃xxxxxxxxxxxxxxxx = (float) (Math.PI / 12) * ☃xxxxxxx;
      float ☃xxxxxxxxxxxxxxxxx = MathHelper.cos(☃xxxxxxxxxxxxx * 0.6F + (float) Math.PI);
      this.frontLeftLeg.rotationPointY = -2.0F * ☃xxxxxxx + 9.0F * ☃xxxxxxxx;
      this.frontLeftLeg.rotationPointZ = -2.0F * ☃xxxxxxx + -8.0F * ☃xxxxxxxx;
      this.frontRightLeg.rotationPointY = this.frontLeftLeg.rotationPointY;
      this.frontRightLeg.rotationPointZ = this.frontLeftLeg.rotationPointZ;
      this.backLeftShin.rotationPointY = this.backLeftLeg.rotationPointY
         + MathHelper.sin((float) (Math.PI / 2) + ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxx * -☃xxxxxxxxxxxxxx * 0.5F * ☃) * 7.0F;
      this.backLeftShin.rotationPointZ = this.backLeftLeg.rotationPointZ
         + MathHelper.cos((float) (-Math.PI / 2) + ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxx * -☃xxxxxxxxxxxxxx * 0.5F * ☃) * 7.0F;
      this.backRightShin.rotationPointY = this.backRightLeg.rotationPointY
         + MathHelper.sin((float) (Math.PI / 2) + ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxx * ☃xxxxxxxxxxxxxx * 0.5F * ☃) * 7.0F;
      this.backRightShin.rotationPointZ = this.backRightLeg.rotationPointZ
         + MathHelper.cos((float) (-Math.PI / 2) + ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxx * ☃xxxxxxxxxxxxxx * 0.5F * ☃) * 7.0F;
      float ☃xxxxxxxxxxxxxxxxxx = ((float) (-Math.PI / 3) + ☃xxxxxxxxxxxxxxxxx) * ☃xxxxxxx + ☃xxxxxxxxxxxxxxx * ☃xxxxxxxx;
      float ☃xxxxxxxxxxxxxxxxxxx = ((float) (-Math.PI / 3) - ☃xxxxxxxxxxxxxxxxx) * ☃xxxxxxx + -☃xxxxxxxxxxxxxxx * ☃xxxxxxxx;
      this.frontLeftShin.rotationPointY = this.frontLeftLeg.rotationPointY + MathHelper.sin((float) (Math.PI / 2) + ☃xxxxxxxxxxxxxxxxxx) * 7.0F;
      this.frontLeftShin.rotationPointZ = this.frontLeftLeg.rotationPointZ + MathHelper.cos((float) (-Math.PI / 2) + ☃xxxxxxxxxxxxxxxxxx) * 7.0F;
      this.frontRightShin.rotationPointY = this.frontRightLeg.rotationPointY + MathHelper.sin((float) (Math.PI / 2) + ☃xxxxxxxxxxxxxxxxxxx) * 7.0F;
      this.frontRightShin.rotationPointZ = this.frontRightLeg.rotationPointZ + MathHelper.cos((float) (-Math.PI / 2) + ☃xxxxxxxxxxxxxxxxxxx) * 7.0F;
      this.backLeftLeg.rotateAngleX = ☃xxxxxxxxxxxxxxxx + -☃xxxxxxxxxxxxxx * 0.5F * ☃ * ☃xxxxxxxx;
      this.backLeftShin.rotateAngleX = -0.08726646F * ☃xxxxxxx + (-☃xxxxxxxxxxxxxx * 0.5F * ☃ - Math.max(0.0F, ☃xxxxxxxxxxxxxx * 0.5F * ☃)) * ☃xxxxxxxx;
      this.backLeftHoof.rotateAngleX = this.backLeftShin.rotateAngleX;
      this.backRightLeg.rotateAngleX = ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxx * 0.5F * ☃ * ☃xxxxxxxx;
      this.backRightShin.rotateAngleX = -0.08726646F * ☃xxxxxxx + (☃xxxxxxxxxxxxxx * 0.5F * ☃ - Math.max(0.0F, -☃xxxxxxxxxxxxxx * 0.5F * ☃)) * ☃xxxxxxxx;
      this.backRightHoof.rotateAngleX = this.backRightShin.rotateAngleX;
      this.frontLeftLeg.rotateAngleX = ☃xxxxxxxxxxxxxxxxxx;
      this.frontLeftShin.rotateAngleX = (this.frontLeftLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F + ☃xxxxxxxxxxxxxxxxx * 0.2F)) * ☃xxxxxxx
         + (☃xxxxxxxxxxxxxxx + Math.max(0.0F, ☃xxxxxxxxxxxxxx * 0.5F * ☃)) * ☃xxxxxxxx;
      this.frontLeftHoof.rotateAngleX = this.frontLeftShin.rotateAngleX;
      this.frontRightLeg.rotateAngleX = ☃xxxxxxxxxxxxxxxxxxx;
      this.frontRightShin.rotateAngleX = (this.frontRightLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F - ☃xxxxxxxxxxxxxxxxx * 0.2F)) * ☃xxxxxxx
         + (-☃xxxxxxxxxxxxxxx + Math.max(0.0F, -☃xxxxxxxxxxxxxx * 0.5F * ☃)) * ☃xxxxxxxx;
      this.frontRightHoof.rotateAngleX = this.frontRightShin.rotateAngleX;
      this.backLeftHoof.rotationPointY = this.backLeftShin.rotationPointY;
      this.backLeftHoof.rotationPointZ = this.backLeftShin.rotationPointZ;
      this.backRightHoof.rotationPointY = this.backRightShin.rotationPointY;
      this.backRightHoof.rotationPointZ = this.backRightShin.rotationPointZ;
      this.frontLeftHoof.rotationPointY = this.frontLeftShin.rotationPointY;
      this.frontLeftHoof.rotationPointZ = this.frontLeftShin.rotationPointZ;
      this.frontRightHoof.rotationPointY = this.frontRightShin.rotationPointY;
      this.frontRightHoof.rotationPointZ = this.frontRightShin.rotationPointZ;
      if (☃xxxxxxxxxxx) {
         this.horseSaddleBottom.rotationPointY = ☃xxxxxxx * 0.5F + ☃xxxxxxxx * 2.0F;
         this.horseSaddleBottom.rotationPointZ = ☃xxxxxxx * 11.0F + ☃xxxxxxxx * 2.0F;
         this.horseSaddleFront.rotationPointY = this.horseSaddleBottom.rotationPointY;
         this.horseSaddleBack.rotationPointY = this.horseSaddleBottom.rotationPointY;
         this.horseLeftSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
         this.horseRightSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
         this.horseLeftSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
         this.horseRightSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
         this.muleLeftChest.rotationPointY = this.muleRightChest.rotationPointY;
         this.horseSaddleFront.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
         this.horseSaddleBack.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
         this.horseLeftSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
         this.horseRightSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
         this.horseLeftSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
         this.horseRightSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
         this.muleLeftChest.rotationPointZ = this.muleRightChest.rotationPointZ;
         this.horseSaddleBottom.rotateAngleX = this.body.rotateAngleX;
         this.horseSaddleFront.rotateAngleX = this.body.rotateAngleX;
         this.horseSaddleBack.rotateAngleX = this.body.rotateAngleX;
         this.horseLeftRein.rotationPointY = this.head.rotationPointY;
         this.horseRightRein.rotationPointY = this.head.rotationPointY;
         this.horseFaceRopes.rotationPointY = this.head.rotationPointY;
         this.horseLeftFaceMetal.rotationPointY = this.head.rotationPointY;
         this.horseRightFaceMetal.rotationPointY = this.head.rotationPointY;
         this.horseLeftRein.rotationPointZ = this.head.rotationPointZ;
         this.horseRightRein.rotationPointZ = this.head.rotationPointZ;
         this.horseFaceRopes.rotationPointZ = this.head.rotationPointZ;
         this.horseLeftFaceMetal.rotationPointZ = this.head.rotationPointZ;
         this.horseRightFaceMetal.rotationPointZ = this.head.rotationPointZ;
         this.horseLeftRein.rotateAngleX = ☃xxxx;
         this.horseRightRein.rotateAngleX = ☃xxxx;
         this.horseFaceRopes.rotateAngleX = this.head.rotateAngleX;
         this.horseLeftFaceMetal.rotateAngleX = this.head.rotateAngleX;
         this.horseRightFaceMetal.rotateAngleX = this.head.rotateAngleX;
         this.horseFaceRopes.rotateAngleY = this.head.rotateAngleY;
         this.horseLeftFaceMetal.rotateAngleY = this.head.rotateAngleY;
         this.horseLeftRein.rotateAngleY = this.head.rotateAngleY;
         this.horseRightFaceMetal.rotateAngleY = this.head.rotateAngleY;
         this.horseRightRein.rotateAngleY = this.head.rotateAngleY;
         if (☃xxxxxxxxxxxx) {
            this.horseLeftSaddleRope.rotateAngleX = (float) (-Math.PI / 3);
            this.horseLeftSaddleMetal.rotateAngleX = (float) (-Math.PI / 3);
            this.horseRightSaddleRope.rotateAngleX = (float) (-Math.PI / 3);
            this.horseRightSaddleMetal.rotateAngleX = (float) (-Math.PI / 3);
            this.horseLeftSaddleRope.rotateAngleZ = 0.0F;
            this.horseLeftSaddleMetal.rotateAngleZ = 0.0F;
            this.horseRightSaddleRope.rotateAngleZ = 0.0F;
            this.horseRightSaddleMetal.rotateAngleZ = 0.0F;
         } else {
            this.horseLeftSaddleRope.rotateAngleX = ☃xxxxxxxxxxxxxxx / 3.0F;
            this.horseLeftSaddleMetal.rotateAngleX = ☃xxxxxxxxxxxxxxx / 3.0F;
            this.horseRightSaddleRope.rotateAngleX = ☃xxxxxxxxxxxxxxx / 3.0F;
            this.horseRightSaddleMetal.rotateAngleX = ☃xxxxxxxxxxxxxxx / 3.0F;
            this.horseLeftSaddleRope.rotateAngleZ = ☃xxxxxxxxxxxxxxx / 5.0F;
            this.horseLeftSaddleMetal.rotateAngleZ = ☃xxxxxxxxxxxxxxx / 5.0F;
            this.horseRightSaddleRope.rotateAngleZ = -☃xxxxxxxxxxxxxxx / 5.0F;
            this.horseRightSaddleMetal.rotateAngleZ = -☃xxxxxxxxxxxxxxx / 5.0F;
         }
      }

      ☃xxxxxxxxxxxxxxxx = (float) (-Math.PI * 5.0 / 12.0) + ☃ * 1.5F;
      if (☃xxxxxxxxxxxxxxxx > 0.0F) {
         ☃xxxxxxxxxxxxxxxx = 0.0F;
      }

      if (☃xxxxxxxxxx) {
         this.tailBase.rotateAngleY = MathHelper.cos(☃xxxxxxxxxxxxx * 0.7F);
         ☃xxxxxxxxxxxxxxxx = 0.0F;
      } else {
         this.tailBase.rotateAngleY = 0.0F;
      }

      this.tailMiddle.rotateAngleY = this.tailBase.rotateAngleY;
      this.tailTip.rotateAngleY = this.tailBase.rotateAngleY;
      this.tailMiddle.rotationPointY = this.tailBase.rotationPointY;
      this.tailTip.rotationPointY = this.tailBase.rotationPointY;
      this.tailMiddle.rotationPointZ = this.tailBase.rotationPointZ;
      this.tailTip.rotationPointZ = this.tailBase.rotationPointZ;
      this.tailBase.rotateAngleX = ☃xxxxxxxxxxxxxxxx;
      this.tailMiddle.rotateAngleX = ☃xxxxxxxxxxxxxxxx;
      this.tailTip.rotateAngleX = (float) (-Math.PI / 12) + ☃xxxxxxxxxxxxxxxx;
   }
}
