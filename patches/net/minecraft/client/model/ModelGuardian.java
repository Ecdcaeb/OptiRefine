package net.minecraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ModelGuardian extends ModelBase {
   private final ModelRenderer guardianBody;
   private final ModelRenderer guardianEye;
   private final ModelRenderer[] guardianSpines;
   private final ModelRenderer[] guardianTail;

   public ModelGuardian() {
      this.textureWidth = 64;
      this.textureHeight = 64;
      this.guardianSpines = new ModelRenderer[12];
      this.guardianBody = new ModelRenderer(this);
      this.guardianBody.setTextureOffset(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12, 12, 16);
      this.guardianBody.setTextureOffset(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2, 12, 12);
      this.guardianBody.setTextureOffset(0, 28).addBox(6.0F, 10.0F, -6.0F, 2, 12, 12, true);
      this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12, 2, 12);
      this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12, 2, 12);

      for (int ☃ = 0; ☃ < this.guardianSpines.length; ☃++) {
         this.guardianSpines[☃] = new ModelRenderer(this, 0, 0);
         this.guardianSpines[☃].addBox(-1.0F, -4.5F, -1.0F, 2, 9, 2);
         this.guardianBody.addChild(this.guardianSpines[☃]);
      }

      this.guardianEye = new ModelRenderer(this, 8, 0);
      this.guardianEye.addBox(-1.0F, 15.0F, 0.0F, 2, 2, 1);
      this.guardianBody.addChild(this.guardianEye);
      this.guardianTail = new ModelRenderer[3];
      this.guardianTail[0] = new ModelRenderer(this, 40, 0);
      this.guardianTail[0].addBox(-2.0F, 14.0F, 7.0F, 4, 4, 8);
      this.guardianTail[1] = new ModelRenderer(this, 0, 54);
      this.guardianTail[1].addBox(0.0F, 14.0F, 0.0F, 3, 3, 7);
      this.guardianTail[2] = new ModelRenderer(this);
      this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0F, 14.0F, 0.0F, 2, 2, 6);
      this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0F, 10.5F, 3.0F, 1, 9, 9);
      this.guardianBody.addChild(this.guardianTail[0]);
      this.guardianTail[0].addChild(this.guardianTail[1]);
      this.guardianTail[1].addChild(this.guardianTail[2]);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.guardianBody.render(☃);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      EntityGuardian ☃ = (EntityGuardian)☃;
      float ☃x = ☃ - ☃.ticksExisted;
      this.guardianBody.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
      this.guardianBody.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      float[] ☃xx = new float[]{1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F};
      float[] ☃xxx = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F};
      float[] ☃xxxx = new float[]{0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F};
      float[] ☃xxxxx = new float[]{0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F};
      float[] ☃xxxxxx = new float[]{-8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F};
      float[] ☃xxxxxxx = new float[]{8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F};
      float ☃xxxxxxxx = (1.0F - ☃.getSpikesAnimation(☃x)) * 0.55F;

      for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 12; ☃xxxxxxxxx++) {
         this.guardianSpines[☃xxxxxxxxx].rotateAngleX = (float) Math.PI * ☃xx[☃xxxxxxxxx];
         this.guardianSpines[☃xxxxxxxxx].rotateAngleY = (float) Math.PI * ☃xxx[☃xxxxxxxxx];
         this.guardianSpines[☃xxxxxxxxx].rotateAngleZ = (float) Math.PI * ☃xxxx[☃xxxxxxxxx];
         this.guardianSpines[☃xxxxxxxxx].rotationPointX = ☃xxxxx[☃xxxxxxxxx] * (1.0F + MathHelper.cos(☃ * 1.5F + ☃xxxxxxxxx) * 0.01F - ☃xxxxxxxx);
         this.guardianSpines[☃xxxxxxxxx].rotationPointY = 16.0F + ☃xxxxxx[☃xxxxxxxxx] * (1.0F + MathHelper.cos(☃ * 1.5F + ☃xxxxxxxxx) * 0.01F - ☃xxxxxxxx);
         this.guardianSpines[☃xxxxxxxxx].rotationPointZ = ☃xxxxxxx[☃xxxxxxxxx] * (1.0F + MathHelper.cos(☃ * 1.5F + ☃xxxxxxxxx) * 0.01F - ☃xxxxxxxx);
      }

      this.guardianEye.rotationPointZ = -8.25F;
      Entity ☃xxxxxxxxx = Minecraft.getMinecraft().getRenderViewEntity();
      if (☃.hasTargetedEntity()) {
         ☃xxxxxxxxx = ☃.getTargetedEntity();
      }

      if (☃xxxxxxxxx != null) {
         Vec3d ☃xxxxxxxxxx = ☃xxxxxxxxx.getPositionEyes(0.0F);
         Vec3d ☃xxxxxxxxxxx = ☃.getPositionEyes(0.0F);
         double ☃xxxxxxxxxxxx = ☃xxxxxxxxxx.y - ☃xxxxxxxxxxx.y;
         if (☃xxxxxxxxxxxx > 0.0) {
            this.guardianEye.rotationPointY = 0.0F;
         } else {
            this.guardianEye.rotationPointY = 1.0F;
         }

         Vec3d ☃xxxxxxxxxxxxx = ☃.getLook(0.0F);
         ☃xxxxxxxxxxxxx = new Vec3d(☃xxxxxxxxxxxxx.x, 0.0, ☃xxxxxxxxxxxxx.z);
         Vec3d ☃xxxxxxxxxxxxxx = new Vec3d(☃xxxxxxxxxxx.x - ☃xxxxxxxxxx.x, 0.0, ☃xxxxxxxxxxx.z - ☃xxxxxxxxxx.z).normalize().rotateYaw((float) (Math.PI / 2));
         double ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.dotProduct(☃xxxxxxxxxxxxxx);
         this.guardianEye.rotationPointX = MathHelper.sqrt((float)Math.abs(☃xxxxxxxxxxxxxxx)) * 2.0F * (float)Math.signum(☃xxxxxxxxxxxxxxx);
      }

      this.guardianEye.showModel = true;
      float ☃xxxxxxxxxx = ☃.getTailAnimation(☃x);
      this.guardianTail[0].rotateAngleY = MathHelper.sin(☃xxxxxxxxxx) * (float) Math.PI * 0.05F;
      this.guardianTail[1].rotateAngleY = MathHelper.sin(☃xxxxxxxxxx) * (float) Math.PI * 0.1F;
      this.guardianTail[1].rotationPointX = -1.5F;
      this.guardianTail[1].rotationPointY = 0.5F;
      this.guardianTail[1].rotationPointZ = 14.0F;
      this.guardianTail[2].rotateAngleY = MathHelper.sin(☃xxxxxxxxxx) * (float) Math.PI * 0.15F;
      this.guardianTail[2].rotationPointX = 0.5F;
      this.guardianTail[2].rotationPointY = 0.5F;
      this.guardianTail[2].rotationPointZ = 6.0F;
   }
}
