package net.zethmayr.fungu.flock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.LongStream;

import static net.zethmayr.fungu.flock.FlockClocks.proposeInsertValues;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class FlockClocksTest {

    private FlockClocks underTest;

    private static long randomSequence() {
        return TEST_RANDOM.nextLong();
    }

    private static FlockClocks secondOfThree() {
        return new FlockClocks(1, LongStream.generate(FlockClocksTest::randomSequence).limit(3).toArray());
    }

    @Test
    void flockMember_givenEmptyArray_throwsOutOfRange() {

        assertThrows(IllegalArgumentException.class, () ->

                new FlockClocks(0, new long[]{}));
    }

    @Test
    void event_givenAnything_incrementsLocalCounter() {
        underTest = secondOfThree();
        final long prior = underTest.localClock();

        final long result = underTest.localEvent();

        assertThat(result, greaterThan(prior));
    }

    @Test
    void addMember_givenSequencePastId_addsNewMemberMovingEnd() {
        underTest = secondOfThree();
        final long priorLastValue = underTest.clocks()[2];

        assertDoesNotThrow(() ->
                underTest.addMember(2, underTest.proposeInsertValues(2)));

        assertEquals(priorLastValue, underTest.clocks()[3]);
    }

    @Test
    void addMember_givenSequencePastEnd_addsNewMemberAddingEnd() {
        underTest = secondOfThree();
        final long priorLastValue = underTest.clocks()[2];

        assertDoesNotThrow(() ->
                underTest.addMember(3, underTest.proposeInsertValues(3)));

        assertEquals(priorLastValue, underTest.clocks()[2]);
    }

    @Test
    void addMember_givenSequenceEqualToId_addsNewMemberMovingId() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.localClock();

        assertDoesNotThrow(() ->
                underTest.addMember(1, underTest.proposeInsertValues(1)));

        assertEquals(0L, underTest.clocks()[1]);
        assertEquals(priorLocalValue, underTest.localClock());
        assertEquals(priorLocalValue, underTest.clocks()[2]);
    }

    @Test
    void addMember_givenSequenceLessThanId_addsNewMemberMovingId() {
        underTest = secondOfThree();
        final long priorFirstValue = underTest.clocks()[0];
        final long priorLocalCount = underTest.localClock();

        assertDoesNotThrow(() ->
                underTest.addMember(0, underTest.proposeInsertValues(0)));

        assertEquals(0L, underTest.clocks()[0]);
        assertEquals(priorFirstValue, underTest.clocks()[1]);
        assertEquals(priorLocalCount, underTest.localClock());
    }

    @Test
    void addMember_whenOccursTwice_addsNewMemberThenIgnores() {
        underTest = secondOfThree();
        final long[] insertValues = underTest.proposeInsertValues(3);

        assertDoesNotThrow(() ->
                underTest.addMember(3, insertValues));
        assertEquals(4, underTest.clocks().length);

        assertThrows(PermanentNopException.class, () ->
                underTest.addMember(3, insertValues));
        assertEquals(4, underTest.clocks().length);
    }

    @Test
    void retireMember_givenSequenceLessThanId_removesMemberMovingId() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.localClock();

        assertDoesNotThrow(() ->
                underTest.retireMember(0, underTest.proposeDeleteValues(0)));

        assertEquals(priorLocalValue, underTest.clocks()[0]);
    }

    @Test
    void retireMember_givenSequenceEqualToId_throws() {
        underTest = secondOfThree();

        assertThrows(IllegalStateException.class, () ->
                underTest.retireMember(1, underTest.proposeDeleteValues(1)));
    }

    @Test
    void retireMember_givenSequencePastId_removesMember() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.localClock();

        assertDoesNotThrow(() ->
                underTest.retireMember(2, underTest.proposeDeleteValues(2)));

        assertEquals(priorLocalValue, underTest.clocks()[1]);
    }

    @Test
    void locally_givenRunnable_incrementsLocalAndRunsGiven() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.localClock();
        final AtomicLong trace = new AtomicLong();

        underTest.locally(() -> {
            trace.set(underTest.localClock());
        });

        assertThat(trace.get(), greaterThan(priorLocalValue));
    }

    @Test
    void locally_givenConsumer_incrementsLocalAndProvidesClocksToGiven() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.localEvent();
        final AtomicReference<EventClocks> trace = new AtomicReference<>();

        underTest.locally(trace::set);

        assertThat(trace.get().localClock(), greaterThan(priorLocalValue));
        assertEquals(1, trace.get().memberId());
        assertThat(trace.get().clocks()[1], greaterThan(priorLocalValue));
        assertThat(underTest.clocks()[0], equalTo(trace.get().clocks()[0]));
    }

    @Test
    void messageReceived_givenTooManyClocks_throws() {
        underTest = secondOfThree();
        final FlockClocks remote = new FlockClocks(2, underTest.clocks());
        assertDoesNotThrow(() ->
                remote.addMember(2, proposeInsertValues(2, remote.clocks())));

        assertThrows(RetryableNopException.class, () ->
                underTest.messageReceived(remote.clocks()));
    }

    @Test
    void messageReceived_givenAdvancedClock_mergesClocks() {
        underTest = secondOfThree();
        final long[] initial = underTest.clocks();
        final FlockClocks remote = new FlockClocks(0, initial);
        remote.localEvent();

        final EventClocks result = assertDoesNotThrow(() -> underTest.messageReceived(remote.clocks()));

        assertThat(result.clocks()[0], greaterThan(initial[0]));
        assertThat(result.clocks()[1], greaterThan(initial[1]));
        assertThat(result.clocks()[2], equalTo(initial[2]));
    }

    @Test
    void messageReceived_givenRemotelyAdvancedOwnClock_throws() {

    }
}
