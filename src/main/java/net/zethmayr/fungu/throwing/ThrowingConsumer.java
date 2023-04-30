package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;

/**
 * As {@link Consumer<T>}, but
 * the {@link #accept(T)} method
 * may throw a checked exception.
 * @param <T> the consumed type.
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Sinkable<Consumer<T>> {

    /**
     * Takes a single value.
     * This is the throwing analogue of {@link Consumer#accept}.
     * @param value a value.
     * @throws Exception per implementation.
     */
    void accept(final T value) throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    default Consumer<T> sinking(final Consumer<Exception> sink) {
        return t -> {
            try {
                this.accept(t);
            } catch (final Exception thrown) {
                sink.accept(thrown);
            }
        };
    }
}
