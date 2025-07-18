package net.minecraft.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Locale;
import org.apache.commons.lang3.Validate;

public class ResourceLocation implements Comparable<ResourceLocation> {
   protected final String namespace;
   protected final String path;

   protected ResourceLocation(int var1, String... var2) {
      this.namespace = org.apache.commons.lang3.StringUtils.isEmpty(☃[0]) ? "minecraft" : ☃[0].toLowerCase(Locale.ROOT);
      this.path = ☃[1].toLowerCase(Locale.ROOT);
      Validate.notNull(this.path);
   }

   public ResourceLocation(String var1) {
      this(0, splitObjectName(☃));
   }

   public ResourceLocation(String var1, String var2) {
      this(0, ☃, ☃);
   }

   protected static String[] splitObjectName(String var0) {
      String[] ☃ = new String[]{"minecraft", ☃};
      int ☃x = ☃.indexOf(58);
      if (☃x >= 0) {
         ☃[1] = ☃.substring(☃x + 1, ☃.length());
         if (☃x > 1) {
            ☃[0] = ☃.substring(0, ☃x);
         }
      }

      return ☃;
   }

   public String getPath() {
      return this.path;
   }

   public String getNamespace() {
      return this.namespace;
   }

   @Override
   public String toString() {
      return this.namespace + ':' + this.path;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof ResourceLocation)) {
         return false;
      } else {
         ResourceLocation ☃ = (ResourceLocation)☃;
         return this.namespace.equals(☃.namespace) && this.path.equals(☃.path);
      }
   }

   @Override
   public int hashCode() {
      return 31 * this.namespace.hashCode() + this.path.hashCode();
   }

   public int compareTo(ResourceLocation var1) {
      int ☃ = this.namespace.compareTo(☃.namespace);
      if (☃ == 0) {
         ☃ = this.path.compareTo(☃.path);
      }

      return ☃;
   }

   public static class Serializer implements JsonDeserializer<ResourceLocation>, JsonSerializer<ResourceLocation> {
      public ResourceLocation deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return new ResourceLocation(JsonUtils.getString(☃, "location"));
      }

      public JsonElement serialize(ResourceLocation var1, Type var2, JsonSerializationContext var3) {
         return new JsonPrimitive(☃.toString());
      }
   }
}
