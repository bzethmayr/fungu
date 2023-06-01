package net.zethmayr.fungu.flock;

import net.zethmayr.fungu.declarations.ReuseResults;
import net.zethmayr.fungu.flock.config.KnownMember;
import net.zethmayr.fungu.throwing.Sink;
import net.zethmayr.fungu.throwing.ThrowingFunction;

import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseImpossible;
import static net.zethmayr.fungu.flock.EventClocks.compareSimilarVectors;
import static net.zethmayr.fungu.flock.FlockArrayUtilities.rangeCheck;
import static net.zethmayr.fungu.flock.IgnorableNopException.becauseNotYetPossible;
import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static net.zethmayr.fungu.throwing.Sinkable.sinking;

/**
 * Implements vector clocks for a process.
 */
public class FlockClocks {

    private volatile int memberId;
    private final AtomicReference<FlockMember[]> clocks;

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
    public FlockClocks(final int memberId, final long[] clocks) {
        rangeCheck(memberId, clocks);
        this.memberId = memberId;
        this.clocks = new AtomicReference<>(
                LongStream.of(clocks)
                        .mapToObj(FlockMember::new)
                        .toArray(FlockMember[]::new)
        );
    }

    /**
     * Re-creates a member based on pre-existing configuration.
     *
     * @param memberId       the local member ID.
     * @param initialMembers some previous values.
     * @throws URISyntaxException if locations are not valid.
     */
    @ReuseResults
    public FlockClocks(final int memberId, final KnownMember[] initialMembers) throws URISyntaxException {
        rangeCheck(memberId, initialMembers);
        this.memberId = memberId;
        final Sink<URISyntaxException> badLocation = sink();
        final FlockMember[] initialClocks = new FlockMember[initialMembers.length];
        IntStream.range(0, initialMembers.length)
                .parallel()
                .forEach(n -> initialClocks[n] = sinking((ThrowingFunction<KnownMember, FlockMember, URISyntaxException>) FlockMember::new, badLocation)
                        .apply(initialMembers[n]));
        this.clocks = new AtomicReference<>(initialClocks);
        sink().raiseChecked(URISyntaxException.class);
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
            final FlockMember[] oldClocks = clocks.get(); // identical reference copy
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
    static void mergeValuesOntoCounters(final long[] sentClocks, final FlockMember[] oldClocks) {
        rangeCheck(sentClocks, oldClocks);
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
    static long[] clockValues(final FlockMember[] proposed) {
        return Stream.of(proposed)
                .mapToLong(FlockMember::getAcquire)
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
            final FlockMember[] moreClocks = proposeInsertion(newIndex);
            if ((sentClocks.length == moreClocks.length)
                    && compareSimilarVectors(sentClocks, moreClocks, FlockMember::getAcquire) != -1) {
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
     * a local-only insertion at the given index,
     * based on the local counts.
     *
     * @param newIndex an index no greater than the local vector length.
     * @return new proposed counters accommodating the new index.
     */
    FlockMember[] proposeInsertion(final int newIndex) {
        final FlockMember[] oldClocks = clocks.get();
        final FlockMember[] moreClocks = new FlockMember[oldClocks.length + 1];
        System.arraycopy(oldClocks, 0, moreClocks, 0, newIndex);
        moreClocks[newIndex] = new FlockMember(0L);
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
            final FlockMember[] lessClocks = proposeDeletion(oldIndex);
            if ((sentClocks.length == lessClocks.length)
                    && compareSimilarVectors(sentClocks, lessClocks, FlockMember::getAcquire) != -1) {
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
    FlockMember[] proposeDeletion(final int oldIndex) {
        final FlockMember[] oldClocks = clocks.get();
        final FlockMember[] lessClocks = new FlockMember[oldClocks.length - 1];
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
