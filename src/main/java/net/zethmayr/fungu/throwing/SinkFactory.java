package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.core.ExceptionFactory;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseThrewImpossibly;

/**
 * Provides a variety of simple exception sinks.
 */
public final class SinkFactory {
    /*
     * If Java had macros, this would be a great place for some IFDEFd debugging stuff.
     * A count match between constructors and throwAny would be nice, for instance.
     * An option to convert mines to holes and vice versa would also be nice.
     * But, that would create a lot of clutter.
     */
    private SinkFactory() {
        throw ExceptionFactory.becauseFactory();
    }

    /**
     * A simple sink suitable for use by a single thread.
     */
    private static class TrivialSink<E extends Exception> implements Sink<E> {
        private Exception thrown;

        @Override
        public void accept(final E thrown) {
            if (isNull(this.thrown)) {
                this.thrown = thrown;
            } else {
                this.thrown.addSuppressed(thrown);
            }
        }

        @Override
        public void raise() throws E {
            if (nonNull(thrown)) {
                throw (E) thrown;
            }
        }
    }

    /**
     * Returns a simple sink
     * suitable for use by a single thread,
     * which stores and raises exceptions thrown.
     *
     * @param <E> the exception type.
     * @return a simple local sink.
     */
    public static <E extends Exception> Sink<E> sink() {
        return new TrivialSink<>();
    }

    /**
     * Base class for implementations which
     * have no use for a raise method.
     *
     * @param <E> the exception type.
     */
    private static abstract class Immediately<E extends Exception> implements Sink<E> {
        @Override
        public void raise() throws E {

        }
    }

    /**
     * Returns a sink which
     * silently swallows exceptions.
     * Provided because someone was going to do this anyway,
     * also for completeness, but you should use something else.
     *
     * @param <E> the exception type.
     * @return a broken sink.
     */
    public static <E extends Exception> Sink<E> hole() {
        return new Immediately<E>() {
            @Override
            public void accept(E thrown) {

            }
        };
    }

    /**
     * Returns a sink which
     * immediately wraps and rethrows exceptions.
     * This is useful in cases where exceptions are
     * impossible, irrecoverable, or both.
     *
     * @param <E> the exception type.
     * @return an exploding sink.
     */
    public static <E extends Exception> Sink<E> mine() {
        return new Immediately<E>() {
            @Override
            public void accept(E thrown) {
                throw becauseThrewImpossibly("mine struck", thrown);
            }
        };
    }

    /**
     * Returns a thread-safe (internally synchronized) sink
     * which stores and raises exceptions thrown.
     *
     * @param <E> the exception type.
     * @return a thread-safe sink.
     */
    public static <E extends Exception> Sink<E> threadSafeSink() {
        return new ThreadSafeSink<>();
    }

    private static final class ThreadSafeSink<E extends Exception> implements Sink<E> {

        private final Object lock = new Object();
        private final TrivialSink<E> delegate = new TrivialSink<>();

        @Override
        public void accept(final E thrown) {
            synchronized (lock) {
                delegate.accept(thrown);
            }
        }

        @Override
        public void raise() throws E {
            synchronized (lock) {
                delegate.raise();
            }
        }
    }
}
