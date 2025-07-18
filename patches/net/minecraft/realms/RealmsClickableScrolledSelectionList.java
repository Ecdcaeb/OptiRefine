package net.minecraft.realms;

import net.minecraft.client.gui.GuiClickableScrolledSelectionListProxy;

public class RealmsClickableScrolledSelectionList {
   private final GuiClickableScrolledSelectionListProxy proxy;

   public RealmsClickableScrolledSelectionList(int var1, int var2, int var3, int var4, int var5) {
      this.proxy = new GuiClickableScrolledSelectionListProxy(this, ☃, ☃, ☃, ☃, ☃);
   }

   public void render(int var1, int var2, float var3) {
      this.proxy.drawScreen(☃, ☃, ☃);
   }

   public int width() {
      return this.proxy.width();
   }

   public int ym() {
      return this.proxy.mouseY();
   }

   public int xm() {
      return this.proxy.mouseX();
   }

   protected void renderItem(int var1, int var2, int var3, int var4, Tezzelator var5, int var6, int var7) {
   }

   public void renderItem(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.renderItem(☃, ☃, ☃, ☃, Tezzelator.instance, ☃, ☃);
   }

   public int getItemCount() {
      return 0;
   }

   public void selectItem(int var1, boolean var2, int var3, int var4) {
   }

   public boolean isSelectedItem(int var1) {
      return false;
   }

   public void renderBackground() {
   }

   public int getMaxPosition() {
      return 0;
   }

   public int getScrollbarPosition() {
      return this.proxy.width() / 2 + 124;
   }

   public void mouseEvent() {
      this.proxy.handleMouseInput();
   }

   public void customMouseEvent(int var1, int var2, int var3, float var4, int var5) {
   }

   public void scroll(int var1) {
      this.proxy.scrollBy(☃);
   }

   public int getScroll() {
      return this.proxy.getAmountScrolled();
   }

   protected void renderList(int var1, int var2, int var3, int var4) {
   }

   public void itemClicked(int var1, int var2, int var3, int var4, int var5) {
   }

   public void renderSelected(int var1, int var2, int var3, Tezzelator var4) {
   }

   public void setLeftPos(int var1) {
      this.proxy.setSlotXBoundsFromLeft(☃);
   }
}
