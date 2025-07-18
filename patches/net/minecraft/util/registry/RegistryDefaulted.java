package net.minecraft.util.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RegistryDefaulted<K, V> extends RegistrySimple<K, V> {
   private final V defaultObject;

   public RegistryDefaulted(V var1) {
      this.defaultObject = ☃;
   }

   @Nonnull
   @Override
   public V getObject(@Nullable K var1) {
      V ☃ = super.getObject(☃);
      return ☃ == null ? this.defaultObject : ☃;
   }
}
