package net.minecraft.client.util;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;

public class SearchTreeManager implements IResourceManagerReloadListener {
   public static final SearchTreeManager.Key<ItemStack> ITEMS = new SearchTreeManager.Key<>();
   public static final SearchTreeManager.Key<RecipeList> RECIPES = new SearchTreeManager.Key<>();
   private final Map<SearchTreeManager.Key<?>, SearchTree<?>> trees = Maps.newHashMap();

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      for (SearchTree<?> ☃ : this.trees.values()) {
         ☃.recalculate();
      }
   }

   public <T> void register(SearchTreeManager.Key<T> var1, SearchTree<T> var2) {
      this.trees.put(☃, ☃);
   }

   public <T> ISearchTree<T> get(SearchTreeManager.Key<T> var1) {
      return (ISearchTree<T>)this.trees.get(☃);
   }

   public static class Key<T> {
   }
}
