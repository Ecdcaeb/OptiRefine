package net.minecraft.client.gui;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Gui {
   public static final ResourceLocation OPTIONS_BACKGROUND = new ResourceLocation("textures/gui/options_background.png");
   public static final ResourceLocation STAT_ICONS = new ResourceLocation("textures/gui/container/stats_icons.png");
   public static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");
   protected float zLevel;

   protected void drawHorizontalLine(int var1, int var2, int var3, int var4) {
      if (☃ < ☃) {
         int ☃ = ☃;
         ☃ = ☃;
         ☃ = ☃;
      }

      drawRect(☃, ☃, ☃ + 1, ☃ + 1, ☃);
   }

   protected void drawVerticalLine(int var1, int var2, int var3, int var4) {
      if (☃ < ☃) {
         int ☃ = ☃;
         ☃ = ☃;
         ☃ = ☃;
      }

      drawRect(☃, ☃ + 1, ☃ + 1, ☃, ☃);
   }

   public static void drawRect(int var0, int var1, int var2, int var3, int var4) {
      if (☃ < ☃) {
         int ☃ = ☃;
         ☃ = ☃;
         ☃ = ☃;
      }

      if (☃ < ☃) {
         int ☃ = ☃;
         ☃ = ☃;
         ☃ = ☃;
      }

      float ☃ = (☃ >> 24 & 0xFF) / 255.0F;
      float ☃x = (☃ >> 16 & 0xFF) / 255.0F;
      float ☃xx = (☃ >> 8 & 0xFF) / 255.0F;
      float ☃xxx = (☃ & 0xFF) / 255.0F;
      Tessellator ☃xxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxx = ☃xxxx.getBuffer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.color(☃x, ☃xx, ☃xxx, ☃);
      ☃xxxxx.begin(7, DefaultVertexFormats.POSITION);
      ☃xxxxx.pos(☃, ☃, 0.0).endVertex();
      ☃xxxxx.pos(☃, ☃, 0.0).endVertex();
      ☃xxxxx.pos(☃, ☃, 0.0).endVertex();
      ☃xxxxx.pos(☃, ☃, 0.0).endVertex();
      ☃xxxx.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   protected void drawGradientRect(int var1, int var2, int var3, int var4, int var5, int var6) {
      float ☃ = (☃ >> 24 & 0xFF) / 255.0F;
      float ☃x = (☃ >> 16 & 0xFF) / 255.0F;
      float ☃xx = (☃ >> 8 & 0xFF) / 255.0F;
      float ☃xxx = (☃ & 0xFF) / 255.0F;
      float ☃xxxx = (☃ >> 24 & 0xFF) / 255.0F;
      float ☃xxxxx = (☃ >> 16 & 0xFF) / 255.0F;
      float ☃xxxxxx = (☃ >> 8 & 0xFF) / 255.0F;
      float ☃xxxxxxx = (☃ & 0xFF) / 255.0F;
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.shadeModel(7425);
      Tessellator ☃xxxxxxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxxxxxx = ☃xxxxxxxx.getBuffer();
      ☃xxxxxxxxx.begin(7, DefaultVertexFormats.POSITION_COLOR);
      ☃xxxxxxxxx.pos(☃, ☃, this.zLevel).color(☃x, ☃xx, ☃xxx, ☃).endVertex();
      ☃xxxxxxxxx.pos(☃, ☃, this.zLevel).color(☃x, ☃xx, ☃xxx, ☃).endVertex();
      ☃xxxxxxxxx.pos(☃, ☃, this.zLevel).color(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxx).endVertex();
      ☃xxxxxxxxx.pos(☃, ☃, this.zLevel).color(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxx).endVertex();
      ☃xxxxxxxx.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
   }

   public void drawCenteredString(FontRenderer var1, String var2, int var3, int var4, int var5) {
      ☃.drawStringWithShadow(☃, ☃ - ☃.getStringWidth(☃) / 2, ☃, ☃);
   }

   public void drawString(FontRenderer var1, String var2, int var3, int var4, int var5) {
      ☃.drawStringWithShadow(☃, ☃, ☃, ☃);
   }

   public void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6) {
      float ☃ = 0.00390625F;
      float ☃x = 0.00390625F;
      Tessellator ☃xx = Tessellator.getInstance();
      BufferBuilder ☃xxx = ☃xx.getBuffer();
      ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃xxx.pos(☃ + 0, ☃ + ☃, this.zLevel).tex((☃ + 0) * 0.00390625F, (☃ + ☃) * 0.00390625F).endVertex();
      ☃xxx.pos(☃ + ☃, ☃ + ☃, this.zLevel).tex((☃ + ☃) * 0.00390625F, (☃ + ☃) * 0.00390625F).endVertex();
      ☃xxx.pos(☃ + ☃, ☃ + 0, this.zLevel).tex((☃ + ☃) * 0.00390625F, (☃ + 0) * 0.00390625F).endVertex();
      ☃xxx.pos(☃ + 0, ☃ + 0, this.zLevel).tex((☃ + 0) * 0.00390625F, (☃ + 0) * 0.00390625F).endVertex();
      ☃xx.draw();
   }

   public void drawTexturedModalRect(float var1, float var2, int var3, int var4, int var5, int var6) {
      float ☃ = 0.00390625F;
      float ☃x = 0.00390625F;
      Tessellator ☃xx = Tessellator.getInstance();
      BufferBuilder ☃xxx = ☃xx.getBuffer();
      ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃xxx.pos(☃ + 0.0F, ☃ + ☃, this.zLevel).tex((☃ + 0) * 0.00390625F, (☃ + ☃) * 0.00390625F).endVertex();
      ☃xxx.pos(☃ + ☃, ☃ + ☃, this.zLevel).tex((☃ + ☃) * 0.00390625F, (☃ + ☃) * 0.00390625F).endVertex();
      ☃xxx.pos(☃ + ☃, ☃ + 0.0F, this.zLevel).tex((☃ + ☃) * 0.00390625F, (☃ + 0) * 0.00390625F).endVertex();
      ☃xxx.pos(☃ + 0.0F, ☃ + 0.0F, this.zLevel).tex((☃ + 0) * 0.00390625F, (☃ + 0) * 0.00390625F).endVertex();
      ☃xx.draw();
   }

   public void drawTexturedModalRect(int var1, int var2, TextureAtlasSprite var3, int var4, int var5) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃x.pos(☃ + 0, ☃ + ☃, this.zLevel).tex(☃.getMinU(), ☃.getMaxV()).endVertex();
      ☃x.pos(☃ + ☃, ☃ + ☃, this.zLevel).tex(☃.getMaxU(), ☃.getMaxV()).endVertex();
      ☃x.pos(☃ + ☃, ☃ + 0, this.zLevel).tex(☃.getMaxU(), ☃.getMinV()).endVertex();
      ☃x.pos(☃ + 0, ☃ + 0, this.zLevel).tex(☃.getMinU(), ☃.getMinV()).endVertex();
      ☃.draw();
   }

   public static void drawModalRectWithCustomSizedTexture(int var0, int var1, float var2, float var3, int var4, int var5, float var6, float var7) {
      float ☃ = 1.0F / ☃;
      float ☃x = 1.0F / ☃;
      Tessellator ☃xx = Tessellator.getInstance();
      BufferBuilder ☃xxx = ☃xx.getBuffer();
      ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃xxx.pos(☃, ☃ + ☃, 0.0).tex(☃ * ☃, (☃ + ☃) * ☃x).endVertex();
      ☃xxx.pos(☃ + ☃, ☃ + ☃, 0.0).tex((☃ + ☃) * ☃, (☃ + ☃) * ☃x).endVertex();
      ☃xxx.pos(☃ + ☃, ☃, 0.0).tex((☃ + ☃) * ☃, ☃ * ☃x).endVertex();
      ☃xxx.pos(☃, ☃, 0.0).tex(☃ * ☃, ☃ * ☃x).endVertex();
      ☃xx.draw();
   }

   public static void drawScaledCustomSizeModalRect(int var0, int var1, float var2, float var3, int var4, int var5, int var6, int var7, float var8, float var9) {
      float ☃ = 1.0F / ☃;
      float ☃x = 1.0F / ☃;
      Tessellator ☃xx = Tessellator.getInstance();
      BufferBuilder ☃xxx = ☃xx.getBuffer();
      ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃xxx.pos(☃, ☃ + ☃, 0.0).tex(☃ * ☃, (☃ + ☃) * ☃x).endVertex();
      ☃xxx.pos(☃ + ☃, ☃ + ☃, 0.0).tex((☃ + ☃) * ☃, (☃ + ☃) * ☃x).endVertex();
      ☃xxx.pos(☃ + ☃, ☃, 0.0).tex((☃ + ☃) * ☃, ☃ * ☃x).endVertex();
      ☃xxx.pos(☃, ☃, 0.0).tex(☃ * ☃, ☃ * ☃x).endVertex();
      ☃xx.draw();
   }
}
