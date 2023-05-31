package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> extends Sinkable<Function<T, R>, E> {

    R apply(final T argument) throws E;

    /**
     * {@inheritDoc}
     * Returns null when an exception was thrown.
     */
    @Override
    default Function<T, R> sinking(final Consumer<E> sink) {
        return t -> {
            try {
                return apply(t);
            } catch (final Exception e) {
                sink.accept((E) e);
            }
            return null;
        };
    }
}
