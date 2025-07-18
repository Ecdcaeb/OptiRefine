package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class ItemPredicate {
   public static final ItemPredicate ANY = new ItemPredicate();
   private final Item item;
   private final Integer data;
   private final MinMaxBounds count;
   private final MinMaxBounds durability;
   private final EnchantmentPredicate[] enchantments;
   private final PotionType potion;
   private final NBTPredicate nbt;

   public ItemPredicate() {
      this.item = null;
      this.data = null;
      this.potion = null;
      this.count = MinMaxBounds.UNBOUNDED;
      this.durability = MinMaxBounds.UNBOUNDED;
      this.enchantments = new EnchantmentPredicate[0];
      this.nbt = NBTPredicate.ANY;
   }

   public ItemPredicate(
      @Nullable Item var1,
      @Nullable Integer var2,
      MinMaxBounds var3,
      MinMaxBounds var4,
      EnchantmentPredicate[] var5,
      @Nullable PotionType var6,
      NBTPredicate var7
   ) {
      this.item = ☃;
      this.data = ☃;
      this.count = ☃;
      this.durability = ☃;
      this.enchantments = ☃;
      this.potion = ☃;
      this.nbt = ☃;
   }

   public boolean test(ItemStack var1) {
      if (this.item != null && ☃.getItem() != this.item) {
         return false;
      } else if (this.data != null && ☃.getMetadata() != this.data) {
         return false;
      } else if (!this.count.test(☃.getCount())) {
         return false;
      } else if (this.durability != MinMaxBounds.UNBOUNDED && !☃.isItemStackDamageable()) {
         return false;
      } else if (!this.durability.test(☃.getMaxDamage() - ☃.getItemDamage())) {
         return false;
      } else if (!this.nbt.test(☃)) {
         return false;
      } else {
         Map<Enchantment, Integer> ☃ = EnchantmentHelper.getEnchantments(☃);

         for (int ☃x = 0; ☃x < this.enchantments.length; ☃x++) {
            if (!this.enchantments[☃x].test(☃)) {
               return false;
            }
         }

         PotionType ☃xx = PotionUtils.getPotionFromItem(☃);
         return this.potion == null || this.potion == ☃xx;
      }
   }

   public static ItemPredicate deserialize(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "item");
         MinMaxBounds ☃x = MinMaxBounds.deserialize(☃.get("count"));
         MinMaxBounds ☃xx = MinMaxBounds.deserialize(☃.get("durability"));
         Integer ☃xxx = ☃.has("data") ? JsonUtils.getInt(☃, "data") : null;
         NBTPredicate ☃xxxx = NBTPredicate.deserialize(☃.get("nbt"));
         Item ☃xxxxx = null;
         if (☃.has("item")) {
            ResourceLocation ☃xxxxxx = new ResourceLocation(JsonUtils.getString(☃, "item"));
            ☃xxxxx = Item.REGISTRY.getObject(☃xxxxxx);
            if (☃xxxxx == null) {
               throw new JsonSyntaxException("Unknown item id '" + ☃xxxxxx + "'");
            }
         }

         EnchantmentPredicate[] ☃xxxxxx = EnchantmentPredicate.deserializeArray(☃.get("enchantments"));
         PotionType ☃xxxxxxx = null;
         if (☃.has("potion")) {
            ResourceLocation ☃xxxxxxxx = new ResourceLocation(JsonUtils.getString(☃, "potion"));
            if (!PotionType.REGISTRY.containsKey(☃xxxxxxxx)) {
               throw new JsonSyntaxException("Unknown potion '" + ☃xxxxxxxx + "'");
            }

            ☃xxxxxxx = PotionType.REGISTRY.getObject(☃xxxxxxxx);
         }

         return new ItemPredicate(☃xxxxx, ☃xxx, ☃x, ☃xx, ☃xxxxxx, ☃xxxxxxx, ☃xxxx);
      } else {
         return ANY;
      }
   }

   public static ItemPredicate[] deserializeArray(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonArray ☃ = JsonUtils.getJsonArray(☃, "items");
         ItemPredicate[] ☃x = new ItemPredicate[☃.size()];

         for (int ☃xx = 0; ☃xx < ☃x.length; ☃xx++) {
            ☃x[☃xx] = deserialize(☃.get(☃xx));
         }

         return ☃x;
      } else {
         return new ItemPredicate[0];
      }
   }
}
