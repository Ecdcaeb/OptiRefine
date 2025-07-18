package net.minecraft.world.gen.layer;

import com.google.common.collect.Lists;
import java.util.List;

public class IntCache {
   private static int intCacheSize = 256;
   private static final List<int[]> freeSmallArrays = Lists.newArrayList();
   private static final List<int[]> inUseSmallArrays = Lists.newArrayList();
   private static final List<int[]> freeLargeArrays = Lists.newArrayList();
   private static final List<int[]> inUseLargeArrays = Lists.newArrayList();

   public static synchronized int[] getIntCache(int var0) {
      if (☃ <= 256) {
         if (freeSmallArrays.isEmpty()) {
            int[] ☃ = new int[256];
            inUseSmallArrays.add(☃);
            return ☃;
         } else {
            int[] ☃ = freeSmallArrays.remove(freeSmallArrays.size() - 1);
            inUseSmallArrays.add(☃);
            return ☃;
         }
      } else if (☃ > intCacheSize) {
         intCacheSize = ☃;
         freeLargeArrays.clear();
         inUseLargeArrays.clear();
         int[] ☃ = new int[intCacheSize];
         inUseLargeArrays.add(☃);
         return ☃;
      } else if (freeLargeArrays.isEmpty()) {
         int[] ☃ = new int[intCacheSize];
         inUseLargeArrays.add(☃);
         return ☃;
      } else {
         int[] ☃ = freeLargeArrays.remove(freeLargeArrays.size() - 1);
         inUseLargeArrays.add(☃);
         return ☃;
      }
   }

   public static synchronized void resetIntCache() {
      if (!freeLargeArrays.isEmpty()) {
         freeLargeArrays.remove(freeLargeArrays.size() - 1);
      }

      if (!freeSmallArrays.isEmpty()) {
         freeSmallArrays.remove(freeSmallArrays.size() - 1);
      }

      freeLargeArrays.addAll(inUseLargeArrays);
      freeSmallArrays.addAll(inUseSmallArrays);
      inUseLargeArrays.clear();
      inUseSmallArrays.clear();
   }

   public static synchronized String getCacheSizes() {
      return "cache: "
         + freeLargeArrays.size()
         + ", tcache: "
         + freeSmallArrays.size()
         + ", allocated: "
         + inUseLargeArrays.size()
         + ", tallocated: "
         + inUseSmallArrays.size();
   }
}
