package net.zethmayr.fungu.throwing;

import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static net.zethmayr.fungu.throwing.SinkFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class SinkFactoryTest {

    @Test
    void sinkFactory_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(SinkFactory.class));
    }

    @Test
    void sink_givenNothing_returnsSink_whenAcceptThenRaise_throwsAccepted() {
        final Exception thrown = new Exception();
        final Sink underTest = sink();

        assertInstanceOf(Sink.class, underTest);
        underTest.accept(thrown);
        final Exception caught = assertThrows(Exception.class, underTest::raise);

        assertSame(thrown, caught);
    }

    @Test
    void sink_givenNothing_returnsSink_whenAcceptTwiceBeforeRaise_throwsFirstSuppressingSecond() {
        final Exception firstThrown = new Exception();
        final Exception secondThrown = new Exception();
        final Sink underTest = sink();

        assertInstanceOf(Sink.class, underTest);
        underTest.accept(firstThrown);
        underTest.accept(secondThrown);
        final Exception caught = assertThrows(Exception.class, underTest::raise);

        assertSame(firstThrown, caught);
        assertSame(secondThrown, caught.getSuppressed()[0]);
    }

    @Test
    void hole_givenNothing_returnsSink_whenAcceptThenRaise_doesNothing() throws Exception {
        final Exception thrown = new Exception();
        final Sink underTest = hole();

        assertInstanceOf(Sink.class, underTest);
        underTest.accept(thrown);
        underTest.raise();
    }

    @Test
    void mine_givenNothing_returnsSink_whenAccept_throwsWrapped() {
        final Exception thrown = new Exception();
        final Sink underTest = mine();

        assertInstanceOf(Sink.class, underTest);
        final RuntimeException caught = assertThrows(RuntimeException.class, () ->
                underTest.accept(thrown));

        assertEquals(thrown, caught.getCause());
    }

    @Test
    void threadSafeSink_givenNothing_returnsSink_whenAcceptThenRaise_throwsAccepted() {
        final Exception thrown = new Exception();
        final Sink underTest = threadSafeSink();

        assertInstanceOf(Sink.class, underTest);
        underTest.accept(thrown);
        final Exception caught = assertThrows(Exception.class, underTest::raise);

        assertEquals(thrown, caught);
    }
}
