package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;

import static net.zethmayr.fungu.core.SuppressionConstants.CONSUMER_CHECKS;

/**
 * As {@link Consumer<T>}, but
 * the {@link #accept(T)} method
 * may throw a checked exception.
 * @param <T> the consumed type.
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> extends Sinkable<Consumer<T>, E> {

    /**
     * Takes a single value.
     * This is the throwing analogue of {@link Consumer#accept}.
     * @param value a value.
     * @throws E per implementation.
     */
    void accept(final T value) throws E;

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings(CONSUMER_CHECKS)
    default Consumer<T> sinking(final Consumer<E> sink) {
        return t -> {
            try {
                this.accept(t);
            } catch (final Exception thrown) {
                sink.accept((E)thrown);
            }
        };
    }
}
