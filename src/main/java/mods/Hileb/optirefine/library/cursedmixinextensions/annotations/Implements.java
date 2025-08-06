package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;

@SuppressWarnings("unused")
public @interface Implements {
    Class<?>[] value() default {};
    String[] itfs() default {};

}
