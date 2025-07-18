package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.ITextComponent;

public class PackMetadataSectionSerializer extends BaseMetadataSectionSerializer<PackMetadataSection> implements JsonSerializer<PackMetadataSection> {
   public PackMetadataSection deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
      JsonObject ☃ = ☃.getAsJsonObject();
      ITextComponent ☃x = (ITextComponent)☃.deserialize(☃.get("description"), ITextComponent.class);
      if (☃x == null) {
         throw new JsonParseException("Invalid/missing description!");
      } else {
         int ☃xx = JsonUtils.getInt(☃, "pack_format");
         return new PackMetadataSection(☃x, ☃xx);
      }
   }

   public JsonElement serialize(PackMetadataSection var1, Type var2, JsonSerializationContext var3) {
      JsonObject ☃ = new JsonObject();
      ☃.addProperty("pack_format", ☃.getPackFormat());
      ☃.add("description", ☃.serialize(☃.getPackDescription()));
      return ☃;
   }

   @Override
   public String getSectionName() {
      return "pack";
   }
}
