package net.minecraft.world.storage.loot.functions;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnchantRandomly extends LootFunction {
   private static final Logger LOGGER = LogManager.getLogger();
   private final List<Enchantment> enchantments;

   public EnchantRandomly(LootCondition[] var1, @Nullable List<Enchantment> var2) {
      super(☃);
      this.enchantments = ☃ == null ? Collections.emptyList() : ☃;
   }

   @Override
   public ItemStack apply(ItemStack var1, Random var2, LootContext var3) {
      Enchantment ☃;
      if (this.enchantments.isEmpty()) {
         List<Enchantment> ☃x = Lists.newArrayList();

         for (Enchantment ☃xx : Enchantment.REGISTRY) {
            if (☃.getItem() == Items.BOOK || ☃xx.canApply(☃)) {
               ☃x.add(☃xx);
            }
         }

         if (☃x.isEmpty()) {
            LOGGER.warn("Couldn't find a compatible enchantment for {}", ☃);
            return ☃;
         }

         ☃ = ☃x.get(☃.nextInt(☃x.size()));
      } else {
         ☃ = this.enchantments.get(☃.nextInt(this.enchantments.size()));
      }

      int ☃x = MathHelper.getInt(☃, ☃.getMinLevel(), ☃.getMaxLevel());
      if (☃.getItem() == Items.BOOK) {
         ☃ = new ItemStack(Items.ENCHANTED_BOOK);
         ItemEnchantedBook.addEnchantment(☃, new EnchantmentData(☃, ☃x));
      } else {
         ☃.addEnchantment(☃, ☃x);
      }

      return ☃;
   }

   public static class Serializer extends LootFunction.Serializer<EnchantRandomly> {
      public Serializer() {
         super(new ResourceLocation("enchant_randomly"), EnchantRandomly.class);
      }

      public void serialize(JsonObject var1, EnchantRandomly var2, JsonSerializationContext var3) {
         if (!☃.enchantments.isEmpty()) {
            JsonArray ☃ = new JsonArray();

            for (Enchantment ☃x : ☃.enchantments) {
               ResourceLocation ☃xx = Enchantment.REGISTRY.getNameForObject(☃x);
               if (☃xx == null) {
                  throw new IllegalArgumentException("Don't know how to serialize enchantment " + ☃x);
               }

               ☃.add(new JsonPrimitive(☃xx.toString()));
            }

            ☃.add("enchantments", ☃);
         }
      }

      public EnchantRandomly deserialize(JsonObject var1, JsonDeserializationContext var2, LootCondition[] var3) {
         List<Enchantment> ☃ = Lists.newArrayList();
         if (☃.has("enchantments")) {
            for (JsonElement ☃x : JsonUtils.getJsonArray(☃, "enchantments")) {
               String ☃xx = JsonUtils.getString(☃x, "enchantment");
               Enchantment ☃xxx = Enchantment.REGISTRY.getObject(new ResourceLocation(☃xx));
               if (☃xxx == null) {
                  throw new JsonSyntaxException("Unknown enchantment '" + ☃xx + "'");
               }

               ☃.add(☃xxx);
            }
         }

         return new EnchantRandomly(☃, ☃);
      }
   }
}
