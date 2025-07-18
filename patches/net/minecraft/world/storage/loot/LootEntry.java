package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootEntry {
   protected final int weight;
   protected final int quality;
   protected final LootCondition[] conditions;

   protected LootEntry(int var1, int var2, LootCondition[] var3) {
      this.weight = ☃;
      this.quality = ☃;
      this.conditions = ☃;
   }

   public int getEffectiveWeight(float var1) {
      return Math.max(MathHelper.floor(this.weight + this.quality * ☃), 0);
   }

   public abstract void addLoot(Collection<ItemStack> var1, Random var2, LootContext var3);

   protected abstract void serialize(JsonObject var1, JsonSerializationContext var2);

   public static class Serializer implements JsonDeserializer<LootEntry>, JsonSerializer<LootEntry> {
      public LootEntry deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "loot item");
         String ☃x = JsonUtils.getString(☃, "type");
         int ☃xx = JsonUtils.getInt(☃, "weight", 1);
         int ☃xxx = JsonUtils.getInt(☃, "quality", 0);
         LootCondition[] ☃xxxx;
         if (☃.has("conditions")) {
            ☃xxxx = JsonUtils.deserializeClass(☃, "conditions", ☃, LootCondition[].class);
         } else {
            ☃xxxx = new LootCondition[0];
         }

         if ("item".equals(☃x)) {
            return LootEntryItem.deserialize(☃, ☃, ☃xx, ☃xxx, ☃xxxx);
         } else if ("loot_table".equals(☃x)) {
            return LootEntryTable.deserialize(☃, ☃, ☃xx, ☃xxx, ☃xxxx);
         } else if ("empty".equals(☃x)) {
            return LootEntryEmpty.deserialize(☃, ☃, ☃xx, ☃xxx, ☃xxxx);
         } else {
            throw new JsonSyntaxException("Unknown loot entry type '" + ☃x + "'");
         }
      }

      public JsonElement serialize(LootEntry var1, Type var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();
         ☃.addProperty("weight", ☃.weight);
         ☃.addProperty("quality", ☃.quality);
         if (☃.conditions.length > 0) {
            ☃.add("conditions", ☃.serialize(☃.conditions));
         }

         if (☃ instanceof LootEntryItem) {
            ☃.addProperty("type", "item");
         } else if (☃ instanceof LootEntryTable) {
            ☃.addProperty("type", "loot_table");
         } else {
            if (!(☃ instanceof LootEntryEmpty)) {
               throw new IllegalArgumentException("Don't know how to serialize " + ☃);
            }

            ☃.addProperty("type", "empty");
         }

         ☃.serialize(☃, ☃);
         return ☃;
      }
   }
}
