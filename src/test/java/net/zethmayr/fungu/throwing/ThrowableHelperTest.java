package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static net.zethmayr.fungu.throwing.ThrowableHelper.raiseIfChecked;
import static net.zethmayr.fungu.throwing.ThrowableHelper.raiseOrChecked;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.*;

class ThrowableHelperTest {

    @Test
    void throwableHelper_whenInstantiated_throwsInstead() {

        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(ThrowableHelper.class));
    }

    @Test
    void raiseOrChecked_givenMatching_throwsChecked() {
        final ExampleCheckedException thrown = new ExampleCheckedException(EXPECTED);

        assertSame(thrown, assertThrows(ExampleCheckedException.class, () ->
                raiseOrChecked(ExampleCheckedException.class, thrown)));
    }

    @Test
    void raiseOrChecked_givenUnmatched_throwsSunkenWrapping() {
        final ExampleCheckedException thrown = new ExampleCheckedException(EXPECTED);

        assertThat(assertThrows(SunkenException.class, () ->
                        raiseOrChecked(RuntimeException.class, thrown)),

                has(Throwable::getCause, sameInstance(thrown)));
    }

    @Test
    void raiseIfChecked_givenMatching_throwsChecked() {
        final ExampleCheckedException thrown = new ExampleCheckedException(EXPECTED);

        assertSame(thrown, assertThrows(ExampleCheckedException.class, () ->
                raiseIfChecked(ExampleCheckedException.class, thrown)));
    }

    @Test
    void raiseIfChecked_givenUnmatched_doesNotThrow() {
        final ExampleCheckedException thrown = new ExampleCheckedException(EXPECTED);

        assertDoesNotThrow(() -> raiseIfChecked(RuntimeException.class, thrown));
    }
}