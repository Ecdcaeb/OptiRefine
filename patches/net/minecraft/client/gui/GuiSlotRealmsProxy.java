package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy extends GuiSlot {
   private final RealmsScrolledSelectionList selectionList;

   public GuiSlotRealmsProxy(RealmsScrolledSelectionList var1, int var2, int var3, int var4, int var5, int var6) {
      super(Minecraft.getMinecraft(), ☃, ☃, ☃, ☃, ☃);
      this.selectionList = ☃;
   }

   @Override
   protected int getSize() {
      return this.selectionList.getItemCount();
   }

   @Override
   protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      this.selectionList.selectItem(☃, ☃, ☃, ☃);
   }

   @Override
   protected boolean isSelected(int var1) {
      return this.selectionList.isSelectedItem(☃);
   }

   @Override
   protected void drawBackground() {
      this.selectionList.renderBackground();
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
      this.selectionList.renderItem(☃, ☃, ☃, ☃, ☃, ☃);
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
      return this.selectionList.getMaxPosition();
   }

   @Override
   protected int getScrollBarX() {
      return this.selectionList.getScrollbarPosition();
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
   }
}
