package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;

public @interface AccessTransformer {
    int access() default -1;
    String name() default "";
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") boolean deobf() default false;
}
