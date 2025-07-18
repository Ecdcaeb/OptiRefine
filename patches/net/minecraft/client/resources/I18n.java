package net.minecraft.client.resources;

public class I18n {
   private static Locale i18nLocale;

   static void setLocale(Locale var0) {
      i18nLocale = ☃;
   }

   public static String format(String var0, Object... var1) {
      return i18nLocale.formatMessage(☃, ☃);
   }

   public static boolean hasKey(String var0) {
      return i18nLocale.hasKey(☃);
   }
}
