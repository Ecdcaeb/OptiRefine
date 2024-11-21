package rml.deserializer;

import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.TYPE;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/20 19:00
 *
 * Act as record in higher java, used for auto construction.
 * If labeled in a CONSTRUCTOR, you should fill parameters if existed.
 * If labeled in a TYPE, it just a node, nothing need to do specially.
 *
 * Note: You could only label one constructor for each record class.
 **/
@PublicAPI
@EarlyClass
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={CONSTRUCTOR, TYPE})
public @interface Record {
    /**
     * @return the parameters list
     */
    String[] value() default {};
}
