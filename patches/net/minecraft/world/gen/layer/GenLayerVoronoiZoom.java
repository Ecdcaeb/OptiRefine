package net.minecraft.world.gen.layer;

public class GenLayerVoronoiZoom extends GenLayer {
   public GenLayerVoronoiZoom(long var1, GenLayer var3) {
      super(☃);
      super.parent = ☃;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      ☃ -= 2;
      ☃ -= 2;
      int ☃ = ☃ >> 2;
      int ☃x = ☃ >> 2;
      int ☃xx = (☃ >> 2) + 2;
      int ☃xxx = (☃ >> 2) + 2;
      int[] ☃xxxx = this.parent.getInts(☃, ☃x, ☃xx, ☃xxx);
      int ☃xxxxx = ☃xx - 1 << 2;
      int ☃xxxxxx = ☃xxx - 1 << 2;
      int[] ☃xxxxxxx = IntCache.getIntCache(☃xxxxx * ☃xxxxxx);

      for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xxx - 1; ☃xxxxxxxx++) {
         int ☃xxxxxxxxx = 0;
         int ☃xxxxxxxxxx = ☃xxxx[☃xxxxxxxxx + 0 + (☃xxxxxxxx + 0) * ☃xx];

         for (int ☃xxxxxxxxxxx = ☃xxxx[☃xxxxxxxxx + 0 + (☃xxxxxxxx + 1) * ☃xx]; ☃xxxxxxxxx < ☃xx - 1; ☃xxxxxxxxx++) {
            double ☃xxxxxxxxxxxx = 3.6;
            this.initChunkSeed(☃xxxxxxxxx + ☃ << 2, ☃xxxxxxxx + ☃x << 2);
            double ☃xxxxxxxxxxxxx = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
            double ☃xxxxxxxxxxxxxx = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
            this.initChunkSeed(☃xxxxxxxxx + ☃ + 1 << 2, ☃xxxxxxxx + ☃x << 2);
            double ☃xxxxxxxxxxxxxxx = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
            double ☃xxxxxxxxxxxxxxxx = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
            this.initChunkSeed(☃xxxxxxxxx + ☃ << 2, ☃xxxxxxxx + ☃x + 1 << 2);
            double ☃xxxxxxxxxxxxxxxxx = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
            double ☃xxxxxxxxxxxxxxxxxx = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
            this.initChunkSeed(☃xxxxxxxxx + ☃ + 1 << 2, ☃xxxxxxxx + ☃x + 1 << 2);
            double ☃xxxxxxxxxxxxxxxxxxx = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
            double ☃xxxxxxxxxxxxxxxxxxxx = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
            int ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxx[☃xxxxxxxxx + 1 + (☃xxxxxxxx + 0) * ☃xx] & 0xFF;
            int ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxx[☃xxxxxxxxx + 1 + (☃xxxxxxxx + 1) * ☃xx] & 0xFF;

            for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
               int ☃xxxxxxxxxxxxxxxxxxxxxxxx = ((☃xxxxxxxx << 2) + ☃xxxxxxxxxxxxxxxxxxxxxxx) * ☃xxxxx + (☃xxxxxxxxx << 2);

               for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxxxx++) {
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxx) * (☃xxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxx)
                     + (☃xxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxx) * (☃xxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxx);
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxx) * (☃xxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxx)
                     + (☃xxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxx) * (☃xxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxx);
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxx) * (☃xxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxx)
                     + (☃xxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxx) * (☃xxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxx);
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxx)
                        * (☃xxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxx)
                     + (☃xxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx) * (☃xxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx);
                  if (☃xxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
                     && ☃xxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                     && ☃xxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx) {
                     ☃xxxxxxx[☃xxxxxxxxxxxxxxxxxxxxxxxx++] = ☃xxxxxxxxxx;
                  } else if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxx
                     && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                     && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx) {
                     ☃xxxxxxx[☃xxxxxxxxxxxxxxxxxxxxxxxx++] = ☃xxxxxxxxxxxxxxxxxxxxx;
                  } else if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxx
                     && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
                     && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx) {
                     ☃xxxxxxx[☃xxxxxxxxxxxxxxxxxxxxxxxx++] = ☃xxxxxxxxxxx;
                  } else {
                     ☃xxxxxxx[☃xxxxxxxxxxxxxxxxxxxxxxxx++] = ☃xxxxxxxxxxxxxxxxxxxxxx;
                  }
               }
            }

            ☃xxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx;
            ☃xxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx;
         }
      }

      int[] ☃xxxxxxxx = IntCache.getIntCache(☃ * ☃);

      for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃; ☃xxxxxxxxx++) {
         System.arraycopy(☃xxxxxxx, (☃xxxxxxxxx + (☃ & 3)) * ☃xxxxx + (☃ & 3), ☃xxxxxxxx, ☃xxxxxxxxx * ☃, ☃);
      }

      return ☃xxxxxxxx;
   }
}
