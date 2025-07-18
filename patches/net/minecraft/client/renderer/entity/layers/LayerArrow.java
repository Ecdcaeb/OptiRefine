package net.minecraft.client.renderer.entity.layers;

import java.util.Random;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.MathHelper;

public class LayerArrow implements LayerRenderer<EntityLivingBase> {
   private final RenderLivingBase<?> renderer;

   public LayerArrow(RenderLivingBase<?> var1) {
      this.renderer = ☃;
   }

   @Override
   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      int ☃ = ☃.getArrowCountInEntity();
      if (☃ > 0) {
         Entity ☃x = new EntityTippedArrow(☃.world, ☃.posX, ☃.posY, ☃.posZ);
         Random ☃xx = new Random(☃.getEntityId());
         RenderHelper.disableStandardItemLighting();

         for (int ☃xxx = 0; ☃xxx < ☃; ☃xxx++) {
            GlStateManager.pushMatrix();
            ModelRenderer ☃xxxx = this.renderer.getMainModel().getRandomModelBox(☃xx);
            ModelBox ☃xxxxx = ☃xxxx.cubeList.get(☃xx.nextInt(☃xxxx.cubeList.size()));
            ☃xxxx.postRender(0.0625F);
            float ☃xxxxxx = ☃xx.nextFloat();
            float ☃xxxxxxx = ☃xx.nextFloat();
            float ☃xxxxxxxx = ☃xx.nextFloat();
            float ☃xxxxxxxxx = (☃xxxxx.posX1 + (☃xxxxx.posX2 - ☃xxxxx.posX1) * ☃xxxxxx) / 16.0F;
            float ☃xxxxxxxxxx = (☃xxxxx.posY1 + (☃xxxxx.posY2 - ☃xxxxx.posY1) * ☃xxxxxxx) / 16.0F;
            float ☃xxxxxxxxxxx = (☃xxxxx.posZ1 + (☃xxxxx.posZ2 - ☃xxxxx.posZ1) * ☃xxxxxxxx) / 16.0F;
            GlStateManager.translate(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx);
            ☃xxxxxx = ☃xxxxxx * 2.0F - 1.0F;
            ☃xxxxxxx = ☃xxxxxxx * 2.0F - 1.0F;
            ☃xxxxxxxx = ☃xxxxxxxx * 2.0F - 1.0F;
            ☃xxxxxx *= -1.0F;
            ☃xxxxxxx *= -1.0F;
            ☃xxxxxxxx *= -1.0F;
            float ☃xxxxxxxxxxxx = MathHelper.sqrt(☃xxxxxx * ☃xxxxxx + ☃xxxxxxxx * ☃xxxxxxxx);
            ☃x.rotationYaw = (float)(Math.atan2(☃xxxxxx, ☃xxxxxxxx) * 180.0F / (float)Math.PI);
            ☃x.rotationPitch = (float)(Math.atan2(☃xxxxxxx, ☃xxxxxxxxxxxx) * 180.0F / (float)Math.PI);
            ☃x.prevRotationYaw = ☃x.rotationYaw;
            ☃x.prevRotationPitch = ☃x.rotationPitch;
            double ☃xxxxxxxxxxxxx = 0.0;
            double ☃xxxxxxxxxxxxxx = 0.0;
            double ☃xxxxxxxxxxxxxxx = 0.0;
            this.renderer.getRenderManager().renderEntity(☃x, 0.0, 0.0, 0.0, 0.0F, ☃, false);
            GlStateManager.popMatrix();
         }

         RenderHelper.enableStandardItemLighting();
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
