package net.minecraft.util.registry;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V> {
   private final K defaultValueKey;
   private V defaultValue;

   public RegistryNamespacedDefaultedByKey(K var1) {
      this.defaultValueKey = ☃;
   }

   @Override
   public void register(int var1, K var2, V var3) {
      if (this.defaultValueKey.equals(☃)) {
         this.defaultValue = ☃;
      }

      super.register(☃, ☃, ☃);
   }

   public void validateKey() {
      Validate.notNull(this.defaultValue, "Missing default of DefaultedMappedRegistry: " + this.defaultValueKey, new Object[0]);
   }

   @Override
   public int getIDForObject(V var1) {
      int ☃ = super.getIDForObject(☃);
      return ☃ == -1 ? super.getIDForObject(this.defaultValue) : ☃;
   }

   @Nonnull
   @Override
   public K getNameForObject(V var1) {
      K ☃ = super.getNameForObject(☃);
      return ☃ == null ? this.defaultValueKey : ☃;
   }

   @Nonnull
   @Override
   public V getObject(@Nullable K var1) {
      V ☃ = super.getObject(☃);
      return ☃ == null ? this.defaultValue : ☃;
   }

   @Nonnull
   @Override
   public V getObjectById(int var1) {
      V ☃ = super.getObjectById(☃);
      return ☃ == null ? this.defaultValue : ☃;
   }

   @Nonnull
   @Override
   public V getRandomObject(Random var1) {
      V ☃ = super.getRandomObject(☃);
      return ☃ == null ? this.defaultValue : ☃;
   }
}
