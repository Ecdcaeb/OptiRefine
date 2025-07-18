package net.minecraft.item.crafting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CraftingManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private static int nextAvailableId;
   public static final RegistryNamespaced<ResourceLocation, IRecipe> REGISTRY = new RegistryNamespaced<>();

   public static boolean init() {
      try {
         register("armordye", new RecipesArmorDyes());
         register("bookcloning", new RecipeBookCloning());
         register("mapcloning", new RecipesMapCloning());
         register("mapextending", new RecipesMapExtending());
         register("fireworks", new RecipeFireworks());
         register("repairitem", new RecipeRepairItem());
         register("tippedarrow", new RecipeTippedArrow());
         register("bannerduplicate", new RecipesBanners.RecipeDuplicatePattern());
         register("banneraddpattern", new RecipesBanners.RecipeAddPattern());
         register("shielddecoration", new ShieldRecipes.Decoration());
         register("shulkerboxcoloring", new ShulkerBoxRecipes.ShulkerBoxColoring());
         return parseJsonRecipes();
      } catch (Throwable var1) {
         return false;
      }
   }

   private static boolean parseJsonRecipes() {
      FileSystem ☃ = null;
      Gson ☃x = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

      try {
         URL ☃xx = CraftingManager.class.getResource("/assets/.mcassetsroot");
         if (☃xx == null) {
            LOGGER.error("Couldn't find .mcassetsroot");
            return false;
         } else {
            URI ☃xxx = ☃xx.toURI();
            Path ☃xxxx;
            if ("file".equals(☃xxx.getScheme())) {
               ☃xxxx = Paths.get(CraftingManager.class.getResource("/assets/minecraft/recipes").toURI());
            } else {
               if (!"jar".equals(☃xxx.getScheme())) {
                  LOGGER.error("Unsupported scheme " + ☃xxx + " trying to list all recipes");
                  return false;
               }

               ☃ = FileSystems.newFileSystem(☃xxx, Collections.emptyMap());
               ☃xxxx = ☃.getPath("/assets/minecraft/recipes");
            }

            Iterator<Path> ☃xxxxx = Files.walk(☃xxxx).iterator();

            while (☃xxxxx.hasNext()) {
               Path ☃xxxxxx = ☃xxxxx.next();
               if ("json".equals(FilenameUtils.getExtension(☃xxxxxx.toString()))) {
                  Path ☃xxxxxxx = ☃xxxx.relativize(☃xxxxxx);
                  String ☃xxxxxxxx = FilenameUtils.removeExtension(☃xxxxxxx.toString()).replaceAll("\\\\", "/");
                  ResourceLocation ☃xxxxxxxxx = new ResourceLocation(☃xxxxxxxx);
                  BufferedReader ☃xxxxxxxxxx = null;

                  try {
                     ☃xxxxxxxxxx = Files.newBufferedReader(☃xxxxxx);
                     register(☃xxxxxxxx, parseRecipeJson(JsonUtils.fromJson(☃x, ☃xxxxxxxxxx, JsonObject.class)));
                  } catch (JsonParseException var25) {
                     LOGGER.error("Parsing error loading recipe " + ☃xxxxxxxxx, var25);
                     return false;
                  } catch (IOException var26) {
                     LOGGER.error("Couldn't read recipe " + ☃xxxxxxxxx + " from " + ☃xxxxxx, var26);
                     return false;
                  } finally {
                     IOUtils.closeQuietly(☃xxxxxxxxxx);
                  }
               }
            }

            return true;
         }
      } catch (IOException | URISyntaxException var28) {
         LOGGER.error("Couldn't get a list of all recipe files", var28);
         return false;
      } finally {
         IOUtils.closeQuietly(☃);
      }
   }

   private static IRecipe parseRecipeJson(JsonObject var0) {
      String ☃ = JsonUtils.getString(☃, "type");
      if ("crafting_shaped".equals(☃)) {
         return ShapedRecipes.deserialize(☃);
      } else if ("crafting_shapeless".equals(☃)) {
         return ShapelessRecipes.deserialize(☃);
      } else {
         throw new JsonSyntaxException("Invalid or unsupported recipe type '" + ☃ + "'");
      }
   }

   public static void register(String var0, IRecipe var1) {
      register(new ResourceLocation(☃), ☃);
   }

   public static void register(ResourceLocation var0, IRecipe var1) {
      if (REGISTRY.containsKey(☃)) {
         throw new IllegalStateException("Duplicate recipe ignored with ID " + ☃);
      } else {
         REGISTRY.register(nextAvailableId++, ☃, ☃);
      }
   }

   public static ItemStack findMatchingResult(InventoryCrafting var0, World var1) {
      for (IRecipe ☃ : REGISTRY) {
         if (☃.matches(☃, ☃)) {
            return ☃.getCraftingResult(☃);
         }
      }

      return ItemStack.EMPTY;
   }

   @Nullable
   public static IRecipe findMatchingRecipe(InventoryCrafting var0, World var1) {
      for (IRecipe ☃ : REGISTRY) {
         if (☃.matches(☃, ☃)) {
            return ☃;
         }
      }

      return null;
   }

   public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting var0, World var1) {
      for (IRecipe ☃ : REGISTRY) {
         if (☃.matches(☃, ☃)) {
            return ☃.getRemainingItems(☃);
         }
      }

      NonNullList<ItemStack> ☃x = NonNullList.withSize(☃.getSizeInventory(), ItemStack.EMPTY);

      for (int ☃xx = 0; ☃xx < ☃x.size(); ☃xx++) {
         ☃x.set(☃xx, ☃.getStackInSlot(☃xx));
      }

      return ☃x;
   }

   @Nullable
   public static IRecipe getRecipe(ResourceLocation var0) {
      return REGISTRY.getObject(☃);
   }

   public static int getIDForRecipe(IRecipe var0) {
      return REGISTRY.getIDForObject(☃);
   }

   @Nullable
   public static IRecipe getRecipeById(int var0) {
      return REGISTRY.getObjectById(☃);
   }
}
