package net.zethmayr.fungu.arc;

import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseImpossible;
import static org.junit.jupiter.api.Assertions.*;

class ProtectedCountedReferenceTest {

    private static class TestCountedReference extends ProtectedCountedReference<Testable> {

        public TestCountedReference() {
            super(Testable.class);
        }

        private boolean alreadyCreated;

        private static final ThreadLocal<CountAndRef<Testable>> COUNT_AND_REF = newLocalCount();

        @Override
        protected ThreadLocal<CountAndRef<Testable>> countAndRef() {
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

    @Test
    void countedReference_whenOpenedAndClosed_closesResourceOnce() throws Exception {
        /*
         * Disclaimer - it is generally inadvisable to
         * leak the resource reference from within a try-with-resources.
         */
        Testable resource;

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
        Testable resource;

        try (final TestCountedReference outer = new TestCountedReference()) {
            resource = outer.getResource();
            final int outerValue = resource.getValue();
            try (final TestCountedReference inner = new TestCountedReference()) {

                final Testable innerResource = inner.getResource();
                assertEquals(outerValue, innerResource.getValue());
            }
            assertFalse(resource.isClosed());
        }
        assertTrue(resource.isClosed());
    }

    @Test
    void countedReference_whenResourceIsClosed_closeIsSuppressedAndClosesAtEnd() throws Exception {
        Testable resource;

        try (final TestCountedReference underTest = new TestCountedReference()) {
            resource = underTest.getResource();
            assertFalse(resource.isClosed());
            resource.close();
            assertFalse(resource.isClosed());

        }
        assertTrue(resource.isClosed());
    }
}
