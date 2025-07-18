package net.minecraft.item.crafting;

import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import javax.annotation.Nullable;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ingredient implements Predicate<ItemStack> {
   public static final Ingredient EMPTY = new Ingredient() {
      @Override
      public boolean apply(@Nullable ItemStack var1) {
         return ☃.isEmpty();
      }
   };
   private final ItemStack[] matchingStacks;
   private IntList matchingStacksPacked;

   private Ingredient(ItemStack... var1) {
      this.matchingStacks = ☃;
   }

   public ItemStack[] getMatchingStacks() {
      return this.matchingStacks;
   }

   public boolean apply(@Nullable ItemStack var1) {
      if (☃ == null) {
         return false;
      } else {
         for (ItemStack ☃ : this.matchingStacks) {
            if (☃.getItem() == ☃.getItem()) {
               int ☃x = ☃.getMetadata();
               if (☃x == 32767 || ☃x == ☃.getMetadata()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public IntList getValidItemStacksPacked() {
      if (this.matchingStacksPacked == null) {
         this.matchingStacksPacked = new IntArrayList(this.matchingStacks.length);

         for (ItemStack ☃ : this.matchingStacks) {
            this.matchingStacksPacked.add(RecipeItemHelper.pack(☃));
         }

         this.matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
      }

      return this.matchingStacksPacked;
   }

   public static Ingredient fromItem(Item var0) {
      return fromStacks(new ItemStack(☃, 1, 32767));
   }

   public static Ingredient fromItems(Item... var0) {
      ItemStack[] ☃ = new ItemStack[☃.length];

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         ☃[☃x] = new ItemStack(☃[☃x]);
      }

      return fromStacks(☃);
   }

   public static Ingredient fromStacks(ItemStack... var0) {
      if (☃.length > 0) {
         for (ItemStack ☃ : ☃) {
            if (!☃.isEmpty()) {
               return new Ingredient(☃);
            }
         }
      }

      return EMPTY;
   }
}
