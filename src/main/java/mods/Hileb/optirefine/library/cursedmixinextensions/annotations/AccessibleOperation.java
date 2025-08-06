package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;


public @interface AccessibleOperation {
    int opcode() default 0;

    String desc() default "";

    @SuppressWarnings("unused") boolean itf() default false;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted") boolean deobf() default false;
}
