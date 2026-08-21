package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.optifine.reflect.ReflectorField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * {@code ReflectorForge} statics pull {@code Reflector.Event_Result_*} during {@code <clinit>},
 * which forces {@code Reflector} (and its client Class literals) to initialize too early.
 * Drop those eager field reads; {@code canEntitySpawn} already treats a null result as deny/default.
 */
@Mixin(targets = "net.optifine.reflect.ReflectorForge")
public abstract class MixinReflectorForge {

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/optifine/reflect/Reflector;getFieldValue(Lnet/optifine/reflect/ReflectorField;)Ljava/lang/Object;"
            )
    )
    private static Object optiRefine$skipEagerEventResult(ReflectorField field) {
        return null;
    }
}
