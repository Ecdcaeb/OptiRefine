package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SignatureFix {
    String value();
}
