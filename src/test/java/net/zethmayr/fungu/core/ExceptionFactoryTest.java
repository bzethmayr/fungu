package net.zethmayr.fungu.core;

import net.zethmayr.fungu.test.ExampleCheckedException;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.*;
import static net.zethmayr.fungu.core.SupplierFactory.from;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.MatcherFactory.hasNull;
import static net.zethmayr.fungu.test.TestConstants.*;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.stringContainsInOrder;
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

    private Matcher<Throwable> hasMessageContaining(final String... parts) {
        return has(Throwable::getMessage, stringContainsInOrder(parts));
    }

    @Test
    void becauseNotInstantiable_givenNothing_returnsUnsupportedOperation() {

        final UnsupportedOperationException result = becauseNotInstantiable();

        assertThat(result, hasMessageAndNoCause(NOT_INSTANTIABLE));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseStaticsOnly_givenNothing_returnsUnsupportedOperation() {

        final UnsupportedOperationException result = becauseStaticsOnly();

        assertThat(result, hasMessageAndNoCause(STATICS_ONLY));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseConstantsOnly_givenNothing_returnsUnsupportedOperation() {

        final UnsupportedOperationException result = becauseConstantsOnly();

        assertThat(result, hasMessageAndNoCause(CONSTANTS_ONLY));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseAdaptersOnly_givenNothing_returnsUnsupportedOperation() {

        final UnsupportedOperationException result = becauseAdaptersOnly();

        assertThat(result, hasMessageAndNoCause(ADAPTERS_ONLY));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void unsupportedBecause_givenMessage_returnsSupplier_returnsUnsupportedOperation() {

        final Supplier<UnsupportedOperationException> underTest = unsupportedBecause(EXPECTED);
        final UnsupportedOperationException result = underTest.get();

        assertThat(result, hasMessageAndNoCause(EXPECTED));
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseThrewBecauseIllegal_givenMessageAndCauseThenArguments_returnsIllegalArgument() {

        final IllegalArgumentException result = becauseThrewBecauseIllegal(
                "%s%s", new ExampleCheckedException(UNEXPECTED), EXPECTED, SHIBBOLETH);

        assertThat(result, hasMessageContaining(EXPECTED, SHIBBOLETH));
        assertThat(result.getCause(), hasMessageContaining(UNEXPECTED));
    }


    @Test
    void becauseNonexistent_givenSingleFormatAndKey_returnsNoSuchElement() {

        final NoSuchElementException result = becauseNonexistent("No such box %s", EXPECTED);

        assertThat(result, hasMessageContaining("No such box", EXPECTED));
        assertSame(NoSuchElementException.class, result.getClass());
    }

    @Test
    void becauseNonExistent_givenDoubleFormatAndKey_returnsNoSuchElement() {

        final NoSuchElementException result = becauseNonexistent("No drawer %s in cabinet %s", SHIBBOLETH, EXPECTED);

        assertThat(result, hasMessageContaining("No drawer", SHIBBOLETH, "in", EXPECTED));
        assertSame(NoSuchElementException.class, result.getClass());
    }

    @Test
    void becauseNotUnique_givenNonUnique_returnsIllegalArgument() {

        final IllegalArgumentException result = becauseNotUnique(EXPECTED);

        assertThat(result, hasMessageContaining(EXPECTED));
        assertNull(result.getCause());
        assertSame(IllegalArgumentException.class, result.getClass());
    }

    @Test
    void notUniqueBecause_givenSource_returnsSupplier_returnsIllegalArgument() {

        final Supplier<IllegalArgumentException> underTest = notUniqueBecause(from(EXPECTED));
        final IllegalArgumentException result = underTest.get();

        assertThat(result, hasMessageContaining(EXPECTED));
        assertNull(result.getCause());
        assertSame(IllegalArgumentException.class, result.getClass());
    }

    @Test
    void becauseConflicting_givenScopeAndConflictingValues_returnsIllegalArgument() {

        final IllegalArgumentException result = becauseConflicting(SHIBBOLETH, EXPECTED, SHIBBOLETH);

        assertThat(result, hasMessageContaining(SHIBBOLETH, EXPECTED, SHIBBOLETH));
        assertNull(result.getCause());
        assertSame(IllegalArgumentException.class, result.getClass());
    }

    @Test
    void conflictingBecause_givenSources_returnsSupplier_returnsIllegalArgument() {

        final Supplier<IllegalArgumentException> underTest = conflictingBecause(EXPECTED::toString, EXPECTED::toString, SHIBBOLETH::toString);
        final IllegalArgumentException result = underTest.get();

        assertThat(result, hasMessageContaining(EXPECTED, EXPECTED, SHIBBOLETH));
        assertNull(result.getCause());
        assertSame(IllegalArgumentException.class, result.getClass());
    }
}
