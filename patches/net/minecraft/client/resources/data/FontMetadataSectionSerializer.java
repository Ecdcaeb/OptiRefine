package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class FontMetadataSectionSerializer extends BaseMetadataSectionSerializer<FontMetadataSection> {
   public FontMetadataSection deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
      JsonObject ☃ = ☃.getAsJsonObject();
      float[] ☃x = new float[256];
      float[] ☃xx = new float[256];
      float[] ☃xxx = new float[256];
      float ☃xxxx = 1.0F;
      float ☃xxxxx = 0.0F;
      float ☃xxxxxx = 0.0F;
      if (☃.has("characters")) {
         if (!☃.get("characters").isJsonObject()) {
            throw new JsonParseException("Invalid font->characters: expected object, was " + ☃.get("characters"));
         }

         JsonObject ☃xxxxxxx = ☃.getAsJsonObject("characters");
         if (☃xxxxxxx.has("default")) {
            if (!☃xxxxxxx.get("default").isJsonObject()) {
               throw new JsonParseException("Invalid font->characters->default: expected object, was " + ☃xxxxxxx.get("default"));
            }

            JsonObject ☃xxxxxxxx = ☃xxxxxxx.getAsJsonObject("default");
            ☃xxxx = JsonUtils.getFloat(☃xxxxxxxx, "width", ☃xxxx);
            Validate.inclusiveBetween(0.0, Float.MAX_VALUE, ☃xxxx, "Invalid default width");
            ☃xxxxx = JsonUtils.getFloat(☃xxxxxxxx, "spacing", ☃xxxxx);
            Validate.inclusiveBetween(0.0, Float.MAX_VALUE, ☃xxxxx, "Invalid default spacing");
            ☃xxxxxx = JsonUtils.getFloat(☃xxxxxxxx, "left", ☃xxxxx);
            Validate.inclusiveBetween(0.0, Float.MAX_VALUE, ☃xxxxxx, "Invalid default left");
         }

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 256; ☃xxxxxxxx++) {
            JsonElement ☃xxxxxxxxx = ☃xxxxxxx.get(Integer.toString(☃xxxxxxxx));
            float ☃xxxxxxxxxx = ☃xxxx;
            float ☃xxxxxxxxxxx = ☃xxxxx;
            float ☃xxxxxxxxxxxx = ☃xxxxxx;
            if (☃xxxxxxxxx != null) {
               JsonObject ☃xxxxxxxxxxxxx = JsonUtils.getJsonObject(☃xxxxxxxxx, "characters[" + ☃xxxxxxxx + "]");
               ☃xxxxxxxxxx = JsonUtils.getFloat(☃xxxxxxxxxxxxx, "width", ☃xxxx);
               Validate.inclusiveBetween(0.0, Float.MAX_VALUE, ☃xxxxxxxxxx, "Invalid width");
               ☃xxxxxxxxxxx = JsonUtils.getFloat(☃xxxxxxxxxxxxx, "spacing", ☃xxxxx);
               Validate.inclusiveBetween(0.0, Float.MAX_VALUE, ☃xxxxxxxxxxx, "Invalid spacing");
               ☃xxxxxxxxxxxx = JsonUtils.getFloat(☃xxxxxxxxxxxxx, "left", ☃xxxxxx);
               Validate.inclusiveBetween(0.0, Float.MAX_VALUE, ☃xxxxxxxxxxxx, "Invalid left");
            }

            ☃x[☃xxxxxxxx] = ☃xxxxxxxxxx;
            ☃xx[☃xxxxxxxx] = ☃xxxxxxxxxxx;
            ☃xxx[☃xxxxxxxx] = ☃xxxxxxxxxxxx;
         }
      }

      return new FontMetadataSection(☃x, ☃xxx, ☃xx);
   }

   @Override
   public String getSectionName() {
      return "font";
   }
}
