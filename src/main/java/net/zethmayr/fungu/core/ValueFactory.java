package net.zethmayr.fungu.core;

import net.zethmayr.fungu.core.ExceptionFactory;

import java.util.function.Supplier;
import java.util.stream.Stream;

public final class ValueFactory {
    private ValueFactory() {
        throw ExceptionFactory.becauseFactory();
    }

    public static Object[] values(final Supplier<?>... suppliers) {
        return Stream.of(suppliers)
                .map(Supplier::get)
                .toArray();
    }
}
