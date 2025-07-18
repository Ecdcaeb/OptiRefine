package net.minecraft.util.math;

import javax.annotation.Nullable;

public class Vec3d {
   public static final Vec3d ZERO = new Vec3d(0.0, 0.0, 0.0);
   public final double x;
   public final double y;
   public final double z;

   public Vec3d(double var1, double var3, double var5) {
      if (☃ == -0.0) {
         ☃ = 0.0;
      }

      if (☃ == -0.0) {
         ☃ = 0.0;
      }

      if (☃ == -0.0) {
         ☃ = 0.0;
      }

      this.x = ☃;
      this.y = ☃;
      this.z = ☃;
   }

   public Vec3d(Vec3i var1) {
      this(☃.getX(), ☃.getY(), ☃.getZ());
   }

   public Vec3d subtractReverse(Vec3d var1) {
      return new Vec3d(☃.x - this.x, ☃.y - this.y, ☃.z - this.z);
   }

   public Vec3d normalize() {
      double ☃ = MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
      return ☃ < 1.0E-4 ? ZERO : new Vec3d(this.x / ☃, this.y / ☃, this.z / ☃);
   }

   public double dotProduct(Vec3d var1) {
      return this.x * ☃.x + this.y * ☃.y + this.z * ☃.z;
   }

   public Vec3d crossProduct(Vec3d var1) {
      return new Vec3d(this.y * ☃.z - this.z * ☃.y, this.z * ☃.x - this.x * ☃.z, this.x * ☃.y - this.y * ☃.x);
   }

   public Vec3d subtract(Vec3d var1) {
      return this.subtract(☃.x, ☃.y, ☃.z);
   }

   public Vec3d subtract(double var1, double var3, double var5) {
      return this.add(-☃, -☃, -☃);
   }

   public Vec3d add(Vec3d var1) {
      return this.add(☃.x, ☃.y, ☃.z);
   }

   public Vec3d add(double var1, double var3, double var5) {
      return new Vec3d(this.x + ☃, this.y + ☃, this.z + ☃);
   }

   public double distanceTo(Vec3d var1) {
      double ☃ = ☃.x - this.x;
      double ☃x = ☃.y - this.y;
      double ☃xx = ☃.z - this.z;
      return MathHelper.sqrt(☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx);
   }

   public double squareDistanceTo(Vec3d var1) {
      double ☃ = ☃.x - this.x;
      double ☃x = ☃.y - this.y;
      double ☃xx = ☃.z - this.z;
      return ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
   }

   public double squareDistanceTo(double var1, double var3, double var5) {
      double ☃ = ☃ - this.x;
      double ☃x = ☃ - this.y;
      double ☃xx = ☃ - this.z;
      return ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
   }

   public Vec3d scale(double var1) {
      return new Vec3d(this.x * ☃, this.y * ☃, this.z * ☃);
   }

   public double length() {
      return MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public double lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   @Nullable
   public Vec3d getIntermediateWithXValue(Vec3d var1, double var2) {
      double ☃ = ☃.x - this.x;
      double ☃x = ☃.y - this.y;
      double ☃xx = ☃.z - this.z;
      if (☃ * ☃ < 1.0E-7F) {
         return null;
      } else {
         double ☃xxx = (☃ - this.x) / ☃;
         return !(☃xxx < 0.0) && !(☃xxx > 1.0) ? new Vec3d(this.x + ☃ * ☃xxx, this.y + ☃x * ☃xxx, this.z + ☃xx * ☃xxx) : null;
      }
   }

   @Nullable
   public Vec3d getIntermediateWithYValue(Vec3d var1, double var2) {
      double ☃ = ☃.x - this.x;
      double ☃x = ☃.y - this.y;
      double ☃xx = ☃.z - this.z;
      if (☃x * ☃x < 1.0E-7F) {
         return null;
      } else {
         double ☃xxx = (☃ - this.y) / ☃x;
         return !(☃xxx < 0.0) && !(☃xxx > 1.0) ? new Vec3d(this.x + ☃ * ☃xxx, this.y + ☃x * ☃xxx, this.z + ☃xx * ☃xxx) : null;
      }
   }

   @Nullable
   public Vec3d getIntermediateWithZValue(Vec3d var1, double var2) {
      double ☃ = ☃.x - this.x;
      double ☃x = ☃.y - this.y;
      double ☃xx = ☃.z - this.z;
      if (☃xx * ☃xx < 1.0E-7F) {
         return null;
      } else {
         double ☃xxx = (☃ - this.z) / ☃xx;
         return !(☃xxx < 0.0) && !(☃xxx > 1.0) ? new Vec3d(this.x + ☃ * ☃xxx, this.y + ☃x * ☃xxx, this.z + ☃xx * ☃xxx) : null;
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof Vec3d)) {
         return false;
      } else {
         Vec3d ☃ = (Vec3d)☃;
         if (Double.compare(☃.x, this.x) != 0) {
            return false;
         } else {
            return Double.compare(☃.y, this.y) != 0 ? false : Double.compare(☃.z, this.z) == 0;
         }
      }
   }

   @Override
   public int hashCode() {
      long ☃ = Double.doubleToLongBits(this.x);
      int ☃x = (int)(☃ ^ ☃ >>> 32);
      ☃ = Double.doubleToLongBits(this.y);
      ☃x = 31 * ☃x + (int)(☃ ^ ☃ >>> 32);
      ☃ = Double.doubleToLongBits(this.z);
      return 31 * ☃x + (int)(☃ ^ ☃ >>> 32);
   }

   @Override
   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ")";
   }

   public Vec3d rotatePitch(float var1) {
      float ☃ = MathHelper.cos(☃);
      float ☃x = MathHelper.sin(☃);
      double ☃xx = this.x;
      double ☃xxx = this.y * ☃ + this.z * ☃x;
      double ☃xxxx = this.z * ☃ - this.y * ☃x;
      return new Vec3d(☃xx, ☃xxx, ☃xxxx);
   }

   public Vec3d rotateYaw(float var1) {
      float ☃ = MathHelper.cos(☃);
      float ☃x = MathHelper.sin(☃);
      double ☃xx = this.x * ☃ + this.z * ☃x;
      double ☃xxx = this.y;
      double ☃xxxx = this.z * ☃ - this.x * ☃x;
      return new Vec3d(☃xx, ☃xxx, ☃xxxx);
   }

   public static Vec3d fromPitchYaw(Vec2f var0) {
      return fromPitchYaw(☃.x, ☃.y);
   }

   public static Vec3d fromPitchYaw(float var0, float var1) {
      float ☃ = MathHelper.cos(-☃ * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃x = MathHelper.sin(-☃ * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃xx = -MathHelper.cos(-☃ * (float) (Math.PI / 180.0));
      float ☃xxx = MathHelper.sin(-☃ * (float) (Math.PI / 180.0));
      return new Vec3d(☃x * ☃xx, ☃xxx, ☃ * ☃xx);
   }
}
