package net.minecraft.client.resources.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class AnimationMetadataSectionSerializer
   extends BaseMetadataSectionSerializer<AnimationMetadataSection>
   implements JsonSerializer<AnimationMetadataSection> {
   public AnimationMetadataSection deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
      List<AnimationFrame> ☃ = Lists.newArrayList();
      JsonObject ☃x = JsonUtils.getJsonObject(☃, "metadata section");
      int ☃xx = JsonUtils.getInt(☃x, "frametime", 1);
      if (☃xx != 1) {
         Validate.inclusiveBetween(1L, 2147483647L, ☃xx, "Invalid default frame time");
      }

      if (☃x.has("frames")) {
         try {
            JsonArray ☃xxx = JsonUtils.getJsonArray(☃x, "frames");

            for (int ☃xxxx = 0; ☃xxxx < ☃xxx.size(); ☃xxxx++) {
               JsonElement ☃xxxxx = ☃xxx.get(☃xxxx);
               AnimationFrame ☃xxxxxx = this.parseAnimationFrame(☃xxxx, ☃xxxxx);
               if (☃xxxxxx != null) {
                  ☃.add(☃xxxxxx);
               }
            }
         } catch (ClassCastException var11) {
            throw new JsonParseException("Invalid animation->frames: expected array, was " + ☃x.get("frames"), var11);
         }
      }

      int ☃xxx = JsonUtils.getInt(☃x, "width", -1);
      int ☃xxxxx = JsonUtils.getInt(☃x, "height", -1);
      if (☃xxx != -1) {
         Validate.inclusiveBetween(1L, 2147483647L, ☃xxx, "Invalid width");
      }

      if (☃xxxxx != -1) {
         Validate.inclusiveBetween(1L, 2147483647L, ☃xxxxx, "Invalid height");
      }

      boolean ☃xxxxxx = JsonUtils.getBoolean(☃x, "interpolate", false);
      return new AnimationMetadataSection(☃, ☃xxx, ☃xxxxx, ☃xx, ☃xxxxxx);
   }

   private AnimationFrame parseAnimationFrame(int var1, JsonElement var2) {
      if (☃.isJsonPrimitive()) {
         return new AnimationFrame(JsonUtils.getInt(☃, "frames[" + ☃ + "]"));
      } else if (☃.isJsonObject()) {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "frames[" + ☃ + "]");
         int ☃x = JsonUtils.getInt(☃, "time", -1);
         if (☃.has("time")) {
            Validate.inclusiveBetween(1L, 2147483647L, ☃x, "Invalid frame time");
         }

         int ☃xx = JsonUtils.getInt(☃, "index");
         Validate.inclusiveBetween(0L, 2147483647L, ☃xx, "Invalid frame index");
         return new AnimationFrame(☃xx, ☃x);
      } else {
         return null;
      }
   }

   public JsonElement serialize(AnimationMetadataSection var1, Type var2, JsonSerializationContext var3) {
      JsonObject ☃ = new JsonObject();
      ☃.addProperty("frametime", ☃.getFrameTime());
      if (☃.getFrameWidth() != -1) {
         ☃.addProperty("width", ☃.getFrameWidth());
      }

      if (☃.getFrameHeight() != -1) {
         ☃.addProperty("height", ☃.getFrameHeight());
      }

      if (☃.getFrameCount() > 0) {
         JsonArray ☃x = new JsonArray();

         for (int ☃xx = 0; ☃xx < ☃.getFrameCount(); ☃xx++) {
            if (☃.frameHasTime(☃xx)) {
               JsonObject ☃xxx = new JsonObject();
               ☃xxx.addProperty("index", ☃.getFrameIndex(☃xx));
               ☃xxx.addProperty("time", ☃.getFrameTimeSingle(☃xx));
               ☃x.add(☃xxx);
            } else {
               ☃x.add(new JsonPrimitive(☃.getFrameIndex(☃xx)));
            }
         }

         ☃.add("frames", ☃x);
      }

      return ☃;
   }

   @Override
   public String getSectionName() {
      return "animation";
   }
}
