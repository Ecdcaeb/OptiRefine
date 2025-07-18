package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;

public class ModelLlama extends ModelQuadruped {
   private final ModelRenderer chest1;
   private final ModelRenderer chest2;

   public ModelLlama(float var1) {
      super(15, ☃);
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-2.0F, -14.0F, -10.0F, 4, 4, 9, ☃);
      this.head.setRotationPoint(0.0F, 7.0F, -6.0F);
      this.head.setTextureOffset(0, 14).addBox(-4.0F, -16.0F, -6.0F, 8, 18, 6, ☃);
      this.head.setTextureOffset(17, 0).addBox(-4.0F, -19.0F, -4.0F, 3, 3, 2, ☃);
      this.head.setTextureOffset(17, 0).addBox(1.0F, -19.0F, -4.0F, 3, 3, 2, ☃);
      this.body = new ModelRenderer(this, 29, 0);
      this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, ☃);
      this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
      this.chest1 = new ModelRenderer(this, 45, 28);
      this.chest1.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, ☃);
      this.chest1.setRotationPoint(-8.5F, 3.0F, 3.0F);
      this.chest1.rotateAngleY = (float) (Math.PI / 2);
      this.chest2 = new ModelRenderer(this, 45, 41);
      this.chest2.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, ☃);
      this.chest2.setRotationPoint(5.5F, 3.0F, 3.0F);
      this.chest2.rotateAngleY = (float) (Math.PI / 2);
      int ☃ = 4;
      int ☃x = 14;
      this.leg1 = new ModelRenderer(this, 29, 29);
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, ☃);
      this.leg1.setRotationPoint(-2.5F, 10.0F, 6.0F);
      this.leg2 = new ModelRenderer(this, 29, 29);
      this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, ☃);
      this.leg2.setRotationPoint(2.5F, 10.0F, 6.0F);
      this.leg3 = new ModelRenderer(this, 29, 29);
      this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, ☃);
      this.leg3.setRotationPoint(-2.5F, 10.0F, -4.0F);
      this.leg4 = new ModelRenderer(this, 29, 29);
      this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, ☃);
      this.leg4.setRotationPoint(2.5F, 10.0F, -4.0F);
      this.leg1.rotationPointX--;
      this.leg2.rotationPointX++;
      this.leg1.rotationPointZ += 0.0F;
      this.leg2.rotationPointZ += 0.0F;
      this.leg3.rotationPointX--;
      this.leg4.rotationPointX++;
      this.leg3.rotationPointZ--;
      this.leg4.rotationPointZ--;
      this.childZOffset += 2.0F;
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      AbstractChestHorse ☃ = (AbstractChestHorse)☃;
      boolean ☃x = !☃.isChild() && ☃.hasChest();
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      if (this.isChild) {
         float ☃xx = 2.0F;
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, this.childYOffset * ☃, this.childZOffset * ☃);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         float ☃xxx = 0.7F;
         GlStateManager.scale(0.71428573F, 0.64935064F, 0.7936508F);
         GlStateManager.translate(0.0F, 21.0F * ☃, 0.22F);
         this.head.render(☃);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         float ☃xxxx = 1.1F;
         GlStateManager.scale(0.625F, 0.45454544F, 0.45454544F);
         GlStateManager.translate(0.0F, 33.0F * ☃, 0.0F);
         this.body.render(☃);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.45454544F, 0.41322312F, 0.45454544F);
         GlStateManager.translate(0.0F, 33.0F * ☃, 0.0F);
         this.leg1.render(☃);
         this.leg2.render(☃);
         this.leg3.render(☃);
         this.leg4.render(☃);
         GlStateManager.popMatrix();
      } else {
         this.head.render(☃);
         this.body.render(☃);
         this.leg1.render(☃);
         this.leg2.render(☃);
         this.leg3.render(☃);
         this.leg4.render(☃);
      }

      if (☃x) {
         this.chest1.render(☃);
         this.chest2.render(☃);
      }
   }
}
