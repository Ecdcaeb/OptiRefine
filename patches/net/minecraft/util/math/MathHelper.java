package net.minecraft.util.math;

import java.util.Random;
import java.util.UUID;
import net.optifine.util.MathUtils;

public class MathHelper {
   public static final float SQRT_2 = sqrt(2.0F);
   private static final int SIN_BITS = 12;
   private static final int SIN_MASK = 4095;
   private static final int SIN_COUNT = 4096;
   private static final int SIN_COUNT_D4 = 1024;
   public static final float PI = MathUtils.roundToFloat(Math.PI);
   public static final float PI2 = MathUtils.roundToFloat(Math.PI * 2);
   public static final float PId2 = MathUtils.roundToFloat(Math.PI / 2);
   private static final float radToIndex = MathUtils.roundToFloat(651.8986469044033);
   public static final float deg2Rad = MathUtils.roundToFloat(Math.PI / 180.0);
   private static final float[] SIN_TABLE_FAST = new float[4096];
   public static boolean fastMath = false;
   private static final float[] SIN_TABLE = new float[65536];
   private static final Random RANDOM = new Random();
   private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
   private static final double FRAC_BIAS;
   private static final double[] ASINE_TAB;
   private static final double[] COS_TAB;

   public static float sin(float value) {
      return fastMath ? SIN_TABLE_FAST[(int)(value * radToIndex) & 4095] : SIN_TABLE[(int)(value * 10430.378F) & 65535];
   }

   public static float cos(float value) {
      return fastMath ? SIN_TABLE_FAST[(int)(value * radToIndex + 1024.0F) & 4095] : SIN_TABLE[(int)(value * 10430.378F + 16384.0F) & 65535];
   }

   public static float sqrt(float value) {
      return (float)Math.sqrt(value);
   }

   public static float sqrt(double value) {
      return (float)Math.sqrt(value);
   }

   public static int floor(float value) {
      int i = (int)value;
      return value < i ? i - 1 : i;
   }

   public static int fastFloor(double value) {
      return (int)(value + 1024.0) - 1024;
   }

   public static int floor(double value) {
      int i = (int)value;
      return value < i ? i - 1 : i;
   }

   public static long lfloor(double value) {
      long i = (long)value;
      return value < i ? i - 1L : i;
   }

   public static int absFloor(double value) {
      return (int)(value >= 0.0 ? value : -value + 1.0);
   }

   public static float abs(float value) {
      return value >= 0.0F ? value : -value;
   }

   public static int abs(int value) {
      return value >= 0 ? value : -value;
   }

   public static int ceil(float value) {
      int i = (int)value;
      return value > i ? i + 1 : i;
   }

   public static int ceil(double value) {
      int i = (int)value;
      return value > i ? i + 1 : i;
   }

   public static int clamp(int num, int min, int max) {
      if (num < min) {
         return min;
      } else {
         return num > max ? max : num;
      }
   }

   public static float clamp(float num, float min, float max) {
      if (num < min) {
         return min;
      } else {
         return num > max ? max : num;
      }
   }

   public static double clamp(double num, double min, double max) {
      if (num < min) {
         return min;
      } else {
         return num > max ? max : num;
      }
   }

   public static double clampedLerp(double lowerBnd, double upperBnd, double slide) {
      if (slide < 0.0) {
         return lowerBnd;
      } else {
         return slide > 1.0 ? upperBnd : lowerBnd + (upperBnd - lowerBnd) * slide;
      }
   }

   public static double absMax(double p_76132_0_, double p_76132_2_) {
      if (p_76132_0_ < 0.0) {
         p_76132_0_ = -p_76132_0_;
      }

      if (p_76132_2_ < 0.0) {
         p_76132_2_ = -p_76132_2_;
      }

      return p_76132_0_ > p_76132_2_ ? p_76132_0_ : p_76132_2_;
   }

   public static int intFloorDiv(int p_76137_0_, int p_76137_1_) {
      return p_76137_0_ < 0 ? -((-p_76137_0_ - 1) / p_76137_1_) - 1 : p_76137_0_ / p_76137_1_;
   }

   public static int getInt(Random random, int minimum, int maximum) {
      return minimum >= maximum ? minimum : random.nextInt(maximum - minimum + 1) + minimum;
   }

   public static float nextFloat(Random random, float minimum, float maximum) {
      return minimum >= maximum ? minimum : random.nextFloat() * (maximum - minimum) + minimum;
   }

