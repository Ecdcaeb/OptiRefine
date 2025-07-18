package net.minecraft.client.util;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

public class RecipeItemHelper {
   public final Int2IntMap itemToCount = new Int2IntOpenHashMap();

   public void accountStack(ItemStack var1) {
      if (!☃.isEmpty() && !☃.isItemDamaged() && !☃.isItemEnchanted() && !☃.hasDisplayName()) {
         int ☃ = pack(☃);
         int ☃x = ☃.getCount();
         this.increment(☃, ☃x);
      }
   }

   public static int pack(ItemStack var0) {
      Item ☃ = ☃.getItem();
      int ☃x = ☃.getHasSubtypes() ? ☃.getMetadata() : 0;
      return Item.REGISTRY.getIDForObject(☃) << 16 | ☃x & 65535;
   }

   public boolean containsItem(int var1) {
      return this.itemToCount.get(☃) > 0;
   }

   public int tryTake(int var1, int var2) {
      int ☃ = this.itemToCount.get(☃);
      if (☃ >= ☃) {
         this.itemToCount.put(☃, ☃ - ☃);
         return ☃;
      } else {
         return 0;
      }
   }

   private void increment(int var1, int var2) {
      this.itemToCount.put(☃, this.itemToCount.get(☃) + ☃);
   }

   public boolean canCraft(IRecipe var1, @Nullable IntList var2) {
      return this.canCraft(☃, ☃, 1);
   }

   public boolean canCraft(IRecipe var1, @Nullable IntList var2, int var3) {
      return new RecipeItemHelper.RecipePicker(☃).tryPick(☃, ☃);
   }

   public int getBiggestCraftableStack(IRecipe var1, @Nullable IntList var2) {
      return this.getBiggestCraftableStack(☃, Integer.MAX_VALUE, ☃);
   }

   public int getBiggestCraftableStack(IRecipe var1, int var2, @Nullable IntList var3) {
      return new RecipeItemHelper.RecipePicker(☃).tryPickAll(☃, ☃);
   }

   public static ItemStack unpack(int var0) {
      return ☃ == 0 ? ItemStack.EMPTY : new ItemStack(Item.getItemById(☃ >> 16 & 65535), 1, ☃ & 65535);
   }

   public void clear() {
      this.itemToCount.clear();
   }

   class RecipePicker {
      private final IRecipe recipe;
      private final List<Ingredient> ingredients = Lists.newArrayList();
      private final int ingredientCount;
      private final int[] possessedIngredientStacks;
      private final int possessedIngredientStackCount;
      private final BitSet data;
      private IntList path = new IntArrayList();

      public RecipePicker(IRecipe var2) {
         this.recipe = ☃;
         this.ingredients.addAll(☃.getIngredients());
         this.ingredients.removeIf(var0 -> var0 == Ingredient.EMPTY);
         this.ingredientCount = this.ingredients.size();
         this.possessedIngredientStacks = this.getUniqueAvailIngredientItems();
         this.possessedIngredientStackCount = this.possessedIngredientStacks.length;
         this.data = new BitSet(
            this.ingredientCount + this.possessedIngredientStackCount + this.ingredientCount + this.ingredientCount * this.possessedIngredientStackCount
         );

         for (int ☃ = 0; ☃ < this.ingredients.size(); ☃++) {
            IntList ☃x = this.ingredients.get(☃).getValidItemStacksPacked();

            for (int ☃xx = 0; ☃xx < this.possessedIngredientStackCount; ☃xx++) {
               if (☃x.contains(this.possessedIngredientStacks[☃xx])) {
                  this.data.set(this.getIndex(true, ☃xx, ☃));
               }
            }
         }
      }

      public boolean tryPick(int var1, @Nullable IntList var2) {
         if (☃ <= 0) {
            return true;
         } else {
            int ☃;
            for (☃ = 0; this.dfs(☃); ☃++) {
               RecipeItemHelper.this.tryTake(this.possessedIngredientStacks[this.path.getInt(0)], ☃);
               int ☃x = this.path.size() - 1;
               this.setSatisfied(this.path.getInt(☃x));

               for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
                  this.toggleResidual((☃xx & 1) == 0, (Integer)this.path.get(☃xx), (Integer)this.path.get(☃xx + 1));
               }

               this.path.clear();
               this.data.clear(0, this.ingredientCount + this.possessedIngredientStackCount);
            }

            boolean ☃x = ☃ == this.ingredientCount;
            boolean ☃xx = ☃x && ☃ != null;
            if (☃xx) {
               ☃.clear();
            }

            this.data.clear(0, this.ingredientCount + this.possessedIngredientStackCount + this.ingredientCount);
            int ☃xxx = 0;
            List<Ingredient> ☃xxxx = this.recipe.getIngredients();

            for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.size(); ☃xxxxx++) {
               if (☃xx && ☃xxxx.get(☃xxxxx) == Ingredient.EMPTY) {
                  ☃.add(0);
               } else {
                  for (int ☃xxxxxx = 0; ☃xxxxxx < this.possessedIngredientStackCount; ☃xxxxxx++) {
                     if (this.hasResidual(false, ☃xxx, ☃xxxxxx)) {
                        this.toggleResidual(true, ☃xxxxxx, ☃xxx);
                        RecipeItemHelper.this.increment(this.possessedIngredientStacks[☃xxxxxx], ☃);
                        if (☃xx) {
                           ☃.add(this.possessedIngredientStacks[☃xxxxxx]);
                        }
                     }
                  }

                  ☃xxx++;
               }
            }

