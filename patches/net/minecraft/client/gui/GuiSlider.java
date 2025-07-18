package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiSlider extends GuiButton {
   private float sliderPosition = 1.0F;
   public boolean isMouseDown;
   private final String name;
   private final float min;
   private final float max;
   private final GuiPageButtonList.GuiResponder responder;
   private GuiSlider.FormatHelper formatHelper;

   public GuiSlider(
      GuiPageButtonList.GuiResponder var1, int var2, int var3, int var4, String var5, float var6, float var7, float var8, GuiSlider.FormatHelper var9
   ) {
      super(☃, ☃, ☃, 150, 20, "");
      this.name = ☃;
      this.min = ☃;
      this.max = ☃;
      this.sliderPosition = (☃ - ☃) / (☃ - ☃);
      this.formatHelper = ☃;
      this.responder = ☃;
      this.displayString = this.getDisplayString();
   }

   public float getSliderValue() {
      return this.min + (this.max - this.min) * this.sliderPosition;
   }

   public void setSliderValue(float var1, boolean var2) {
      this.sliderPosition = (☃ - this.min) / (this.max - this.min);
      this.displayString = this.getDisplayString();
      if (☃) {
         this.responder.setEntryValue(this.id, this.getSliderValue());
      }
   }

   public float getSliderPosition() {
      return this.sliderPosition;
   }

   private String getDisplayString() {
      return this.formatHelper == null
         ? I18n.format(this.name) + ": " + this.getSliderValue()
         : this.formatHelper.getText(this.id, I18n.format(this.name), this.getSliderValue());
   }

   @Override
   protected int getHoverState(boolean var1) {
      return 0;
   }

   @Override
   protected void mouseDragged(Minecraft var1, int var2, int var3) {
      if (this.visible) {
         if (this.isMouseDown) {
            this.sliderPosition = (float)(☃ - (this.x + 4)) / (this.width - 8);
            if (this.sliderPosition < 0.0F) {
               this.sliderPosition = 0.0F;
            }

            if (this.sliderPosition > 1.0F) {
               this.sliderPosition = 1.0F;
            }

            this.displayString = this.getDisplayString();
            this.responder.setEntryValue(this.id, this.getSliderValue());
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.drawTexturedModalRect(this.x + (int)(this.sliderPosition * (this.width - 8)), this.y, 0, 66, 4, 20);
         this.drawTexturedModalRect(this.x + (int)(this.sliderPosition * (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
      }
   }

   public void setSliderPosition(float var1) {
      this.sliderPosition = ☃;
      this.displayString = this.getDisplayString();
      this.responder.setEntryValue(this.id, this.getSliderValue());
   }

   @Override
   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      if (super.mousePressed(☃, ☃, ☃)) {
         this.sliderPosition = (float)(☃ - (this.x + 4)) / (this.width - 8);
         if (this.sliderPosition < 0.0F) {
            this.sliderPosition = 0.0F;
         }

         if (this.sliderPosition > 1.0F) {
            this.sliderPosition = 1.0F;
         }

         this.displayString = this.getDisplayString();
         this.responder.setEntryValue(this.id, this.getSliderValue());
         this.isMouseDown = true;
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void mouseReleased(int var1, int var2) {
      this.isMouseDown = false;
   }

   public interface FormatHelper {
      String getText(int var1, String var2, float var3);
   }
}
