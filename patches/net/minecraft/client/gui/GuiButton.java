package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui {
   protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
   protected int width = 200;
   protected int height = 20;
   public int x;
   public int y;
   public String displayString;
   public int id;
   public boolean enabled = true;
   public boolean visible = true;
   protected boolean hovered;

   public GuiButton(int var1, int var2, int var3, String var4) {
      this(☃, ☃, ☃, 200, 20, ☃);
   }

   public GuiButton(int var1, int var2, int var3, int var4, int var5, String var6) {
      this.id = ☃;
      this.x = ☃;
      this.y = ☃;
      this.width = ☃;
      this.height = ☃;
      this.displayString = ☃;
   }

   protected int getHoverState(boolean var1) {
      int ☃ = 1;
      if (!this.enabled) {
         ☃ = 0;
      } else if (☃) {
         ☃ = 2;
      }

      return ☃;
   }

   public void drawButton(Minecraft var1, int var2, int var3, float var4) {
      if (this.visible) {
         FontRenderer ☃ = ☃.fontRenderer;
         ☃.getTextureManager().bindTexture(BUTTON_TEXTURES);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.hovered = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
         int ☃x = this.getHoverState(this.hovered);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         this.drawTexturedModalRect(this.x, this.y, 0, 46 + ☃x * 20, this.width / 2, this.height);
         this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + ☃x * 20, this.width / 2, this.height);
         this.mouseDragged(☃, ☃, ☃);
         int ☃xx = 14737632;
         if (!this.enabled) {
            ☃xx = 10526880;
         } else if (this.hovered) {
            ☃xx = 16777120;
         }

         this.drawCenteredString(☃, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, ☃xx);
      }
   }

   protected void mouseDragged(Minecraft var1, int var2, int var3) {
   }

   public void mouseReleased(int var1, int var2) {
   }

   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      return this.enabled && this.visible && ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
   }

   public boolean isMouseOver() {
      return this.hovered;
   }

   public void drawButtonForegroundLayer(int var1, int var2) {
   }

   public void playPressSound(SoundHandler var1) {
      ☃.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }

   public int getButtonWidth() {
      return this.width;
   }

   public void setWidth(int var1) {
      this.width = ☃;
   }
}
