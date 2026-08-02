package mods.Hileb.optirefine.mixin.defaults.minecraft.world;

import mods.Hileb.optirefine.library.common.utils.Checked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Checked
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
