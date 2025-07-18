package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class LayerSpiderEyes<T extends EntitySpider> implements LayerRenderer<T> {
   private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");
   private final RenderSpider<T> spiderRenderer;

   public LayerSpiderEyes(RenderSpider<T> var1) {
      this.spiderRenderer = ☃;
   }

   public void doRenderLayer(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.spiderRenderer.bindTexture(SPIDER_EYES);
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
      if (☃.isInvisible()) {
         GlStateManager.depthMask(false);
      } else {
         GlStateManager.depthMask(true);
      }

      int ☃ = 61680;
      int ☃x = ☃ % 65536;
      int ☃xx = ☃ / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃x, ☃xx);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
      this.spiderRenderer.getMainModel().render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
      ☃ = ☃.getBrightnessForRender();
      ☃x = ☃ % 65536;
      ☃xx = ☃ / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃x, ☃xx);
      this.spiderRenderer.setLightmap(☃);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
