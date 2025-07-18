package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiLockIconButton extends GuiButton {
   private boolean locked;

   public GuiLockIconButton(int var1, int var2, int var3) {
      super(☃, ☃, ☃, 20, 20, "");
   }

   public boolean isLocked() {
      return this.locked;
   }

   public void setLocked(boolean var1) {
      this.locked = ☃;
   }

   @Override
   public void drawButton(Minecraft var1, int var2, int var3, float var4) {
      if (this.visible) {
         ☃.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         boolean ☃ = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
         GuiLockIconButton.Icon ☃x;
         if (this.locked) {
            if (!this.enabled) {
               ☃x = GuiLockIconButton.Icon.LOCKED_DISABLED;
            } else if (☃) {
               ☃x = GuiLockIconButton.Icon.LOCKED_HOVER;
            } else {
               ☃x = GuiLockIconButton.Icon.LOCKED;
            }
         } else if (!this.enabled) {
            ☃x = GuiLockIconButton.Icon.UNLOCKED_DISABLED;
         } else if (☃) {
            ☃x = GuiLockIconButton.Icon.UNLOCKED_HOVER;
         } else {
            ☃x = GuiLockIconButton.Icon.UNLOCKED;
         }

         this.drawTexturedModalRect(this.x, this.y, ☃x.getX(), ☃x.getY(), this.width, this.height);
      }
   }

   static enum Icon {
      LOCKED(0, 146),
      LOCKED_HOVER(0, 166),
      LOCKED_DISABLED(0, 186),
      UNLOCKED(20, 146),
      UNLOCKED_HOVER(20, 166),
      UNLOCKED_DISABLED(20, 186);

      private final int x;
      private final int y;

      private Icon(int var3, int var4) {
         this.x = ☃;
         this.y = ☃;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }
   }
}
