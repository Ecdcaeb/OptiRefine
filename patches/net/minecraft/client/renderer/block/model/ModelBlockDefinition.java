package net.minecraft.client.renderer.block.model;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.multipart.Multipart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.util.JsonUtils;

public class ModelBlockDefinition {
   @VisibleForTesting
   static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(ModelBlockDefinition.class, new ModelBlockDefinition.Deserializer())
      .registerTypeAdapter(Variant.class, new Variant.Deserializer())
      .registerTypeAdapter(VariantList.class, new VariantList.Deserializer())
      .registerTypeAdapter(Multipart.class, new Multipart.Deserializer())
      .registerTypeAdapter(Selector.class, new Selector.Deserializer())
      .create();
   private final Map<String, VariantList> mapVariants = Maps.newHashMap();
   private Multipart multipart;

   public static ModelBlockDefinition parseFromReader(Reader var0) {
      return JsonUtils.fromJson(GSON, ☃, ModelBlockDefinition.class);
   }

   public ModelBlockDefinition(Map<String, VariantList> var1, Multipart var2) {
      this.multipart = ☃;
      this.mapVariants.putAll(☃);
   }

   public ModelBlockDefinition(List<ModelBlockDefinition> var1) {
      ModelBlockDefinition ☃ = null;

      for (ModelBlockDefinition ☃x : ☃) {
         if (☃x.hasMultipartData()) {
            this.mapVariants.clear();
            ☃ = ☃x;
         }

         this.mapVariants.putAll(☃x.mapVariants);
      }

      if (☃ != null) {
         this.multipart = ☃.multipart;
      }
   }

   public boolean hasVariant(String var1) {
      return this.mapVariants.get(☃) != null;
   }

   public VariantList getVariant(String var1) {
      VariantList ☃ = this.mapVariants.get(☃);
      if (☃ == null) {
         throw new ModelBlockDefinition.MissingVariantException();
      } else {
         return ☃;
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else {
         if (☃ instanceof ModelBlockDefinition) {
            ModelBlockDefinition ☃ = (ModelBlockDefinition)☃;
            if (this.mapVariants.equals(☃.mapVariants)) {
               return this.hasMultipartData() ? this.multipart.equals(☃.multipart) : !☃.hasMultipartData();
            }
         }

         return false;
      }
   }

   @Override
   public int hashCode() {
      return 31 * this.mapVariants.hashCode() + (this.hasMultipartData() ? this.multipart.hashCode() : 0);
   }

   public Set<VariantList> getMultipartVariants() {
      Set<VariantList> ☃ = Sets.newHashSet(this.mapVariants.values());
      if (this.hasMultipartData()) {
         ☃.addAll(this.multipart.getVariants());
      }

      return ☃;
   }

   public boolean hasMultipartData() {
      return this.multipart != null;
   }

   public Multipart getMultipartData() {
      return this.multipart;
   }

   public static class Deserializer implements JsonDeserializer<ModelBlockDefinition> {
      public ModelBlockDefinition deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         Map<String, VariantList> ☃x = this.parseMapVariants(☃, ☃);
         Multipart ☃xx = this.parseMultipart(☃, ☃);
         if (!☃x.isEmpty() || ☃xx != null && !☃xx.getVariants().isEmpty()) {
            return new ModelBlockDefinition(☃x, ☃xx);
         } else {
            throw new JsonParseException("Neither 'variants' nor 'multipart' found");
         }
      }

      protected Map<String, VariantList> parseMapVariants(JsonDeserializationContext var1, JsonObject var2) {
         Map<String, VariantList> ☃ = Maps.newHashMap();
         if (☃.has("variants")) {
            JsonObject ☃x = JsonUtils.getJsonObject(☃, "variants");

            for (Entry<String, JsonElement> ☃xx : ☃x.entrySet()) {
               ☃.put(☃xx.getKey(), (VariantList)☃.deserialize(☃xx.getValue(), VariantList.class));
            }
         }

         return ☃;
      }

      @Nullable
      protected Multipart parseMultipart(JsonDeserializationContext var1, JsonObject var2) {
         if (!☃.has("multipart")) {
            return null;
         } else {
            JsonArray ☃ = JsonUtils.getJsonArray(☃, "multipart");
            return (Multipart)☃.deserialize(☃, Multipart.class);
         }
      }
   }

   public class MissingVariantException extends RuntimeException {
      protected MissingVariantException() {
      }
   }
}
