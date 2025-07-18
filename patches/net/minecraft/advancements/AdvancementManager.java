package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvancementManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Gson GSON = new GsonBuilder()
      .registerTypeHierarchyAdapter(Advancement.Builder.class, new JsonDeserializer<Advancement.Builder>() {
         public Advancement.Builder deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            JsonObject ☃ = JsonUtils.getJsonObject(☃, "advancement");
            return Advancement.Builder.deserialize(☃, ☃);
         }
      })
      .registerTypeAdapter(AdvancementRewards.class, new AdvancementRewards.Deserializer())
      .registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer())
      .registerTypeHierarchyAdapter(Style.class, new Style.Serializer())
      .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
      .create();
   private static final AdvancementList ADVANCEMENT_LIST = new AdvancementList();
   private final File advancementsDir;
   private boolean hasErrored;

   public AdvancementManager(@Nullable File var1) {
      this.advancementsDir = ☃;
      this.reload();
   }

   public void reload() {
      this.hasErrored = false;
      ADVANCEMENT_LIST.clear();
      Map<ResourceLocation, Advancement.Builder> ☃ = this.loadCustomAdvancements();
      this.loadBuiltInAdvancements(☃);
      ADVANCEMENT_LIST.loadAdvancements(☃);

      for (Advancement ☃x : ADVANCEMENT_LIST.getRoots()) {
         if (☃x.getDisplay() != null) {
            AdvancementTreeNode.layout(☃x);
         }
      }
   }

   public boolean hasErrored() {
      return this.hasErrored;
   }

   private Map<ResourceLocation, Advancement.Builder> loadCustomAdvancements() {
      if (this.advancementsDir == null) {
         return Maps.newHashMap();
      } else {
         Map<ResourceLocation, Advancement.Builder> ☃ = Maps.newHashMap();
         this.advancementsDir.mkdirs();

         for (File ☃x : FileUtils.listFiles(this.advancementsDir, new String[]{"json"}, true)) {
            String ☃xx = FilenameUtils.removeExtension(this.advancementsDir.toURI().relativize(☃x.toURI()).toString());
            String[] ☃xxx = ☃xx.split("/", 2);
            if (☃xxx.length == 2) {
               ResourceLocation ☃xxxx = new ResourceLocation(☃xxx[0], ☃xxx[1]);

               try {
                  Advancement.Builder ☃xxxxx = JsonUtils.gsonDeserialize(
                     GSON, FileUtils.readFileToString(☃x, StandardCharsets.UTF_8), Advancement.Builder.class
                  );
                  if (☃xxxxx == null) {
                     LOGGER.error("Couldn't load custom advancement " + ☃xxxx + " from " + ☃x + " as it's empty or null");
                  } else {
                     ☃.put(☃xxxx, ☃xxxxx);
                  }
               } catch (IllegalArgumentException | JsonParseException var8) {
                  LOGGER.error("Parsing error loading custom advancement " + ☃xxxx, var8);
                  this.hasErrored = true;
               } catch (IOException var9) {
                  LOGGER.error("Couldn't read custom advancement " + ☃xxxx + " from " + ☃x, var9);
                  this.hasErrored = true;
               }
            }
         }

         return ☃;
      }
   }

   private void loadBuiltInAdvancements(Map<ResourceLocation, Advancement.Builder> var1) {
      FileSystem ☃ = null;

      try {
         URL ☃x = AdvancementManager.class.getResource("/assets/.mcassetsroot");
         if (☃x == null) {
            LOGGER.error("Couldn't find .mcassetsroot");
            this.hasErrored = true;
         } else {
            URI ☃xx = ☃x.toURI();
            Path ☃xxx;
            if ("file".equals(☃xx.getScheme())) {
               ☃xxx = Paths.get(CraftingManager.class.getResource("/assets/minecraft/advancements").toURI());
            } else {
               if (!"jar".equals(☃xx.getScheme())) {
                  LOGGER.error("Unsupported scheme " + ☃xx + " trying to list all built-in advancements (NYI?)");
                  this.hasErrored = true;
                  return;
               }

               ☃ = FileSystems.newFileSystem(☃xx, Collections.emptyMap());
               ☃xxx = ☃.getPath("/assets/minecraft/advancements");
            }

            Iterator<Path> ☃xxxx = Files.walk(☃xxx).iterator();

            while (☃xxxx.hasNext()) {
               Path ☃xxxxx = ☃xxxx.next();
               if ("json".equals(FilenameUtils.getExtension(☃xxxxx.toString()))) {
                  Path ☃xxxxxx = ☃xxx.relativize(☃xxxxx);
                  String ☃xxxxxxx = FilenameUtils.removeExtension(☃xxxxxx.toString()).replaceAll("\\\\", "/");
                  ResourceLocation ☃xxxxxxxx = new ResourceLocation("minecraft", ☃xxxxxxx);
                  if (!☃.containsKey(☃xxxxxxxx)) {
                     BufferedReader ☃xxxxxxxxx = null;

                     try {
                        ☃xxxxxxxxx = Files.newBufferedReader(☃xxxxx);
                        Advancement.Builder ☃xxxxxxxxxx = JsonUtils.fromJson(GSON, ☃xxxxxxxxx, Advancement.Builder.class);
                        ☃.put(☃xxxxxxxx, ☃xxxxxxxxxx);
                     } catch (JsonParseException var25) {
                        LOGGER.error("Parsing error loading built-in advancement " + ☃xxxxxxxx, var25);
                        this.hasErrored = true;
                     } catch (IOException var26) {
                        LOGGER.error("Couldn't read advancement " + ☃xxxxxxxx + " from " + ☃xxxxx, var26);
                        this.hasErrored = true;
                     } finally {
                        IOUtils.closeQuietly(☃xxxxxxxxx);
                     }
                  }
               }
            }
         }
      } catch (IOException | URISyntaxException var28) {
         LOGGER.error("Couldn't get a list of all built-in advancement files", var28);
         this.hasErrored = true;
      } finally {
         IOUtils.closeQuietly(☃);
      }
   }

   @Nullable
   public Advancement getAdvancement(ResourceLocation var1) {
      return ADVANCEMENT_LIST.getAdvancement(☃);
   }

   public Iterable<Advancement> getAdvancements() {
      return ADVANCEMENT_LIST.getAdvancements();
   }
}
