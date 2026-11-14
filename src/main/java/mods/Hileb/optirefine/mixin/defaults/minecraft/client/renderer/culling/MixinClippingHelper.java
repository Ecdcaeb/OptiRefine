package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.culling;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.renderer.culling.ClippingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(ClippingHelper.class)
public abstract class MixinClippingHelper {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added field (OF:8 public); nit: '= false' initializer on @Public instance field (AGENT.md §4 convention)
    @Unique
    private boolean disabled;

    @Shadow
    // [AUDIT-OK] baseline member frustum (public float[][], deobf:9)
    public float[][] frustum;

    @WrapMethod(method = "isBoxInFrustum")
    // [AUDIT-OK] isBoxInFrustum baseline; disabled guard matches OF:14-15
    public boolean optiRefine$testBoxWhenClippingEnabled(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Operation<Boolean> original){
        if (disabled) {
            return true;
        } else  return original.call(minX, minY, minZ, maxX, maxY, maxZ);
    }


    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    // [AUDIT-OK] OF-added member — body identical to OF:47-91
    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (this.disabled) {
            return true;
        } else {
            float var13 = (float)minX;
            float var14 = (float)minY;
            float var15 = (float)minZ;
            float var16 = (float)maxX;
            float var17 = (float)maxY;
            float var18 = (float)maxZ;

            for (int var19 = 0; var19 < 6; var19++) {
                float[] var20 = this.frustum[var19];
                float var21 = var20[0];
                float var22 = var20[1];
                float var23 = var20[2];
                float var24 = var20[3];
                if (var19 < 4) {
                    if (var21 * var13 + var22 * var14 + var23 * var15 + var24 <= 0.0F
                            || var21 * var16 + var22 * var14 + var23 * var15 + var24 <= 0.0F
                            || var21 * var13 + var22 * var17 + var23 * var15 + var24 <= 0.0F
                            || var21 * var16 + var22 * var17 + var23 * var15 + var24 <= 0.0F
                            || var21 * var13 + var22 * var14 + var23 * var18 + var24 <= 0.0F
                            || var21 * var16 + var22 * var14 + var23 * var18 + var24 <= 0.0F
                            || var21 * var13 + var22 * var17 + var23 * var18 + var24 <= 0.0F
                            || var21 * var16 + var22 * var17 + var23 * var18 + var24 <= 0.0F) {
                        return false;
                    }
                } else if (var21 * var13 + var22 * var14 + var23 * var15 + var24 <= 0.0F
                        && var21 * var16 + var22 * var14 + var23 * var15 + var24 <= 0.0F
                        && var21 * var13 + var22 * var17 + var23 * var15 + var24 <= 0.0F
                        && var21 * var16 + var22 * var17 + var23 * var15 + var24 <= 0.0F
                        && var21 * var13 + var22 * var14 + var23 * var18 + var24 <= 0.0F
                        && var21 * var16 + var22 * var14 + var23 * var18 + var24 <= 0.0F
                        && var21 * var13 + var22 * var17 + var23 * var18 + var24 <= 0.0F
                        && var21 * var16 + var22 * var17 + var23 * var18 + var24 <= 0.0F) {
                    return false;
                }
            }
            return true;
        }
    }


    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initializeCullingState(CallbackInfo ci) {
        this.disabled = false;
    }
}
