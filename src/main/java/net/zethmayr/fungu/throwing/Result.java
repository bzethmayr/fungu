package net.zethmayr.fungu.throwing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.util.Objects.nonNull;
import static net.zethmayr.fungu.throwing.ThrowableHelper.raiseIfChecked;
import static net.zethmayr.fungu.throwing.ThrowableHelper.raiseOrChecked;

/**
 * Presents the result of some evaluation,
 * either as the valid result or as an exception thrown.
 *
 * @param <T> the result type
 * @param <E> the exception type
 */
public interface Result<T, E extends Exception> extends Revenant<E, Result<T, E>> {
    /**
     * Returns a result if one is present.
     * In the case where the result is null as the result of successful evaluation,
     * no exception will be present.
     *
     * @return a result, or null.
     * @throws SunkenException if an exception is present.
     */
    T get();

    /**
     * Returns an exception if one is present.
     * No exception will be present if evaluation was successful.
     *
     * @return a thrown exception.
     */
    E getException();

    /**
     * Returns the result value if no exception is present,
     * or throws the exception.
     *
     * @return the result value.
     * @throws E as thrown by evaluation.
     */
    @Nullable
    default T getOrThrow() throws E {
        final E thrown = getException();
        if (nonNull(thrown)) {
            throw thrown;
        }
        return get();
    }

    @Override
    default void raise() throws E {
        getOrThrow();
    }

    /**
     * {@inheritDoc}
     * This implementation avoids unnecessary rethrows.
     */
    @Override
    default <C extends Exception> void raiseChecked(@NotNull final Class<C> check) throws C, SunkenException {
        final E thrown = getException();
        if (nonNull(thrown)) {
            raiseOrChecked(check, thrown);
        }
    }

    /**
     * {@inheritDoc}
     * This implementation avoids unnecessary rethrows.
     */
    @Override
    default <C extends Exception> Result<T, E> raiseOr(@NotNull final Class<C> check) throws C {
        final E thrown = getException();
        if (nonNull(thrown)) {
            raiseIfChecked(check, thrown);
        }
        return this;
    }

    /**
     * Returns the raw result as an {@link Object},
     * which may be either a thrown exception or a result value.
     *
     * @return the salient result
     */
    default Object getResult() {
        return Optional.ofNullable((Object) getException())
                .orElseGet(this::get);
    }
}
