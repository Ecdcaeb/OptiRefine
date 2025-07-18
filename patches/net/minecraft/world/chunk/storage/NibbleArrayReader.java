package net.minecraft.world.chunk.storage;

public class NibbleArrayReader {
   public final byte[] data;
   private final int depthBits;
   private final int depthBitsPlusFour;

   public NibbleArrayReader(byte[] var1, int var2) {
      this.data = ☃;
      this.depthBits = ☃;
      this.depthBitsPlusFour = ☃ + 4;
   }

   public int get(int var1, int var2, int var3) {
      int ☃ = ☃ << this.depthBitsPlusFour | ☃ << this.depthBits | ☃;
      int ☃x = ☃ >> 1;
      int ☃xx = ☃ & 1;
      return ☃xx == 0 ? this.data[☃x] & 15 : this.data[☃x] >> 4 & 15;
   }
}
