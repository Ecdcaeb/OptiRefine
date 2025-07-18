package net.minecraft.world.chunk;

public class NibbleArray {
   private final byte[] data;

   public NibbleArray() {
      this.data = new byte[2048];
   }

   public NibbleArray(byte[] var1) {
      this.data = ☃;
      if (☃.length != 2048) {
         throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + ☃.length);
      }
   }

   public int get(int var1, int var2, int var3) {
      return this.getFromIndex(this.getCoordinateIndex(☃, ☃, ☃));
   }

   public void set(int var1, int var2, int var3, int var4) {
      this.setIndex(this.getCoordinateIndex(☃, ☃, ☃), ☃);
   }

   private int getCoordinateIndex(int var1, int var2, int var3) {
      return ☃ << 8 | ☃ << 4 | ☃;
   }

   public int getFromIndex(int var1) {
      int ☃ = this.getNibbleIndex(☃);
      return this.isLowerNibble(☃) ? this.data[☃] & 15 : this.data[☃] >> 4 & 15;
   }

   public void setIndex(int var1, int var2) {
      int ☃ = this.getNibbleIndex(☃);
      if (this.isLowerNibble(☃)) {
         this.data[☃] = (byte)(this.data[☃] & 240 | ☃ & 15);
      } else {
         this.data[☃] = (byte)(this.data[☃] & 15 | (☃ & 15) << 4);
      }
   }

   private boolean isLowerNibble(int var1) {
      return (☃ & 1) == 0;
   }

   private int getNibbleIndex(int var1) {
      return ☃ >> 1;
   }

   public byte[] getData() {
      return this.data;
   }
}
