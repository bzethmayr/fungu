package net.zethmayr.fungu.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.core.ExceptionFactory.*;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionFactoryTest {

    @Test
    void exceptionFactory_whenInstantiated_throws() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(ExceptionFactory.class));
    }

    @Test
    void becauseNotInstantiable_givenNothing_returnsExpectedException() {

        final UnsupportedOperationException result = becauseNotInstantiable();

        Assertions.assertEquals(NOT_INSTANTIABLE, result.getMessage());
        assertNull(result.getCause());
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseStaticsOnly_givenNothing_returnsExpectedException() {

        final UnsupportedOperationException result = becauseStaticsOnly();

        Assertions.assertEquals(STATICS_ONLY, result.getMessage());
        assertNull(result.getCause());
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseConstantsOnly_givenNothing_returnsExpectedException() {

        final UnsupportedOperationException result = becauseConstantsOnly();

        Assertions.assertEquals(CONSTANTS_ONLY, result.getMessage());
        assertNull(result.getCause());
        assertSame(UnsupportedOperationException.class, result.getClass());
    }

    @Test
    void becauseAdaptersOnly_givenNothing_returnsExpectedException() {

        final UnsupportedOperationException result = becauseAdaptersOnly();

        Assertions.assertEquals(ADAPTERS_ONLY, result.getMessage());
        assertNull(result.getCause());
        assertSame(UnsupportedOperationException.class, result.getClass());
    }
}
