package net.zethmayr.fungu.fields;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a method is a setter.
 */
@Retention(RUNTIME)
@Target(METHOD)
@Inherited
public @interface Sets {

    /**
     * Where not obvious,
     * indicates what mutable interface this method is associated with.
     *
     * @return an associated mutable interface.
     */
    Class<? extends SetsX> value() default SetsX.class;
}
