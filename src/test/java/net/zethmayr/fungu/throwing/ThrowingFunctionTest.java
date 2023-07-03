package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static net.zethmayr.fungu.throwing.Sinkable.sinking;
import static net.zethmayr.fungu.throwing.SinkableHelper.sinkable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWithIgnoringCase;
import static org.junit.jupiter.api.Assertions.*;

class ThrowingFunctionTest implements TestsSinkable {

    @Test
    @Override
    public void sinking_givenSink_givenTarget_whenNoException_raiseDoesNotThrow() {
        final Sink<ExampleCheckedException> sink = sink();

        final Function<String, String> sunk = sinking(sinkable(ThrowingFunctionTest::example), sink);
        final String result = sunk.apply("");

        assertThat(result, startsWithIgnoringCase("example"));
        assertDoesNotThrow(sink::raise);
    }

    @Test
    @Override
    public void sinking_givenSink_givenTarget_whenException_raiseThrows() {
        final Sink<ExampleCheckedException> sink = sink();

        final Function<String, String> sunk = sinking(sinkable(ThrowingFunctionTest::example), sink);
        final String result = sunk.apply(EXPECTED);

        assertNull(result);
        assertThrows(ExampleCheckedException.class, sink::raise);
    }

    private static String example(final String given) throws ExampleCheckedException {
        if (EXPECTED.equals(given)) {
            throw new ExampleCheckedException(EXPECTED);
        }
        return "Example: " + given;
    }
}