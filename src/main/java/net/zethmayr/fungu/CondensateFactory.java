package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;

import java.util.function.Supplier;

import static net.zethmayr.fungu.ForkFactory.forkOf;

public final class CondensateFactory {

    private CondensateFactory() {
        throw ExceptionFactory.becauseAdaptersOnly();
    }

    static Condensate<Void, Exception> condense(final Runnable source) {
        try {
            return new ForkCondensate<>((Void) null);
        } catch (final Exception thrown) {
            return new ForkCondensate<>(thrown);
        }
    }

    static <T> Condensate<T, Exception> condense(final Supplier<T> source) {
        try {
            return new ForkCondensate<>(source.get());
        } catch (final Exception thrown) {
            return new ForkCondensate<>(thrown);
        }
    }

    private static class ForkCondensate<T, E extends Exception> implements Condensate<T, E> {
        final Fork<T, E> forked;

        ForkCondensate(final T value) {
            forked = forkOf(value, null);
        }

        ForkCondensate(final E exception) {
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
