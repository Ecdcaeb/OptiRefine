package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.culling;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.renderer.culling.ClippingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
@Mixin(ClippingHelper.class)
public abstract class MixinClippingHelper {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added field (OF:8 public); nit: '= false' initializer on @Public instance field (AGENT.md §4 convention)
    public boolean disabled = false;

    @Shadow
    // [AUDIT-OK] baseline member frustum (public float[][], deobf:9)
    public float[][] frustum;

    @WrapMethod(method = "isBoxInFrustum")
    // [AUDIT-OK] isBoxInFrustum baseline; disabled guard matches OF:14-15
    public boolean optional_isBoxInFrustum(double d, double e, double f, double g, double h, double i, Operation<Boolean> original){
        if (disabled) {
            return true;
        } else  return original.call(d, e, f, g, h, i);
    }


    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    // [AUDIT-OK] OF-added member — body identical to OF:47-91
    public boolean isBoxInFrustumFully(double var1, double var3, double var5, double var7, double var9, double var11) {
        if (this.disabled) {
            return true;
        } else {
            float var13 = (float)var1;
            float var14 = (float)var3;
            float var15 = (float)var5;
            float var16 = (float)var7;
            float var17 = (float)var9;
            float var18 = (float)var11;

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

}
