package net.zethmayr.fungu.flock;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static net.zethmayr.fungu.ForkFactory.overOrdinal;
import static net.zethmayr.fungu.flock.FlockTreeUtilities.compareScattered;
import static net.zethmayr.fungu.flock.FlockTreeUtilities.nthScatteredIndex;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class FlockTreeUtilitiesTest {

    @Test
    void flockTreeUtilities_whenInstantiated_throwsInstead() {

        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(FlockTreeUtilities.class));
    }

    @Test
    void nthIndex_givenSequenceZero_returnsZero() {

        assertEquals(0, nthScatteredIndex(0));
    }

    @Test
    void nthIndex_givenSequenceOne_returnsMinimum() {
        assertEquals(MIN_VALUE, nthScatteredIndex(1));
    }

    @Test
    void nthIndex_givenSequenceTwo_returnsPositiveMidpoint() {
        assertEquals(MAX_VALUE / 2 + 1, nthScatteredIndex(2));
    }


    @Test
    void nthIndex_givenOrderedSequences_returnsComparatorOrderedIndices() {
        final int[] results = new int[512];

        IntStream.rangeClosed(0, 511)
                .forEach(n -> results[n] = nthScatteredIndex(n));

        IntStream.range(1, 511)
                .forEach(n -> assertEquals(-1, compareScattered(results[n - 1], results[n])));
    }

    @Test
    void nthIndex_givenOrderedSequencesWithMax_returnsComparatorOrderedIndices() {
        final int[] results = new int[512];

        IntStream.rangeClosed(0, 511)
                .forEach(n -> results[n] = nthScatteredIndex(n, 512));

        IntStream.range(1, 511)
                .forEach(n -> {
                    assertEquals(-1, compareScattered(results[n - 1], results[n]));
                    assertThat(n, greaterThanOrEqualTo(-512));
                    assertThat(n, lessThanOrEqualTo(512));
                });
    }

    @Test
    void compareScattered_asComparator_givenNthIndicesWithMax_returnsSameOrderInRangeWithoutDuplicates() {
        final int[] order = new int[1024];
        final NavigableSet<Integer> results = new TreeSet<>(FlockTreeUtilities::compareScattered);

        IntStream.rangeClosed(0, 1023)
                .forEach(n -> results.add(order[n] = nthScatteredIndex(n, 1024)));

        assertThat(results, hasSize(1024));
        results.stream().map(overOrdinal()).forEach(f -> {
            assertEquals(order[(int) (long) f.bottom()], f.top());
            assertThat(f.top(), greaterThanOrEqualTo(-1024));
            assertThat(f.top(), lessThanOrEqualTo(1024));
        });
    }

    @Test
    void compareScattered_asComparator_givenSequencePastMax_immediatelyReturnsOutOfOrder() {
        final NavigableSet<Integer> results = new TreeSet<>(FlockTreeUtilities::compareScattered);
        final int[] order = new int[1025];

        IntStream.range(0, 1024)
                .forEach(n -> results.add(order[n] = nthScatteredIndex(n, 1024)));
        assertThat(results, hasSize(1024));
        results.stream().map(overOrdinal()).forEach(f ->
                assertEquals(order[(int) (long) f.bottom()], f.top()));

        results.add(order[1024] = nthScatteredIndex(1024, 1024));
        assertThat(results, hasSize(1025));
        results.stream().limit(1).map(overOrdinal()).forEach(f ->
                assertNotEquals(order[(int) (long) f.bottom()], f.top()));
        results.stream().skip(1).map(overOrdinal()).forEach(f ->
                assertEquals(order[(int) (long) f.bottom()], f.top()));
    }

    @Test
    void compareScattered_asComparator_givenNthIndices_returnsSameIterationOrder() {
        final NavigableSet<Integer> results = new TreeSet<>(FlockTreeUtilities::compareScattered);
        final int[] order = new int[256];

        IntStream.rangeClosed(0, 255)
                .forEach(n -> results.add(order[n] = nthScatteredIndex(n)));

        assertThat(results.size(), is(256));
        results.stream()
                .map(overOrdinal())
                .forEach(f ->
                    assertEquals(f.top(), order[(int) (long) f.bottom()])
                );
    }
}
