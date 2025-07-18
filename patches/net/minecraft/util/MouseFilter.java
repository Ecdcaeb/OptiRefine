package net.minecraft.util;

public class MouseFilter {
   private float targetValue;
   private float remainingValue;
   private float lastAmount;

   public float smooth(float var1, float var2) {
      this.targetValue += ☃;
      ☃ = (this.targetValue - this.remainingValue) * ☃;
      this.lastAmount = this.lastAmount + (☃ - this.lastAmount) * 0.5F;
      if (☃ > 0.0F && ☃ > this.lastAmount || ☃ < 0.0F && ☃ < this.lastAmount) {
         ☃ = this.lastAmount;
      }

      this.remainingValue += ☃;
      return ☃;
   }

   public void reset() {
      this.targetValue = 0.0F;
      this.remainingValue = 0.0F;
      this.lastAmount = 0.0F;
   }
}
