package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The throwing analogue of {@link Supplier}
 *
 * @param <T> the supplied type.
 * @param <E> the exception type.
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> extends Sinkable<Supplier<T>, E> {

    /**
     * Provides a value.
     * This is the throwing analogue of {@link Supplier#get()}
     *
     * @return a value.
     * @throws E when something goes wrong.
     */
    T get() throws E;

    /**
     * {@inheritDoc}
     * Supplies null when an exception was thrown.
     */
    @Override
    default Supplier<T> sinking(final Consumer<E> sink) {
        return () -> {
            try {
                return this.get();
            } catch (final Exception thrown) {
                sink.accept((E) thrown);
            }
            return null;
        };
    }
}
