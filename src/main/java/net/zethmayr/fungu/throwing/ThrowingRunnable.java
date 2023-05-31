package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;

/**
 * The throwing analogue of {@link Runnable}.
 *
 * @param <E> the exception type.
 */
@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> extends Sinkable<Runnable, E> {

    /**
     * Runs an operation.
     * This is the throwing analogue of {@link Runnable#run}.
     *
     * @throws E per implementation.
     */
    void run() throws E;

    @Override
    default Runnable sinking(final Consumer<E> sink) {
        return () -> {
            try {
                this.run();
            } catch (final Exception thrown) {
                sink.accept((E) thrown);
            }
        };
    }
}