   public static double nextDouble(Random random, double minimum, double maximum) {
      return minimum >= maximum ? minimum : random.nextDouble() * (maximum - minimum) + minimum;
   }

   public static double average(long[] values) {
      long i = 0L;

      for (long j : values) {
         i += j;
      }

      return (double)i / values.length;
   }

   public static boolean epsilonEquals(float p_180185_0_, float p_180185_1_) {
      return abs(p_180185_1_ - p_180185_0_) < 1.0E-5F;
   }

   public static int normalizeAngle(int p_180184_0_, int p_180184_1_) {
      return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
   }

   public static float positiveModulo(float numerator, float denominator) {
      return (numerator % denominator + denominator) % denominator;
   }

   public static double positiveModulo(double numerator, double denominator) {
      return (numerator % denominator + denominator) % denominator;
   }

   public static float wrapDegrees(float value) {
      value %= 360.0F;
      if (value >= 180.0F) {
         value -= 360.0F;
      }

      if (value < -180.0F) {
         value += 360.0F;
      }

      return value;
   }

   public static double wrapDegrees(double value) {
      value %= 360.0;
      if (value >= 180.0) {
         value -= 360.0;
      }

      if (value < -180.0) {
         value += 360.0;
      }

      return value;
   }

   public static int wrapDegrees(int angle) {
      angle %= 360;
      if (angle >= 180) {
         angle -= 360;
      }

      if (angle < -180) {
         angle += 360;
      }

      return angle;
   }

   public static int getInt(String value, int defaultValue) {
      try {
         return Integer.parseInt(value);
      } catch (Throwable var3) {
         return defaultValue;
      }
   }

   public static int getInt(String value, int defaultValue, int max) {
      return Math.max(max, getInt(value, defaultValue));
   }

   public static double getDouble(String value, double defaultValue) {
      try {
         return Double.parseDouble(value);
      } catch (Throwable var4) {
         return defaultValue;
      }
   }

   public static double getDouble(String value, double defaultValue, double max) {
      return Math.max(max, getDouble(value, defaultValue));
   }

   public static int smallestEncompassingPowerOfTwo(int value) {
      int i = value - 1;
      i |= i >> 1;
      i |= i >> 2;
      i |= i >> 4;
      i |= i >> 8;
      i |= i >> 16;
      return i + 1;
   }

   private static boolean isPowerOfTwo(int value) {
      return value != 0 && (value & value - 1) == 0;
   }

