package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;

public abstract class RenderLiving<T extends EntityLiving> extends RenderLivingBase<T> {
   public RenderLiving(RenderManager var1, ModelBase var2, float var3) {
      super(☃, ☃, ☃);
   }

   protected boolean canRenderName(T var1) {
      return super.canRenderName(☃) && (☃.getAlwaysRenderNameTagForRender() || ☃.hasCustomName() && ☃ == this.renderManager.pointedEntity);
   }

   public boolean shouldRender(T var1, ICamera var2, double var3, double var5, double var7) {
      if (super.shouldRender(☃, ☃, ☃, ☃, ☃)) {
         return true;
      } else if (☃.getLeashed() && ☃.getLeashHolder() != null) {
         Entity ☃ = ☃.getLeashHolder();
         return ☃.isBoundingBoxInFrustum(☃.getRenderBoundingBox());
      } else {
         return false;
      }
   }

   public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
      if (!this.renderOutlines) {
         this.renderLeash(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public void setLightmap(T var1) {
      int ☃ = ☃.getBrightnessForRender();
      int ☃x = ☃ % 65536;
      int ☃xx = ☃ / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃x, ☃xx);
   }

   private double interpolateValue(double var1, double var3, double var5) {
      return ☃ + (☃ - ☃) * ☃;
   }

   protected void renderLeash(T var1, double var2, double var4, double var6, float var8, float var9) {
      Entity ☃ = ☃.getLeashHolder();
      if (☃ != null) {
         ☃ -= (1.6 - ☃.height) * 0.5;
         Tessellator ☃x = Tessellator.getInstance();
         BufferBuilder ☃xx = ☃x.getBuffer();
         double ☃xxx = this.interpolateValue(☃.prevRotationYaw, ☃.rotationYaw, ☃ * 0.5F) * (float) (Math.PI / 180.0);
         double ☃xxxx = this.interpolateValue(☃.prevRotationPitch, ☃.rotationPitch, ☃ * 0.5F) * (float) (Math.PI / 180.0);
         double ☃xxxxx = Math.cos(☃xxx);
         double ☃xxxxxx = Math.sin(☃xxx);
         double ☃xxxxxxx = Math.sin(☃xxxx);
         if (☃ instanceof EntityHanging) {
            ☃xxxxx = 0.0;
            ☃xxxxxx = 0.0;
            ☃xxxxxxx = -1.0;
         }

         double ☃xxxxxxxx = Math.cos(☃xxxx);
         double ☃xxxxxxxxx = this.interpolateValue(☃.prevPosX, ☃.posX, ☃) - ☃xxxxx * 0.7 - ☃xxxxxx * 0.5 * ☃xxxxxxxx;
         double ☃xxxxxxxxxx = this.interpolateValue(☃.prevPosY + ☃.getEyeHeight() * 0.7, ☃.posY + ☃.getEyeHeight() * 0.7, ☃) - ☃xxxxxxx * 0.5 - 0.25;
         double ☃xxxxxxxxxxx = this.interpolateValue(☃.prevPosZ, ☃.posZ, ☃) - ☃xxxxxx * 0.7 + ☃xxxxx * 0.5 * ☃xxxxxxxx;
         double ☃xxxxxxxxxxxx = this.interpolateValue(☃.prevRenderYawOffset, ☃.renderYawOffset, ☃) * (float) (Math.PI / 180.0) + (Math.PI / 2);
         ☃xxxxx = Math.cos(☃xxxxxxxxxxxx) * ☃.width * 0.4;
         ☃xxxxxx = Math.sin(☃xxxxxxxxxxxx) * ☃.width * 0.4;
         double ☃xxxxxxxxxxxxx = this.interpolateValue(☃.prevPosX, ☃.posX, ☃) + ☃xxxxx;
         double ☃xxxxxxxxxxxxxx = this.interpolateValue(☃.prevPosY, ☃.posY, ☃);
         double ☃xxxxxxxxxxxxxxx = this.interpolateValue(☃.prevPosZ, ☃.posZ, ☃) + ☃xxxxxx;
         ☃ += ☃xxxxx;
         ☃ += ☃xxxxxx;
         double ☃xxxxxxxxxxxxxxxx = (float)(☃xxxxxxxxx - ☃xxxxxxxxxxxxx);
         double ☃xxxxxxxxxxxxxxxxx = (float)(☃xxxxxxxxxx - ☃xxxxxxxxxxxxxx);
         double ☃xxxxxxxxxxxxxxxxxx = (float)(☃xxxxxxxxxxx - ☃xxxxxxxxxxxxxxx);
         GlStateManager.disableTexture2D();
         GlStateManager.disableLighting();
         GlStateManager.disableCull();
         int ☃xxxxxxxxxxxxxxxxxxx = 24;
         double ☃xxxxxxxxxxxxxxxxxxxx = 0.025;
         ☃xx.begin(5, DefaultVertexFormats.POSITION_COLOR);

         for (int ☃xxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxx <= 24; ☃xxxxxxxxxxxxxxxxxxxxx++) {
            float ☃xxxxxxxxxxxxxxxxxxxxxx = 0.5F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxx = 0.4F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxx = 0.3F;
            if (☃xxxxxxxxxxxxxxxxxxxxx % 2 == 0) {
               ☃xxxxxxxxxxxxxxxxxxxxxx *= 0.7F;
               ☃xxxxxxxxxxxxxxxxxxxxxxx *= 0.7F;
               ☃xxxxxxxxxxxxxxxxxxxxxxxx *= 0.7F;
            }

            float ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx / 24.0F;
            ☃xx.pos(
                  ☃ + ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx + 0.0,
                  ☃
                     + ☃xxxxxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxx) * 0.5
                     + ((24.0F - ☃xxxxxxxxxxxxxxxxxxxxx) / 18.0F + 0.125F),
                  ☃ + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx
               )
               .color(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
               .endVertex();
            ☃xx.pos(
                  ☃ + ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx + 0.025,
                  ☃
                     + ☃xxxxxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxx) * 0.5
                     + ((24.0F - ☃xxxxxxxxxxxxxxxxxxxxx) / 18.0F + 0.125F)
                     + 0.025,
                  ☃ + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx
               )
               .color(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
               .endVertex();
         }

         ☃x.draw();
         ☃xx.begin(5, DefaultVertexFormats.POSITION_COLOR);

         for (int ☃xxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxx <= 24; ☃xxxxxxxxxxxxxxxxxxxxx++) {
            float ☃xxxxxxxxxxxxxxxxxxxxxx = 0.5F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxx = 0.4F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxx = 0.3F;
            if (☃xxxxxxxxxxxxxxxxxxxxx % 2 == 0) {
               ☃xxxxxxxxxxxxxxxxxxxxxx *= 0.7F;
               ☃xxxxxxxxxxxxxxxxxxxxxxx *= 0.7F;
               ☃xxxxxxxxxxxxxxxxxxxxxxxx *= 0.7F;
            }

            float ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx / 24.0F;
            ☃xx.pos(
                  ☃ + ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx + 0.0,
                  ☃
                     + ☃xxxxxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxx) * 0.5
                     + ((24.0F - ☃xxxxxxxxxxxxxxxxxxxxx) / 18.0F + 0.125F)
                     + 0.025,
                  ☃ + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx
               )
               .color(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
               .endVertex();
            ☃xx.pos(
                  ☃ + ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx + 0.025,
                  ☃
                     + ☃xxxxxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxx) * 0.5
                     + ((24.0F - ☃xxxxxxxxxxxxxxxxxxxxx) / 18.0F + 0.125F),
                  ☃ + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxx + 0.025
               )
               .color(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx, 1.0F)
               .endVertex();
         }

         ☃x.draw();
         GlStateManager.enableLighting();
         GlStateManager.enableTexture2D();
         GlStateManager.enableCull();
      }
   }
}
