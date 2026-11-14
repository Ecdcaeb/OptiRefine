package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Initializes Forge results without triggering Reflector's client class resolution. */
@Mixin(targets = "net.optifine.reflect.ReflectorForge", remap = false)
public abstract class MixinReflectorForge {
    @Shadow public static Object EVENT_RESULT_ALLOW;
    @Shadow public static Object EVENT_RESULT_DENY;
    @Shadow public static Object EVENT_RESULT_DEFAULT;

    @Inject(method = "<clinit>", at = @At("HEAD"), cancellable = true)
    private static void optiRefine$initializeEventResults(CallbackInfo ci) {
        EVENT_RESULT_ALLOW = Event.Result.ALLOW;
        EVENT_RESULT_DENY = Event.Result.DENY;
        EVENT_RESULT_DEFAULT = Event.Result.DEFAULT;
        ci.cancel();
    }
}
