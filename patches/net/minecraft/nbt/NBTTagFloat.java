package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.util.math.MathHelper;

public class NBTTagFloat extends NBTPrimitive {
   private float data;

   NBTTagFloat() {
   }

   public NBTTagFloat(float var1) {
      this.data = ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeFloat(this.data);
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(96L);
      this.data = ☃.readFloat();
   }

   @Override
   public byte getId() {
      return 5;
   }

   @Override
   public String toString() {
      return this.data + "f";
   }

   public NBTTagFloat copy() {
      return new NBTTagFloat(this.data);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && this.data == ((NBTTagFloat)☃).data;
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ Float.floatToIntBits(this.data);
   }

   @Override
   public long getLong() {
      return (long)this.data;
   }

   @Override
   public int getInt() {
      return MathHelper.floor(this.data);
   }

   @Override
   public short getShort() {
      return (short)(MathHelper.floor(this.data) & 65535);
   }

   @Override
   public byte getByte() {
      return (byte)(MathHelper.floor(this.data) & 0xFF);
   }

   @Override
   public double getDouble() {
      return this.data;
   }

   @Override
   public float getFloat() {
      return this.data;
   }
}
