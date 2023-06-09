package net.zethmayr.fungu.core;

import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContractConstantsTest {

    @Test
    void contractConstants_whenInstantiated_throwsInstead() {

        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(ContractConstants.class));
    }

}