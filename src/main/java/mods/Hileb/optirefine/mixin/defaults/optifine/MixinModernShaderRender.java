package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.shaders.modern.ModernShaderPipeline;
import net.minecraft.client.renderer.EntityRenderer;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.optifine.shaders.ShadersRender", remap = false)
public abstract class MixinModernShaderRender {
    @Inject(method = "renderShadowMap", at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/Shaders;useProgram(Lnet/optifine/shaders/Program;)V", ordinal = 0))
    private static void optiRefine$shadowCompute(EntityRenderer renderer, int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        ModernShaderPipeline.dispatch(Shaders.ProgramShadow);
    }

    @Inject(method = "renderShadowMap", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/EXTFramebufferObject;glBindFramebufferEXT(II)V", ordinal = 1))
    private static void optiRefine$shadowComposite(EntityRenderer renderer, int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        ModernShaderPipeline.renderShadowcomp();
    }
}
