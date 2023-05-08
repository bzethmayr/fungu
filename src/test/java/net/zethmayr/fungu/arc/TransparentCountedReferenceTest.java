package net.zethmayr.fungu.arc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static net.zethmayr.fungu.arc.TransparentCountedReference.openTransparent;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseUnsupported;
import static org.junit.jupiter.api.Assertions.*;

public class TransparentCountedReferenceTest implements TestsCountedReference<Testable, TransparentCountedReference<Testable>> {

    private static class TestTransparentReference extends TransparentCountedReference<Testable> {

        private static final ThreadLocal<CountAndRef<Testable>> COUNT_AND_REF = newLocalCount();

        public TestTransparentReference() {
            super(Testable.class);
        }

        @Override
        protected ThreadLocal<CountAndRef<Testable>> countAndRef() {
            return COUNT_AND_REF;
        }

        @Override
        protected TestResource createResource() {
            return new TestResource();
        }
    }

    @Override
    public TestTransparentReference newReference() {
        throw becauseUnsupported("this would not be used");
    }

    @Test
    @Override
    public void countedReference_whenOpenedAndClosed_closesResourceOnce() throws IOException {
        Testable resource;

        try (final Testable used = openTransparent(TestTransparentReference::new)) {
            resource = used;
            assertFalse(resource.isClosed());
        }

        assertTrue(resource.isClosed());
    }

    @Test
    @Override
    public void countedReference_whenOpenedTwiceAndClosedTwice_closesOneResourceOnce() throws IOException {
        Testable resource;

        try (final Testable used = openTransparent(TestTransparentReference::new)) {
            resource = used;
            final int outerValue = resource.getValue();
            try (final Testable inner = openTransparent(TestTransparentReference::new)) {

                // the proxy instances are NOT equal, but wrap the same resource
                assertEquals(outerValue, inner.getValue());
            }
            assertFalse(resource.isClosed());
        }
        assertTrue(resource.isClosed());
    }
}