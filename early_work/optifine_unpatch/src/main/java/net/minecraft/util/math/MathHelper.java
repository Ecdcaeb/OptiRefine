/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Double
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Random
 *  java.util.UUID
 *  net.minecraft.util.math.Vec3i
 */
package net.minecraft.util.math;

import java.util.Random;
import java.util.UUID;
import net.minecraft.util.math.Vec3i;

public class MathHelper {
    public static final float SQRT_2;
    private static final float[] SIN_TABLE;
    private static final Random RANDOM;
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    private static final double FRAC_BIAS;
    private static final double[] ASINE_TAB;
    private static final double[] COS_TAB;

    public static float sin(float f) {
        return SIN_TABLE[(int)(f * 10430.378f) & 0xFFFF];
    }

    public static float cos(float f) {
        return SIN_TABLE[(int)(f * 10430.378f + 16384.0f) & 0xFFFF];
    }

    public static float sqrt(float f) {
        return (float)Math.sqrt((double)f);
    }

    public static float sqrt(double d) {
        return (float)Math.sqrt((double)d);
    }

    public static int floor(float f) {
        int n = (int)f;
        return f < (float)n ? n - 1 : n;
    }

    public static int fastFloor(double d) {
        return (int)(d + 1024.0) - 1024;
    }

    public static int floor(double d) {
        int n = (int)d;
        return d < (double)n ? n - 1 : n;
    }

    public static long lfloor(double d) {
        long l = (long)d;
        return d < (double)l ? l - 1L : l;
    }

    public static int absFloor(double d) {
        return (int)(d >= 0.0 ? d : -d + 1.0);
    }

    public static float abs(float f) {
        return f >= 0.0f ? f : -f;
    }

    public static int abs(int n) {
        return n >= 0 ? n : -n;
    }

    public static int ceil(float f) {
        int n = (int)f;
        return f > (float)n ? n + 1 : n;
    }

    public static int ceil(double d) {
        int n = (int)d;
        return d > (double)n ? n + 1 : n;
    }

    public static int clamp(int n, int n2, int n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }

    public static float clamp(float f, float f2, float f3) {
        if (f < f2) {
            return f2;
        }
        if (f > f3) {
            return f3;
        }
        return f;
    }

    public static double clamp(double d, double d2, double d3) {
        if (d < d2) {
            return d2;
        }
        if (d > d3) {
            return d3;
        }
        return d;
    }

    public static double clampedLerp(double d, double d2, double d3) {
        if (d3 < 0.0) {
            return d;
        }
        if (d3 > 1.0) {
            return d2;
        }
        return d + (d2 - d) * d3;
    }

    public static double absMax(double d, double d2) {
        if (d < 0.0) {
            d = -d;
        }
        if (d2 < 0.0) {
            d2 = -d2;
        }
        return d > d2 ? d : d2;
    }

    public static int intFloorDiv(int n, int n2) {
        if (n < 0) {
            return -((-n - 1) / n2) - 1;
        }
        return n / n2;
    }

    public static int getInt(Random random, int n, int n2) {
        if (n >= n2) {
            return n;
        }
        return random.nextInt(n2 - n + 1) + n;
    }

    public static float nextFloat(Random random, float f, float f2) {
        if (f >= f2) {
            return f;
        }
        return random.nextFloat() * (f2 - f) + f;
    }

    public static double nextDouble(Random random, double d, double d2) {
        if (d >= d2) {
            return d;
        }
        return random.nextDouble() * (d2 - d) + d;
    }

    public static double average(long[] lArray) {
        long l = 0L;
        for (long l2 : lArray) {
            l += l2;
        }
        return (double)l / (double)lArray.length;
    }

    public static boolean epsilonEquals(float f, float f2) {
        return MathHelper.abs(f2 - f) < 1.0E-5f;
    }

    public static int normalizeAngle(int n, int n2) {
        return (n % n2 + n2) % n2;
    }

    public static float positiveModulo(float f, float f2) {
        return (f % f2 + f2) % f2;
    }

    public static double positiveModulo(double d, double d2) {
        return (d % d2 + d2) % d2;
    }

