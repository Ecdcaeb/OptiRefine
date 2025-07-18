package net.minecraft.network.datasync;

public class DataParameter<T> {
   private final int id;
   private final DataSerializer<T> serializer;

   public DataParameter(int var1, DataSerializer<T> var2) {
      this.id = ☃;
      this.serializer = ☃;
   }

   public int getId() {
      return this.id;
   }

   public DataSerializer<T> getSerializer() {
      return this.serializer;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ != null && this.getClass() == ☃.getClass()) {
         DataParameter<?> ☃ = (DataParameter<?>)☃;
         return this.id == ☃.id;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.id;
   }
}
