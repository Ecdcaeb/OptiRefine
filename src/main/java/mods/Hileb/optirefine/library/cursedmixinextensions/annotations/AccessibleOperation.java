package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;

@SuppressWarnings("all")
public @interface AccessibleOperation {
    int opcode() default 0;

    String desc() default "";

    boolean itf() default false;

    boolean deobf() default false;
}
