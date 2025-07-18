package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImage extends GuiButton {
   private final ResourceLocation resourceLocation;
   private final int xTexStart;
   private final int yTexStart;
   private final int yDiffText;

   public GuiButtonImage(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ResourceLocation var9) {
      super(☃, ☃, ☃, ☃, ☃, "");
      this.xTexStart = ☃;
      this.yTexStart = ☃;
      this.yDiffText = ☃;
      this.resourceLocation = ☃;
   }

   public void setPosition(int var1, int var2) {
      this.x = ☃;
      this.y = ☃;
   }

   @Override
   public void drawButton(Minecraft var1, int var2, int var3, float var4) {
      if (this.visible) {
         this.hovered = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
         ☃.getTextureManager().bindTexture(this.resourceLocation);
         GlStateManager.disableDepth();
         int ☃ = this.xTexStart;
         int ☃x = this.yTexStart;
         if (this.hovered) {
            ☃x += this.yDiffText;
         }

         this.drawTexturedModalRect(this.x, this.y, ☃, ☃x, this.width, this.height);
         GlStateManager.enableDepth();
      }
   }
}
