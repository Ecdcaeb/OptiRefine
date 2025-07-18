package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.List;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class SoundListSerializer implements JsonDeserializer<SoundList> {
   public SoundList deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
      JsonObject ☃ = JsonUtils.getJsonObject(☃, "entry");
      boolean ☃x = JsonUtils.getBoolean(☃, "replace", false);
      String ☃xx = JsonUtils.getString(☃, "subtitle", null);
      List<Sound> ☃xxx = this.deserializeSounds(☃);
      return new SoundList(☃xxx, ☃x, ☃xx);
   }

   private List<Sound> deserializeSounds(JsonObject var1) {
      List<Sound> ☃ = Lists.newArrayList();
      if (☃.has("sounds")) {
         JsonArray ☃x = JsonUtils.getJsonArray(☃, "sounds");

         for (int ☃xx = 0; ☃xx < ☃x.size(); ☃xx++) {
            JsonElement ☃xxx = ☃x.get(☃xx);
            if (JsonUtils.isString(☃xxx)) {
               String ☃xxxx = JsonUtils.getString(☃xxx, "sound");
               ☃.add(new Sound(☃xxxx, 1.0F, 1.0F, 1, Sound.Type.FILE, false));
            } else {
               ☃.add(this.deserializeSound(JsonUtils.getJsonObject(☃xxx, "sound")));
            }
         }
      }

      return ☃;
   }

   private Sound deserializeSound(JsonObject var1) {
      String ☃ = JsonUtils.getString(☃, "name");
      Sound.Type ☃x = this.deserializeType(☃, Sound.Type.FILE);
      float ☃xx = JsonUtils.getFloat(☃, "volume", 1.0F);
      Validate.isTrue(☃xx > 0.0F, "Invalid volume", new Object[0]);
      float ☃xxx = JsonUtils.getFloat(☃, "pitch", 1.0F);
      Validate.isTrue(☃xxx > 0.0F, "Invalid pitch", new Object[0]);
      int ☃xxxx = JsonUtils.getInt(☃, "weight", 1);
      Validate.isTrue(☃xxxx > 0, "Invalid weight", new Object[0]);
      boolean ☃xxxxx = JsonUtils.getBoolean(☃, "stream", false);
      return new Sound(☃, ☃xx, ☃xxx, ☃xxxx, ☃x, ☃xxxxx);
   }

   private Sound.Type deserializeType(JsonObject var1, Sound.Type var2) {
      Sound.Type ☃ = ☃;
      if (☃.has("type")) {
         ☃ = Sound.Type.getByName(JsonUtils.getString(☃, "type"));
         Validate.notNull(☃, "Invalid type", new Object[0]);
      }

      return ☃;
   }
}
