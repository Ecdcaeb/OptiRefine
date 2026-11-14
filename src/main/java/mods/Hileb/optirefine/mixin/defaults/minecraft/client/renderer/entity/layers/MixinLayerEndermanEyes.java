package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LayerEndermanEyes.class)
public abstract class MixinLayerEndermanEyes {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/monster/EntityEnderman;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelEnderman;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    // [AUDIT-OK] ModelEnderman.render INVOKE unique; begin/endSpiderEyes + renderOverlayEyes match OF LayerEndermanEyes:33-41 (OF reuses beginSpiderEyes for enderman)
    public void optiRefine$renderShaderAwareEndermanEyes(ModelEnderman model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original){
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
