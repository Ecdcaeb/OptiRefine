package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class EnchantWithLevels extends LootFunction {
   private final RandomValueRange randomLevel;
   private final boolean isTreasure;

   public EnchantWithLevels(LootCondition[] var1, RandomValueRange var2, boolean var3) {
      super(☃);
      this.randomLevel = ☃;
      this.isTreasure = ☃;
   }

   @Override
   public ItemStack apply(ItemStack var1, Random var2, LootContext var3) {
      return EnchantmentHelper.addRandomEnchantment(☃, ☃, this.randomLevel.generateInt(☃), this.isTreasure);
   }

   public static class Serializer extends LootFunction.Serializer<EnchantWithLevels> {
      public Serializer() {
         super(new ResourceLocation("enchant_with_levels"), EnchantWithLevels.class);
      }

      public void serialize(JsonObject var1, EnchantWithLevels var2, JsonSerializationContext var3) {
         ☃.add("levels", ☃.serialize(☃.randomLevel));
         ☃.addProperty("treasure", ☃.isTreasure);
      }

      public EnchantWithLevels deserialize(JsonObject var1, JsonDeserializationContext var2, LootCondition[] var3) {
         RandomValueRange ☃ = JsonUtils.deserializeClass(☃, "levels", ☃, RandomValueRange.class);
         boolean ☃x = JsonUtils.getBoolean(☃, "treasure", false);
         return new EnchantWithLevels(☃, ☃, ☃x);
      }
   }
}
