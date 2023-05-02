package net.zethmayr.fungu;

import java.util.function.Supplier;

import static net.zethmayr.fungu.ForkFactory.forkOf;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseAdaptersOnly;

/**
 * Wraps results of evaluations.
 */
public final class ResultFactory {

    private ResultFactory() {
        throw becauseAdaptersOnly();
    }

    static Result<Void, Exception> evaluate(final Runnable source) {
        try {
            return new ForkResult<>((Void) null);
        } catch (final Exception thrown) {
            return new ForkResult<>(thrown);
        }
    }

    static <T> Result<T, Exception> evaluate(final Supplier<T> source) {
        try {
            return new ForkResult<>(source.get());
        } catch (final Exception thrown) {
            return new ForkResult<>(thrown);
        }
    }

    private static class ForkResult<T, E extends Exception> implements Result<T, E> {
        final Fork<T, E> forked;

        ForkResult(final T value) {
            forked = forkOf(value, null);
        }

        ForkResult(final E exception) {
            forked = forkOf(null, exception);
        }

        @Override
        public T get() {
            return forked.top();
        }

        @Override
        public E getException() {
            return forked.bottom();
        }
    }
}
