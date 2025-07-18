package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.util.math.MathHelper;

public class NBTTagDouble extends NBTPrimitive {
   private double data;

   NBTTagDouble() {
   }

   public NBTTagDouble(double var1) {
      this.data = ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeDouble(this.data);
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(128L);
      this.data = ☃.readDouble();
   }

   @Override
   public byte getId() {
      return 6;
   }

   @Override
   public String toString() {
      return this.data + "d";
   }

   public NBTTagDouble copy() {
      return new NBTTagDouble(this.data);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && this.data == ((NBTTagDouble)☃).data;
   }

   @Override
   public int hashCode() {
      long ☃ = Double.doubleToLongBits(this.data);
      return super.hashCode() ^ (int)(☃ ^ ☃ >>> 32);
   }

   @Override
   public long getLong() {
      return (long)Math.floor(this.data);
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
      return (float)this.data;
   }
}
