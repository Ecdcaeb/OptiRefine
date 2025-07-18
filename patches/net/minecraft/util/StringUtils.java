package net.minecraft.util;

import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class StringUtils {
   private static final Pattern PATTERN_CONTROL_CODE = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

   public static String ticksToElapsedTime(int var0) {
      int ☃ = ☃ / 20;
      int ☃x = ☃ / 60;
      ☃ %= 60;
      return ☃ < 10 ? ☃x + ":0" + ☃ : ☃x + ":" + ☃;
   }

   public static String stripControlCodes(String var0) {
      return PATTERN_CONTROL_CODE.matcher(☃).replaceAll("");
   }

   public static boolean isNullOrEmpty(@Nullable String var0) {
      return org.apache.commons.lang3.StringUtils.isEmpty(☃);
   }
}
