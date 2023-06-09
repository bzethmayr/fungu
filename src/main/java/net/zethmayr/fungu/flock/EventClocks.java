package net.zethmayr.fungu.flock;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.ToLongFunction;
import java.util.stream.IntStream;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseIllegal;
import static net.zethmayr.fungu.flock.FlockArrayUtilities.box;

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
    @Override
    default int compareTo(@NotNull final EventClocks compared) {
        return compareSimilarVectors(clocks(), compared.clocks());
    }

    /**
     * Compares two boxed event timestamp vectors in place.
     * Returns {@code -1} if the first vector strictly precedes (is obsoleted by) the second vector.
     * Returns {@code 0} if the vectors are the same.
     * Returns {@code 1} if the first vector strictly follows (obsoletes) the second vector.
     *
     * @param ourClocks   the first (commonly local) vector.
     * @param ourUnboxer  the unboxer for the first vector
     * @param theirClocks the second (commonly remote) vector.
     * @param theirUnboxer the unboxer for the second vector.
     * @param <T> the first vector's boxing type.
     * @param <U> the second vector's boxing type.
     * @return a comparison result.
     */
    static <T, U> int compareSimilarVectors(
            final T[] ourClocks, final ToLongFunction<T> ourUnboxer,
            final U[] theirClocks, final ToLongFunction<U> theirUnboxer
    ) {
        if (theirClocks.length != ourClocks.length) {
            throw becauseIllegal("Not comparable");
        }
        final AtomicBoolean someOursLesser = new AtomicBoolean(false);
        final AtomicBoolean someOursGreater = new AtomicBoolean(false);
        IntStream.range(0, theirClocks.length)
                .parallel()
                .forEach(n -> {
                    if (ourUnboxer.applyAsLong(ourClocks[n]) > theirUnboxer.applyAsLong(theirClocks[n])) {
                        someOursGreater.set(true);
                    }
                    if (ourUnboxer.applyAsLong(ourClocks[n]) < theirUnboxer.applyAsLong(theirClocks[n])) {
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

    /**
     * Compares boxed vs unboxed timestamp vectors,
     * boxing primitives.
     *
     * @param ourClocks   the boxed vector.
     * @param ourUnboxer the unboxer for the first vector
     * @param theirClocks the unboxed vector.
     * @param <T> the first vector's boxing type.
     * @return a comparison result.
     * @see #compareSimilarVectors(Object[], ToLongFunction, Object[], ToLongFunction)
     */
    static <T> int compareSimilarVectors(
            final T[] ourClocks, final ToLongFunction<T> ourUnboxer,
            final long[] theirClocks
    ) {
        return compareSimilarVectors(ourClocks, ourUnboxer, box(theirClocks), Long::longValue);
    }

    /**
     * Compares unboxed vs boxed timestamp vectors,
     * boxing primitives.
     *
     * @param ourClocks   the unboxed vector.
     * @param theirClocks the boxed vector.
     * @param theirUnboxer the unboxer for the second vector.
     * @param <U> the second vector's boxing type.
     * @return a comparison result.
     * @see #compareSimilarVectors(Object[], ToLongFunction, Object[], ToLongFunction)
     */
    static <U> int compareSimilarVectors(
            final long[] ourClocks,
            final U[] theirClocks,
            final ToLongFunction<U> theirUnboxer
    ) {
        return compareSimilarVectors(box(ourClocks), Long::longValue, theirClocks, theirUnboxer);
    }

    /**
     * Compares primitive vs primitive timestamp vectors.
     * @param ourClocks the first (commonly local) vector.
     * @param theirClocks the second (commonly remote) vector.
     * @return a comparison result
     * @see #compareSimilarVectors(Object[], ToLongFunction, Object[], ToLongFunction)
     */
    static int compareSimilarVectors(final long[] ourClocks, final long[] theirClocks) {
        return compareSimilarVectors(box(ourClocks), Long::longValue, box(theirClocks), Long::longValue);
    }
}
