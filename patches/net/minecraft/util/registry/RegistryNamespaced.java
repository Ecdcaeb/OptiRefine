package net.minecraft.util.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.IntIdentityHashBiMap;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V> {
   protected final IntIdentityHashBiMap<V> underlyingIntegerMap = new IntIdentityHashBiMap(256);
   protected final Map<V, K> inverseObjectRegistry = ((BiMap)this.registryObjects).inverse();

   public void register(int var1, K var2, V var3) {
      this.underlyingIntegerMap.put(☃, ☃);
      this.putObject(☃, ☃);
   }

   @Override
   protected Map<K, V> createUnderlyingMap() {
      return HashBiMap.create();
   }

   @Nullable
   @Override
   public V getObject(@Nullable K var1) {
      return super.getObject(☃);
   }

   @Nullable
   public K getNameForObject(V var1) {
      return this.inverseObjectRegistry.get(☃);
   }

   @Override
   public boolean containsKey(K var1) {
      return super.containsKey(☃);
   }

   public int getIDForObject(@Nullable V var1) {
      return this.underlyingIntegerMap.getId(☃);
   }

   @Nullable
   public V getObjectById(int var1) {
      return this.underlyingIntegerMap.get(☃);
   }

   @Override
   public Iterator<V> iterator() {
      return this.underlyingIntegerMap.iterator();
   }
}
