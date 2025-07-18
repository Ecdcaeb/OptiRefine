package net.minecraft.world.storage.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import org.apache.commons.lang3.ArrayUtils;

public class LootPool {
   private final LootEntry[] lootEntries;
   private final LootCondition[] poolConditions;
   private final RandomValueRange rolls;
   private final RandomValueRange bonusRolls;

   public LootPool(LootEntry[] var1, LootCondition[] var2, RandomValueRange var3, RandomValueRange var4) {
      this.lootEntries = ☃;
      this.poolConditions = ☃;
      this.rolls = ☃;
      this.bonusRolls = ☃;
   }

   protected void createLootRoll(Collection<ItemStack> var1, Random var2, LootContext var3) {
      List<LootEntry> ☃ = Lists.newArrayList();
      int ☃x = 0;

      for (LootEntry ☃xx : this.lootEntries) {
         if (LootConditionManager.testAllConditions(☃xx.conditions, ☃, ☃)) {
            int ☃xxx = ☃xx.getEffectiveWeight(☃.getLuck());
            if (☃xxx > 0) {
               ☃.add(☃xx);
               ☃x += ☃xxx;
            }
         }
      }

      if (☃x != 0 && !☃.isEmpty()) {
         int ☃xxx = ☃.nextInt(☃x);

         for (LootEntry ☃xxxx : ☃) {
            ☃xxx -= ☃xxxx.getEffectiveWeight(☃.getLuck());
            if (☃xxx < 0) {
               ☃xxxx.addLoot(☃, ☃, ☃);
               return;
            }
         }
      }
   }

   public void generateLoot(Collection<ItemStack> var1, Random var2, LootContext var3) {
      if (LootConditionManager.testAllConditions(this.poolConditions, ☃, ☃)) {
         int ☃ = this.rolls.generateInt(☃) + MathHelper.floor(this.bonusRolls.generateFloat(☃) * ☃.getLuck());

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            this.createLootRoll(☃, ☃, ☃);
         }
      }
   }

   public static class Serializer implements JsonDeserializer<LootPool>, JsonSerializer<LootPool> {
      public LootPool deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "loot pool");
         LootEntry[] ☃x = JsonUtils.deserializeClass(☃, "entries", ☃, LootEntry[].class);
         LootCondition[] ☃xx = JsonUtils.deserializeClass(☃, "conditions", new LootCondition[0], ☃, LootCondition[].class);
         RandomValueRange ☃xxx = JsonUtils.deserializeClass(☃, "rolls", ☃, RandomValueRange.class);
         RandomValueRange ☃xxxx = JsonUtils.deserializeClass(☃, "bonus_rolls", new RandomValueRange(0.0F, 0.0F), ☃, RandomValueRange.class);
         return new LootPool(☃x, ☃xx, ☃xxx, ☃xxxx);
      }

      public JsonElement serialize(LootPool var1, Type var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();
         ☃.add("entries", ☃.serialize(☃.lootEntries));
         ☃.add("rolls", ☃.serialize(☃.rolls));
         if (☃.bonusRolls.getMin() != 0.0F && ☃.bonusRolls.getMax() != 0.0F) {
            ☃.add("bonus_rolls", ☃.serialize(☃.bonusRolls));
         }

         if (!ArrayUtils.isEmpty(☃.poolConditions)) {
            ☃.add("conditions", ☃.serialize(☃.poolConditions));
         }

         return ☃;
      }
   }
}
