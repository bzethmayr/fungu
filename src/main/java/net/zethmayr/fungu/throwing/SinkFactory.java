package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.core.ExceptionFactory;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
    private static class TrivialSink implements Sink {
        private Exception thrown;

        @Override
        public void accept(final Exception thrown) {
            if (isNull(this.thrown)) {
                this.thrown = thrown;
            } else {
                this.thrown.addSuppressed(thrown);
            }
        }

        @Override
        public void raise() throws Exception {
            if (nonNull(thrown)) {
                throw thrown;
            }
        }
    }

    /**
     * Returns a simple sink
     * suitable for use by a single thread,
     * which stores and raises exceptions thrown.
     *
     * @return a simple local sink.
     */
    public static Sink sink() {
        return new TrivialSink();
    }

    /**
     * Base class for implementations which
     * have no use for a raise method.
     */
    private static abstract class Immediately implements Sink {
        @Override
        public void raise() throws Exception {

        }
    }

    /**
     * Returns a sink which
     * silently swallows exceptions.
     * Provided because someone was going to do this anyway,
     * also for completeness, but you should use something else.
     *
     * @return a broken sink.
     */
    public static Sink hole() {
        return new Immediately() {
            @Override
            public void accept(Exception thrown) {

            }
        };
    }

    /**
     * Returns a sink which
     * immediately wraps and rethrows exceptions.
     * This is useful in cases where exceptions are
     * impossible, irrecoverable, or both.
     *
     * @return an exploding sink.
     */
    public static Sink mine() {
        return new Immediately() {
            @Override
            public void accept(Exception thrown) {
                throw ExceptionFactory.becauseThrewImpossibly("mine struck", thrown);
            }
        };
    }

    /**
     * Returns a thread-safe (internally synchronized) sink
     * which stores and raises exceptions thrown.
     */
    public static Sink threadSafeSink() {
        return new ThreadSafeSink();
    }

    private static class ThreadSafeSink implements Sink {

        private final Object lock = new Object();
        private final TrivialSink delegate = new TrivialSink();

        @Override
        public void accept(final Exception thrown) {
            synchronized (lock) {
                delegate.accept(thrown);
            }
        }

        @Override
        public void raise() throws Exception {
            synchronized (lock) {
                delegate.raise();
            }
        }
    }
}
