package net.minecraft.util;

public class IntegerCache {
   private static final Integer[] CACHE = new Integer[65535];

   public static Integer getInteger(int var0) {
      return ☃ > 0 && ☃ < CACHE.length ? CACHE[☃] : ☃;
   }

   static {
      int ☃ = 0;

      for (int ☃x = CACHE.length; ☃ < ☃x; ☃++) {
         CACHE[☃] = ☃;
      }
   }
}
