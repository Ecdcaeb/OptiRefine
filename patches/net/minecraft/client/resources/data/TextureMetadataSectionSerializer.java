package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;

public class TextureMetadataSectionSerializer extends BaseMetadataSectionSerializer<TextureMetadataSection> {
   public TextureMetadataSection deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
      JsonObject ☃ = ☃.getAsJsonObject();
      boolean ☃x = JsonUtils.getBoolean(☃, "blur", false);
      boolean ☃xx = JsonUtils.getBoolean(☃, "clamp", false);
      return new TextureMetadataSection(☃x, ☃xx);
   }

   @Override
   public String getSectionName() {
      return "texture";
   }
}
