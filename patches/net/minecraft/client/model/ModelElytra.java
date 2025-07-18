package net.minecraft.client.model;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ModelElytra extends ModelBase {
   private final ModelRenderer rightWing;
   private final ModelRenderer leftWing = new ModelRenderer(this, 22, 0);

   public ModelElytra() {
      this.leftWing.addBox(-10.0F, 0.0F, 0.0F, 10, 20, 2, 1.0F);
      this.rightWing = new ModelRenderer(this, 22, 0);
      this.rightWing.mirror = true;
      this.rightWing.addBox(0.0F, 0.0F, 0.0F, 10, 20, 2, 1.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableCull();
      if (☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).isChild()) {
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         GlStateManager.translate(0.0F, 1.5F, -0.1F);
         this.leftWing.render(☃);
         this.rightWing.render(☃);
         GlStateManager.popMatrix();
      } else {
         this.leftWing.render(☃);
         this.rightWing.render(☃);
      }
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      float ☃ = (float) (Math.PI / 12);
      float ☃x = (float) (-Math.PI / 12);
      float ☃xx = 0.0F;
      float ☃xxx = 0.0F;
      if (☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).isElytraFlying()) {
         float ☃xxxx = 1.0F;
         if (☃.motionY < 0.0) {
            Vec3d ☃xxxxx = new Vec3d(☃.motionX, ☃.motionY, ☃.motionZ).normalize();
            ☃xxxx = 1.0F - (float)Math.pow(-☃xxxxx.y, 1.5);
         }

         ☃ = ☃xxxx * (float) (Math.PI / 9) + (1.0F - ☃xxxx) * ☃;
         ☃x = ☃xxxx * (float) (-Math.PI / 2) + (1.0F - ☃xxxx) * ☃x;
      } else if (☃.isSneaking()) {
         ☃ = (float) (Math.PI * 2.0 / 9.0);
         ☃x = (float) (-Math.PI / 4);
         ☃xx = 3.0F;
         ☃xxx = 0.08726646F;
      }

      this.leftWing.rotationPointX = 5.0F;
      this.leftWing.rotationPointY = ☃xx;
      if (☃ instanceof AbstractClientPlayer) {
         AbstractClientPlayer ☃xxxx = (AbstractClientPlayer)☃;
         ☃xxxx.rotateElytraX = (float)(☃xxxx.rotateElytraX + (☃ - ☃xxxx.rotateElytraX) * 0.1);
         ☃xxxx.rotateElytraY = (float)(☃xxxx.rotateElytraY + (☃xxx - ☃xxxx.rotateElytraY) * 0.1);
         ☃xxxx.rotateElytraZ = (float)(☃xxxx.rotateElytraZ + (☃x - ☃xxxx.rotateElytraZ) * 0.1);
         this.leftWing.rotateAngleX = ☃xxxx.rotateElytraX;
         this.leftWing.rotateAngleY = ☃xxxx.rotateElytraY;
         this.leftWing.rotateAngleZ = ☃xxxx.rotateElytraZ;
      } else {
         this.leftWing.rotateAngleX = ☃;
         this.leftWing.rotateAngleZ = ☃x;
         this.leftWing.rotateAngleY = ☃xxx;
      }

      this.rightWing.rotationPointX = -this.leftWing.rotationPointX;
      this.rightWing.rotateAngleY = -this.leftWing.rotateAngleY;
      this.rightWing.rotationPointY = this.leftWing.rotationPointY;
      this.rightWing.rotateAngleX = this.leftWing.rotateAngleX;
      this.rightWing.rotateAngleZ = -this.leftWing.rotateAngleZ;
   }
}