    public static float wrapDegrees(float f) {
        if ((f %= 360.0f) >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }

    public static double wrapDegrees(double d) {
        if ((d %= 360.0) >= 180.0) {
            d -= 360.0;
        }
        if (d < -180.0) {
            d += 360.0;
        }
        return d;
    }

    public static int wrapDegrees(int n) {
        if ((n %= 360) >= 180) {
            n -= 360;
        }
        if (n < -180) {
            n += 360;
        }
        return n;
    }

    public static int getInt(String string, int n) {
        try {
            return Integer.parseInt((String)string);
        }
        catch (Throwable throwable) {
            return n;
        }
    }

    public static int getInt(String string, int n, int n2) {
        return Math.max((int)n2, (int)MathHelper.getInt(string, n));
    }

    public static double getDouble(String string, double d) {
        try {
            return Double.parseDouble((String)string);
        }
        catch (Throwable throwable) {
            return d;
        }
    }

    public static double getDouble(String string, double d, double d2) {
        return Math.max((double)d2, (double)MathHelper.getDouble(string, d));
    }

    public static int smallestEncompassingPowerOfTwo(int n) {
        \u2603 = n - 1;
        \u2603 |= \u2603 >> 1;
        \u2603 |= \u2603 >> 2;
        \u2603 |= \u2603 >> 4;
        \u2603 |= \u2603 >> 8;
        \u2603 |= \u2603 >> 16;
        return \u2603 + 1;
    }

    private static boolean isPowerOfTwo(int n) {
        return n != 0 && (n & n - 1) == 0;
    }

    public static int log2DeBruijn(int n) {
        n = MathHelper.isPowerOfTwo(n) ? n : MathHelper.smallestEncompassingPowerOfTwo(n);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)((long)n * 125613361L >> 27) & 0x1F];
    }

    public static int log2(int n) {
        return MathHelper.log2DeBruijn(n) - (MathHelper.isPowerOfTwo(n) ? 0 : 1);
    }

    public static int roundUp(int n, int n2) {
        if (n2 == 0) {
            return 0;
        }
        if (n == 0) {
            return n2;
        }
        if (n < 0) {
            n2 *= -1;
        }
        if ((\u2603 = n % n2) == 0) {
            return n;
        }
        return n + n2 - \u2603;
    }

    public static int rgb(float f, float f2, float f3) {
        return MathHelper.rgb(MathHelper.floor(f * 255.0f), MathHelper.floor(f2 * 255.0f), MathHelper.floor(f3 * 255.0f));
    }

    public static int rgb(int n, int n2, int n3) {
        \u2603 = n;
        \u2603 = (\u2603 << 8) + n2;
        \u2603 = (\u2603 << 8) + n3;
        return \u2603;
    }

    public static int multiplyColor(int n, int n2) {
        \u2603 = (n & 0xFF0000) >> 16;
        \u2603 = (n2 & 0xFF0000) >> 16;
        \u2603 = (n & 0xFF00) >> 8;
        \u2603 = (n2 & 0xFF00) >> 8;
        \u2603 = (n & 0xFF) >> 0;
        \u2603 = (n2 & 0xFF) >> 0;
        \u2603 = (int)((float)\u2603 * (float)\u2603 / 255.0f);
        \u2603 = (int)((float)\u2603 * (float)\u2603 / 255.0f);
        \u2603 = (int)((float)\u2603 * (float)\u2603 / 255.0f);
        return n & 0xFF000000 | \u2603 << 16 | \u2603 << 8 | \u2603;
    }

    public static double frac(double d) {
        return d - Math.floor((double)d);
    }

    public static long getPositionRandom(Vec3i vec3i) {
        return MathHelper.getCoordinateRandom(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public static long getCoordinateRandom(int n, int n2, int n3) {
        long l = (long)(n * 3129871) ^ (long)n3 * 116129781L ^ (long)n2;
        l = l * l * 42317861L + l * 11L;
        return l;
    }

    public static UUID getRandomUUID(Random random) {
        long l = random.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
        \u2603 = random.nextLong() & 0x3FFFFFFFFFFFFFFFL | Long.MIN_VALUE;
        return new UUID(l, \u2603);
    }

    public static UUID getRandomUUID() {
        return MathHelper.getRandomUUID(RANDOM);
    }

    public static double pct(double d, double d2, double d3) {
        return (d - d2) / (d3 - d2);
    }

    public static double atan2(double d, double d2) {
        \u2603 = d2 * d2 + d * d;
        if (Double.isNaN((double)\u2603)) {
            return Double.NaN;
        }
        boolean bl = \u2603 = d < 0.0;
        if (\u2603) {
            d = -d;
        }
        boolean bl2 = \u2603 = d2 < 0.0;
        if (\u2603) {
            d2 = -d2;
        }
        boolean bl3 = \u2603 = d > d2;
        if (\u2603) {
            \u2603 = d2;
            d2 = d;
            d = \u2603;
        }
        \u2603 = MathHelper.fastInvSqrt(\u2603);
        d2 *= \u2603;
        \u2603 = FRAC_BIAS + (d *= \u2603);
        int n = (int)Double.doubleToRawLongBits((double)\u2603);
        double \u26032 = ASINE_TAB[n];
        double \u26033 = COS_TAB[n];
        double \u26034 = \u2603 - FRAC_BIAS;
        double \u26035 = d * \u26033 - d2 * \u26034;
        double \u26036 = (6.0 + \u26035 * \u26035) * \u26035 * 0.16666666666666666;
        double \u26037 = \u26032 + \u26036;
        if (\u2603) {
            \u26037 = 1.5707963267948966 - \u26037;
        }
        if (\u2603) {
            \u26037 = Math.PI - \u26037;
        }
        if (\u2603) {
            \u26037 = -\u26037;
        }
        return \u26037;
    }

    public static double fastInvSqrt(double \u260322) {
        double d;
        d = 0.5 * \u260322;
        long l = Double.doubleToRawLongBits((double)\u260322);
        l = 6910469410427058090L - (l >> 1);
        double \u260322 = Double.longBitsToDouble((long)l);
        \u260322 *= 1.5 - d * \u260322 * \u260322;
        return \u260322;
    }

    public static int hsvToRGB(float f5, float f2, float f32) {
        float f4;
        int n = (int)(f5 * 6.0f) % 6;
        float \u26032 = f5 * 6.0f - (float)n;
        float \u26033 = f32 * (1.0f - f2);
        float \u26034 = f32 * (1.0f - \u26032 * f2);
        float \u26035 = f32 * (1.0f - (1.0f - \u26032) * f2);
        switch (n) {
            case 0: {
                f4 = f32;
                \u2603 = \u26035;
                \u2603 = \u26033;
                break;
            }
            case 1: {
                f4 = \u26034;
                \u2603 = f32;
                \u2603 = \u26033;
                break;
            }
            case 2: {
                f4 = \u26033;
                \u2603 = f32;
                \u2603 = \u26035;
                break;
            }
            case 3: {
                f4 = \u26033;
                \u2603 = \u26034;
                \u2603 = f32;
                break;
            }
            case 4: {
                f4 = \u26035;
                \u2603 = \u26033;
                \u2603 = f32;
                break;
            }
            case 5: {
                float f32;
                f4 = f32;
                \u2603 = \u26033;
                \u2603 = \u26034;
                break;
            }
            default: {
                float f5;
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + f5 + ", " + f2 + ", " + f32);
            }
        }
        int \u26036 = MathHelper.clamp((int)(f4 * 255.0f), 0, 255);
        int \u26037 = MathHelper.clamp((int)(\u2603 * 255.0f), 0, 255);
        int \u26038 = MathHelper.clamp((int)(\u2603 * 255.0f), 0, 255);
        return \u26036 << 16 | \u26037 << 8 | \u26038;
    }

    public static int hash(int n) {
        n ^= n >>> 16;
        n *= -2048144789;
        n ^= n >>> 13;
        n *= -1028477387;
        n ^= n >>> 16;
        return n;
    }

    static {
        int n;
        SQRT_2 = MathHelper.sqrt(2.0f);
        RANDOM = new Random();
        SIN_TABLE = new float[65536];
        for (n = 0; n < 65536; ++n) {
            MathHelper.SIN_TABLE[n] = (float)Math.sin((double)((double)n * Math.PI * 2.0 / 65536.0));
        }
        MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
        FRAC_BIAS = Double.longBitsToDouble((long)4805340802404319232L);
        ASINE_TAB = new double[257];
        COS_TAB = new double[257];
        for (n = 0; n < 257; ++n) {
            double d = (double)n / 256.0;
            \u2603 = Math.asin((double)d);
            MathHelper.COS_TAB[n] = Math.cos((double)\u2603);
            MathHelper.ASINE_TAB[n] = \u2603;
        }
    }
}
