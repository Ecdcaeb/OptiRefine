package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeRepairItem implements IRecipe {
   @Override
   public boolean matches(InventoryCrafting var1, World var2) {
      List<ItemStack> ☃ = Lists.newArrayList();

      for (int ☃x = 0; ☃x < ☃.getSizeInventory(); ☃x++) {
         ItemStack ☃xx = ☃.getStackInSlot(☃x);
         if (!☃xx.isEmpty()) {
            ☃.add(☃xx);
            if (☃.size() > 1) {
               ItemStack ☃xxx = ☃.get(0);
               if (☃xx.getItem() != ☃xxx.getItem() || ☃xxx.getCount() != 1 || ☃xx.getCount() != 1 || !☃xxx.getItem().isDamageable()) {
                  return false;
               }
            }
         }
      }

      return ☃.size() == 2;
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting var1) {
      List<ItemStack> ☃ = Lists.newArrayList();

      for (int ☃x = 0; ☃x < ☃.getSizeInventory(); ☃x++) {
         ItemStack ☃xx = ☃.getStackInSlot(☃x);
         if (!☃xx.isEmpty()) {
            ☃.add(☃xx);
            if (☃.size() > 1) {
               ItemStack ☃xxx = ☃.get(0);
               if (☃xx.getItem() != ☃xxx.getItem() || ☃xxx.getCount() != 1 || ☃xx.getCount() != 1 || !☃xxx.getItem().isDamageable()) {
                  return ItemStack.EMPTY;
               }
            }
         }
      }

      if (☃.size() == 2) {
         ItemStack ☃xx = ☃.get(0);
         ItemStack ☃xxx = ☃.get(1);
         if (☃xx.getItem() == ☃xxx.getItem() && ☃xx.getCount() == 1 && ☃xxx.getCount() == 1 && ☃xx.getItem().isDamageable()) {
            Item ☃xxxx = ☃xx.getItem();
            int ☃xxxxx = ☃xxxx.getMaxDamage() - ☃xx.getItemDamage();
            int ☃xxxxxx = ☃xxxx.getMaxDamage() - ☃xxx.getItemDamage();
            int ☃xxxxxxx = ☃xxxxx + ☃xxxxxx + ☃xxxx.getMaxDamage() * 5 / 100;
            int ☃xxxxxxxx = ☃xxxx.getMaxDamage() - ☃xxxxxxx;
            if (☃xxxxxxxx < 0) {
               ☃xxxxxxxx = 0;
            }

            return new ItemStack(☃xx.getItem(), 1, ☃xxxxxxxx);
         }
      }

      return ItemStack.EMPTY;
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
