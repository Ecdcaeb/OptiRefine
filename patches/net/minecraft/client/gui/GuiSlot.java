package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

public abstract class GuiSlot {
   protected final Minecraft mc;
   protected int width;
   protected int height;
   protected int top;
   protected int bottom;
   protected int right;
   protected int left;
   protected final int slotHeight;
   private int scrollUpButtonID;
   private int scrollDownButtonID;
   protected int mouseX;
   protected int mouseY;
   protected boolean centerListVertically = true;
   protected int initialClickY = -2;
   protected float scrollMultiplier;
   protected float amountScrolled;
   protected int selectedElement = -1;
   protected long lastClicked;
   protected boolean visible = true;
   protected boolean showSelectionBox = true;
   protected boolean hasListHeader;
   protected int headerPadding;
   private boolean enabled = true;

   public GuiSlot(Minecraft var1, int var2, int var3, int var4, int var5, int var6) {
      this.mc = ☃;
      this.width = ☃;
      this.height = ☃;
      this.top = ☃;
      this.bottom = ☃;
      this.slotHeight = ☃;
      this.left = 0;
      this.right = ☃;
   }

   public void setDimensions(int var1, int var2, int var3, int var4) {
      this.width = ☃;
      this.height = ☃;
      this.top = ☃;
      this.bottom = ☃;
      this.left = 0;
      this.right = ☃;
   }

   public void setShowSelectionBox(boolean var1) {
      this.showSelectionBox = ☃;
   }

   protected void setHasListHeader(boolean var1, int var2) {
      this.hasListHeader = ☃;
      this.headerPadding = ☃;
      if (!☃) {
         this.headerPadding = 0;
      }
   }

   protected abstract int getSize();

   protected abstract void elementClicked(int var1, boolean var2, int var3, int var4);

   protected abstract boolean isSelected(int var1);

   protected int getContentHeight() {
      return this.getSize() * this.slotHeight + this.headerPadding;
   }

   protected abstract void drawBackground();

   protected void updateItemPos(int var1, int var2, int var3, float var4) {
   }

