package net.minecraft.world.storage.loot;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootTableManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Gson GSON_INSTANCE = new GsonBuilder()
      .registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
      .registerTypeAdapter(LootPool.class, new LootPool.Serializer())
      .registerTypeAdapter(LootTable.class, new LootTable.Serializer())
      .registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.Serializer())
      .registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.Serializer())
      .registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.Serializer())
      .registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
      .create();
   private final LoadingCache<ResourceLocation, LootTable> registeredLootTables = CacheBuilder.newBuilder().build(new LootTableManager.Loader());
   private final File baseFolder;

   public LootTableManager(@Nullable File var1) {
      this.baseFolder = ☃;
      this.reloadLootTables();
   }

   public LootTable getLootTableFromLocation(ResourceLocation var1) {
      return (LootTable)this.registeredLootTables.getUnchecked(☃);
   }

   public void reloadLootTables() {
      this.registeredLootTables.invalidateAll();

      for (ResourceLocation ☃ : LootTableList.getAll()) {
         this.getLootTableFromLocation(☃);
      }
   }

   class Loader extends CacheLoader<ResourceLocation, LootTable> {
      private Loader() {
      }

      public LootTable load(ResourceLocation var1) throws Exception {
         if (☃.getPath().contains(".")) {
            LootTableManager.LOGGER.debug("Invalid loot table name '{}' (can't contain periods)", ☃);
            return LootTable.EMPTY_LOOT_TABLE;
         } else {
            LootTable ☃ = this.loadLootTable(☃);
            if (☃ == null) {
               ☃ = this.loadBuiltinLootTable(☃);
            }

            if (☃ == null) {
               ☃ = LootTable.EMPTY_LOOT_TABLE;
               LootTableManager.LOGGER.warn("Couldn't find resource table {}", ☃);
            }

            return ☃;
         }
      }

      @Nullable
      private LootTable loadLootTable(ResourceLocation var1) {
         if (LootTableManager.this.baseFolder == null) {
            return null;
         } else {
            File ☃ = new File(new File(LootTableManager.this.baseFolder, ☃.getNamespace()), ☃.getPath() + ".json");
            if (☃.exists()) {
               if (☃.isFile()) {
                  String ☃x;
                  try {
                     ☃x = Files.toString(☃, StandardCharsets.UTF_8);
                  } catch (IOException var6) {
                     LootTableManager.LOGGER.warn("Couldn't load loot table {} from {}", ☃, ☃, var6);
                     return LootTable.EMPTY_LOOT_TABLE;
                  }

                  try {
                     return JsonUtils.gsonDeserialize(LootTableManager.GSON_INSTANCE, ☃x, LootTable.class);
                  } catch (IllegalArgumentException | JsonParseException var5) {
                     LootTableManager.LOGGER.error("Couldn't load loot table {} from {}", ☃, ☃, var5);
                     return LootTable.EMPTY_LOOT_TABLE;
                  }
               } else {
                  LootTableManager.LOGGER.warn("Expected to find loot table {} at {} but it was a folder.", ☃, ☃);
                  return LootTable.EMPTY_LOOT_TABLE;
               }
            } else {
               return null;
            }
         }
      }

      @Nullable
      private LootTable loadBuiltinLootTable(ResourceLocation var1) {
         URL ☃ = LootTableManager.class.getResource("/assets/" + ☃.getNamespace() + "/loot_tables/" + ☃.getPath() + ".json");
         if (☃ != null) {
            String ☃x;
            try {
               ☃x = Resources.toString(☃, StandardCharsets.UTF_8);
            } catch (IOException var6) {
               LootTableManager.LOGGER.warn("Couldn't load loot table {} from {}", ☃, ☃, var6);
               return LootTable.EMPTY_LOOT_TABLE;
            }

            try {
               return JsonUtils.gsonDeserialize(LootTableManager.GSON_INSTANCE, ☃x, LootTable.class);
            } catch (JsonParseException var5) {
               LootTableManager.LOGGER.error("Couldn't load loot table {} from {}", ☃, ☃, var5);
               return LootTable.EMPTY_LOOT_TABLE;
            }
         } else {
            return null;
         }
      }
   }
}
