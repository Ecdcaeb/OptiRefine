package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeBookCloning implements IRecipe {
   @Override
   public boolean matches(InventoryCrafting var1, World var2) {
      int ☃ = 0;
      ItemStack ☃x = ItemStack.EMPTY;

      for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
         ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
         if (!☃xxx.isEmpty()) {
            if (☃xxx.getItem() == Items.WRITTEN_BOOK) {
               if (!☃x.isEmpty()) {
                  return false;
               }

               ☃x = ☃xxx;
            } else {
               if (☃xxx.getItem() != Items.WRITABLE_BOOK) {
                  return false;
               }

               ☃++;
            }
         }
      }

      return !☃x.isEmpty() && ☃x.hasTagCompound() && ☃ > 0;
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting var1) {
      int ☃ = 0;
      ItemStack ☃x = ItemStack.EMPTY;

      for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
         ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
         if (!☃xxx.isEmpty()) {
            if (☃xxx.getItem() == Items.WRITTEN_BOOK) {
               if (!☃x.isEmpty()) {
                  return ItemStack.EMPTY;
               }

               ☃x = ☃xxx;
            } else {
               if (☃xxx.getItem() != Items.WRITABLE_BOOK) {
                  return ItemStack.EMPTY;
               }

               ☃++;
            }
         }
      }

      if (!☃x.isEmpty() && ☃x.hasTagCompound() && ☃ >= 1 && ItemWrittenBook.getGeneration(☃x) < 2) {
         ItemStack ☃xxx = new ItemStack(Items.WRITTEN_BOOK, ☃);
         ☃xxx.setTagCompound(☃x.getTagCompound().copy());
         ☃xxx.getTagCompound().setInteger("generation", ItemWrittenBook.getGeneration(☃x) + 1);
         if (☃x.hasDisplayName()) {
            ☃xxx.setStackDisplayName(☃x.getDisplayName());
         }

         return ☃xxx;
      } else {
         return ItemStack.EMPTY;
      }
   }

   @Override
   public ItemStack getRecipeOutput() {
      return ItemStack.EMPTY;
   }

   @Override
   public NonNullList<ItemStack> getRemainingItems(InventoryCrafting var1) {
      NonNullList<ItemStack> ☃ = NonNullList.withSize(☃.getSizeInventory(), ItemStack.EMPTY);

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         ItemStack ☃xx = ☃.getStackInSlot(☃x);
         if (☃xx.getItem() instanceof ItemWrittenBook) {
            ItemStack ☃xxx = ☃xx.copy();
            ☃xxx.setCount(1);
            ☃.set(☃x, ☃xxx);
            break;
         }
      }

      return ☃;
   }

   @Override
   public boolean isDynamic() {
      return true;
   }

   @Override
   public boolean canFit(int var1, int var2) {
      return ☃ >= 3 && ☃ >= 3;
   }
}
