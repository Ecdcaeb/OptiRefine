package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public enum EnumFacing implements IStringSerializable {
   DOWN(0, 1, -1, "down", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Y, new Vec3i(0, -1, 0)),
   UP(1, 0, -1, "up", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Y, new Vec3i(0, 1, 0)),
   NORTH(2, 3, 2, "north", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, -1)),
   SOUTH(3, 2, 0, "south", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, 1)),
   WEST(4, 5, 1, "west", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X, new Vec3i(-1, 0, 0)),
   EAST(5, 4, 3, "east", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.X, new Vec3i(1, 0, 0));

   private final int index;
   private final int opposite;
   private final int horizontalIndex;
   private final String name;
   private final EnumFacing.Axis axis;
   private final EnumFacing.AxisDirection axisDirection;
   private final Vec3i directionVec;
   private static final EnumFacing[] VALUES = new EnumFacing[6];
   private static final EnumFacing[] HORIZONTALS = new EnumFacing[4];
   private static final Map<String, EnumFacing> NAME_LOOKUP = Maps.newHashMap();

   private EnumFacing(int var3, int var4, int var5, String var6, EnumFacing.AxisDirection var7, EnumFacing.Axis var8, Vec3i var9) {
      this.index = ☃;
      this.horizontalIndex = ☃;
      this.opposite = ☃;
      this.name = ☃;
      this.axis = ☃;
      this.axisDirection = ☃;
      this.directionVec = ☃;
   }

   public int getIndex() {
      return this.index;
   }

   public int getHorizontalIndex() {
      return this.horizontalIndex;
   }

   public EnumFacing.AxisDirection getAxisDirection() {
      return this.axisDirection;
   }

   public EnumFacing getOpposite() {
      return byIndex(this.opposite);
   }

   public EnumFacing rotateAround(EnumFacing.Axis var1) {
      switch (☃) {
         case X:
            if (this != WEST && this != EAST) {
               return this.rotateX();
            }

            return this;
         case Y:
            if (this != UP && this != DOWN) {
               return this.rotateY();
            }

            return this;
         case Z:
            if (this != NORTH && this != SOUTH) {
               return this.rotateZ();
            }

            return this;
         default:
            throw new IllegalStateException("Unable to get CW facing for axis " + ☃);
      }
   }

   public EnumFacing rotateY() {
      switch (this) {
         case NORTH:
            return EAST;
         case EAST:
            return SOUTH;
         case SOUTH:
            return WEST;
         case WEST:
            return NORTH;
         default:
            throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
      }
   }

   private EnumFacing rotateX() {
      switch (this) {
         case NORTH:
            return DOWN;
         case EAST:
         case WEST:
         default:
            throw new IllegalStateException("Unable to get X-rotated facing of " + this);
         case SOUTH:
            return UP;
         case UP:
            return NORTH;
         case DOWN:
            return SOUTH;
      }
   }

   private EnumFacing rotateZ() {
      switch (this) {
         case EAST:
            return DOWN;
         case SOUTH:
         default:
            throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
         case WEST:
            return UP;
         case UP:
            return EAST;
         case DOWN:
            return WEST;
      }
   }

   public EnumFacing rotateYCCW() {
      switch (this) {
         case NORTH:
            return WEST;
         case EAST:
            return NORTH;
         case SOUTH:
            return EAST;
         case WEST:
            return SOUTH;
         default:
            throw new IllegalStateException("Unable to get CCW facing of " + this);
      }
   }

   public int getXOffset() {
      return this.axis == EnumFacing.Axis.X ? this.axisDirection.getOffset() : 0;
   }

   public int getYOffset() {
      return this.axis == EnumFacing.Axis.Y ? this.axisDirection.getOffset() : 0;
   }

   public int getZOffset() {
      return this.axis == EnumFacing.Axis.Z ? this.axisDirection.getOffset() : 0;
   }

   public String getName2() {
      return this.name;
   }

   public EnumFacing.Axis getAxis() {
      return this.axis;
   }

   @Nullable
   public static EnumFacing byName(String var0) {
      return ☃ == null ? null : NAME_LOOKUP.get(☃.toLowerCase(Locale.ROOT));
   }

   public static EnumFacing byIndex(int var0) {
      return VALUES[MathHelper.abs(☃ % VALUES.length)];
   }

   public static EnumFacing byHorizontalIndex(int var0) {
      return HORIZONTALS[MathHelper.abs(☃ % HORIZONTALS.length)];
   }

   public static EnumFacing fromAngle(double var0) {
      return byHorizontalIndex(MathHelper.floor(☃ / 90.0 + 0.5) & 3);
   }

   public float getHorizontalAngle() {
      return (this.horizontalIndex & 3) * 90;
   }

   public static EnumFacing random(Random var0) {
      return values()[☃.nextInt(values().length)];
   }

   public static EnumFacing getFacingFromVector(float var0, float var1, float var2) {
      EnumFacing ☃ = NORTH;
      float ☃x = Float.MIN_VALUE;

      for (EnumFacing ☃xx : values()) {
         float ☃xxx = ☃ * ☃xx.directionVec.getX() + ☃ * ☃xx.directionVec.getY() + ☃ * ☃xx.directionVec.getZ();
         if (☃xxx > ☃x) {
            ☃x = ☃xxx;
            ☃ = ☃xx;
         }
      }

      return ☃;
   }

   @Override
   public String toString() {
      return this.name;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public static EnumFacing getFacingFromAxis(EnumFacing.AxisDirection var0, EnumFacing.Axis var1) {
      for (EnumFacing ☃ : values()) {
         if (☃.getAxisDirection() == ☃ && ☃.getAxis() == ☃) {
            return ☃;
         }
      }

      throw new IllegalArgumentException("No such direction: " + ☃ + " " + ☃);
   }

   public static EnumFacing getDirectionFromEntityLiving(BlockPos var0, EntityLivingBase var1) {
      if (Math.abs(☃.posX - (☃.getX() + 0.5F)) < 2.0 && Math.abs(☃.posZ - (☃.getZ() + 0.5F)) < 2.0) {
         double ☃ = ☃.posY + ☃.getEyeHeight();
         if (☃ - ☃.getY() > 2.0) {
            return UP;
         }

         if (☃.getY() - ☃ > 0.0) {
            return DOWN;
         }
      }

      return ☃.getHorizontalFacing().getOpposite();
   }

   public Vec3i getDirectionVec() {
      return this.directionVec;
   }

   static {
      for (EnumFacing ☃ : values()) {
         VALUES[☃.index] = ☃;
         if (☃.getAxis().isHorizontal()) {
            HORIZONTALS[☃.horizontalIndex] = ☃;
         }

         NAME_LOOKUP.put(☃.getName2().toLowerCase(Locale.ROOT), ☃);
      }
   }

   public static enum Axis implements Predicate<EnumFacing>, IStringSerializable {
      X("x", EnumFacing.Plane.HORIZONTAL),
      Y("y", EnumFacing.Plane.VERTICAL),
      Z("z", EnumFacing.Plane.HORIZONTAL);

      private static final Map<String, EnumFacing.Axis> NAME_LOOKUP = Maps.newHashMap();
      private final String name;
      private final EnumFacing.Plane plane;

      private Axis(String var3, EnumFacing.Plane var4) {
         this.name = ☃;
         this.plane = ☃;
      }

      @Nullable
      public static EnumFacing.Axis byName(String var0) {
         return ☃ == null ? null : NAME_LOOKUP.get(☃.toLowerCase(Locale.ROOT));
      }

      public String getName2() {
         return this.name;
      }

      public boolean isVertical() {
         return this.plane == EnumFacing.Plane.VERTICAL;
      }

      public boolean isHorizontal() {
         return this.plane == EnumFacing.Plane.HORIZONTAL;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public boolean apply(@Nullable EnumFacing var1) {
         return ☃ != null && ☃.getAxis() == this;
      }

      public EnumFacing.Plane getPlane() {
         return this.plane;
      }

      @Override
      public String getName() {
         return this.name;
      }

      static {
         for (EnumFacing.Axis ☃ : values()) {
            NAME_LOOKUP.put(☃.getName2().toLowerCase(Locale.ROOT), ☃);
         }
      }
   }

   public static enum AxisDirection {
      POSITIVE(1, "Towards positive"),
      NEGATIVE(-1, "Towards negative");

      private final int offset;
      private final String description;

      private AxisDirection(int var3, String var4) {
         this.offset = ☃;
         this.description = ☃;
      }

      public int getOffset() {
         return this.offset;
      }

      @Override
      public String toString() {
         return this.description;
      }
   }

   public static enum Plane implements Predicate<EnumFacing>, Iterable<EnumFacing> {
      HORIZONTAL,
      VERTICAL;

      public EnumFacing[] facings() {
         switch (this) {
            case HORIZONTAL:
               return new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
            case VERTICAL:
               return new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
            default:
               throw new Error("Someone's been tampering with the universe!");
         }
      }

      public EnumFacing random(Random var1) {
         EnumFacing[] ☃ = this.facings();
         return ☃[☃.nextInt(☃.length)];
      }

      public boolean apply(@Nullable EnumFacing var1) {
         return ☃ != null && ☃.getAxis().getPlane() == this;
      }

      @Override
      public Iterator<EnumFacing> iterator() {
         return Iterators.forArray(this.facings());
      }
   }
}
