package net.minecraft.world.gen.layer;

public class GenLayerAddIsland extends GenLayer {
   public GenLayerAddIsland(long var1, GenLayer var3) {
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
            int ☃xxxxxxxx = ☃xxxx[☃xxxxxxx + 0 + (☃xxxxxx + 0) * ☃xx];
            int ☃xxxxxxxxx = ☃xxxx[☃xxxxxxx + 2 + (☃xxxxxx + 0) * ☃xx];
            int ☃xxxxxxxxxx = ☃xxxx[☃xxxxxxx + 0 + (☃xxxxxx + 2) * ☃xx];
            int ☃xxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 2 + (☃xxxxxx + 2) * ☃xx];
            int ☃xxxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1) * ☃xx];
            this.initChunkSeed(☃xxxxxxx + ☃, ☃xxxxxx + ☃);
            if (☃xxxxxxxxxxxx != 0 || ☃xxxxxxxx == 0 && ☃xxxxxxxxx == 0 && ☃xxxxxxxxxx == 0 && ☃xxxxxxxxxxx == 0) {
               if (☃xxxxxxxxxxxx > 0 && (☃xxxxxxxx == 0 || ☃xxxxxxxxx == 0 || ☃xxxxxxxxxx == 0 || ☃xxxxxxxxxxx == 0)) {
                  if (this.nextInt(5) == 0) {
                     if (☃xxxxxxxxxxxx == 4) {
                        ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = 4;
                     } else {
                        ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = 0;
                     }
                  } else {
                     ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = ☃xxxxxxxxxxxx;
                  }
               } else {
                  ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = ☃xxxxxxxxxxxx;
               }
            } else {
               int ☃xxxxxxxxxxxxx = 1;
               int ☃xxxxxxxxxxxxxx = 1;
               if (☃xxxxxxxx != 0 && this.nextInt(☃xxxxxxxxxxxxx++) == 0) {
                  ☃xxxxxxxxxxxxxx = ☃xxxxxxxx;
               }

               if (☃xxxxxxxxx != 0 && this.nextInt(☃xxxxxxxxxxxxx++) == 0) {
                  ☃xxxxxxxxxxxxxx = ☃xxxxxxxxx;
               }

               if (☃xxxxxxxxxx != 0 && this.nextInt(☃xxxxxxxxxxxxx++) == 0) {
                  ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxx;
               }

               if (☃xxxxxxxxxxx != 0 && this.nextInt(☃xxxxxxxxxxxxx++) == 0) {
                  ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxx;
               }

               if (this.nextInt(3) == 0) {
                  ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = ☃xxxxxxxxxxxxxx;
               } else if (☃xxxxxxxxxxxxxx == 4) {
                  ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = 4;
               } else {
                  ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = 0;
               }
            }
         }
      }

      return ☃xxxxx;
   }
}
