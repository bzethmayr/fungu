package net.zethmayr.fungu.flock;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.zethmayr.fungu.NumberHelper.longValues;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseIllegal;

/**
 * Presents a stable view of the clock values for a given event.
 */
public interface EventClocks extends Comparable<EventClocks> {

    /**
     * Returns the member index where the event occurred.
     *
     * @return the member ID.
     */
    int memberId();

    /**
     * Returns the local clock values from the event.
     *
     * @return the clock values.
     */
    long[] clocks();

    /**
     * Returns the local sequence value from the event.
     *
     * @return the local clock.
     */
    default long localClock() {
        return clocks()[memberId()];
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation is a partial ordering.
     *
     * @see #compareSimilarVectors(long[], long[])
     */
    default int compareTo(@NotNull final EventClocks compared) {
        return compareSimilarVectors(clocks(), compared.clocks());
    }

    /**
     * Compares boxed vs boxed timestamp vectors.
     *
     * @param ourClocks   the first (commonly local) vector.
     * @param theirClocks the second (commonly remote) vector.
     * @return a comparison result.
     * @see #compareSimilarVectors(long[], long[])
     */
    static int compareSimilarVectors(final Number[] ourClocks, final Number[] theirClocks) {
        return compareSimilarVectors(longValues(ourClocks), longValues(theirClocks));
    }

    /**
     * Compares unboxed vs boxed timestamp vectors.
     *
     * @param ourClocks   the unboxed vector.
     * @param theirClocks the boxed vector.
     * @return a comparison result.
     * @see #compareSimilarVectors(long[], long[])
     */
    static int compareSimilarVectors(final long[] ourClocks, final Number[] theirClocks) {
        return compareSimilarVectors(ourClocks, longValues(theirClocks));
    }

    /**
     * Compares boxed vs unboxed timestamp vectors.
     *
     * @param ourClocks   the boxed vector.
     * @param theirClocks the unboxed vector.
     * @return a comparison result.
     * @see #compareSimilarVectors(long[], long[])
     */
    static int compareSimilarVectors(final Number[] ourClocks, final long[] theirClocks) {
        return compareSimilarVectors(longValues(ourClocks), theirClocks);
    }

    /**
     * Compares two event timestamp vectors.
     * Returns {@code -1} if the first vector strictly precedes (is obsoleted by) the second vector.
     * Returns {@code 0} if the vectors are the same.
     * Returns {@code 1} if the first vector strictly follows (obsoletes) the second vector.
     *
     * @param ourClocks   the first (commonly local) vector.
     * @param theirClocks the second (commonly remote) vector.
     * @return a comparison result.
     */
    static int compareSimilarVectors(final long[] ourClocks, final long[] theirClocks) {
        if (theirClocks.length != ourClocks.length) {
            throw becauseIllegal("Not comparable");
        }
        final AtomicBoolean someOursLesser = new AtomicBoolean(false);
        final AtomicBoolean someOursGreater = new AtomicBoolean(false);
        IntStream.range(0, theirClocks.length)
                .parallel()
                .forEach(n -> {
                    if (ourClocks[n] > theirClocks[n]) {
                        someOursGreater.set(true);
                    }
                    if (ourClocks[n] < theirClocks[n]) {
                        someOursLesser.set(true);
                    }
                });
        if (someOursLesser.get() && !someOursGreater.get()) {
            return -1;
        }
        if (someOursGreater.get() && !someOursLesser.get()) {
            return 1;
        }
        return 0;
    }
}
