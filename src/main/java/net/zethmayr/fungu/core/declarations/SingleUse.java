package net.zethmayr.fungu.core.declarations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates that a method's results,
 * or a class's instances,
 * should not be reused outside their scope of creation.
 */
@Retention(SOURCE)
@Target({TYPE, METHOD})
public @interface SingleUse {
}
