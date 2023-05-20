package net.zethmayr.fungu;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseIllegal;

/**
 * A tuple with explicit type information for its top and bottom values.
 *
 * @param <T> the top type.
 * @param <B> the bottom type.
 */
public interface TypedFork<T, B> extends Fork<T, B> {

    /**
     * The top type per construction.
     *
     * @return the top type.
     */
    Class<T> topType();

    /**
     * The bottom type per construction.
     *
     * @return the bottom type.
     */
    Class<B> bottomType();

    /**
     * {@inheritDoc}
     */
    @Override
    default Class<?> nthRawType(final int zeroOrOne) {
        return switch (zeroOrOne) {
            case 0 -> topType();
            case 1 -> bottomType();
            default -> throw becauseIllegal(NUPLE_INDEX_ERR_F, zeroOrOne);
        };
    }
}
