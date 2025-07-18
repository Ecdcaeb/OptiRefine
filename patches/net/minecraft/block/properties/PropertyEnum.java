package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import net.minecraft.util.IStringSerializable;

public class PropertyEnum<T extends Enum<T> & IStringSerializable> extends PropertyHelper<T> {
   private final ImmutableSet<T> allowedValues;
   private final Map<String, T> nameToValue = Maps.newHashMap();

   protected PropertyEnum(String var1, Class<T> var2, Collection<T> var3) {
      super(☃, ☃);
      this.allowedValues = ImmutableSet.copyOf(☃);

      for (T ☃ : ☃) {
         String ☃x = ☃.getName();
         if (this.nameToValue.containsKey(☃x)) {
            throw new IllegalArgumentException("Multiple values have the same name '" + ☃x + "'");
         }

         this.nameToValue.put(☃x, ☃);
      }
   }

   @Override
   public Collection<T> getAllowedValues() {
      return this.allowedValues;
   }

   @Override
   public Optional<T> parseValue(String var1) {
      return Optional.fromNullable(this.nameToValue.get(☃));
   }

   public String getName(T var1) {
      return ☃.getName();
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ instanceof PropertyEnum && super.equals(☃)) {
         PropertyEnum<?> ☃ = (PropertyEnum<?>)☃;
         return this.allowedValues.equals(☃.allowedValues) && this.nameToValue.equals(☃.nameToValue);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int ☃ = super.hashCode();
      ☃ = 31 * ☃ + this.allowedValues.hashCode();
      return 31 * ☃ + this.nameToValue.hashCode();
   }

   public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String var0, Class<T> var1) {
      return create(☃, ☃, Predicates.alwaysTrue());
   }

   public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String var0, Class<T> var1, Predicate<T> var2) {
      return create(☃, ☃, Collections2.filter(Lists.newArrayList(☃.getEnumConstants()), ☃));
   }

   public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String var0, Class<T> var1, T... var2) {
      return create(☃, ☃, Lists.newArrayList(☃));
   }

   public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String var0, Class<T> var1, Collection<T> var2) {
      return new PropertyEnum<>(☃, ☃, ☃);
   }
}
