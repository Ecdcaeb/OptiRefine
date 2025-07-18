package net.minecraft.stats;

import java.util.BitSet;
import javax.annotation.Nullable;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RecipeBook {
   protected final BitSet recipes = new BitSet();
   protected final BitSet newRecipes = new BitSet();
   protected boolean isGuiOpen;
   protected boolean isFilteringCraftable;

   public void copyFrom(RecipeBook var1) {
      this.recipes.clear();
      this.newRecipes.clear();
      this.recipes.or(☃.recipes);
      this.newRecipes.or(☃.newRecipes);
   }

   public void unlock(IRecipe var1) {
      if (!☃.isDynamic()) {
         this.recipes.set(getRecipeId(☃));
      }
   }

   public boolean isUnlocked(@Nullable IRecipe var1) {
      return this.recipes.get(getRecipeId(☃));
   }

   public void lock(IRecipe var1) {
      int ☃ = getRecipeId(☃);
      this.recipes.clear(☃);
      this.newRecipes.clear(☃);
   }

   protected static int getRecipeId(@Nullable IRecipe var0) {
      return CraftingManager.REGISTRY.getIDForObject(☃);
   }

   public boolean isNew(IRecipe var1) {
      return this.newRecipes.get(getRecipeId(☃));
   }

   public void markSeen(IRecipe var1) {
      this.newRecipes.clear(getRecipeId(☃));
   }

   public void markNew(IRecipe var1) {
      this.newRecipes.set(getRecipeId(☃));
   }

   public boolean isGuiOpen() {
      return this.isGuiOpen;
   }

   public void setGuiOpen(boolean var1) {
      this.isGuiOpen = ☃;
   }

   public boolean isFilteringCraftable() {
      return this.isFilteringCraftable;
   }

   public void setFilteringCraftable(boolean var1) {
      this.isFilteringCraftable = ☃;
   }
}
