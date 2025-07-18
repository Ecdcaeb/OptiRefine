package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EnchantmentPredicate {
   public static final EnchantmentPredicate ANY = new EnchantmentPredicate();
   private final Enchantment enchantment;
   private final MinMaxBounds levels;

   public EnchantmentPredicate() {
      this.enchantment = null;
      this.levels = MinMaxBounds.UNBOUNDED;
   }

   public EnchantmentPredicate(@Nullable Enchantment var1, MinMaxBounds var2) {
      this.enchantment = ☃;
      this.levels = ☃;
   }

   public boolean test(Map<Enchantment, Integer> var1) {
      if (this.enchantment != null) {
         if (!☃.containsKey(this.enchantment)) {
            return false;
         }

         int ☃ = ☃.get(this.enchantment);
         if (this.levels != null && !this.levels.test(☃)) {
            return false;
         }
      } else if (this.levels != null) {
         for (Integer ☃ : ☃.values()) {
            if (this.levels.test(☃.intValue())) {
               return true;
            }
         }

         return false;
      }

      return true;
   }

   public static EnchantmentPredicate deserialize(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "enchantment");
         Enchantment ☃x = null;
         if (☃.has("enchantment")) {
            ResourceLocation ☃xx = new ResourceLocation(JsonUtils.getString(☃, "enchantment"));
            ☃x = Enchantment.REGISTRY.getObject(☃xx);
            if (☃x == null) {
               throw new JsonSyntaxException("Unknown enchantment '" + ☃xx + "'");
            }
         }

         MinMaxBounds ☃xx = MinMaxBounds.deserialize(☃.get("levels"));
         return new EnchantmentPredicate(☃x, ☃xx);
      } else {
         return ANY;
      }
   }

   public static EnchantmentPredicate[] deserializeArray(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonArray ☃ = JsonUtils.getJsonArray(☃, "enchantments");
         EnchantmentPredicate[] ☃x = new EnchantmentPredicate[☃.size()];

         for (int ☃xx = 0; ☃xx < ☃x.length; ☃xx++) {
            ☃x[☃xx] = deserialize(☃.get(☃xx));
         }

         return ☃x;
      } else {
         return new EnchantmentPredicate[0];
      }
   }
}
