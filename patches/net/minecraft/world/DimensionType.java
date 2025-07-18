package net.minecraft.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum DimensionType {
   OVERWORLD(0, "overworld", "", WorldProviderSurface.class),
   NETHER(-1, "the_nether", "_nether", WorldProviderHell.class),
   THE_END(1, "the_end", "_end", WorldProviderEnd.class);

   private final int id;
   private final String name;
   private final String suffix;
   private final Class<? extends WorldProvider> clazz;

   private DimensionType(int var3, String var4, String var5, Class<? extends WorldProvider> var6) {
      this.id = ☃;
      this.name = ☃;
      this.suffix = ☃;
      this.clazz = ☃;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public WorldProvider createDimension() {
      try {
         Constructor<? extends WorldProvider> ☃ = this.clazz.getConstructor();
         return ☃.newInstance();
      } catch (NoSuchMethodException var2) {
         throw new Error("Could not create new dimension", var2);
      } catch (InvocationTargetException var3) {
         throw new Error("Could not create new dimension", var3);
      } catch (InstantiationException var4) {
         throw new Error("Could not create new dimension", var4);
      } catch (IllegalAccessException var5) {
         throw new Error("Could not create new dimension", var5);
      }
   }

   public static DimensionType getById(int var0) {
      for (DimensionType ☃ : values()) {
         if (☃.getId() == ☃) {
            return ☃;
         }
      }

      throw new IllegalArgumentException("Invalid dimension id " + ☃);
   }

   public static DimensionType byName(String var0) {
      for (DimensionType ☃ : values()) {
         if (☃.getName().equals(☃)) {
            return ☃;
         }
      }

      throw new IllegalArgumentException("Invalid dimension " + ☃);
   }
}
