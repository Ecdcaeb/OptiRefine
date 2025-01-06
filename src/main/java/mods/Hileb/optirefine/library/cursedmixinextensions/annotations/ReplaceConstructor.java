package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Overwrite for constructor methods.
 * @see org.spongepowered.asm.mixin.Overwrite
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReplaceConstructor {
}
