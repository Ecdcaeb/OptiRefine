package net.minecraft.util.text.translation;

@Deprecated
public class I18n {
   private static final LanguageMap localizedName = LanguageMap.getInstance();
   private static final LanguageMap fallbackTranslator = new LanguageMap();

   @Deprecated
   public static String translateToLocal(String var0) {
      return localizedName.translateKey(☃);
   }

   @Deprecated
   public static String translateToLocalFormatted(String var0, Object... var1) {
      return localizedName.translateKeyFormat(☃, ☃);
   }

   @Deprecated
   public static String translateToFallback(String var0) {
      return fallbackTranslator.translateKey(☃);
   }

   @Deprecated
   public static boolean canTranslate(String var0) {
      return localizedName.isKeyTranslated(☃);
   }

   public static long getLastTranslationUpdateTimeInMilliseconds() {
      return localizedName.getLastUpdateTimeInMilliseconds();
   }
}
