package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.shaders.modern.ShaderBiomeExpressions;
import net.optifine.expr.IExpression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(targets = "net.optifine.shaders.uniform.ShaderExpressionResolver", remap = false)
public abstract class MixinShaderBiomeExpressions {
    @Shadow private Map<String, IExpression> mapExpressions;

    @Inject(method = "<init>(Ljava/util/Map;)V", at = @At("RETURN"))
    private void optiRefine$registerBiomeExpressions(Map<String, IExpression> customExpressions, CallbackInfo ci) {
        ShaderBiomeExpressions.register(mapExpressions);
    }
}
