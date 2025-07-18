package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;

public class PropertyBool extends PropertyHelper<Boolean> {
   private final ImmutableSet<Boolean> allowedValues = ImmutableSet.of(true, false);

   protected PropertyBool(String var1) {
      super(☃, Boolean.class);
   }

   @Override
   public Collection<Boolean> getAllowedValues() {
      return this.allowedValues;
   }

   public static PropertyBool create(String var0) {
      return new PropertyBool(☃);
   }

   @Override
   public Optional<Boolean> parseValue(String var1) {
      return !"true".equals(☃) && !"false".equals(☃) ? Optional.absent() : Optional.of(Boolean.valueOf(☃));
   }

   public String getName(Boolean var1) {
      return ☃.toString();
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ instanceof PropertyBool && super.equals(☃)) {
         PropertyBool ☃ = (PropertyBool)☃;
         return this.allowedValues.equals(☃.allowedValues);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return 31 * super.hashCode() + this.allowedValues.hashCode();
   }
}
