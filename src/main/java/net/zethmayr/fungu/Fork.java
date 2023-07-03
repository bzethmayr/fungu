package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;
import net.zethmayr.fungu.hypothetical.Nuple;

import java.util.Optional;

import static net.zethmayr.fungu.core.TypeHelper.typeOrDefault;

/**
 * A tuple with a top and bottom value.
 *
 * @param <T> the top type.
 * @param <B> the bottom type.
 */
public interface Fork<T, B> extends Nuple {
    /**
     * Exception message format for out-of-range nuple indices.
     */
    String NUPLE_INDEX_ERR_F = "Zero or one are allowed; not %s";

    /**
     * Returns the number of items in this tuple - 2.
     *
     * @return 2.
     */
    @Override
    default int arity() {
        return 2;
    }


    T top();

    B bottom();

    /**
     * Returns a new fork with
     * the same concrete type and values.
     * Implementations must create a new instance of their class.
     *
     * @param top    a top value
     * @param bottom a bottom value
     * @return a new fork of the same concrete type.
     */
    Fork<T, B> with(final T top, final B bottom);

    /**
     * Returns a new fork with
     * the same concrete type and top value
     * and a new bottom value.
     *
     * @param bottom the new bottom value.
     * @return a new fork of the same concrete type.
     */
    default Fork<T, B> withBottom(final B bottom) {
        return with(top(), bottom);
    }

    /**
     * Returns a new fork with
     * the same concrete type and bottom value
     * and a new top value.
     *
     * @param top the new top value.
     * @return a new fork of the same concrete type.
     */
    default Fork<T, B> withTop(final T top) {
        return with(top, bottom());
    }

    @Override
    default Class<?> nthRawType(final int zeroOrOne) {
        return switch (zeroOrOne) {
            case 0 -> typeOrDefault(top(), Void.class);
            case 1 -> typeOrDefault(bottom(), Void.class);
            default -> throw ExceptionFactory.becauseIllegal(NUPLE_INDEX_ERR_F, zeroOrOne);
        };
    }

    @Override
    default <X> Class<X> nthType(final int zeroOrOne, final Class<X> expectedType) {
        return Optional.of(nthRawType(zeroOrOne))
                .filter(expectedType::isAssignableFrom)
                .isPresent()
                ? expectedType
                : null;
    }

    @Override
    default Object nthRawMember(final int zeroOrOne) {
        return switch (zeroOrOne) {
            case 0 -> top();
            case 1 -> bottom();
            default -> throw ExceptionFactory.becauseIllegal(NUPLE_INDEX_ERR_F, zeroOrOne);
        };
    }

    @Override
    default <X> X nthMember(final int zeroOrOne, final Class<X> expectedType) {
        return Optional.ofNullable(nthRawMember(zeroOrOne))
                .filter(expectedType::isInstance)
                .map(expectedType::cast)
                .orElse(null);
    }
}
