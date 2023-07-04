package net.zethmayr.fungu.flock;

import static java.lang.Integer.*;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseStaticsOnly;

/**
 * Provides integer-typed utility methods for working with scattered indices.
 */
public final class FlockTreeUtilities {

    private FlockTreeUtilities() {
        throw becauseStaticsOnly();
    }

    /**
     * Returns a scattered index from a sequence value.
     * When there is a maximum practicable cluster size,
     * leaving some number of slack bits vs the theoretical maximum,
     * this value can be divided by {@literal 2 ^ slack bits} to reduce message space.
     *
     * @param sequence a sequence value.
     * @return a scattered index.
     */
    public static int nthScatteredIndex(final int sequence) {
        return reverse(sequence);
    }

    /**
     * Returns a scattered index from a sequence value,
     * accounting for a maximum practicable cluster size.
     *
     * @param sequence a sequence value.
     * @param maxSequence  the maximum practical cluster size.
     * @return a scattered index.
     */
    public static int nthScatteredIndex(final int sequence, final int maxSequence) {
        return nthScatteredIndex(sequence) / divisor(maxSequence);
    }

    private static int divisor(final int maxSequence) {
        return MAX_VALUE / highestOneBit(maxSequence);
    }

    /**
     * Compares two scattered indices.
     *
     * @param first  the first index.
     * @param second the second index.
     * @return a comparison result.
     * @see Integer#compare(int, int)
     */
    public static int compareScattered(final int first, final int second) {
        // there's no guarantee that this is reversible, but, it does happen to be.
        return compare(nthScatteredIndex(first), nthScatteredIndex(second));
    }
}
