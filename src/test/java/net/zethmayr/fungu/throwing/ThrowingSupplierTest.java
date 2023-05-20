package net.zethmayr.fungu.throwing;

import net.zethmayr.fungu.test.ExampleCheckedException;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static net.zethmayr.fungu.throwing.Sinkable.sinking;
import static org.junit.jupiter.api.Assertions.*;

public class ThrowingSupplierTest {

    private ThrowingSupplier<String, ExampleCheckedException> underTest;

    @Test
    void sinking_givenSink_whenGetReturnsValue_raiseDoesNotThrow() {
        underTest = EXPECTED::toString;
        final Sink<ExampleCheckedException> sink = sink();

        final String result = sinking(underTest, sink).get();

        assertDoesNotThrow(sink::raise);
        assertEquals(EXPECTED, result);
    }

    @Test
    void sinking_givenSink_whenGetThrows_raiseThrows() {
        underTest = () -> {
            throw new ExampleCheckedException(EXPECTED);
        };
        final Sink<ExampleCheckedException> sink = sink();

        final String result = sinking(underTest, sink).get();

        assertThrows(ExampleCheckedException.class, sink::raise);
        assertNull(result);
    }
}
