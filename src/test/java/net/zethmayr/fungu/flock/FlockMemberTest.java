package net.zethmayr.fungu.flock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlockMemberTest {

    private FlockMember underTest;

    private static long randomSequence() {
        return TEST_RANDOM.nextLong();
    }

    private static FlockMember secondOfThree() {
        return new FlockMember(1, LongStream.generate(FlockMemberTest::randomSequence).limit(3).toArray());
    }

    @Test
    void event_givenAnything_incrementsLocalCounter() {
        underTest = secondOfThree();
        final long prior = underTest.localClock();

        final long result = underTest.event();

        assertThat(result, greaterThan(prior));
    }

    @Test
    void addMember_givenSequencePastId_addsNewMemberMovingEnd() {
        underTest = secondOfThree();
        final long priorLastValue = underTest.clocks()[2];

        underTest.addMember(2);

        assertEquals(priorLastValue, underTest.clocks()[3]);
    }

    @Test
    void addMember_givenSequencePastEnd_addsNewMemberAddingEnd() {
        underTest = secondOfThree();
        final long priorLastValue = underTest.clocks()[2];

        underTest.addMember(3);

        assertEquals(priorLastValue, underTest.clocks()[2]);
    }

    @Test
    void addMember_givenSequenceEqualToId_addsNewMemberMovingId() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.localClock();

        underTest.addMember(1);

        assertEquals(0L, underTest.clocks()[1]);
        assertEquals(priorLocalValue, underTest.localClock());
        assertEquals(priorLocalValue, underTest.clocks()[2]);
    }

    @Test
    void addMember_givenSequenceLessThanId_addsNewMemberMovingId() {
        underTest = secondOfThree();
        final long priorFirstValue = underTest.clocks()[0];
        final long priorLocalCount = underTest.localClock();

        underTest.addMember(0);

        assertEquals(0L, underTest.clocks()[0]);
        assertEquals(priorFirstValue, underTest.clocks()[1]);
        assertEquals(priorLocalCount, underTest.localClock());
    }

    @Test
    void retireMember_givenSequenceLessThanId_removesMemberMovingId() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.localClock();

        underTest.retireMember(0);

        assertEquals(priorLocalValue, underTest.clocks()[0]);
    }

    @Test
    void retireMember_givenSequenceEqualToId_throws() {
        underTest = secondOfThree();

        assertThrows(IllegalStateException.class, () ->
                underTest.retireMember(1));
    }

    @Test
    void retireMember_givenSequencePastId_removesMember() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.localClock();

        underTest.retireMember(2);

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
    void locally_givenSequenceConsumer_incrementsLocalAndProvidesToGiven() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.event();
        final AtomicLong trace = new AtomicLong();

        underTest.locally(trace::set);

        assertThat(trace.get(), greaterThan(priorLocalValue));
    }

    @Test
    void locally_givenClocksConsumer_incrementsLocalAndProvidesClocksToGiven() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.event();

        underTest.locally((long[] a) ->

                assertThat(a[1], greaterThan(priorLocalValue)));
    }

    @Test
    void locally_givenSequenceAndClocksConsumer_incrementsLocalAndProvidesAllToGiven() {
        underTest = secondOfThree();
        final long priorLocalValue = underTest.localClock();

        underTest.locally((n, a) -> {

            assertThat(n, greaterThan(priorLocalValue));
            assertEquals(n, a[1]);
        });
    }
}
