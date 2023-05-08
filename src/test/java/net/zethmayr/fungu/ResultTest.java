package net.zethmayr.fungu;

import net.zethmayr.fungu.test.ExampleCheckedException;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.ResultFactory.*;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.MatcherFactory.hasNull;
import static net.zethmayr.fungu.test.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    private static <T, E extends Exception> Matcher<Result<T, E>> hasValueAndNoException(final String value) {
        return allOf(
                has(Result::get, value),
                hasNull(Result::getException),
                has(Result::getResult, value)
        );
    }

    private static <T, E extends Exception> Matcher<Result<T, E>> hasExceptionAndNoValue() {
        return allOf(
                hasNull(Result::get),
                has(Result::getException, instanceOf(ExampleCheckedException.class)),
                has(Result::getResult, instanceOf(ExampleCheckedException.class))
        );
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

    @Test
    void failure_givenException_hasExceptionAndNoValue() {

        final Result<String, ExampleCheckedException> result = failure(new ExampleCheckedException(EXPECTED));

        assertThat(result, hasExceptionAndNoValue());
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
    void evaluate_givenSupplier_whenNoException_returnsSuccessWithValue() {

        final Result<String, Exception> result = evaluate(() -> EXPECTED);

        assertThat(result, hasValueAndNoException(EXPECTED));
        assertEquals(EXPECTED, assertDoesNotThrow(result::getOrThrow));
    }

    @Test
    void evaluateThrowing_givenRunnable_whenException_returnsFailure() {

        final Result<String, ExampleCheckedException> result = evaluateThrowing(() -> {
            throw new ExampleCheckedException(EXPECTED);
        });

        assertThat(result, hasExceptionAndNoValue());
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

        final Result<String, ExampleCheckedException> result = evaluateThrowing(() -> {
            if (true) {
                throw new ExampleCheckedException(EXPECTED);
            }
            return UNEXPECTED;
        });

        assertThat(result, hasExceptionAndNoValue());
        assertThrows(ExampleCheckedException.class, result::getOrThrow);
    }

    @Test
    void evaluateThrowing_givenSupplier_whenNoException_returnsSuccess() {

        final Result<String, Exception> result = evaluateThrowing(() -> EXPECTED);

        assertThat(result, hasValueAndNoException(EXPECTED));
        assertEquals(EXPECTED, assertDoesNotThrow(result::getOrThrow));
    }
}