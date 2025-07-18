package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer {
   public GenLayerRiverInit(long var1, GenLayer var3) {
      super(☃);
      this.parent = ☃;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int[] ☃ = this.parent.getInts(☃, ☃, ☃, ☃);
      int[] ☃x = IntCache.getIntCache(☃ * ☃);

      for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < ☃; ☃xxx++) {
            this.initChunkSeed(☃xxx + ☃, ☃xx + ☃);
            ☃x[☃xxx + ☃xx * ☃] = ☃[☃xxx + ☃xx * ☃] > 0 ? this.nextInt(299999) + 2 : 0;
         }
      }

      return ☃x;
   }
}
