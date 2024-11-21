package rml.jrx.announces;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/14 17:50
 *
 * A class could be discovered, loaded and initialized in pre-init.
 **/
@EarlyClass
@Documented
@PrivateAPI
@Retention(RetentionPolicy.RUNTIME)
@Target(value={TYPE})
public @interface BeDiscovered {
    String value();

    String PRE_INIT = "preInit";
    String MOD_LOADING = "modLoading";

}
