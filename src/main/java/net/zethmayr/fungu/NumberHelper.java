package net.zethmayr.fungu;

import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseStaticsOnly;

/**
 * Common operations on numbers.
 */
public final class NumberHelper {
    private NumberHelper() {
        throw becauseStaticsOnly();
    }

    /**
     * Adds one to the given value.
     *
     * @param number an int.
     * @return the following int.
     */
    public static int increment(final int number) {
        return number + 1;
    }

    /**
     * Returns an integer increment operator.
     *
     * @return the increment operator.
     * @see #increment(int)
     */
    public static IntUnaryOperator increment() {
        return NumberHelper::increment;
    }

    /**
     * Subtracts one from the given value.
     *
     * @param number an int.
     * @return the preceding int.
     */
    public static int decrement(final int number) {
        return number - 1;
    }

    /**
     * Returns an integer decrement operator.
     *
     * @return the decrement operator.
     */
    public static IntUnaryOperator decrement() {
        return NumberHelper::decrement;
    }

    /**
     * Returns the values of the given numbers as {@code long}s
     *
     * @param boxed some numbers with long values.
     * @return the long values.
     */
    public static long[] longValues(final Number[] boxed) {
        final long[] longs = new long[boxed.length];
        IntStream.range(0, boxed.length)
                .parallel()
                .forEach(n ->
                        longs[n] = boxed[n].longValue()
                );
        return longs;
    }
}
