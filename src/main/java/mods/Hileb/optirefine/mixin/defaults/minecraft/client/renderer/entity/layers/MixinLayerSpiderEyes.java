package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(LayerSpiderEyes.class)
public abstract class MixinLayerSpiderEyes {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/monster/EntitySpider;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    // [AUDIT-OK] ModelBase.render INVOKE unique; begin/endSpiderEyes + renderOverlayEyes=true/false match OF:33-44
    public void wrap_render(ModelBase instance, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original){
        boolean shaders = Config.isShaders();
        net.optifine.shaders.Program oldProgram = shaders ? Shaders.activeProgram : null;
        RenderGlobal renderGlobal = Config.getRenderGlobal();
        boolean overlayEyes = RenderGlobal_renderOverlayEyes_get(renderGlobal);
        if (shaders) {
            Shaders.beginSpiderEyes();
        }
        RenderGlobal_renderOverlayEyes_set(renderGlobal, true);
        try {
            original.call(instance, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        } finally {
            RenderGlobal_renderOverlayEyes_set(renderGlobal, overlayEyes);
            if (shaders) {
                Shaders.endSpiderEyes();
                net.minecraft.client.renderer.GlStateManager.alphaFunc(516, 0.1F);
                Shaders.useProgram(oldProgram);
            }
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderOverlayEyes Z")
    private static native void RenderGlobal_renderOverlayEyes_set(RenderGlobal renderGlobal, boolean val);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderOverlayEyes Z")
    private static native boolean RenderGlobal_renderOverlayEyes_get(RenderGlobal renderGlobal);

    @Inject(method = "doRenderLayer(Lnet/minecraft/entity/monster/EntitySpider;FFFFFFF)V", at = @At("RETURN"))
    private void optiRefine$restoreDepthMask(CallbackInfo ci) {
        net.minecraft.client.renderer.GlStateManager.depthMask(true);
    }
}
