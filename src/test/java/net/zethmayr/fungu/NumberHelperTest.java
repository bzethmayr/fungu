package net.zethmayr.fungu;

import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.NumberHelper.decrement;
import static net.zethmayr.fungu.NumberHelper.increment;
import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumberHelperTest {

    @Test
    void numberHelper_whenInstantiated_throwsInstead() {

        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(NumberHelper.class));
    }

    @Test
    void increment_givenLong_returnsIncrementedLong() {
        final long initial = TEST_RANDOM.nextLong();
        final long expected = initial + 1;

        assertEquals(expected, increment(initial));
    }

    @Test
    void increment_givenInt_returnsIncrementedInt() {
        final int initial = TEST_RANDOM.nextInt();
        final int expected = initial + 1;

        assertEquals(expected, increment(initial));
    }

    @Test
    void decrement_givenLong_returnsDecrementedLong() {
        final long initial = TEST_RANDOM.nextLong();
        final long expected = initial - 1;

        assertEquals(expected, decrement(initial));
    }

    @Test
    void decrement_givenInt_returnsDecrementedInt() {
        final int initial = TEST_RANDOM.nextInt();
        final int expected = initial - 1;

        assertEquals(expected, decrement(initial));
    }
}
