package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.cem.CemEntityVariable;
import mods.Hileb.optirefine.optifine.cem.CemRenderParameter;
import net.optifine.entity.model.anim.IRenderResolver;
import net.optifine.entity.model.anim.RenderResolverTileEntity;
import net.optifine.expr.IExpression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.optifine.entity.model.anim.ModelResolver", remap = false)
public abstract class MixinModernCemModelResolver {
    @Shadow
    private IRenderResolver renderResolver;

    @Inject(method = "getExpression", at = @At("HEAD"), cancellable = true)
    private void optiRefine$modernParameter(String name, CallbackInfoReturnable<IExpression> cir) {
        boolean tileEntity = renderResolver instanceof RenderResolverTileEntity;
        if (CemEntityVariable.isVariable(name)) {
            // Reserved namespaces cannot fall through to an unrelated model named var/varb.
            cir.setReturnValue(tileEntity ? null : CemEntityVariable.resolve(name));
            return;
        }
        IExpression parameter = CemRenderParameter.resolve(name, tileEntity);
        if (parameter != null) {
            cir.setReturnValue(parameter);
        }
    }
}
