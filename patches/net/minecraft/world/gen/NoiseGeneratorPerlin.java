package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorPerlin extends NoiseGenerator {
   private final NoiseGeneratorSimplex[] noiseLevels;
   private final int levels;

   public NoiseGeneratorPerlin(Random var1, int var2) {
      this.levels = ☃;
      this.noiseLevels = new NoiseGeneratorSimplex[☃];

      for (int ☃ = 0; ☃ < ☃; ☃++) {
         this.noiseLevels[☃] = new NoiseGeneratorSimplex(☃);
      }
   }

   public double getValue(double var1, double var3) {
      double ☃ = 0.0;
      double ☃x = 1.0;

      for (int ☃xx = 0; ☃xx < this.levels; ☃xx++) {
         ☃ += this.noiseLevels[☃xx].getValue(☃ * ☃x, ☃ * ☃x) / ☃x;
         ☃x /= 2.0;
      }

      return ☃;
   }

   public double[] getRegion(double[] var1, double var2, double var4, int var6, int var7, double var8, double var10, double var12) {
      return this.getRegion(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, 0.5);
   }

   public double[] getRegion(double[] var1, double var2, double var4, int var6, int var7, double var8, double var10, double var12, double var14) {
      if (☃ != null && ☃.length >= ☃ * ☃) {
         for (int ☃ = 0; ☃ < ☃.length; ☃++) {
            ☃[☃] = 0.0;
         }
      } else {
         ☃ = new double[☃ * ☃];
      }

      double ☃ = 1.0;
      double ☃x = 1.0;

      for (int ☃xx = 0; ☃xx < this.levels; ☃xx++) {
         this.noiseLevels[☃xx].add(☃, ☃, ☃, ☃, ☃, ☃ * ☃x * ☃, ☃ * ☃x * ☃, 0.55 / ☃);
         ☃x *= ☃;
         ☃ *= ☃;
      }

      return ☃;
   }
}
