package net.zethmayr.fungu.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static net.zethmayr.fungu.core.SupplierFactory.from;
import static net.zethmayr.fungu.core.SupplierFactory.like;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.*;

public class SupplierFactoryTest {

    @Test
    void supplierFactory_whenInstantiated_throws() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(SupplierFactory.class));
    }

    @Test
    void from_givenValue_returnsSupplierOfValue() {

        final Supplier<String> result = from(EXPECTED);

        Assertions.assertEquals(EXPECTED, result.get());
    }

    @Test
    void from_givenNothing_returnsSupplierOfNothing() {

        final Supplier<String> result = from(null);

        assertNull(result.get());
    }

    @Test
    void from_givenInstanceAndGetter_returnsSupplierOfField() {

        final Supplier<Integer> result = from(EXPECTED, String::length);

        assertEquals(8, result.get());
    }

    @Test
    void like_givenGenericSupplier_returnsObjectSupplier() {

        final Supplier<Object> result = like(EXPECTED::length);

        assertEquals(8, result.get());
    }
}