   public static int log2DeBruijn(int value) {
      value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
      return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(value * 125613361L >> 27) & 31];
   }

   public static int log2(int value) {
      return log2DeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
   }

   public static int roundUp(int number, int interval) {
      if (interval == 0) {
         return 0;
      } else if (number == 0) {
         return interval;
      } else {
         if (number < 0) {
            interval *= -1;
         }

         int i = number % interval;
         return i == 0 ? number : number + interval - i;
      }
   }

   public static int rgb(float rIn, float gIn, float bIn) {
      return rgb(floor(rIn * 255.0F), floor(gIn * 255.0F), floor(bIn * 255.0F));
   }

   public static int rgb(int rIn, int gIn, int bIn) {
      int lvt_3_1_ = (rIn << 8) + gIn;
      return (lvt_3_1_ << 8) + bIn;
   }

   public static int multiplyColor(int p_180188_0_, int p_180188_1_) {
      int i = (p_180188_0_ & 0xFF0000) >> 16;
      int j = (p_180188_1_ & 0xFF0000) >> 16;
      int k = (p_180188_0_ & 0xFF00) >> 8;
      int l = (p_180188_1_ & 0xFF00) >> 8;
      int i1 = (p_180188_0_ & 0xFF) >> 0;
      int j1 = (p_180188_1_ & 0xFF) >> 0;
      int k1 = (int)((float)i * j / 255.0F);
      int l1 = (int)((float)k * l / 255.0F);
      int i2 = (int)((float)i1 * j1 / 255.0F);
      return p_180188_0_ & 0xFF000000 | k1 << 16 | l1 << 8 | i2;
   }

   public static double frac(double number) {
      return number - Math.floor(number);
   }

   public static long getPositionRandom(Vec3i pos) {
      return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
   }

   public static long getCoordinateRandom(int x, int y, int z) {
      long i = x * 3129871 ^ z * 116129781L ^ y;
      return i * i * 42317861L + i * 11L;
   }

   public static UUID getRandomUUID(Random rand) {
      long i = rand.nextLong() & -61441L | 16384L;
      long j = rand.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
      return new UUID(i, j);
   }

   public static UUID getRandomUUID() {
      return getRandomUUID(RANDOM);
   }

   public static double pct(double p_181160_0_, double p_181160_2_, double p_181160_4_) {
      return (p_181160_0_ - p_181160_2_) / (p_181160_4_ - p_181160_2_);
   }

   public static double atan2(double p_181159_0_, double p_181159_2_) {
      double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;
      if (Double.isNaN(d0)) {
         return Double.NaN;
      } else {
         boolean flag = p_181159_0_ < 0.0;
         if (flag) {
            p_181159_0_ = -p_181159_0_;
         }

         boolean flag1 = p_181159_2_ < 0.0;
         if (flag1) {
            p_181159_2_ = -p_181159_2_;
         }

         boolean flag2 = p_181159_0_ > p_181159_2_;
         if (flag2) {
            double d1 = p_181159_2_;
            p_181159_2_ = p_181159_0_;
            p_181159_0_ = d1;
         }

         double d9 = fastInvSqrt(d0);
         p_181159_2_ *= d9;
         p_181159_0_ *= d9;
         double d2 = FRAC_BIAS + p_181159_0_;
         int i = (int)Double.doubleToRawLongBits(d2);
         double d3 = ASINE_TAB[i];
         double d4 = COS_TAB[i];
         double d5 = d2 - FRAC_BIAS;
         double d6 = p_181159_0_ * d4 - p_181159_2_ * d5;
         double d7 = (6.0 + d6 * d6) * d6 * 0.16666666666666666;
         double d8 = d3 + d7;
         if (flag2) {
            d8 = (Math.PI / 2) - d8;
         }

         if (flag1) {
            d8 = Math.PI - d8;
         }

         if (flag) {
            d8 = -d8;
         }

         return d8;
      }
   }

   public static double fastInvSqrt(double p_181161_0_) {
      double d0 = 0.5 * p_181161_0_;
      long i = Double.doubleToRawLongBits(p_181161_0_);
      i = 6910469410427058090L - (i >> 1);
      p_181161_0_ = Double.longBitsToDouble(i);
      return p_181161_0_ * (1.5 - d0 * p_181161_0_ * p_181161_0_);
   }

   public static int hsvToRGB(float hue, float saturation, float value) {
      int i = (int)(hue * 6.0F) % 6;
      float f = hue * 6.0F - i;
      float f1 = value * (1.0F - saturation);
      float f2 = value * (1.0F - f * saturation);
      float f3 = value * (1.0F - (1.0F - f) * saturation);
      float f4;
      float f5;
      float f6;
      switch (i) {
         case 0:
            f4 = value;
            f5 = f3;
            f6 = f1;
            break;
         case 1:
            f4 = f2;
            f5 = value;
            f6 = f1;
            break;
         case 2:
            f4 = f1;
            f5 = value;
            f6 = f3;
            break;
         case 3:
            f4 = f1;
            f5 = f2;
            f6 = value;
            break;
         case 4:
            f4 = f3;
            f5 = f1;
            f6 = value;
            break;
         case 5:
            f4 = value;
            f5 = f1;
            f6 = f2;
            break;
         default:
            throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
      }

      int j = clamp((int)(f4 * 255.0F), 0, 255);
      int k = clamp((int)(f5 * 255.0F), 0, 255);
      int l = clamp((int)(f6 * 255.0F), 0, 255);
      return j << 16 | k << 8 | l;
   }

   public static int hash(int p_188208_0_) {
      p_188208_0_ ^= p_188208_0_ >>> 16;
      p_188208_0_ *= -2048144789;
      p_188208_0_ ^= p_188208_0_ >>> 13;
      p_188208_0_ *= -1028477387;
      return p_188208_0_ ^ p_188208_0_ >>> 16;
   }

   static {
      for (int i = 0; i < 65536; i++) {
         SIN_TABLE[i] = (float)Math.sin(i * Math.PI * 2.0 / 65536.0);
      }

      for (int s = 0; s < SIN_TABLE_FAST.length; s++) {
         SIN_TABLE_FAST[s] = MathUtils.roundToFloat(Math.sin(s * Math.PI * 2.0 / 4096.0));
      }

      MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{
         0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
      };
      FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
      ASINE_TAB = new double[257];
      COS_TAB = new double[257];

      for (int j = 0; j < 257; j++) {
         double d0 = j / 256.0;
         double d1 = Math.asin(d0);
         COS_TAB[j] = Math.cos(d1);
         ASINE_TAB[j] = d1;
      }
   }
}
