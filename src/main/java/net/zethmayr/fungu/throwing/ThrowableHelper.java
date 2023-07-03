package net.zethmayr.fungu.throwing;

import org.jetbrains.annotations.NotNull;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseStaticsOnly;
import static net.zethmayr.fungu.throwing.SunkenException.becauseSunken;

/**
 * Package-private statics used for composing slightly safer methods.
 */
final class ThrowableHelper {

    private ThrowableHelper() {
        throw becauseStaticsOnly();
    }

    static <C extends Exception> void raiseIfChecked(
            @NotNull final Class<C> check, @NotNull final Exception thrown
    ) throws C {
        if (check.isAssignableFrom(thrown.getClass())) {
            throw check.cast(thrown);
        }
    }

    static <C extends Exception> void raiseOrChecked(
            @NotNull final Class<C> check, @NotNull final Exception thrown
    ) throws C, SunkenException {
        raiseIfChecked(check, thrown);
        throw becauseSunken(thrown);
    }
}
