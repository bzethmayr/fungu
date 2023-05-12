package net.zethmayr.fungu.core;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.*;
import static net.zethmayr.fungu.core.SupplierFactory.from;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.MatcherFactory.hasNull;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestConstants.SHIBBOLETH;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionFactoryTest {

    @Test
    void exceptionFactory_whenInstantiated_throws() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(ExceptionFactory.class));
    }

    private Matcher<Exception> hasMessageAndNoCause(final String message) {
        return allOf(
                has(Exception::getMessage, message),
                hasNull(Exception::getCause)
        );
    }

    private Matcher<Exception> hasMessageContaining(final String... parts) {
        return has(Exception::getMessage, stringContainsInOrder(parts));
    }

    @Test
    void becauseNotInstantiable_givenNothing_returnsExpectedException() {

        final UnsupportedOperationException result = becauseNotInstantiable();

        assertThat(result, hasMessageAndNoCause(NOT_INSTANTIABLE));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseStaticsOnly_givenNothing_returnsExpectedException() {

        final UnsupportedOperationException result = becauseStaticsOnly();

        assertThat(result, hasMessageAndNoCause(STATICS_ONLY));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseConstantsOnly_givenNothing_returnsExpectedException() {

        final UnsupportedOperationException result = becauseConstantsOnly();

        assertThat(result, hasMessageAndNoCause(CONSTANTS_ONLY));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseAdaptersOnly_givenNothing_returnsExpectedException() {

        final UnsupportedOperationException result = becauseAdaptersOnly();

        assertThat(result, hasMessageAndNoCause(ADAPTERS_ONLY));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void unsupportedBecause_givenMessage_returnsSupplier_returnsExpectedException() {

        final Supplier<UnsupportedOperationException> underTest = unsupportedBecause(EXPECTED);
        final UnsupportedOperationException result = underTest.get();

        assertThat(result, hasMessageAndNoCause(EXPECTED));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseNotUnique_givenNonUnique_returnsExpectedException() {

        final IllegalArgumentException result = becauseNotUnique(EXPECTED);

        assertThat(result, hasMessageContaining(EXPECTED));
        assertNull(result.getCause());
        assertSame(IllegalArgumentException.class, result.getClass());
    }

    @Test
    void notUniqueBecause_givenSource_returnsSupplier_returnsExpectedException() {

        final Supplier<IllegalArgumentException> underTest = notUniqueBecause(from(EXPECTED));
        final IllegalArgumentException result = underTest.get();

        assertThat(result, hasMessageContaining(EXPECTED));
        assertNull(result.getCause());
        assertSame(IllegalArgumentException.class, result.getClass());
    }

    @Test
    void becauseConflicting_givenScopeAndConflictingValues_returnsExpectedException() {

        final IllegalArgumentException result = becauseConflicting(SHIBBOLETH, EXPECTED, SHIBBOLETH);

        assertThat(result, hasMessageContaining(SHIBBOLETH, EXPECTED, SHIBBOLETH));
        assertNull(result.getCause());
        assertSame(IllegalArgumentException.class, result.getClass());
    }

    @Test
    void conflictingBecause_givenSources_returnsSupplier_returnsExpectedException() {

        final Supplier<IllegalArgumentException> underTest = conflictingBecause(EXPECTED::toString, EXPECTED::toString, SHIBBOLETH::toString);
        final IllegalArgumentException result = underTest.get();

        assertThat(result, hasMessageContaining(EXPECTED, EXPECTED, SHIBBOLETH));
        assertNull(result.getCause());
        assertSame(IllegalArgumentException.class, result.getClass());
    }
}
