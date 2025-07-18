package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Random;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

public class RandomValueRange {
   private final float min;
   private final float max;

   public RandomValueRange(float var1, float var2) {
      this.min = ☃;
      this.max = ☃;
   }

   public RandomValueRange(float var1) {
      this.min = ☃;
      this.max = ☃;
   }

   public float getMin() {
      return this.min;
   }

   public float getMax() {
      return this.max;
   }

   public int generateInt(Random var1) {
      return MathHelper.getInt(☃, MathHelper.floor(this.min), MathHelper.floor(this.max));
   }

   public float generateFloat(Random var1) {
      return MathHelper.nextFloat(☃, this.min, this.max);
   }

   public boolean isInRange(int var1) {
      return ☃ <= this.max && ☃ >= this.min;
   }

   public static class Serializer implements JsonDeserializer<RandomValueRange>, JsonSerializer<RandomValueRange> {
      public RandomValueRange deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         if (JsonUtils.isNumber(☃)) {
            return new RandomValueRange(JsonUtils.getFloat(☃, "value"));
         } else {
            JsonObject ☃ = JsonUtils.getJsonObject(☃, "value");
            float ☃x = JsonUtils.getFloat(☃, "min");
            float ☃xx = JsonUtils.getFloat(☃, "max");
            return new RandomValueRange(☃x, ☃xx);
         }
      }

      public JsonElement serialize(RandomValueRange var1, Type var2, JsonSerializationContext var3) {
         if (☃.min == ☃.max) {
            return new JsonPrimitive(☃.min);
         } else {
            JsonObject ☃ = new JsonObject();
            ☃.addProperty("min", ☃.min);
            ☃.addProperty("max", ☃.max);
            return ☃;
         }
      }
   }
}
