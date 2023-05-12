package net.zethmayr.fungu;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Function;

import static net.zethmayr.fungu.DecisionHelper.*;
import static net.zethmayr.fungu.core.SupplierFactory.from;
import static net.zethmayr.fungu.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class DecisionHelperTest {

    @Test
    void anyNull_whenNone_returnsFalse() {

        assertFalse(anyNull());
    }

    @Test
    void anyNull_whenNull_returnsTrue() {

        assertTrue(anyNull(NULL_STRING));
    }

    @Test
    void anyNull_whenNonNull_returnsFalse() {

        assertFalse(anyNull(EXPECTED));
    }

    @Test
    void anyNull_whenNonNullAndNull_returnsTrue() {

        assertTrue(anyNull(EXPECTED, NULL_STRING));
    }

    @Test
    void anyNull_whenNullAndNonNull_returnsTrue() {

        assertTrue(anyNull(NULL_STRING, EXPECTED));
    }

    @Test
    void anyNull_whenTwoNonNull_returnsFalse() {

        assertFalse(anyNull(EXPECTED, SHIBBOLETH));
    }

    @Test
    void anyNull_whenTwoNull_returnsTrue() {

        assertTrue(anyNull(NULL_OBJECT, NULL_STRING));
    }

    @Test
    void maybeGet_givenTestAndSupplier_whenTestFails_returnsNull() {

        final Function<Object, String> underTest = maybeGet(Objects::nonNull, from(UNEXPECTED));

        assertNull(underTest.apply(NULL_OBJECT));
    }

    @Test
    void maybeGet_givenTestAndSupplier_whenTestPasses_returnsSuppliedValue() {

        final Function<Object, String> underTest = maybeGet(Objects::nonNull, from(EXPECTED));

        assertEquals(EXPECTED, underTest.apply(UNEXPECTED));
    }

    @Test
    void maybeApply_givenTestAndMapper_whenTestFails_returnsNull() {

        final Function<String, Integer> underTest = maybeApply(EXPECTED::equals, String::length);

        assertNull(underTest.apply(UNEXPECTED));
    }

    @Test
    void maybeApply_givenTestAndMapper_whenTestPasses_returnsResult() {

        final Function<String, Integer> underTest = maybeApply(EXPECTED::equals, String::length);

        assertEquals(8, underTest.apply(EXPECTED));
    }

    @Test
    void maybeResult_givenTestAndResult_whenTestFails_returnsNull() {

        final Function<String, Object> underTest = maybeResult(UNEXPECTED::equals, NOT_EVEN_WRONG);

        assertNull(underTest.apply(EXPECTED));
    }

    @Test
    void maybeResult_givenTestAndResult_whenTestPasses_returnsResult() {

        final Function<Object, String> underTest = maybeResult(SHIBBOLETH::equals, EXPECTED);

        assertEquals(EXPECTED, underTest.apply(SHIBBOLETH));
    }
}
