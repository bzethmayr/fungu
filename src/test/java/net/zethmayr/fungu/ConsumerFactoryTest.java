package net.zethmayr.fungu;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.zethmayr.fungu.ConsumerFactory.*;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.*;

class ConsumerFactoryTest {

    @Test
    void consumerFactory_whenInstantiated_throws() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(ConsumerFactory.class));
    }

    @Test
    void nothing_givenValue_doesNothing() {

        final Consumer<String> underTest = nothing();
        underTest.accept(EXPECTED);

        assertNotNull(underTest);
    }

    @Test
    void consumer_givenConsuming_returnsConsumer() {
        final StringBuilder exampleInstance = new StringBuilder();


        final Consumer<String> restrictedConsumer = consumer(exampleInstance::append);
        restrictedConsumer.accept(EXPECTED);

        assertEquals(EXPECTED, exampleInstance.toString());
    }

    @Test
    void ignoringResult_givenFunction_returnsConsumer() {
        final StringBuilder exampleInstance = new StringBuilder();
        final Function<CharSequence, StringBuilder> returning = exampleInstance::append;

        final Consumer<String> appending = ignoringResult(returning);
        appending.accept(EXPECTED);

        assertEquals(EXPECTED, exampleInstance.toString());
    }

    @Test
    void redirectingResult_givenFunctionAndConsumer_returnsConsumer() {
        final AtomicInteger exampleInstance = new AtomicInteger();
        final StringBuilder observingInstance = new StringBuilder();

        final Consumer<Integer> accumulating = redirectingResult(exampleInstance::addAndGet,
                ignoringResult(observingInstance::append));
        accumulating.accept(42);

        assertEquals(42, exampleInstance.get());
        assertEquals("42", observingInstance.toString());
    }
}
