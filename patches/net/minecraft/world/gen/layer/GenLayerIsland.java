package net.minecraft.world.gen.layer;

public class GenLayerIsland extends GenLayer {
   public GenLayerIsland(long var1) {
      super(☃);
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int[] ☃ = IntCache.getIntCache(☃ * ☃);

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
            this.initChunkSeed(☃ + ☃xx, ☃ + ☃x);
            ☃[☃xx + ☃x * ☃] = this.nextInt(10) == 0 ? 1 : 0;
         }
      }

      if (☃ > -☃ && ☃ <= 0 && ☃ > -☃ && ☃ <= 0) {
         ☃[-☃ + -☃ * ☃] = 1;
      }

      return ☃;
   }
}
