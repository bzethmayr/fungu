package net.zethmayr.fungu.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static java.util.Objects.nonNull;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseStaticsOnly;

/**
 * Null-safe type accessors.
 */
public final class TypeHelper {
    private TypeHelper() {
        throw becauseStaticsOnly();
    }

    /**
     * Returns the concrete type of the given instance,
     * or the default type.
     *
     * @param inspected an instance.
     * @param nullType  the default type.
     * @return a non-null type.
     */
    @NotNull
    public static Class<?> typeOrDefault(
            @Nullable final Object inspected, @NotNull final Class<?> nullType) {
        return nonNull(inspected)
                ? inspected.getClass()
                : nullType;
    }

    /**
     * Returns a function that
     * returns the concrete type of arguments,
     * or the given type.
     *
     * @param nullType the default type.
     * @return a non-nullable typing function.
     */
    @NotNull
    public static Function<@Nullable Object, @NotNull Class<?>> defaultingType(@NotNull final Class<?> nullType) {
        return t -> typeOrDefault(t, nullType);
    }
}
