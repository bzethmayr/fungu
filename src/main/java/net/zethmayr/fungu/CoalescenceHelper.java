package net.zethmayr.fungu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseStaticsOnly;

/**
 * Provides functions in the family {@code coalesce},
 * which returns the first non-null value,
 * and in the family {@code defaultUnless},
 * which returns the first non-null value or a default.
 */
public final class CoalescenceHelper {

    private CoalescenceHelper() {
        throw becauseStaticsOnly();
    }

    /**
     * Returns the first non-null value.
     *
     * @param values some values.
     * @param <T>    the value type.
     * @return the first non-null value.
     */
    @Nullable
    @SafeVarargs
    public static <T> T coalesce(final @Nullable T... values) {
        for (final T candidate : values) {
            if (candidate != null) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Similar to {@link #coalesce},
     * but a default value is returned instead of null.
     *
     * @param defaultValue the default value
     * @param values       some values
     * @param <T>          the value type
     * @return a non-null value
     */
    @NotNull
    @SafeVarargs
    public static <T> T defaultUnless(@NotNull final T defaultValue, final @Nullable T... values) {
        final T coalesced = coalesce(values);
        return coalesced == null ? defaultValue : coalesced;
    }

    /**
     * Returns a supplier which
     * runs the suppliers given
     * until a non-null result is returned.
     * Subsequent suppliers are not evaluated.
     *
     * @param sources some value sources.
     * @param <T>     the value type.
     * @return a supplier giving the first value returned.
     */
    @NotNull
    @SafeVarargs
    public static <T> Supplier<@Nullable T> coalesces(final Supplier<@Nullable T>... sources) {
        return () -> {
            for (final Supplier<T> candidate : sources) {
                final T result = candidate.get();
                if (result != null) {
                    return result;
                }
            }
            return null;
        };
    }

    /**
     * Similar to {@link #coalesces},
     * but a default value is returned instead of null.
     *
     * @param defaultValue the default value
     * @param sources      some value sources
     * @param <T>          the value type.
     * @return a supplier giving a non-null value.
     */
    @NotNull
    @SafeVarargs
    public static <T> Supplier<@NotNull T> defaultsUnless(
            @NotNull final T defaultValue, final Supplier<@Nullable T>... sources
    ) {
        final Supplier<T> coalesces = coalesces(sources);
        return () -> defaultUnless(defaultValue, coalesces.get());
    }

    /**
     * Runs the given suppliers
     * until a non-null value is returned.
     * Subsequent suppliers are not evaluated.
     *
     * @param sources some value sources.
     * @param <T>     the value type.
     * @return the first non-null result.
     */
    @Nullable
    @SafeVarargs
    public static <T> T coalesced(final Supplier<@Nullable T>... sources) {
        return coalesces(sources).get();
    }

    /**
     * Similar to {@link #coalesced(Supplier[])},
     * but a default value is returned instead of null.
     *
     * @param defaultValue the default value.
     * @param sources      some value sources.
     * @param <T>          the value type.
     * @return a non-null value.
     */
    @NotNull
    @SafeVarargs
    public static <T> T defaultedUnless(
            @NotNull final T defaultValue,
            final Supplier<@Nullable T>... sources
    ) {
        return defaultUnless(defaultValue, coalesced(sources));
    }

    /**
     * Returns a function which
     * applies the functions given to its argument
     * until a non-null result is returned.
     * Subsequent functions are not evaluated.
     *
     * @param accesses some functions of common interface type
     * @param <T>      the common argument type
     * @param <R>      the common result type
     * @return a function returning the first non-null result
     */
    @NotNull
    @SafeVarargs
    public static <T, R> Function<T, @Nullable R> coalescing(
            final Function<@NotNull T, @Nullable R>... accesses
    ) {
        return t -> {
            for (final Function<T, R> access : accesses) {
                final R result = access.apply(t);
                if (result != null) {
                    return result;
                }
            }
            return null;
        };
    }

    /**
     * Similar to {@link #coalescing},
     * but a default value is returned instead of null.
     *
     * @param defaultResult the default result.
     * @param accesses      some functions of common interface type.
     * @param <T>           the common argument type.
     * @param <R>           the common result type.
     * @return a function returning a non-null value.
     */
    @NotNull
    @SafeVarargs
    public static <T, R> Function<T, @NotNull R> defaultingUnless(
            @NotNull final R defaultResult, final Function<@NotNull T, @Nullable R>... accesses
    ) {
        final Function<T, R> coalescing = coalescing(accesses);
        return t -> defaultUnless(defaultResult, coalescing.apply(t));
    }

    /**
     * Given an argument,
     * applies the functions given
     * until a non-null result is returned.
     * Subsequent functions are not evaluated.
     *
     * @param source   the common argument
     * @param accesses some functions of common interface type
     * @param <T>      the common argument type
     * @param <R>      the common result type
     * @return the first non-null result
     */
    @Nullable
    @SafeVarargs
    public static <T, R> R coalesced(
            @NotNull final T source, final Function<@NotNull T, @Nullable R>... accesses
    ) {
        return coalescing(accesses).apply(source);
    }

    /**
     * Similar to {@link #coalesced(Object, Function[])},
     * but a default value is returned instead of null.
     *
     * @param source        the common argument
     * @param defaultResult the default result
     * @param accesses      some functions of common interface type
     * @param <T>           the common argument type.
     * @param <R>           the common result type.
     * @return a non-null result.
     */
    @NotNull
    @SafeVarargs
    public static <T, R> R defaultedUnless(
            @NotNull final T source, @NotNull final R defaultResult, final Function<@NotNull T, @Nullable R>... accesses
    ) {
        return defaultUnless(defaultResult, coalesced(source, accesses));
    }
}
