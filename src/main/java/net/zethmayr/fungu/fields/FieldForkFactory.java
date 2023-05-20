package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.core.SuppressionConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseFactory;
import static net.zethmayr.fungu.core.SuppressionConstants.CHECK_ASSURED;

/**
 * Creates field-aware forks, such that
 * the top and bottom are associated with arbitrary field interfaces.
 */
public final class FieldForkFactory {

    private FieldForkFactory() {
        throw becauseFactory();
    }

    /**
     * Creates a new field-aware fork with the given types and contents.
     *
     * @param topHas     the top gettable field interface.
     * @param topType    the top value type.
     * @param top        the top value.
     * @param bottomHas  the bottom gettable field type.
     * @param bottomType the bottom value type.
     * @param bottom     the bottom value.
     * @param <T>        the top type.
     * @param <B>        the bottom type.
     * @return a new field-aware fork.
     */
    public static <T, B> FieldFork<T, B> fieldForkOf(
            @NotNull final Class<? extends HasX> topHas, @NotNull final Class<T> topType, @Nullable final T top,
            @NotNull final Class<? extends HasX> bottomHas, @NotNull final Class<B> bottomType, @Nullable final B bottom
    ) {
        return new FieldForkImpl<>(topHas, topType, top, bottomHas, bottomType, bottom);
    }

    @SuppressWarnings(CHECK_ASSURED)
    public static <T, B> FieldFork<T, B> fieldForkOf(
            @NotNull final Class<? extends HasX> topHas, @NotNull final T top,
            @NotNull final Class<? extends HasX> bottomHas, @NotNull final B bottom
    ) {
        return fieldForkOf(topHas, (Class<T>) top.getClass(), top, bottomHas, (Class<B>) bottom.getClass(), bottom);
    }

    private record FieldForkImpl<T, B>(
            Class<? extends HasX> topHas, Class<T> topType, T top,
            Class<? extends HasX> bottomHas, Class<B> bottomType, B bottom
    ) implements FieldFork<T, B> {

        @Override
        public FieldForkImpl<T, B> with(final T top, final B bottom) {
            return new FieldForkImpl<>(topHas, topType, top, bottomHas, bottomType, bottom);
        }
    }
}
