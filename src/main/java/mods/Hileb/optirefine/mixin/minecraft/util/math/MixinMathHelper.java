package mods.Hileb.optirefine.mixin.minecraft.util.math;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.util.math.MathHelper;
import net.optifine.util.MathUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(MathHelper.class)
public abstract class MixinMathHelper {

    @Shadow @Final
    public static float SQRT_2;
    @Unique
    private static final int SIN_BITS = 12;
    @Unique
    private static final int SIN_MASK = 4095;
    @Unique
    private static final int SIN_COUNT = 4096;
    @Unique
    private static final int SIN_COUNT_D4 = 1024;
    @Unique @Public
    private static final float PI = MathUtils.roundToFloat(Math.PI);
    @Unique @Public
    private static final float PI2 = MathUtils.roundToFloat(Math.PI * 2);
    @Unique @Public
    private static final float PId2 = MathUtils.roundToFloat(1.5707963267948966);
    @Unique @Public
    private static final float radToIndex = MathUtils.roundToFloat(651.8986469044033);
    @Unique @Public
    private static final float deg2Rad = MathUtils.roundToFloat(Math.PI / 180);
    @Unique @Public
    private static final float[] SIN_TABLE_FAST = new float[4096];
    @Unique @Public
    private static final boolean fastMath = false;
    @Shadow @Final
    private static float[] SIN_TABLE;
    @Shadow @Final
    private static Random RANDOM;
    @Shadow @Final
    private static int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    @Shadow @Final
    private static double FRAC_BIAS;
    @Shadow @Final
    private static double[] ASINE_TAB;
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
        for (int s = 0; s < SIN_TABLE_FAST.length; ++s) {
            SIN_TABLE_FAST[s] = MathUtils.roundToFloat(Math.sin((double)s * Math.PI * 2.0 / 4096.0));
        }
    }


}
