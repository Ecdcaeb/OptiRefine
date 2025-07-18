package net.minecraft.block.properties;

import com.google.common.base.Optional;
import java.util.Collection;

public interface IProperty<T extends Comparable<T>> {
   String getName();

   Collection<T> getAllowedValues();

   Class<T> getValueClass();

   Optional<T> parseValue(String var1);

   String getName(T var1);
}
