package net.minecraft.util.math;

import com.google.common.base.MoreObjects;
import javax.annotation.concurrent.Immutable;

@Immutable
public class Vec3i implements Comparable<Vec3i> {
   public static final Vec3i NULL_VECTOR = new Vec3i(0, 0, 0);
   private final int x;
   private final int y;
   private final int z;

   public Vec3i(int var1, int var2, int var3) {
      this.x = ☃;
      this.y = ☃;
      this.z = ☃;
   }

   public Vec3i(double var1, double var3, double var5) {
      this(MathHelper.floor(☃), MathHelper.floor(☃), MathHelper.floor(☃));
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof Vec3i)) {
         return false;
      } else {
         Vec3i ☃ = (Vec3i)☃;
         if (this.getX() != ☃.getX()) {
            return false;
         } else {
            return this.getY() != ☃.getY() ? false : this.getZ() == ☃.getZ();
         }
      }
   }

   @Override
   public int hashCode() {
      return (this.getY() + this.getZ() * 31) * 31 + this.getX();
   }

   public int compareTo(Vec3i var1) {
      if (this.getY() == ☃.getY()) {
         return this.getZ() == ☃.getZ() ? this.getX() - ☃.getX() : this.getZ() - ☃.getZ();
      } else {
         return this.getY() - ☃.getY();
      }
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public Vec3i crossProduct(Vec3i var1) {
      return new Vec3i(
         this.getY() * ☃.getZ() - this.getZ() * ☃.getY(), this.getZ() * ☃.getX() - this.getX() * ☃.getZ(), this.getX() * ☃.getY() - this.getY() * ☃.getX()
      );
   }

   public double getDistance(int var1, int var2, int var3) {
      double ☃ = this.getX() - ☃;
      double ☃x = this.getY() - ☃;
      double ☃xx = this.getZ() - ☃;
      return Math.sqrt(☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx);
   }

   public double distanceSq(double var1, double var3, double var5) {
      double ☃ = this.getX() - ☃;
      double ☃x = this.getY() - ☃;
      double ☃xx = this.getZ() - ☃;
      return ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
   }

   public double distanceSqToCenter(double var1, double var3, double var5) {
      double ☃ = this.getX() + 0.5 - ☃;
      double ☃x = this.getY() + 0.5 - ☃;
      double ☃xx = this.getZ() + 0.5 - ☃;
      return ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
   }

   public double distanceSq(Vec3i var1) {
      return this.distanceSq(☃.getX(), ☃.getY(), ☃.getZ());
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
   }
}
