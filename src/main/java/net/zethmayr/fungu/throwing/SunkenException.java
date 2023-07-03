package net.zethmayr.fungu.throwing;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

/**
 * This unchecked exception indicates the wrapped exception was previously thrown.
 */
public final class SunkenException extends RuntimeException {

    private static final Set<Class<?>> WRAPS = Stream.of(
            SunkenException.class
    ).collect(toUnmodifiableSet());

    private SunkenException(final Exception cause) {
        super(cause);
    }

    static SunkenException becauseSunken(final Exception cause) {
        return new SunkenException(cause);
    }

    @Override
    public Throwable getCause() {
        return unwrap(super.getCause());
    }

    public static Throwable unwrap(final Throwable thrown) {
        return unwrap(thrown, WRAPS);
    }

    public static Throwable unwrap(final Throwable thrown, final Set<Class<?>> wrappers) {
        Throwable ugh = thrown;
        while (wrappers.contains(ugh.getClass())) {
            ugh = ugh.getCause();
        }
        return ugh;
    }
}
