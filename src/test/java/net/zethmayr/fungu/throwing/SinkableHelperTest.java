package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import org.junit.jupiter.api.Test;

import java.util.IntSummaryStatistics;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static net.zethmayr.fungu.test.TestHelper.randomString;
import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static net.zethmayr.fungu.throwing.Sinkable.sinking;
import static net.zethmayr.fungu.throwing.SinkableHelper.sinkable;
import static org.junit.jupiter.api.Assertions.*;

class SinkableHelperTest {

    @Test
    void sinkableHelper_whenInstantiated_throwsInstead() {

        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(SinkableHelper.class));
    }

    static String biFunctionExample(final String first, final String second) throws ExampleCheckedException {
        final String result = first + second;
        if (result.contains("throw")) {
            throw new ExampleCheckedException(result);
        }
        return result;
    }

    @Test
    void sinkable_givenImplicitlySinkableBiFunction_returnsExplicitlySinkableBiFunction() {
        final Sink<ExampleCheckedException> sink = sink();

        final BiFunction<String, String, String> sunk = sinking(sinkable(SinkableHelperTest::biFunctionExample), sink);
        final String result = sunk.apply("heath", "row");

        assertNull(result);
        assertEquals("heathrow", assertThrows(ExampleCheckedException.class, sink::raise).getMessage());
    }

    static String functionExample(final String argument) throws ExampleCheckedException {
        if (argument.contains("throw")) {
            throw new ExampleCheckedException(argument);
        }
        final IntSummaryStatistics stats = argument.codePoints().summaryStatistics();
        return randomString(argument.length(), stats.getMin(), stats.getMax());
    }

    @Test
    void sinkable_givenImplicitlySinkableFunction_returnsExplicitlySinkableFunction() {
        final Sink<ExampleCheckedException> sink = sink();

        final Function<String, String> sunk = sinking(sinkable(SinkableHelperTest::functionExample), sink);
        final String result = sunk.apply(EXPECTED);

        assertNotEquals(EXPECTED, result);
        assertDoesNotThrow(sink::raise);
    }
}