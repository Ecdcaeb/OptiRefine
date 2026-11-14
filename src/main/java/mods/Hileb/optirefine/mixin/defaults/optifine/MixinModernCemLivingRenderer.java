package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderLivingBase.class)
public abstract class MixinModernCemLivingRenderer {
    @WrapMethod(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V")
    private void optiRefine$livingContext(EntityLivingBase entity, double x, double y, double z,
                                         float yaw, float partialTicks, Operation<Void> original) {
        CemRenderContext.push(entity, entity.world, partialTicks);
        try {
            original.call(entity, x, y, z, yaw, partialTicks);
        } finally {
            CemRenderContext.pop();
        }
    }
}
