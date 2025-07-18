package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Smelt extends LootFunction {
   private static final Logger LOGGER = LogManager.getLogger();

   public Smelt(LootCondition[] var1) {
      super(☃);
   }

   @Override
   public ItemStack apply(ItemStack var1, Random var2, LootContext var3) {
      if (☃.isEmpty()) {
         return ☃;
      } else {
         ItemStack ☃ = FurnaceRecipes.instance().getSmeltingResult(☃);
         if (☃.isEmpty()) {
            LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", ☃);
            return ☃;
         } else {
            ItemStack ☃x = ☃.copy();
            ☃x.setCount(☃.getCount());
            return ☃x;
         }
      }
   }

   public static class Serializer extends LootFunction.Serializer<Smelt> {
      protected Serializer() {
         super(new ResourceLocation("furnace_smelt"), Smelt.class);
      }

      public void serialize(JsonObject var1, Smelt var2, JsonSerializationContext var3) {
      }

      public Smelt deserialize(JsonObject var1, JsonDeserializationContext var2, LootCondition[] var3) {
         return new Smelt(☃);
      }
   }
}
