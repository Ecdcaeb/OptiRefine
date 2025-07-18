package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTPrimitive {
   private short data;

   public NBTTagShort() {
   }

   public NBTTagShort(short var1) {
      this.data = ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeShort(this.data);
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(80L);
      this.data = ☃.readShort();
   }

   @Override
   public byte getId() {
      return 2;
   }

   @Override
   public String toString() {
      return this.data + "s";
   }

   public NBTTagShort copy() {
      return new NBTTagShort(this.data);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && this.data == ((NBTTagShort)☃).data;
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ this.data;
   }

   @Override
   public long getLong() {
      return this.data;
   }

   @Override
   public int getInt() {
      return this.data;
   }

   @Override
   public short getShort() {
      return this.data;
   }

   @Override
   public byte getByte() {
      return (byte)(this.data & 255);
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
