package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.nonNull;

public final class TypeHelper {
    private TypeHelper() {
        throw ExceptionFactory.becauseStaticsOnly();
    }

    @NotNull
    public static Class<?> typeOrDefault(
            @Nullable final Object inspected, @NotNull final Class<?> nullType) {
        return nonNull(inspected)
                ? inspected.getClass()
                : nullType;
    }
}
