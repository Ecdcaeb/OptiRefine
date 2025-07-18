package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipesMapCloning implements IRecipe {
   @Override
   public boolean matches(InventoryCrafting var1, World var2) {
      int ☃ = 0;
      ItemStack ☃x = ItemStack.EMPTY;

      for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
         ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
         if (!☃xxx.isEmpty()) {
            if (☃xxx.getItem() == Items.FILLED_MAP) {
               if (!☃x.isEmpty()) {
                  return false;
               }

               ☃x = ☃xxx;
            } else {
               if (☃xxx.getItem() != Items.MAP) {
                  return false;
               }

               ☃++;
            }
         }
      }

      return !☃x.isEmpty() && ☃ > 0;
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting var1) {
      int ☃ = 0;
      ItemStack ☃x = ItemStack.EMPTY;

      for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
         ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
         if (!☃xxx.isEmpty()) {
            if (☃xxx.getItem() == Items.FILLED_MAP) {
               if (!☃x.isEmpty()) {
                  return ItemStack.EMPTY;
               }

               ☃x = ☃xxx;
            } else {
               if (☃xxx.getItem() != Items.MAP) {
                  return ItemStack.EMPTY;
               }

               ☃++;
            }
         }
      }

      if (!☃x.isEmpty() && ☃ >= 1) {
         ItemStack ☃xxx = new ItemStack(Items.FILLED_MAP, ☃ + 1, ☃x.getMetadata());
         if (☃x.hasDisplayName()) {
            ☃xxx.setStackDisplayName(☃x.getDisplayName());
         }

         if (☃x.hasTagCompound()) {
            ☃xxx.setTagCompound(☃x.getTagCompound());
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
         if (☃xx.getItem().hasContainerItem()) {
            ☃.set(☃x, new ItemStack(☃xx.getItem().getContainerItem()));
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
