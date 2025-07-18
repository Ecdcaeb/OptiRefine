package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public abstract class GuiListExtended extends GuiSlot {
   public GuiListExtended(Minecraft var1, int var2, int var3, int var4, int var5, int var6) {
      super(☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   protected void elementClicked(int var1, boolean var2, int var3, int var4) {
   }

   @Override
   protected boolean isSelected(int var1) {
      return false;
   }

   @Override
   protected void drawBackground() {
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
      this.getListEntry(☃)
         .drawEntry(☃, ☃, ☃, this.getListWidth(), ☃, ☃, ☃, this.isMouseYWithinSlotBounds(☃) && this.getSlotIndexFromScreenCoords(☃, ☃) == ☃, ☃);
   }

   @Override
   protected void updateItemPos(int var1, int var2, int var3, float var4) {
      this.getListEntry(☃).updatePosition(☃, ☃, ☃, ☃);
   }

   public boolean mouseClicked(int var1, int var2, int var3) {
      if (this.isMouseYWithinSlotBounds(☃)) {
         int ☃ = this.getSlotIndexFromScreenCoords(☃, ☃);
         if (☃ >= 0) {
            int ☃x = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int ☃xx = this.top + 4 - this.getAmountScrolled() + ☃ * this.slotHeight + this.headerPadding;
            int ☃xxx = ☃ - ☃x;
            int ☃xxxx = ☃ - ☃xx;
            if (this.getListEntry(☃).mousePressed(☃, ☃, ☃, ☃, ☃xxx, ☃xxxx)) {
               this.setEnabled(false);
               return true;
            }
         }
      }

      return false;
   }

   public boolean mouseReleased(int var1, int var2, int var3) {
      for (int ☃ = 0; ☃ < this.getSize(); ☃++) {
         int ☃x = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
         int ☃xx = this.top + 4 - this.getAmountScrolled() + ☃ * this.slotHeight + this.headerPadding;
         int ☃xxx = ☃ - ☃x;
         int ☃xxxx = ☃ - ☃xx;
         this.getListEntry(☃).mouseReleased(☃, ☃, ☃, ☃, ☃xxx, ☃xxxx);
      }

      this.setEnabled(true);
      return false;
   }

   public abstract GuiListExtended.IGuiListEntry getListEntry(int var1);

   public interface IGuiListEntry {
      void updatePosition(int var1, int var2, int var3, float var4);

      void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9);

      boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6);

      void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6);
   }
}
