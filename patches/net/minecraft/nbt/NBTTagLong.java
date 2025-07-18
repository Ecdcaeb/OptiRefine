package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTPrimitive {
   private long data;

   NBTTagLong() {
   }

   public NBTTagLong(long var1) {
      this.data = ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeLong(this.data);
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(128L);
      this.data = ☃.readLong();
   }

   @Override
   public byte getId() {
      return 4;
   }

   @Override
   public String toString() {
      return this.data + "L";
   }

   public NBTTagLong copy() {
      return new NBTTagLong(this.data);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && this.data == ((NBTTagLong)☃).data;
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ (int)(this.data ^ this.data >>> 32);
   }

   @Override
   public long getLong() {
      return this.data;
   }

   @Override
   public int getInt() {
      return (int)(this.data & -1L);
   }

   @Override
   public short getShort() {
      return (short)(this.data & 65535L);
   }

   @Override
   public byte getByte() {
      return (byte)(this.data & 255L);
   }

   @Override
   public double getDouble() {
      return this.data;
   }

   @Override
   public float getFloat() {
      return (float)this.data;
   }
}
