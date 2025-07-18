package net.minecraft.block.properties;

import com.google.common.base.MoreObjects;

public abstract class PropertyHelper<T extends Comparable<T>> implements IProperty<T> {
   private final Class<T> valueClass;
   private final String name;

   protected PropertyHelper(String var1, Class<T> var2) {
      this.valueClass = ☃;
      this.name = ☃;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public Class<T> getValueClass() {
      return this.valueClass;
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("name", this.name).add("clazz", this.valueClass).add("values", this.getAllowedValues()).toString();
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof PropertyHelper)) {
         return false;
      } else {
         PropertyHelper<?> ☃ = (PropertyHelper<?>)☃;
         return this.valueClass.equals(☃.valueClass) && this.name.equals(☃.name);
      }
   }

   @Override
   public int hashCode() {
      return 31 * this.valueClass.hashCode() + this.name.hashCode();
   }
}
