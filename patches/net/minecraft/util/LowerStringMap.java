package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class LowerStringMap<V> implements Map<String, V> {
   private final Map<String, V> internalMap = Maps.newLinkedHashMap();

   @Override
   public int size() {
      return this.internalMap.size();
   }

   @Override
   public boolean isEmpty() {
      return this.internalMap.isEmpty();
   }

   @Override
   public boolean containsKey(Object var1) {
      return this.internalMap.containsKey(☃.toString().toLowerCase(Locale.ROOT));
   }

   @Override
   public boolean containsValue(Object var1) {
      return this.internalMap.containsKey(☃);
   }

   @Override
   public V get(Object var1) {
      return this.internalMap.get(☃.toString().toLowerCase(Locale.ROOT));
   }

   public V put(String var1, V var2) {
      return this.internalMap.put(☃.toLowerCase(Locale.ROOT), ☃);
   }

   @Override
   public V remove(Object var1) {
      return this.internalMap.remove(☃.toString().toLowerCase(Locale.ROOT));
   }

   @Override
   public void putAll(Map<? extends String, ? extends V> var1) {
      for (Entry<? extends String, ? extends V> ☃ : ☃.entrySet()) {
         this.put(☃.getKey(), (V)☃.getValue());
      }
   }

   @Override
   public void clear() {
      this.internalMap.clear();
   }

   @Override
   public Set<String> keySet() {
      return this.internalMap.keySet();
   }

   @Override
   public Collection<V> values() {
      return this.internalMap.values();
   }

   @Override
   public Set<Entry<String, V>> entrySet() {
      return this.internalMap.entrySet();
   }
}
