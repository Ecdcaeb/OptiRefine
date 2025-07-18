package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;

public class MinMaxBounds {
   public static final MinMaxBounds UNBOUNDED = new MinMaxBounds(null, null);
   private final Float min;
   private final Float max;

   public MinMaxBounds(@Nullable Float var1, @Nullable Float var2) {
      this.min = ☃;
      this.max = ☃;
   }

   public boolean test(float var1) {
      return this.min != null && this.min > ☃ ? false : this.max == null || !(this.max < ☃);
   }

   public boolean testSquare(double var1) {
      return this.min != null && this.min * this.min > ☃ ? false : this.max == null || !(this.max * this.max < ☃);
   }

   public static MinMaxBounds deserialize(@Nullable JsonElement var0) {
      if (☃ == null || ☃.isJsonNull()) {
         return UNBOUNDED;
      } else if (JsonUtils.isNumber(☃)) {
         float ☃ = JsonUtils.getFloat(☃, "value");
         return new MinMaxBounds(☃, ☃);
      } else {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "value");
         Float ☃x = ☃.has("min") ? JsonUtils.getFloat(☃, "min") : null;
         Float ☃xx = ☃.has("max") ? JsonUtils.getFloat(☃, "max") : null;
         return new MinMaxBounds(☃x, ☃xx);
      }
   }
}
