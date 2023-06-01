package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;

/**
 * Defers an exception to allow functional composition.
 */
public interface Sink<E extends Exception> extends Consumer<E> {

    /**
     * Receives exceptions from sinkable implementations.
     *
     * @param thrown an exception.
     */
    @Override
    void accept(final E thrown);

    /**
     * Throws received exceptions.
     *
     * @throws E received from sinkable implementation.
     */
    void raise() throws E;

    /**
     * Throws received exceptions
     * of the given class,
     * wrapping and rethrowing any other exceptions.
     *
     * @param check the constraining class.
     * @param <C>   the constraining type.
     * @throws C a checked or wrapped exception.
     */
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

    /**
     * Throws received exceptions
     * of the given class,
     * or returns this instance,
     * which may still have received other exceptions.
     * <p>
     * Recommended usage is with a terminating {@link #raiseChecked(Class)}:
     * <pre>
     * sink.raiseOr(URISyntaxException.class).raiseChecked(IOException.class);
     * </pre>
     *
     * @param check the constraining class.
     * @param <C>   the constraining class
     * @return this sink, when no such exception was thrown.
     * @throws C a checked exception, if such was thrown.
     */
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
