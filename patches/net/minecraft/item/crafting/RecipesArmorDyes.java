package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipesArmorDyes implements IRecipe {
   @Override
   public boolean matches(InventoryCrafting var1, World var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      List<ItemStack> ☃x = Lists.newArrayList();

      for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
         ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
         if (!☃xxx.isEmpty()) {
            if (☃xxx.getItem() instanceof ItemArmor) {
               ItemArmor ☃xxxx = (ItemArmor)☃xxx.getItem();
               if (☃xxxx.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !☃.isEmpty()) {
                  return false;
               }

               ☃ = ☃xxx;
            } else {
               if (☃xxx.getItem() != Items.DYE) {
                  return false;
               }

               ☃x.add(☃xxx);
            }
         }
      }

      return !☃.isEmpty() && !☃x.isEmpty();
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting var1) {
      ItemStack ☃ = ItemStack.EMPTY;
      int[] ☃x = new int[3];
      int ☃xx = 0;
      int ☃xxx = 0;
      ItemArmor ☃xxxx = null;

      for (int ☃xxxxx = 0; ☃xxxxx < ☃.getSizeInventory(); ☃xxxxx++) {
         ItemStack ☃xxxxxx = ☃.getStackInSlot(☃xxxxx);
         if (!☃xxxxxx.isEmpty()) {
            if (☃xxxxxx.getItem() instanceof ItemArmor) {
               ☃xxxx = (ItemArmor)☃xxxxxx.getItem();
               if (☃xxxx.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !☃.isEmpty()) {
                  return ItemStack.EMPTY;
               }

               ☃ = ☃xxxxxx.copy();
               ☃.setCount(1);
               if (☃xxxx.hasColor(☃xxxxxx)) {
                  int ☃xxxxxxx = ☃xxxx.getColor(☃);
                  float ☃xxxxxxxx = (☃xxxxxxx >> 16 & 0xFF) / 255.0F;
                  float ☃xxxxxxxxx = (☃xxxxxxx >> 8 & 0xFF) / 255.0F;
                  float ☃xxxxxxxxxx = (☃xxxxxxx & 0xFF) / 255.0F;
                  ☃xx = (int)(☃xx + Math.max(☃xxxxxxxx, Math.max(☃xxxxxxxxx, ☃xxxxxxxxxx)) * 255.0F);
                  ☃x[0] = (int)(☃x[0] + ☃xxxxxxxx * 255.0F);
                  ☃x[1] = (int)(☃x[1] + ☃xxxxxxxxx * 255.0F);
                  ☃x[2] = (int)(☃x[2] + ☃xxxxxxxxxx * 255.0F);
                  ☃xxx++;
               }
            } else {
               if (☃xxxxxx.getItem() != Items.DYE) {
                  return ItemStack.EMPTY;
               }

               float[] ☃xxxxxxx = EnumDyeColor.byDyeDamage(☃xxxxxx.getMetadata()).getColorComponentValues();
               int ☃xxxxxxxx = (int)(☃xxxxxxx[0] * 255.0F);
               int ☃xxxxxxxxx = (int)(☃xxxxxxx[1] * 255.0F);
               int ☃xxxxxxxxxx = (int)(☃xxxxxxx[2] * 255.0F);
               ☃xx += Math.max(☃xxxxxxxx, Math.max(☃xxxxxxxxx, ☃xxxxxxxxxx));
               ☃x[0] += ☃xxxxxxxx;
               ☃x[1] += ☃xxxxxxxxx;
               ☃x[2] += ☃xxxxxxxxxx;
               ☃xxx++;
            }
         }
      }

      if (☃xxxx == null) {
         return ItemStack.EMPTY;
      } else {
         int ☃xxxxxx = ☃x[0] / ☃xxx;
         int ☃xxxxxxx = ☃x[1] / ☃xxx;
         int ☃xxxxxxxx = ☃x[2] / ☃xxx;
         float ☃xxxxxxxxx = (float)☃xx / ☃xxx;
         float ☃xxxxxxxxxx = Math.max(☃xxxxxx, Math.max(☃xxxxxxx, ☃xxxxxxxx));
         ☃xxxxxx = (int)(☃xxxxxx * ☃xxxxxxxxx / ☃xxxxxxxxxx);
         ☃xxxxxxx = (int)(☃xxxxxxx * ☃xxxxxxxxx / ☃xxxxxxxxxx);
         ☃xxxxxxxx = (int)(☃xxxxxxxx * ☃xxxxxxxxx / ☃xxxxxxxxxx);
         int var25 = (☃xxxxxx << 8) + ☃xxxxxxx;
         var25 = (var25 << 8) + ☃xxxxxxxx;
         ☃xxxx.setColor(☃, var25);
         return ☃;
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
