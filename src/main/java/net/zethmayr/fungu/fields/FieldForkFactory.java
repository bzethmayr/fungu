package net.zethmayr.fungu.fields;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseFactory;

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
            final Class<? extends HasX> topHas, final Class<T> topType, final T top,
            final Class<? extends HasX> bottomHas, final Class<B> bottomType, final B bottom
    ) {
        return new FieldForkImpl<>(topHas, topType, top, bottomHas, bottomType, bottom);
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
