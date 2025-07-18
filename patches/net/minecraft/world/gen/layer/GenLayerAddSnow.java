package net.minecraft.world.gen.layer;

public class GenLayerAddSnow extends GenLayer {
   public GenLayerAddSnow(long var1, GenLayer var3) {
      super(☃);
      this.parent = ☃;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int ☃ = ☃ - 1;
      int ☃x = ☃ - 1;
      int ☃xx = ☃ + 2;
      int ☃xxx = ☃ + 2;
      int[] ☃xxxx = this.parent.getInts(☃, ☃x, ☃xx, ☃xxx);
      int[] ☃xxxxx = IntCache.getIntCache(☃ * ☃);

      for (int ☃xxxxxx = 0; ☃xxxxxx < ☃; ☃xxxxxx++) {
         for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃; ☃xxxxxxx++) {
            int ☃xxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1) * ☃xx];
            this.initChunkSeed(☃xxxxxxx + ☃, ☃xxxxxx + ☃);
            if (☃xxxxxxxx == 0) {
               ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = 0;
            } else {
               int ☃xxxxxxxxx = this.nextInt(6);
               byte var15;
               if (☃xxxxxxxxx == 0) {
                  var15 = 4;
               } else if (☃xxxxxxxxx <= 1) {
                  var15 = 3;
               } else {
                  var15 = 1;
               }

               ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = var15;
            }
         }
      }

      return ☃xxxxx;
   }
}
