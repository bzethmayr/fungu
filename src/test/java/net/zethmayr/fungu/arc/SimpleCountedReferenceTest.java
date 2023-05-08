package net.zethmayr.fungu.arc;

import net.zethmayr.fungu.core.ExceptionFactory;
import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.io.IOException;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseImpossible;
import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;
import static org.junit.jupiter.api.Assertions.*;

class SimpleCountedReferenceTest implements TestsCountedReference<TestResource, SimpleCountedReference<TestResource>> {

    private static class TestSimpleCountedReference extends SimpleCountedReference<TestResource> {

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
                throw becauseImpossible("Already opened");
            }
            alreadyCreated = true;
            return new TestResource();
        }
    }

    @Override
    public TestSimpleCountedReference newReference() {
        return new TestSimpleCountedReference();
    }

    @Test
    @Override
    public void countedReference_whenOpenedAndClosed_closesResourceOnce() throws IOException {
        /*
         * Disclaimer - it is generally inadvisable to
         * leak the resource reference from within a try-with-resources.
         */
        TestResource resource;

        try (final TestSimpleCountedReference handle = newReference()) {
            resource = handle.getResource();
            assertFalse(resource.isClosed());
        }

        assertTrue(resource.isClosed());
    }


    @Test
    @Override
    public void countedReference_whenOpenedTwiceAndClosedTwice_closesOneResourceOnce() throws IOException {
        /*
         * Disclaimer - it is generally inadvisable to
         * leak the resource reference from within a try-with-resources.
         */
        TestResource resource;

        try (final TestSimpleCountedReference outer = newReference()) {
            resource = outer.getResource();
            final int outerValue = resource.getValue();
            try (final TestSimpleCountedReference inner = newReference()) {

                final TestResource innerResource = inner.getResource();
                assertSame(resource, innerResource);
                assertEquals(outerValue, innerResource.getValue());
            }
            assertFalse(resource.isClosed());
        }
        assertTrue(resource.isClosed());
    }

    @Test
    public void countedReference_whenResourceIsClosed_closeIsDuplicate() {

        assertThrows(IllegalStateException.class, () -> {
            try (final TestSimpleCountedReference underTest = new TestSimpleCountedReference()) {
                final TestResource resource = underTest.getResource();
                assertFalse(resource.isClosed());
                resource.close();
                assertTrue(resource.isClosed());

            } // throws due to duplicate close, at end of block
        });
    }
}
