package mods.Hileb.optirefine.mixin.defaults.minecraft.util.math;

import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.util.math.MathHelper;
import net.optifine.util.MathUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Checked
@Mixin(MathHelper.class)
public abstract class MixinMathHelper {

    @SuppressWarnings("unused")
    @Shadow @Final
    public static float SQRT_2;
    @SuppressWarnings("unused")
    @Unique
    private static final int SIN_BITS = 12;
    @SuppressWarnings("unused")
    @Unique
    private static final int SIN_MASK = 4095;
    @SuppressWarnings("unused")
    @Unique
    private static final int SIN_COUNT = 4096;
    @SuppressWarnings("unused")
    @Unique
    private static final int SIN_COUNT_D4 = 1024;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private static final float PI = MathUtils.roundToFloat(Math.PI);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private static final float PI2 = MathUtils.roundToFloat(Math.PI * 2);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private static final float PId2 = MathUtils.roundToFloat(1.5707963267948966);
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    private static final float radToIndex = MathUtils.roundToFloat(651.8986469044033);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private static final float deg2Rad = MathUtils.roundToFloat(Math.PI / 180);
    @Unique @Public
    private static float[] SIN_TABLE_FAST;
    @SuppressWarnings("MissingUnique")
    @AccessTransformer(access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL)
    private static float[] _ACC_SIN_TABLE_FAST;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    private static boolean fastMath = false;
    @Shadow @Final
    private static float[] SIN_TABLE;
    @SuppressWarnings("unused")
    @Shadow @Final
    private static Random RANDOM;
    @SuppressWarnings("unused")
    @Shadow @Final
    private static int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    @SuppressWarnings("unused")
    @Shadow @Final
    private static double FRAC_BIAS;
    @SuppressWarnings("unused")
    @Shadow @Final
    private static double[] ASINE_TAB;
    @SuppressWarnings("unused")
    @Shadow @Final
    private static double[] COS_TAB;

    /**
     * @author Hileb
     * @reason ov
     */
    @Overwrite
    public static float sin(float value) {
        if (fastMath) {
            return SIN_TABLE_FAST[(int)(value * radToIndex) & 0xFFF];
        }
        return SIN_TABLE[(int)(value * 10430.378f) & 0xFFFF];
    }

    /**
     * @author Hileb
     * @reason ov
     */
    @Overwrite
    public static float cos(float value) {
        if (fastMath) {
            return SIN_TABLE_FAST[(int)(value * radToIndex + 1024.0f) & 0xFFF];
        }
        return SIN_TABLE[(int)(value * 10430.378f + 16384.0f) & 0xFFFF];
    }

    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void inject_static(CallbackInfo ci){
        SIN_TABLE_FAST = new float[4096];
        for (int s = 0; s < SIN_TABLE_FAST.length; ++s) {
            SIN_TABLE_FAST[s] = MathUtils.roundToFloat(Math.sin((double)s * Math.PI * 2.0 / 4096.0));
        }
    }


}

/*
--- net/minecraft/util/math/MathHelper.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/util/math/MathHelper.java	Tue Aug 19 14:59:58 2025
@@ -1,26 +1,38 @@
 package net.minecraft.util.math;

 import java.util.Random;
 import java.util.UUID;
+import net.optifine.util.MathUtils;

 public class MathHelper {
    public static final float SQRT_2 = sqrt(2.0F);
+   private static final int SIN_BITS = 12;
+   private static final int SIN_MASK = 4095;
+   private static final int SIN_COUNT = 4096;
+   private static final int SIN_COUNT_D4 = 1024;
+   public static final float PI = MathUtils.roundToFloat(Math.PI);
+   public static final float PI2 = MathUtils.roundToFloat(Math.PI * 2);
+   public static final float PId2 = MathUtils.roundToFloat(Math.PI / 2);
+   private static final float radToIndex = MathUtils.roundToFloat(651.8986469044033);
+   public static final float deg2Rad = MathUtils.roundToFloat(Math.PI / 180.0);
+   private static final float[] SIN_TABLE_FAST = new float[4096];
+   public static boolean fastMath = false;
    private static final float[] SIN_TABLE = new float[65536];
    private static final Random RANDOM = new Random();
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    private static final double FRAC_BIAS;
    private static final double[] ASINE_TAB;
    private static final double[] COS_TAB;

    public static float sin(float var0) {
-      return SIN_TABLE[(int)(var0 * 10430.378F) & 65535];
+      return fastMath ? SIN_TABLE_FAST[(int)(var0 * radToIndex) & 4095] : SIN_TABLE[(int)(var0 * 10430.378F) & 65535];
    }

    public static float cos(float var0) {
-      return SIN_TABLE[(int)(var0 * 10430.378F + 16384.0F) & 65535];
+      return fastMath ? SIN_TABLE_FAST[(int)(var0 * radToIndex + 1024.0F) & 4095] : SIN_TABLE[(int)(var0 * 10430.378F + 16384.0F) & 65535];
    }

    public static float sqrt(float var0) {
       return (float)Math.sqrt(var0);
    }

@@ -422,21 +434,25 @@

    static {
       for (int var0 = 0; var0 < 65536; var0++) {
          SIN_TABLE[var0] = (float)Math.sin(var0 * Math.PI * 2.0 / 65536.0);
       }

+      for (int var5 = 0; var5 < SIN_TABLE_FAST.length; var5++) {
+         SIN_TABLE_FAST[var5] = MathUtils.roundToFloat(Math.sin(var5 * Math.PI * 2.0 / 4096.0));
+      }
+
       MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{
          0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
       };
       FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
       ASINE_TAB = new double[257];
       COS_TAB = new double[257];

-      for (int var5 = 0; var5 < 257; var5++) {
-         double var1 = var5 / 256.0;
+      for (int var6 = 0; var6 < 257; var6++) {
+         double var1 = var6 / 256.0;
          double var3 = Math.asin(var1);
-         COS_TAB[var5] = Math.cos(var3);
-         ASINE_TAB[var5] = var3;
+         COS_TAB[var6] = Math.cos(var3);
+         ASINE_TAB[var6] = var3;
       }
    }
 }
 */
