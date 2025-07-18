package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;

public class PropertyInteger extends PropertyHelper<Integer> {
   private final ImmutableSet<Integer> allowedValues;

   protected PropertyInteger(String var1, int var2, int var3) {
      super(☃, Integer.class);
      if (☃ < 0) {
         throw new IllegalArgumentException("Min value of " + ☃ + " must be 0 or greater");
      } else if (☃ <= ☃) {
         throw new IllegalArgumentException("Max value of " + ☃ + " must be greater than min (" + ☃ + ")");
      } else {
         Set<Integer> ☃ = Sets.newHashSet();

         for (int ☃x = ☃; ☃x <= ☃; ☃x++) {
            ☃.add(☃x);
         }

         this.allowedValues = ImmutableSet.copyOf(☃);
      }
   }

   @Override
   public Collection<Integer> getAllowedValues() {
      return this.allowedValues;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ instanceof PropertyInteger && super.equals(☃)) {
         PropertyInteger ☃ = (PropertyInteger)☃;
         return this.allowedValues.equals(☃.allowedValues);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return 31 * super.hashCode() + this.allowedValues.hashCode();
   }

   public static PropertyInteger create(String var0, int var1, int var2) {
      return new PropertyInteger(☃, ☃, ☃);
   }

   @Override
   public Optional<Integer> parseValue(String var1) {
      try {
         Integer ☃ = Integer.valueOf(☃);
         return this.allowedValues.contains(☃) ? Optional.of(☃) : Optional.absent();
      } catch (NumberFormatException var3) {
         return Optional.absent();
      }
   }

   public String getName(Integer var1) {
      return ☃.toString();
   }
}
