package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootingEnchantBonus extends LootFunction {
   private final RandomValueRange count;
   private final int limit;

   public LootingEnchantBonus(LootCondition[] var1, RandomValueRange var2, int var3) {
      super(☃);
      this.count = ☃;
      this.limit = ☃;
   }

   @Override
   public ItemStack apply(ItemStack var1, Random var2, LootContext var3) {
      Entity ☃ = ☃.getKiller();
      if (☃ instanceof EntityLivingBase) {
         int ☃x = EnchantmentHelper.getLootingModifier((EntityLivingBase)☃);
         if (☃x == 0) {
            return ☃;
         }

         float ☃xx = ☃x * this.count.generateFloat(☃);
         ☃.grow(Math.round(☃xx));
         if (this.limit != 0 && ☃.getCount() > this.limit) {
            ☃.setCount(this.limit);
         }
      }

      return ☃;
   }

   public static class Serializer extends LootFunction.Serializer<LootingEnchantBonus> {
      protected Serializer() {
         super(new ResourceLocation("looting_enchant"), LootingEnchantBonus.class);
      }

      public void serialize(JsonObject var1, LootingEnchantBonus var2, JsonSerializationContext var3) {
         ☃.add("count", ☃.serialize(☃.count));
         if (☃.limit > 0) {
            ☃.add("limit", ☃.serialize(☃.limit));
         }
      }

      public LootingEnchantBonus deserialize(JsonObject var1, JsonDeserializationContext var2, LootCondition[] var3) {
         int ☃ = JsonUtils.getInt(☃, "limit", 0);
         return new LootingEnchantBonus(☃, JsonUtils.deserializeClass(☃, "count", ☃, RandomValueRange.class), ☃);
      }
   }
}
