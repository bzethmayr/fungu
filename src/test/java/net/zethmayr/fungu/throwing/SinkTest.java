package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import net.zethmayr.fungu.test.TestRuntimeException;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestConstants.UNEXPECTED;
import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SinkTest {

    @Test
    void raise_givenExpected_throwsExpected() {
        final Sink<ExampleCheckedException> underTest = sink();
        underTest.accept(new ExampleCheckedException(EXPECTED));

        assertThrows(ExampleCheckedException.class, underTest::raise);
    }

    @Test
    void raiseChecked_givenExpected_throwsExpected() {
        final Sink<Exception> underTest = sink();

        underTest.accept(new ExampleCheckedException(EXPECTED));

        final ExampleCheckedException thrown = assertThrows(ExampleCheckedException.class, () ->
                underTest.raiseChecked(ExampleCheckedException.class));
        assertEquals(EXPECTED, thrown.getMessage());
    }

    @Test
    void raiseChecked_givenUnexpected_throwsWrapped() {
        final Sink<Exception> underTest = sink();

        underTest.accept(new Exception(UNEXPECTED));

        final RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                underTest.raiseChecked(ExampleCheckedException.class));
        assertEquals(UNEXPECTED, thrown.getCause().getMessage());
    }

    @Test
    void raiseOr_givenExpected_throwsExpected() {
        final Sink<Exception> underTest = sink();

        underTest.accept(new ExampleCheckedException(EXPECTED));

        assertThrows(ExampleCheckedException.class, () -> underTest.raiseOr(ExampleCheckedException.class));
    }

    @Test
    void raiseOr_givenUnexpected_returnsChaining_raiseChecked_givenUnexpected_throwsWrapped() {
        final Sink<Exception> underTest = sink();

        underTest.accept(new Exception(UNEXPECTED));

        final RuntimeException thrown = assertThrows(RuntimeException.class, () ->
        underTest.raiseOr(ExampleCheckedException.class).raiseChecked(TestRuntimeException.class));
        assertEquals(UNEXPECTED, thrown.getCause().getMessage());
    }
}
