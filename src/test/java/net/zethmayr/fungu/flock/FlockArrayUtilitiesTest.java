package net.zethmayr.fungu.flock;

import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.flock.FlockArrayUtilities.rangeCheck;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlockArrayUtilitiesTest {

    @Test
    void rangeCheck_givenPrimitiveAndBoxedArray_whenLengthMismatch_throws() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->

                rangeCheck(new long[3], new String[2]));

        assertThat(thrown.getMessage(), containsString("dissimilar"));
    }

    @Test
    void rangeCheck_givenNegativeIndexAndPrimitiveArray_throws() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->

                rangeCheck(-1, new long[1]));

        assertThat(thrown.getMessage(), stringContainsInOrder("-1", "range"));
    }

    @Test
    void rangeCheck_givenExcessiveIndexAndPrimitiveArray_throws() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->

                rangeCheck(0, new long[0]));

        assertThat(thrown.getMessage(), stringContainsInOrder("0", "range"));
    }

    @Test
    void rangeCheck_givenNegativeIndexAndBoxedArray_throws() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->

                rangeCheck(-1, new Long[1]));

        assertThat(thrown.getMessage(), stringContainsInOrder("-1", "range"));
    }


    @Test
    void rangeCheck_givenExcessiveIndexAndBoxedArray_throws() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->

                rangeCheck(0, new Long[0]));

        assertThat(thrown.getMessage(), stringContainsInOrder("0", "range"));
    }
}