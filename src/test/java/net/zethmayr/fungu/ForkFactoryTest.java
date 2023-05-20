package net.zethmayr.fungu;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.zethmayr.fungu.ForkFactory.*;
import static net.zethmayr.fungu.test.TestConstants.*;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.*;

class ForkFactoryTest implements TestsFork {

    @Test
    void forkFactory_whenInstantiated_throws() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(ForkFactory.class));
    }

    @Test
    void overValue_givenValue_whenMappedOnValue_returnsForksOverValue() {
        Stream.of(EXPECTED, 0, null)

                .map(overValue(EXPECTED))

                .forEach(f -> assertEquals(EXPECTED, f.bottom()));
    }

    @Test
    void over_givenSource_whenMappedOnValues_returnsForksOverValues() {
        final Supplier<Integer> source = Arrays.asList(1, 2, 3).iterator()::next;
        Stream.of(3, 2, 1)

                .map(over(source))

                .forEach(f -> assertEquals(4, f.top() + f.bottom()));
    }

    @Test
    void over_givenFunction_whenMappedOnValues_returnsForksOverResults() {
        Stream.of(2, 3, 4)

                .map(over(n -> n * n))

                .forEach(f -> assertTrue(f.top() < f.bottom()));
    }

    void combine_givenBiFunction_whenMappedOnForks_combinesToValues() {
        Stream.of(
                forkOf("dinner", 4),
                forkOf("seafood", 3),
                forkOf("barbecue", 2))

                .map(combine((s, n) -> s.length() + n))

                .forEach(v -> assertEquals(10, v));
    }

    @Test
    void overOrdinal_givenNothing_whenMapped_returnsForksOverOrdinal() {
        Stream.of(4, 3, 2, 1)

                .map(overOrdinal())

                .forEach(f -> assertEquals(4, f.top() +  f.bottom()));
    }

    @Test
    void overPrior_givenNothing_whenMapped_returnsForksOverPrior() {
        Stream.of(1, 2, 3, 4)

                .map(overPrior())

                .forEach(f -> {
                    final int top = f.top();
                    if (top == 1) {
                        assertNull(f.bottom());
                    } else {
                        assertEquals(top - 1, f.bottom());
                    }
                });
    }

    @Test
    @Override
    public void equalValuedForksAreEqual() {
        final Fork<String, String> underTest = forkOf(EXPECTED, SHIBBOLETH);
        final Fork<String, String> equivalent = forkOf(EXPECTED, SHIBBOLETH);

        assertThat(underTest, not(sameInstance(equivalent)));
        assertEquals(equivalent, underTest);
        assertEquals(underTest, equivalent);
    }

    @Test
    @Override
    public void distinctValuedForksAreDistinct() {
        final Fork<String, String> underTest = forkOf(EXPECTED, EXPECTED);
        final Fork<String, String> distinct = forkOf(UNEXPECTED, UNEXPECTED);

        assertThat(underTest, not(sameInstance(distinct)));
        assertNotEquals(distinct, underTest);
        assertNotEquals(underTest, distinct);
    }
}
