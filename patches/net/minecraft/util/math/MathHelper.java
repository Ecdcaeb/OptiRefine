package net.minecraft.util.math;

import java.util.Random;
import java.util.UUID;

public class MathHelper {
   public static final float SQRT_2 = sqrt(2.0F);
   private static final float[] SIN_TABLE = new float[65536];
   private static final Random RANDOM = new Random();
   private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
   private static final double FRAC_BIAS;
   private static final double[] ASINE_TAB;
   private static final double[] COS_TAB;

   public static float sin(float var0) {
      return SIN_TABLE[(int)(☃ * 10430.378F) & 65535];
   }

   public static float cos(float var0) {
      return SIN_TABLE[(int)(☃ * 10430.378F + 16384.0F) & 65535];
   }

   public static float sqrt(float var0) {
      return (float)Math.sqrt(☃);
   }

   public static float sqrt(double var0) {
      return (float)Math.sqrt(☃);
   }

   public static int floor(float var0) {
      int ☃ = (int)☃;
      return ☃ < ☃ ? ☃ - 1 : ☃;
   }

   public static int fastFloor(double var0) {
      return (int)(☃ + 1024.0) - 1024;
   }

   public static int floor(double var0) {
      int ☃ = (int)☃;
      return ☃ < ☃ ? ☃ - 1 : ☃;
   }

   public static long lfloor(double var0) {
      long ☃ = (long)☃;
      return ☃ < ☃ ? ☃ - 1L : ☃;
   }

   public static int absFloor(double var0) {
      return (int)(☃ >= 0.0 ? ☃ : -☃ + 1.0);
   }

   public static float abs(float var0) {
      return ☃ >= 0.0F ? ☃ : -☃;
   }

   public static int abs(int var0) {
      return ☃ >= 0 ? ☃ : -☃;
   }

   public static int ceil(float var0) {
      int ☃ = (int)☃;
      return ☃ > ☃ ? ☃ + 1 : ☃;
   }

   public static int ceil(double var0) {
      int ☃ = (int)☃;
      return ☃ > ☃ ? ☃ + 1 : ☃;
   }

   public static int clamp(int var0, int var1, int var2) {
      if (☃ < ☃) {
         return ☃;
      } else {
         return ☃ > ☃ ? ☃ : ☃;
      }
   }

   public static float clamp(float var0, float var1, float var2) {
      if (☃ < ☃) {
         return ☃;
      } else {
         return ☃ > ☃ ? ☃ : ☃;
      }
   }

   public static double clamp(double var0, double var2, double var4) {
      if (☃ < ☃) {
         return ☃;
      } else {
         return ☃ > ☃ ? ☃ : ☃;
      }
   }

   public static double clampedLerp(double var0, double var2, double var4) {
      if (☃ < 0.0) {
         return ☃;
      } else {
         return ☃ > 1.0 ? ☃ : ☃ + (☃ - ☃) * ☃;
      }
   }

   public static double absMax(double var0, double var2) {
      if (☃ < 0.0) {
         ☃ = -☃;
      }

      if (☃ < 0.0) {
         ☃ = -☃;
      }

      return ☃ > ☃ ? ☃ : ☃;
   }

   public static int intFloorDiv(int var0, int var1) {
      return ☃ < 0 ? -((-☃ - 1) / ☃) - 1 : ☃ / ☃;
   }

   public static int getInt(Random var0, int var1, int var2) {
      return ☃ >= ☃ ? ☃ : ☃.nextInt(☃ - ☃ + 1) + ☃;
   }

   public static float nextFloat(Random var0, float var1, float var2) {
      return ☃ >= ☃ ? ☃ : ☃.nextFloat() * (☃ - ☃) + ☃;
   }

   public static double nextDouble(Random var0, double var1, double var3) {
      return ☃ >= ☃ ? ☃ : ☃.nextDouble() * (☃ - ☃) + ☃;
   }

   public static double average(long[] var0) {
      long ☃ = 0L;

      for (long ☃x : ☃) {
         ☃ += ☃x;
      }

      return (double)☃ / ☃.length;
   }

   public static boolean epsilonEquals(float var0, float var1) {
      return abs(☃ - ☃) < 1.0E-5F;
   }

   public static int normalizeAngle(int var0, int var1) {
      return (☃ % ☃ + ☃) % ☃;
   }

   public static float positiveModulo(float var0, float var1) {
      return (☃ % ☃ + ☃) % ☃;
   }

   public static double positiveModulo(double var0, double var2) {
      return (☃ % ☃ + ☃) % ☃;
   }

   public static float wrapDegrees(float var0) {
      ☃ %= 360.0F;
      if (☃ >= 180.0F) {
         ☃ -= 360.0F;
      }

      if (☃ < -180.0F) {
         ☃ += 360.0F;
      }

      return ☃;
   }

