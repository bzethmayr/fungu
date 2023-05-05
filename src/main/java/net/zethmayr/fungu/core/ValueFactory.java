package net.zethmayr.fungu.core;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Provides adapters down to values.
 */
public final class ValueFactory {
    private ValueFactory() {
        throw ExceptionFactory.becauseFactory();
    }

    /**
     * Returns the values returned by the suppliers passed.
     *
     * @param suppliers some value suppliers.
     * @return some values.
     */
    public static Object[] values(final Supplier<?>... suppliers) {
        return Stream.of(suppliers)
                .map(Supplier::get)
                .toArray();
    }
}
