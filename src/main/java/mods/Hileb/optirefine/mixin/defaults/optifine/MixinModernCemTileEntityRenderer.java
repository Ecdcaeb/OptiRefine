package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TileEntityRendererDispatcher.class)
public abstract class MixinModernCemTileEntityRenderer {
    @WrapMethod(method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V")
    private void optiRefine$tileContext(TileEntity tileEntity, double x, double y, double z,
                                       float partialTicks, int destroyStage, float alpha, Operation<Void> original) {
        CemRenderContext.push(null, tileEntity.getWorld(), partialTicks);
        try {
            original.call(tileEntity, x, y, z, partialTicks, destroyStage, alpha);
        } finally {
            CemRenderContext.pop();
        }
    }
}
