package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.optifine.reflect.ReflectorClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Uses ReflectorClass's registered string form so its reflective resolution is deferred.
 * The Class argument is still loaded by the caller; ReflectorForge's initializer separately
 * avoids entering Reflector during early client bootstrap.
 */
@Mixin(targets = "net.optifine.reflect.Reflector", remap = false)
public abstract class MixinReflector {

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/Class;)Lnet/optifine/reflect/ReflectorClass;"
            )
    )
    private static ReflectorClass optiRefine$lazyReflectorClass(Class<?> targetClass) {
        return new ReflectorClass(targetClass.getName());
    }
}
