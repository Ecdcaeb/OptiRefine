package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.cem.CemEntityVariable;
import net.optifine.entity.model.anim.IModelResolver;
import net.optifine.expr.ExpressionParser;
import net.optifine.expr.IExpression;
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
public abstract class MixinModernCemVariableUpdater {
    @Shadow private String modelVariableName;
    @Shadow private String expressionText;
    @Unique private CemEntityVariable optiRefine$entityVariable;
    @Unique private IExpression optiRefine$entityExpression;

    @Inject(method = "initialize", at = @At("HEAD"), cancellable = true)
    private void optiRefine$initializeEntityVariable(IModelResolver resolver, CallbackInfoReturnable<Boolean> cir) {
        optiRefine$entityVariable = null;
        optiRefine$entityExpression = null;
        if (!CemEntityVariable.isVariable(modelVariableName)) {
            return;
        }
        IExpression target = resolver.getExpression(modelVariableName);
        if (!(target instanceof CemEntityVariable)) {
            LogManager.getLogger("OptiRefine/CEM").warn("Entity model variable not available: {}", modelVariableName);
            cir.setReturnValue(false);
            return;
        }
        try {
            IExpression value = new ExpressionParser(resolver).parse(expressionText);
            if (value == null || value.getExpressionType() != target.getExpressionType()) {
                throw new ParseException("Invalid assignment type for " + modelVariableName + ", expected " + target.getExpressionType());
            }
            optiRefine$entityVariable = (CemEntityVariable) target;
            optiRefine$entityExpression = value;
            cir.setReturnValue(true);
        } catch (ParseException exception) {
            LogManager.getLogger("OptiRefine/CEM").warn("Error parsing CEM expression {}: {}", expressionText, exception.getMessage());
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void optiRefine$updateEntityVariable(CallbackInfo ci) {
        if (optiRefine$entityVariable != null) {
            optiRefine$entityVariable.assign(optiRefine$entityExpression);
            ci.cancel();
        }
    }
}
