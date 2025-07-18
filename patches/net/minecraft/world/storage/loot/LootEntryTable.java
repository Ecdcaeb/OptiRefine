package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Collection;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootEntryTable extends LootEntry {
   protected final ResourceLocation table;

   public LootEntryTable(ResourceLocation var1, int var2, int var3, LootCondition[] var4) {
      super(☃, ☃, ☃);
      this.table = ☃;
   }

   @Override
   public void addLoot(Collection<ItemStack> var1, Random var2, LootContext var3) {
      LootTable ☃ = ☃.getLootTableManager().getLootTableFromLocation(this.table);
      Collection<ItemStack> ☃x = ☃.generateLootForPools(☃, ☃);
      ☃.addAll(☃x);
   }

   @Override
   protected void serialize(JsonObject var1, JsonSerializationContext var2) {
      ☃.addProperty("name", this.table.toString());
   }

   public static LootEntryTable deserialize(JsonObject var0, JsonDeserializationContext var1, int var2, int var3, LootCondition[] var4) {
      ResourceLocation ☃ = new ResourceLocation(JsonUtils.getString(☃, "name"));
      return new LootEntryTable(☃, ☃, ☃, ☃);
   }
}
