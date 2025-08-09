package mods.Hileb.optirefine.mixin.minecraft.client.renderer.culling;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.renderer.culling.ClippingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClippingHelper.class)
public abstract class MixinClippingHelper {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public boolean disabled = false;

    @Shadow
    public float[][] frustum;

    @WrapMethod(method = "isBoxInFrustum")
    public boolean optional_isBoxInFrustum(double d, double e, double f, double g, double h, double i, Operation<Boolean> original){
        if (disabled) {
            return true;
        } else  return original.call(d, e, f, g, h, i);
    }


    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (this.disabled) {
            return true;
        } else {
            float minXf = (float)minX;
            float minYf = (float)minY;
            float minZf = (float)minZ;
            float maxXf = (float)maxX;
            float maxYf = (float)maxY;
            float maxZf = (float)maxZ;

            for (int i = 0; i < 6; i++) {
                float[] frustumi = this.frustum[i];
                float frustumi0 = frustumi[0];
                float frustumi1 = frustumi[1];
                float frustumi2 = frustumi[2];
                float frustumi3 = frustumi[3];
                if (i < 4) {
                    if (frustumi0 * minXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0F
                            || frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0F
                            || frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0F
                            || frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0F
                            || frustumi0 * minXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                            || frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                            || frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                            || frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0F) {
                        return false;
                    }
                } else if (frustumi0 * minXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0F
                        && frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0F
                        && frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0F
                        && frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0F
                        && frustumi0 * minXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                        && frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                        && frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                        && frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0F) {
                    return false;
                }
            }

            return true;
        }
    }

}
