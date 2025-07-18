package net.minecraft.world.storage.loot.conditions;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public class LootConditionManager {
   private static final Map<ResourceLocation, LootCondition.Serializer<?>> NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
   private static final Map<Class<? extends LootCondition>, LootCondition.Serializer<?>> CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();

   public static <T extends LootCondition> void registerCondition(LootCondition.Serializer<? extends T> var0) {
      ResourceLocation ☃ = ☃.getLootTableLocation();
      Class<T> ☃x = (Class<T>)☃.getConditionClass();
      if (NAME_TO_SERIALIZER_MAP.containsKey(☃)) {
         throw new IllegalArgumentException("Can't re-register item condition name " + ☃);
      } else if (CLASS_TO_SERIALIZER_MAP.containsKey(☃x)) {
         throw new IllegalArgumentException("Can't re-register item condition class " + ☃x.getName());
      } else {
         NAME_TO_SERIALIZER_MAP.put(☃, ☃);
         CLASS_TO_SERIALIZER_MAP.put(☃x, ☃);
      }
   }

   public static boolean testAllConditions(@Nullable LootCondition[] var0, Random var1, LootContext var2) {
      if (☃ == null) {
         return true;
      } else {
         for (LootCondition ☃ : ☃) {
            if (!☃.testCondition(☃, ☃)) {
               return false;
            }
         }

         return true;
      }
   }

   public static LootCondition.Serializer<?> getSerializerForName(ResourceLocation var0) {
      LootCondition.Serializer<?> ☃ = NAME_TO_SERIALIZER_MAP.get(☃);
      if (☃ == null) {
         throw new IllegalArgumentException("Unknown loot item condition '" + ☃ + "'");
      } else {
         return ☃;
      }
   }

   public static <T extends LootCondition> LootCondition.Serializer<T> getSerializerFor(T var0) {
      LootCondition.Serializer<T> ☃ = (LootCondition.Serializer<T>)CLASS_TO_SERIALIZER_MAP.get(☃.getClass());
      if (☃ == null) {
         throw new IllegalArgumentException("Unknown loot item condition " + ☃);
      } else {
         return ☃;
      }
   }

   static {
      registerCondition(new RandomChance.Serializer());
      registerCondition(new RandomChanceWithLooting.Serializer());
      registerCondition(new EntityHasProperty.Serializer());
      registerCondition(new KilledByPlayer.Serializer());
      registerCondition(new EntityHasScore.Serializer());
   }

   public static class Serializer implements JsonDeserializer<LootCondition>, JsonSerializer<LootCondition> {
      public LootCondition deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "condition");
         ResourceLocation ☃x = new ResourceLocation(JsonUtils.getString(☃, "condition"));

         LootCondition.Serializer<?> ☃xx;
         try {
            ☃xx = LootConditionManager.getSerializerForName(☃x);
         } catch (IllegalArgumentException var8) {
            throw new JsonSyntaxException("Unknown condition '" + ☃x + "'");
         }

         return ☃xx.deserialize(☃, ☃);
      }

      public JsonElement serialize(LootCondition var1, Type var2, JsonSerializationContext var3) {
         LootCondition.Serializer<LootCondition> ☃ = LootConditionManager.getSerializerFor(☃);
         JsonObject ☃x = new JsonObject();
         ☃.serialize(☃x, ☃, ☃);
         ☃x.addProperty("condition", ☃.getLootTableLocation().toString());
         return ☃x;
      }
   }
}
