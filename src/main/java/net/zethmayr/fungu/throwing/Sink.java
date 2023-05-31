package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;

/**
 * Defers an exception to allow functional composition.
 */
public interface Sink<E extends Exception> extends Consumer<E> {

    /**
     * Receives exceptions from sinkable implementations.
     * @param thrown an exception.
     */
    @Override
    void accept(final E thrown);

    /**
     * Throws received exceptions.
     * @throws E received from sinkable implementation.
     */
    void raise() throws E;

    default <C extends Exception> void raiseChecked(final Class<C> check) throws C {
        try {
            raise();
        } catch (final Exception e) {
            if (check.isAssignableFrom(e.getClass())) {
                throw check.cast(e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    default <C extends Exception> Sink<E> raiseOr(final Class<C> check) throws C {
        try {
            raise();
        } catch (final Exception e) {
            if (check.isAssignableFrom(e.getClass())) {
                throw check.cast(e);
            }
        }
        return this;
    }
}
