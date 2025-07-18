package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagIntArray extends NBTBase {
   private int[] intArray;

   NBTTagIntArray() {
   }

   public NBTTagIntArray(int[] var1) {
      this.intArray = ☃;
   }

   public NBTTagIntArray(List<Integer> var1) {
      this(toArray(☃));
   }

   private static int[] toArray(List<Integer> var0) {
      int[] ☃ = new int[☃.size()];

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         Integer ☃xx = ☃.get(☃x);
         ☃[☃x] = ☃xx == null ? 0 : ☃xx;
      }

      return ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeInt(this.intArray.length);

      for (int ☃ : this.intArray) {
         ☃.writeInt(☃);
      }
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(192L);
      int ☃ = ☃.readInt();
      ☃.read(32 * ☃);
      this.intArray = new int[☃];

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         this.intArray[☃x] = ☃.readInt();
      }
   }

   @Override
   public byte getId() {
      return 11;
   }

   @Override
   public String toString() {
      StringBuilder ☃ = new StringBuilder("[I;");

      for (int ☃x = 0; ☃x < this.intArray.length; ☃x++) {
         if (☃x != 0) {
            ☃.append(',');
         }

         ☃.append(this.intArray[☃x]);
      }

      return ☃.append(']').toString();
   }

   public NBTTagIntArray copy() {
      int[] ☃ = new int[this.intArray.length];
      System.arraycopy(this.intArray, 0, ☃, 0, this.intArray.length);
      return new NBTTagIntArray(☃);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && Arrays.equals(this.intArray, ((NBTTagIntArray)☃).intArray);
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(this.intArray);
   }

   public int[] getIntArray() {
      return this.intArray;
   }
}
