package net.zethmayr.fungu.flock;

import net.zethmayr.fungu.declarations.ReuseResults;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseIllegal;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseImpossible;
import static net.zethmayr.fungu.flock.EventClocks.compareSimilarVectors;
import static net.zethmayr.fungu.flock.IgnorableNopException.becauseNotYetPossible;

/**
 * Implements vector clocks for a process.
 */
public class FlockMember {

    private volatile int memberId;
    private final AtomicReference<AtomicLong[]> clocks;

    private record ClockData(int memberId, long[] clocks) implements EventClocks {
    }

    private final Object lock = new Object();

    /**
     * Creates a new member based on the given ID and clocks.
     * A common constructional pattern might be similar to:
     * <pre>
     * final long[] knownClocks = ...;
     * final FlockMember(knownClocks.length, proposeInsertValues(knownClocks.length, knownClocks);
     * </pre>
     *
     * @param memberId the member ID.
     * @param clocks   the clock values.
     * @throws IllegalArgumentException if the ID is out of range of the given clocks.
     */
    @ReuseResults
    public FlockMember(final int memberId, final long[] clocks) {
        if (memberId >= clocks.length) {
            throw becauseIllegal("ID %s is out of range", memberId);
        }
        this.memberId = memberId;
        this.clocks = new AtomicReference<>(
                LongStream.of(clocks)
                        .mapToObj(AtomicLong::new)
                        .toArray(AtomicLong[]::new)
        );
    }

    /**
     * Creates a new member copying the given clock data.
     *
     * @param clockData event/clock data to copy.
     */
    @ReuseResults
    public FlockMember(final EventClocks clockData) {
        this(clockData.memberId(), clockData.clocks());
    }

    /**
     * For a local event,
     * increments the local clock
     * and returns the local sequence value.
     *
     * @return the new local sequence value.
     */
    public long localEvent() {
        synchronized (lock) {
            return clocks.get()[memberId].incrementAndGet();
        }
    }

    /**
     * For a message received,
     * increments the local clock
     * and updates clocks per received values.
     *
     * @param sentClocks the remote clock values.
     * @return updated local clocks from the message.
     * @throws IgnorableNopException if the clock vectors are different lengths.
     */
    public EventClocks messageReceived(final long[] sentClocks) throws IgnorableNopException {
        final EventClocks clockData;
        synchronized (lock) {
            final AtomicLong[] oldClocks = clocks.get(); // identical reference copy
            if (sentClocks.length == oldClocks.length) {
                localEvent();
                mergeValuesOntoCounters(sentClocks, oldClocks); // does not need to update reference
            } else {
                throw becauseNotYetPossible("Size disagreement");
            }
            clockData = clockData();
        }
        return clockData;
    }

    /**
     * Merges similar vectors of remote values onto local counters.
     *
     * @param sentClocks the remote values.
     * @param oldClocks  the local counters.
     */
    public static void mergeValuesOntoCounters(final long[] sentClocks, final AtomicLong[] oldClocks) {
        if (sentClocks.length != oldClocks.length) {
            throw becauseIllegal("Cannot merge dissimilar vectors");
        }
        IntStream.range(0, oldClocks.length)
                .parallel()
                .forEach(n -> {
                    if (sentClocks[n] > oldClocks[n].getAcquire()) {
                        /*
                         * in the case where the same number of dissimilar inserts have occurred,
                         * the results will at least temporarily converge to higher values...
                         */
                        oldClocks[n].set(sentClocks[n]);
                    }
                });
    }

    /**
     * For a message being sent,
     * increments the local clock
     * and returns the known clock values.
     * Messages being sent outside the clocked scope don't need this treatment,
     * and should be treated as {@link #localEvent() local events}.
     *
     * @return the clock data for a message.
     */
    public EventClocks messageClocks() {
        /*
         * Note that the internal use of this method
         * is not semantically correct.
         */
        synchronized (lock) {
            localEvent();
            return clockData();
        }
    }

    /**
     * Returns a copy of the clock data as of the time of call.
     *
     * @return a clock data snapshot.
     */
    public EventClocks clockData() {
        synchronized (lock) {
            return new ClockData(memberId, clocks());
        }
    }

    /**
     * Runs a local event that
     * will not make reference to the clock values
     * from when it is run.
     * <p>
     * Note that sampling the local values from within the event
     * has no guarantee of returning relevant values.
     *
     * @param event a local event.
     */
    public void locally(final Runnable event) {
        localEvent();
        event.run();
    }

    /**
     * Runs a local event that
     * receives the relevant clock data
     * from when it is run.
     * <p>
     * Note that sampling the local values from within the event
     * has no guarantee of returning relevant values.
     * Events still need to use {@link #messageClocks()} instead of the provided values
     * when sending a message to other nodes.
     *
     * @param clockedEvent an event receiving a unique set of clock values.
     */
    public void locally(final Consumer<EventClocks> clockedEvent) {
        final EventClocks clocks;
        synchronized (lock) {
            // this usage is non-semantic
            clocks = messageClocks();
        }
        clockedEvent.accept(clocks);
    }

