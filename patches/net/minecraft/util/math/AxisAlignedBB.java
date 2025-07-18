package net.minecraft.util.math;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;

public class AxisAlignedBB {
   public final double minX;
   public final double minY;
   public final double minZ;
   public final double maxX;
   public final double maxY;
   public final double maxZ;

   public AxisAlignedBB(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.minX = Math.min(☃, ☃);
      this.minY = Math.min(☃, ☃);
      this.minZ = Math.min(☃, ☃);
      this.maxX = Math.max(☃, ☃);
      this.maxY = Math.max(☃, ☃);
      this.maxZ = Math.max(☃, ☃);
   }

   public AxisAlignedBB(BlockPos var1) {
      this(☃.getX(), ☃.getY(), ☃.getZ(), ☃.getX() + 1, ☃.getY() + 1, ☃.getZ() + 1);
   }

   public AxisAlignedBB(BlockPos var1, BlockPos var2) {
      this(☃.getX(), ☃.getY(), ☃.getZ(), ☃.getX(), ☃.getY(), ☃.getZ());
   }

   public AxisAlignedBB(Vec3d var1, Vec3d var2) {
      this(☃.x, ☃.y, ☃.z, ☃.x, ☃.y, ☃.z);
   }

   public AxisAlignedBB setMaxY(double var1) {
      return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, ☃, this.maxZ);
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof AxisAlignedBB)) {
         return false;
      } else {
         AxisAlignedBB ☃ = (AxisAlignedBB)☃;
         if (Double.compare(☃.minX, this.minX) != 0) {
            return false;
         } else if (Double.compare(☃.minY, this.minY) != 0) {
            return false;
         } else if (Double.compare(☃.minZ, this.minZ) != 0) {
            return false;
         } else if (Double.compare(☃.maxX, this.maxX) != 0) {
            return false;
         } else {
            return Double.compare(☃.maxY, this.maxY) != 0 ? false : Double.compare(☃.maxZ, this.maxZ) == 0;
         }
      }
   }

   @Override
   public int hashCode() {
      long ☃ = Double.doubleToLongBits(this.minX);
      int ☃x = (int)(☃ ^ ☃ >>> 32);
      ☃ = Double.doubleToLongBits(this.minY);
      ☃x = 31 * ☃x + (int)(☃ ^ ☃ >>> 32);
      ☃ = Double.doubleToLongBits(this.minZ);
      ☃x = 31 * ☃x + (int)(☃ ^ ☃ >>> 32);
      ☃ = Double.doubleToLongBits(this.maxX);
      ☃x = 31 * ☃x + (int)(☃ ^ ☃ >>> 32);
      ☃ = Double.doubleToLongBits(this.maxY);
      ☃x = 31 * ☃x + (int)(☃ ^ ☃ >>> 32);
      ☃ = Double.doubleToLongBits(this.maxZ);
      return 31 * ☃x + (int)(☃ ^ ☃ >>> 32);
   }

   public AxisAlignedBB contract(double var1, double var3, double var5) {
      double ☃ = this.minX;
      double ☃x = this.minY;
      double ☃xx = this.minZ;
      double ☃xxx = this.maxX;
      double ☃xxxx = this.maxY;
      double ☃xxxxx = this.maxZ;
      if (☃ < 0.0) {
         ☃ -= ☃;
      } else if (☃ > 0.0) {
         ☃xxx -= ☃;
      }

      if (☃ < 0.0) {
         ☃x -= ☃;
      } else if (☃ > 0.0) {
         ☃xxxx -= ☃;
      }

      if (☃ < 0.0) {
         ☃xx -= ☃;
      } else if (☃ > 0.0) {
         ☃xxxxx -= ☃;
      }

      return new AxisAlignedBB(☃, ☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
   }

   public AxisAlignedBB expand(double var1, double var3, double var5) {
      double ☃ = this.minX;
      double ☃x = this.minY;
      double ☃xx = this.minZ;
      double ☃xxx = this.maxX;
      double ☃xxxx = this.maxY;
      double ☃xxxxx = this.maxZ;
      if (☃ < 0.0) {
         ☃ += ☃;
      } else if (☃ > 0.0) {
         ☃xxx += ☃;
      }

      if (☃ < 0.0) {
         ☃x += ☃;
      } else if (☃ > 0.0) {
         ☃xxxx += ☃;
      }

      if (☃ < 0.0) {
         ☃xx += ☃;
      } else if (☃ > 0.0) {
         ☃xxxxx += ☃;
      }

      return new AxisAlignedBB(☃, ☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
   }

   public AxisAlignedBB grow(double var1, double var3, double var5) {
      double ☃ = this.minX - ☃;
      double ☃x = this.minY - ☃;
      double ☃xx = this.minZ - ☃;
      double ☃xxx = this.maxX + ☃;
      double ☃xxxx = this.maxY + ☃;
      double ☃xxxxx = this.maxZ + ☃;
      return new AxisAlignedBB(☃, ☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
   }

   public AxisAlignedBB grow(double var1) {
      return this.grow(☃, ☃, ☃);
   }

   public AxisAlignedBB intersect(AxisAlignedBB var1) {
      double ☃ = Math.max(this.minX, ☃.minX);
      double ☃x = Math.max(this.minY, ☃.minY);
      double ☃xx = Math.max(this.minZ, ☃.minZ);
      double ☃xxx = Math.min(this.maxX, ☃.maxX);
      double ☃xxxx = Math.min(this.maxY, ☃.maxY);
      double ☃xxxxx = Math.min(this.maxZ, ☃.maxZ);
      return new AxisAlignedBB(☃, ☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
   }

   public AxisAlignedBB union(AxisAlignedBB var1) {
      double ☃ = Math.min(this.minX, ☃.minX);
      double ☃x = Math.min(this.minY, ☃.minY);
      double ☃xx = Math.min(this.minZ, ☃.minZ);
      double ☃xxx = Math.max(this.maxX, ☃.maxX);
      double ☃xxxx = Math.max(this.maxY, ☃.maxY);
      double ☃xxxxx = Math.max(this.maxZ, ☃.maxZ);
      return new AxisAlignedBB(☃, ☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
   }

   public AxisAlignedBB offset(double var1, double var3, double var5) {
      return new AxisAlignedBB(this.minX + ☃, this.minY + ☃, this.minZ + ☃, this.maxX + ☃, this.maxY + ☃, this.maxZ + ☃);
   }

   public AxisAlignedBB offset(BlockPos var1) {
      return new AxisAlignedBB(
         this.minX + ☃.getX(), this.minY + ☃.getY(), this.minZ + ☃.getZ(), this.maxX + ☃.getX(), this.maxY + ☃.getY(), this.maxZ + ☃.getZ()
      );
   }

   public AxisAlignedBB offset(Vec3d var1) {
      return this.offset(☃.x, ☃.y, ☃.z);
   }

   public double calculateXOffset(AxisAlignedBB var1, double var2) {
      if (!(☃.maxY <= this.minY) && !(☃.minY >= this.maxY) && !(☃.maxZ <= this.minZ) && !(☃.minZ >= this.maxZ)) {
         if (☃ > 0.0 && ☃.maxX <= this.minX) {
            double ☃ = this.minX - ☃.maxX;
            if (☃ < ☃) {
               ☃ = ☃;
            }
         } else if (☃ < 0.0 && ☃.minX >= this.maxX) {
            double ☃ = this.maxX - ☃.minX;
            if (☃ > ☃) {
               ☃ = ☃;
            }
         }

         return ☃;
      } else {
         return ☃;
      }
   }

   public double calculateYOffset(AxisAlignedBB var1, double var2) {
      if (!(☃.maxX <= this.minX) && !(☃.minX >= this.maxX) && !(☃.maxZ <= this.minZ) && !(☃.minZ >= this.maxZ)) {
         if (☃ > 0.0 && ☃.maxY <= this.minY) {
            double ☃ = this.minY - ☃.maxY;
            if (☃ < ☃) {
               ☃ = ☃;
            }
         } else if (☃ < 0.0 && ☃.minY >= this.maxY) {
            double ☃ = this.maxY - ☃.minY;
            if (☃ > ☃) {
               ☃ = ☃;
            }
         }

         return ☃;
      } else {
         return ☃;
      }
   }

   public double calculateZOffset(AxisAlignedBB var1, double var2) {
      if (!(☃.maxX <= this.minX) && !(☃.minX >= this.maxX) && !(☃.maxY <= this.minY) && !(☃.minY >= this.maxY)) {
         if (☃ > 0.0 && ☃.maxZ <= this.minZ) {
            double ☃ = this.minZ - ☃.maxZ;
            if (☃ < ☃) {
               ☃ = ☃;
            }
         } else if (☃ < 0.0 && ☃.minZ >= this.maxZ) {
            double ☃ = this.maxZ - ☃.minZ;
            if (☃ > ☃) {
               ☃ = ☃;
            }
         }

         return ☃;
      } else {
         return ☃;
      }
   }

   public boolean intersects(AxisAlignedBB var1) {
      return this.intersects(☃.minX, ☃.minY, ☃.minZ, ☃.maxX, ☃.maxY, ☃.maxZ);
   }

   public boolean intersects(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.minX < ☃ && this.maxX > ☃ && this.minY < ☃ && this.maxY > ☃ && this.minZ < ☃ && this.maxZ > ☃;
   }

   public boolean intersects(Vec3d var1, Vec3d var2) {
      return this.intersects(Math.min(☃.x, ☃.x), Math.min(☃.y, ☃.y), Math.min(☃.z, ☃.z), Math.max(☃.x, ☃.x), Math.max(☃.y, ☃.y), Math.max(☃.z, ☃.z));
   }

   public boolean contains(Vec3d var1) {
      if (☃.x <= this.minX || ☃.x >= this.maxX) {
         return false;
      } else {
         return ☃.y <= this.minY || ☃.y >= this.maxY ? false : !(☃.z <= this.minZ) && !(☃.z >= this.maxZ);
      }
   }

   public double getAverageEdgeLength() {
      double ☃ = this.maxX - this.minX;
      double ☃x = this.maxY - this.minY;
      double ☃xx = this.maxZ - this.minZ;
      return (☃ + ☃x + ☃xx) / 3.0;
   }

   public AxisAlignedBB shrink(double var1) {
      return this.grow(-☃);
   }

   @Nullable
   public RayTraceResult calculateIntercept(Vec3d var1, Vec3d var2) {
      Vec3d ☃ = this.collideWithXPlane(this.minX, ☃, ☃);
      EnumFacing ☃x = EnumFacing.WEST;
      Vec3d ☃xx = this.collideWithXPlane(this.maxX, ☃, ☃);
      if (☃xx != null && this.isClosest(☃, ☃, ☃xx)) {
         ☃ = ☃xx;
         ☃x = EnumFacing.EAST;
      }

      ☃xx = this.collideWithYPlane(this.minY, ☃, ☃);
      if (☃xx != null && this.isClosest(☃, ☃, ☃xx)) {
         ☃ = ☃xx;
         ☃x = EnumFacing.DOWN;
      }

      ☃xx = this.collideWithYPlane(this.maxY, ☃, ☃);
      if (☃xx != null && this.isClosest(☃, ☃, ☃xx)) {
         ☃ = ☃xx;
         ☃x = EnumFacing.UP;
      }

      ☃xx = this.collideWithZPlane(this.minZ, ☃, ☃);
      if (☃xx != null && this.isClosest(☃, ☃, ☃xx)) {
         ☃ = ☃xx;
         ☃x = EnumFacing.NORTH;
      }

      ☃xx = this.collideWithZPlane(this.maxZ, ☃, ☃);
      if (☃xx != null && this.isClosest(☃, ☃, ☃xx)) {
         ☃ = ☃xx;
         ☃x = EnumFacing.SOUTH;
      }

      return ☃ == null ? null : new RayTraceResult(☃, ☃x);
   }

   @VisibleForTesting
   boolean isClosest(Vec3d var1, @Nullable Vec3d var2, Vec3d var3) {
      return ☃ == null || ☃.squareDistanceTo(☃) < ☃.squareDistanceTo(☃);
   }

   @Nullable
   @VisibleForTesting
   Vec3d collideWithXPlane(double var1, Vec3d var3, Vec3d var4) {
      Vec3d ☃ = ☃.getIntermediateWithXValue(☃, ☃);
      return ☃ != null && this.intersectsWithYZ(☃) ? ☃ : null;
   }

   @Nullable
   @VisibleForTesting
   Vec3d collideWithYPlane(double var1, Vec3d var3, Vec3d var4) {
      Vec3d ☃ = ☃.getIntermediateWithYValue(☃, ☃);
      return ☃ != null && this.intersectsWithXZ(☃) ? ☃ : null;
   }

   @Nullable
   @VisibleForTesting
   Vec3d collideWithZPlane(double var1, Vec3d var3, Vec3d var4) {
      Vec3d ☃ = ☃.getIntermediateWithZValue(☃, ☃);
      return ☃ != null && this.intersectsWithXY(☃) ? ☃ : null;
   }

   @VisibleForTesting
   public boolean intersectsWithYZ(Vec3d var1) {
      return ☃.y >= this.minY && ☃.y <= this.maxY && ☃.z >= this.minZ && ☃.z <= this.maxZ;
   }

   @VisibleForTesting
   public boolean intersectsWithXZ(Vec3d var1) {
      return ☃.x >= this.minX && ☃.x <= this.maxX && ☃.z >= this.minZ && ☃.z <= this.maxZ;
   }

   @VisibleForTesting
   public boolean intersectsWithXY(Vec3d var1) {
      return ☃.x >= this.minX && ☃.x <= this.maxX && ☃.y >= this.minY && ☃.y <= this.maxY;
   }

   @Override
   public String toString() {
      return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
   }

   public boolean hasNaN() {
      return Double.isNaN(this.minX)
         || Double.isNaN(this.minY)
         || Double.isNaN(this.minZ)
         || Double.isNaN(this.maxX)
         || Double.isNaN(this.maxY)
         || Double.isNaN(this.maxZ);
   }

   public Vec3d getCenter() {
      return new Vec3d(this.minX + (this.maxX - this.minX) * 0.5, this.minY + (this.maxY - this.minY) * 0.5, this.minZ + (this.maxZ - this.minZ) * 0.5);
   }
}
