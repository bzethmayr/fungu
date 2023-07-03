package net.zethmayr.fungu.core.declarations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates that a method,
 * or all of a class's methods,
 * neither create nor destroy any instances.
 */
@Retention(SOURCE)
@Target({TYPE, METHOD})
public @interface GarbageFree {
}
