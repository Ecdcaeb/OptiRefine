package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.tileentity.TileEntityBeacon;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
@Mixin(TileEntityBeaconRenderer.class)
public abstract class MixinTileEntityBeaconRenderer {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1

    @WrapMethod(method = "renderBeacon")
    // [AUDIT-OK] renderBeacon baseline; textureScale/empty guards + begin/endBeacon match OF:21-43
    public void _renderBeacon(double x, double y, double z, double partialTicks, double textureScale, List<TileEntityBeacon.BeamSegment> beamSegments, double totalWorldTime, Operation<Void> original) {
        if (!(textureScale <= 0.0) && !beamSegments.isEmpty()) {
            boolean shaders = Config.isShaders();
            if (shaders) {
                Shaders.beginBeacon();
            }
            try {
                original.call(x, y, z, partialTicks, textureScale, beamSegments, totalWorldTime);
            } finally {
                if (shaders) {
                    Shaders.endBeacon();
                }
            }

        }
    }

    @WrapOperation(method = "renderBeamSegment(DDDDDDII[FDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;depthMask(Z)V", ordinal = 1))
    // [AUDIT-FIXED] three-way audit (P18): OF only overrides the middle depthMask(false) call (OF:108-110);
    // without ordinal the override hit all three (start true / mid false / end true), wrongly disabling
    // depth writes on the first/last passes when beaconBeamDepth=false.
    private static void depthMask(boolean flagIn, Operation<Void> original){
        original.call(flagIn);
        if (Config.isShaders()) {
            GlStateManager.depthMask(Shaders.isBeaconBeamDepth());
        }
    }


}
