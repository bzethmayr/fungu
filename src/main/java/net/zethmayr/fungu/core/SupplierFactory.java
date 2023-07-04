package net.zethmayr.fungu.core;

import net.zethmayr.fungu.core.declarations.ReuseResults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseAdaptersOnly;

/**
 * Provides ad-hoc {@link Supplier} implementations.
 */
@ReuseResults
public final class SupplierFactory {
    private SupplierFactory() {
        throw becauseAdaptersOnly();
    }

    /**
     * Returns a supplier with a covariant null result.
     *
     * @param <T> the supplied type.
     * @return a supplier.
     */
    @NotNull
    public static <T> Supplier<@Nullable T> nothing() {
        return () -> null;
    }

    /**
     * Returns a supplier with a fixed value.
     *
     * @param value the value.
     * @param <T>   the value type.
     * @return a supplier.
     */
    @NotNull
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
    @NotNull
    public static <T, H> Supplier<T> from(final H instance, @NotNull final Function<H, T> getter) {
        return () -> getter.apply(instance);
    }

    /**
     * Explicitly erases a supplier's generic type.
     *
     * @param genericSupplier a supplier
     * @param <T>             the generic type
     * @return a supplier
     */
    @NotNull
    public static <T> Supplier<Object> like(@NotNull final Supplier<T> genericSupplier) {
        return genericSupplier::get;
    }
}
