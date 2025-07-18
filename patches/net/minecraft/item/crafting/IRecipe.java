package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public interface IRecipe {
   boolean matches(InventoryCrafting var1, World var2);

   ItemStack getCraftingResult(InventoryCrafting var1);

   boolean canFit(int var1, int var2);

   ItemStack getRecipeOutput();

   NonNullList<ItemStack> getRemainingItems(InventoryCrafting var1);

   default NonNullList<Ingredient> getIngredients() {
      return NonNullList.create();
   }

   default boolean isDynamic() {
      return false;
   }

   default String getGroup() {
      return "";
   }
}
