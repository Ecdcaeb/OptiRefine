package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeTippedArrow implements IRecipe {
   @Override
   public boolean matches(InventoryCrafting var1, World var2) {
      if (☃.getWidth() == 3 && ☃.getHeight() == 3) {
         for (int ☃ = 0; ☃ < ☃.getWidth(); ☃++) {
            for (int ☃x = 0; ☃x < ☃.getHeight(); ☃x++) {
               ItemStack ☃xx = ☃.getStackInRowAndColumn(☃, ☃x);
               if (☃xx.isEmpty()) {
                  return false;
               }

               Item ☃xxx = ☃xx.getItem();
               if (☃ == 1 && ☃x == 1) {
                  if (☃xxx != Items.LINGERING_POTION) {
                     return false;
                  }
               } else if (☃xxx != Items.ARROW) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting var1) {
      ItemStack ☃ = ☃.getStackInRowAndColumn(1, 1);
      if (☃.getItem() != Items.LINGERING_POTION) {
         return ItemStack.EMPTY;
      } else {
         ItemStack ☃x = new ItemStack(Items.TIPPED_ARROW, 8);
         PotionUtils.addPotionToItemStack(☃x, PotionUtils.getPotionFromItem(☃));
         PotionUtils.appendEffects(☃x, PotionUtils.getFullEffectsFromItem(☃));
         return ☃x;
      }
   }

   @Override
   public ItemStack getRecipeOutput() {
      return ItemStack.EMPTY;
   }

   @Override
   public NonNullList<ItemStack> getRemainingItems(InventoryCrafting var1) {
      return NonNullList.withSize(☃.getSizeInventory(), ItemStack.EMPTY);
   }

   @Override
   public boolean isDynamic() {
      return true;
   }

   @Override
   public boolean canFit(int var1, int var2) {
      return ☃ >= 2 && ☃ >= 2;
   }
}
