package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;

public interface ReFork<T, B> extends Fork<T, B> {

    Class<T> topType();

    Class<B> bottomType();

    @Override
    default Class<?> nthRawType(final int zeroOrOne) {
        return switch (zeroOrOne) {
            case 0 -> topType();
            case 1 -> bottomType();
            default -> throw ExceptionFactory.becauseIllegal(NUPLE_INDEX_ERR_F, zeroOrOne);
        };
    }
}