   public static double wrapDegrees(double var0) {
      ☃ %= 360.0;
      if (☃ >= 180.0) {
         ☃ -= 360.0;
      }

      if (☃ < -180.0) {
         ☃ += 360.0;
      }

      return ☃;
   }

   public static int wrapDegrees(int var0) {
      ☃ %= 360;
      if (☃ >= 180) {
         ☃ -= 360;
      }

      if (☃ < -180) {
         ☃ += 360;
      }

      return ☃;
   }

   public static int getInt(String var0, int var1) {
      try {
         return Integer.parseInt(☃);
      } catch (Throwable var3) {
         return ☃;
      }
   }

   public static int getInt(String var0, int var1, int var2) {
      return Math.max(☃, getInt(☃, ☃));
   }

   public static double getDouble(String var0, double var1) {
      try {
         return Double.parseDouble(☃);
      } catch (Throwable var4) {
         return ☃;
      }
   }

   public static double getDouble(String var0, double var1, double var3) {
      return Math.max(☃, getDouble(☃, ☃));
   }

   public static int smallestEncompassingPowerOfTwo(int var0) {
      int ☃ = ☃ - 1;
      ☃ |= ☃ >> 1;
      ☃ |= ☃ >> 2;
      ☃ |= ☃ >> 4;
      ☃ |= ☃ >> 8;
      ☃ |= ☃ >> 16;
      return ☃ + 1;
   }

   private static boolean isPowerOfTwo(int var0) {
      return ☃ != 0 && (☃ & ☃ - 1) == 0;
   }

