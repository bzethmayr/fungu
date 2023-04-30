package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;
import net.zethmayr.fungu.hypothetical.Nuple;

import java.util.Optional;

import static net.zethmayr.fungu.TypeHelper.typeOrDefault;

public interface Fork<T, B> extends Nuple {
    String NUPLE_INDEX_ERR_F = "Zero or one are allowed; not %s";

    @Override
    default int arity() {
        return 2;
    }

    T top();
    B bottom();

    /**
     * Implementations must create a new instance of their class.
     * @param top a top value
     * @param bottom a bottom value
     * @return a new fork
     */
    Fork<T, B> with(final T top, final B bottom);

    default Fork<T, B> copy() {
        return with(top(), bottom());
    }

    default Fork<T, B> withBottom(final B bottom) {
        return with(top(), bottom);
    }

    default Fork<T, B> withTop(final T top) {
        return with(top, bottom());
    }

    default Class<?> nthRawType(final int zeroOrOne) {
        return switch (zeroOrOne) {
            case 0 -> typeOrDefault(top(), Void.class);
            case 1 -> typeOrDefault(bottom(), Void.class);
            default -> throw ExceptionFactory.becauseIllegal(NUPLE_INDEX_ERR_F, zeroOrOne);
        };
    }

    default <X> Class<X> nthType(final int zeroOrOne, final Class<X> expectedType) {
        return Optional.of(nthRawType(zeroOrOne))
                .filter(expectedType::isAssignableFrom)
                .isPresent()
                ? expectedType
                : null;
    }

    default Object nthRawMember(final int zeroOrOne) {
        return switch (zeroOrOne) {
            case 0 -> top();
            case 1 -> bottom();
            default -> throw ExceptionFactory.becauseIllegal(NUPLE_INDEX_ERR_F, zeroOrOne);
        };
    }

    default <X> X nthMember(final int zeroOrOne, final Class<X> expectedType) {
        return Optional.ofNullable(nthRawMember(zeroOrOne))
                .filter(expectedType::isInstance)
                .map(expectedType::cast)
                .orElse(null);
    }
}
