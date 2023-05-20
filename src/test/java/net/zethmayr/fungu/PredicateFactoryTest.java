package net.zethmayr.fungu;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Predicate;

import static net.zethmayr.fungu.PredicateFactory.*;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.*;

class PredicateFactoryTest {

    @Test
    void predicateFactory_whenInstantiated_throws() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(PredicateFactory.class));
    }

    @Test
    void allOf_givenNone_testReturnsTrue() {
        final Predicate<String> underTest = allOf();

        assertTrue(underTest.test(EXPECTED));
    }

    @Test
    void allOf_givenFailing_testReturnsFalse() {
        final Predicate<String> underTest = allOf(NEVER);

        assertFalse(underTest.test(EXPECTED));
    }

    @Test
    void allOf_givenPassing_testReturnsTrue() {
        final Predicate<String> underTest = allOf(Objects::nonNull);

        assertTrue(underTest.test(EXPECTED));
    }

    @Test
    void allOf_givenFailingAndPassing_testReturnsFalse() {
        final Predicate<String> underTest = allOf(NEVER, ALWAYS);

        assertFalse(underTest.test(EXPECTED));
    }

    @Test
    void allOf_givenPassingAndFailing_testReturnsFalse() {
        final Predicate<String> underTest = allOf(ALWAYS, NEVER);

        assertFalse(underTest.test(EXPECTED));
    }

    @Test
    void allOf_givenPassingAndPassing_testReturnsTrue() {
        final Predicate<String> underTest = allOf(ALWAYS, Objects::nonNull);

        assertTrue(underTest.test(EXPECTED));
    }

    @Test
    void anyOf_givenNone_testReturnsFalse() {
        final Predicate<String> underTest = anyOf();

        assertFalse(underTest.test(EXPECTED));
    }

    @Test
    void anyOf_givenFailing_testReturnsFalse() {
        final Predicate<String> underTest = anyOf(NEVER);

        assertFalse(underTest.test(EXPECTED));
    }

    @Test
    void anyOf_givenPassing_testReturnsTrue() {
        final Predicate<String> underTest = anyOf(Objects::nonNull);

        assertTrue(underTest.test(EXPECTED));
    }

    @Test
    void anyOf_givenFailingAndPassing_testReturnsTrue() {
        final Predicate<String> underTest = anyOf(Objects::isNull, ALWAYS);

        assertTrue(underTest.test(EXPECTED));
    }

    @Test
    void noneOf_givenNone_testReturnsTrue() {
        final Predicate<String> underTest = noneOf();

        assertTrue(underTest.test(EXPECTED));
    }

    @Test
    void noneOf_givenFailing_testReturnsTrue() {
        final Predicate<String> underTest = noneOf(NEVER);

        assertTrue(underTest.test(EXPECTED));
    }

    @Test
    void noneOf_givenPassing_testReturnsFalse() {
        final Predicate<String> underTest = noneOf(ALWAYS);

        assertFalse(underTest.test(EXPECTED));
    }

    @Test
    void noneOf_givenFailingAndFailing_testReturnsTrue() {
        final Predicate<String> underTest = noneOf(Objects::isNull, NEVER);

        assertTrue(underTest.test(EXPECTED));
    }

    @Test
    void noneOf_givenFailingAndPassing_testReturnsFalse() {
        final Predicate<String> underTest = noneOf(NEVER, ALWAYS);

        assertFalse(underTest.test(EXPECTED));
    }
}
