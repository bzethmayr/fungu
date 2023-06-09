package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import net.zethmayr.fungu.test.TestRuntimeException;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestConstants.UNEXPECTED;
import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static org.junit.jupiter.api.Assertions.*;

class SinkTest implements TestsRevenant {

    @Test
    @Override
    public void raise_givenExpected_throwsExpected() {
        final Sink<ExampleCheckedException> underTest = sink();
        underTest.accept(new ExampleCheckedException(EXPECTED));

        assertThrows(ExampleCheckedException.class, underTest::raise);
    }

    @Test
    @Override
    public void raise_givenNone_throwsNone() {
        final Sink<Exception> underTest = sink();

        assertDoesNotThrow(underTest::raise);
    }

    @Test
    @Override
    public void raiseChecked_givenExpected_throwsExpected() {
        final Sink<Exception> underTest = sink();

        underTest.accept(new ExampleCheckedException(EXPECTED));

        final ExampleCheckedException thrown = assertThrows(ExampleCheckedException.class, () ->
                underTest.raiseChecked(ExampleCheckedException.class));
        assertEquals(EXPECTED, thrown.getMessage());
    }

    @Test
    @Override
    public void raiseChecked_givenUnexpected_throwsWrapped() {
        final Sink<Exception> underTest = sink();

        underTest.accept(new Exception(UNEXPECTED));

        final SunkenException thrown = assertThrows(SunkenException.class, () ->
                underTest.raiseChecked(ExampleCheckedException.class));
        assertEquals(UNEXPECTED, thrown.getCause().getMessage());
    }

    @Test
    @Override
    public void raiseChecked_givenNone_throwsNone() {
        final Sink<ExampleCheckedException> underTest = sink();

        assertDoesNotThrow(() -> underTest.raiseChecked(ExampleCheckedException.class));
    }

    @Test
    @Override
    public void raiseOr_givenExpected_throwsExpected() {
        final Sink<Exception> underTest = sink();

        underTest.accept(new ExampleCheckedException(EXPECTED));

        assertThrows(ExampleCheckedException.class, () -> underTest.raiseOr(ExampleCheckedException.class));
    }

    @Test
    @Override
    public void raiseOr_givenUnexpected_returnsChaining_raiseChecked_givenUnexpected_throwsWrapped() {
        final Sink<Exception> underTest = sink();

        underTest.accept(new Exception(UNEXPECTED));

        final RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                underTest.raiseOr(ExampleCheckedException.class).raiseChecked(TestRuntimeException.class));
        assertEquals(UNEXPECTED, thrown.getCause().getMessage());
    }

    @Test
    @Override
    public void raiseOr_givenNone_returnsSelf() {
        final Sink<ExampleCheckedException> underTest = sink();

        assertSame(underTest, assertDoesNotThrow(() ->
                underTest.raiseOr(ExampleCheckedException.class)));
    }
}
