package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class RealmsSliderButton extends RealmsButton {
   public float value = 1.0F;
   public boolean sliding;
   private final float minValue;
   private final float maxValue;
   private int steps;

   public RealmsSliderButton(int var1, int var2, int var3, int var4, int var5, int var6) {
      this(☃, ☃, ☃, ☃, ☃, 0, 1.0F, ☃);
   }

   public RealmsSliderButton(int var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8) {
      super(☃, ☃, ☃, ☃, 20, "");
      this.minValue = ☃;
      this.maxValue = ☃;
      this.value = this.toPct(☃);
      this.getProxy().displayString = this.getMessage();
   }

   public String getMessage() {
      return "";
   }

   public float toPct(float var1) {
      return MathHelper.clamp((this.clamp(☃) - this.minValue) / (this.maxValue - this.minValue), 0.0F, 1.0F);
   }

   public float toValue(float var1) {
      return this.clamp(this.minValue + (this.maxValue - this.minValue) * MathHelper.clamp(☃, 0.0F, 1.0F));
   }

   public float clamp(float var1) {
      ☃ = this.clampSteps(☃);
      return MathHelper.clamp(☃, this.minValue, this.maxValue);
   }

   protected float clampSteps(float var1) {
      if (this.steps > 0) {
         ☃ = this.steps * Math.round(☃ / this.steps);
      }

      return ☃;
   }

   @Override
   public int getYImage(boolean var1) {
      return 0;
   }

   @Override
   public void renderBg(int var1, int var2) {
      if (this.getProxy().visible) {
         if (this.sliding) {
            this.value = (float)(☃ - (this.getProxy().x + 4)) / (this.getProxy().getButtonWidth() - 8);
            this.value = MathHelper.clamp(this.value, 0.0F, 1.0F);
            float ☃ = this.toValue(this.value);
            this.clicked(☃);
            this.value = this.toPct(☃);
            this.getProxy().displayString = this.getMessage();
         }

         Minecraft.getMinecraft().getTextureManager().bindTexture(WIDGETS_LOCATION);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.blit(this.getProxy().x + (int)(this.value * (this.getProxy().getButtonWidth() - 8)), this.getProxy().y, 0, 66, 4, 20);
         this.blit(this.getProxy().x + (int)(this.value * (this.getProxy().getButtonWidth() - 8)) + 4, this.getProxy().y, 196, 66, 4, 20);
      }
   }

   @Override
   public void clicked(int var1, int var2) {
      this.value = (float)(☃ - (this.getProxy().x + 4)) / (this.getProxy().getButtonWidth() - 8);
      this.value = MathHelper.clamp(this.value, 0.0F, 1.0F);
      this.clicked(this.toValue(this.value));
      this.getProxy().displayString = this.getMessage();
      this.sliding = true;
   }

   public void clicked(float var1) {
   }

   @Override
   public void released(int var1, int var2) {
      this.sliding = false;
   }
}
