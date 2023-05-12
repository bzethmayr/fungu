package net.zethmayr.fungu.core;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseAdaptersOnly;

/**
 * Provides ad-hoc {@link Supplier} implementations.
 */
public final class SupplierFactory {
    private SupplierFactory() {
        throw becauseAdaptersOnly();
    }

    /**
     * Returns a supplier with a covariant null result.
     */
    public static <T> Supplier<T> nothing() {
        return () -> null;
    }

    /**
     * Returns a supplier with a fixed value.
     *
     * @param value the value.
     * @param <T>   the value type
     * @return a supplier
     */
    public static <T> Supplier<T> from(final T value) {
        return () -> value;
    }

    /**
     * Returns a supplier based on an instance field.
     *
     * @param instance the instance
     * @param getter   the field getter
     * @param <T>      the field type
     * @param <H>      the instance type
     * @return a supplier
     */
    public static <T, H> Supplier<T> from(final H instance, final Function<H, T> getter) {
        return () -> getter.apply(instance);
    }

    /**
     * Explicitly erases a supplier's generic type.
     *
     * @param genericSupplier a supplier
     * @param <T>             the generic type
     * @return a supplier
     */
    public static <T> Supplier<Object> like(final Supplier<T> genericSupplier) {
        return genericSupplier::get;
    }
}
