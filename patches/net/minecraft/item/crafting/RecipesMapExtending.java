package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class RecipesMapExtending extends ShapedRecipes {
   public RecipesMapExtending() {
      super(
         "",
         3,
         3,
         NonNullList.from(
            Ingredient.EMPTY,
            Ingredient.fromItems(Items.PAPER),
            Ingredient.fromItems(Items.PAPER),
            Ingredient.fromItems(Items.PAPER),
            Ingredient.fromItems(Items.PAPER),
            Ingredient.fromItem(Items.FILLED_MAP),
            Ingredient.fromItems(Items.PAPER),
            Ingredient.fromItems(Items.PAPER),
            Ingredient.fromItems(Items.PAPER),
            Ingredient.fromItems(Items.PAPER)
         ),
         new ItemStack(Items.MAP)
      );
   }

   @Override
   public boolean matches(InventoryCrafting var1, World var2) {
      if (!super.matches(☃, ☃)) {
         return false;
      } else {
         ItemStack ☃ = ItemStack.EMPTY;

         for (int ☃x = 0; ☃x < ☃.getSizeInventory() && ☃.isEmpty(); ☃x++) {
            ItemStack ☃xx = ☃.getStackInSlot(☃x);
            if (☃xx.getItem() == Items.FILLED_MAP) {
               ☃ = ☃xx;
            }
         }

         if (☃.isEmpty()) {
            return false;
         } else {
            MapData ☃xx = Items.FILLED_MAP.getMapData(☃, ☃);
            if (☃xx == null) {
               return false;
            } else {
               return this.isExplorationMap(☃xx) ? false : ☃xx.scale < 4;
            }
         }
      }
   }

   private boolean isExplorationMap(MapData var1) {
      if (☃.mapDecorations != null) {
         for (MapDecoration ☃ : ☃.mapDecorations.values()) {
            if (☃.getType() == MapDecoration.Type.MANSION || ☃.getType() == MapDecoration.Type.MONUMENT) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting var1) {
      ItemStack ☃ = ItemStack.EMPTY;

      for (int ☃x = 0; ☃x < ☃.getSizeInventory() && ☃.isEmpty(); ☃x++) {
         ItemStack ☃xx = ☃.getStackInSlot(☃x);
         if (☃xx.getItem() == Items.FILLED_MAP) {
            ☃ = ☃xx;
         }
      }

      ☃ = ☃.copy();
      ☃.setCount(1);
      if (☃.getTagCompound() == null) {
         ☃.setTagCompound(new NBTTagCompound());
      }

      ☃.getTagCompound().setInteger("map_scale_direction", 1);
      return ☃;
   }

   @Override
   public boolean isDynamic() {
      return true;
   }
}
