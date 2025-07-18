package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.util.math.MathHelper;

public class NoiseGeneratorOctaves extends NoiseGenerator {
   private final NoiseGeneratorImproved[] generatorCollection;
   private final int octaves;

   public NoiseGeneratorOctaves(Random var1, int var2) {
      this.octaves = ☃;
      this.generatorCollection = new NoiseGeneratorImproved[☃];

      for (int ☃ = 0; ☃ < ☃; ☃++) {
         this.generatorCollection[☃] = new NoiseGeneratorImproved(☃);
      }
   }

   public double[] generateNoiseOctaves(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7, double var8, double var10, double var12) {
      if (☃ == null) {
         ☃ = new double[☃ * ☃ * ☃];
      } else {
         for (int ☃ = 0; ☃ < ☃.length; ☃++) {
            ☃[☃] = 0.0;
         }
      }

      double ☃ = 1.0;

      for (int ☃x = 0; ☃x < this.octaves; ☃x++) {
         double ☃xx = ☃ * ☃ * ☃;
         double ☃xxx = ☃ * ☃ * ☃;
         double ☃xxxx = ☃ * ☃ * ☃;
         long ☃xxxxx = MathHelper.lfloor(☃xx);
         long ☃xxxxxx = MathHelper.lfloor(☃xxxx);
         ☃xx -= ☃xxxxx;
         ☃xxxx -= ☃xxxxxx;
         ☃xxxxx %= 16777216L;
         ☃xxxxxx %= 16777216L;
         ☃xx += ☃xxxxx;
         ☃xxxx += ☃xxxxxx;
         this.generatorCollection[☃x].populateNoiseArray(☃, ☃xx, ☃xxx, ☃xxxx, ☃, ☃, ☃, ☃ * ☃, ☃ * ☃, ☃ * ☃, ☃);
         ☃ /= 2.0;
      }

      return ☃;
   }

   public double[] generateNoiseOctaves(double[] var1, int var2, int var3, int var4, int var5, double var6, double var8, double var10) {
      return this.generateNoiseOctaves(☃, ☃, 10, ☃, ☃, 1, ☃, ☃, 1.0, ☃);
   }
}
