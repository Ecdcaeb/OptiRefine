package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class Variant {
   private final ResourceLocation modelLocation;
   private final ModelRotation rotation;
   private final boolean uvLock;
   private final int weight;

   public Variant(ResourceLocation var1, ModelRotation var2, boolean var3, int var4) {
      this.modelLocation = ☃;
      this.rotation = ☃;
      this.uvLock = ☃;
      this.weight = ☃;
   }

   public ResourceLocation getModelLocation() {
      return this.modelLocation;
   }

   public ModelRotation getRotation() {
      return this.rotation;
   }

   public boolean isUvLock() {
      return this.uvLock;
   }

   public int getWeight() {
      return this.weight;
   }

   @Override
   public String toString() {
      return "Variant{modelLocation=" + this.modelLocation + ", rotation=" + this.rotation + ", uvLock=" + this.uvLock + ", weight=" + this.weight + '}';
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof Variant)) {
         return false;
      } else {
         Variant ☃ = (Variant)☃;
         return this.modelLocation.equals(☃.modelLocation) && this.rotation == ☃.rotation && this.uvLock == ☃.uvLock && this.weight == ☃.weight;
      }
   }

   @Override
   public int hashCode() {
      int ☃ = this.modelLocation.hashCode();
      ☃ = 31 * ☃ + this.rotation.hashCode();
      ☃ = 31 * ☃ + Boolean.valueOf(this.uvLock).hashCode();
      return 31 * ☃ + this.weight;
   }

   public static class Deserializer implements JsonDeserializer<Variant> {
      public Variant deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         String ☃x = this.getStringModel(☃);
         ModelRotation ☃xx = this.parseModelRotation(☃);
         boolean ☃xxx = this.parseUvLock(☃);
         int ☃xxxx = this.parseWeight(☃);
         return new Variant(this.getResourceLocationBlock(☃x), ☃xx, ☃xxx, ☃xxxx);
      }

      private ResourceLocation getResourceLocationBlock(String var1) {
         ResourceLocation ☃ = new ResourceLocation(☃);
         return new ResourceLocation(☃.getNamespace(), "block/" + ☃.getPath());
      }

      private boolean parseUvLock(JsonObject var1) {
         return JsonUtils.getBoolean(☃, "uvlock", false);
      }

      protected ModelRotation parseModelRotation(JsonObject var1) {
         int ☃ = JsonUtils.getInt(☃, "x", 0);
         int ☃x = JsonUtils.getInt(☃, "y", 0);
         ModelRotation ☃xx = ModelRotation.getModelRotation(☃, ☃x);
         if (☃xx == null) {
            throw new JsonParseException("Invalid BlockModelRotation x: " + ☃ + ", y: " + ☃x);
         } else {
            return ☃xx;
         }
      }

      protected String getStringModel(JsonObject var1) {
         return JsonUtils.getString(☃, "model");
      }

      protected int parseWeight(JsonObject var1) {
         int ☃ = JsonUtils.getInt(☃, "weight", 1);
         if (☃ < 1) {
            throw new JsonParseException("Invalid weight " + ☃ + " found, expected integer >= 1");
         } else {
            return ☃;
         }
      }
   }
}
