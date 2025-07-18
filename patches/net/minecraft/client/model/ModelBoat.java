package net.minecraft.client.model;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.MathHelper;

public class ModelBoat extends ModelBase implements IMultipassModel {
   public ModelRenderer[] boatSides = new ModelRenderer[5];
   public ModelRenderer[] paddles = new ModelRenderer[2];
   public ModelRenderer noWater;
   private final int patchList = GLAllocation.generateDisplayLists(1);

   public ModelBoat() {
      this.boatSides[0] = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
      this.boatSides[1] = new ModelRenderer(this, 0, 19).setTextureSize(128, 64);
      this.boatSides[2] = new ModelRenderer(this, 0, 27).setTextureSize(128, 64);
      this.boatSides[3] = new ModelRenderer(this, 0, 35).setTextureSize(128, 64);
      this.boatSides[4] = new ModelRenderer(this, 0, 43).setTextureSize(128, 64);
      int ☃ = 32;
      int ☃x = 6;
      int ☃xx = 20;
      int ☃xxx = 4;
      int ☃xxxx = 28;
      this.boatSides[0].addBox(-14.0F, -9.0F, -3.0F, 28, 16, 3, 0.0F);
      this.boatSides[0].setRotationPoint(0.0F, 3.0F, 1.0F);
      this.boatSides[1].addBox(-13.0F, -7.0F, -1.0F, 18, 6, 2, 0.0F);
      this.boatSides[1].setRotationPoint(-15.0F, 4.0F, 4.0F);
      this.boatSides[2].addBox(-8.0F, -7.0F, -1.0F, 16, 6, 2, 0.0F);
      this.boatSides[2].setRotationPoint(15.0F, 4.0F, 0.0F);
      this.boatSides[3].addBox(-14.0F, -7.0F, -1.0F, 28, 6, 2, 0.0F);
      this.boatSides[3].setRotationPoint(0.0F, 4.0F, -9.0F);
      this.boatSides[4].addBox(-14.0F, -7.0F, -1.0F, 28, 6, 2, 0.0F);
      this.boatSides[4].setRotationPoint(0.0F, 4.0F, 9.0F);
      this.boatSides[0].rotateAngleX = (float) (Math.PI / 2);
      this.boatSides[1].rotateAngleY = (float) (Math.PI * 3.0 / 2.0);
      this.boatSides[2].rotateAngleY = (float) (Math.PI / 2);
      this.boatSides[3].rotateAngleY = (float) Math.PI;
      this.paddles[0] = this.makePaddle(true);
      this.paddles[0].setRotationPoint(3.0F, -5.0F, 9.0F);
      this.paddles[1] = this.makePaddle(false);
      this.paddles[1].setRotationPoint(3.0F, -5.0F, -9.0F);
      this.paddles[1].rotateAngleY = (float) Math.PI;
      this.paddles[0].rotateAngleZ = (float) (Math.PI / 16);
      this.paddles[1].rotateAngleZ = (float) (Math.PI / 16);
      this.noWater = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
      this.noWater.addBox(-14.0F, -9.0F, -3.0F, 28, 16, 3, 0.0F);
      this.noWater.setRotationPoint(0.0F, -3.0F, 1.0F);
      this.noWater.rotateAngleX = (float) (Math.PI / 2);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      EntityBoat ☃ = (EntityBoat)☃;
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);

      for (int ☃x = 0; ☃x < 5; ☃x++) {
         this.boatSides[☃x].render(☃);
      }

      this.renderPaddle(☃, 0, ☃, ☃);
      this.renderPaddle(☃, 1, ☃, ☃);
   }

   @Override
   public void renderMultipass(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.colorMask(false, false, false, false);
      this.noWater.render(☃);
      GlStateManager.colorMask(true, true, true, true);
   }

   protected ModelRenderer makePaddle(boolean var1) {
      ModelRenderer ☃ = new ModelRenderer(this, 62, ☃ ? 0 : 20).setTextureSize(128, 64);
      int ☃x = 20;
      int ☃xx = 7;
      int ☃xxx = 6;
      float ☃xxxx = -5.0F;
      ☃.addBox(-1.0F, 0.0F, -5.0F, 2, 2, 18);
      ☃.addBox(☃ ? -1.001F : 0.001F, -3.0F, 8.0F, 1, 6, 7);
      return ☃;
   }

   protected void renderPaddle(EntityBoat var1, int var2, float var3, float var4) {
      float ☃ = ☃.getRowingTime(☃, ☃);
      ModelRenderer ☃x = this.paddles[☃];
      ☃x.rotateAngleX = (float)MathHelper.clampedLerp((float) (-Math.PI / 3), (float) (-Math.PI / 12), (MathHelper.sin(-☃) + 1.0F) / 2.0F);
      ☃x.rotateAngleY = (float)MathHelper.clampedLerp((float) (-Math.PI / 4), (float) (Math.PI / 4), (MathHelper.sin(-☃ + 1.0F) + 1.0F) / 2.0F);
      if (☃ == 1) {
         ☃x.rotateAngleY = (float) Math.PI - ☃x.rotateAngleY;
      }

      ☃x.render(☃);
   }
}
