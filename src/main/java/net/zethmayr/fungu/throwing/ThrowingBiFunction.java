package net.zethmayr.fungu.throwing;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static net.zethmayr.fungu.core.SuppressionConstants.CONSUMER_CHECKS;

/**
 * The throwing analogue of {@link BiFunction}.
 * @param <T> the first argument type.
 * @param <U> the second argument type.
 * @param <R> the result type.
 * @param <E> the thrown type.
 */
public interface ThrowingBiFunction<T, U, R, E extends Exception> extends Sinkable<BiFunction<T, U, R>, E> {

    /**
     * Given two values, returns a value.
     * This is the throwing equivalent of {@link BiFunction#apply(Object, Object)}.
     * @param first the first argument
     * @param second the second argument.
     * @return a value.
     * @throws E if something goes wrong.
     */
    R apply(final T first, final U second) throws E;

    /**
     * {@inheritDoc}
     * Returns null when an exception was thrown.
     */
    @Override
    @SuppressWarnings(CONSUMER_CHECKS)
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
