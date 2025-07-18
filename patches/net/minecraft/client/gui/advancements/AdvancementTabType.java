package net.minecraft.client.gui.advancements;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

enum AdvancementTabType {
   ABOVE(0, 0, 28, 32, 8),
   BELOW(84, 0, 28, 32, 8),
   LEFT(0, 64, 32, 28, 5),
   RIGHT(96, 64, 32, 28, 5);

   public static final int MAX_TABS;
   private final int textureX;
   private final int textureY;
   private final int width;
   private final int height;
   private final int max;

   private AdvancementTabType(int var3, int var4, int var5, int var6, int var7) {
      this.textureX = ☃;
      this.textureY = ☃;
      this.width = ☃;
      this.height = ☃;
      this.max = ☃;
   }

   public int getMax() {
      return this.max;
   }

   public void draw(Gui var1, int var2, int var3, boolean var4, int var5) {
      int ☃ = this.textureX;
      if (☃ > 0) {
         ☃ += this.width;
      }

      if (☃ == this.max - 1) {
         ☃ += this.width;
      }

      int ☃x = ☃ ? this.textureY + this.height : this.textureY;
      ☃.drawTexturedModalRect(☃ + this.getX(☃), ☃ + this.getY(☃), ☃, ☃x, this.width, this.height);
   }

   public void drawIcon(int var1, int var2, int var3, RenderItem var4, ItemStack var5) {
      int ☃ = ☃ + this.getX(☃);
      int ☃x = ☃ + this.getY(☃);
      switch (this) {
         case ABOVE:
            ☃ += 6;
            ☃x += 9;
            break;
         case BELOW:
            ☃ += 6;
            ☃x += 6;
            break;
         case LEFT:
            ☃ += 10;
            ☃x += 5;
            break;
         case RIGHT:
            ☃ += 6;
            ☃x += 5;
      }

      ☃.renderItemAndEffectIntoGUI(null, ☃, ☃, ☃x);
   }

   public int getX(int var1) {
      switch (this) {
         case ABOVE:
            return (this.width + 4) * ☃;
         case BELOW:
            return (this.width + 4) * ☃;
         case LEFT:
            return -this.width + 4;
         case RIGHT:
            return 248;
         default:
            throw new UnsupportedOperationException("Don't know what this tab type is!" + this);
      }
   }

   public int getY(int var1) {
      switch (this) {
         case ABOVE:
            return -this.height + 4;
         case BELOW:
            return 136;
         case LEFT:
            return this.height * ☃;
         case RIGHT:
            return this.height * ☃;
         default:
            throw new UnsupportedOperationException("Don't know what this tab type is!" + this);
      }
   }

   public boolean isMouseOver(int var1, int var2, int var3, int var4, int var5) {
      int ☃ = ☃ + this.getX(☃);
      int ☃x = ☃ + this.getY(☃);
      return ☃ > ☃ && ☃ < ☃ + this.width && ☃ > ☃x && ☃ < ☃x + this.height;
   }

   static {
      int ☃ = 0;

      for (AdvancementTabType ☃x : values()) {
         ☃ += ☃x.max;
      }

      MAX_TABS = ☃;
   }
}
