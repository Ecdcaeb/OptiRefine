package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

public class DistancePredicate {
   public static final DistancePredicate ANY = new DistancePredicate(
      MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED
   );
   private final MinMaxBounds x;
   private final MinMaxBounds y;
   private final MinMaxBounds z;
   private final MinMaxBounds horizontal;
   private final MinMaxBounds absolute;

   public DistancePredicate(MinMaxBounds var1, MinMaxBounds var2, MinMaxBounds var3, MinMaxBounds var4, MinMaxBounds var5) {
      this.x = ☃;
      this.y = ☃;
      this.z = ☃;
      this.horizontal = ☃;
      this.absolute = ☃;
   }

   public boolean test(double var1, double var3, double var5, double var7, double var9, double var11) {
      float ☃ = (float)(☃ - ☃);
      float ☃x = (float)(☃ - ☃);
      float ☃xx = (float)(☃ - ☃);
      if (!this.x.test(MathHelper.abs(☃)) || !this.y.test(MathHelper.abs(☃x)) || !this.z.test(MathHelper.abs(☃xx))) {
         return false;
      } else {
         return !this.horizontal.testSquare(☃ * ☃ + ☃xx * ☃xx) ? false : this.absolute.testSquare(☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx);
      }
   }

   public static DistancePredicate deserialize(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "distance");
         MinMaxBounds ☃x = MinMaxBounds.deserialize(☃.get("x"));
         MinMaxBounds ☃xx = MinMaxBounds.deserialize(☃.get("y"));
         MinMaxBounds ☃xxx = MinMaxBounds.deserialize(☃.get("z"));
         MinMaxBounds ☃xxxx = MinMaxBounds.deserialize(☃.get("horizontal"));
         MinMaxBounds ☃xxxxx = MinMaxBounds.deserialize(☃.get("absolute"));
         return new DistancePredicate(☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
      } else {
         return ANY;
      }
   }
}
