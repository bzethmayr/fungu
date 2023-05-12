package net.zethmayr.fungu.core;

import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SuppressionConstantsTest {

    @Test
    void suppressionConstants_whenInstantiated_throws() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(SuppressionConstants.class));
    }

    @Test
    void literalValues_whenInstantiated_throws() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(SuppressionConstants.LiteralValues.class));
    }
}
