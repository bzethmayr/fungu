package net.zethmayr.fungu;

import net.zethmayr.fungu.throwing.ThrowingConsumer;
import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.io.IOException;

import static net.zethmayr.fungu.CloseableFactory.closeIntercepted;
import static net.zethmayr.fungu.CloseableFactory.closeable;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseImpossible;
import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.*;

public class CloseableFactoryTest {

    @Test
    void closeableFactory_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(CloseableFactory.class));
    }

    @Test
    void closeable_givenNotCloseableAndDisposer_returnsCloseableCallingDisposer() throws Exception {
        class Forgettable {
            private boolean forgotten;

            public void forget() {
                forgotten = true;
            }

            public boolean isForgotten() {
                return forgotten;
            }
        }
        final Forgettable notCloseable = new Forgettable();

        final Closeable underTest = closeable(notCloseable, Forgettable::forget);
        underTest.close();

        assertTrue(notCloseable.isForgotten());
    }

    @Test
    void closeIntercepted_givenCloseableAndInterfaces_returnsInterceptedWithInterfaces() throws Exception {
        final ThrowingConsumer<TestResource, IOException> interceptor = r -> {
        };

        final TestResource realResource = new TestResource();
        final Testable underTest = closeIntercepted(Testable.class, realResource, interceptor);
        final int realValue = realResource.getValue();
        assertEquals(realValue, underTest.getValue());
        underTest.close();
        assertFalse(underTest.isClosed());
        realResource.close();
        assertTrue(underTest.isClosed());
    }
}
