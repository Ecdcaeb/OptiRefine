package net.minecraft.world.gen.layer;

public class GenLayerRemoveTooMuchOcean extends GenLayer {
   public GenLayerRemoveTooMuchOcean(long var1, GenLayer var3) {
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
            int ☃xxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1 - 1) * (☃ + 2)];
            int ☃xxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + 1 + (☃xxxxxx + 1) * (☃ + 2)];
            int ☃xxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 - 1 + (☃xxxxxx + 1) * (☃ + 2)];
            int ☃xxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1 + 1) * (☃ + 2)];
            int ☃xxxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1) * ☃xx];
            ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = ☃xxxxxxxxxxxx;
            this.initChunkSeed(☃xxxxxxx + ☃, ☃xxxxxx + ☃);
            if (☃xxxxxxxxxxxx == 0 && ☃xxxxxxxx == 0 && ☃xxxxxxxxx == 0 && ☃xxxxxxxxxx == 0 && ☃xxxxxxxxxxx == 0 && this.nextInt(2) == 0) {
               ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = 1;
            }
         }
      }

      return ☃xxxxx;
   }
}