    /**
     * Returns the current local clock value,
     * with no further consistency guarantees.
     *
     * @return the local clock value.
     */
    public long localClock() {
        synchronized (lock) {
            return clocks.get()[memberId].getAcquire();
        }
    }

    /**
     * Returns the current known clock values,
     * with no further consistency guarantees.
     *
     * @return the known clock values.
     */
    public long[] clocks() {
        return clockValues(clocks.get());
    }

    /**
     * Returns the current values from the given counters,
     * without strong consistency guarantees -
     * interleaved increments may still occur.
     *
     * @param proposed some clock counters.
     * @return the counter values.
     */
    public static long[] clockValues(final AtomicLong[] proposed) {
        return Stream.of(proposed)
                .mapToLong(AtomicLong::getAcquire)
                .toArray();
    }

    /**
     * Adds a node at the given index.
     * Before a node sends a message with a new member to another node,
     * it must have sent an addMember to that node.
     *
     * @param newIndex   an index no greater than the local vector length.
     * @param sentClocks proposed new minimum clocks.
     */
    public void addMember(final int newIndex, final long[] sentClocks) {
        synchronized (lock) {
            final AtomicLong[] moreClocks = proposeInsertion(newIndex);
            if (compareSimilarVectors(sentClocks, moreClocks) != -1) {
                mergeValuesOntoCounters(sentClocks, moreClocks);
                clocks.set(moreClocks);
                if (memberId >= newIndex) {
                    memberId += 1;
                }
            }
        }
    }

    /**
     * Returns new proposed counters with
     * an insertion at the given index,
     * based on the local counts.
     *
     * @param newIndex an index no greater than the local vector length.
     * @return new proposed counters accommodating the new index.
     */
    public AtomicLong[] proposeInsertion(final int newIndex) {
        final AtomicLong[] oldClocks = clocks.get();
        final AtomicLong[] moreClocks = new AtomicLong[oldClocks.length + 1];
        System.arraycopy(oldClocks, 0, moreClocks, 0, newIndex);
        moreClocks[newIndex] = new AtomicLong();
        if (newIndex < oldClocks.length) {
            System.arraycopy(oldClocks, newIndex, moreClocks, newIndex + 1, oldClocks.length - newIndex);
        }
        return moreClocks;
    }

    /**
     * Returns new proposed values with
     * an insertion at the given index,
     * based on the given values.
     *
     * @param newIndex  an index no greater than the number of values.
     * @param oldClocks existing clock values.
     * @return new proposed values accommodating the new index.
     */
    public static long[] proposeInsertValues(final int newIndex, final long[] oldClocks) {
        final long[] moreClocks = new long[oldClocks.length + 1];
        System.arraycopy(oldClocks, 0, moreClocks, 0, newIndex);
        moreClocks[newIndex] = 0L;
        if (newIndex < oldClocks.length) {
            System.arraycopy(oldClocks, newIndex, moreClocks, newIndex + 1, oldClocks.length - newIndex);
        }
        return moreClocks;
    }

    /**
     * Returns new proposed values with
     * an insertion at the given index,
     * based on the local counts.
     *
     * @param newIndex an index no greater than the local vector length.
     * @return new proposed values accommodating the new index.
     */
    public long[] proposeInsertValues(final int newIndex) {
        return clockValues(proposeInsertion(newIndex));
    }

    /**
     * Removes the node at the given index.
     * Before a node sends a message with fewer members to another node,
     * it needs to send a removal to that node.
     *
     * @param oldIndex   an index smaller than the local vector length.
     * @param sentClocks proposed new minimum counts.
     */
    public void retireMember(final int oldIndex, final long[] sentClocks) {
        synchronized (lock) {
            if (memberId == oldIndex) {
                throw becauseImpossible("This instance %s is already presumed dead", memberId);
            }
            final AtomicLong[] lessClocks = proposeDeletion(oldIndex);
            if (compareSimilarVectors(sentClocks, lessClocks) != -1) {
                mergeValuesOntoCounters(sentClocks, lessClocks);
                clocks.set(lessClocks);
                if (memberId > oldIndex) {
                    memberId -= 1;
                }
            }
        }
    }

    /**
     * Returns new proposed counters with
     * a deletion at the given index,
     * based on the local counts.
     *
     * @param oldIndex an index smaller than the local vector length.
     * @return new proposed counters removing the given index.
     */
    public AtomicLong[] proposeDeletion(final int oldIndex) {
        final AtomicLong[] oldClocks = clocks.get();
        final AtomicLong[] lessClocks = new AtomicLong[oldClocks.length - 1];
        System.arraycopy(oldClocks, 0, lessClocks, 0, oldIndex);
        if (oldIndex < lessClocks.length) {
            System.arraycopy(oldClocks, oldIndex + 1, lessClocks, oldIndex, lessClocks.length - oldIndex);
        }
        return lessClocks;
    }

    /**
     * Returns new proposed values with
     * a deletion at the given index,
     * based on the local counts.
     *
     * @param oldIndex an index smaller than the local vector length.
     * @return new proposed values removing the given index.
     */
    public long[] proposeDeleteValues(final int oldIndex) {
        return clockValues(proposeDeletion(oldIndex));
    }
}
