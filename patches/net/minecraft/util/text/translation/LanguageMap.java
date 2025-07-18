package net.minecraft.util.text.translation;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public class LanguageMap {
   private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
   private static final Splitter EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2);
   private static final LanguageMap instance = new LanguageMap();
   private final Map<String, String> languageList = Maps.newHashMap();
   private long lastUpdateTimeInMilliseconds;

   public LanguageMap() {
      try {
         InputStream ☃ = LanguageMap.class.getResourceAsStream("/assets/minecraft/lang/en_us.lang");

         for (String ☃x : IOUtils.readLines(☃, StandardCharsets.UTF_8)) {
            if (!☃x.isEmpty() && ☃x.charAt(0) != '#') {
               String[] ☃xx = (String[])Iterables.toArray(EQUAL_SIGN_SPLITTER.split(☃x), String.class);
               if (☃xx != null && ☃xx.length == 2) {
                  String ☃xxx = ☃xx[0];
                  String ☃xxxx = NUMERIC_VARIABLE_PATTERN.matcher(☃xx[1]).replaceAll("%$1s");
                  this.languageList.put(☃xxx, ☃xxxx);
               }
            }
         }

         this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
      } catch (IOException var7) {
      }
   }

   static LanguageMap getInstance() {
      return instance;
   }

   public static synchronized void replaceWith(Map<String, String> var0) {
      instance.languageList.clear();
      instance.languageList.putAll(☃);
      instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
   }

   public synchronized String translateKey(String var1) {
      return this.tryTranslateKey(☃);
   }

   public synchronized String translateKeyFormat(String var1, Object... var2) {
      String ☃ = this.tryTranslateKey(☃);

      try {
         return String.format(☃, ☃);
      } catch (IllegalFormatException var5) {
         return "Format error: " + ☃;
      }
   }

   private String tryTranslateKey(String var1) {
      String ☃ = this.languageList.get(☃);
      return ☃ == null ? ☃ : ☃;
   }

   public synchronized boolean isKeyTranslated(String var1) {
      return this.languageList.containsKey(☃);
   }

   public long getLastUpdateTimeInMilliseconds() {
      return this.lastUpdateTimeInMilliseconds;
   }
}
