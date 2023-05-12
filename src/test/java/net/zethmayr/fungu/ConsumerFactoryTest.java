package net.zethmayr.fungu;

import net.zethmayr.fungu.test.Irrelevant;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.zethmayr.fungu.ConsumerFactory.*;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.MatcherFactory.hasNull;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestConstants.SHIBBOLETH;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ConsumerFactoryTest {

    @Test
    void consumerFactory_whenInstantiated_throws() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(ConsumerFactory.class));
    }

    @Test
    void nothing_givenValue_doesNothing() {

        final Consumer<String> underTest = nowhere();
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

    @Test
    void prefixingFrom_givenPrefacedAndPrefix_callsPrefacedWithPrefixAndArgument() {
        final Map<String, Irrelevant> exampleStructure = new HashMap<>();
        final Supplier<String> prefixSource = EXPECTED::toString;

        final Consumer<Irrelevant> underTest = prefixingFrom(exampleStructure::put, prefixSource);
        underTest.accept(new Irrelevant(SHIBBOLETH));

        assertThat(exampleStructure.get(EXPECTED), has(Irrelevant::getField, SHIBBOLETH));
    }

    @Test
    void suffixingFrom_givenSuffixedAndSuffix_callsSuffixedWithArgumentAndSuffix() {
        final Map<String, Irrelevant> exampleStructure = new HashMap<>();
        final Supplier<Irrelevant> suffixSource = Irrelevant::new;

        final Consumer<String> underTest = suffixingFrom(exampleStructure::put, suffixSource);
        underTest.accept(EXPECTED);

        assertThat(exampleStructure.get(EXPECTED), hasNull(Irrelevant::getField));
    }
}
