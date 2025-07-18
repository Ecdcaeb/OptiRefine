package net.minecraft.util.registry;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrySimple<K, V> implements IRegistry<K, V> {
   private static final Logger LOGGER = LogManager.getLogger();
   protected final Map<K, V> registryObjects = this.createUnderlyingMap();
   private Object[] values;

   protected Map<K, V> createUnderlyingMap() {
      return Maps.newHashMap();
   }

   @Nullable
   @Override
   public V getObject(@Nullable K var1) {
      return this.registryObjects.get(☃);
   }

   @Override
   public void putObject(K var1, V var2) {
      Validate.notNull(☃);
      Validate.notNull(☃);
      this.values = null;
      if (this.registryObjects.containsKey(☃)) {
         LOGGER.debug("Adding duplicate key '{}' to registry", ☃);
      }

      this.registryObjects.put(☃, ☃);
   }

   @Override
   public Set<K> getKeys() {
      return Collections.unmodifiableSet(this.registryObjects.keySet());
   }

   @Nullable
   public V getRandomObject(Random var1) {
      if (this.values == null) {
         Collection<?> ☃ = this.registryObjects.values();
         if (☃.isEmpty()) {
            return null;
         }

         this.values = ☃.toArray(new Object[☃.size()]);
      }

      return (V)this.values[☃.nextInt(this.values.length)];
   }

   public boolean containsKey(K var1) {
      return this.registryObjects.containsKey(☃);
   }

   @Override
   public Iterator<V> iterator() {
      return this.registryObjects.values().iterator();
   }
}
