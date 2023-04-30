package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class CoalescenceHelper {

    private CoalescenceHelper() {
        throw ExceptionFactory.becauseStaticsOnly();
    }

    @Nullable
    @SafeVarargs
    public static <T> T coalesce(final @Nullable T... values) {
        return Stream.of(values)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @NotNull
    @SafeVarargs
    public static <T> T defaultUnless(@NotNull final T defaultValue, final @Nullable T... values) {
        return Optional.ofNullable(coalesce(values))
                .orElse(defaultValue);
    }

    @NotNull
    @SafeVarargs
    public static <T> Supplier<@Nullable T> coalesces(final Supplier<@Nullable T>... sources) {
        return () -> Stream.of(sources)
                .map(Supplier::get)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @NotNull
    @SafeVarargs
    public static <T> Supplier<@NotNull T> defaultsUnless(
            @NotNull final T defaultValue, final Supplier<@Nullable T>... sources
    ) {
        final Supplier<T> coalesces = coalesces(sources);
        return () -> defaultUnless(defaultValue, coalesces.get());
    }

    @Nullable
    @SafeVarargs
    public static <T> T coalesced(final Supplier<@Nullable T>... sources) {
        return coalesces(sources).get();
    }

    @NotNull
    @SafeVarargs
    public static <T> T defaultedUnless(
            @NotNull final T defaultValue,
            final Supplier<@Nullable T>... sources
    ) {
        return defaultUnless(defaultValue, coalesced(sources));
    }

    @NotNull
    @SafeVarargs
    public static <T, R> Function<T, @Nullable R> coalescing(
            final Function<@NotNull T, @Nullable R>... accesses
    ) {
        return t -> Stream.of(accesses)
                .map(f -> f.apply(t))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @NotNull
    @SafeVarargs
    public static <T, R> Function<T, @NotNull R> defaultingUnless(
            @NotNull final R defaultResult, final Function<@NotNull T, @Nullable R>... accesses
    ) {
        final Function<T, R> coalescing = coalescing(accesses);
        return t -> defaultUnless(defaultResult, coalescing.apply(t));
    }

    @Nullable
    @SafeVarargs
    public static <T, R> R coalesced(
            @NotNull final T source, final Function<@NotNull T, @Nullable R>... accesses
    ) {
        return coalescing(accesses).apply(source);
    }

    @NotNull
    @SafeVarargs
    public static <T, R> R defaultedUnless(
            @NotNull final T source, @NotNull final R defaultResult, final Function<@NotNull T, @Nullable R>... accesses
    ) {
        return defaultUnless(defaultResult, coalesced(source, accesses));
    }
}
