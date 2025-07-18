package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapPopulator {
   public static <K, V> Map<K, V> createMap(Iterable<K> var0, Iterable<V> var1) {
      return populateMap(☃, ☃, Maps.newLinkedHashMap());
   }

   public static <K, V> Map<K, V> populateMap(Iterable<K> var0, Iterable<V> var1, Map<K, V> var2) {
      Iterator<V> ☃ = ☃.iterator();

      for (K ☃x : ☃) {
         ☃.put(☃x, ☃.next());
      }

      if (☃.hasNext()) {
         throw new NoSuchElementException();
      } else {
         return ☃;
      }
   }
}
