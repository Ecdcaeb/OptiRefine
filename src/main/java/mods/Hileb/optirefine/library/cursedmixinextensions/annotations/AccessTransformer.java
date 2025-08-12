package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AccessTransformer {
    int access() default -1;
    String name() default "";
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") boolean deobf() default false;
}
