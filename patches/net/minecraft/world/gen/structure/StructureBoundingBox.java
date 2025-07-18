package net.minecraft.world.gen.structure;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class StructureBoundingBox {
   public int minX;
   public int minY;
   public int minZ;
   public int maxX;
   public int maxY;
   public int maxZ;

   public StructureBoundingBox() {
   }

   public StructureBoundingBox(int[] var1) {
      if (☃.length == 6) {
         this.minX = ☃[0];
         this.minY = ☃[1];
         this.minZ = ☃[2];
         this.maxX = ☃[3];
         this.maxY = ☃[4];
         this.maxZ = ☃[5];
      }
   }

   public static StructureBoundingBox getNewBoundingBox() {
      return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
   }

   public static StructureBoundingBox getComponentToAddBoundingBox(
      int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, EnumFacing var9
   ) {
      switch (☃) {
         case NORTH:
            return new StructureBoundingBox(☃ + ☃, ☃ + ☃, ☃ - ☃ + 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃);
         case SOUTH:
            return new StructureBoundingBox(☃ + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃);
         case WEST:
            return new StructureBoundingBox(☃ - ☃ + 1 + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃);
         case EAST:
            return new StructureBoundingBox(☃ + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃);
         default:
            return new StructureBoundingBox(☃ + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃);
      }
   }

   public static StructureBoundingBox createProper(int var0, int var1, int var2, int var3, int var4, int var5) {
      return new StructureBoundingBox(Math.min(☃, ☃), Math.min(☃, ☃), Math.min(☃, ☃), Math.max(☃, ☃), Math.max(☃, ☃), Math.max(☃, ☃));
   }

   public StructureBoundingBox(StructureBoundingBox var1) {
      this.minX = ☃.minX;
      this.minY = ☃.minY;
      this.minZ = ☃.minZ;
      this.maxX = ☃.maxX;
      this.maxY = ☃.maxY;
      this.maxZ = ☃.maxZ;
   }

   public StructureBoundingBox(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.minX = ☃;
      this.minY = ☃;
      this.minZ = ☃;
      this.maxX = ☃;
      this.maxY = ☃;
      this.maxZ = ☃;
   }

   public StructureBoundingBox(Vec3i var1, Vec3i var2) {
      this.minX = Math.min(☃.getX(), ☃.getX());
      this.minY = Math.min(☃.getY(), ☃.getY());
      this.minZ = Math.min(☃.getZ(), ☃.getZ());
      this.maxX = Math.max(☃.getX(), ☃.getX());
      this.maxY = Math.max(☃.getY(), ☃.getY());
      this.maxZ = Math.max(☃.getZ(), ☃.getZ());
   }

   public StructureBoundingBox(int var1, int var2, int var3, int var4) {
      this.minX = ☃;
      this.minZ = ☃;
      this.maxX = ☃;
      this.maxZ = ☃;
      this.minY = 1;
      this.maxY = 512;
   }

   public boolean intersectsWith(StructureBoundingBox var1) {
      return this.maxX >= ☃.minX && this.minX <= ☃.maxX && this.maxZ >= ☃.minZ && this.minZ <= ☃.maxZ && this.maxY >= ☃.minY && this.minY <= ☃.maxY;
   }

   public boolean intersectsWith(int var1, int var2, int var3, int var4) {
      return this.maxX >= ☃ && this.minX <= ☃ && this.maxZ >= ☃ && this.minZ <= ☃;
   }

   public void expandTo(StructureBoundingBox var1) {
      this.minX = Math.min(this.minX, ☃.minX);
      this.minY = Math.min(this.minY, ☃.minY);
      this.minZ = Math.min(this.minZ, ☃.minZ);
      this.maxX = Math.max(this.maxX, ☃.maxX);
      this.maxY = Math.max(this.maxY, ☃.maxY);
      this.maxZ = Math.max(this.maxZ, ☃.maxZ);
   }

   public void offset(int var1, int var2, int var3) {
      this.minX += ☃;
      this.minY += ☃;
      this.minZ += ☃;
      this.maxX += ☃;
      this.maxY += ☃;
      this.maxZ += ☃;
   }

   public boolean isVecInside(Vec3i var1) {
      return ☃.getX() >= this.minX && ☃.getX() <= this.maxX && ☃.getZ() >= this.minZ && ☃.getZ() <= this.maxZ && ☃.getY() >= this.minY && ☃.getY() <= this.maxY;
   }

   public Vec3i getLength() {
      return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
   }

   public int getXSize() {
      return this.maxX - this.minX + 1;
   }

   public int getYSize() {
      return this.maxY - this.minY + 1;
   }

   public int getZSize() {
      return this.maxZ - this.minZ + 1;
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
         .add("x0", this.minX)
         .add("y0", this.minY)
         .add("z0", this.minZ)
         .add("x1", this.maxX)
         .add("y1", this.maxY)
         .add("z1", this.maxZ)
         .toString();
   }

   public NBTTagIntArray toNBTTagIntArray() {
      return new NBTTagIntArray(new int[]{this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ});
   }
}
