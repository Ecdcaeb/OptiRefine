package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.resources.ConnectedTextureModes;
import net.optifine.ConnectedProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ConnectedProperties.class, remap = false)
public abstract class MixinConnectedProperties {
    @Inject(method = "parseConnect", at = @At("HEAD"), cancellable = true)
    private static void optiRefine$parseStateConnection(String value, CallbackInfoReturnable<Integer> cir) {
        if (value != null && value.trim().equals("state")) cir.setReturnValue(ConnectedTextureModes.STATE);
    }
}
