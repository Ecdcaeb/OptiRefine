package net.minecraft.util;

import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.Validate;

public class BitArray {
   private final long[] longArray;
   private final int bitsPerEntry;
   private final long maxEntryValue;
   private final int arraySize;

   public BitArray(int var1, int var2) {
      Validate.inclusiveBetween(1L, 32L, ☃);
      this.arraySize = ☃;
      this.bitsPerEntry = ☃;
      this.maxEntryValue = (1L << ☃) - 1L;
      this.longArray = new long[MathHelper.roundUp(☃ * ☃, 64) / 64];
   }

   public void setAt(int var1, int var2) {
      Validate.inclusiveBetween(0L, this.arraySize - 1, ☃);
      Validate.inclusiveBetween(0L, this.maxEntryValue, ☃);
      int ☃ = ☃ * this.bitsPerEntry;
      int ☃x = ☃ / 64;
      int ☃xx = ((☃ + 1) * this.bitsPerEntry - 1) / 64;
      int ☃xxx = ☃ % 64;
      this.longArray[☃x] = this.longArray[☃x] & ~(this.maxEntryValue << ☃xxx) | (☃ & this.maxEntryValue) << ☃xxx;
      if (☃x != ☃xx) {
         int ☃xxxx = 64 - ☃xxx;
         int ☃xxxxx = this.bitsPerEntry - ☃xxxx;
         this.longArray[☃xx] = this.longArray[☃xx] >>> ☃xxxxx << ☃xxxxx | (☃ & this.maxEntryValue) >> ☃xxxx;
      }
   }

   public int getAt(int var1) {
      Validate.inclusiveBetween(0L, this.arraySize - 1, ☃);
      int ☃ = ☃ * this.bitsPerEntry;
      int ☃x = ☃ / 64;
      int ☃xx = ((☃ + 1) * this.bitsPerEntry - 1) / 64;
      int ☃xxx = ☃ % 64;
      if (☃x == ☃xx) {
         return (int)(this.longArray[☃x] >>> ☃xxx & this.maxEntryValue);
      } else {
         int ☃xxxx = 64 - ☃xxx;
         return (int)((this.longArray[☃x] >>> ☃xxx | this.longArray[☃xx] << ☃xxxx) & this.maxEntryValue);
      }
   }

   public long[] getBackingLongArray() {
      return this.longArray;
   }

   public int size() {
      return this.arraySize;
   }
}
