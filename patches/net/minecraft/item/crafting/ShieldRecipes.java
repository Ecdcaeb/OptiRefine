package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShieldRecipes {
   public static class Decoration implements IRecipe {
      @Override
      public boolean matches(InventoryCrafting var1, World var2) {
         ItemStack ☃ = ItemStack.EMPTY;
         ItemStack ☃x = ItemStack.EMPTY;

         for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
            ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
            if (!☃xxx.isEmpty()) {
               if (☃xxx.getItem() == Items.BANNER) {
                  if (!☃x.isEmpty()) {
                     return false;
                  }

                  ☃x = ☃xxx;
               } else {
                  if (☃xxx.getItem() != Items.SHIELD) {
                     return false;
                  }

                  if (!☃.isEmpty()) {
                     return false;
                  }

                  if (☃xxx.getSubCompound("BlockEntityTag") != null) {
                     return false;
                  }

                  ☃ = ☃xxx;
               }
            }
         }

         return !☃.isEmpty() && !☃x.isEmpty();
      }

      @Override
      public ItemStack getCraftingResult(InventoryCrafting var1) {
         ItemStack ☃ = ItemStack.EMPTY;
         ItemStack ☃x = ItemStack.EMPTY;

         for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
            ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
            if (!☃xxx.isEmpty()) {
               if (☃xxx.getItem() == Items.BANNER) {
                  ☃ = ☃xxx;
               } else if (☃xxx.getItem() == Items.SHIELD) {
                  ☃x = ☃xxx.copy();
               }
            }
         }

         if (☃x.isEmpty()) {
            return ☃x;
         } else {
            NBTTagCompound ☃xxx = ☃.getSubCompound("BlockEntityTag");
            NBTTagCompound ☃xxxx = ☃xxx == null ? new NBTTagCompound() : ☃xxx.copy();
            ☃xxxx.setInteger("Base", ☃.getMetadata() & 15);
            ☃x.setTagInfo("BlockEntityTag", ☃xxxx);
            return ☃x;
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
         return ☃ * ☃ >= 2;
      }
   }
}
