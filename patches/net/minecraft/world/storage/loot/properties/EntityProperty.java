package net.minecraft.world.storage.loot.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public interface EntityProperty {
   boolean testProperty(Random var1, Entity var2);

   public abstract static class Serializer<T extends EntityProperty> {
      private final ResourceLocation name;
      private final Class<T> propertyClass;

      protected Serializer(ResourceLocation var1, Class<T> var2) {
         this.name = ☃;
         this.propertyClass = ☃;
      }

      public ResourceLocation getName() {
         return this.name;
      }

      public Class<T> getPropertyClass() {
         return this.propertyClass;
      }

      public abstract JsonElement serialize(T var1, JsonSerializationContext var2);

      public abstract T deserialize(JsonElement var1, JsonDeserializationContext var2);
   }
}
