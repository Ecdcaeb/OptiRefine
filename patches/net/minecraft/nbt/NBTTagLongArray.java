package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagLongArray extends NBTBase {
   private long[] data;

   NBTTagLongArray() {
   }

   public NBTTagLongArray(long[] var1) {
      this.data = ☃;
   }

   public NBTTagLongArray(List<Long> var1) {
      this(toArray(☃));
   }

   private static long[] toArray(List<Long> var0) {
      long[] ☃ = new long[☃.size()];

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         Long ☃xx = ☃.get(☃x);
         ☃[☃x] = ☃xx == null ? 0L : ☃xx;
      }

      return ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeInt(this.data.length);

      for (long ☃ : this.data) {
         ☃.writeLong(☃);
      }
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(192L);
      int ☃ = ☃.readInt();
      ☃.read(64 * ☃);
      this.data = new long[☃];

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         this.data[☃x] = ☃.readLong();
      }
   }

   @Override
   public byte getId() {
      return 12;
   }

   @Override
   public String toString() {
      StringBuilder ☃ = new StringBuilder("[L;");

      for (int ☃x = 0; ☃x < this.data.length; ☃x++) {
         if (☃x != 0) {
            ☃.append(',');
         }

         ☃.append(this.data[☃x]).append('L');
      }

      return ☃.append(']').toString();
   }

   public NBTTagLongArray copy() {
      long[] ☃ = new long[this.data.length];
      System.arraycopy(this.data, 0, ☃, 0, this.data.length);
      return new NBTTagLongArray(☃);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && Arrays.equals(this.data, ((NBTTagLongArray)☃).data);
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(this.data);
   }
}
