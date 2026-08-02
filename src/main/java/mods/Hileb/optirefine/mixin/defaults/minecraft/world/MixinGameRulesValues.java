package mods.Hileb.optirefine.mixin.defaults.minecraft.world;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(targets = "net.minecraft.world.GameRules$Value")
public abstract class MixinGameRulesValues {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


    @SuppressWarnings("unused")
    @Shadow
// [AUDIT-OK] baseline member valueString exists in GameRules$Value
    private String valueString;

    @SuppressWarnings("unused")
    @Shadow
// [AUDIT-OK] baseline member valueBoolean exists in GameRules$Value
    private boolean valueBoolean;

// [AUDIT-OK] setValue(Ljava/lang/String;)V matches baseline; HEAD+cancel replicates OF early-return for "true"/"false" (valueInteger/valueDouble left stale, same as OF)
    @Inject(method = "setValue", at = @At("HEAD"), cancellable = true)
    public void __setValue(String value, CallbackInfo ci){
        this.valueString = value;
        if (value != null) {
            if ("false".equals(value)) {
                this.valueBoolean = false;
                ci.cancel();
            }
            if ("true".equals(value)) {
                this.valueBoolean = true;
                ci.cancel();
            }
        }
    }

}
