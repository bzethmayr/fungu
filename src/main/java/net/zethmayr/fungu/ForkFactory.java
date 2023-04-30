package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ForkFactory {

    private ForkFactory() {
        throw ExceptionFactory.becauseFactory();
    }

    public static <T, B> Fork<T, B> forkOf(final T top, final B bottom) {
        return new SimpleFork<>(top, bottom);
    }

    public static <T, B> ReFork<T, B> reForkOf(
            @NotNull final Class<T> topType, final T top, @NotNull final Class<B> bottomType, final B bottom
    ) {
        return new TypedFork<>(topType, top, bottomType, bottom);
    }

    public static <T, B> ReFork<T, B> reForkOf(@NotNull final T top, @NotNull final B bottom) {
        return reForkOf((Class<T>)top.getClass(), top, (Class<B>) bottom.getClass(), bottom);
    }

    public static <T, B> Function<T, Fork<T, B>> overValue(final B value) {
        return t -> forkOf(t, value);
    }

    public static <T, B> Function<T, Fork<T, B>> over(final Supplier<B> source) {
        return t -> forkOf(t, source.get());
    }

    public static <T, B> Function<T, Fork<T, B>> over(final Function<T, B> mapping) {
        return t -> forkOf(t, mapping.apply(t));
    }

    public static <T, B, C> Function<Fork<T, B>, C> combine(final BiFunction<T, B, C> combining) {
        return f -> combining.apply(f.top(), f.bottom());
    }

    public static <T> Function<T, Fork<T, Long>> overOrdinal() {
        final AtomicLong order = new AtomicLong();

        return over(order::getAndIncrement);
    }

    public static <T> Function<T, Fork<T, T>> overPrior() {
        final AtomicReference<T> prior = new AtomicReference<T>();

        return over(prior::getAndSet);
    }

}
