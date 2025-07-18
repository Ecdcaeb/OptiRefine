package net.minecraft.item.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ShapedRecipes implements IRecipe {
   private final int recipeWidth;
   private final int recipeHeight;
   private final NonNullList<Ingredient> recipeItems;
   private final ItemStack recipeOutput;
   private final String group;

   public ShapedRecipes(String var1, int var2, int var3, NonNullList<Ingredient> var4, ItemStack var5) {
      this.group = ☃;
      this.recipeWidth = ☃;
      this.recipeHeight = ☃;
      this.recipeItems = ☃;
      this.recipeOutput = ☃;
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
   public NonNullList<Ingredient> getIngredients() {
      return this.recipeItems;
   }

   @Override
   public boolean canFit(int var1, int var2) {
      return ☃ >= this.recipeWidth && ☃ >= this.recipeHeight;
   }

   @Override
   public boolean matches(InventoryCrafting var1, World var2) {
      for (int ☃ = 0; ☃ <= 3 - this.recipeWidth; ☃++) {
         for (int ☃x = 0; ☃x <= 3 - this.recipeHeight; ☃x++) {
            if (this.checkMatch(☃, ☃, ☃x, true)) {
               return true;
            }

            if (this.checkMatch(☃, ☃, ☃x, false)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean checkMatch(InventoryCrafting var1, int var2, int var3, boolean var4) {
      for (int ☃ = 0; ☃ < 3; ☃++) {
         for (int ☃x = 0; ☃x < 3; ☃x++) {
            int ☃xx = ☃ - ☃;
            int ☃xxx = ☃x - ☃;
            Ingredient ☃xxxx = Ingredient.EMPTY;
            if (☃xx >= 0 && ☃xxx >= 0 && ☃xx < this.recipeWidth && ☃xxx < this.recipeHeight) {
               if (☃) {
                  ☃xxxx = this.recipeItems.get(this.recipeWidth - ☃xx - 1 + ☃xxx * this.recipeWidth);
               } else {
                  ☃xxxx = this.recipeItems.get(☃xx + ☃xxx * this.recipeWidth);
               }
            }

            if (!☃xxxx.apply(☃.getStackInRowAndColumn(☃, ☃x))) {
               return false;
            }
         }
      }

      return true;
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting var1) {
      return this.getRecipeOutput().copy();
   }

   public int getWidth() {
      return this.recipeWidth;
   }

   public int getHeight() {
      return this.recipeHeight;
   }

   public static ShapedRecipes deserialize(JsonObject var0) {
      String ☃ = JsonUtils.getString(☃, "group", "");
      Map<String, Ingredient> ☃x = deserializeKey(JsonUtils.getJsonObject(☃, "key"));
      String[] ☃xx = shrink(patternFromJson(JsonUtils.getJsonArray(☃, "pattern")));
      int ☃xxx = ☃xx[0].length();
      int ☃xxxx = ☃xx.length;
      NonNullList<Ingredient> ☃xxxxx = deserializeIngredients(☃xx, ☃x, ☃xxx, ☃xxxx);
      ItemStack ☃xxxxxx = deserializeItem(JsonUtils.getJsonObject(☃, "result"), true);
      return new ShapedRecipes(☃, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx);
   }

   private static NonNullList<Ingredient> deserializeIngredients(String[] var0, Map<String, Ingredient> var1, int var2, int var3) {
      NonNullList<Ingredient> ☃ = NonNullList.withSize(☃ * ☃, Ingredient.EMPTY);
      Set<String> ☃x = Sets.newHashSet(☃.keySet());
      ☃x.remove(" ");

      for (int ☃xx = 0; ☃xx < ☃.length; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < ☃[☃xx].length(); ☃xxx++) {
            String ☃xxxx = ☃[☃xx].substring(☃xxx, ☃xxx + 1);
            Ingredient ☃xxxxx = ☃.get(☃xxxx);
            if (☃xxxxx == null) {
               throw new JsonSyntaxException("Pattern references symbol '" + ☃xxxx + "' but it's not defined in the key");
            }

            ☃x.remove(☃xxxx);
            ☃.set(☃xxx + ☃ * ☃xx, ☃xxxxx);
         }
      }

      if (!☃x.isEmpty()) {
         throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + ☃x);
      } else {
         return ☃;
      }
   }

   @VisibleForTesting
   static String[] shrink(String... var0) {
      int ☃ = Integer.MAX_VALUE;
      int ☃x = 0;
      int ☃xx = 0;
      int ☃xxx = 0;

      for (int ☃xxxx = 0; ☃xxxx < ☃.length; ☃xxxx++) {
         String ☃xxxxx = ☃[☃xxxx];
         ☃ = Math.min(☃, firstNonSpace(☃xxxxx));
         int ☃xxxxxx = lastNonSpace(☃xxxxx);
         ☃x = Math.max(☃x, ☃xxxxxx);
         if (☃xxxxxx < 0) {
            if (☃xx == ☃xxxx) {
               ☃xx++;
            }

            ☃xxx++;
         } else {
            ☃xxx = 0;
         }
      }

      if (☃.length == ☃xxx) {
         return new String[0];
      } else {
         String[] ☃xxxxx = new String[☃.length - ☃xxx - ☃xx];

         for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxxx.length; ☃xxxxxx++) {
            ☃xxxxx[☃xxxxxx] = ☃[☃xxxxxx + ☃xx].substring(☃, ☃x + 1);
         }

         return ☃xxxxx;
      }
   }

   private static int firstNonSpace(String var0) {
      int ☃ = 0;

      while (☃ < ☃.length() && ☃.charAt(☃) == ' ') {
         ☃++;
      }

      return ☃;
   }

   private static int lastNonSpace(String var0) {
      int ☃ = ☃.length() - 1;

      while (☃ >= 0 && ☃.charAt(☃) == ' ') {
         ☃--;
      }

      return ☃;
   }

   private static String[] patternFromJson(JsonArray var0) {
      String[] ☃ = new String[☃.size()];
      if (☃.length > 3) {
         throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
      } else if (☃.length == 0) {
         throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
      } else {
         for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
            String ☃xx = JsonUtils.getString(☃.get(☃x), "pattern[" + ☃x + "]");
            if (☃xx.length() > 3) {
               throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
            }

            if (☃x > 0 && ☃[0].length() != ☃xx.length()) {
               throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
            }

            ☃[☃x] = ☃xx;
         }

         return ☃;
      }
   }

   private static Map<String, Ingredient> deserializeKey(JsonObject var0) {
      Map<String, Ingredient> ☃ = Maps.newHashMap();

      for (Entry<String, JsonElement> ☃x : ☃.entrySet()) {
         if (☃x.getKey().length() != 1) {
            throw new JsonSyntaxException("Invalid key entry: '" + ☃x.getKey() + "' is an invalid symbol (must be 1 character only).");
         }

         if (" ".equals(☃x.getKey())) {
            throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
         }

         ☃.put(☃x.getKey(), deserializeIngredient(☃x.getValue()));
      }

      ☃.put(" ", Ingredient.EMPTY);
      return ☃;
   }

   public static Ingredient deserializeIngredient(@Nullable JsonElement var0) {
      if (☃ == null || ☃.isJsonNull()) {
         throw new JsonSyntaxException("Item cannot be null");
      } else if (☃.isJsonObject()) {
         return Ingredient.fromStacks(deserializeItem(☃.getAsJsonObject(), false));
      } else if (!☃.isJsonArray()) {
         throw new JsonSyntaxException("Expected item to be object or array of objects");
      } else {
         JsonArray ☃ = ☃.getAsJsonArray();
         if (☃.size() == 0) {
            throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
         } else {
            ItemStack[] ☃x = new ItemStack[☃.size()];

            for (int ☃xx = 0; ☃xx < ☃.size(); ☃xx++) {
               ☃x[☃xx] = deserializeItem(JsonUtils.getJsonObject(☃.get(☃xx), "item"), false);
            }

            return Ingredient.fromStacks(☃x);
         }
      }
   }

   public static ItemStack deserializeItem(JsonObject var0, boolean var1) {
      String ☃ = JsonUtils.getString(☃, "item");
      Item ☃x = Item.REGISTRY.getObject(new ResourceLocation(☃));
      if (☃x == null) {
         throw new JsonSyntaxException("Unknown item '" + ☃ + "'");
      } else if (☃x.getHasSubtypes() && !☃.has("data")) {
         throw new JsonParseException("Missing data for item '" + ☃ + "'");
      } else {
         int ☃xx = JsonUtils.getInt(☃, "data", 0);
         int ☃xxx = ☃ ? JsonUtils.getInt(☃, "count", 1) : 1;
         return new ItemStack(☃x, ☃xxx, ☃xx);
      }
   }
}
