package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagByteArray extends NBTBase {
   private byte[] data;

   NBTTagByteArray() {
   }

   public NBTTagByteArray(byte[] var1) {
      this.data = ☃;
   }

   public NBTTagByteArray(List<Byte> var1) {
      this(toArray(☃));
   }

   private static byte[] toArray(List<Byte> var0) {
      byte[] ☃ = new byte[☃.size()];

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         Byte ☃xx = ☃.get(☃x);
         ☃[☃x] = ☃xx == null ? 0 : ☃xx;
      }

      return ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeInt(this.data.length);
      ☃.write(this.data);
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(192L);
      int ☃ = ☃.readInt();
      ☃.read(8 * ☃);
      this.data = new byte[☃];
      ☃.readFully(this.data);
   }

   @Override
   public byte getId() {
      return 7;
   }

   @Override
   public String toString() {
      StringBuilder ☃ = new StringBuilder("[B;");

      for (int ☃x = 0; ☃x < this.data.length; ☃x++) {
         if (☃x != 0) {
            ☃.append(',');
         }

         ☃.append(this.data[☃x]).append('B');
      }

      return ☃.append(']').toString();
   }

   @Override
   public NBTBase copy() {
      byte[] ☃ = new byte[this.data.length];
      System.arraycopy(this.data, 0, ☃, 0, this.data.length);
      return new NBTTagByteArray(☃);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && Arrays.equals(this.data, ((NBTTagByteArray)☃).data);
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(this.data);
   }

   public byte[] getByteArray() {
      return this.data;
   }
}
