package net.zethmayr.fungu.arc;

import java.io.Closeable;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseImpossible;
import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;

/**
 * Models some general aspects of resources for test purposes.
 */
class TestResource implements Testable, Closeable {

    /**
     * A resource reference might refer to a closed and therefore invalid resource.
     */
    private boolean isClosed;

    /**
     * Each resource obtained refers to unique state.
     */
    private final int value = TEST_RANDOM.nextInt();

    @Override
    public void close() {
        /*
         * Real implementations often ignore duplicate close,
         * but this increases test stricture.
         */
        if (isClosed) {
            throw becauseImpossible("Already closed");
        }
        isClosed = true;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public int getValue() {
        return value;
    }
}
