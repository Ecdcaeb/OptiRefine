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
            if (Config.isShaders()) {
                Shaders.beginBeacon();
            }
            original.call(x, y, z, partialTicks, textureScale, beamSegments, totalWorldTime);
            if (Config.isShaders()) {
                Shaders.endBeacon();
            }

        }
    }

    @WrapOperation(method = "renderBeamSegment(DDDDDDII[FDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;depthMask(Z)V"))
    // [AUDIT-ISSUE] no ordinal -> applies isBeaconBeamDepth() override to ALL THREE depthMask calls in renderBeamSegment (start true, mid false, end true); OF only overrides the depthMask(false) at OF:108-110 — restrict to ordinal 1 (the false call) or only override when flag==false
    private static void depthMask(boolean flagIn, Operation<Void> original){
        original.call(flagIn);
        if (Config.isShaders()) {
            GlStateManager.depthMask(Shaders.isBeaconBeamDepth());
        }
    }


}
