package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorImproved extends NoiseGenerator {
   private final int[] permutations = new int[512];
   public double xCoord;
   public double yCoord;
   public double zCoord;
   private static final double[] GRAD_X = new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
   private static final double[] GRAD_Y = new double[]{1.0, 1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0};
   private static final double[] GRAD_Z = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};
   private static final double[] GRAD_2X = new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
   private static final double[] GRAD_2Z = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};

   public NoiseGeneratorImproved() {
      this(new Random());
   }

   public NoiseGeneratorImproved(Random var1) {
      this.xCoord = ☃.nextDouble() * 256.0;
      this.yCoord = ☃.nextDouble() * 256.0;
      this.zCoord = ☃.nextDouble() * 256.0;
      int ☃ = 0;

      while (☃ < 256) {
         this.permutations[☃] = ☃++;
      }

      for (int ☃x = 0; ☃x < 256; ☃x++) {
         int ☃xx = ☃.nextInt(256 - ☃x) + ☃x;
         int ☃xxx = this.permutations[☃x];
         this.permutations[☃x] = this.permutations[☃xx];
         this.permutations[☃xx] = ☃xxx;
         this.permutations[☃x + 256] = this.permutations[☃x];
      }
   }

   public final double lerp(double var1, double var3, double var5) {
      return ☃ + ☃ * (☃ - ☃);
   }

   public final double grad2(int var1, double var2, double var4) {
      int ☃ = ☃ & 15;
      return GRAD_2X[☃] * ☃ + GRAD_2Z[☃] * ☃;
   }

   public final double grad(int var1, double var2, double var4, double var6) {
      int ☃ = ☃ & 15;
      return GRAD_X[☃] * ☃ + GRAD_Y[☃] * ☃ + GRAD_Z[☃] * ☃;
   }

   public void populateNoiseArray(
      double[] var1, double var2, double var4, double var6, int var8, int var9, int var10, double var11, double var13, double var15, double var17
   ) {
      if (☃ == 1) {
         int ☃ = 0;
         int ☃x = 0;
         int ☃xx = 0;
         int ☃xxx = 0;
         double ☃xxxx = 0.0;
         double ☃xxxxx = 0.0;
         int ☃xxxxxx = 0;
         double ☃xxxxxxx = 1.0 / ☃;

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃; ☃xxxxxxxx++) {
            double ☃xxxxxxxxx = ☃ + ☃xxxxxxxx * ☃ + this.xCoord;
            int ☃xxxxxxxxxx = (int)☃xxxxxxxxx;
            if (☃xxxxxxxxx < ☃xxxxxxxxxx) {
               ☃xxxxxxxxxx--;
            }

            int ☃xxxxxxxxxxx = ☃xxxxxxxxxx & 0xFF;
            ☃xxxxxxxxx -= ☃xxxxxxxxxx;
            double ☃xxxxxxxxxxxx = ☃xxxxxxxxx * ☃xxxxxxxxx * ☃xxxxxxxxx * (☃xxxxxxxxx * (☃xxxxxxxxx * 6.0 - 15.0) + 10.0);

            for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < ☃; ☃xxxxxxxxxxxxx++) {
               double ☃xxxxxxxxxxxxxx = ☃ + ☃xxxxxxxxxxxxx * ☃ + this.zCoord;
               int ☃xxxxxxxxxxxxxxx = (int)☃xxxxxxxxxxxxxx;
               if (☃xxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxx) {
                  ☃xxxxxxxxxxxxxxx--;
               }

               int ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx & 0xFF;
               ☃xxxxxxxxxxxxxx -= ☃xxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxx * 6.0 - 15.0) + 10.0);
               ☃ = this.permutations[☃xxxxxxxxxxx] + 0;
               ☃x = this.permutations[☃] + ☃xxxxxxxxxxxxxxxx;
               ☃xx = this.permutations[☃xxxxxxxxxxx + 1] + 0;
               ☃xxx = this.permutations[☃xx] + ☃xxxxxxxxxxxxxxxx;
               ☃xxxx = this.lerp(
                  ☃xxxxxxxxxxxx,
                  this.grad2(this.permutations[☃x], ☃xxxxxxxxx, ☃xxxxxxxxxxxxxx),
                  this.grad(this.permutations[☃xxx], ☃xxxxxxxxx - 1.0, 0.0, ☃xxxxxxxxxxxxxx)
               );
               ☃xxxxx = this.lerp(
                  ☃xxxxxxxxxxxx,
                  this.grad(this.permutations[☃x + 1], ☃xxxxxxxxx, 0.0, ☃xxxxxxxxxxxxxx - 1.0),
                  this.grad(this.permutations[☃xxx + 1], ☃xxxxxxxxx - 1.0, 0.0, ☃xxxxxxxxxxxxxx - 1.0)
               );
               double ☃xxxxxxxxxxxxxxxxxx = this.lerp(☃xxxxxxxxxxxxxxxxx, ☃xxxx, ☃xxxxx);
               ☃[☃xxxxxx++] += ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxx;
            }
         }
      } else {
         int ☃ = 0;
         double ☃x = 1.0 / ☃;
         int ☃xx = -1;
         int ☃xxx = 0;
         int ☃xxxx = 0;
         int ☃xxxxx = 0;
         int ☃xxxxxx = 0;
         int ☃xxxxxxx = 0;
         int ☃xxxxxxxx = 0;
         double ☃xxxxxxxxx = 0.0;
         double ☃xxxxxxxxxx = 0.0;
         double ☃xxxxxxxxxxx = 0.0;
         double ☃xxxxxxxxxxxx = 0.0;

         for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < ☃; ☃xxxxxxxxxxxxx++) {
            double ☃xxxxxxxxxxxxxx = ☃ + ☃xxxxxxxxxxxxx * ☃ + this.xCoord;
            int ☃xxxxxxxxxxxxxxx = (int)☃xxxxxxxxxxxxxx;
            if (☃xxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxx) {
               ☃xxxxxxxxxxxxxxx--;
            }

            int ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx & 0xFF;
            ☃xxxxxxxxxxxxxx -= ☃xxxxxxxxxxxxxxx;
            double ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxx * 6.0 - 15.0) + 10.0);

            for (int ☃xxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxx < ☃; ☃xxxxxxxxxxxxxxxxxx++) {
               double ☃xxxxxxxxxxxxxxxxxxx = ☃ + ☃xxxxxxxxxxxxxxxxxx * ☃ + this.zCoord;
               int ☃xxxxxxxxxxxxxxxxxxxx = (int)☃xxxxxxxxxxxxxxxxxxx;
               if (☃xxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxx) {
                  ☃xxxxxxxxxxxxxxxxxxxx--;
               }

               int ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx & 0xFF;
               ☃xxxxxxxxxxxxxxxxxxx -= ☃xxxxxxxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx
                  * ☃xxxxxxxxxxxxxxxxxxx
                  * ☃xxxxxxxxxxxxxxxxxxx
                  * (☃xxxxxxxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxxxxxxx * 6.0 - 15.0) + 10.0);

               for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxx < ☃; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃ + ☃xxxxxxxxxxxxxxxxxxxxxxx * ☃ + this.yCoord;
                  int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = (int)☃xxxxxxxxxxxxxxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxx) {
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxx--;
                  }

                  int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx & 0xFF;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxx -= ☃xxxxxxxxxxxxxxxxxxxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxx
                     * ☃xxxxxxxxxxxxxxxxxxxxxxxx
                     * ☃xxxxxxxxxxxxxxxxxxxxxxxx
                     * (☃xxxxxxxxxxxxxxxxxxxxxxxx * (☃xxxxxxxxxxxxxxxxxxxxxxxx * 6.0 - 15.0) + 10.0);
                  if (☃xxxxxxxxxxxxxxxxxxxxxxx == 0 || ☃xxxxxxxxxxxxxxxxxxxxxxxxxx != ☃xx) {
                     ☃xx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
                     ☃xxx = this.permutations[☃xxxxxxxxxxxxxxxx] + ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
                     ☃xxxx = this.permutations[☃xxx] + ☃xxxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxx = this.permutations[☃xxx + 1] + ☃xxxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxxx = this.permutations[☃xxxxxxxxxxxxxxxx + 1] + ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxxxx = this.permutations[☃xxxxxx] + ☃xxxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxxxxx = this.permutations[☃xxxxxx + 1] + ☃xxxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxxxxxx = this.lerp(
                        ☃xxxxxxxxxxxxxxxxx,
                        this.grad(this.permutations[☃xxxx], ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx),
                        this.grad(this.permutations[☃xxxxxxx], ☃xxxxxxxxxxxxxx - 1.0, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx)
                     );
                     ☃xxxxxxxxxx = this.lerp(
                        ☃xxxxxxxxxxxxxxxxx,
                        this.grad(this.permutations[☃xxxxx], ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx - 1.0, ☃xxxxxxxxxxxxxxxxxxx),
                        this.grad(this.permutations[☃xxxxxxxx], ☃xxxxxxxxxxxxxx - 1.0, ☃xxxxxxxxxxxxxxxxxxxxxxxx - 1.0, ☃xxxxxxxxxxxxxxxxxxx)
                     );
                     ☃xxxxxxxxxxx = this.lerp(
                        ☃xxxxxxxxxxxxxxxxx,
                        this.grad(this.permutations[☃xxxx + 1], ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx - 1.0),
                        this.grad(this.permutations[☃xxxxxxx + 1], ☃xxxxxxxxxxxxxx - 1.0, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx - 1.0)
                     );
                     ☃xxxxxxxxxxxx = this.lerp(
                        ☃xxxxxxxxxxxxxxxxx,
                        this.grad(this.permutations[☃xxxxx + 1], ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx - 1.0, ☃xxxxxxxxxxxxxxxxxxx - 1.0),
                        this.grad(this.permutations[☃xxxxxxxx + 1], ☃xxxxxxxxxxxxxx - 1.0, ☃xxxxxxxxxxxxxxxxxxxxxxxx - 1.0, ☃xxxxxxxxxxxxxxxxxxx - 1.0)
                     );
                  }

                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.lerp(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx);
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.lerp(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.lerp(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
                  ☃[☃++] += ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃x;
               }
            }
         }
      }
   }
}
