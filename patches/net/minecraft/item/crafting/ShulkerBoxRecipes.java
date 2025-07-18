package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShulkerBoxRecipes {
   public static class ShulkerBoxColoring implements IRecipe {
      @Override
      public boolean matches(InventoryCrafting var1, World var2) {
         int ☃ = 0;
         int ☃x = 0;

         for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
            ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
            if (!☃xxx.isEmpty()) {
               if (Block.getBlockFromItem(☃xxx.getItem()) instanceof BlockShulkerBox) {
                  ☃++;
               } else {
                  if (☃xxx.getItem() != Items.DYE) {
                     return false;
                  }

                  ☃x++;
               }

               if (☃x > 1 || ☃ > 1) {
                  return false;
               }
            }
         }

         return ☃ == 1 && ☃x == 1;
      }

      @Override
      public ItemStack getCraftingResult(InventoryCrafting var1) {
         ItemStack ☃ = ItemStack.EMPTY;
         ItemStack ☃x = ItemStack.EMPTY;

         for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
            ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
            if (!☃xxx.isEmpty()) {
               if (Block.getBlockFromItem(☃xxx.getItem()) instanceof BlockShulkerBox) {
                  ☃ = ☃xxx;
               } else if (☃xxx.getItem() == Items.DYE) {
                  ☃x = ☃xxx;
               }
            }
         }

         ItemStack ☃xxx = BlockShulkerBox.getColoredItemStack(EnumDyeColor.byDyeDamage(☃x.getMetadata()));
         if (☃.hasTagCompound()) {
            ☃xxx.setTagCompound(☃.getTagCompound().copy());
         }

         return ☃xxx;
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
         return ☃ * ☃ >= 2;
      }
   }
}
