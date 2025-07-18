package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class LayerDeadmau5Head implements LayerRenderer<AbstractClientPlayer> {
   private final RenderPlayer playerRenderer;

   public LayerDeadmau5Head(RenderPlayer var1) {
      this.playerRenderer = ☃;
   }

   public void doRenderLayer(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if ("deadmau5".equals(☃.getName()) && ☃.hasSkin() && !☃.isInvisible()) {
         this.playerRenderer.bindTexture(☃.getLocationSkin());

         for (int ☃ = 0; ☃ < 2; ☃++) {
            float ☃x = ☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃ - (☃.prevRenderYawOffset + (☃.renderYawOffset - ☃.prevRenderYawOffset) * ☃);
            float ☃xx = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
            GlStateManager.pushMatrix();
            GlStateManager.rotate(☃x, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(☃xx, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.375F * (☃ * 2 - 1), 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.375F, 0.0F);
            GlStateManager.rotate(-☃xx, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-☃x, 0.0F, 1.0F, 0.0F);
            float ☃xxx = 1.3333334F;
            GlStateManager.scale(1.3333334F, 1.3333334F, 1.3333334F);
            this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
            GlStateManager.popMatrix();
         }
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return true;
   }
}
