package net.minecraft.util.math;

import net.minecraft.entity.Entity;

public class ChunkPos {
   public final int x;
   public final int z;

   public ChunkPos(int var1, int var2) {
      this.x = ☃;
      this.z = ☃;
   }

   public ChunkPos(BlockPos var1) {
      this.x = ☃.getX() >> 4;
      this.z = ☃.getZ() >> 4;
   }

   public static long asLong(int var0, int var1) {
      return ☃ & 4294967295L | (☃ & 4294967295L) << 32;
   }

   @Override
   public int hashCode() {
      int ☃ = 1664525 * this.x + 1013904223;
      int ☃x = 1664525 * (this.z ^ -559038737) + 1013904223;
      return ☃ ^ ☃x;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof ChunkPos)) {
         return false;
      } else {
         ChunkPos ☃ = (ChunkPos)☃;
         return this.x == ☃.x && this.z == ☃.z;
      }
   }

   public double getDistanceSq(Entity var1) {
      double ☃ = this.x * 16 + 8;
      double ☃x = this.z * 16 + 8;
      double ☃xx = ☃ - ☃.posX;
      double ☃xxx = ☃x - ☃.posZ;
      return ☃xx * ☃xx + ☃xxx * ☃xxx;
   }

   public int getXStart() {
      return this.x << 4;
   }

   public int getZStart() {
      return this.z << 4;
   }

   public int getXEnd() {
      return (this.x << 4) + 15;
   }

   public int getZEnd() {
      return (this.z << 4) + 15;
   }

   public BlockPos getBlock(int var1, int var2, int var3) {
      return new BlockPos((this.x << 4) + ☃, ☃, (this.z << 4) + ☃);
   }

   @Override
   public String toString() {
      return "[" + this.x + ", " + this.z + "]";
   }
}
