package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Mouse;

public class GuiClickableScrolledSelectionListProxy extends GuiSlot {
   private final RealmsClickableScrolledSelectionList proxy;

   public GuiClickableScrolledSelectionListProxy(RealmsClickableScrolledSelectionList var1, int var2, int var3, int var4, int var5, int var6) {
      super(Minecraft.getMinecraft(), ☃, ☃, ☃, ☃, ☃);
      this.proxy = ☃;
   }

   @Override
   protected int getSize() {
      return this.proxy.getItemCount();
   }

   @Override
   protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      this.proxy.selectItem(☃, ☃, ☃, ☃);
   }

   @Override
   protected boolean isSelected(int var1) {
      return this.proxy.isSelectedItem(☃);
   }

   @Override
   protected void drawBackground() {
      this.proxy.renderBackground();
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
      this.proxy.renderItem(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public int width() {
      return this.width;
   }

   public int mouseY() {
      return this.mouseY;
   }

   public int mouseX() {
      return this.mouseX;
   }

   @Override
   protected int getContentHeight() {
      return this.proxy.getMaxPosition();
   }

   @Override
   protected int getScrollBarX() {
      return this.proxy.getScrollbarPosition();
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      if (this.scrollMultiplier > 0.0F && Mouse.getEventButtonState()) {
         this.proxy.customMouseEvent(this.top, this.bottom, this.headerPadding, this.amountScrolled, this.slotHeight);
      }
   }

   public void renderSelected(int var1, int var2, int var3, Tezzelator var4) {
      this.proxy.renderSelected(☃, ☃, ☃, ☃);
   }

   @Override
   protected void drawSelectionBox(int var1, int var2, int var3, int var4, float var5) {
      int ☃ = this.getSize();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         int ☃xx = ☃ + ☃x * this.slotHeight + this.headerPadding;
         int ☃xxx = this.slotHeight - 4;
         if (☃xx > this.bottom || ☃xx + ☃xxx < this.top) {
            this.updateItemPos(☃x, ☃, ☃xx, ☃);
         }

         if (this.showSelectionBox && this.isSelected(☃x)) {
            this.renderSelected(this.width, ☃xx, ☃xxx, Tezzelator.instance);
         }

         this.drawSlot(☃x, ☃, ☃xx, ☃xxx, ☃, ☃, ☃);
      }
   }
}
