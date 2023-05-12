package net.zethmayr.fungu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseStaticsOnly;
import static net.zethmayr.fungu.core.SupplierFactory.nothing;

/**
 * Provides some common logical evaluations.
 */
public final class DecisionHelper {
    private DecisionHelper() {
        throw becauseStaticsOnly();
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

    /**
     * Returns a function which applies the given test,
     * returning the first supplier result when the test passes
     * and the second supplier result when the test fails.
     *
     * @param test      the test.
     * @param supplier  the supplier when the test passes.
     * @param whenFalse the supplier when the test fails.
     * @param <T>       the tested type.
     * @param <R>       the result type.
     * @return a function returning a supplied result.
     */
    @NotNull
    public static <T, R> Function<T, @Nullable R> maybeGet(
            @NotNull final Predicate<T> test, @NotNull final Supplier<R> supplier, @NotNull final Supplier<R> whenFalse
    ) {
        return t -> test.test(t) ? supplier.get() : whenFalse.get();

    }

    /**
     * Returns a function which applies the given test,
     * returning the supplied result when the test passes,
     * and null when the test fails.
     *
     * @param test     the test.
     * @param supplier the supplier when the test passes.
     * @param <T>      the tested type.
     * @param <R>      the result type.
     * @return a function returning a supplied result, or null.
     */
    @NotNull
    public static <T, R> Function<T, @Nullable R> maybeGet(
            @NotNull final Predicate<T> test, @NotNull final Supplier<R> supplier
    ) {
        return maybeGet(test, supplier, nothing());
    }

    /**
     * Returns a function which applies the given test,
     * applying the given function when the test passes,
     * and using the given supplier when the test fails.
     *
     * @param test      the test.
     * @param mapper    the mapper for values passing the test.
     * @param whenFalse the supplier when the test fails.
     * @param <T>       the tested type.
     * @param <R>       the result type.
     * @return a function.
     */
    @NotNull
    public static <T, R> Function<T, R> maybeApply(
            @NotNull final Predicate<T> test, @NotNull final Function<T, R> mapper, @NotNull final Supplier<R> whenFalse
    ) {
        return t -> test.test(t) ? mapper.apply(t) : whenFalse.get();
    }

    /**
     * Returns a function which applies the given test,
     * applying the given function when the test passes,
     * and returning null when the test fails.
     *
     * @param test   the test.
     * @param mapper the mapper for values passing the test.
     * @param <T>    the tested type.
     * @param <R>    the result type.
     * @return a function.
     */
    @NotNull
    public static <T, R> Function<T, @Nullable R> maybeApply(
            @NotNull final Predicate<T> test, @NotNull final Function<T, R> mapper
    ) {
        return maybeApply(test, mapper, nothing());
    }

    /**
     * Returns a function which applies the given test,
     * returning the given result when the test passes,
     * and using the given supplier when the test fails.
     *
     * @param test     the test.
     * @param result   the result when the test passes.
     * @param whenNull the supplier when the test fails.
     * @param <T>      the tested type.
     * @param <R>      the result type.
     * @return a function.
     */
    @NotNull
    public static <T, R> Function<T, @Nullable R> maybeResult(
            @NotNull final Predicate<T> test, @Nullable final R result, @NotNull final Supplier<R> whenNull
    ) {
        return t -> test.test(t) ? result : whenNull.get();

    }

    /**
     * Returns a function which applies the given test,
     * returning the given result when the test passes,
     * and null when the test fails.
     *
     * @param test   the tested type.
     * @param result the result for passing tests.
     * @param <T>    the tested type.
     * @param <R>    the result type.
     * @return a function.
     */
    @NotNull
    public static <T, R> Function<T, @Nullable R> maybeResult(
            @NotNull final Predicate<T> test, @Nullable final R result
    ) {
        return maybeResult(test, result, nothing());
    }
}
