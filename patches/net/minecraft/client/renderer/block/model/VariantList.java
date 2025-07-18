package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.List;

public class VariantList {
   private final List<Variant> variantList;

   public VariantList(List<Variant> var1) {
      this.variantList = ☃;
   }

   public List<Variant> getVariantList() {
      return this.variantList;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ instanceof VariantList) {
         VariantList ☃ = (VariantList)☃;
         return this.variantList.equals(☃.variantList);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.variantList.hashCode();
   }

   public static class Deserializer implements JsonDeserializer<VariantList> {
      public VariantList deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         List<Variant> ☃ = Lists.newArrayList();
         if (☃.isJsonArray()) {
            JsonArray ☃x = ☃.getAsJsonArray();
            if (☃x.size() == 0) {
               throw new JsonParseException("Empty variant array");
            }

            for (JsonElement ☃xx : ☃x) {
               ☃.add((Variant)☃.deserialize(☃xx, Variant.class));
            }
         } else {
            ☃.add((Variant)☃.deserialize(☃, Variant.class));
         }

         return new VariantList(☃);
      }
   }
}
