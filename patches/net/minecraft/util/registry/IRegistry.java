package net.minecraft.util.registry;

import java.util.Set;
import javax.annotation.Nullable;

public interface IRegistry<K, V> extends Iterable<V> {
   @Nullable
   V getObject(K var1);

   void putObject(K var1, V var2);

   Set<K> getKeys();
}
