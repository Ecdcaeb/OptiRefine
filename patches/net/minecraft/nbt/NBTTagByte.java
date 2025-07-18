package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTPrimitive {
   private byte data;

   NBTTagByte() {
   }

   public NBTTagByte(byte var1) {
      this.data = ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeByte(this.data);
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(72L);
      this.data = ☃.readByte();
   }

   @Override
   public byte getId() {
      return 1;
   }

   @Override
   public String toString() {
      return this.data + "b";
   }

   public NBTTagByte copy() {
      return new NBTTagByte(this.data);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && this.data == ((NBTTagByte)☃).data;
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
      return this.data;
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
