package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static net.zethmayr.fungu.PredicateFactory.NEVER;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.MatcherFactory.hasNull;
import static net.zethmayr.fungu.test.TestConstants.*;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static net.zethmayr.fungu.throwing.ResultFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class ResultFactoryTest {

    private static <T, E extends Exception> Matcher<Result<T, E>> hasValueAndNoException(final String value) {
        return allOf(
                has(Result::get, value),
                hasNull(Result::getException),
                has(Result::getResult, value)
        );
    }

    @Test
    void resultFactory_whenInstantiated_throwsInstead() {

        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(ResultFactory.class));
    }

    @Test
    void success_givenValue_hasValueAndNoException() {

        final Result<String, ExampleCheckedException> result = success(EXPECTED);

        assertThat(result, hasValueAndNoException(EXPECTED));
        assertEquals(EXPECTED, assertDoesNotThrow(result::getOrThrow));
    }

    @Test
    void success_givenNull_hasNullAndNoException() {

        final Result<String, ExampleCheckedException> result = success(NULL_STRING);

        assertThat(result, hasValueAndNoException(NULL_STRING));
        assertNull(assertDoesNotThrow(result::getOrThrow));
    }

    private Matcher<Result<?, ?>> hasException(final Class<? extends Exception> expected) {
        return has(Result::getException, instanceOf(expected));
    }

    private ExampleCheckedException exampleException() {
        return new ExampleCheckedException(EXPECTED);
    }

    private Matcher<Result<?, ?>> hasExampleException() {
        return hasException(ExampleCheckedException.class);
    }

    @Test
    void failure_givenException_hasExceptionAndGetThrows() {

        final Result<String, ExampleCheckedException> result = failure(exampleException());

        assertThat(result, hasExampleException());
        assertThrows(SunkenException.class, result::get);
        assertThrows(ExampleCheckedException.class, result::getOrThrow);
    }

    @Test
    void evaluate_givenRunnable_whenNoException_returnsEmptySuccess() {

        final Result<Void, Exception> result = evaluate(() -> {
        });

        assertThat(result, hasValueAndNoException(null));
        assertNull(assertDoesNotThrow(result::getOrThrow));
    }

    @Test
    void evaluate_givenRunnable_whenException_returnsFailure() {

        final Result<Void, Exception> result = evaluate((Runnable) () -> {
            throw new RuntimeException();
        });

        assertThat(result, hasException(RuntimeException.class));
    }

    @Test
    void evaluate_givenSupplier_whenNoException_returnsSuccessWithValue() {

        final Result<String, Exception> result = evaluate(() -> EXPECTED);

        assertThat(result, hasValueAndNoException(EXPECTED));
        assertEquals(EXPECTED, assertDoesNotThrow(result::getOrThrow));
    }

    @Test
    void evaluate_givenSupplier_whenException_returnsFailure() {

        final Result<String, Exception> result = evaluate(() -> Optional.of(EXPECTED)
                .filter(NEVER)
                .orElseThrow(RuntimeException::new)
        );

        assertThat(result, hasException(RuntimeException.class));
    }

    @Test
    void evaluateThrowing_givenRunnable_whenException_returnsFailure() {

        final Result<Void, ExampleCheckedException> result = evaluateThrowing((ThrowingRunnable<ExampleCheckedException>) () -> {
            throw new ExampleCheckedException(EXPECTED);
        });

        assertThat(result, hasExampleException());
        assertThrows(SunkenException.class, result::get);
        assertThrows(ExampleCheckedException.class, result::getOrThrow);
    }

    @Test
    void evaluateThrowing_givenRunnable_whenNoException_returnsEmptySuccess() {

        final Result<Void, Exception> result = evaluateThrowing(() -> {
        });

        assertThat(result, hasValueAndNoException(null));
        assertNull(assertDoesNotThrow(result::getOrThrow));
    }

    @Test
    void evaluateThrowing_givenSupplier_whenException_returnsFailure() {

        final Result<String, ExampleCheckedException> result = evaluateThrowing(() -> Optional.of(UNEXPECTED)
                .filter(NEVER)
                .orElseThrow(this::exampleException));

        assertThat(result, hasExampleException());
        assertThrows(SunkenException.class, result::get);
        assertThrows(ExampleCheckedException.class, result::getOrThrow);
    }

    @Test
    void evaluateThrowing_givenSupplier_whenNoException_returnsSuccess() {

        final Result<String, Exception> result = evaluateThrowing(() -> EXPECTED);

        assertThat(result, hasValueAndNoException(EXPECTED));
        assertEquals(EXPECTED, result.get());
        assertEquals(EXPECTED, assertDoesNotThrow(result::getOrThrow));
    }
}
