package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.cem.CemPartVisibility;
import net.minecraft.client.model.ModelRenderer;
import net.optifine.expr.IExpression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.optifine.entity.model.anim.ModelResolver", remap = false)
public abstract class MixinCemPartVariables {
    @Shadow public abstract ModelRenderer getModelRenderer(String name);

    @Inject(method = "getExpression", at = @At("HEAD"), cancellable = true)
    private void optiRefine$resolvePartVisibility(String name, CallbackInfoReturnable<IExpression> cir) {
        if (CemPartVisibility.matches(name)) {
            ModelRenderer part = getModelRenderer(name.substring(0, name.indexOf('.')));
            cir.setReturnValue(part == null ? null : new CemPartVisibility(part, name.endsWith(".visible_boxes")));
        }
    }
}
