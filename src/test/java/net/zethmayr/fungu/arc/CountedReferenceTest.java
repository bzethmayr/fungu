package net.zethmayr.fungu.arc;

import net.zethmayr.fungu.core.ExceptionFactory;
import org.junit.jupiter.api.Test;

import java.io.Closeable;

import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;
import static org.junit.jupiter.api.Assertions.*;

class CountedReferenceTest {

    /**
     * Models some general aspects of resources for test purposes.
     */
    private static class TestResource implements Closeable {

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
                throw ExceptionFactory.becauseImpossible("Already closed");
            }
            isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }

        public int getValue() {
            return value;
        }
    }

    private static class TestCountedReference extends CountedReference<TestResource> {

        private boolean alreadyCreated;

        private static final ThreadLocal<CountAndRef<TestResource>> COUNT_AND_REF = newLocalCount();

        @Override
        protected ThreadLocal<CountAndRef<TestResource>> countAndRef() {
            return COUNT_AND_REF;
        }

        @Override
        protected TestResource createResource() {
            /*
             * This restriction is not unreasonable -
             * a counted reference is already intended to avoid doing this more than once.
             */
            if (alreadyCreated) {
                throw ExceptionFactory.becauseImpossible("Already opened");
            }
            alreadyCreated = true;
            return new TestResource();
        }
    }

    @Test
    void countedReference_whenOpenedAndClosed_closesResourceOnce() throws Exception {
        /*
         * Disclaimer - it is generally inadvisable to
         * leak the resource reference from within a try-with-resources.
         */
        TestResource resource;

        try (final TestCountedReference handle = new TestCountedReference()) {
            resource = handle.getResource();
            assertFalse(resource.isClosed());
        }

        assertTrue(resource.isClosed());
    }

    @Test
    void countedReference_whenOpenedTwiceAndClosedTwice_closesOneResourceOnce() throws Exception {
        /*
         * Disclaimer - it is generally inadvisable to
         * leak the resource reference from within a try-with-resources.
         */
        TestResource resource;

        try (final TestCountedReference outer = new TestCountedReference()) {
            resource = outer.getResource();
            final int outerValue = resource.getValue();
            try (final TestCountedReference inner = new TestCountedReference()) {

                final TestResource innerResource = inner.getResource();
                assertSame(resource, innerResource);
                assertEquals(outerValue, innerResource.getValue());
            }
            assertFalse(resource.isClosed());
        }
        assertTrue(resource.isClosed());
    }

    @Test
    void countedReference_whenResourceIsClosed_closeIsDuplicate() {

        assertThrows(IllegalStateException.class, () -> {
            try (final TestCountedReference underTest = new TestCountedReference()) {
                final TestResource resource = underTest.getResource();
                assertFalse(resource.isClosed());
                resource.close();
                assertTrue(resource.isClosed());

            } // throws due to duplicate close, at end of block
        });
    }
}
