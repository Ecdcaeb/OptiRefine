package mods.Hileb.optirefine.library.cursedmixinextensions.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Supplies the generated JVM method descriptor when a return type is inaccessible to source code.
 * Arguments remain unchanged; reference returns are checked against the generated return type.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SignatureFix {
    String value();
}
