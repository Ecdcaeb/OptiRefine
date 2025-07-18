package net.minecraft.client.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class RecipeBookClient extends RecipeBook {
   public static final Map<CreativeTabs, List<RecipeList>> RECIPES_BY_TAB = Maps.newHashMap();
   public static final List<RecipeList> ALL_RECIPES = Lists.newArrayList();

   private static RecipeList newRecipeList(CreativeTabs var0) {
      RecipeList ☃ = new RecipeList();
      ALL_RECIPES.add(☃);
      RECIPES_BY_TAB.computeIfAbsent(☃, var0x -> new ArrayList<>()).add(☃);
      RECIPES_BY_TAB.computeIfAbsent(CreativeTabs.SEARCH, var0x -> new ArrayList<>()).add(☃);
      return ☃;
   }

   private static CreativeTabs getItemStackTab(ItemStack var0) {
      CreativeTabs ☃ = ☃.getItem().getCreativeTab();
      if (☃ == CreativeTabs.BUILDING_BLOCKS || ☃ == CreativeTabs.TOOLS || ☃ == CreativeTabs.REDSTONE) {
         return ☃;
      } else {
         return ☃ == CreativeTabs.COMBAT ? CreativeTabs.TOOLS : CreativeTabs.MISC;
      }
   }

   static {
      Table<CreativeTabs, String, RecipeList> ☃ = HashBasedTable.create();

      for (IRecipe ☃x : CraftingManager.REGISTRY) {
         if (!☃x.isDynamic()) {
            CreativeTabs ☃xx = getItemStackTab(☃x.getRecipeOutput());
            String ☃xxx = ☃x.getGroup();
            RecipeList ☃xxxx;
            if (☃xxx.isEmpty()) {
               ☃xxxx = newRecipeList(☃xx);
            } else {
               ☃xxxx = (RecipeList)☃.get(☃xx, ☃xxx);
               if (☃xxxx == null) {
                  ☃xxxx = newRecipeList(☃xx);
                  ☃.put(☃xx, ☃xxx, ☃xxxx);
               }
            }

            ☃xxxx.add(☃x);
         }
      }
   }
}
