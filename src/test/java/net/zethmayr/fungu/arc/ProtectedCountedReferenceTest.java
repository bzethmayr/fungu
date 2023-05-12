package net.zethmayr.fungu.arc;

import net.zethmayr.fungu.TestResource;
import net.zethmayr.fungu.Testable;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseImpossible;
import static org.junit.jupiter.api.Assertions.*;

class ProtectedCountedReferenceTest implements TestsCountedReference<Testable, ProtectedCountedReference<Testable>> {

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

    @Override
    public TestCountedReference newReference() {
        return new TestCountedReference();
    }

    @Test
    @Override
    public void countedReference_whenOpenedAndClosed_closesResourceOnce() throws IOException {
        /*
         * Disclaimer - it is generally inadvisable to
         * leak the resource reference from within a try-with-resources.
         */
        Testable resource;

        try (final TestCountedReference handle = newReference()) {
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
        Testable resource;

        try (final TestCountedReference outer = newReference()) {
            resource = outer.getResource();
            final int outerValue = resource.getValue();
            try (final TestCountedReference inner = newReference()) {

                final Testable innerResource = inner.getResource();
                assertEquals(outerValue, innerResource.getValue());
            }
            assertFalse(resource.isClosed());
        }
        assertTrue(resource.isClosed());
    }

    @Test
    public void countedReference_whenResourceIsClosed_closeIsSuppressedAndClosesAtEnd() throws IOException {
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
