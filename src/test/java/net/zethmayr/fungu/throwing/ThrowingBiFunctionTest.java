package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static net.zethmayr.fungu.throwing.Sinkable.sinking;
import static net.zethmayr.fungu.throwing.SinkableHelper.sinkable;
import static org.junit.jupiter.api.Assertions.*;

class ThrowingBiFunctionTest implements TestsSinkable {

    @Test
    @Override
    public void sinking_givenSink_givenTarget_whenNoException_raiseDoesNotThrow() {
        final Sink<ExampleCheckedException> sink = sink();

        final BiFunction<String, String, String> sunk = sinking(sinkable(ThrowingBiFunctionTest::example), sink);
        final String result = sunk.apply("zyx", "ABC");

        assertEquals("XVT", result);
        assertDoesNotThrow(sink::raise);
    }

    @Test
    @Override
    public void sinking_givenSink_givenTarget_whenException_raiseThrows() {
        final Sink<ExampleCheckedException> sink = sink();

        final BiFunction<String, String, String> sunk = sinking(sinkable(ThrowingBiFunctionTest::example), sink);
        final String result = sunk.apply("abra", "cadabra");

        assertThrows(ExampleCheckedException.class, sink::raise);
    }

    private static String example(final String first, final String second) throws ExampleCheckedException {
        if (first.length() != second.length()) {
            throw new ExampleCheckedException("length mismatch");
        }
        final StringBuilder buffer = new StringBuilder(first.length());
        IntStream.range(0, first.length())
                .mapMulti((x, c) -> {
                    final int candidate = first.codePointAt(x);
                    final int comparison = second.codePointAt(x);
                    if (candidate == comparison) {
                        c.accept(candidate);
                    } else {
                        c.accept(31 + abs(candidate - comparison));
                    }
                })
                .forEach(buffer::appendCodePoint);
        return buffer.toString();
    }
}