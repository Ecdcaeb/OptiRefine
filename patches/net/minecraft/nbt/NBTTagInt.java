package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTPrimitive {
   private int data;

   NBTTagInt() {
   }

   public NBTTagInt(int var1) {
      this.data = ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeInt(this.data);
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(96L);
      this.data = ☃.readInt();
   }

   @Override
   public byte getId() {
      return 3;
   }

   @Override
   public String toString() {
      return String.valueOf(this.data);
   }

   public NBTTagInt copy() {
      return new NBTTagInt(this.data);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && this.data == ((NBTTagInt)☃).data;
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
      return (short)(this.data & 65535);
   }

   @Override
   public byte getByte() {
      return (byte)(this.data & 0xFF);
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
