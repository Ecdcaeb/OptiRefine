package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShapelessRecipes implements IRecipe {
   private final ItemStack recipeOutput;
   private final NonNullList<Ingredient> recipeItems;
   private final String group;

   public ShapelessRecipes(String var1, ItemStack var2, NonNullList<Ingredient> var3) {
      this.group = ☃;
      this.recipeOutput = ☃;
      this.recipeItems = ☃;
   }

   @Override
   public String getGroup() {
      return this.group;
   }

   @Override
   public ItemStack getRecipeOutput() {
      return this.recipeOutput;
   }

   @Override
   public NonNullList<Ingredient> getIngredients() {
      return this.recipeItems;
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
   public boolean matches(InventoryCrafting var1, World var2) {
      List<Ingredient> ☃ = Lists.newArrayList(this.recipeItems);

      for (int ☃x = 0; ☃x < ☃.getHeight(); ☃x++) {
         for (int ☃xx = 0; ☃xx < ☃.getWidth(); ☃xx++) {
            ItemStack ☃xxx = ☃.getStackInRowAndColumn(☃xx, ☃x);
            if (!☃xxx.isEmpty()) {
               boolean ☃xxxx = false;

               for (Ingredient ☃xxxxx : ☃) {
                  if (☃xxxxx.apply(☃xxx)) {
                     ☃xxxx = true;
                     ☃.remove(☃xxxxx);
                     break;
                  }
               }

               if (!☃xxxx) {
                  return false;
               }
            }
         }
      }

      return ☃.isEmpty();
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting var1) {
      return this.recipeOutput.copy();
   }

   public static ShapelessRecipes deserialize(JsonObject var0) {
      String ☃ = JsonUtils.getString(☃, "group", "");
      NonNullList<Ingredient> ☃x = deserializeIngredients(JsonUtils.getJsonArray(☃, "ingredients"));
      if (☃x.isEmpty()) {
         throw new JsonParseException("No ingredients for shapeless recipe");
      } else if (☃x.size() > 9) {
         throw new JsonParseException("Too many ingredients for shapeless recipe");
      } else {
         ItemStack ☃xx = ShapedRecipes.deserializeItem(JsonUtils.getJsonObject(☃, "result"), true);
         return new ShapelessRecipes(☃, ☃xx, ☃x);
      }
   }

   private static NonNullList<Ingredient> deserializeIngredients(JsonArray var0) {
      NonNullList<Ingredient> ☃ = NonNullList.create();

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         Ingredient ☃xx = ShapedRecipes.deserializeIngredient(☃.get(☃x));
         if (☃xx != Ingredient.EMPTY) {
            ☃.add(☃xx);
         }
      }

      return ☃;
   }

   @Override
   public boolean canFit(int var1, int var2) {
      return ☃ * ☃ >= this.recipeItems.size();
   }
}
