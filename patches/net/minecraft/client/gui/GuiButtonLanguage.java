package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonLanguage extends GuiButton {
   public GuiButtonLanguage(int var1, int var2, int var3) {
      super(☃, ☃, ☃, 20, 20, "");
   }

   @Override
   public void drawButton(Minecraft var1, int var2, int var3, float var4) {
      if (this.visible) {
         ☃.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         boolean ☃ = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
         int ☃x = 106;
         if (☃) {
            ☃x += this.height;
         }

         this.drawTexturedModalRect(this.x, this.y, 0, ☃x, this.width, this.height);
      }
   }
}
