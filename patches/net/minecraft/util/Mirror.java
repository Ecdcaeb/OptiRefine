package net.minecraft.util;

public enum Mirror {
   NONE("no_mirror"),
   LEFT_RIGHT("mirror_left_right"),
   FRONT_BACK("mirror_front_back");

   private final String name;
   private static final String[] mirrorNames = new String[values().length];

   private Mirror(String var3) {
      this.name = ☃;
   }

   public int mirrorRotation(int var1, int var2) {
      int ☃ = ☃ / 2;
      int ☃x = ☃ > ☃ ? ☃ - ☃ : ☃;
      switch (this) {
         case FRONT_BACK:
            return (☃ - ☃x) % ☃;
         case LEFT_RIGHT:
            return (☃ - ☃x + ☃) % ☃;
         default:
            return ☃;
      }
   }

   public Rotation toRotation(EnumFacing var1) {
      EnumFacing.Axis ☃ = ☃.getAxis();
      return (this != LEFT_RIGHT || ☃ != EnumFacing.Axis.Z) && (this != FRONT_BACK || ☃ != EnumFacing.Axis.X) ? Rotation.NONE : Rotation.CLOCKWISE_180;
   }

   public EnumFacing mirror(EnumFacing var1) {
      switch (this) {
         case FRONT_BACK:
            if (☃ == EnumFacing.WEST) {
               return EnumFacing.EAST;
            } else {
               if (☃ == EnumFacing.EAST) {
                  return EnumFacing.WEST;
               }

               return ☃;
            }
         case LEFT_RIGHT:
            if (☃ == EnumFacing.NORTH) {
               return EnumFacing.SOUTH;
            } else {
               if (☃ == EnumFacing.SOUTH) {
                  return EnumFacing.NORTH;
               }

               return ☃;
            }
         default:
            return ☃;
      }
   }

   static {
      int ☃ = 0;

      for (Mirror ☃x : values()) {
         mirrorNames[☃++] = ☃x.name;
      }
   }
}
