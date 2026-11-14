package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderManager.class)
public abstract class MixinModernCemRenderManager {
    @WrapMethod(method = "renderEntity")
    private void optiRefine$entityContext(Entity entity, double x, double y, double z, float yaw,
                                         float partialTicks, boolean hideDebugBox, Operation<Void> original) {
        CemRenderContext.push(entity, entity.world, partialTicks);
        try {
            original.call(entity, x, y, z, yaw, partialTicks, hideDebugBox);
        } finally {
            CemRenderContext.pop();
        }
    }
}
