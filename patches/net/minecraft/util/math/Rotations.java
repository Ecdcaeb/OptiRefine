package net.minecraft.util.math;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

public class Rotations {
   protected final float x;
   protected final float y;
   protected final float z;

   public Rotations(float var1, float var2, float var3) {
      this.x = !Float.isInfinite(☃) && !Float.isNaN(☃) ? ☃ % 360.0F : 0.0F;
      this.y = !Float.isInfinite(☃) && !Float.isNaN(☃) ? ☃ % 360.0F : 0.0F;
      this.z = !Float.isInfinite(☃) && !Float.isNaN(☃) ? ☃ % 360.0F : 0.0F;
   }

   public Rotations(NBTTagList var1) {
      this(☃.getFloatAt(0), ☃.getFloatAt(1), ☃.getFloatAt(2));
   }

   public NBTTagList writeToNBT() {
      NBTTagList ☃ = new NBTTagList();
      ☃.appendTag(new NBTTagFloat(this.x));
      ☃.appendTag(new NBTTagFloat(this.y));
      ☃.appendTag(new NBTTagFloat(this.z));
      return ☃;
   }

   @Override
   public boolean equals(Object var1) {
      if (!(☃ instanceof Rotations)) {
         return false;
      } else {
         Rotations ☃ = (Rotations)☃;
         return this.x == ☃.x && this.y == ☃.y && this.z == ☃.z;
      }
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getZ() {
      return this.z;
   }
}
