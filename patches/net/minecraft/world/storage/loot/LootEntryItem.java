package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Collection;
import java.util.Random;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class LootEntryItem extends LootEntry {
   protected final Item item;
   protected final LootFunction[] functions;

   public LootEntryItem(Item var1, int var2, int var3, LootFunction[] var4, LootCondition[] var5) {
      super(☃, ☃, ☃);
      this.item = ☃;
      this.functions = ☃;
   }

   @Override
   public void addLoot(Collection<ItemStack> var1, Random var2, LootContext var3) {
      ItemStack ☃ = new ItemStack(this.item);

      for (LootFunction ☃x : this.functions) {
         if (LootConditionManager.testAllConditions(☃x.getConditions(), ☃, ☃)) {
            ☃ = ☃x.apply(☃, ☃, ☃);
         }
      }

      if (!☃.isEmpty()) {
         if (☃.getCount() < this.item.getItemStackLimit()) {
            ☃.add(☃);
         } else {
            int ☃xx = ☃.getCount();

            while (☃xx > 0) {
               ItemStack ☃xxx = ☃.copy();
               ☃xxx.setCount(Math.min(☃.getMaxStackSize(), ☃xx));
               ☃xx -= ☃xxx.getCount();
               ☃.add(☃xxx);
            }
         }
      }
   }

   @Override
   protected void serialize(JsonObject var1, JsonSerializationContext var2) {
      if (this.functions != null && this.functions.length > 0) {
         ☃.add("functions", ☃.serialize(this.functions));
      }

      ResourceLocation ☃ = Item.REGISTRY.getNameForObject(this.item);
      if (☃ == null) {
         throw new IllegalArgumentException("Can't serialize unknown item " + this.item);
      } else {
         ☃.addProperty("name", ☃.toString());
      }
   }

   public static LootEntryItem deserialize(JsonObject var0, JsonDeserializationContext var1, int var2, int var3, LootCondition[] var4) {
      Item ☃ = JsonUtils.getItem(☃, "name");
      LootFunction[] ☃x;
      if (☃.has("functions")) {
         ☃x = JsonUtils.deserializeClass(☃, "functions", ☃, LootFunction[].class);
      } else {
         ☃x = new LootFunction[0];
      }

      return new LootEntryItem(☃, ☃, ☃, ☃x, ☃);
   }
}
