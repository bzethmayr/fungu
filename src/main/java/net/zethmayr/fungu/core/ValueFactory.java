package net.zethmayr.fungu.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseFactory;

/**
 * Provides adapters down to values.
 */
public final class ValueFactory {
    private ValueFactory() {
        throw becauseFactory();
    }

    /**
     * Returns the values returned by the suppliers passed.
     *
     * @param suppliers some value suppliers.
     * @return some values.
     */
    @NotNull
    public static Object[] values(@NotNull final Supplier<?>... suppliers) {
        final int length = suppliers.length;
        final Object[] results = new Object[length];
        for (int index = 0; index < length; index++) {
            results[index] = suppliers[index].get();
        }
        return results;
    }
}
