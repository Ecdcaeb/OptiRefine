package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.cem.CemPartVisibility;
import net.optifine.entity.model.anim.IModelResolver;
import net.optifine.expr.ExpressionParser;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.ParseException;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.optifine.entity.model.anim.ModelVariableUpdater", remap = false)
public abstract class MixinCemPartVariableUpdater {
    @Shadow private String modelVariableName;
    @Shadow private String expressionText;
    @Unique private CemPartVisibility optiRefine$visibilityTarget;
    @Unique private IExpressionBool optiRefine$visibilityExpression;

    @Inject(method = "initialize", at = @At("HEAD"), cancellable = true)
    private void optiRefine$initializeVisibility(IModelResolver resolver, CallbackInfoReturnable<Boolean> cir) {
        optiRefine$visibilityTarget = null;
        optiRefine$visibilityExpression = null;
        if (!CemPartVisibility.matches(modelVariableName)) {
            return;
        }
        if (!(resolver.getExpression(modelVariableName) instanceof CemPartVisibility target)) {
            LogManager.getLogger("OptiRefine/CEM").warn("Model part visibility variable not available: {}", modelVariableName);
            cir.setReturnValue(false);
            return;
        }
        try {
            optiRefine$visibilityExpression = new ExpressionParser(resolver).parseBool(expressionText);
            optiRefine$visibilityTarget = target;
            cir.setReturnValue(true);
        } catch (ParseException exception) {
            LogManager.getLogger("OptiRefine/CEM").warn("Error parsing CEM expression {}: {}", expressionText, exception.getMessage());
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void optiRefine$updateVisibility(CallbackInfo ci) {
        if (optiRefine$visibilityTarget != null) {
            optiRefine$visibilityTarget.set(optiRefine$visibilityExpression.eval());
            ci.cancel();
        }
    }
}
