package net.minecraft.world.storage.loot.functions;

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
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootFunctionManager {
   private static final Map<ResourceLocation, LootFunction.Serializer<?>> NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
   private static final Map<Class<? extends LootFunction>, LootFunction.Serializer<?>> CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();

   public static <T extends LootFunction> void registerFunction(LootFunction.Serializer<? extends T> var0) {
      ResourceLocation ☃ = ☃.getFunctionName();
      Class<T> ☃x = (Class<T>)☃.getFunctionClass();
      if (NAME_TO_SERIALIZER_MAP.containsKey(☃)) {
         throw new IllegalArgumentException("Can't re-register item function name " + ☃);
      } else if (CLASS_TO_SERIALIZER_MAP.containsKey(☃x)) {
         throw new IllegalArgumentException("Can't re-register item function class " + ☃x.getName());
      } else {
         NAME_TO_SERIALIZER_MAP.put(☃, ☃);
         CLASS_TO_SERIALIZER_MAP.put(☃x, ☃);
      }
   }

   public static LootFunction.Serializer<?> getSerializerForName(ResourceLocation var0) {
      LootFunction.Serializer<?> ☃ = NAME_TO_SERIALIZER_MAP.get(☃);
      if (☃ == null) {
         throw new IllegalArgumentException("Unknown loot item function '" + ☃ + "'");
      } else {
         return ☃;
      }
   }

   public static <T extends LootFunction> LootFunction.Serializer<T> getSerializerFor(T var0) {
      LootFunction.Serializer<T> ☃ = (LootFunction.Serializer<T>)CLASS_TO_SERIALIZER_MAP.get(☃.getClass());
      if (☃ == null) {
         throw new IllegalArgumentException("Unknown loot item function " + ☃);
      } else {
         return ☃;
      }
   }

   static {
      registerFunction(new SetCount.Serializer());
      registerFunction(new SetMetadata.Serializer());
      registerFunction(new EnchantWithLevels.Serializer());
      registerFunction(new EnchantRandomly.Serializer());
      registerFunction(new SetNBT.Serializer());
      registerFunction(new Smelt.Serializer());
      registerFunction(new LootingEnchantBonus.Serializer());
      registerFunction(new SetDamage.Serializer());
      registerFunction(new SetAttributes.Serializer());
   }

   public static class Serializer implements JsonDeserializer<LootFunction>, JsonSerializer<LootFunction> {
      public LootFunction deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "function");
         ResourceLocation ☃x = new ResourceLocation(JsonUtils.getString(☃, "function"));

         LootFunction.Serializer<?> ☃xx;
         try {
            ☃xx = LootFunctionManager.getSerializerForName(☃x);
         } catch (IllegalArgumentException var8) {
            throw new JsonSyntaxException("Unknown function '" + ☃x + "'");
         }

         return ☃xx.deserialize(☃, ☃, JsonUtils.deserializeClass(☃, "conditions", new LootCondition[0], ☃, LootCondition[].class));
      }

      public JsonElement serialize(LootFunction var1, Type var2, JsonSerializationContext var3) {
         LootFunction.Serializer<LootFunction> ☃ = LootFunctionManager.getSerializerFor(☃);
         JsonObject ☃x = new JsonObject();
         ☃.serialize(☃x, ☃, ☃);
         ☃x.addProperty("function", ☃.getFunctionName().toString());
         if (☃.getConditions() != null && ☃.getConditions().length > 0) {
            ☃x.add("conditions", ☃.serialize(☃.getConditions()));
         }

         return ☃x;
      }
   }
}
