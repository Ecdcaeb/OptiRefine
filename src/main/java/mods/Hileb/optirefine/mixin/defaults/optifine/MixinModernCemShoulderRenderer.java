package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerEntityOnShoulder.class)
public abstract class MixinModernCemShoulderRenderer {
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderedEntity Lnet.minecraft.entity.Entity;")
    private static native Entity optiRefine$getRenderedEntity(RenderGlobal renderGlobal);

    @WrapOperation(method = "renderEntityOnShoulder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void optiRefine$shoulderContext(ModelBase model, Entity renderArgument, float limbSwing,
                                           float limbSpeed, float age, float headYaw, float headPitch,
                                           float scale, Operation<Void> original) {
        Entity shoulder = optiRefine$getRenderedEntity(Config.getRenderGlobal());
        CemRenderContext.push(shoulder, renderArgument.world, CemRenderContext.partialTicks());
        try {
            original.call(model, renderArgument, limbSwing, limbSpeed, age, headYaw, headPitch, scale);
        } finally {
            CemRenderContext.pop();
        }
    }
}
