package net.minecraft.world.gen.layer;

public class GenLayerEdge extends GenLayer {
   private final GenLayerEdge.Mode mode;

   public GenLayerEdge(long var1, GenLayer var3, GenLayerEdge.Mode var4) {
      super(☃);
      this.parent = ☃;
      this.mode = ☃;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      switch (this.mode) {
         case COOL_WARM:
         default:
            return this.getIntsCoolWarm(☃, ☃, ☃, ☃);
         case HEAT_ICE:
            return this.getIntsHeatIce(☃, ☃, ☃, ☃);
         case SPECIAL:
            return this.getIntsSpecial(☃, ☃, ☃, ☃);
      }
   }

   private int[] getIntsCoolWarm(int var1, int var2, int var3, int var4) {
      int ☃ = ☃ - 1;
      int ☃x = ☃ - 1;
      int ☃xx = 1 + ☃ + 1;
      int ☃xxx = 1 + ☃ + 1;
      int[] ☃xxxx = this.parent.getInts(☃, ☃x, ☃xx, ☃xxx);
      int[] ☃xxxxx = IntCache.getIntCache(☃ * ☃);

      for (int ☃xxxxxx = 0; ☃xxxxxx < ☃; ☃xxxxxx++) {
         for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃; ☃xxxxxxx++) {
            this.initChunkSeed(☃xxxxxxx + ☃, ☃xxxxxx + ☃);
            int ☃xxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1) * ☃xx];
            if (☃xxxxxxxx == 1) {
               int ☃xxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1 - 1) * ☃xx];
               int ☃xxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + 1 + (☃xxxxxx + 1) * ☃xx];
               int ☃xxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 - 1 + (☃xxxxxx + 1) * ☃xx];
               int ☃xxxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1 + 1) * ☃xx];
               boolean ☃xxxxxxxxxxxxx = ☃xxxxxxxxx == 3 || ☃xxxxxxxxxx == 3 || ☃xxxxxxxxxxx == 3 || ☃xxxxxxxxxxxx == 3;
               boolean ☃xxxxxxxxxxxxxx = ☃xxxxxxxxx == 4 || ☃xxxxxxxxxx == 4 || ☃xxxxxxxxxxx == 4 || ☃xxxxxxxxxxxx == 4;
               if (☃xxxxxxxxxxxxx || ☃xxxxxxxxxxxxxx) {
                  ☃xxxxxxxx = 2;
               }
            }

            ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = ☃xxxxxxxx;
         }
      }

      return ☃xxxxx;
   }

   private int[] getIntsHeatIce(int var1, int var2, int var3, int var4) {
      int ☃ = ☃ - 1;
      int ☃x = ☃ - 1;
      int ☃xx = 1 + ☃ + 1;
      int ☃xxx = 1 + ☃ + 1;
      int[] ☃xxxx = this.parent.getInts(☃, ☃x, ☃xx, ☃xxx);
      int[] ☃xxxxx = IntCache.getIntCache(☃ * ☃);

      for (int ☃xxxxxx = 0; ☃xxxxxx < ☃; ☃xxxxxx++) {
         for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃; ☃xxxxxxx++) {
            int ☃xxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1) * ☃xx];
            if (☃xxxxxxxx == 4) {
               int ☃xxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1 - 1) * ☃xx];
               int ☃xxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + 1 + (☃xxxxxx + 1) * ☃xx];
               int ☃xxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 - 1 + (☃xxxxxx + 1) * ☃xx];
               int ☃xxxxxxxxxxxx = ☃xxxx[☃xxxxxxx + 1 + (☃xxxxxx + 1 + 1) * ☃xx];
               boolean ☃xxxxxxxxxxxxx = ☃xxxxxxxxx == 2 || ☃xxxxxxxxxx == 2 || ☃xxxxxxxxxxx == 2 || ☃xxxxxxxxxxxx == 2;
               boolean ☃xxxxxxxxxxxxxx = ☃xxxxxxxxx == 1 || ☃xxxxxxxxxx == 1 || ☃xxxxxxxxxxx == 1 || ☃xxxxxxxxxxxx == 1;
               if (☃xxxxxxxxxxxxxx || ☃xxxxxxxxxxxxx) {
                  ☃xxxxxxxx = 3;
               }
            }

            ☃xxxxx[☃xxxxxxx + ☃xxxxxx * ☃] = ☃xxxxxxxx;
         }
      }

      return ☃xxxxx;
   }

   private int[] getIntsSpecial(int var1, int var2, int var3, int var4) {
      int[] ☃ = this.parent.getInts(☃, ☃, ☃, ☃);
      int[] ☃x = IntCache.getIntCache(☃ * ☃);

      for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < ☃; ☃xxx++) {
            this.initChunkSeed(☃xxx + ☃, ☃xx + ☃);
            int ☃xxxx = ☃[☃xxx + ☃xx * ☃];
            if (☃xxxx != 0 && this.nextInt(13) == 0) {
               ☃xxxx |= 1 + this.nextInt(15) << 8 & 3840;
            }

            ☃x[☃xxx + ☃xx * ☃] = ☃xxxx;
         }
      }

      return ☃x;
   }

   public static enum Mode {
      COOL_WARM,
      HEAT_ICE,
      SPECIAL;
   }
}
