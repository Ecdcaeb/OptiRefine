package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LayerEnderDragonEyes.class)
public abstract class MixinLayerEnderDragonEyes {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/boss/EntityDragon;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    // [AUDIT-OK] ModelBase.render INVOKE unique; begin/endSpiderEyes + renderOverlayEyes match OF LayerEnderDragonEyes:33-41 (OF reuses beginSpiderEyes for dragon)
    public void optiRefine$renderShaderAwareDragonEyes(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original){
        boolean shaders = Config.isShaders();
        net.optifine.shaders.Program oldProgram = shaders ? Shaders.activeProgram : null;
        RenderGlobal renderGlobal = Config.getRenderGlobal();
        boolean overlayEyes = optiRefine$getRenderGlobalRenderOverlayEyes(renderGlobal);
        if (shaders) {
            Shaders.beginSpiderEyes();
        }
        optiRefine$setRenderGlobalRenderOverlayEyes(renderGlobal, true);
        try {
            original.call(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        } finally {
            optiRefine$setRenderGlobalRenderOverlayEyes(renderGlobal, overlayEyes);
            if (shaders) {
                Shaders.endSpiderEyes();
                net.minecraft.client.renderer.GlStateManager.alphaFunc(516, 0.1F);
                Shaders.useProgram(oldProgram);
            }
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderOverlayEyes Z")
    private static native void optiRefine$setRenderGlobalRenderOverlayEyes(RenderGlobal renderGlobal, boolean val);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderOverlayEyes Z")
    private static native boolean optiRefine$getRenderGlobalRenderOverlayEyes(RenderGlobal renderGlobal);
}