   public static int log2DeBruijn(int var0) {
      ☃ = isPowerOfTwo(☃) ? ☃ : smallestEncompassingPowerOfTwo(☃);
      return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(☃ * 125613361L >> 27) & 31];
   }

   public static int log2(int var0) {
      return log2DeBruijn(☃) - (isPowerOfTwo(☃) ? 0 : 1);
   }

   public static int roundUp(int var0, int var1) {
      if (☃ == 0) {
         return 0;
      } else if (☃ == 0) {
         return ☃;
      } else {
         if (☃ < 0) {
            ☃ *= -1;
         }

         int ☃ = ☃ % ☃;
         return ☃ == 0 ? ☃ : ☃ + ☃ - ☃;
      }
   }

   public static int rgb(float var0, float var1, float var2) {
      return rgb(floor(☃ * 255.0F), floor(☃ * 255.0F), floor(☃ * 255.0F));
   }

   public static int rgb(int var0, int var1, int var2) {
      int var3 = (☃ << 8) + ☃;
      return (var3 << 8) + ☃;
   }

   public static int multiplyColor(int var0, int var1) {
      int ☃ = (☃ & 0xFF0000) >> 16;
      int ☃x = (☃ & 0xFF0000) >> 16;
      int ☃xx = (☃ & 0xFF00) >> 8;
      int ☃xxx = (☃ & 0xFF00) >> 8;
      int ☃xxxx = (☃ & 0xFF) >> 0;
      int ☃xxxxx = (☃ & 0xFF) >> 0;
      int ☃xxxxxx = (int)((float)☃ * ☃x / 255.0F);
      int ☃xxxxxxx = (int)((float)☃xx * ☃xxx / 255.0F);
      int ☃xxxxxxxx = (int)((float)☃xxxx * ☃xxxxx / 255.0F);
      return ☃ & 0xFF000000 | ☃xxxxxx << 16 | ☃xxxxxxx << 8 | ☃xxxxxxxx;
   }

   public static double frac(double var0) {
      return ☃ - Math.floor(☃);
   }

   public static long getPositionRandom(Vec3i var0) {
      return getCoordinateRandom(☃.getX(), ☃.getY(), ☃.getZ());
   }

   public static long getCoordinateRandom(int var0, int var1, int var2) {
      long ☃ = ☃ * 3129871 ^ ☃ * 116129781L ^ ☃;
      return ☃ * ☃ * 42317861L + ☃ * 11L;
   }

   public static UUID getRandomUUID(Random var0) {
      long ☃ = ☃.nextLong() & -61441L | 16384L;
      long ☃x = ☃.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
      return new UUID(☃, ☃x);
   }

   public static UUID getRandomUUID() {
      return getRandomUUID(RANDOM);
   }

   public static double pct(double var0, double var2, double var4) {
      return (☃ - ☃) / (☃ - ☃);
   }

   public static double atan2(double var0, double var2) {
      double ☃ = ☃ * ☃ + ☃ * ☃;
      if (Double.isNaN(☃)) {
         return Double.NaN;
      } else {
         boolean ☃x = ☃ < 0.0;
         if (☃x) {
            ☃ = -☃;
         }

         boolean ☃xx = ☃ < 0.0;
         if (☃xx) {
            ☃ = -☃;
         }

         boolean ☃xxx = ☃ > ☃;
         if (☃xxx) {
            double ☃xxxx = ☃;
            ☃ = ☃;
            ☃ = ☃xxxx;
         }

         double ☃xxxx = fastInvSqrt(☃);
         ☃ *= ☃xxxx;
         ☃ *= ☃xxxx;
         double ☃xxxxx = FRAC_BIAS + ☃;
         int ☃xxxxxx = (int)Double.doubleToRawLongBits(☃xxxxx);
         double ☃xxxxxxx = ASINE_TAB[☃xxxxxx];
         double ☃xxxxxxxx = COS_TAB[☃xxxxxx];
         double ☃xxxxxxxxx = ☃xxxxx - FRAC_BIAS;
         double ☃xxxxxxxxxx = ☃ * ☃xxxxxxxx - ☃ * ☃xxxxxxxxx;
         double ☃xxxxxxxxxxx = (6.0 + ☃xxxxxxxxxx * ☃xxxxxxxxxx) * ☃xxxxxxxxxx * 0.16666666666666666;
         double ☃xxxxxxxxxxxx = ☃xxxxxxx + ☃xxxxxxxxxxx;
         if (☃xxx) {
            ☃xxxxxxxxxxxx = (Math.PI / 2) - ☃xxxxxxxxxxxx;
         }

         if (☃xx) {
            ☃xxxxxxxxxxxx = Math.PI - ☃xxxxxxxxxxxx;
         }

         if (☃x) {
            ☃xxxxxxxxxxxx = -☃xxxxxxxxxxxx;
         }

         return ☃xxxxxxxxxxxx;
      }
   }

   public static double fastInvSqrt(double var0) {
      double ☃ = 0.5 * ☃;
      long ☃x = Double.doubleToRawLongBits(☃);
      ☃x = 6910469410427058090L - (☃x >> 1);
      ☃ = Double.longBitsToDouble(☃x);
      return ☃ * (1.5 - ☃ * ☃ * ☃);
   }

   public static int hsvToRGB(float var0, float var1, float var2) {
      int ☃ = (int)(☃ * 6.0F) % 6;
      float ☃x = ☃ * 6.0F - ☃;
      float ☃xx = ☃ * (1.0F - ☃);
      float ☃xxx = ☃ * (1.0F - ☃x * ☃);
      float ☃xxxx = ☃ * (1.0F - (1.0F - ☃x) * ☃);
      float ☃xxxxx;
      float ☃xxxxxx;
      float ☃xxxxxxx;
      switch (☃) {
         case 0:
            ☃xxxxx = ☃;
            ☃xxxxxx = ☃xxxx;
            ☃xxxxxxx = ☃xx;
            break;
         case 1:
            ☃xxxxx = ☃xxx;
            ☃xxxxxx = ☃;
            ☃xxxxxxx = ☃xx;
            break;
         case 2:
            ☃xxxxx = ☃xx;
            ☃xxxxxx = ☃;
            ☃xxxxxxx = ☃xxxx;
            break;
         case 3:
            ☃xxxxx = ☃xx;
            ☃xxxxxx = ☃xxx;
            ☃xxxxxxx = ☃;
            break;
         case 4:
            ☃xxxxx = ☃xxxx;
            ☃xxxxxx = ☃xx;
            ☃xxxxxxx = ☃;
            break;
         case 5:
            ☃xxxxx = ☃;
            ☃xxxxxx = ☃xx;
            ☃xxxxxxx = ☃xxx;
            break;
         default:
            throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + ☃ + ", " + ☃ + ", " + ☃);
      }

      int ☃ = clamp((int)(☃xxxxx * 255.0F), 0, 255);
      int ☃x = clamp((int)(☃xxxxxx * 255.0F), 0, 255);
      int ☃xx = clamp((int)(☃xxxxxxx * 255.0F), 0, 255);
      return ☃ << 16 | ☃x << 8 | ☃xx;
   }

   public static int hash(int var0) {
      ☃ ^= ☃ >>> 16;
      ☃ *= -2048144789;
      ☃ ^= ☃ >>> 13;
      ☃ *= -1028477387;
      return ☃ ^ ☃ >>> 16;
   }

   static {
      for (int ☃ = 0; ☃ < 65536; ☃++) {
         SIN_TABLE[☃] = (float)Math.sin(☃ * Math.PI * 2.0 / 65536.0);
      }

      MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{
         0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
      };
      FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
      ASINE_TAB = new double[257];
      COS_TAB = new double[257];

      for (int ☃ = 0; ☃ < 257; ☃++) {
         double ☃x = ☃ / 256.0;
         double ☃xx = Math.asin(☃x);
         COS_TAB[☃] = Math.cos(☃xx);
         ASINE_TAB[☃] = ☃xx;
      }
   }
}
