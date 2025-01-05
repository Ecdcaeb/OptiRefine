package mods.Hileb.optirefine.mixinx.cursedmixinextensions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Change the super class of the annotated class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeSuperClass {
    /**
     * The super class to use.
     */
    Class<?> value() default Object.class;
    String name() default "";
    boolean remap() default true;
}
