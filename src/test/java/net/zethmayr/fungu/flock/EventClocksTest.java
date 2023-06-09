package net.zethmayr.fungu.flock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EventClocksTest {

    private record ExampleEventClocks(int memberId, long[] clocks) implements EventClocks {
    }

    private static EventClocks example(final int memberId, final long[] clocks) {
        return new ExampleEventClocks(memberId, clocks);
    }

    private EventClocks underTest;

    @Test
    void memberId_whenId_returnsId() {
        underTest = example(1, new long[]{0L});

        assertEquals(1, underTest.memberId());
    }

    @Test
    void clocks_whenNoClocks_returnsNull() {
        underTest = example(1, null);

        assertNull(underTest.clocks());
    }

    @Test
    void localClock_whenIdAndClocks_returnsLocalClock() {
        underTest = example(1, new long[]{0, 1, 2});

        assertEquals(1L, underTest.localClock());
    }

    @Test
    void compareTo_givenEqualClocks_returnsZero() {
        underTest = example(0, new long[]{1, 2, 3});
        final EventClocks comparison = example(1, new long[]{1, 2, 3});

        assertEquals(0, underTest.compareTo(comparison));
    }

    @Test
    void compareTo_givenRegressedClocks_returnsPositive() {
        underTest = example(0, new long[]{1, 2, 3});
        final EventClocks comparison = example(1, new long[]{1, 1, 3});

        assertEquals(1, underTest.compareTo(comparison));
    }

    @Test
    void compareTo_givenAdvancedClocks_returnsNegative() {
        underTest = example(0, new long[]{2, 3, 5});
        final EventClocks comparison = example(1, new long[]{3, 3, 5});

        assertEquals(-1, underTest.compareTo(comparison));
    }
}