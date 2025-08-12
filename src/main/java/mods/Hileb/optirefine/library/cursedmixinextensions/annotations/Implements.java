package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
public @interface Implements {
    Class<?>[] value() default {};
    String[] itfs() default {};


}
