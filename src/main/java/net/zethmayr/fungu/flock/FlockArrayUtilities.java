package net.zethmayr.fungu.flock;

import java.util.stream.IntStream;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseIllegal;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseStaticsOnly;

final class FlockArrayUtilities {
    private FlockArrayUtilities() {
        throw becauseStaticsOnly();
    }


    static void rangeCheck(final long[] primary, final long[] secondary) {
        if (primary.length != secondary.length) {
            throw becauseDissimilar();
        }
    }

    static <T> void rangeCheck(final long[] primary, final T[] secondary) {
        if (primary.length != secondary.length) {
            throw becauseDissimilar();
        }
    }

    static void rangeCheck(final int index, final long[] values) {
        if (index >= values.length) {
            throw becauseOutOfRange(index);
        }
    }

    static <T> void rangeCheck(final int index, final T[] values) {
        if (index >= values.length) {
            throw becauseOutOfRange(index);
        }
    }

    static Long[] box(final long[] primitives) {
        final Long[] boxed = new Long[primitives.length];
        IntStream.range(0, primitives.length)
                .parallel()
                .forEach(n ->
                        boxed[n] = primitives[n]
                );
        return boxed;
    }

    static IllegalArgumentException becauseOutOfRange(final int index) {
        return becauseIllegal("ID %s is out of range", index);
    }

    static IllegalArgumentException becauseDissimilar() {
        return becauseIllegal("Cannot merge dissimilar vectors");
    }
}
