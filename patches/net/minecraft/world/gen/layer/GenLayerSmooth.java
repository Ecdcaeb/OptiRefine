package net.minecraft.world.gen.layer;

public class GenLayerSmooth extends GenLayer {
   public GenLayerSmooth(long var1, GenLayer var3) {
      super(☃);
      super.parent = ☃;
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
            int ☃xxxxxxxx = ☃xxxx[☃xxxxxxx + 0 + (☃xxxxxx + 1) * ☃xx];
            int ☃xxxxxxxxx = ☃xxxx[☃xxxxxxx + 2 + (☃xxxxxx + 1) * ☃xx];
            int ☃xxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 0) * ☃xx];
            int ☃xxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 2) * ☃xx];
            int ☃xxxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1) * ☃xx];
            if (☃xxxxxxxx == ☃xxxxxxxxx && ☃xxxxxxxxxx == ☃xxxxxxxxxxx) {
               this.initChunkSeed(☃xxxxxxx + ☃, ☃xxxxxx + ☃);
               if (this.nextInt(2) == 0) {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxx;
               } else {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxx;
               }
            } else {
               if (☃xxxxxxxx == ☃xxxxxxxxx) {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxx;
               }

               if (☃xxxxxxxxxx == ☃xxxxxxxxxxx) {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxx;
               }
            }

            ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = ☃xxxxxxxxxxxx;
         }
      }

      return ☃xxxxx;
   }
}
