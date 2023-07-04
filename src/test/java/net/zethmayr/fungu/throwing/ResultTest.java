package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import net.zethmayr.fungu.test.TestRuntimeException;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.throwing.ResultFactory.failure;
import static net.zethmayr.fungu.throwing.ResultFactory.success;
import static org.junit.jupiter.api.Assertions.*;

class ResultTest implements TestsRevenant {

    private Result<Void, ExampleCheckedException> failExample() {
        return failure(new ExampleCheckedException(EXPECTED));
    }

    @Test
    @Override
    public void raise_givenExpected_throwsExpected() {
        final Result<Void, ExampleCheckedException> underTest = failExample();

        assertThrows(ExampleCheckedException.class,
                underTest::raise);
    }

    @Test
    @Override
    public void raise_givenNone_throwsNone() {
        final Result<Void, ExampleCheckedException> underTest = success(null);

        assertDoesNotThrow(underTest::raise);
    }

    @Test
    @Override
    public void raiseChecked_givenExpected_throwsExpected() {
        final Result<Void, ExampleCheckedException> underTest = failExample();

        assertThrows(ExampleCheckedException.class, () ->
                underTest.raiseChecked(ExampleCheckedException.class));
    }

    @Test
    @Override
    public void raiseChecked_givenUnexpected_throwsWrapped() {
        final Result<Void, ExampleCheckedException> underTest = failExample();
        final ExampleCheckedException cause = underTest.getException();

        final SunkenException thrown = assertThrows(SunkenException.class, () ->
                underTest.raiseChecked(SunkenException.class));

        assertEquals(cause, thrown.getCause());
    }

    @Test
    @Override
    public void raiseChecked_givenNone_throwsNone() {
        final Result<Void, ExampleCheckedException> underTest = success(null);

        assertDoesNotThrow(() ->
                underTest.raiseChecked(ExampleCheckedException.class));
    }

    @Test
    @Override
    public void raiseOr_givenExpected_throwsExpected() {
        final Result<Void, ExampleCheckedException> underTest = failExample();

        assertThrows(ExampleCheckedException.class, () ->
                underTest.raiseOr(ExampleCheckedException.class));
    }

    @Test
    @Override
    public void raiseOr_givenUnexpected_returnsChaining_raiseChecked_givenUnexpected_throwsWrapped() {
        final Result<Void, ExampleCheckedException> underTest = failExample();
        final ExampleCheckedException cause = underTest.getException();

        final SunkenException thrown = assertThrows(SunkenException.class, () ->
                underTest.raiseOr(TestRuntimeException.class).raiseChecked(RuntimeException.class));

        assertEquals(cause, thrown.getCause());
    }

    @Test
    @Override
    public void raiseOr_givenNone_returnsSelf() {
        final Result<Void, ExampleCheckedException> underTest = success(null);

        assertSame(underTest, assertDoesNotThrow(() ->
                underTest.raiseOr(ExampleCheckedException.class)));
    }
}
