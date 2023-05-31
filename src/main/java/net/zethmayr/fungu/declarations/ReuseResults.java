package net.zethmayr.fungu.declarations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates that a method's results,
 * or all of a class's methods' results,
 * are immutable or otherwise freely reusable instances.
 */
@Retention(SOURCE)
@Target({TYPE, METHOD, CONSTRUCTOR})
public @interface ReuseResults {
}
