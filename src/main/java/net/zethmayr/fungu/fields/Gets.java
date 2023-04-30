package net.zethmayr.fungu.fields;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a method is a getter.
 */
@Retention(RUNTIME)
@Target(METHOD)
@Inherited
public @interface Gets {

    /**
     * Where not obvious, indicates what field interface this method is associated with.
     *
     * @return an associated field interface.
     */
    Class<? extends HasX> value() default HasX.class;
}
