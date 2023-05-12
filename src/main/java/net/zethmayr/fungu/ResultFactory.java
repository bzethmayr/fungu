package net.zethmayr.fungu;

import net.zethmayr.fungu.throwing.ThrowingRunnable;
import net.zethmayr.fungu.throwing.ThrowingSupplier;

import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseAdaptersOnly;

/**
 * Wraps results of evaluations.
 */
public final class ResultFactory {

    private ResultFactory() {
        throw becauseAdaptersOnly();
    }

    /**
     * Wraps a successful evaluation.
     *
     * @param value the value.
     * @param <T>   the value type.
     * @param <E>   the exception type.
     * @return a result with a value.
     */
    public static <T, E extends Exception> Result<T, E> success(final T value) {
        return new SimpleResult<>(value, null);
    }

    /**
     * Wraps a failed evaluation.
     *
     * @param thrown the exception
     * @param <T>    the value type.
     * @param <E>    the exception type.
     * @return a result with an exception.
     */
    public static <T, E extends Exception> Result<T, E> failure(final E thrown) {
        return new SimpleResult<>(null, thrown);
    }

    /**
     * Evaluates the given runnable,
     * returning an empty result
     * or a result with any exception thrown.
     *
     * @param source the source.
     * @return the result.
     */
    public static Result<Void, Exception> evaluate(final Runnable source) {
        try {
            source.run();
            return success(null);
        } catch (final Exception thrown) {
            return failure(thrown);
        }
    }

    /**
     * Evaluates the given runnable,
     * returning an empty result
     * or a result with the exception thrown.
     *
     * @param source the source.
     * @param <E>    the exception type.
     * @return a result.
     */
    public static <E extends Exception> Result<Void, E> evaluateThrowing(final ThrowingRunnable<E> source) {
        try {
            source.run();
            return success(null);
        } catch (final Exception thrown) {
            return failure((E) thrown); //TODO consider a checked sink
        }
    }

    /**
     * Evaluates the given supplier,
     * returning a result with either
     * the supplied value or any exception thrown.
     *
     * @param source the supplier.
     * @param <T>    the value type.
     * @return a result.
     */
    public static <T> Result<T, Exception> evaluate(final Supplier<T> source) {
        try {
            return success(source.get());
        } catch (final Exception thrown) {
            return failure(thrown);
        }
    }

    /**
     * Evaluates the given supplier,
     * returning a result with either
     * the supplied value or the exception thrown.
     *
     * @param source the supplier.
     * @param <T>    the value type.
     * @param <E>    the exception type.
     * @return a result.
     */
    public static <T, E extends Exception> Result<T, E> evaluateThrowing(final ThrowingSupplier<T, E> source) {
        try {
            return success(source.get());
        } catch (final Exception thrown) {
            return failure((E) thrown);
        }
    }

    private record SimpleResult<T, E extends Exception>(T value, E thrown) implements Result<T, E> {

        @Override
        public T get() {
            return value;
        }

        @Override
        public E getException() {
            return thrown;
        }
    }
}
