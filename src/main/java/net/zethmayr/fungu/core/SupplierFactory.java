package net.zethmayr.fungu.core;

import net.zethmayr.fungu.core.ExceptionFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public final class SupplierFactory {
    private SupplierFactory() {
        throw ExceptionFactory.becauseAdaptersOnly();
    }

    /**
     * Returns a supplier with a fixed value.
     * @param value the value.
     * @return a supplier
     * @param <T> the value type
     */
    public static <T> Supplier<T> from(final T value) {
        return () -> value;
    }

    /**
     * Returns a supplier based on an instance field.
     * @param instance the instance
     * @param getter the field getter
     * @return a supplier
     * @param <T> the field type
     * @param <H> the instance type
     */
    public static <T, H> Supplier<T> from(final H instance, final Function<H, T> getter) {
        return () -> getter.apply(instance);
    }

    /**
     * Explicitly erases a supplier's generic type.
     * @param genericSupplier a supplier
     * @return a supplier
     * @param <T> the generic type
     */
    public static <T> Supplier<Object> like(final Supplier<T> genericSupplier) {
        return genericSupplier::get;
    }
}
