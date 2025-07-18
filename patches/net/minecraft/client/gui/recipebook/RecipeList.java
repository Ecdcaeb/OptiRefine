package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;
import java.util.BitSet;
import java.util.List;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class RecipeList {
   private List<IRecipe> recipes = Lists.newArrayList();
   private final BitSet craftable = new BitSet();
   private final BitSet canFit = new BitSet();
   private final BitSet inBook = new BitSet();
   private boolean singleResultItem = true;

   public boolean isNotEmpty() {
      return !this.inBook.isEmpty();
   }

   public void updateKnownRecipes(RecipeBook var1) {
      for (int ☃ = 0; ☃ < this.recipes.size(); ☃++) {
         this.inBook.set(☃, ☃.isUnlocked(this.recipes.get(☃)));
      }
   }

   public void canCraft(RecipeItemHelper var1, int var2, int var3, RecipeBook var4) {
      for (int ☃ = 0; ☃ < this.recipes.size(); ☃++) {
         IRecipe ☃x = this.recipes.get(☃);
         boolean ☃xx = ☃x.canFit(☃, ☃) && ☃.isUnlocked(☃x);
         this.canFit.set(☃, ☃xx);
         this.craftable.set(☃, ☃xx && ☃.canCraft(☃x, null));
      }
   }

   public boolean isCraftable(IRecipe var1) {
      return this.craftable.get(this.recipes.indexOf(☃));
   }

   public boolean containsCraftableRecipes() {
      return !this.craftable.isEmpty();
   }

   public boolean containsValidRecipes() {
      return !this.canFit.isEmpty();
   }

   public List<IRecipe> getRecipes() {
      return this.recipes;
   }

   public List<IRecipe> getRecipes(boolean var1) {
      List<IRecipe> ☃ = Lists.newArrayList();

      for (int ☃x = this.inBook.nextSetBit(0); ☃x >= 0; ☃x = this.inBook.nextSetBit(☃x + 1)) {
         if ((☃ ? this.craftable : this.canFit).get(☃x)) {
            ☃.add(this.recipes.get(☃x));
         }
      }

      return ☃;
   }

   public List<IRecipe> getDisplayRecipes(boolean var1) {
      List<IRecipe> ☃ = Lists.newArrayList();

      for (int ☃x = this.inBook.nextSetBit(0); ☃x >= 0; ☃x = this.inBook.nextSetBit(☃x + 1)) {
         if (this.canFit.get(☃x) && this.craftable.get(☃x) == ☃) {
            ☃.add(this.recipes.get(☃x));
         }
      }

      return ☃;
   }

   public void add(IRecipe var1) {
      this.recipes.add(☃);
      if (this.singleResultItem) {
         ItemStack ☃ = this.recipes.get(0).getRecipeOutput();
         ItemStack ☃x = ☃.getRecipeOutput();
         this.singleResultItem = ItemStack.areItemsEqual(☃, ☃x) && ItemStack.areItemStackTagsEqual(☃, ☃x);
      }
   }

   public boolean hasSingleResultItem() {
      return this.singleResultItem;
   }
}
