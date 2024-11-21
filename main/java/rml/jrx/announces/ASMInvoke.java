package rml.jrx.announces;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/5/11 17:07
 *
 * A class to node a class that referenced in ASM codes.
 **/
@PublicAPI
@EarlyClass
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
public @interface ASMInvoke {

}
