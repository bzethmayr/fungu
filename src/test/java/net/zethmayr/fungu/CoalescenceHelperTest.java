package net.zethmayr.fungu;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.function.Function.identity;
import static net.zethmayr.fungu.CoalescenceHelper.*;
import static net.zethmayr.fungu.core.SupplierFactory.from;
import static net.zethmayr.fungu.test.TestConstants.*;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.*;

class CoalescenceHelperTest {

    @Test
    void coalescenceHelper_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(CoalescenceHelper.class));
    }

    @Test
    void coalesce_givenNull_returnsNull() {

        assertNull(coalesce(NULL_OBJECT));
    }

    @Test
    void coalesce_givenValue_returnsValue() {

        assertEquals(EXPECTED, coalesce(EXPECTED));
    }

    @Test
    void coalesce_givenValues_returnsFirst() {

        assertEquals(EXPECTED, coalesce(EXPECTED, UNEXPECTED));
    }

    @Test
    void coalesce_givenNullAndValue_returnsValue() {

        assertEquals(EXPECTED, coalesce(NULL_OBJECT, EXPECTED));
    }

    @Test
    void coalesce_givenNulls_returnsNull() {

        assertNull(coalesce(NULL_OBJECT, NULL_OBJECT));
    }

    @Test
    void defaultUnless_givenDefaultAndNull_returnsDefault() {

        assertEquals(EXPECTED, defaultUnless(EXPECTED, NULL_OBJECT));
    }

    @Test
    void defaultUnless_givenDefaultAndValue_returnsValue() {

        assertEquals(EXPECTED, defaultUnless(UNEXPECTED, EXPECTED));
    }

    @Test
    void defaultUnless_givenDefaultAndValueSecond_returnsValue() {

        assertEquals(EXPECTED, defaultUnless(UNEXPECTED, NULL_OBJECT, EXPECTED));
    }

    @Test
    void coalesces_givenSourceWithNull_returnsSupplier_returningNull() {
        final Supplier<String> withNull = from(NULL_STRING);

        final Supplier<String> underTest = coalesces(withNull);

        assertNull(underTest.get());
    }

    @Test
    void coalesces_givenSourceWithValue_returnsSupplier_returningValue() {
        final Supplier<String> withValue = from(EXPECTED);

        final Supplier<String> underTest = coalesces(withValue);

        assertEquals(EXPECTED, underTest.get());
    }

    @Test
    void coalesces_givenSourcesWithNullAndValue_returnsSupplier_returningValue() {

        final Supplier<String> underTest = coalesces(from(NULL_STRING), from(EXPECTED));

        assertEquals(EXPECTED, underTest.get());
    }

    @Test
    void coalesces_givenSourcesWithValues_returnsSupplier_returningFirstValue() {

        final Supplier<String> underTest = coalesces(from(EXPECTED), from(UNEXPECTED));

        assertEquals(EXPECTED, underTest.get());
    }

    @Test
    void defaultsUnless_givenDefaultAndSourceWithNull_returnsSupplier_returningDefault() {

        final Supplier<String> underTest = defaultsUnless(EXPECTED, from(NULL_STRING));

        assertEquals(EXPECTED, underTest.get());
    }

    @Test
    void defaultsUnless_givenDefaultAndSourceWithValue_returnsSupplier_returningValue() {

        final Supplier<String> underTest = defaultsUnless(UNEXPECTED, from(EXPECTED));

        assertEquals(EXPECTED, underTest.get());
    }

    @Test
    void coalesced_givenSourcesWithNull_returnsNull() {

        assertNull(coalesced(from(NULL_STRING), from(NULL_STRING)));
    }

    @Test
    void coalesced_givenSourcesWithValues_returnsFirstValue() {

        assertEquals(EXPECTED, coalesced(from(EXPECTED), from(UNEXPECTED)));
    }

    @Test
    void defaultedUnless_givenDefaultAndSourcesWithNull_returnsDefault() {

        assertEquals(EXPECTED, defaultedUnless(EXPECTED, from(NULL_STRING), from(NULL_STRING)));
    }

    @Test
    void defaultedUnless_givenDefaultAndSourcesWithValues_returnsFirstValue() {

        assertEquals(EXPECTED, defaultedUnless(UNEXPECTED, from(EXPECTED), from(UNEXPECTED)));
    }

    @Test
    void coalescing_givenAccessReturningNull_givenSource_returnsNull() {

        final Function<String, Integer> underTest = coalescing(s -> null);

        assertNull(underTest.apply(UNEXPECTED));
    }

    @Test
    void coalescing_givenAccessReturningValue_givenSource_returnsValue() {

        final Function<String, Integer> underTest = coalescing(String::length);

        assertEquals(8, underTest.apply(EXPECTED));
    }

    @Test
    void coalescing_givenAccessesReturningNullAndValue_givenSource_returnsValue() {

        final Function<String, Integer> underTest = coalescing(s -> null, String::length);

        assertEquals(8, underTest.apply(EXPECTED));
    }

    @Test
    void coalescing_givenAccessesReturningValues_givenSource_returnsFirstResult() {

        final Function<String, Integer> underTest = coalescing(String::length, String::hashCode);

        assertEquals(8, underTest.apply(EXPECTED));
    }

    @Test
    void defaultingUnless_givenDefaultAndAccessReturningNull_givenSource_returnsDefault() {

        final Function<String, Integer> underTest = defaultingUnless(7, s -> null);

        assertEquals(7, underTest.apply(UNEXPECTED));
    }

    @Test
    void defaultingUnless_givenDefaultAndAccessReturningValue_givenSource_returnsResult() {

        final Function<String, Integer> underTest = defaultingUnless(7, String::length);

        assertEquals(8, underTest.apply(EXPECTED));
    }

    @Test
    void defaultedUnless_givenSourceAndDefaultAndAccessReturningNull_returnsDefault() {

        assertEquals(EXPECTED, defaultedUnless(UNEXPECTED, EXPECTED, s -> null));
    }

    @Test
    void defaultedUnless_givenSourceAndDefaultAndAccessReturningValue_returnsValue() {

        assertEquals(EXPECTED, defaultedUnless(EXPECTED, UNEXPECTED, identity()));
    }
}
