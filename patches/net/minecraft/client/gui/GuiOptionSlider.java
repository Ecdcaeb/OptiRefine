package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.MathHelper;

public class GuiOptionSlider extends GuiButton {
   private float sliderValue = 1.0F;
   public boolean dragging;
   private final GameSettings.Options options;
   private final float minValue;
   private final float maxValue;

   public GuiOptionSlider(int var1, int var2, int var3, GameSettings.Options var4) {
      this(☃, ☃, ☃, ☃, 0.0F, 1.0F);
   }

   public GuiOptionSlider(int var1, int var2, int var3, GameSettings.Options var4, float var5, float var6) {
      super(☃, ☃, ☃, 150, 20, "");
      this.options = ☃;
      this.minValue = ☃;
      this.maxValue = ☃;
      Minecraft ☃ = Minecraft.getMinecraft();
      this.sliderValue = ☃.normalizeValue(☃.gameSettings.getOptionFloatValue(☃));
      this.displayString = ☃.gameSettings.getKeyBinding(☃);
   }

   @Override
   protected int getHoverState(boolean var1) {
      return 0;
   }

   @Override
   protected void mouseDragged(Minecraft var1, int var2, int var3) {
      if (this.visible) {
         if (this.dragging) {
            this.sliderValue = (float)(☃ - (this.x + 4)) / (this.width - 8);
            this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
            float ☃ = this.options.denormalizeValue(this.sliderValue);
            ☃.gameSettings.setOptionFloatValue(this.options, ☃);
            this.sliderValue = this.options.normalizeValue(☃);
            this.displayString = ☃.gameSettings.getKeyBinding(this.options);
         }

         ☃.getTextureManager().bindTexture(BUTTON_TEXTURES);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (this.width - 8)), this.y, 0, 66, 4, 20);
         this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
      }
   }

   @Override
   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      if (super.mousePressed(☃, ☃, ☃)) {
         this.sliderValue = (float)(☃ - (this.x + 4)) / (this.width - 8);
         this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
         ☃.gameSettings.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue));
         this.displayString = ☃.gameSettings.getKeyBinding(this.options);
         this.dragging = true;
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void mouseReleased(int var1, int var2) {
      this.dragging = false;
   }
}
