package net.zethmayr.fungu;

import net.zethmayr.fungu.throwing.Sink;
import net.zethmayr.fungu.throwing.SinkFactory;
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

    public static <T, E extends Exception> Result<T, E> success(final T value) {
        return new SimpleResult<>(value, null);
    }

    public static <T, E extends Exception> Result<T, E> failure(final E thrown) {
        return new SimpleResult<>(null, thrown);
    }

    public static Result<Void, Exception> evaluate(final Runnable source) {
        try {
            source.run();
            return success(null);
        } catch (final Exception thrown) {
            return failure(thrown);
        }
    }

    public static <E extends Exception> Result<Void, E> evaluateThrowing(final ThrowingRunnable<E> source) {
        try {
            source.run();
            return success(null);
        } catch (final Exception thrown) {
            return failure((E) thrown); //TODO consider a checked sink
        }
    }

    public static <T> Result<T, Exception> evaluate(final Supplier<T> source) {
        try {
            return success(source.get());
        } catch (final Exception thrown) {
            return failure(thrown);
        }
    }

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
