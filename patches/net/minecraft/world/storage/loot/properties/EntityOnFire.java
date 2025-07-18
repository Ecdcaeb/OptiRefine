package net.minecraft.world.storage.loot.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EntityOnFire implements EntityProperty {
   private final boolean onFire;

   public EntityOnFire(boolean var1) {
      this.onFire = ☃;
   }

   @Override
   public boolean testProperty(Random var1, Entity var2) {
      return ☃.isBurning() == this.onFire;
   }

   public static class Serializer extends EntityProperty.Serializer<EntityOnFire> {
      protected Serializer() {
         super(new ResourceLocation("on_fire"), EntityOnFire.class);
      }

      public JsonElement serialize(EntityOnFire var1, JsonSerializationContext var2) {
         return new JsonPrimitive(☃.onFire);
      }

      public EntityOnFire deserialize(JsonElement var1, JsonDeserializationContext var2) {
         return new EntityOnFire(JsonUtils.getBoolean(☃, "on_fire"));
      }
   }
}
