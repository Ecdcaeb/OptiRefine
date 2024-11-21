package rml.jrx.announces;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/2/7 0:57
 *
 *
 * This announces for the public api for java-mod.
 * Please do not invoke the method which is not a {@link PublicAPI} not in documents. Since it will change sometime. But we will {@link Deprecated} it at first.
 **/
@EarlyClass
@Documented
@PrivateAPI
@Retention(RetentionPolicy.SOURCE)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
public @interface PublicAPI {
}
