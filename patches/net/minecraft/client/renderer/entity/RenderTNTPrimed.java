package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderTNTPrimed extends Render<EntityTNTPrimed> {
   public RenderTNTPrimed(RenderManager var1) {
      super(☃);
      this.shadowSize = 0.5F;
   }

   public void doRender(EntityTNTPrimed var1, double var2, double var4, double var6, float var8, float var9) {
      BlockRendererDispatcher ☃ = Minecraft.getMinecraft().getBlockRendererDispatcher();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)☃, (float)☃ + 0.5F, (float)☃);
      if (☃.getFuse() - ☃ + 1.0F < 10.0F) {
         float ☃x = 1.0F - (☃.getFuse() - ☃ + 1.0F) / 10.0F;
         ☃x = MathHelper.clamp(☃x, 0.0F, 1.0F);
         ☃x *= ☃x;
         ☃x *= ☃x;
         float ☃xx = 1.0F + ☃x * 0.3F;
         GlStateManager.scale(☃xx, ☃xx, ☃xx);
      }

      float ☃x = (1.0F - (☃.getFuse() - ☃ + 1.0F) / 100.0F) * 0.8F;
      this.bindEntityTexture(☃);
      GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(-0.5F, -0.5F, 0.5F);
      ☃.renderBlockBrightness(Blocks.TNT.getDefaultState(), ☃.getBrightness());
      GlStateManager.translate(0.0F, 0.0F, 1.0F);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
         ☃.renderBlockBrightness(Blocks.TNT.getDefaultState(), 1.0F);
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      } else if (☃.getFuse() / 5 % 2 == 0) {
         GlStateManager.disableTexture2D();
         GlStateManager.disableLighting();
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.DST_ALPHA);
         GlStateManager.color(1.0F, 1.0F, 1.0F, ☃x);
         GlStateManager.doPolygonOffset(-3.0F, -3.0F);
         GlStateManager.enablePolygonOffset();
         ☃.renderBlockBrightness(Blocks.TNT.getDefaultState(), 1.0F);
         GlStateManager.doPolygonOffset(0.0F, 0.0F);
         GlStateManager.disablePolygonOffset();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
         GlStateManager.enableLighting();
         GlStateManager.enableTexture2D();
      }

      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityTNTPrimed var1) {
      return TextureMap.LOCATION_BLOCKS_TEXTURE;
   }
}