   protected abstract void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7);

   protected void drawListHeader(int var1, int var2, Tessellator var3) {
   }

   protected void clickedHeader(int var1, int var2) {
   }

   protected void renderDecorations(int var1, int var2) {
   }

   public int getSlotIndexFromScreenCoords(int var1, int var2) {
      int ☃ = this.left + this.width / 2 - this.getListWidth() / 2;
      int ☃x = this.left + this.width / 2 + this.getListWidth() / 2;
      int ☃xx = ☃ - this.top - this.headerPadding + (int)this.amountScrolled - 4;
      int ☃xxx = ☃xx / this.slotHeight;
      return ☃ < this.getScrollBarX() && ☃ >= ☃ && ☃ <= ☃x && ☃xxx >= 0 && ☃xx >= 0 && ☃xxx < this.getSize() ? ☃xxx : -1;
   }

   public void registerScrollButtons(int var1, int var2) {
      this.scrollUpButtonID = ☃;
      this.scrollDownButtonID = ☃;
   }

   protected void bindAmountScrolled() {
      this.amountScrolled = MathHelper.clamp(this.amountScrolled, 0.0F, (float)this.getMaxScroll());
   }

   public int getMaxScroll() {
      return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
   }

   public int getAmountScrolled() {
      return (int)this.amountScrolled;
   }

   public boolean isMouseYWithinSlotBounds(int var1) {
      return ☃ >= this.top && ☃ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
   }

   public void scrollBy(int var1) {
      this.amountScrolled += ☃;
      this.bindAmountScrolled();
      this.initialClickY = -2;
   }

   public void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == this.scrollUpButtonID) {
            this.amountScrolled = this.amountScrolled - this.slotHeight * 2 / 3;
            this.initialClickY = -2;
            this.bindAmountScrolled();
         } else if (☃.id == this.scrollDownButtonID) {
            this.amountScrolled = this.amountScrolled + this.slotHeight * 2 / 3;
            this.initialClickY = -2;
            this.bindAmountScrolled();
         }
      }
   }

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
         this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         float ☃xxxx = 32.0F;
         ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         ☃xxx.pos(this.left, this.bottom, 0.0).tex(this.left / 32.0F, (this.bottom + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
         ☃xxx.pos(this.right, this.bottom, 0.0).tex(this.right / 32.0F, (this.bottom + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
         ☃xxx.pos(this.right, this.top, 0.0).tex(this.right / 32.0F, (this.top + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
         ☃xxx.pos(this.left, this.top, 0.0).tex(this.left / 32.0F, (this.top + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
         ☃xx.draw();
         int ☃xxxxx = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
         int ☃xxxxxx = this.top + 4 - (int)this.amountScrolled;
         if (this.hasListHeader) {
            this.drawListHeader(☃xxxxx, ☃xxxxxx, ☃xx);
         }

         this.drawSelectionBox(☃xxxxx, ☃xxxxxx, ☃, ☃, ☃);
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
         int ☃xxxxxxx = 4;
         ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         ☃xxx.pos(this.left, this.top + 4, 0.0).tex(0.0, 1.0).color(0, 0, 0, 0).endVertex();
         ☃xxx.pos(this.right, this.top + 4, 0.0).tex(1.0, 1.0).color(0, 0, 0, 0).endVertex();
         ☃xxx.pos(this.right, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
         ☃xxx.pos(this.left, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
         ☃xx.draw();
         ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         ☃xxx.pos(this.left, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
         ☃xxx.pos(this.right, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
         ☃xxx.pos(this.right, this.bottom - 4, 0.0).tex(1.0, 0.0).color(0, 0, 0, 0).endVertex();
         ☃xxx.pos(this.left, this.bottom - 4, 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex();
         ☃xx.draw();
         int ☃xxxxxxxx = this.getMaxScroll();
         if (☃xxxxxxxx > 0) {
            int ☃xxxxxxxxx = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            ☃xxxxxxxxx = MathHelper.clamp(☃xxxxxxxxx, 32, this.bottom - this.top - 8);
            int ☃xxxxxxxxxx = (int)this.amountScrolled * (this.bottom - this.top - ☃xxxxxxxxx) / ☃xxxxxxxx + this.top;
            if (☃xxxxxxxxxx < this.top) {
               ☃xxxxxxxxxx = this.top;
            }

            ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            ☃xxx.pos(☃, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxx.pos(☃x, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxx.pos(☃x, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            ☃xxx.pos(☃, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            ☃xx.draw();
            ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            ☃xxx.pos(☃, ☃xxxxxxxxxx + ☃xxxxxxxxx, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            ☃xxx.pos(☃x, ☃xxxxxxxxxx + ☃xxxxxxxxx, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            ☃xxx.pos(☃x, ☃xxxxxxxxxx, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            ☃xxx.pos(☃, ☃xxxxxxxxxx, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            ☃xx.draw();
            ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            ☃xxx.pos(☃, ☃xxxxxxxxxx + ☃xxxxxxxxx - 1, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
            ☃xxx.pos(☃x - 1, ☃xxxxxxxxxx + ☃xxxxxxxxx - 1, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
            ☃xxx.pos(☃x - 1, ☃xxxxxxxxxx, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
            ☃xxx.pos(☃, ☃xxxxxxxxxx, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
            ☃xx.draw();
         }

         this.renderDecorations(☃, ☃);
         GlStateManager.enableTexture2D();
         GlStateManager.shadeModel(7424);
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
      }
   }

   public void handleMouseInput() {
      if (this.isMouseYWithinSlotBounds(this.mouseY)) {
         if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
            int ☃ = (this.width - this.getListWidth()) / 2;
            int ☃x = (this.width + this.getListWidth()) / 2;
            int ☃xx = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
            int ☃xxx = ☃xx / this.slotHeight;
            if (☃xxx < this.getSize() && this.mouseX >= ☃ && this.mouseX <= ☃x && ☃xxx >= 0 && ☃xx >= 0) {
               this.elementClicked(☃xxx, false, this.mouseX, this.mouseY);
               this.selectedElement = ☃xxx;
            } else if (this.mouseX >= ☃ && this.mouseX <= ☃x && ☃xx < 0) {
               this.clickedHeader(this.mouseX - ☃, this.mouseY - this.top + (int)this.amountScrolled - 4);
            }
         }

         if (!Mouse.isButtonDown(0) || !this.getEnabled()) {
            this.initialClickY = -1;
         } else if (this.initialClickY == -1) {
            boolean ☃ = true;
            if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
               int ☃x = (this.width - this.getListWidth()) / 2;
               int ☃xx = (this.width + this.getListWidth()) / 2;
               int ☃xxx = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
               int ☃xxxx = ☃xxx / this.slotHeight;
               if (☃xxxx < this.getSize() && this.mouseX >= ☃x && this.mouseX <= ☃xx && ☃xxxx >= 0 && ☃xxx >= 0) {
                  boolean ☃xxxxx = ☃xxxx == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                  this.elementClicked(☃xxxx, ☃xxxxx, this.mouseX, this.mouseY);
                  this.selectedElement = ☃xxxx;
                  this.lastClicked = Minecraft.getSystemTime();
               } else if (this.mouseX >= ☃x && this.mouseX <= ☃xx && ☃xxx < 0) {
                  this.clickedHeader(this.mouseX - ☃x, this.mouseY - this.top + (int)this.amountScrolled - 4);
                  ☃ = false;
               }

               int ☃xxxxx = this.getScrollBarX();
               int ☃xxxxxx = ☃xxxxx + 6;
               if (this.mouseX >= ☃xxxxx && this.mouseX <= ☃xxxxxx) {
                  this.scrollMultiplier = -1.0F;
                  int ☃xxxxxxx = this.getMaxScroll();
                  if (☃xxxxxxx < 1) {
                     ☃xxxxxxx = 1;
                  }

                  int ☃xxxxxxxx = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / this.getContentHeight());
                  ☃xxxxxxxx = MathHelper.clamp(☃xxxxxxxx, 32, this.bottom - this.top - 8);
                  this.scrollMultiplier = this.scrollMultiplier / ((float)(this.bottom - this.top - ☃xxxxxxxx) / ☃xxxxxxx);
               } else {
                  this.scrollMultiplier = 1.0F;
               }

               if (☃) {
                  this.initialClickY = this.mouseY;
               } else {
                  this.initialClickY = -2;
               }
            } else {
               this.initialClickY = -2;
            }
         } else if (this.initialClickY >= 0) {
            this.amountScrolled = this.amountScrolled - (this.mouseY - this.initialClickY) * this.scrollMultiplier;
            this.initialClickY = this.mouseY;
         }

         int ☃ = Mouse.getEventDWheel();
         if (☃ != 0) {
            if (☃ > 0) {
               ☃ = -1;
            } else if (☃ < 0) {
               ☃ = 1;
            }

            this.amountScrolled = this.amountScrolled + ☃ * this.slotHeight / 2;
         }
      }
   }

   public void setEnabled(boolean var1) {
      this.enabled = ☃;
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public int getListWidth() {
      return 220;
   }

   protected void drawSelectionBox(int var1, int var2, int var3, int var4, float var5) {
      int ☃ = this.getSize();
      Tessellator ☃x = Tessellator.getInstance();
      BufferBuilder ☃xx = ☃x.getBuffer();

      for (int ☃xxx = 0; ☃xxx < ☃; ☃xxx++) {
         int ☃xxxx = ☃ + ☃xxx * this.slotHeight + this.headerPadding;
         int ☃xxxxx = this.slotHeight - 4;
         if (☃xxxx > this.bottom || ☃xxxx + ☃xxxxx < this.top) {
            this.updateItemPos(☃xxx, ☃, ☃xxxx, ☃);
         }

         if (this.showSelectionBox && this.isSelected(☃xxx)) {
            int ☃xxxxxx = this.left + (this.width / 2 - this.getListWidth() / 2);
            int ☃xxxxxxx = this.left + this.width / 2 + this.getListWidth() / 2;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableTexture2D();
            ☃xx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            ☃xx.pos(☃xxxxxx, ☃xxxx + ☃xxxxx + 2, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            ☃xx.pos(☃xxxxxxx, ☃xxxx + ☃xxxxx + 2, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            ☃xx.pos(☃xxxxxxx, ☃xxxx - 2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            ☃xx.pos(☃xxxxxx, ☃xxxx - 2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            ☃xx.pos(☃xxxxxx + 1, ☃xxxx + ☃xxxxx + 1, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xx.pos(☃xxxxxxx - 1, ☃xxxx + ☃xxxxx + 1, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xx.pos(☃xxxxxxx - 1, ☃xxxx - 1, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            ☃xx.pos(☃xxxxxx + 1, ☃xxxx - 1, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            ☃x.draw();
            GlStateManager.enableTexture2D();
         }

         this.drawSlot(☃xxx, ☃, ☃xxxx, ☃xxxxx, ☃, ☃, ☃);
      }
   }

   protected int getScrollBarX() {
      return this.width / 2 + 124;
   }

   protected void overlayBackground(int var1, int var2, int var3, int var4) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float ☃xx = 32.0F;
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      ☃x.pos(this.left, ☃, 0.0).tex(0.0, ☃ / 32.0F).color(64, 64, 64, ☃).endVertex();
      ☃x.pos(this.left + this.width, ☃, 0.0).tex(this.width / 32.0F, ☃ / 32.0F).color(64, 64, 64, ☃).endVertex();
      ☃x.pos(this.left + this.width, ☃, 0.0).tex(this.width / 32.0F, ☃ / 32.0F).color(64, 64, 64, ☃).endVertex();
      ☃x.pos(this.left, ☃, 0.0).tex(0.0, ☃ / 32.0F).color(64, 64, 64, ☃).endVertex();
      ☃.draw();
   }

   public void setSlotXBoundsFromLeft(int var1) {
      this.left = ☃;
      this.right = ☃ + this.width;
   }

   public int getSlotHeight() {
      return this.slotHeight;
   }
}
