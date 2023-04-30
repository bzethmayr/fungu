package net.zethmayr.fungu;

import org.junit.jupiter.api.Test;

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
        final Predicate<String> underTest = allOf(ALWAYS);

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
        final Predicate<String> underTest = allOf(ALWAYS, ALWAYS);

        assertTrue(underTest.test(EXPECTED));
    }
}
