package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class Locale {
   private static final Splitter SPLITTER = Splitter.on('=').limit(2);
   private static final Pattern PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
   Map<String, String> properties = Maps.newHashMap();
   private boolean unicode;

   public synchronized void loadLocaleDataFiles(IResourceManager var1, List<String> var2) {
      this.properties.clear();

      for (String ☃ : ☃) {
         String ☃x = String.format("lang/%s.lang", ☃);

         for (String ☃xx : ☃.getResourceDomains()) {
            try {
               this.loadLocaleData(☃.getAllResources(new ResourceLocation(☃xx, ☃x)));
            } catch (IOException var9) {
            }
         }
      }

      this.checkUnicode();
   }

   public boolean isUnicode() {
      return this.unicode;
   }

   private void checkUnicode() {
      this.unicode = false;
      int ☃ = 0;
      int ☃x = 0;

      for (String ☃xx : this.properties.values()) {
         int ☃xxx = ☃xx.length();
         ☃x += ☃xxx;

         for (int ☃xxxx = 0; ☃xxxx < ☃xxx; ☃xxxx++) {
            if (☃xx.charAt(☃xxxx) >= 256) {
               ☃++;
            }
         }
      }

      float ☃xx = (float)☃ / ☃x;
      this.unicode = ☃xx > 0.1;
   }

   private void loadLocaleData(List<IResource> var1) throws IOException {
      for (IResource ☃ : ☃) {
         InputStream ☃x = ☃.getInputStream();

         try {
            this.loadLocaleData(☃x);
         } finally {
            IOUtils.closeQuietly(☃x);
         }
      }
   }

   private void loadLocaleData(InputStream var1) throws IOException {
      for (String ☃ : IOUtils.readLines(☃, StandardCharsets.UTF_8)) {
         if (!☃.isEmpty() && ☃.charAt(0) != '#') {
            String[] ☃x = (String[])Iterables.toArray(SPLITTER.split(☃), String.class);
            if (☃x != null && ☃x.length == 2) {
               String ☃xx = ☃x[0];
               String ☃xxx = PATTERN.matcher(☃x[1]).replaceAll("%$1s");
               this.properties.put(☃xx, ☃xxx);
            }
         }
      }
   }

   private String translateKeyPrivate(String var1) {
      String ☃ = this.properties.get(☃);
      return ☃ == null ? ☃ : ☃;
   }

   public String formatMessage(String var1, Object[] var2) {
      String ☃ = this.translateKeyPrivate(☃);

      try {
         return String.format(☃, ☃);
      } catch (IllegalFormatException var5) {
         return "Format error: " + ☃;
      }
   }

   public boolean hasKey(String var1) {
      return this.properties.containsKey(☃);
   }
}
