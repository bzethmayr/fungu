package net.zethmayr.fungu.core;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static net.zethmayr.fungu.core.ValueFactory.values;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValueFactoryTest {

    @Test
    void valueFactory_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(ValueFactory.class));
    }

    @Test
    void values_givenNoSuppliers_returnsEmptyArray() {

        final Object[] result = values();

        assertEquals(0, result.length);
    }

    @Test
    void values_givenOneSupplier_returnsOneValue() {

        final Object[] result = values(EXPECTED::length);

        assertEquals(1, result.length);
        assertEquals(8, result[0]);
    }

    @Test
    void values_givenTwoSuppliers_returnsTwoValues() {
        final AtomicInteger counter = new AtomicInteger();

        final Object[] result = values(counter::incrementAndGet, counter::incrementAndGet);

        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(2, result[1]);
    }
}
