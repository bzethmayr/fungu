package net.zethmayr.fungu.core.declarations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates that this is a lower-level package
 * and can be freely depended on by higher-level packages,
 * but should not depend on non-core packages.
 */
@Retention(SOURCE)
@Target(PACKAGE)
public @interface LowerLevel {
}
