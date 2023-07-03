package net.zethmayr.fungu.core.declarations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates that this functionality is not actually usable,
 * either since it is still being implemented or
 * because it just doesn't work.
 */

@Retention(SOURCE)
@Target({PACKAGE, TYPE, METHOD})
public @interface NotDone {
}
