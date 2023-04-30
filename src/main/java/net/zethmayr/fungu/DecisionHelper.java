package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;

import java.util.Objects;
import java.util.stream.Stream;

public final class DecisionHelper {
    private DecisionHelper() {
        throw ExceptionFactory.becauseStaticsOnly();
    }

    public static boolean anyNull(final Object... values) {
        return Stream.of(values)
                .anyMatch(Objects::isNull);
    }
}
