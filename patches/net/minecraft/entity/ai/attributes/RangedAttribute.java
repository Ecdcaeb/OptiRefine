package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;
import net.minecraft.util.math.MathHelper;

public class RangedAttribute extends BaseAttribute {
   private final double minimumValue;
   private final double maximumValue;
   private String description;

   public RangedAttribute(@Nullable IAttribute var1, String var2, double var3, double var5, double var7) {
      super(☃, ☃, ☃);
      this.minimumValue = ☃;
      this.maximumValue = ☃;
      if (☃ > ☃) {
         throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
      } else if (☃ < ☃) {
         throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
      } else if (☃ > ☃) {
         throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
      }
   }

   public RangedAttribute setDescription(String var1) {
      this.description = ☃;
      return this;
   }

   public String getDescription() {
      return this.description;
   }

   @Override
   public double clampValue(double var1) {
      return MathHelper.clamp(☃, this.minimumValue, this.maximumValue);
   }
}