            return ☃x;
         }
      }

      private int[] getUniqueAvailIngredientItems() {
         IntCollection ☃ = new IntAVLTreeSet();

         for (Ingredient ☃x : this.ingredients) {
            ☃.addAll(☃x.getValidItemStacksPacked());
         }

         IntIterator ☃x = ☃.iterator();

         while (☃x.hasNext()) {
            if (!RecipeItemHelper.this.containsItem(☃x.nextInt())) {
               ☃x.remove();
            }
         }

         return ☃.toIntArray();
      }

      private boolean dfs(int var1) {
         int ☃ = this.possessedIngredientStackCount;

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            if (RecipeItemHelper.this.itemToCount.get(this.possessedIngredientStacks[☃x]) >= ☃) {
               this.visit(false, ☃x);

               while (!this.path.isEmpty()) {
                  int ☃xx = this.path.size();
                  boolean ☃xxx = (☃xx & 1) == 1;
                  int ☃xxxx = this.path.getInt(☃xx - 1);
                  if (!☃xxx && !this.isSatisfied(☃xxxx)) {
                     break;
                  }

                  int ☃xxxxx = ☃xxx ? this.ingredientCount : ☃;
                  int ☃xxxxxx = 0;

                  while (true) {
                     if (☃xxxxxx < ☃xxxxx) {
                        if (this.hasVisited(☃xxx, ☃xxxxxx) || !this.hasConnection(☃xxx, ☃xxxx, ☃xxxxxx) || !this.hasResidual(☃xxx, ☃xxxx, ☃xxxxxx)) {
                           ☃xxxxxx++;
                           continue;
                        }

                        this.visit(☃xxx, ☃xxxxxx);
                     }

                     ☃xxxxxx = this.path.size();
                     if (☃xxxxxx == ☃xx) {
                        this.path.removeInt(☃xxxxxx - 1);
                     }
                     break;
                  }
               }

               if (!this.path.isEmpty()) {
                  return true;
               }
            }
         }

         return false;
      }

      private boolean isSatisfied(int var1) {
         return this.data.get(this.getSatisfiedIndex(☃));
      }

      private void setSatisfied(int var1) {
         this.data.set(this.getSatisfiedIndex(☃));
      }

      private int getSatisfiedIndex(int var1) {
         return this.ingredientCount + this.possessedIngredientStackCount + ☃;
      }

      private boolean hasConnection(boolean var1, int var2, int var3) {
         return this.data.get(this.getIndex(☃, ☃, ☃));
      }

      private boolean hasResidual(boolean var1, int var2, int var3) {
         return ☃ != this.data.get(1 + this.getIndex(☃, ☃, ☃));
      }

      private void toggleResidual(boolean var1, int var2, int var3) {
         this.data.flip(1 + this.getIndex(☃, ☃, ☃));
      }

      private int getIndex(boolean var1, int var2, int var3) {
         int ☃ = ☃ ? ☃ * this.ingredientCount + ☃ : ☃ * this.ingredientCount + ☃;
         return this.ingredientCount + this.possessedIngredientStackCount + this.ingredientCount + 2 * ☃;
      }

      private void visit(boolean var1, int var2) {
         this.data.set(this.getVisitedIndex(☃, ☃));
         this.path.add(☃);
      }

      private boolean hasVisited(boolean var1, int var2) {
         return this.data.get(this.getVisitedIndex(☃, ☃));
      }

      private int getVisitedIndex(boolean var1, int var2) {
         return (☃ ? 0 : this.ingredientCount) + ☃;
      }

      public int tryPickAll(int var1, @Nullable IntList var2) {
         int ☃ = 0;
         int ☃x = Math.min(☃, this.getMinIngredientCount()) + 1;

         while (true) {
            int ☃xx = (☃ + ☃x) / 2;
            if (this.tryPick(☃xx, null)) {
               if (☃x - ☃ <= 1) {
                  if (☃xx > 0) {
                     this.tryPick(☃xx, ☃);
                  }

                  return ☃xx;
               }

               ☃ = ☃xx;
            } else {
               ☃x = ☃xx;
            }
         }
      }

      private int getMinIngredientCount() {
         int ☃ = Integer.MAX_VALUE;

         for (Ingredient ☃x : this.ingredients) {
            int ☃xx = 0;
            IntListIterator var5 = ☃x.getValidItemStacksPacked().iterator();

            while (var5.hasNext()) {
               int ☃xxx = (Integer)var5.next();
               ☃xx = Math.max(☃xx, RecipeItemHelper.this.itemToCount.get(☃xxx));
            }

            if (☃ > 0) {
               ☃ = Math.min(☃, ☃xx);
            }
         }

         return ☃;
      }
   }
}
