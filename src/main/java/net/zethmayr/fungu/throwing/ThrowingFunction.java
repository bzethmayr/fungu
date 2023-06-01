package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The throwing analogue of {@link Function}.
 *
 * @param <T> the argument type.
 * @param <R> the result type
 * @param <E> the thrown type.
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> extends Sinkable<Function<T, R>, E> {

    /**
     * Given an argument, returns a result.
     * This is the throwing analoque of {@link Function#apply(Object)}
     *
     * @param argument the argument.
     * @return the result.
     * @throws E if something goes wrong.
     */
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
