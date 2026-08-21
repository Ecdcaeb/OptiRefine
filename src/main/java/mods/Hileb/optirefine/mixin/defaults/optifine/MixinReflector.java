package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.optifine.reflect.ReflectorClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * OptiFine's {@code Reflector} statics eagerly construct {@code new ReflectorClass(SomeClient.class)},
 * which forces those client classes to load during {@code <clinit>} (e.g. {@code RenderItemFrame}
 * while {@code GameSettings} is still constructing). Redirect Class-literal constructors to the
 * string form so resolution is deferred until first {@code exists()}/{@code getTargetClass()}.
 */
@Mixin(targets = "net.optifine.reflect.Reflector")
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
