package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.resources.Language;
import net.minecraft.util.JsonUtils;

public class LanguageMetadataSectionSerializer extends BaseMetadataSectionSerializer<LanguageMetadataSection> {
   public LanguageMetadataSection deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
      JsonObject ☃ = ☃.getAsJsonObject();
      Set<Language> ☃x = Sets.newHashSet();

      for (Entry<String, JsonElement> ☃xx : ☃.entrySet()) {
         String ☃xxx = ☃xx.getKey();
         if (☃xxx.length() > 16) {
            throw new JsonParseException("Invalid language->'" + ☃xxx + "': language code must not be more than " + 16 + " characters long");
         }

         JsonObject ☃xxxx = JsonUtils.getJsonObject(☃xx.getValue(), "language");
         String ☃xxxxx = JsonUtils.getString(☃xxxx, "region");
         String ☃xxxxxx = JsonUtils.getString(☃xxxx, "name");
         boolean ☃xxxxxxx = JsonUtils.getBoolean(☃xxxx, "bidirectional", false);
         if (☃xxxxx.isEmpty()) {
            throw new JsonParseException("Invalid language->'" + ☃xxx + "'->region: empty value");
         }

         if (☃xxxxxx.isEmpty()) {
            throw new JsonParseException("Invalid language->'" + ☃xxx + "'->name: empty value");
         }

         if (!☃x.add(new Language(☃xxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx))) {
            throw new JsonParseException("Duplicate language->'" + ☃xxx + "' defined");
         }
      }

      return new LanguageMetadataSection(☃x);
   }

   @Override
   public String getSectionName() {
      return "language";
   }
}
