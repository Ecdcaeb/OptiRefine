package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.properties.EntityProperty;
import net.minecraft.world.storage.loot.properties.EntityPropertyManager;

public class EntityHasProperty implements LootCondition {
   private final EntityProperty[] properties;
   private final LootContext.EntityTarget target;

   public EntityHasProperty(EntityProperty[] var1, LootContext.EntityTarget var2) {
      this.properties = ☃;
      this.target = ☃;
   }

   @Override
   public boolean testCondition(Random var1, LootContext var2) {
      Entity ☃ = ☃.getEntity(this.target);
      if (☃ == null) {
         return false;
      } else {
         for (EntityProperty ☃x : this.properties) {
            if (!☃x.testProperty(☃, ☃)) {
               return false;
            }
         }

         return true;
      }
   }

   public static class Serializer extends LootCondition.Serializer<EntityHasProperty> {
      protected Serializer() {
         super(new ResourceLocation("entity_properties"), EntityHasProperty.class);
      }

      public void serialize(JsonObject var1, EntityHasProperty var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();

         for (EntityProperty ☃x : ☃.properties) {
            EntityProperty.Serializer<EntityProperty> ☃xx = EntityPropertyManager.getSerializerFor(☃x);
            ☃.add(☃xx.getName().toString(), ☃xx.serialize(☃x, ☃));
         }

         ☃.add("properties", ☃);
         ☃.add("entity", ☃.serialize(☃.target));
      }

      public EntityHasProperty deserialize(JsonObject var1, JsonDeserializationContext var2) {
         Set<Entry<String, JsonElement>> ☃ = JsonUtils.getJsonObject(☃, "properties").entrySet();
         EntityProperty[] ☃x = new EntityProperty[☃.size()];
         int ☃xx = 0;

         for (Entry<String, JsonElement> ☃xxx : ☃) {
            ☃x[☃xx++] = EntityPropertyManager.getSerializerForName(new ResourceLocation(☃xxx.getKey())).deserialize(☃xxx.getValue(), ☃);
         }

         return new EntityHasProperty(☃x, JsonUtils.deserializeClass(☃, "entity", ☃, LootContext.EntityTarget.class));
      }
   }
}
