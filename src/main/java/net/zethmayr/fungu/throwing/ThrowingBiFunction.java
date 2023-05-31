package net.zethmayr.fungu.throwing;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface ThrowingBiFunction<T, U, R, E extends Exception> extends Sinkable<BiFunction<T, U, R>, E> {

    R apply(final T first, final U second) throws E;

    /**
     * {@inheritDoc}
     * Returns null when an exception was thrown.
     */
    @Override
    default BiFunction<T, U, R> sinking(final Consumer<E> sink) {
        return (t, u) -> {
            try {
                return apply(t, u);
            } catch (final Exception thrown) {
                sink.accept((E) thrown);
            }
            return null;
        };
    }
}
