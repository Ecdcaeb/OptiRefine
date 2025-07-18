package net.minecraft.util;

public enum Rotation {
   NONE("rotate_0"),
   CLOCKWISE_90("rotate_90"),
   CLOCKWISE_180("rotate_180"),
   COUNTERCLOCKWISE_90("rotate_270");

   private final String name;
   private static final String[] rotationNames = new String[values().length];

   private Rotation(String var3) {
      this.name = ☃;
   }

   public Rotation add(Rotation var1) {
      switch (☃) {
         case CLOCKWISE_180:
            switch (this) {
               case NONE:
                  return CLOCKWISE_180;
               case CLOCKWISE_90:
                  return COUNTERCLOCKWISE_90;
               case CLOCKWISE_180:
                  return NONE;
               case COUNTERCLOCKWISE_90:
                  return CLOCKWISE_90;
            }
         case COUNTERCLOCKWISE_90:
            switch (this) {
               case NONE:
                  return COUNTERCLOCKWISE_90;
               case CLOCKWISE_90:
                  return NONE;
               case CLOCKWISE_180:
                  return CLOCKWISE_90;
               case COUNTERCLOCKWISE_90:
                  return CLOCKWISE_180;
            }
         case CLOCKWISE_90:
            switch (this) {
               case NONE:
                  return CLOCKWISE_90;
               case CLOCKWISE_90:
                  return CLOCKWISE_180;
               case CLOCKWISE_180:
                  return COUNTERCLOCKWISE_90;
               case COUNTERCLOCKWISE_90:
                  return NONE;
            }
         default:
            return this;
      }
   }

   public EnumFacing rotate(EnumFacing var1) {
      if (☃.getAxis() == EnumFacing.Axis.Y) {
         return ☃;
      } else {
         switch (this) {
            case CLOCKWISE_90:
               return ☃.rotateY();
            case CLOCKWISE_180:
               return ☃.getOpposite();
            case COUNTERCLOCKWISE_90:
               return ☃.rotateYCCW();
            default:
               return ☃;
         }
      }
   }

   public int rotate(int var1, int var2) {
      switch (this) {
         case CLOCKWISE_90:
            return (☃ + ☃ / 4) % ☃;
         case CLOCKWISE_180:
            return (☃ + ☃ / 2) % ☃;
         case COUNTERCLOCKWISE_90:
            return (☃ + ☃ * 3 / 4) % ☃;
         default:
            return ☃;
      }
   }

   static {
      int ☃ = 0;

      for (Rotation ☃x : values()) {
         rotationNames[☃++] = ☃x.name;
      }
   }
}
