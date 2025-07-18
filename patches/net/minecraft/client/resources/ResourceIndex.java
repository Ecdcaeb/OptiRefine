package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceIndex {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Map<String, File> resourceMap = Maps.newHashMap();

   protected ResourceIndex() {
   }

   public ResourceIndex(File var1, String var2) {
      File ☃ = new File(☃, "objects");
      File ☃x = new File(☃, "indexes/" + ☃ + ".json");
      BufferedReader ☃xx = null;

      try {
         ☃xx = Files.newReader(☃x, StandardCharsets.UTF_8);
         JsonObject ☃xxx = new JsonParser().parse(☃xx).getAsJsonObject();
         JsonObject ☃xxxx = JsonUtils.getJsonObject(☃xxx, "objects", null);
         if (☃xxxx != null) {
            for (Entry<String, JsonElement> ☃xxxxx : ☃xxxx.entrySet()) {
               JsonObject ☃xxxxxx = (JsonObject)☃xxxxx.getValue();
               String ☃xxxxxxx = ☃xxxxx.getKey();
               String[] ☃xxxxxxxx = ☃xxxxxxx.split("/", 2);
               String ☃xxxxxxxxx = ☃xxxxxxxx.length == 1 ? ☃xxxxxxxx[0] : ☃xxxxxxxx[0] + ":" + ☃xxxxxxxx[1];
               String ☃xxxxxxxxxx = JsonUtils.getString(☃xxxxxx, "hash");
               File ☃xxxxxxxxxxx = new File(☃, ☃xxxxxxxxxx.substring(0, 2) + "/" + ☃xxxxxxxxxx);
               this.resourceMap.put(☃xxxxxxxxx, ☃xxxxxxxxxxx);
            }
         }
      } catch (JsonParseException var20) {
         LOGGER.error("Unable to parse resource index file: {}", ☃x);
      } catch (FileNotFoundException var21) {
         LOGGER.error("Can't find the resource index file: {}", ☃x);
      } finally {
         IOUtils.closeQuietly(☃xx);
      }
   }

   @Nullable
   public File getFile(ResourceLocation var1) {
      String ☃ = ☃.toString();
      return this.resourceMap.get(☃);
   }

   public boolean isFileExisting(ResourceLocation var1) {
      File ☃ = this.getFile(☃);
      return ☃ != null && ☃.isFile();
   }

   public File getPackMcmeta() {
      return this.resourceMap.get("pack.mcmeta");
   }
}
