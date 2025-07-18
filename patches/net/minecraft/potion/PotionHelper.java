package net.minecraft.potion;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class PotionHelper {
   private static final List<PotionHelper.MixPredicate<PotionType>> POTION_TYPE_CONVERSIONS = Lists.newArrayList();
   private static final List<PotionHelper.MixPredicate<Item>> POTION_ITEM_CONVERSIONS = Lists.newArrayList();
   private static final List<Ingredient> POTION_ITEMS = Lists.newArrayList();
   private static final Predicate<ItemStack> IS_POTION_ITEM = new Predicate<ItemStack>() {
      public boolean apply(ItemStack var1) {
         for (Ingredient ☃ : PotionHelper.POTION_ITEMS) {
            if (☃.apply(☃)) {
               return true;
            }
         }

         return false;
      }
   };

   public static boolean isReagent(ItemStack var0) {
      return isItemConversionReagent(☃) || isTypeConversionReagent(☃);
   }

   protected static boolean isItemConversionReagent(ItemStack var0) {
      int ☃ = 0;

      for (int ☃x = POTION_ITEM_CONVERSIONS.size(); ☃ < ☃x; ☃++) {
         if (POTION_ITEM_CONVERSIONS.get(☃).reagent.apply(☃)) {
            return true;
         }
      }

      return false;
   }

   protected static boolean isTypeConversionReagent(ItemStack var0) {
      int ☃ = 0;

      for (int ☃x = POTION_TYPE_CONVERSIONS.size(); ☃ < ☃x; ☃++) {
         if (POTION_TYPE_CONVERSIONS.get(☃).reagent.apply(☃)) {
            return true;
         }
      }

      return false;
   }

   public static boolean hasConversions(ItemStack var0, ItemStack var1) {
      return !IS_POTION_ITEM.apply(☃) ? false : hasItemConversions(☃, ☃) || hasTypeConversions(☃, ☃);
   }

   protected static boolean hasItemConversions(ItemStack var0, ItemStack var1) {
      Item ☃ = ☃.getItem();
      int ☃x = 0;

      for (int ☃xx = POTION_ITEM_CONVERSIONS.size(); ☃x < ☃xx; ☃x++) {
         PotionHelper.MixPredicate<Item> ☃xxx = POTION_ITEM_CONVERSIONS.get(☃x);
         if (☃xxx.input == ☃ && ☃xxx.reagent.apply(☃)) {
            return true;
         }
      }

      return false;
   }

   protected static boolean hasTypeConversions(ItemStack var0, ItemStack var1) {
      PotionType ☃ = PotionUtils.getPotionFromItem(☃);
      int ☃x = 0;

      for (int ☃xx = POTION_TYPE_CONVERSIONS.size(); ☃x < ☃xx; ☃x++) {
         PotionHelper.MixPredicate<PotionType> ☃xxx = POTION_TYPE_CONVERSIONS.get(☃x);
         if (☃xxx.input == ☃ && ☃xxx.reagent.apply(☃)) {
            return true;
         }
      }

      return false;
   }

   public static ItemStack doReaction(ItemStack var0, ItemStack var1) {
      if (!☃.isEmpty()) {
         PotionType ☃ = PotionUtils.getPotionFromItem(☃);
         Item ☃x = ☃.getItem();
         int ☃xx = 0;

         for (int ☃xxx = POTION_ITEM_CONVERSIONS.size(); ☃xx < ☃xxx; ☃xx++) {
            PotionHelper.MixPredicate<Item> ☃xxxx = POTION_ITEM_CONVERSIONS.get(☃xx);
            if (☃xxxx.input == ☃x && ☃xxxx.reagent.apply(☃)) {
               return PotionUtils.addPotionToItemStack(new ItemStack(☃xxxx.output), ☃);
            }
         }

         ☃xx = 0;

         for (int ☃xxxx = POTION_TYPE_CONVERSIONS.size(); ☃xx < ☃xxxx; ☃xx++) {
            PotionHelper.MixPredicate<PotionType> ☃xxxxx = POTION_TYPE_CONVERSIONS.get(☃xx);
            if (☃xxxxx.input == ☃ && ☃xxxxx.reagent.apply(☃)) {
               return PotionUtils.addPotionToItemStack(new ItemStack(☃x), ☃xxxxx.output);
            }
         }
      }

      return ☃;
   }

   public static void init() {
      addContainer(Items.POTIONITEM);
      addContainer(Items.SPLASH_POTION);
      addContainer(Items.LINGERING_POTION);
      addContainerRecipe(Items.POTIONITEM, Items.GUNPOWDER, Items.SPLASH_POTION);
      addContainerRecipe(Items.SPLASH_POTION, Items.DRAGON_BREATH, Items.LINGERING_POTION);
      addMix(PotionTypes.WATER, Items.SPECKLED_MELON, PotionTypes.MUNDANE);
      addMix(PotionTypes.WATER, Items.GHAST_TEAR, PotionTypes.MUNDANE);
      addMix(PotionTypes.WATER, Items.RABBIT_FOOT, PotionTypes.MUNDANE);
      addMix(PotionTypes.WATER, Items.BLAZE_POWDER, PotionTypes.MUNDANE);
      addMix(PotionTypes.WATER, Items.SPIDER_EYE, PotionTypes.MUNDANE);
      addMix(PotionTypes.WATER, Items.SUGAR, PotionTypes.MUNDANE);
      addMix(PotionTypes.WATER, Items.MAGMA_CREAM, PotionTypes.MUNDANE);
      addMix(PotionTypes.WATER, Items.GLOWSTONE_DUST, PotionTypes.THICK);
      addMix(PotionTypes.WATER, Items.REDSTONE, PotionTypes.MUNDANE);
      addMix(PotionTypes.WATER, Items.NETHER_WART, PotionTypes.AWKWARD);
      addMix(PotionTypes.AWKWARD, Items.GOLDEN_CARROT, PotionTypes.NIGHT_VISION);
      addMix(PotionTypes.NIGHT_VISION, Items.REDSTONE, PotionTypes.LONG_NIGHT_VISION);
      addMix(PotionTypes.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, PotionTypes.INVISIBILITY);
      addMix(PotionTypes.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, PotionTypes.LONG_INVISIBILITY);
      addMix(PotionTypes.INVISIBILITY, Items.REDSTONE, PotionTypes.LONG_INVISIBILITY);
      addMix(PotionTypes.AWKWARD, Items.MAGMA_CREAM, PotionTypes.FIRE_RESISTANCE);
      addMix(PotionTypes.FIRE_RESISTANCE, Items.REDSTONE, PotionTypes.LONG_FIRE_RESISTANCE);
      addMix(PotionTypes.AWKWARD, Items.RABBIT_FOOT, PotionTypes.LEAPING);
      addMix(PotionTypes.LEAPING, Items.REDSTONE, PotionTypes.LONG_LEAPING);
      addMix(PotionTypes.LEAPING, Items.GLOWSTONE_DUST, PotionTypes.STRONG_LEAPING);
      addMix(PotionTypes.LEAPING, Items.FERMENTED_SPIDER_EYE, PotionTypes.SLOWNESS);
      addMix(PotionTypes.LONG_LEAPING, Items.FERMENTED_SPIDER_EYE, PotionTypes.LONG_SLOWNESS);
      addMix(PotionTypes.SLOWNESS, Items.REDSTONE, PotionTypes.LONG_SLOWNESS);
      addMix(PotionTypes.SWIFTNESS, Items.FERMENTED_SPIDER_EYE, PotionTypes.SLOWNESS);
      addMix(PotionTypes.LONG_SWIFTNESS, Items.FERMENTED_SPIDER_EYE, PotionTypes.LONG_SLOWNESS);
      addMix(PotionTypes.AWKWARD, Items.SUGAR, PotionTypes.SWIFTNESS);
      addMix(PotionTypes.SWIFTNESS, Items.REDSTONE, PotionTypes.LONG_SWIFTNESS);
      addMix(PotionTypes.SWIFTNESS, Items.GLOWSTONE_DUST, PotionTypes.STRONG_SWIFTNESS);
      addMix(
         PotionTypes.AWKWARD, Ingredient.fromStacks(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata())), PotionTypes.WATER_BREATHING
      );
      addMix(PotionTypes.WATER_BREATHING, Items.REDSTONE, PotionTypes.LONG_WATER_BREATHING);
      addMix(PotionTypes.AWKWARD, Items.SPECKLED_MELON, PotionTypes.HEALING);
      addMix(PotionTypes.HEALING, Items.GLOWSTONE_DUST, PotionTypes.STRONG_HEALING);
      addMix(PotionTypes.HEALING, Items.FERMENTED_SPIDER_EYE, PotionTypes.HARMING);
      addMix(PotionTypes.STRONG_HEALING, Items.FERMENTED_SPIDER_EYE, PotionTypes.STRONG_HARMING);
      addMix(PotionTypes.HARMING, Items.GLOWSTONE_DUST, PotionTypes.STRONG_HARMING);
      addMix(PotionTypes.POISON, Items.FERMENTED_SPIDER_EYE, PotionTypes.HARMING);
      addMix(PotionTypes.LONG_POISON, Items.FERMENTED_SPIDER_EYE, PotionTypes.HARMING);
      addMix(PotionTypes.STRONG_POISON, Items.FERMENTED_SPIDER_EYE, PotionTypes.STRONG_HARMING);
      addMix(PotionTypes.AWKWARD, Items.SPIDER_EYE, PotionTypes.POISON);
      addMix(PotionTypes.POISON, Items.REDSTONE, PotionTypes.LONG_POISON);
      addMix(PotionTypes.POISON, Items.GLOWSTONE_DUST, PotionTypes.STRONG_POISON);
      addMix(PotionTypes.AWKWARD, Items.GHAST_TEAR, PotionTypes.REGENERATION);
      addMix(PotionTypes.REGENERATION, Items.REDSTONE, PotionTypes.LONG_REGENERATION);
      addMix(PotionTypes.REGENERATION, Items.GLOWSTONE_DUST, PotionTypes.STRONG_REGENERATION);
      addMix(PotionTypes.AWKWARD, Items.BLAZE_POWDER, PotionTypes.STRENGTH);
      addMix(PotionTypes.STRENGTH, Items.REDSTONE, PotionTypes.LONG_STRENGTH);
      addMix(PotionTypes.STRENGTH, Items.GLOWSTONE_DUST, PotionTypes.STRONG_STRENGTH);
      addMix(PotionTypes.WATER, Items.FERMENTED_SPIDER_EYE, PotionTypes.WEAKNESS);
      addMix(PotionTypes.WEAKNESS, Items.REDSTONE, PotionTypes.LONG_WEAKNESS);
   }

   private static void addContainerRecipe(ItemPotion var0, Item var1, ItemPotion var2) {
      POTION_ITEM_CONVERSIONS.add(new PotionHelper.MixPredicate<>(☃, Ingredient.fromItems(☃), ☃));
   }

   private static void addContainer(ItemPotion var0) {
      POTION_ITEMS.add(Ingredient.fromItems(☃));
   }

   private static void addMix(PotionType var0, Item var1, PotionType var2) {
      addMix(☃, Ingredient.fromItems(☃), ☃);
   }

   private static void addMix(PotionType var0, Ingredient var1, PotionType var2) {
      POTION_TYPE_CONVERSIONS.add(new PotionHelper.MixPredicate<>(☃, ☃, ☃));
   }

   static class MixPredicate<T> {
      final T input;
      final Ingredient reagent;
      final T output;

      public MixPredicate(T var1, Ingredient var2, T var3) {
         this.input = ☃;
         this.reagent = ☃;
         this.output = ☃;
      }
   }
}
