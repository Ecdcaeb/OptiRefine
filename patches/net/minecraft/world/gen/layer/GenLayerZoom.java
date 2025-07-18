package net.minecraft.world.gen.layer;

public class GenLayerZoom extends GenLayer {
   public GenLayerZoom(long var1, GenLayer var3) {
      super(☃);
      super.parent = ☃;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int ☃ = ☃ >> 1;
      int ☃x = ☃ >> 1;
      int ☃xx = (☃ >> 1) + 2;
      int ☃xxx = (☃ >> 1) + 2;
      int[] ☃xxxx = this.parent.getInts(☃, ☃x, ☃xx, ☃xxx);
      int ☃xxxxx = ☃xx - 1 << 1;
      int ☃xxxxxx = ☃xxx - 1 << 1;
      int[] ☃xxxxxxx = IntCache.getIntCache(☃xxxxx * ☃xxxxxx);

      for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xxx - 1; ☃xxxxxxxx++) {
         int ☃xxxxxxxxx = (☃xxxxxxxx << 1) * ☃xxxxx;
         int ☃xxxxxxxxxx = 0;
         int ☃xxxxxxxxxxx = ☃xxxx[☃xxxxxxxxxx + 0 + (☃xxxxxxxx + 0) * ☃xx];

         for (int ☃xxxxxxxxxxxx = ☃xxxx[☃xxxxxxxxxx + 0 + (☃xxxxxxxx + 1) * ☃xx]; ☃xxxxxxxxxx < ☃xx - 1; ☃xxxxxxxxxx++) {
            this.initChunkSeed(☃xxxxxxxxxx + ☃ << 1, ☃xxxxxxxx + ☃x << 1);
            int ☃xxxxxxxxxxxxx = ☃xxxx[☃xxxxxxxxxx + 1 + (☃xxxxxxxx + 0) * ☃xx];
            int ☃xxxxxxxxxxxxxx = ☃xxxx[☃xxxxxxxxxx + 1 + (☃xxxxxxxx + 1) * ☃xx];
            ☃xxxxxxx[☃xxxxxxxxx] = ☃xxxxxxxxxxx;
            ☃xxxxxxx[☃xxxxxxxxx++ + ☃xxxxx] = this.selectRandom(new int[]{☃xxxxxxxxxxx, ☃xxxxxxxxxxxx});
            ☃xxxxxxx[☃xxxxxxxxx] = this.selectRandom(new int[]{☃xxxxxxxxxxx, ☃xxxxxxxxxxxxx});
            ☃xxxxxxx[☃xxxxxxxxx++ + ☃xxxxx] = this.selectModeOrRandom(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxxx);
            ☃xxxxxxxxxxx = ☃xxxxxxxxxxxxx;
            ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxxxx;
         }
      }

      int[] ☃xxxxxxxx = IntCache.getIntCache(☃ * ☃);

      for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃; ☃xxxxxxxxx++) {
         System.arraycopy(☃xxxxxxx, (☃xxxxxxxxx + (☃ & 1)) * ☃xxxxx + (☃ & 1), ☃xxxxxxxx, ☃xxxxxxxxx * ☃, ☃);
      }

      return ☃xxxxxxxx;
   }

   public static GenLayer magnify(long var0, GenLayer var2, int var3) {
      GenLayer ☃ = ☃;

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         ☃ = new GenLayerZoom(☃ + ☃x, ☃);
      }

      return ☃;
   }
}
