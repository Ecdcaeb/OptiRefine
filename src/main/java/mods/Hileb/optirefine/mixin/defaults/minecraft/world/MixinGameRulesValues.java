package mods.Hileb.optirefine.mixin.defaults.minecraft.world;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.GameRules$Value")
public abstract class MixinGameRulesValues {

    @SuppressWarnings("unused")
    @Shadow
    private String valueString;

    @SuppressWarnings("unused")
    @Shadow
    private boolean valueBoolean;

    @Inject(method = "setValue", at = @At("HEAD"), cancellable = true)
    public void __setValue(String value, CallbackInfo ci){
        this.valueString = value;
        if (value != null) {
            if (value.equals("false")) {
                this.valueBoolean = false;
                ci.cancel();
            }
            if (value.equals("true")) {
                this.valueBoolean = true;
                ci.cancel();
            }
        }
    }

}
