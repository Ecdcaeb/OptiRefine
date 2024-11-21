package rml.jrx.announces;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/2/7 1:00
 *
 * This announces is for the early class. You can call it early. Before the game real bootstrap.
 **/
@PublicAPI
@EarlyClass
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={TYPE})
public @interface EarlyClass {
}
