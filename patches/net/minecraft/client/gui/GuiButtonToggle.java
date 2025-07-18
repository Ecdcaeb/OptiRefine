package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonToggle extends GuiButton {
   protected ResourceLocation resourceLocation;
   protected boolean stateTriggered;
   protected int xTexStart;
   protected int yTexStart;
   protected int xDiffTex;
   protected int yDiffTex;

   public GuiButtonToggle(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      super(☃, ☃, ☃, ☃, ☃, "");
      this.stateTriggered = ☃;
   }

   public void initTextureValues(int var1, int var2, int var3, int var4, ResourceLocation var5) {
      this.xTexStart = ☃;
      this.yTexStart = ☃;
      this.xDiffTex = ☃;
      this.yDiffTex = ☃;
      this.resourceLocation = ☃;
   }

   public void setStateTriggered(boolean var1) {
      this.stateTriggered = ☃;
   }

   public boolean isStateTriggered() {
      return this.stateTriggered;
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
         if (this.stateTriggered) {
            ☃ += this.xDiffTex;
         }

         if (this.hovered) {
            ☃x += this.yDiffTex;
         }

         this.drawTexturedModalRect(this.x, this.y, ☃, ☃x, this.width, this.height);
         GlStateManager.enableDepth();
      }
   }
}
