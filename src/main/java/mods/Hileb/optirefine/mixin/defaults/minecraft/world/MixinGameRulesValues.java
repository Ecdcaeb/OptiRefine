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

    @Shadow
    private int valueInteger;

    // Avoid parsing boolean literals while preserving the baseline integer representation.
    @Inject(method = "setValue", at = @At("HEAD"), cancellable = true)
    public void optiRefine$parseBooleanLiteral(String value, CallbackInfo ci){
        this.valueString = value;
        if (value != null) {
            if ("false".equals(value)) {
                this.valueBoolean = false;
                this.valueInteger = 0;
                ci.cancel();
            }
            if ("true".equals(value)) {
                this.valueBoolean = true;
                this.valueInteger = 1;
                ci.cancel();
            }
        }
    }

}
