package net.zethmayr.fungu.throwing;

import org.jetbrains.annotations.Contract;

import static net.zethmayr.fungu.core.ContractConstants.IDENTITY;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseAdaptersOnly;

/**
 * Provides adapter identity methods to safely avoid inline casting
 * when using throwing methods as sinkable methods.
 */
public final class SinkableHelper {

    private SinkableHelper() {
        throw becauseAdaptersOnly();
    }

    /**
     * Returns an implicitly sinkable
     * bi-function
     * as an explicitly sinkable
     * bi-function.
     *
     * @param duck the implicitly sinkable method.
     * @param <T>  the first argument type.
     * @param <U>  the second argument type.
     * @param <R>  the result type.
     * @param <E>  the exception type.
     * @return the explicitly sinkable method.
     */
    @Contract(IDENTITY)
    public static <T, U, R, E extends Exception> ThrowingBiFunction<T, U, R, E> sinkable(
            final ThrowingBiFunction<T, U, R, E> duck
    ) {
        return duck;
    }

    /**
     * Returns an implicitly sinkable
     * function
     * as an explicitly sinkable
     * function.
     *
     * @param duck the implicitly sinkable method.
     * @param <T>  the argument type.
     * @param <R>  the result type.
     * @param <E>  the exception type.
     * @return the explicitly sinkable method.
     */
    @Contract(IDENTITY)
    public static <T, R, E extends Exception> ThrowingFunction<T, R, E> sinkable(
            final ThrowingFunction<T, R, E> duck
    ) {
        return duck;
    }

    /**
     * Returns an implicitly sinkable
     * supplier
     * as an explicitly sinkable
     * supplier.
     *
     * @param duck the implicitly sinkable method.
     * @param <T>  the supplied type.
     * @param <E>  the exception type.
     * @return the explicitly sinkable method.
     */
    @Contract(IDENTITY)
    public static <T, E extends Exception> ThrowingSupplier<T, E> sinkable(final ThrowingSupplier<T, E> duck) {
        return duck;
    }

    /**
     * Returns an implicitly sinkable
     * consumer
     * as an explicitly sinkable
     * consumer.
     *
     * @param duck the implicitly sinkable method.
     * @param <T>  the consumed type.
     * @param <E>  the exception type.
     * @return the explicitly sinkable method.
     */
    @Contract(IDENTITY)
    public static <T, E extends Exception> ThrowingConsumer<T, E> sinkable(
            final ThrowingConsumer<T, E> duck
    ) {
        return duck;
    }

    /**
     * Returns an implicitly sinkable
     * runnable
     * as an explicitly sinkable
     * runnable.
     *
     * @param duck the implicitly sinkable method.
     * @param <E>  the exception type.
     * @return the explicitly sinkable method.
     */
    @Contract(IDENTITY)
    public static <E extends Exception> ThrowingRunnable<E> sinkable(
            final ThrowingRunnable<E> duck
    ) {
        return duck;
    }
}
