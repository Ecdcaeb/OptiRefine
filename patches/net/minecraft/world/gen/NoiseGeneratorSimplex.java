package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex {
   private static final int[][] grad3 = new int[][]{
      {1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}
   };
   public static final double SQRT_3 = Math.sqrt(3.0);
   private final int[] p = new int[512];
   public double xo;
   public double yo;
   public double zo;
   private static final double F2 = 0.5 * (SQRT_3 - 1.0);
   private static final double G2 = (3.0 - SQRT_3) / 6.0;

   public NoiseGeneratorSimplex() {
      this(new Random());
   }

   public NoiseGeneratorSimplex(Random var1) {
      this.xo = ☃.nextDouble() * 256.0;
      this.yo = ☃.nextDouble() * 256.0;
      this.zo = ☃.nextDouble() * 256.0;
      int ☃ = 0;

      while (☃ < 256) {
         this.p[☃] = ☃++;
      }

      for (int ☃x = 0; ☃x < 256; ☃x++) {
         int ☃xx = ☃.nextInt(256 - ☃x) + ☃x;
         int ☃xxx = this.p[☃x];
         this.p[☃x] = this.p[☃xx];
         this.p[☃xx] = ☃xxx;
         this.p[☃x + 256] = this.p[☃x];
      }
   }

   private static int fastFloor(double var0) {
      return ☃ > 0.0 ? (int)☃ : (int)☃ - 1;
   }

   private static double dot(int[] var0, double var1, double var3) {
      return ☃[0] * ☃ + ☃[1] * ☃;
   }

   public double getValue(double var1, double var3) {
      double ☃ = 0.5 * (SQRT_3 - 1.0);
      double ☃x = (☃ + ☃) * ☃;
      int ☃xx = fastFloor(☃ + ☃x);
      int ☃xxx = fastFloor(☃ + ☃x);
      double ☃xxxx = (3.0 - SQRT_3) / 6.0;
      double ☃xxxxx = (☃xx + ☃xxx) * ☃xxxx;
      double ☃xxxxxx = ☃xx - ☃xxxxx;
      double ☃xxxxxxx = ☃xxx - ☃xxxxx;
      double ☃xxxxxxxx = ☃ - ☃xxxxxx;
      double ☃xxxxxxxxx = ☃ - ☃xxxxxxx;
      int ☃xxxxxxxxxx;
      int ☃xxxxxxxxxxx;
      if (☃xxxxxxxx > ☃xxxxxxxxx) {
         ☃xxxxxxxxxx = 1;
         ☃xxxxxxxxxxx = 0;
      } else {
         ☃xxxxxxxxxx = 0;
         ☃xxxxxxxxxxx = 1;
      }

      double ☃xxxxxxxxxxxx = ☃xxxxxxxx - ☃xxxxxxxxxx + ☃xxxx;
      double ☃xxxxxxxxxxxxx = ☃xxxxxxxxx - ☃xxxxxxxxxxx + ☃xxxx;
      double ☃xxxxxxxxxxxxxx = ☃xxxxxxxx - 1.0 + 2.0 * ☃xxxx;
      double ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxx - 1.0 + 2.0 * ☃xxxx;
      int ☃xxxxxxxxxxxxxxxx = ☃xx & 0xFF;
      int ☃xxxxxxxxxxxxxxxxx = ☃xxx & 0xFF;
      int ☃xxxxxxxxxxxxxxxxxx = this.p[☃xxxxxxxxxxxxxxxx + this.p[☃xxxxxxxxxxxxxxxxx]] % 12;
      int ☃xxxxxxxxxxxxxxxxxxx = this.p[☃xxxxxxxxxxxxxxxx + ☃xxxxxxxxxx + this.p[☃xxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxx]] % 12;
      int ☃xxxxxxxxxxxxxxxxxxxx = this.p[☃xxxxxxxxxxxxxxxx + 1 + this.p[☃xxxxxxxxxxxxxxxxx + 1]] % 12;
      double ☃xxxxxxxxxxxxxxxxxxxxx = 0.5 - ☃xxxxxxxx * ☃xxxxxxxx - ☃xxxxxxxxx * ☃xxxxxxxxx;
      double ☃xxxxxxxxxxxxxxxxxxxxxx;
      if (☃xxxxxxxxxxxxxxxxxxxxx < 0.0) {
         ☃xxxxxxxxxxxxxxxxxxxxxx = 0.0;
      } else {
         ☃xxxxxxxxxxxxxxxxxxxxx *= ☃xxxxxxxxxxxxxxxxxxxxx;
         ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxx * dot(grad3[☃xxxxxxxxxxxxxxxxxx], ☃xxxxxxxx, ☃xxxxxxxxx);
      }

      double ☃xxxxxxxxxxxxxxxxxxxxxxx = 0.5 - ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx - ☃xxxxxxxxxxxxx * ☃xxxxxxxxxxxxx;
      double ☃xxxxxxxxxxxxxxxxxxxxxxxx;
      if (☃xxxxxxxxxxxxxxxxxxxxxxx < 0.0) {
         ☃xxxxxxxxxxxxxxxxxxxxxxxx = 0.0;
      } else {
         ☃xxxxxxxxxxxxxxxxxxxxxxx *= ☃xxxxxxxxxxxxxxxxxxxxxxx;
         ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxx * dot(grad3[☃xxxxxxxxxxxxxxxxxxx], ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx);
      }

      double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = 0.5 - ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx;
      double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
      if (☃xxxxxxxxxxxxxxxxxxxxxxxxx < 0.0) {
         ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0;
      } else {
         ☃xxxxxxxxxxxxxxxxxxxxxxxxx *= ☃xxxxxxxxxxxxxxxxxxxxxxxxx;
         ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx
            * ☃xxxxxxxxxxxxxxxxxxxxxxxxx
            * dot(grad3[☃xxxxxxxxxxxxxxxxxxxx], ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx);
      }

      return 70.0 * (☃xxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxx);
   }

   public void add(double[] var1, double var2, double var4, int var6, int var7, double var8, double var10, double var12) {
      int ☃ = 0;

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         double ☃xx = (☃ + ☃x) * ☃ + this.yo;

         for (int ☃xxx = 0; ☃xxx < ☃; ☃xxx++) {
            double ☃xxxx = (☃ + ☃xxx) * ☃ + this.xo;
            double ☃xxxxx = (☃xxxx + ☃xx) * F2;
            int ☃xxxxxx = fastFloor(☃xxxx + ☃xxxxx);
            int ☃xxxxxxx = fastFloor(☃xx + ☃xxxxx);
            double ☃xxxxxxxx = (☃xxxxxx + ☃xxxxxxx) * G2;
            double ☃xxxxxxxxx = ☃xxxxxx - ☃xxxxxxxx;
            double ☃xxxxxxxxxx = ☃xxxxxxx - ☃xxxxxxxx;
            double ☃xxxxxxxxxxx = ☃xxxx - ☃xxxxxxxxx;
            double ☃xxxxxxxxxxxx = ☃xx - ☃xxxxxxxxxx;
            int ☃xxxxxxxxxxxxx;
            int ☃xxxxxxxxxxxxxx;
            if (☃xxxxxxxxxxx > ☃xxxxxxxxxxxx) {
               ☃xxxxxxxxxxxxx = 1;
               ☃xxxxxxxxxxxxxx = 0;
            } else {
               ☃xxxxxxxxxxxxx = 0;
               ☃xxxxxxxxxxxxxx = 1;
            }

            double ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxx - ☃xxxxxxxxxxxxx + G2;
            double ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx - ☃xxxxxxxxxxxxxx + G2;
            double ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx - 1.0 + 2.0 * G2;
            double ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx - 1.0 + 2.0 * G2;
            int ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxx & 0xFF;
            int ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxx & 0xFF;
            int ☃xxxxxxxxxxxxxxxxxxxxx = this.p[☃xxxxxxxxxxxxxxxxxxx + this.p[☃xxxxxxxxxxxxxxxxxxxx]] % 12;
            int ☃xxxxxxxxxxxxxxxxxxxxxx = this.p[☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxx + this.p[☃xxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxx]] % 12;
            int ☃xxxxxxxxxxxxxxxxxxxxxxx = this.p[☃xxxxxxxxxxxxxxxxxxx + 1 + this.p[☃xxxxxxxxxxxxxxxxxxxx + 1]] % 12;
            double ☃xxxxxxxxxxxxxxxxxxxxxxxx = 0.5 - ☃xxxxxxxxxxx * ☃xxxxxxxxxxx - ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx;
            double ☃xxxxxxxxxxxxxxxxxxxxxxxxx;
            if (☃xxxxxxxxxxxxxxxxxxxxxxxx < 0.0) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxx = 0.0;
            } else {
               ☃xxxxxxxxxxxxxxxxxxxxxxxx *= ☃xxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxx
                  * ☃xxxxxxxxxxxxxxxxxxxxxxxx
                  * dot(grad3[☃xxxxxxxxxxxxxxxxxxxxx], ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
            }

            double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = 0.5 - ☃xxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx;
            double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx;
            if (☃xxxxxxxxxxxxxxxxxxxxxxxxxx < 0.0) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0;
            } else {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxx *= ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxx
                  * ☃xxxxxxxxxxxxxxxxxxxxxxxxxx
                  * dot(grad3[☃xxxxxxxxxxxxxxxxxxxxxx], ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx);
            }

            double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.5 - ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx;
            double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx < 0.0) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0;
            } else {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx *= ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                  * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                  * dot(grad3[☃xxxxxxxxxxxxxxxxxxxxxxx], ☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx);
            }

            ☃[☃++] += 70.0 * (☃xxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * ☃;
         }
      }
   }
}
