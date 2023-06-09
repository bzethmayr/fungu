package net.zethmayr.fungu.throwing;

import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowingConsumerTest implements TestsSinkable {

    final ThrowingConsumer<Closeable, IOException> example = Closeable::close;

    @Test
    @Override
    public void sinking_givenSink_givenTarget_whenNoException_raiseDoesNotThrow() {
        final Sink<IOException> sink = sink();
        final Closeable closes = () -> {
        };

        final Consumer<Closeable> sunk = example.sinking(sink);
        sunk.accept(closes);

        assertDoesNotThrow(sink::raise);
    }

    @Test
    @Override
    public void sinking_givenSink_givenTarget_whenException_raiseThrows() {
        final Sink<IOException> sink = sink();
        final Closeable breaks = () -> {
            throw new IOException();
        };

        final Consumer<Closeable> sunk = example.sinking(sink);
        sunk.accept(breaks);

        assertThrows(IOException.class, sink::raise);
    }
}
