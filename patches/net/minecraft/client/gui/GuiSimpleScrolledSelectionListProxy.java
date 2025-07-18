package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.util.math.MathHelper;

public class GuiSimpleScrolledSelectionListProxy extends GuiSlot {
   private final RealmsSimpleScrolledSelectionList realmsScrolledSelectionList;

   public GuiSimpleScrolledSelectionListProxy(RealmsSimpleScrolledSelectionList var1, int var2, int var3, int var4, int var5, int var6) {
      super(Minecraft.getMinecraft(), ☃, ☃, ☃, ☃, ☃);
      this.realmsScrolledSelectionList = ☃;
   }

   @Override
   protected int getSize() {
      return this.realmsScrolledSelectionList.getItemCount();
   }

   @Override
   protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      this.realmsScrolledSelectionList.selectItem(☃, ☃, ☃, ☃);
   }

   @Override
   protected boolean isSelected(int var1) {
      return this.realmsScrolledSelectionList.isSelectedItem(☃);
   }

   @Override
   protected void drawBackground() {
      this.realmsScrolledSelectionList.renderBackground();
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
      this.realmsScrolledSelectionList.renderItem(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public int getWidth() {
      return this.width;
   }

   public int getMouseY() {
      return this.mouseY;
   }

   public int getMouseX() {
      return this.mouseX;
   }

   @Override
   protected int getContentHeight() {
      return this.realmsScrolledSelectionList.getMaxPosition();
   }

   @Override
   protected int getScrollBarX() {
      return this.realmsScrolledSelectionList.getScrollbarPosition();
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      if (this.visible) {
         this.mouseX = ☃;
         this.mouseY = ☃;
         this.drawBackground();
         int ☃ = this.getScrollBarX();
         int ☃x = ☃ + 6;
         this.bindAmountScrolled();
         GlStateManager.disableLighting();
         GlStateManager.disableFog();
         Tessellator ☃xx = Tessellator.getInstance();
         BufferBuilder ☃xxx = ☃xx.getBuffer();
         int ☃xxxx = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
         int ☃xxxxx = this.top + 4 - (int)this.amountScrolled;
         if (this.hasListHeader) {
            this.drawListHeader(☃xxxx, ☃xxxxx, ☃xx);
         }

         this.drawSelectionBox(☃xxxx, ☃xxxxx, ☃, ☃, ☃);
         GlStateManager.disableDepth();
         this.overlayBackground(0, this.top, 255, 255);
         this.overlayBackground(this.bottom, this.height, 255, 255);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ZERO,
            GlStateManager.DestFactor.ONE
         );
         GlStateManager.disableAlpha();
         GlStateManager.shadeModel(7425);
         GlStateManager.disableTexture2D();
         int ☃xxxxxx = this.getMaxScroll();
         if (☃xxxxxx > 0) {
            int ☃xxxxxxx = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            ☃xxxxxxx = MathHelper.clamp(☃xxxxxxx, 32, this.bottom - this.top - 8);
            int ☃xxxxxxxx = (int)this.amountScrolled * (this.bottom - this.top - ☃xxxxxxx) / ☃xxxxxx + this.top;
            if (☃xxxxxxxx < this.top) {
               ☃xxxxxxxx = this.top;
            }

            ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            ☃xxx.pos(☃, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxx.pos(☃x, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxx.pos(☃x, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            ☃xxx.pos(☃, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            ☃xx.draw();
            ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            ☃xxx.pos(☃, ☃xxxxxxxx + ☃xxxxxxx, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            ☃xxx.pos(☃x, ☃xxxxxxxx + ☃xxxxxxx, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            ☃xxx.pos(☃x, ☃xxxxxxxx, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            ☃xxx.pos(☃, ☃xxxxxxxx, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            ☃xx.draw();
            ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            ☃xxx.pos(☃, ☃xxxxxxxx + ☃xxxxxxx - 1, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
            ☃xxx.pos(☃x - 1, ☃xxxxxxxx + ☃xxxxxxx - 1, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
            ☃xxx.pos(☃x - 1, ☃xxxxxxxx, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
            ☃xxx.pos(☃, ☃xxxxxxxx, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
            ☃xx.draw();
         }

         this.renderDecorations(☃, ☃);
         GlStateManager.enableTexture2D();
         GlStateManager.shadeModel(7424);
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
      }
   }
}
