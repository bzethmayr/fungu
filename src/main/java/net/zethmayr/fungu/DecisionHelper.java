package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Provides common evaluations to boolean.
 */
public final class DecisionHelper {
    private DecisionHelper() {
        throw ExceptionFactory.becauseStaticsOnly();
    }

    /**
     * Returns true if any value is null.
     *
     * @param values some values.
     * @return true if any value is null, otherwise false.
     */
    public static boolean anyNull(final Object... values) {
        return Stream.of(values)
                .anyMatch(Objects::isNull);
    }
}
