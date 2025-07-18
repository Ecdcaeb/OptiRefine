package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.text.translation.LanguageMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanguageManager implements IResourceManagerReloadListener {
   private static final Logger LOGGER = LogManager.getLogger();
   private final MetadataSerializer metadataSerializer;
   private String currentLanguage;
   protected static final Locale CURRENT_LOCALE = new Locale();
   private final Map<String, Language> languageMap = Maps.newHashMap();

   public LanguageManager(MetadataSerializer var1, String var2) {
      this.metadataSerializer = ☃;
      this.currentLanguage = ☃;
      I18n.setLocale(CURRENT_LOCALE);
   }

   public void parseLanguageMetadata(List<IResourcePack> var1) {
      this.languageMap.clear();

      for (IResourcePack ☃ : ☃) {
         try {
            LanguageMetadataSection ☃x = ☃.getPackMetadata(this.metadataSerializer, "language");
            if (☃x != null) {
               for (Language ☃xx : ☃x.getLanguages()) {
                  if (!this.languageMap.containsKey(☃xx.getLanguageCode())) {
                     this.languageMap.put(☃xx.getLanguageCode(), ☃xx);
                  }
               }
            }
         } catch (RuntimeException var7) {
            LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", ☃.getPackName(), var7);
         } catch (IOException var8) {
            LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", ☃.getPackName(), var8);
         }
      }
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      List<String> ☃ = Lists.newArrayList(new String[]{"en_us"});
      if (!"en_us".equals(this.currentLanguage)) {
         ☃.add(this.currentLanguage);
      }

      CURRENT_LOCALE.loadLocaleDataFiles(☃, ☃);
      LanguageMap.replaceWith(CURRENT_LOCALE.properties);
   }

   public boolean isCurrentLocaleUnicode() {
      return CURRENT_LOCALE.isUnicode();
   }

   public boolean isCurrentLanguageBidirectional() {
      return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
   }

   public void setCurrentLanguage(Language var1) {
      this.currentLanguage = ☃.getLanguageCode();
   }

   public Language getCurrentLanguage() {
      String ☃ = this.languageMap.containsKey(this.currentLanguage) ? this.currentLanguage : "en_us";
      return this.languageMap.get(☃);
   }

   public SortedSet<Language> getLanguages() {
      return Sets.newTreeSet(this.languageMap.values());
   }

   public Language getLanguage(String var1) {
      return this.languageMap.get(☃);
   }
}
