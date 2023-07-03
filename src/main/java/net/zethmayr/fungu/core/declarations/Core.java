package net.zethmayr.fungu.core.declarations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates that this is a core package and
 * may not have any internal dependencies.
 */
@Retention(SOURCE)
@Target(PACKAGE)
public @interface Core {
}
