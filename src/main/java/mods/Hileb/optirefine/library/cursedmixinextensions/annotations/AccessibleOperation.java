package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;

import org.spongepowered.asm.mixin.injection.Desc;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
* NOP
*   NOP, invoke will be removed
*   usually -> private native Target _cast_this();
*
* INVOKE
*  invoke the method
*  usually     -> private static native R foo(instance, args)
*              -> private static native R foo(args)
*  specifically-> public R foo(args)
*
* FIELD
*  usually -> private static native void _xx_set(instance, value)
*             private static native void _xx_set(value)
*  specifically-> public native void _xx_set(value)
*
* NEW
*  usually -> private static native TYPE _new_Type(AccessibleOperation.Construction construction, args...)
*
* CHECKCAST
*  usually -> private static native TYPE_1 cast(TYPE_0)
* */
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessibleOperation {
    int opcode() default 0;

    String desc() default "";

    @SuppressWarnings("unused") boolean itf() default false;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted") boolean deobf() default false;

    class Construction {
        public static Construction construction(){
            return null;
        }
    }

    @Repeatable(Reference.References.class)
    @Retention(RetentionPolicy.SOURCE)
    @interface Reference{
        Class<?> value();
        Desc desc() default @Desc("");

        @Retention(RetentionPolicy.SOURCE)
        @interface References{
            Reference[] value();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface BuildIn{}

}
