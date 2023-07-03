package net.zethmayr.fungu.throwing;

import org.jetbrains.annotations.NotNull;

import static net.zethmayr.fungu.throwing.ThrowableHelper.raiseIfChecked;
import static net.zethmayr.fungu.throwing.ThrowableHelper.raiseOrChecked;

/**
 * Can re-raise received exceptions, including checked exceptions.
 * Since other checked or unchecked exceptions can also be thrown in many contexts,
 * there are methods here to check for specific exception types.
 *
 * @param <E> the exception type.
 * @param <R> the covariant revenant type.
 */
public interface Revenant<E extends Exception, R extends Revenant<E, R>> {

    /**
     * Throws any received exceptions.
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
     * @throws C               a checked exception, if such was thrown
     * @throws SunkenException an unchecked wrapping exception, if another exception was thrown
     */
    default <C extends Exception> void raiseChecked(@NotNull final Class<C> check) throws C, SunkenException {
        try {
            raise();
        } catch (final Exception e) {
            raiseOrChecked(check, e);
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
    default <C extends Exception> Revenant<E, R> raiseOr(final Class<C> check) throws C {
        try {
            raise();
        } catch (final Exception e) {
            raiseIfChecked(check, e);
        }
        return this;
    }
}
