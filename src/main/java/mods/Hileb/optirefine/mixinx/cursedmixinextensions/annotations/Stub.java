package mods.Hileb.optirefine.mixinx.cursedmixinextensions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Stub {
    Class<?> value() default Object.class;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.CLASS)
    @interface Applied {}
}
