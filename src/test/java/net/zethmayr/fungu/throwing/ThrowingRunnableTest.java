package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowingRunnableTest implements TestsSinkable {

    final ThrowingRunnable<ExampleCheckedException> succeeds = () -> {
    };
    final ThrowingRunnable<ExampleCheckedException> fails = () -> {
        throw new ExampleCheckedException(EXPECTED);
    };

    @Test
    @Override
    public void sinking_givenSink_givenTarget_whenNoException_raiseDoesNotThrow() {
        final Sink<ExampleCheckedException> sink = sink();

        final Runnable sunk = succeeds.sinking(sink);
        sunk.run();

        assertDoesNotThrow(sink::raise);
    }

    @Test
    @Override
    public void sinking_givenSink_givenTarget_whenException_raiseThrows() {
        final Sink<ExampleCheckedException> sink = sink();

        final Runnable sunk = fails.sinking(sink);
        sunk.run();

        assertThrows(ExampleCheckedException.class, sink::raise);
    }
}
