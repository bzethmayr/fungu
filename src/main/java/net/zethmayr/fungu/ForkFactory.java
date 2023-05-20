package net.zethmayr.fungu;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseFactory;

/**
 * Creates simple and/or type-aware forks,
 * and provides some related utility methods.
 *
 * The family {@code forkOf} creates new values.
 * The family {@code over} returns mappers from top values to forks.
 * Since the difference is arbitrary, there is no family {@code under}.
 *
 * @see Fork
 */
public final class ForkFactory {

    private ForkFactory() {
        throw becauseFactory();
    }

    /**
     * Creates a simple fork
     * with the given values.
     *
     * @param top    the top value.
     * @param bottom the bottom value.
     * @param <T>    the top type.
     * @param <B>    the bottom type.
     * @return a new simple fork.
     */
    public static <T, B> Fork<T, B> forkOf(final T top, final B bottom) {
        return new SimpleFork<>(top, bottom);
    }

    /**
     * Creates a reifiable (type-aware) fork
     * with the given types and values.
     *
     * @param topType    the top type.
     * @param top        the top value.
     * @param bottomType the bottom type.
     * @param bottom     the bottom value.
     * @param <T>        the top type.
     * @param <B>        the bottom type.
     * @return a new type-aware fork.
     */
    public static <T, B> TypedFork<T, B> reForkOf(
            @NotNull final Class<T> topType, final T top, @NotNull final Class<B> bottomType, final B bottom
    ) {
        return new SimpleTypedFork<>(topType, top, bottomType, bottom);
    }

    /**
     * Creates a reifiable (type-aware) fork
     * with inferred types and the given values.
     *
     * @param top    the top value.
     * @param bottom the bottom value.
     * @param <T>    the top type.
     * @param <B>    the bottom type.
     * @return a new type-aware fork.
     */
    public static <T, B> TypedFork<T, B> reForkOf(@NotNull final T top, @NotNull final B bottom) {
        return reForkOf((Class<T>) top.getClass(), top, (Class<B>) bottom.getClass(), bottom);
    }

    /**
     * Returns a function which
     * returns simple forks
     * with the given bottom value and a new top value.
     *
     * @param value the bottom value.
     * @param <T>   the top type.
     * @param <B>   the bottom type.
     * @return a top-to-fork function
     */
    public static <T, B> Function<T, Fork<T, B>> overValue(final B value) {
        return t -> forkOf(t, value);
    }

    /**
     * Returns a function which
     * returns simple forks
     * with a supplied bottom value and a new top value.
     *
     * @param source the bottom value source.
     * @param <T>    the top type.
     * @param <B>    the bottom type.
     * @return a top-to-fork function.
     */
    public static <T, B> Function<T, Fork<T, B>> over(final Supplier<B> source) {
        return t -> forkOf(t, source.get());
    }

    /**
     * Returns a function which
     * returns simple forks
     * with a calculated bottom value per the new top value.
     *
     * @param mapping the bottom-calculating function.
     * @param <T>     the top type.
     * @param <B>     the bottom type.
     * @return a top-to-fork function.
     */
    public static <T, B> Function<T, Fork<T, B>> over(final Function<T, B> mapping) {
        return t -> forkOf(t, mapping.apply(t));
    }

    /**
     * Returns a function which
     * combines forks to single values.
     *
     * @param combining a function that combines the top and bottom types.
     * @param <T>       the top type.
     * @param <B>       the bottom type.
     * @param <C>       some combined type.
     * @return a fork-combining function.
     */
    public static <T, B, C> Function<Fork<T, B>, C> combine(final BiFunction<T, B, C> combining) {
        return f -> combining.apply(f.top(), f.bottom());
    }

    /**
     * Returns a (stateful) function which
     * returns a fork with
     * a new top value and the encounter order as bottom value.
     *
     * @param <T> the top type.
     * @return a top-to-fork function.
     */
    public static <T> Function<T, Fork<T, Long>> overOrdinal() {
        final AtomicLong order = new AtomicLong();

        return over(order::getAndIncrement);
    }

    /**
     * Returns a (stateful) function which
     * returns a fork with
     * a new top value and the prior value, if any, as bottom value.
     *
     * @param <T> the top type.
     * @return a top-to-fork function.
     */
    public static <T> Function<T, Fork<T, T>> overPrior() {
        final AtomicReference<T> prior = new AtomicReference<T>();

        return over(prior::getAndSet);
    }
}
