package mods.Hileb.optirefine.mixin.defaults.minecraft.util.math;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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
@Mixin(MathHelper.class)
public abstract class MixinMathHelper {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1


    @SuppressWarnings("unused")
    @Shadow @Final
    @Public
// [AUDIT-OK] baseline member SQRT_2 exists in target class (@Public on shadow = no-op)
    private static float SQRT_2;
    @SuppressWarnings("unused")
// [AUDIT-OK] OF-added constant SIN_BITS (in OF MathHelper, not in baseline)
    private static final int SIN_BITS = 12;
    @SuppressWarnings("unused")
    @Unique
// [AUDIT-OK] OF-added constant SIN_MASK (in OF MathHelper, not in baseline)
    private static final int SIN_MASK = 4095;
    @SuppressWarnings("unused")
// [AUDIT-OK] OF-added constant SIN_COUNT (in OF MathHelper, not in baseline)
    private static final int SIN_COUNT = 4096;
    @SuppressWarnings("unused")
    @Unique
// [AUDIT-OK] OF-added constant SIN_COUNT_D4 (in OF MathHelper, not in baseline)
    private static final int SIN_COUNT_D4 = 1024;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added constant PI (in OF MathHelper, not in baseline); private static final + @Public per rules
    private static final float PI = MathUtils.roundToFloat(Math.PI);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added constant PI2 (in OF MathHelper, not in baseline)
    private static final float PI2 = MathUtils.roundToFloat(Math.PI * 2);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added constant PId2 (in OF MathHelper, not in baseline)
    private static final float PId2 = MathUtils.roundToFloat(1.5707963267948966);
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added constant radToIndex (in OF MathHelper, not in baseline; private in OF, public here is harmless)
    private static final float radToIndex = MathUtils.roundToFloat(651.8986469044033);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added constant deg2Rad (in OF MathHelper, not in baseline)
    private static final float deg2Rad = MathUtils.roundToFloat(Math.PI / 180);
    @Public
// [AUDIT-OK] OF-added field SIN_TABLE_FAST (in OF MathHelper, not in baseline; OF: private static final, filled in <clinit>) - filled here via <clinit> inject
    private static float[] SIN_TABLE_FAST;
    @SuppressWarnings("MissingUnique")
    @AccessTransformer(access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL)
// [AUDIT-ISSUE] @AccessTransformer has no name attribute - name defaults to the placeholder "_ACC_SIN_TABLE_FAST", which is removed before matching, so the AT matches nothing and is a silent no-op
// (SIN_TABLE_FAST stays public non-final via @Public; intended ACC_PUBLIC|ACC_STATIC|ACC_FINAL never applied). Fix: add name = "SIN_TABLE_FAST" or remove the dead AT.
    private static float[] _ACC_SIN_TABLE_FAST;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added field fastMath (in OF MathHelper, not in baseline; public in OF)
    private static boolean fastMath = false;
    @Shadow @Final
// [AUDIT-OK] baseline member SIN_TABLE exists in target class
    private static float[] SIN_TABLE;
    @SuppressWarnings("unused")
    @Shadow @Final
// [AUDIT-OK] baseline member RANDOM exists in target class
    private static Random RANDOM;
    @SuppressWarnings("unused")
    @Shadow @Final
// [AUDIT-OK] baseline member MULTIPLY_DE_BRUIJN_BIT_POSITION exists in target class
    private static int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    @SuppressWarnings("unused")
    @Shadow @Final
// [AUDIT-OK] baseline member FRAC_BIAS exists in target class
    private static double FRAC_BIAS;
    @SuppressWarnings("unused")
    @Shadow @Final
// [AUDIT-OK] baseline member ASINE_TAB exists in target class
    private static double[] ASINE_TAB;
    @SuppressWarnings("unused")
    @Shadow @Final
// [AUDIT-OK] baseline member COS_TAB exists in target class
    private static double[] COS_TAB;

    @WrapMethod(method = "sin")
// [AUDIT-OK] sin fast path matches OF (SIN_TABLE_FAST[(int)(v*radToIndex)&0xFFF], else vanilla path)
    private static float sin(float value, Operation<Float> original) {
        if (fastMath) {
            return SIN_TABLE_FAST[(int)(value * radToIndex) & 0xFFF];
        }
        return original.call(value);
    }

    @WrapMethod(method = "cos")
// [AUDIT-OK] cos fast path matches OF (SIN_TABLE_FAST[(int)(v*radToIndex+1024f)&0xFFF], else vanilla path)
    private static float cos(float value, Operation<Float> original) {
        if (fastMath) {
            return SIN_TABLE_FAST[(int)(value * radToIndex + 1024.0f) & 0xFFF];
        }
        return original.call(value);
    }

    @Inject(method = "<clinit>", at = @At("HEAD"))
// [AUDIT-OK] <clinit> HEAD fill of SIN_TABLE_FAST matches OF <clinit> (static handler for static target)
    private static void inject_static(CallbackInfo ci){
        SIN_TABLE_FAST = new float[4096];
        for (int s = 0; s < SIN_TABLE_FAST.length; ++s) {
            SIN_TABLE_FAST[s] = MathUtils.roundToFloat(Math.sin((double)s * Math.PI * 2.0 / 4096.0));
        }
    }


}
