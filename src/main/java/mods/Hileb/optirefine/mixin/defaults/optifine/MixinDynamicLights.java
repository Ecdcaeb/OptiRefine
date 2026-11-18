package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.DynamicLightSample;
import net.minecraft.util.math.BlockPos;
import net.optifine.DynamicLight;
import net.optifine.DynamicLights;
import net.optifine.DynamicLightsMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = DynamicLights.class, remap = false)
public abstract class MixinDynamicLights {
    @Shadow private static DynamicLightsMap mapDynamicLights;
    @Unique private static final DynamicLightSample[] optiRefine$NO_LIGHTS = new DynamicLightSample[0];
    @Unique private static volatile DynamicLightSample[] optiRefine$lights = optiRefine$NO_LIGHTS;

    // The first return is the throttle. The other returns follow a lighting update, including an empty map.
    @Inject(method = "update", at = {@At(value = "RETURN", ordinal = 1), @At(value = "RETURN", ordinal = 2)}, require = 2)
    private static void optiRefine$publishUpdatedLights(CallbackInfo ci) {
        optiRefine$publishLights();
    }

    @Inject(method = {"entityRemoved", "removeLights", "clear"}, at = @At("RETURN"))
    private static void optiRefine$publishRemovedLights(CallbackInfo ci) {
        optiRefine$publishLights();
    }

    @Unique
    private static void optiRefine$publishLights() {
        synchronized (mapDynamicLights) {
            if (mapDynamicLights.size() == 0) {
                optiRefine$lights = optiRefine$NO_LIGHTS;
                return;
            }
            List<DynamicLight> live = mapDynamicLights.valueList();
            DynamicLightSample[] previous = optiRefine$lights;
            DynamicLightSample[] next = previous.length == live.size() ? null : new DynamicLightSample[live.size()];
            for (int i = 0; i < live.size(); i++) {
                DynamicLight light = live.get(i);
                DynamicLightSample sample = i < previous.length ? previous[i] : null;
                if (sample == null || !sample.matches(light)) {
                    if (next == null) next = previous.clone();
                    sample = new DynamicLightSample(light);
                }
                if (next != null) next[i] = sample;
            }
            if (next != null) optiRefine$lights = next;
        }
    }

    /**
     * @author OptiRefine
     * @reason Apply G5's light falloff without taking the update lock on chunk workers.
     */
    @Overwrite
    public static double getLightLevel(BlockPos pos) {
        double strongest = 0.0D;
        for (DynamicLightSample light : optiRefine$lights) {
            int level = light.level();
            if (level <= 0) continue;
            double x = pos.getX() - light.x();
            double y = pos.getY() - light.y();
            double z = pos.getZ() - light.z();
            double distanceSquared = x * x + y * y + z * z;
            if (light.underwater() && !Config.isClearWater()) {
                level = Config.limit(level - 2, 0, 15);
                distanceSquared *= 2.0D;
            }
            if (distanceSquared > 56.25D) continue;
            double contribution = (1.0D - Math.sqrt(distanceSquared) / 7.5D) * level;
            if (contribution > strongest) strongest = contribution;
        }
        return Config.limit(strongest, 0.0D, 15.0D);
    }
}
