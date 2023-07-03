package net.zethmayr.fungu.flock;

import net.zethmayr.fungu.core.SupplierFactory;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static net.zethmayr.fungu.flock.RetryableNopException.becauseRetryNop;
import static net.zethmayr.fungu.flock.RetryableNopException.retryNopBecause;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RetryableNopExceptionTest {

    @Test
    void becauseRetryNop_givenMessage_returnsException() {

        final RetryableNopException underTest = becauseRetryNop(EXPECTED);

        assertEquals(EXPECTED, underTest.getMessage());
    }

    @Test
    void retryNopBecause_givenMessage_suppliesDistinctExceptions() {

        final Supplier<RetryableNopException> underTest = retryNopBecause(EXPECTED);
        final RetryableNopException result = underTest.get();

        assertEquals(EXPECTED, result.getMessage());
        assertNotSame(result, underTest.get());
    }
}