package mods.Hileb.optirefine.mixin.minecraft.client.renderer.tileentity;

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

    @WrapMethod(method = "renderBeacon")
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
    public void depthMask(boolean flagIn, Operation<Void> original){
        if (!flagIn) {
            GlStateManager.depthMask(false);
            if (Config.isShaders()) {
                GlStateManager.depthMask(Shaders.isBeaconBeamDepth());
            }
        } else original.call(true);
    }


}
