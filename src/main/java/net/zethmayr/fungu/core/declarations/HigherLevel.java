package net.zethmayr.fungu.core.declarations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates that this is a higher-level package and
 * should not be depended on by lower-level packages.
 */
@Retention(SOURCE)
@Target(PACKAGE)
public @interface HigherLevel {
}
