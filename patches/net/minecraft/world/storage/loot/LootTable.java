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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootTable {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final LootTable EMPTY_LOOT_TABLE = new LootTable(new LootPool[0]);
   private final LootPool[] pools;

   public LootTable(LootPool[] var1) {
      this.pools = ☃;
   }

   public List<ItemStack> generateLootForPools(Random var1, LootContext var2) {
      List<ItemStack> ☃ = Lists.newArrayList();
      if (☃.addLootTable(this)) {
         for (LootPool ☃x : this.pools) {
            ☃x.generateLoot(☃, ☃, ☃);
         }

         ☃.removeLootTable(this);
      } else {
         LOGGER.warn("Detected infinite loop in loot tables");
      }

      return ☃;
   }

   public void fillInventory(IInventory var1, Random var2, LootContext var3) {
      List<ItemStack> ☃ = this.generateLootForPools(☃, ☃);
      List<Integer> ☃x = this.getEmptySlotsRandomized(☃, ☃);
      this.shuffleItems(☃, ☃x.size(), ☃);

      for (ItemStack ☃xx : ☃) {
         if (☃x.isEmpty()) {
            LOGGER.warn("Tried to over-fill a container");
            return;
         }

         if (☃xx.isEmpty()) {
            ☃.setInventorySlotContents(☃x.remove(☃x.size() - 1), ItemStack.EMPTY);
         } else {
            ☃.setInventorySlotContents(☃x.remove(☃x.size() - 1), ☃xx);
         }
      }
   }

   private void shuffleItems(List<ItemStack> var1, int var2, Random var3) {
      List<ItemStack> ☃ = Lists.newArrayList();
      Iterator<ItemStack> ☃x = ☃.iterator();

      while (☃x.hasNext()) {
         ItemStack ☃xx = ☃x.next();
         if (☃xx.isEmpty()) {
            ☃x.remove();
         } else if (☃xx.getCount() > 1) {
            ☃.add(☃xx);
            ☃x.remove();
         }
      }

      ☃ -= ☃.size();

      while (☃ > 0 && !☃.isEmpty()) {
         ItemStack ☃xx = ☃.remove(MathHelper.getInt(☃, 0, ☃.size() - 1));
         int ☃xxx = MathHelper.getInt(☃, 1, ☃xx.getCount() / 2);
         ItemStack ☃xxxx = ☃xx.splitStack(☃xxx);
         if (☃xx.getCount() > 1 && ☃.nextBoolean()) {
            ☃.add(☃xx);
         } else {
            ☃.add(☃xx);
         }

         if (☃xxxx.getCount() > 1 && ☃.nextBoolean()) {
            ☃.add(☃xxxx);
         } else {
            ☃.add(☃xxxx);
         }
      }

      ☃.addAll(☃);
      Collections.shuffle(☃, ☃);
   }

   private List<Integer> getEmptySlotsRandomized(IInventory var1, Random var2) {
      List<Integer> ☃ = Lists.newArrayList();

      for (int ☃x = 0; ☃x < ☃.getSizeInventory(); ☃x++) {
         if (☃.getStackInSlot(☃x).isEmpty()) {
            ☃.add(☃x);
         }
      }

      Collections.shuffle(☃, ☃);
      return ☃;
   }

   public static class Serializer implements JsonDeserializer<LootTable>, JsonSerializer<LootTable> {
      public LootTable deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "loot table");
         LootPool[] ☃x = JsonUtils.deserializeClass(☃, "pools", new LootPool[0], ☃, LootPool[].class);
         return new LootTable(☃x);
      }

      public JsonElement serialize(LootTable var1, Type var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();
         ☃.add("pools", ☃.serialize(☃.pools));
         return ☃;
      }
   }
}
