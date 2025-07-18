package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class ScaledResolution {
   private final double scaledWidthD;
   private final double scaledHeightD;
   private int scaledWidth;
   private int scaledHeight;
   private int scaleFactor;

   public ScaledResolution(Minecraft var1) {
      this.scaledWidth = ☃.displayWidth;
      this.scaledHeight = ☃.displayHeight;
      this.scaleFactor = 1;
      boolean ☃ = ☃.isUnicode();
      int ☃x = ☃.gameSettings.guiScale;
      if (☃x == 0) {
         ☃x = 1000;
      }

      while (this.scaleFactor < ☃x && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
         this.scaleFactor++;
      }

      if (☃ && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
         this.scaleFactor--;
      }

      this.scaledWidthD = (double)this.scaledWidth / this.scaleFactor;
      this.scaledHeightD = (double)this.scaledHeight / this.scaleFactor;
      this.scaledWidth = MathHelper.ceil(this.scaledWidthD);
      this.scaledHeight = MathHelper.ceil(this.scaledHeightD);
   }

   public int getScaledWidth() {
      return this.scaledWidth;
   }

   public int getScaledHeight() {
      return this.scaledHeight;
   }

   public double getScaledWidth_double() {
      return this.scaledWidthD;
   }

   public double getScaledHeight_double() {
      return this.scaledHeightD;
   }

   public int getScaleFactor() {
      return this.scaleFactor;
   }
}
